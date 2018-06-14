package com.xkd.controller;

import com.xkd.model.Company;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.model.vo.DemoBean;
import com.xkd.service.CompanyRelativeUserService;
import com.xkd.service.CompanyService;
import com.xkd.service.FieldShowService;
import com.xkd.service.SolrService;
import com.xkd.utils.ExcelUtilSpecial;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建人:巫建辉
 * 创建时间：2017-8-18
 * 功能描述：Sorl相关的接口
 */

@Api(description = "搜索企业，下载企业，查询下载状态")
@RestController
public class SolrController extends BaseController {
    @Autowired
    SolrService solrService;
    @Autowired
    CompanyService companyService;

    @Autowired
    FieldShowService fieldShowService;

    ConcurrentHashMap<String, Object> downloadStatusMap = new ConcurrentHashMap<>();


    @Autowired
    CompanyRelativeUserService companyRelativeUserService;

    /**
     * @param req
     * @param queryContent        检索值
     * @param industrys          行业数组
     * @param userLevels           客户等级数组
     * @param companyName         公司名称
     * @param uname               联系人
     * @param province            省
     * @param city                市
     * @param county              县
     * @param mobile              联系人电话
     * @param channel             渠道来源
     * @param companyAdviserName  顾问名称
     * @param companyDirectorName 总监名称
     * @param orderFlag           排序规则    1 修改时间降序   2 修改时间升序  3 客户等级降序  4 客户等级升序   如果不传 结合queryContent 默认按相关度排序
     * @param currentPage         当前页
     * @param pageSize            每页显示多少条
     * @return
     */
    @ApiOperation(value = "查询企业信息")
    @ResponseBody
    @RequestMapping(value = "/solr/company/search", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter search(HttpServletRequest req,
                                   @ApiParam(value = "检索值",required = false) @RequestParam(required = false)  String queryContent,
                                   @ApiParam(value = "行业条件数组",required = false)@RequestParam (required = false)  String[] industrys,
                                   @ApiParam(value = "客户级别数组",required = false)@RequestParam(required = false)   String[] userLevels,
                                   @ApiParam(value = "公司名称",required = false)@RequestParam(required = false)   String companyName,
                                   @ApiParam(value = "联系人",required = false) @RequestParam(required = false)  String uname,
                                   @ApiParam(value = "省",required = false)@RequestParam(required = false)   String province,
                                   @ApiParam(value = "市 ",required = false)@RequestParam(required = false)   String city,
                                   @ApiParam(value = "区",required = false)@RequestParam(required = false)   String county,
                                   @ApiParam(value = "手机",required = false) @RequestParam(required = false)  String mobile,
                                   @ApiParam(value = "来源",required = false)@RequestParam(required = false)   String channel,
                                   @ApiParam(value = "顾问",required = false)@RequestParam(required = false)   String companyAdviserName,
                                   @ApiParam(value = "总监",required = false)@RequestParam(required = false)   String companyDirectorName,
                                   @ApiParam(value = "排序规则    1 修改时间降序   2 修改时间升序  3 客户等级降序  4 客户等级升序   如果不传 结合queryContent 默认按相关度排序",required = false) @RequestParam(required = false)  String orderFlag,
                                   @ApiParam(value = "是否引入数据部门权限  0  否  1  是",required = false) @RequestParam(required = false)  Integer withDataPermission,
                                   @ApiParam(value = "部门Id 如果引入数据权限，此部门Id需要传值",required = false) @RequestParam(required = false)  String departmentId,
                                   @ApiParam(value = "是否是公海客户   1  我负责的客户 2 我参与的客户  3 总监是我的客户  4 公海客户  5 全部客户 ",required = false) @RequestParam(required = false)  Integer publicFlag,
                                   @ApiParam(value = "优先级",required = false) @RequestParam(required = false)  String priority,
                                   @ApiParam(value = "当前页码",required = true) @RequestParam(required = true) Integer currentPage,
                                   @ApiParam(value = "每页多少条数据",required = true) @RequestParam(required = true) Integer pageSize
    ) {
        //登录员工Id
        String loginUserId = (String) req.getAttribute("loginUserId");

        String token = req.getHeader("token");
        List<String> industryList = new ArrayList<>();
        List<String> customerTypeList = new ArrayList();
        List<String> customerDegreeList = new ArrayList();


        if (!StringUtils.isBlank(companyName)) {
            companyName = companyName.replaceAll("\\*", "");
        }

        if (industrys != null && industrys.length > 0) {
            for (int i = 0; i < industrys.length; i++) {
                industryList.add(industrys[i]);
            }
        }


        if (userLevels != null && userLevels.length > 0) {
            for (int i = 0; i < userLevels.length; i++) {
                customerDegreeList.add(userLevels[i]);
            }
        }


        try {
            Map<String, Object> map = solrService.queryCompany(queryContent, industryList, customerTypeList,
                    customerDegreeList, companyName, uname, province, city, county, mobile, channel, companyAdviserName, companyDirectorName, orderFlag,  null, loginUserId, token,withDataPermission,departmentId,publicFlag,priority, currentPage, pageSize);

            ResponseDbCenter responseCenter = ResponseConstants.SUCCESS;
            responseCenter.setTotalRows(map.get("totalRows").toString());
            responseCenter.setResModel(map.get("data"));
            responseCenter.setResExtra(map.get("facetList"));

            return responseCenter;
        } catch (Exception e) {


            log.error("异常栈:",e);
            ResponseDbCenter responseCenter = ResponseConstants.FUNC_MODULE_SERVERERROR;
            responseCenter.setResModel(new ArrayList());
            return responseCenter;
        }

    }


    /**
     * @param req
     * @param queryContent        检索值
     * @param industrys          行业数组
     * @param userLevels           客户等级数组
     * @param companyName         公司名称
     * @param uname               联系人
     * @param province            省
     * @param city                市
     * @param county              县
     * @param mobile              联系人电话
     * @param channel             渠道来源
     * @param companyAdviserName  顾问名称
     * @param companyDirectorName 总监名称
     * @param orderFlag           排序规则    1 修改时间降序   2 修改时间升序  3 客户等级降序  4 客户等级升序   如果不传 结合queryContent 默认按相关度排序
     * @param downloadId          下载Id ，该值由前端传入
     */
    @ApiOperation(value = "下载企业信息")
    @ResponseBody
    @RequestMapping(value = "/solr/company/downLoad", method = {RequestMethod.GET, RequestMethod.POST})
    public void downLoad(HttpServletRequest req, HttpServletResponse resp,
                         @ApiParam(value = "检索值",required = false) @RequestParam(required = false)  String queryContent,
                         @ApiParam(value = "行业条件数组",required = false)@RequestParam (required = false)  String[] industrys,
                         @ApiParam(value = "客户级别数组",required = false)@RequestParam(required = false)   String[] userLevels,
                         @ApiParam(value = "公司名称",required = false)@RequestParam(required = false)   String companyName,
                         @ApiParam(value = "联系人",required = false) @RequestParam(required = false)  String uname,
                         @ApiParam(value = "省",required = false)@RequestParam(required = false)   String province,
                         @ApiParam(value = "市 ",required = false)@RequestParam(required = false)   String city,
                         @ApiParam(value = "区",required = false)@RequestParam(required = false)   String county,
                         @ApiParam(value = "手机",required = false) @RequestParam(required = false)  String mobile,
                         @ApiParam(value = "来源",required = false)@RequestParam(required = false)   String channel,
                         @ApiParam(value = "顾问",required = false)@RequestParam(required = false)   String companyAdviserName,
                         @ApiParam(value = "总监",required = false)@RequestParam(required = false)   String companyDirectorName,
                         @ApiParam(value = "排序规则    1 修改时间降序   2 修改时间升序  3 客户等级降序  4 客户等级升序   如果不传 结合queryContent 默认按相关度排序",required = false) @RequestParam(required = false)  String orderFlag,
                         @ApiParam(value = "是否引入数据部门权限  0  否  1  是",required = false) @RequestParam(required = false)  Integer withDataPermission,
                         @ApiParam(value = "部门Id 如果引入数据权限，此部门Id需要传值",required = false) @RequestParam(required = false)  String departmentId,
                         @ApiParam(value = "是否是公海客户   1  我负责的客户 2 我参与的客户  3 总监是我的客户  4 公海客户  5 全部客户 ",required = false) @RequestParam(required = false)  Integer publicFlag,
                         @ApiParam(value = "优先级",required = false) @RequestParam(required = false)  String priority,
                         @ApiParam(value = "下载Id ，该值由前端传入",required = true) @RequestParam(required = true) String downloadId
    ) {
        if (downloadId != null) {
            downloadStatusMap.put(downloadId, 1);  //0表示正在下载
        }
        String token = req.getHeader("token");

        try {

            //登录员工Id
            String loginUserId = (String) req.getAttribute("loginUserId");


            List<String> industryList = new ArrayList();
            List<String> customerTypeList = new ArrayList();
            List<String> customerDegreeList = new ArrayList();


            if (!StringUtils.isBlank(companyName)) {
                companyName = companyName.replaceAll("\\*", "");
            }

            if (industrys != null && industrys.length > 0) {
                for (int i = 0; i < industrys.length; i++) {
                    industryList.add(industrys[i]);
                }
            }


            if (userLevels != null && userLevels.length > 0) {
                for (int i = 0; i < userLevels.length; i++) {
                    customerDegreeList.add(userLevels[i]);
                }
            }


            Map<String, Object> map = solrService.queryCompany(queryContent, industryList, customerTypeList,
                    customerDegreeList, companyName, uname,  province, city, county, mobile, channel, companyAdviserName, companyDirectorName, orderFlag,  null, loginUserId, token,withDataPermission,departmentId,publicFlag,priority, 1, 1);
            Integer pageSize = 1000;
            Integer totalCount = (Integer) map.get("totalRows");
            Integer batchSize = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;






            List<Map<String,Object>> fieldShowList=fieldShowService.selectFieldShow("company");


            Integer columnSize = fieldShowList.size();
            String[] columnTitles = new String[columnSize];
            String[] columnKeys = new String[columnSize];

            for (int i = 0; i <fieldShowList.size() ; i++) {
                columnTitles[i] = (String) fieldShowList.get(i).get("fieldName");
                columnKeys[i] =  (String) fieldShowList.get(i).get("field");
            }

            List<Map<String, Object>> allData = new ArrayList<>();

            for (int i = 1; i <= batchSize; i++) {
                Map<String, Object> map2 = solrService.queryCompany(queryContent, industryList, customerTypeList,
                        customerDegreeList, companyName, uname,  province, city, county, mobile, channel, companyAdviserName, companyDirectorName, orderFlag,  null, loginUserId, token,withDataPermission,departmentId,publicFlag,priority, i, pageSize);
                List<Map<String, Object>> list = (List<Map<String, Object>>) map2.get("data");
                for (int j = 0; j < list.size(); j++) {
                    Map<String ,Object> company = list.get(j);
                    Map<String, Object> m = new HashMap<>();
                    for (int k = 0; k <columnKeys.length; k++) {
                        m.put(columnKeys[k],company.get(columnKeys[k]));
                    }
                    allData.add(m);
                }
            }


            Workbook workbook = ExcelUtilSpecial.createWorkbook("企业信息", columnTitles, columnKeys, allData);
            resp.setHeader("Content-Disposition", "attachment;filename= " + java.net.URLEncoder.encode("企业信息", "UTF-8") + ".xls");
            //设置导出文件的格式
            resp.setContentType("application/ms-excel");
            workbook.write(resp.getOutputStream());
            workbook.close();


            if (downloadId != null) {
                downloadStatusMap.remove(downloadId);
            }

        } catch (Exception e) {
            log.error("异常栈:",e);
            if (downloadId != null) {
                downloadStatusMap.remove(downloadId);
            }

        }

    }


    /**
     * 查询企业信息下载状态 ，下载成功之后返回成功，否则阻塞
     *
     * @param req
     * @param resp
     * @param downloadId 下载Id。由前端传入
     * @return
     */
    @ApiOperation(value = "查询下载状态")
    @ResponseBody
    @RequestMapping(value = "/solr/company/downLoadStatus", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter downLoad(HttpServletRequest req, HttpServletResponse resp,
                                     @ApiParam(value = "下载Id ，该值由前端传入",required = true) @RequestParam(required = true) String downloadId) {
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        Integer status = (Integer) downloadStatusMap.get(downloadId);
        while (status != null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("异常栈:",e);
            }
            status = (Integer) downloadStatusMap.get(downloadId);
        }
        responseDbCenter.setResModel(status == null ? 0 : 1);
        return responseDbCenter;
    }









}
