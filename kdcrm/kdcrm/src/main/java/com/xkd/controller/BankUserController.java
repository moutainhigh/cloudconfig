package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.BankUserService;
import com.xkd.utils.DateUtils;
import com.xkd.utils.ExcelUtilSpecial;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.io.FileInputStream;
import java.text.DateFormat;
import java.util.*;

/**
 * Created by dell on 2017/12/11.
 */

@Api(description = "银行网点人员维护")
@Transactional
@Controller
@RequestMapping("/bankUser")
public class BankUserController extends BaseController  {

    @Autowired
    private BankUserService bankUserService;



    @ApiOperation(value = "添加或修改网点人员")
    @ResponseBody
    @RequestMapping(value = "/changeUserInfo",method = {RequestMethod.POST})
    public ResponseDbCenter changeUserInfo(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "插入标志  insert 插入 update更新",required = true)@RequestParam(required = true) String flag,
                                           @ApiParam(value = "姓名",required = true)@RequestParam(required = true)  String uname,
                                           @ApiParam(value = "岗位",required = false)@RequestParam(required = false)     String station,
                                           @ApiParam(value = "电话",required = false)@RequestParam(required = false)  String phone,
                                           @ApiParam(value = "网点Id" ,required = true)@RequestParam (required = true)  String pointId,
                                           @ApiParam(value = "邮箱",required = false)@RequestParam(required = false)  String email,
                                           @ApiParam(value = "网点用户关系Id",required = false)@RequestParam(required = false)   String id,
                                           @ApiParam(value = "性别 男  女",required = false)@RequestParam(required = false)  String sex,
                                           @ApiParam(value = "描述",required = false)@RequestParam(required = false)  String desc,
                                           @ApiParam(value = "手机",required = false)@RequestParam(required = false)  String mobile,
                                           @ApiParam(value = "年龄",required = false)@RequestParam(required = false)  String age,
                                           @ApiParam(value = "学历",required = false)@RequestParam(required = false)  String degree,
                                           @ApiParam(value = "工作年限",required = false)@RequestParam(required = false)  String workAge



    ) throws Exception {

        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");

        if (StringUtils.isBlank(flag) || StringUtils.isBlank(uname)
             ) {

            throw new GlobalException(ResponseConstants.MISSING_PARAMTER);
        }




        bankUserService.insertBankUser(flag, uname, station, phone, pointId, email, id, sex, desc, mobile, age, workAge, degree, loginUserId);

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }



    @ApiOperation(value = "查询银行人员列表")
    @ResponseBody
    @RequestMapping(value = "selectBankUserList",method = RequestMethod.POST)
    public  ResponseDbCenter selectBankUserList(
            @ApiParam(value = "网点Id",required = true) @RequestParam(required = true)  String pointId,
            @ApiParam(value = "人员姓名",required = false) @RequestParam(required = false)  String uname,
            @ApiParam(value = "性别  男  女",required = false) @RequestParam(required = false) String sex,
            @ApiParam(value = "当前页",required = true) @RequestParam(required = true) Integer currentPage,
            @ApiParam(value = "每页数量",required = true) @RequestParam(required = true) Integer pageSize ){
        try {
            List<Map<String,Object>> list=bankUserService.selectBankUserList(pointId,uname,sex,currentPage,pageSize);
            Integer count=bankUserService.selectBankUserListCount(pointId,uname,sex);
            ResponseDbCenter responseDbCenter=new ResponseDbCenter();
            responseDbCenter.setResModel(list);
            responseDbCenter.setTotalRows(count+"");
            return  responseDbCenter;

        }catch (Exception e){
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;

        }
    }



    @ApiOperation(value = "根据条件导出银行人员")
    @ResponseBody
    @RequestMapping(value = "exportBankUserList",method = RequestMethod.GET)
    public  void exportBankUserList( HttpServletResponse resp,
            @ApiParam(value = "网点Id",required = true) @RequestParam(required = true)  String pointId,
            @ApiParam(value = "人员姓名",required = false) @RequestParam(required = false)  String uname,
            @ApiParam(value = "性别  男  女",required = false) @RequestParam(required = false) String sex ){
        try {
            List<Map<String,Object>> list=bankUserService.selectBankUserList(pointId,uname,sex,1,10000);
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

            columnTitles[5]="工作年限";
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



    @ApiOperation(value = "删除网点人员")
    @ResponseBody
    @RequestMapping(value = "deleteBankUserInfo",method = RequestMethod.POST)
    public  ResponseDbCenter deleteBankUserInfo(
            @ApiParam(value = "网点人员Id",required = true) @RequestParam(required = true)  String ids ){
        try {

            List<String> list=new ArrayList<>();
            if (!StringUtils.isBlank(ids)){
                String[] idArra=ids.split(",");
                for (int i = 0; i < idArra.length ; i++) {
                    list.add(idArra[i]);
                }
            }
            if(list.size()>0){
                bankUserService.deleteBankUserInfo(list);
            }

            ResponseDbCenter responseDbCenter=new ResponseDbCenter();
             return  responseDbCenter;

        }catch (Exception e){
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }






    @ApiOperation(value = "通过Excel导入银行人员")
    @ResponseBody
    @RequestMapping(value = "importBankUserInfo",method = RequestMethod.POST)
    public  ResponseDbCenter importBankUserInfo(HttpServletRequest req, HttpServletResponse rsp,
                                                @ApiParam(value = "网点Id",required = true) @RequestParam(required = true)  String pointId,
          @RequestParam  MultipartFile  file){
        String loginUserId = (String) req.getAttribute("loginUserId");
        try {
            XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream());
            XSSFSheet hssfSheet=wb.getSheetAt(0);
            List<Map<String,Object>>   list=new ArrayList<>();
            Integer totalRows=hssfSheet.getLastRowNum();
            List<Map> mapList=new ArrayList<>();
            boolean flag=true;
            StringBuffer note=new StringBuffer();
            for (int i = 3; i <=totalRows ; i++) {
                XSSFRow hssfRow= hssfSheet.getRow(i);
                String station=hssfRow.getCell(0)==null?null:hssfRow.getCell(0).getStringCellValue();
                String uname=hssfRow.getCell(1)==null?null:hssfRow.getCell(1).getStringCellValue();
                String age=hssfRow.getCell(2)==null?null:hssfRow.getCell(2).getStringCellValue();
                String sex=hssfRow.getCell(3)==null?null:hssfRow.getCell(3).getStringCellValue();
                String degree=hssfRow.getCell(4)==null?null:hssfRow.getCell(4).getStringCellValue();
                String workAge=hssfRow.getCell(5)==null?null:hssfRow.getCell(5).getStringCellValue();
                String email=hssfRow.getCell(6)==null?null:hssfRow.getCell(6).getStringCellValue();
                String mobile=hssfRow.getCell(7)==null?null:hssfRow.getCell(7).getStringCellValue();

                if (StringUtils.isBlank(station)
                       && StringUtils.isBlank(uname)
                        && StringUtils.isBlank(age)
                        && StringUtils.isBlank(sex)
                        && StringUtils.isBlank(degree)
                        && StringUtils.isBlank(workAge)
                        && StringUtils.isBlank(email)
                        && StringUtils.isBlank(mobile)
                        ){
                    break;
                }


                StringBuffer stringBuffer=new StringBuffer();
                stringBuffer.append("      第"+(i+1)+"列： ");
                boolean thisRowOk=true;
                if (StringUtils.isBlank(uname)){
                    flag=false;
                    thisRowOk=false;
                    stringBuffer.append(" 姓名不能为空  ");
                }

               try {
                   if (StringUtils.isNotBlank(age)){
                     Integer ageI=Integer.valueOf(age);
                   }
               }catch (Exception e){
                   flag=false;
                   thisRowOk=false;
                   stringBuffer.append(" 年龄必须为数字  "  );
               }


                if (StringUtils.isNotBlank(sex)&&!"男".equals(sex)&&!"女".equals(sex)){
                    flag=false;
                    thisRowOk=false;
                    stringBuffer.append(" 性别必须是男或女 ");
                }

                if (StringUtils.isNotBlank(email)){
                    if (!email.contains("@")){
                        flag=false;
                        thisRowOk=false;
                        stringBuffer.append("邮箱格式不正确 ");
                    }
                }



                if (thisRowOk==false){
                    note.append(stringBuffer);
                 }


                Map<String,Object> map=new HashMap<>();
                map.put("station",station);
                map.put("uname",uname);
                map.put("age",age);
                map.put("sex",sex);
                map.put("degree",degree);
                map.put("workAge",workAge);
                map.put("email",email);
                map.put("mobile",mobile);
                mapList.add(map);
            }


            if (flag){
                /**
                 * 验证完之后 插入库中
                 */
                for (int i = 0; i < mapList.size(); i++) {
                    Map<String,Object>  map=mapList.get(i);
                    String station= (String) map.get("station");
                    String uname=(String) map.get("uname");
                    String age=(String) map.get("age");
                    String sex=(String) map.get("sex");
                    String degree=(String) map.get("degree");
                    String workAge=(String) map.get("workAge");
                    String email=(String) map.get("email");
                    String mobile=(String) map.get("mobile");
                    bankUserService.insertBankUser("insert", uname, station, mobile, pointId, email, null, sex, null, mobile, age, workAge, degree,loginUserId);

                }
            }

             ResponseDbCenter responseDbCenter=new ResponseDbCenter();
             if (note.length()>1){
                 responseDbCenter.setRepCode("CRM1003");
                 responseDbCenter.setRepNote(note.toString());
             }else {
                 responseDbCenter=ResponseConstants.SUCCESS;
              }
            return  responseDbCenter;

        }catch (Exception e){
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }



}
