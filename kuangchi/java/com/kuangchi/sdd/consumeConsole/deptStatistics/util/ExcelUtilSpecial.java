package com.kuangchi.sdd.consumeConsole.deptStatistics.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;
import java.util.Map;

/**
 * Created by jianhui.wu on 2016/2/19.
 */
public class ExcelUtilSpecial {
    private static final int MAX_NUM = 65535;

     /**
      * 
      * 
      * columnsTitles为列标题, 和columnsKeys该列中数据所对应的键, 依次对应
      * 
      * **/
    public static Workbook exportExcel(String sheetName, String[] columnsTitles,String[] columnsKeys, List<Map<String, Object>> data,String begin_time,String end_time) throws Exception {
        Workbook wb = new HSSFWorkbook();
        if(data.size()==0){
       	 Sheet sheet = wb.createSheet(sheetName);
            
            for(int i=0;i<100; i++){
            	sheet.setColumnWidth(i, 5000);
            	//sheet.createRow(i).setHeightInPoints(1000);
            }
            int columnsNum=columnsKeys.length;
          //标题
            Row row0 = sheet.createRow(0);
            Cell cell00 = row0.createCell(0);
            cell00.setCellValue(sheetName);
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,columnsNum-1));
            Font titleFont=wb.createFont();
            titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
            titleFont.setFontHeightInPoints((short) (24));
            CellStyle titleCellStyle=wb.createCellStyle();
            titleCellStyle.setFont(titleFont);
            titleCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            titleCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            titleCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
            titleCellStyle.setBorderBottom((short)1);
            titleCellStyle.setBorderLeft((short)1);
            titleCellStyle.setBorderRight((short)1);
            titleCellStyle.setBorderTop((short)1);
            cell00.setCellStyle(titleCellStyle);

