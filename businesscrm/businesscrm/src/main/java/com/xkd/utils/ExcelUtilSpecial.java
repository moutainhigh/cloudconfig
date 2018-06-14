package com.xkd.utils;


import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

















    /**
     * @Title: createExcelTemplate
     * @Description: 生成Excel导入模板
     * @param @param filePath  Excel文件路径
     * @param @param handers   Excel列标题(数组)
     * @param @param downData  下拉框数据(数组)
     * @param @param downRows  下拉列的序号(数组,序号从0开始)
     * @return void
     * @throws
     */
    public static Workbook createExcelTemplate( String[] handers,
                                            List<String[]> downData, String[] downRows){

        HSSFWorkbook wb = new HSSFWorkbook();//创建工作薄

        //表头样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        //字体样式
        HSSFFont fontStyle = wb.createFont();
        fontStyle.setFontName("微软雅黑");
        fontStyle.setFontHeightInPoints((short)12);
        fontStyle.setBold(true);
        style.setFont(fontStyle);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());

        //新建sheet
        HSSFSheet sheet1 = wb.createSheet("Sheet1");
        HSSFSheet sheet2 = wb.createSheet("Sheet2");
        HSSFSheet sheet3 = wb.createSheet("Sheet3");

        //生成sheet1内容
        HSSFRow rowFirst = sheet1.createRow(0);//第一个sheet的第一行为标题
        //写标题
        for(int i=0;i<handers.length;i++){
            HSSFCell cell = rowFirst.createCell(i); //获取第一行的每个单元格
            sheet1.setColumnWidth(i, 4000); //设置每列的列宽
            cell.setCellStyle(style); //加样式
            cell.setCellValue(handers[i]); //往单元格里写数据
        }

        //设置下拉框数据
        String[] arr = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        int index = 0;
        HSSFRow row = null;
        for(int r=0;r<downRows.length;r++){
            String[] dlData = downData.get(r);//获取下拉对象
            int rownum = Integer.parseInt(downRows[r]);

            if(dlData.length<5){ //255以内的下拉
                //255以内的下拉,参数分别是：作用的sheet、下拉内容数组、起始行、终止行、起始列、终止列
                sheet1.addValidationData(setDataValidation(sheet1, dlData, 1, 50000, rownum ,rownum)); //超过255个报错
            } else { //255以上的下拉，即下拉列表元素很多的情况

                //1、设置有效性
                //String strFormula = "Sheet2!$A$1:$A$5000" ; //Sheet2第A1到A5000作为下拉列表来源数据
                String strFormula = "Sheet2!$"+arr[index]+"$1:$"+arr[index]+"$"+(dlData.length); //Sheet2第A1到A5000作为下拉列表来源数据
                sheet2.setColumnWidth(r, 4000); //设置每列的列宽
                //设置数据有效性加载在哪个单元格上,参数分别是：从sheet2获取A1到A5000作为一个下拉的数据、起始行、终止行、起始列、终止列
                sheet1.addValidationData(SetDataValidation(strFormula, 1, 50000, rownum, rownum)); //下拉列表元素很多的情况

                //2、生成sheet2内容
                for(int j=0;j<dlData.length;j++){
                    if(index==0){ //第1个下拉选项，直接创建行、列
                        row = sheet2.createRow(j); //创建数据行
                        sheet2.setColumnWidth(j, 4000); //设置每列的列宽
                        row.createCell(0).setCellValue(dlData[j]); //设置对应单元格的值

                    } else { //非第1个下拉选项

                        int rowCount = sheet2.getLastRowNum();
                        //System.out.println("========== LastRowNum =========" + rowCount);
                        if(j<=rowCount){ //前面创建过的行，直接获取行，创建列
                            //获取行，创建列
                            sheet2.getRow(j).createCell(index).setCellValue(dlData[j]); //设置对应单元格的值

                        } else { //未创建过的行，直接创建行、创建列
                            sheet2.setColumnWidth(j, 4000); //设置每列的列宽
                            //创建行、创建列
                            sheet2.createRow(j).createCell(index).setCellValue(dlData[j]); //设置对应单元格的值
                        }
                    }
                }
                index++;
            }
        }

        return  wb;
    }




    /**
     *
     * @Title: SetDataValidation
     * @Description: 下拉列表元素很多的情况 (255以上的下拉)
     * @param @param strFormula
     * @param @param firstRow   起始行
     * @param @param endRow     终止行
     * @param @param firstCol   起始列
     * @param @param endCol     终止列
     * @param @return
     * @return HSSFDataValidation
     * @throws
     */
    private static HSSFDataValidation SetDataValidation(String strFormula,
                                                        int firstRow, int endRow, int firstCol, int endCol) {


        // 设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(strFormula);
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions,constraint);

        dataValidation.createErrorBox("Error", "Error");
        dataValidation.createPromptBox("", null);

        return dataValidation;
    }


    /**
     *
     * @Title: setDataValidation
     * @Description: 下拉列表元素不多的情况(255以内的下拉)
     * @param @param sheet
     * @param @param textList
     * @param @param firstRow
     * @param @param endRow
     * @param @param firstCol
     * @param @param endCol
     * @param @return
     * @return DataValidation
     * @throws
     */
    private static DataValidation setDataValidation(Sheet sheet, String[] textList, int firstRow, int endRow, int firstCol, int endCol) {

        DataValidationHelper helper = sheet.getDataValidationHelper();
        //加载下拉列表内容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textList);
        //DVConstraint constraint = new DVConstraint();
        constraint.setExplicitListValues(textList);

        System.out.println(firstRow+"-"+endRow+"-"+firstCol+"-"+endCol);
        //设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList( firstRow, endRow,  firstCol,  endCol);

        //数据有效性对象
        DataValidation data_validation = helper.createValidation(constraint, regions);
//        DataValidation data_validation = new DataValidation(regions, constraint);

        return data_validation;
    }




}
