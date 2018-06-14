package com.xkd.service;

import com.xkd.mapper.UserPayMapper;
import com.xkd.utils.DateUtils;
import com.xkd.utils.PropertiesUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserPayService {

    @Autowired
    private UserPayMapper userPayMapper;

    public Map<String,String> getUserPayByOpenId(String openId) {
        return userPayMapper.getUserPayByOpenId(openId);
    }

    public void saveUserPay(Map<String, String> userInfo) {
        userPayMapper.saveUserPay(userInfo);
    }

    public void saveUserPayLogger(Map<String, String> csOrder) {
        userPayMapper.saveUserPayLogger(csOrder);
    }

    public void editUserPay(Map<String, String> csOrder) {
        userPayMapper.editUserPay(csOrder);
    }

    public Map<String,String> getUserPayAdviser(String mobile) {
        return userPayMapper.getUserPayAdviser(mobile);
    }

    public List<Map<String,String>> getAllAdviser() {
        return userPayMapper.getAllAdviser();
    }

    public List<Map<String,String>> getPayUserList(String adviserName) {
        return userPayMapper.getPayUserList(adviserName);
    }

    //导出excel
    public String writePayUserListWord(List<Map<String, String>> list) {

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("春节特惠票务数据");
        XSSFCellStyle style_title = wb.createCellStyle();
        Font fontHeader=wb.createFont();
        fontHeader.setFontHeightInPoints((short)12);
        fontHeader.setBold(true);
        //字体名称
        fontHeader.setFontName("宋体");
        style_title.setFont(fontHeader);
        style_title.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style_title.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        XSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style_title.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);


        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("index","序号");
        map.put("payTime","支付时间");
        map.put("uname","姓名");
        map.put("mobile","联系方式");
        map.put("course","课程");
        map.put("adviserName","顾问");

        XSSFRow row = sheet.createRow((int) 0);
        row.setHeightInPoints(30);
        XSSFCell cell = null;
        int cell_0 = 0;
        for (String key : map.keySet()) {
            cell = row.createCell(cell_0);
            cell.setCellValue(map.get(key));
            cell.setCellStyle(style_title);
            cell_0 ++;
        }
        if(null != list && list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                row = sheet.createRow(i+1);
                cell_0 = 0;

                for (String key : map.keySet()) {
                    cell = row.createCell(cell_0);
                    if(key.equals("index")){
                        cell.setCellValue((i+1));
                    }else{
                        Object value = list.get(i).get(key);
                        if(null == value){
                            cell.setCellValue("--");
                        }else{
                            cell.setCellValue(value+"");
                        }
                    }
                    cell.setCellStyle(style);
                    cell_0 ++;
                }
            }
        }
        String time = DateUtils.currtimeToString14();
        String path = PropertiesUtil.FILE_UPLOAD_PATH+"/userPay/"+time+".xlsx";
        FileOutputStream outputStream = null;
        try {
            // //给文件夹设置读写修改操作
            File dir = new File(PropertiesUtil.FILE_UPLOAD_PATH+"userPay");
            String os = System.getProperty("os.name");
            if (!dir.exists()) {
                dir.mkdirs();  //如果文件不存在  在目录中创建文件夹   这里要注意mkdir()和mkdirs()的区别
                if(!os.toLowerCase().startsWith("win")){
                    Runtime.getRuntime().exec("chmod 777 " + dir.getPath());
                }
            }

            outputStream = new FileOutputStream(path);
            wb.write(outputStream);
            wb.close();
            outputStream.close();

            //给文件设置读写修改操作
            File targetFile = new File(path);

            if(targetFile.exists()){

                targetFile.setExecutable(true);//设置可执行权限
                targetFile.setReadable(true);//设置可读权限
                targetFile.setWritable(true);//设置可写权限
                String saveFilename = targetFile.getPath();
                if(!os.toLowerCase().startsWith("win")){

                    Runtime.getRuntime().exec("chmod 777 " + saveFilename);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return PropertiesUtil.FILE_HTTP_PATH+"userPay/"+time+".xlsx";
    }
}
