package com.xkd.controller;

import com.sun.javafx.geom.AreaOp;
import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.ContractService;
import com.xkd.service.UserDataPermissionService;
import com.xkd.service.UserService;
import com.xkd.utils.DateUtils;
import com.xkd.utils.FileUtil;
import com.xkd.utils.PropertiesUtil;
import com.xkd.utils.ZipUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by dell on 2018/4/27.
 */

@Api(description = "合同相关接口")
@Controller
@RequestMapping("/contract")
@Transactional
public class ContractController extends BaseController {

    @Autowired
    ContractService contractService;

    @Autowired
    UserService userService;

    @Autowired
    UserDataPermissionService userDataPermissionService;

    @ApiOperation(value = "检索合同")
    @ResponseBody
    @RequestMapping(value = "/searchContract", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter searchContract(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "检索值", required = false) @RequestParam(required = false) String searchValue,
                                           @ApiParam(value = "部门Id", required = false) @RequestParam(required = false) String departmentId,
                                           @ApiParam(value = "公司Id", required = false) @RequestParam(required = false) String companyId,
                                           @ApiParam(value = "签约时间起 时间格式 2012-10-11", required = false) @RequestParam(required = false) String startDate,
                                           @ApiParam(value = "签约时间止 时间格式 2012-10-11", required = false) @RequestParam(required = false) String endDate,
                                           @ApiParam(value = "当前第几页", required = true) @RequestParam(required = true) Integer currentPage,
                                           @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");
        List<Map<String, Object>> list = new ArrayList<>();
        Integer count = 0;
        List<String> departmentIdList = new ArrayList<>();
        try {
            departmentIdList = userDataPermissionService.getDataPermissionDepartmentIdList(departmentId, loginUserId);
            list = contractService.searchContract(companyId, departmentIdList, searchValue, startDate, endDate, loginUserId, currentPage, pageSize);
            count = contractService.searchContractCount(companyId, departmentIdList, searchValue, startDate, endDate, loginUserId);
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(list);
        responseDbCenter.setTotalRows(count + "");
        return responseDbCenter;
    }


    @ApiOperation(value = "公司下的的合同列表")
    @ResponseBody
    @RequestMapping(value = "/searchContractByCompanyId", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter searchContract(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "公司Id", required = false) @RequestParam(required = false) String companyId,
                                           @ApiParam(value = "当前第几页", required = true) @RequestParam(required = true) Integer currentPage,
                                           @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        Integer count = 0;
        List<String> departmentIdList = new ArrayList<>();
        try {
            list = contractService.searchContract(companyId, departmentIdList, null, null, null, null, currentPage, pageSize);
            count = contractService.searchContractCount(companyId, departmentIdList, null, null, null, null);
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);

        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(list);
        responseDbCenter.setTotalRows(count + "");
        return responseDbCenter;
    }


    @ApiOperation(value = "添加合同")
    @ResponseBody
    @RequestMapping(value = "/addContract", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter addContract(HttpServletRequest req, HttpServletResponse rsp,
                                        @ApiParam(value = "合同名称", required = true) @RequestParam(required = true) String contractName,
                                        @ApiParam(value = "客户Id", required = true) @RequestParam(required = true) String companyId,
                                        @ApiParam(value = "合同编号", required = true) @RequestParam(required = true) String contractNo,
                                        @ApiParam(value = "合同金额", required = true) @RequestParam(required = true) Double amount,
                                        @ApiParam(value = "签约时间  时间格式 2012-10-11", required = true) @RequestParam(required = true) String signDate,
                                        @ApiParam(value = "urls 多个值用逗号分隔", required = false) @RequestParam(required = false) String urls
    ) throws Exception {
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);

        List<String> urlList = new ArrayList<>();
        if (StringUtils.isNotBlank(urls)) {
            String[] urlArray = urls.split(",");
            urlList = Arrays.asList(urlArray);
        }

        try {
            Map<String, Object> existInDb = contractService.selectByContractNo(contractNo, (String) loginUserMap.get("pcCompanyId"));
            if (existInDb != null) {
                return ResponseConstants.CONTRACT_NO_EXISTS;
            }
            Map<String, Object> map = new HashMap<>();
            String id = UUID.randomUUID().toString();
            map.put("id", id);
            map.put("createdBy", loginUserId);
            map.put("createDate", new Date());
            map.put("updatedBy", loginUserId);
            map.put("updateDate", new Date());
            map.put("contractName", contractName);
            map.put("contractNo", contractNo);
            map.put("amount", amount);
            map.put("signDate", signDate);
            map.put("pcCompanyId", loginUserMap.get("pcCompanyId"));
            map.put("companyId", companyId);
            map.put("urls", urlList);
            map.put("departmentId", loginUserMap.get("departmentId"));
            contractService.insertContract(map, loginUserId);
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);

        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }


    @ApiOperation(value = "更新合同")
    @ResponseBody
    @RequestMapping(value = "/updateContract", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter updateContract(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "Id", required = true) @RequestParam(required = true) String id,
                                           @ApiParam(value = "合同名称", required = false) @RequestParam(required = false) String contractName,
                                           @ApiParam(value = "客户Id", required = false) @RequestParam(required = false) String companyId,
                                           @ApiParam(value = "合同编号", required = false) @RequestParam(required = false) String contractNo,
                                           @ApiParam(value = "合同金额", required = false) @RequestParam(required = false) Double amount,
                                           @ApiParam(value = "签约时间  时间格式 2012-10-11", required = false) @RequestParam(required = false) String signDate,
                                           @ApiParam(value = "urls 多个值用逗号分隔", required = false) @RequestParam(required = false) String urls
    ) throws Exception {
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);

        List<String> urlList = new ArrayList<>();
        if (StringUtils.isNotBlank(urls)) {
            String[] urlArray = urls.split(",");
            urlList = Arrays.asList(urlArray);
        }

        try {
            Map<String, Object> existInDb = contractService.selectByContractNo(contractNo, (String) loginUserMap.get("pcCompanyId"));
            if (existInDb != null) {
                if (!id.equals(existInDb.get("id"))) {
                    return ResponseConstants.CONTRACT_NO_EXISTS;
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("updatedBy", loginUserId);
            map.put("updateDate", new Date());
            map.put("contractName", contractName);
            map.put("contractNo", contractNo);
            map.put("amount", amount);
            map.put("companyId", companyId);
            map.put("urls", urlList);
            map.put("signDate", signDate);
            contractService.updateContract(map, loginUserId);
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }


    @ApiOperation(value = "删除合同")
    @ResponseBody
    @RequestMapping(value = "/deleteContract", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter deleteContract(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "Ids", required = true) @RequestParam(required = true) String ids
    ) throws Exception {
        try {
            String[] idArray = ids.split(",");
            List<String> idList = Arrays.asList(idArray);
            contractService.deleteContractByIds(idList);
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);

        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }


    @ApiOperation(value = "查询合同详情")
    @ResponseBody
    @RequestMapping(value = "/contractDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter contractDetail(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "id 合同Id", required = true) @RequestParam(required = true) String id
    ) throws Exception {
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        try {
            Map<String, Object> map = contractService.contactDetail(id);
            responseDbCenter.setResModel(map);
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);

        }
        return responseDbCenter;
    }


    @ResponseBody
    @RequestMapping(value = "/uploadContract", method = {RequestMethod.POST})
    @ApiOperation(value = "上传合同附件")
    public ResponseDbCenter uploadContract(@RequestParam(value = "file", required = false) MultipartFile file,
                                           HttpServletRequest req) throws Exception {

        if (file == null) {

            return ResponseConstants.MISSING_PARAMTER;
        }
        MultipartFile[] files=new MultipartFile[1];
        files[0]=file;


        String uploadPath = PropertiesUtil.FILE_UPLOAD_PATH + "contracts";
        String httpPath = PropertiesUtil.FILE_HTTP_PATH + "contracts";

        List<String> fileList = FileUtil.fileUpload(files, uploadPath, httpPath);

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(fileList);

        return responseDbCenter;
    }


    @ResponseBody
    @RequestMapping(value = "/downloadContract", method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value = "打包下载合同")
    public ResponseDbCenter downloadContract(HttpServletResponse resp, @RequestParam(value = "id", required = false) String id,
                                             HttpServletRequest req) throws Exception {


         ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        try {

            List<Map<String, Object>> list = contractService.selectContractAttachement(id);

            Map<String,Object> contractMap=contractService.contactDetail(id);

            String uploadPath = PropertiesUtil.FILE_UPLOAD_PATH + "contracts";
            String httpPath = PropertiesUtil.FILE_HTTP_PATH + "contracts";
            ZipOutputStream zos = null;
            try {
                zos = new ZipOutputStream(resp.getOutputStream());
                for (int i = 0; i < list.size(); i++) {
                    String url = (String) list.get(i).get("url");
                    if (StringUtils.isNotBlank(url)) {
                        url = url.replace(httpPath, "");
                        String[] strs = url.split("___");
                        String fileName = strs[0];
                        if (strs.length > 0) {
                            fileName = strs[1];
                        }
                        url = uploadPath + url;
                        File file = new File(url);
                        if (file.exists()) {
                            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                            ZipEntry zipEntry = new ZipEntry(fileName);
                            zos.putNextEntry(zipEntry);
                            byte[] buffer = new byte[1024];
                            int read = 0;
                            while ((read = bis.read(buffer)) != -1) {
                                zos.write(buffer, 0, read);
                            }
                            zos.closeEntry();
                        }
                    }
                }

                resp.setHeader("Content-Disposition", "attachment;filename= " + java.net.URLEncoder.encode(contractMap.get("contractName")+"合同附件", "UTF-8") + ".zip");
                //设置导出文件的格式
                resp.setContentType("application/zip");

                zos.flush();
                zos.finish();
                zos.close();
                resp.getOutputStream().flush();
                resp.getOutputStream().close();
            } catch (Exception e) {
                log.error("异常栈:", e);
            } finally {
                if (zos != null) {
                    zos.close();
                }
            }

        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        return responseDbCenter;
    }


}