            Font columnTitleFont=wb.createFont();
            columnTitleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
            CellStyle columnTitleStyle=wb.createCellStyle();
            columnTitleStyle.setFont(columnTitleFont);
            columnTitleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            columnTitleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            columnTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);
            columnTitleStyle.setBorderBottom((short)1);
            columnTitleStyle.setBorderLeft((short)1);
            columnTitleStyle.setBorderRight((short)1);
            columnTitleStyle.setBorderTop((short)1);


            CellStyle contentStyle = wb.createCellStyle();
            contentStyle.setBorderBottom((short)1);
            contentStyle.setBorderLeft((short)1);
            contentStyle.setBorderRight((short)1);
            contentStyle.setBorderTop((short)1);
            contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
            
            
            //统计时间段
            Row row1 = sheet.createRow(1);
            Cell cell0=row1.createCell(0);
            cell0.setCellValue("统计时间段："+begin_time+" to "+end_time);
            sheet.addMergedRegion(new CellRangeAddress(1,1,0,columnsNum-1));
            Font timeFont=wb.createFont();
            timeFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
            timeFont.setColor(HSSFColor.RED.index);
            CellStyle timeGroupStyle=wb.createCellStyle();
            timeGroupStyle.setFont(timeFont);
            timeGroupStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            timeGroupStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            timeGroupStyle.setAlignment(CellStyle.ALIGN_LEFT);
            timeGroupStyle.setBorderBottom((short)1);
            timeGroupStyle.setBorderLeft((short)1);
            timeGroupStyle.setBorderRight((short)1);
            timeGroupStyle.setBorderTop((short)1);
            cell0.setCellStyle(timeGroupStyle);
            
            //表头
            Row row2 = sheet.createRow(2);
            for (int i=0;i<columnsNum;i++){
               Cell cell=   row2.createCell(i);
                cell.setCellValue(columnsTitles[i]);
                cell.setCellStyle(columnTitleStyle);
            }   
       }
        Integer sheetNum=data.size()%(MAX_NUM-2)==0?data.size()/MAX_NUM:(data.size()/MAX_NUM+1);
        for (int i=0;i<sheetNum;i++){
            generateSheet(sheetName,columnsTitles,columnsKeys,data,wb,(MAX_NUM-2)*i,begin_time,end_time);
        }
        return wb;
    }

    public static void generateSheet(String sheetName,String[] columnsTitles,String[] columnsKeys, List<Map<String, Object>> data, Workbook workbook, int start,String begin_time,String end_time) {
        Sheet sheet = workbook.createSheet(sheetName);
        for(int i=0;i<columnsTitles.length; i++){
        	sheet.setColumnWidth(i, 5000);
        	//sheet.createRow(i).setHeightInPoints(1000);
        }
        
        int columnsNum=columnsKeys.length;
        //标题
        Row row0 = sheet.createRow(0);
        Cell cell00 = row0.createCell(0);
        cell00.setCellValue(sheetName);
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,columnsNum-1));
        Font titleFont=workbook.createFont();
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleFont.setFontHeightInPoints((short) (24));
        CellStyle titleCellStyle=workbook.createCellStyle();
        titleCellStyle.setFont(titleFont);
        titleCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        titleCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        titleCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        titleCellStyle.setBorderBottom((short)1);
        titleCellStyle.setBorderLeft((short)1);
        titleCellStyle.setBorderRight((short)1);
        titleCellStyle.setBorderTop((short)1);
        cell00.setCellStyle(titleCellStyle);

        Font columnTitleFont=workbook.createFont();
        columnTitleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        CellStyle columnTitleStyle=workbook.createCellStyle();
        columnTitleStyle.setFont(columnTitleFont);
        columnTitleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        columnTitleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        columnTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);
        columnTitleStyle.setBorderBottom((short)1);
        columnTitleStyle.setBorderLeft((short)1);
        columnTitleStyle.setBorderRight((short)1);
        columnTitleStyle.setBorderTop((short)1);


        CellStyle contentStyle = workbook.createCellStyle();
        contentStyle.setBorderBottom((short)1);
        contentStyle.setBorderLeft((short)1);
        contentStyle.setBorderRight((short)1);
        contentStyle.setBorderTop((short)1);
        contentStyle.setAlignment(CellStyle.ALIGN_CENTER);

        //统计时间段
        Row row1 = sheet.createRow(1);
        Cell cell0=row1.createCell(0);
        cell0.setCellValue("统计时间段："+begin_time+" to "+end_time);
        sheet.addMergedRegion(new CellRangeAddress(1,1,0,columnsNum-1));
        Font timeFont=workbook.createFont();
        timeFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        timeFont.setColor(HSSFColor.RED.index);
        CellStyle timeGroupStyle=workbook.createCellStyle();
        timeGroupStyle.setFont(timeFont);
        timeGroupStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        timeGroupStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        timeGroupStyle.setAlignment(CellStyle.ALIGN_LEFT);
        timeGroupStyle.setBorderBottom((short)1);
        timeGroupStyle.setBorderLeft((short)1);
        timeGroupStyle.setBorderRight((short)1);
        timeGroupStyle.setBorderTop((short)1);
        cell0.setCellStyle(timeGroupStyle);
      
        //表头
        Row row2 = sheet.createRow(2);
        for (int i=0;i<columnsNum;i++){
           Cell cell=   row2.createCell(i);
            cell.setCellValue(columnsTitles[i]);
            cell.setCellStyle(columnTitleStyle);
        }

        for (int i = 0; i < MAX_NUM-2; i++) {
            if (start+i>=data.size()){
                break;
            }
            Row row = sheet.createRow(i+3);
            for (int j=0;j<columnsNum;j++){
                Cell cell=row.createCell(j);
                cell.setCellStyle(contentStyle);
                if(j==0||j==1){
                	  Object  d=data.get(i).get(columnsKeys[j]);
                      if (d!=null) {
                          cell.setCellValue(d.toString());

                      }
                }else{
                	  Double d=(Double) data.get(i).get(columnsKeys[j]);
                	  cell.setCellValue(d);
                }
              
            }
        }
    }





}
