package com.kuangchi.sdd.businessConsole.test.util;

import com.google.gson.internal.LinkedTreeMap;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jianhui.wu on 2016/2/5.
 */
public class ExcelUtil {
    public static void export(List<LinkedTreeMap> list,OutputStream out){
        Workbook wb = new HSSFWorkbook();  // or new XSSFWorkbook();
        Sheet sheet = wb.createSheet("sheet1");
            for(int i=0;i<list.size();i++){
                Row row=sheet.createRow(i);
                Set keySet=list.get(i).keySet();
                Iterator<String> iterator=keySet.iterator();
                int j=0;
                while (iterator.hasNext()){
                    String value=iterator.next();
                    Cell cell=row.createCell(j++);
                    cell.setCellValue(list.get(i).get(value).toString());
                }
            }
        try{
            wb.write(out);
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
