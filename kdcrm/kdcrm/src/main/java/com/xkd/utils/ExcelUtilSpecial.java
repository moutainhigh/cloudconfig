package com.xkd.utils;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;
import java.util.Map;


public class ExcelUtilSpecial {
    private static final int MAX_NUM = 65535;

 
    
     /**
      * 
      * 
      * columnsTitles为列标题, 和columnsKeys该列中数据所对应的键, 依次对应
      * 
      * **/
    public static Workbook createWorkbook(String sheetName, String[] columnsTitles,String[] columnsKeys, List<Map<String, Object>> data) throws Exception {
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
               titleFont.setBold(true);
               titleFont.setFontHeightInPoints((short) (24));
               CellStyle titleCellStyle=wb.createCellStyle();
               titleCellStyle.setFont(titleFont);
               titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
               titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
               titleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
               titleCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
               titleCellStyle.setBorderBottom(BorderStyle.THIN);
               titleCellStyle.setBorderLeft (BorderStyle.THIN);
               titleCellStyle.setBorderRight(BorderStyle.THIN);
               titleCellStyle.setBorderTop(BorderStyle.THIN);
               cell00.setCellStyle(titleCellStyle);

               Font columnTitleFont=wb.createFont();
               columnTitleFont.setBold(true);
               CellStyle columnTitleStyle=wb.createCellStyle();
               columnTitleStyle.setFont(columnTitleFont);
               columnTitleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
               columnTitleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
               columnTitleStyle.setAlignment(HorizontalAlignment.CENTER);
               columnTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
               columnTitleStyle.setBorderBottom(BorderStyle.THIN);
               columnTitleStyle.setBorderLeft(BorderStyle.THIN);
               columnTitleStyle.setBorderRight(BorderStyle.THIN);
               columnTitleStyle.setBorderTop(BorderStyle.THIN);


               CellStyle contentStyle = wb.createCellStyle();
               contentStyle.setBorderBottom(BorderStyle.THIN);
               contentStyle.setBorderLeft(BorderStyle.THIN);
               contentStyle.setBorderRight(BorderStyle.THIN);
               contentStyle.setBorderTop(BorderStyle.THIN);
               contentStyle.setAlignment(HorizontalAlignment.CENTER);
               contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);

               //表头
               Row row1 = sheet.createRow(1);
               for (int i=0;i<columnsNum;i++){
                  Cell cell=   row1.createCell(i);
                   cell.setCellValue(columnsTitles[i]);
                   cell.setCellStyle(columnTitleStyle);
               }   
          }
        Integer sheetNum=data.size()%(MAX_NUM-2)==0?data.size()/MAX_NUM:(data.size()/MAX_NUM+1);
        for (int i=0;i<sheetNum;i++){
            generateSheet(sheetName,columnsTitles,columnsKeys,data,wb,(MAX_NUM-2)*i);
        }
        return wb;
    }

    public static void generateSheet(String sheetName,String[] columnsTitles,String[] columnsKeys, List<Map<String, Object>> data, Workbook workbook, int start) {
        Sheet sheet = workbook.createSheet(sheetName);
        for(int i=0;i<columnsTitles.length; i++){
        	sheet.setColumnWidth(i, 7000);
        	//sheet.createRow(i).setHeightInPoints(1000);
        }
        int columnsNum=columnsKeys.length;
        //标题
        Row row0 = sheet.createRow(0);
        Cell cell00 = row0.createCell(0);
        cell00.setCellValue(sheetName);
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,columnsNum-1));
        Font titleFont=workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) (24));
        CellStyle titleCellStyle=workbook.createCellStyle();
        titleCellStyle.setFont(titleFont);
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        titleCellStyle.setBorderBottom(BorderStyle.THIN);
        titleCellStyle.setBorderLeft(BorderStyle.THIN);
        titleCellStyle.setBorderRight(BorderStyle.THIN);
        titleCellStyle.setBorderTop(BorderStyle.THIN);
        cell00.setCellStyle(titleCellStyle);

        Font columnTitleFont=workbook.createFont();
        columnTitleFont.setBold(true);
        CellStyle columnTitleStyle=workbook.createCellStyle();
        columnTitleStyle.setFont(columnTitleFont);
        columnTitleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        columnTitleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        columnTitleStyle.setAlignment(HorizontalAlignment.CENTER);
        columnTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        columnTitleStyle.setBorderBottom(BorderStyle.THIN);
        columnTitleStyle.setBorderLeft(BorderStyle.THIN);
        columnTitleStyle.setBorderRight(BorderStyle.THIN);
        columnTitleStyle.setBorderTop(BorderStyle.THIN);


        CellStyle contentStyle = workbook.createCellStyle();
        contentStyle.setBorderBottom(BorderStyle.THIN);
        contentStyle.setBorderLeft(BorderStyle.THIN);
        contentStyle.setBorderRight(BorderStyle.THIN);
        contentStyle.setBorderTop(BorderStyle.THIN);
        contentStyle.setAlignment(HorizontalAlignment.CENTER);
        contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);




        //表头
        Row row1 = sheet.createRow(1);
        for (int i=0;i<columnsNum;i++){
           Cell cell=row1.createCell(i);
            cell.setCellValue(columnsTitles[i]);
            cell.setCellStyle(columnTitleStyle);
        }

        for (int i = 0; i < MAX_NUM-2; i++) {
            if (start+i>=data.size()){
                break;
            }
            Row row = sheet.createRow(i+2);
            for (int j=0;j<columnsNum;j++){
                Cell cell=row.createCell(j);
                cell.setCellStyle(contentStyle);
                Object  d=data.get(i).get(columnsKeys[j]);
                if (d!=null) {
                    cell.setCellValue(d.toString());

                }
            }
        }
    }





}
