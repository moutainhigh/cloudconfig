package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.BankProjectUserService;
import com.xkd.utils.CompanyInfoApi;
import com.xkd.utils.ExcelUtilSpecial;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/12/15.
 */
@Api(description = "银行网点项目用户相关接口")
@Controller
@RequestMapping("/bankProjectUser")
@Transactional
public class BankProjectUserController extends  BaseController {

    @Autowired
    BankProjectUserService bankProjectUserService;


    @ApiOperation(value = "添加网点，项目 ，人员关系")
    @ResponseBody
    @RequestMapping(value = "/insertBankProjectUserList",method = {RequestMethod.POST})
    public ResponseDbCenter insertBankProjectUserList(HttpServletRequest req, HttpServletResponse rsp,
                                                        @ApiParam(value="网点Id",required = true) @RequestParam(required = false) String pointId,
                                                        @ApiParam(value="银行项目Id",required = true) @RequestParam(required = false) 	String bankProjectId,
                                                        @ApiParam(value="人员Id，多个值以逗号分隔",required = true) @RequestParam(required = false) String userIds )
            throws Exception {

        try {
            List<String > list=new ArrayList<>();
            if (StringUtils.isNotBlank(userIds)){
                String[] uIds=userIds.split(",");
                for (int i = 0; i <uIds.length ; i++) {
                    list.add(uIds[i]);
                }
            }
            if (list.size()>0){
                bankProjectUserService.insertBankProjectUserList(bankProjectId,pointId,list);
            }

        } catch (Exception e) {

            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }



    @ApiOperation(value = "查询在不在某一个银行网点项目下的人员")
    @ResponseBody
    @RequestMapping(value = "/selectUserNotUnderProject",method = {RequestMethod.POST})
    public ResponseDbCenter selectUserNotUnderProject(HttpServletRequest req, HttpServletResponse rsp,
                                                      @ApiParam(value="网点Id",required = true) @RequestParam(required = false) String pointId,
                                                      @ApiParam(value="银行项目Id",required = true) @RequestParam(required = false) 	String bankProjectId,
                                                      @ApiParam(value="人员名称",required = false) @RequestParam(required = false) 	String uname,
                                                      @ApiParam(value="性别",required = false) @RequestParam(required = false) 	String sex,
                                                      @ApiParam(value="当前页",required = true) @RequestParam(required = false) 	Integer currentPage,
                                                      @ApiParam(value="每页多少条",required = true) @RequestParam(required = false) 	Integer pageSize

                                                      )
            throws Exception {

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        try {
         List<Map<String,Object>>  mapList=          bankProjectUserService.selectUserNotUnderProject(bankProjectId, pointId,uname,sex,currentPage,pageSize);
            Integer count=bankProjectUserService.selectUserNotUnderProjectCount(bankProjectId,pointId,uname,sex);
            responseDbCenter.setResModel(mapList);
            responseDbCenter.setTotalRows(count+"");

        } catch (Exception e) {

            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }


        return responseDbCenter;
    }



    @ApiOperation(value = "查询在某一个银行网点项目下的人员")
    @ResponseBody
    @RequestMapping(value = "/selectUserUnderProject",method = {RequestMethod.POST})
    public ResponseDbCenter selectUserUnderProject(HttpServletRequest req, HttpServletResponse rsp,
                                                   @ApiParam(value="网点Id",required = true) @RequestParam(required = false) String pointId,
                                                   @ApiParam(value="银行项目Id",required = true) @RequestParam(required = false) 	String bankProjectId,
                                                   @ApiParam(value="人员名称",required = false) @RequestParam(required = false) 	String uname,
                                                   @ApiParam(value="性别",required = false) @RequestParam(required = false) 	String sex,
                                                   @ApiParam(value="当前页",required = true) @RequestParam(required = false) 	Integer currentPage,
                                                   @ApiParam(value="每页多少条",required = true) @RequestParam(required = false) 	Integer pageSize
    )
            throws Exception {

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        try {
            List<Map<String,Object>>  mapList=          bankProjectUserService.selectUserUnderProject(bankProjectId, pointId,uname,sex,currentPage,pageSize);
            Integer count=bankProjectUserService.selectUserUnderProjectCount(bankProjectId, pointId, uname, sex);
            responseDbCenter.setResModel(mapList);
            responseDbCenter.setTotalRows(count+"");

        } catch (Exception e) {

            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }


        return responseDbCenter;
    }



    @ApiOperation(value = "删除某一个银行网点项目下的人员")
    @ResponseBody
    @RequestMapping(value = "/deleteUserUnderProject",method = {RequestMethod.POST})
    public ResponseDbCenter deleteUserUnderProject(HttpServletRequest req, HttpServletResponse rsp,

                                                   @ApiParam(value="Id列表，多个值 用逗号分隔",required = true) @RequestParam(required = false) 	String ids
    )
            throws Exception {

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();


        try {
            List<String > idList=new ArrayList<>();
            if (StringUtils.isNotBlank(ids)){
                String[] idArra=ids.split(",");
                for (int i = 0; i <idArra.length ; i++) {
                    idList.add(idArra[i]);
                }
            }
            bankProjectUserService.deleteBankProjectUser(idList);
        } catch (Exception e) {

            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }


        return responseDbCenter;
    }



    @ApiOperation(value = "根据条件导出银行项目人员")
    @ResponseBody
    @RequestMapping(value = "exportBankProjectUserList",method = RequestMethod.GET)
    public  void exportBankProjectUserList( HttpServletResponse resp,
                                     @ApiParam(value = "网点Id",required = true) @RequestParam(required = true)  String pointId,
                                     @ApiParam(value = "项目Id",required = true) @RequestParam(required = true)  String bankProjectId,
                                     @ApiParam(value = "人员姓名",required = false) @RequestParam(required = false)  String uname,
                                     @ApiParam(value = "性别  男  女",required = false) @RequestParam(required = false) String sex ){
        try {
            List<Map<String,Object>> list=bankProjectUserService.selectUserUnderProject(bankProjectId, pointId, uname, sex, 1,10000);
            Integer size=8;

            String[] columnTitles=new String[size];
            String[] columnKeys=new String[size];
            columnTitles[0]="岗位";
            columnKeys[0]="station";

            columnTitles[1]="姓名";
            columnKeys[1]="uname";

            columnTitles[2]="年龄";
            columnKeys[2]="age";

            columnTitles[3]="性别";
            columnKeys[3]="sex";

            columnTitles[4]="学历";
            columnKeys[4]="degree";

            columnTitles[5]="工龄";
            columnKeys[5]="workAge";

            columnTitles[6]="邮箱";
            columnKeys[6]="email";

            columnTitles[7]="手机";
            columnKeys[7]="mobile";



            Workbook workbook = ExcelUtilSpecial.createWorkbook("人员信息", columnTitles, columnKeys, list);
            resp.setHeader("Content-Disposition", "attachment;filename= " + java.net.URLEncoder.encode("人员信息", "UTF-8") + ".xls");
            //设置导出文件的格式
            resp.setContentType("application/ms-excel");
            workbook.write(resp.getOutputStream());
            workbook.close();

        }catch (Exception e){
            e.printStackTrace();

        }
    }

}
