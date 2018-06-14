package com.kuangchi.sdd.consumeConsole.incomeStatistics.util;


import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import com.kuangchi.sdd.consumeConsole.incomeStatistics.model.IncomeStatistics;



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
    public static Workbook exportExcel(String sheetName, String colTitles[],IncomeStatistics incomes) throws Exception {
        Workbook wb = new HSSFWorkbook();
            generateSheet(sheetName,colTitles,incomes,wb);
        return wb;
    }

    public static void generateSheet(String sheetName,String[] columnsTitles,IncomeStatistics incomes, Workbook workbook) {
        Sheet sheet = workbook.createSheet(sheetName);
        //标题
        Row row0 = sheet.createRow(0);
        Cell cell00 = row0.createCell(0);
        cell00.setCellValue(sheetName);
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,11));
        
        
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
        columnTitleFont.setFontHeightInPoints((short) 16);//设置字体大小  
        //columnTitleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        CellStyle columnTitleStyle=workbook.createCellStyle();
        columnTitleStyle.setFont(columnTitleFont);
        columnTitleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        columnTitleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        columnTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);
       /* columnTitleStyle.setBorderBottom((short)1);
        columnTitleStyle.setBorderLeft((short)1);
        columnTitleStyle.setBorderRight((short)1);
        columnTitleStyle.setBorderTop((short)1);*/

        
        
        CellStyle contentStyle = workbook.createCellStyle();
   /*     contentStyle.setBorderBottom((short)1);
        contentStyle.setBorderLeft((short)1);
        contentStyle.setBorderRight((short)1);
        contentStyle.setBorderTop((short)1);*/
        contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
        
        //内容样式
        CellStyle contentStyles = workbook.createCellStyle();
        Font font = workbook.createFont();    
        font.setFontName("黑体");    
        font.setFontHeightInPoints((short) 14);//设置字体大小  
        contentStyles.setFont(font);
        contentStyles.setAlignment(CellStyle.ALIGN_CENTER);  
        
        //时间红色样式
        CellStyle contentStyles2 = workbook.createCellStyle();
        Font font2 = workbook.createFont();    
        font2.setColor(HSSFColor.RED.index);    
        font2.setFontHeightInPoints((short) 14);//设置字体大小  
        contentStyles2.setFont(font2);
        contentStyles2.setAlignment(CellStyle.ALIGN_CENTER);  
        
      //第一行数据
       
        
        Row row_til = sheet.createRow(1);
        Cell cell_titles = row_til.createCell((short) 0);
        Cell cell_time1 = row_til.createCell((short) 2);
        Cell cell_to = row_til.createCell((short) 4);
        Cell cell_time2 = row_til.createCell((short) 6);
       
        cell_titles.setCellValue("统计时间段：");
        cell_time1.setCellValue(incomes.getBegin_time());
        cell_to.setCellValue("to");
        cell_time2.setCellValue(incomes.getEnd_time());
       
        
        cell_titles.setCellStyle(contentStyles2);
        cell_time1.setCellStyle(contentStyles2);
        cell_to.setCellStyle(contentStyles2);
        cell_time2.setCellStyle(contentStyles2);
       
        
        
        sheet.addMergedRegion(new CellRangeAddress(1,1,0,1));
        sheet.addMergedRegion(new CellRangeAddress(1,1,4,5));
        sheet.addMergedRegion(new CellRangeAddress(1,1,8,9));
        sheet.addMergedRegion(new CellRangeAddress(1,1,2,3));
        sheet.addMergedRegion(new CellRangeAddress(1,1,6,7));
        sheet.addMergedRegion(new CellRangeAddress(1,1,10,11));
        
        
        
        Row row_d = sheet.createRow(2);
        Cell cell_no = row_d.createCell((short) 0);
        Cell cell_nos = row_d.createCell((short) 2);
        Cell cell_name = row_d.createCell((short) 4);
        Cell cell_names = row_d.createCell((short) 6);
        Cell cell_deptname = row_d.createCell((short) 8);
        Cell cell_deptnames = row_d.createCell((short) 10);
        cell_no.setCellValue("员工工号：");
        cell_nos.setCellValue(incomes.getStaff_no());
        cell_name.setCellValue("员工名称：");
        cell_names.setCellValue(incomes.getStaff_name());
        cell_deptname.setCellValue("部门名称：");
        cell_deptnames.setCellValue(incomes.getDept_name());
        
        cell_no.setCellStyle(contentStyles);
        cell_nos.setCellStyle(contentStyles);
        cell_name.setCellStyle(contentStyles);
        cell_names.setCellStyle(contentStyles);
        cell_deptname.setCellStyle(contentStyles);
        cell_deptnames.setCellStyle(contentStyles);
        
        
        sheet.addMergedRegion(new CellRangeAddress(2,2,0,1));
        sheet.addMergedRegion(new CellRangeAddress(2,2,4,5));
        sheet.addMergedRegion(new CellRangeAddress(2,2,8,9));
        sheet.addMergedRegion(new CellRangeAddress(2,2,2,3));
        sheet.addMergedRegion(new CellRangeAddress(2,2,6,7));
        sheet.addMergedRegion(new CellRangeAddress(2,2,10,11));
        
        
        
        
        Row row_c = sheet.createRow(3);
        Cell cell_title = row_c.createCell((short) 0);
        Cell cell_title2 = row_c.createCell((short) 6);
        cell_title.setCellValue("收入");
        cell_title2.setCellValue("支出");
        cell_title.setCellStyle(contentStyles);
        cell_title2.setCellStyle(contentStyles);
        sheet.addMergedRegion(new CellRangeAddress(3,3,0,5));
        sheet.addMergedRegion(new CellRangeAddress(3,3,6,11));
        
       
       
        //表头
       Row row1 = sheet.createRow(4);
        for (int i=0;i<6;i++){
          Cell cell=  row1.createCell(i*2);
            cell.setCellValue(columnsTitles[i]);
           cell.setCellStyle(columnTitleStyle);
        }
        
        for(int i=4;i<11;i++){
        	  sheet.addMergedRegion(new CellRangeAddress(i,i,0,1));
              sheet.addMergedRegion(new CellRangeAddress(i,i,2,3));
              sheet.addMergedRegion(new CellRangeAddress(i,i,4,5));
              sheet.addMergedRegion(new CellRangeAddress(i,i,6,7));
              sheet.addMergedRegion(new CellRangeAddress(i,i,8,9));
              sheet.addMergedRegion(new CellRangeAddress(i,i,10,11));
        }
      
        
        
        Row row_3 = sheet.createRow(5);
        Cell cell3 = row_3.createCell((short) 0);
        Cell cell3_1 = row_3.createCell((short) 2);
        Cell cell3_2 = row_3.createCell((short) 4);
        Cell cell3_3 = row_3.createCell((short) 6);
        Cell cell3_4 = row_3.createCell((short) 8);
        Cell cell3_5 = row_3.createCell((short) 10);
        cell3.setCellValue("充值");
        cell3_1.setCellValue(incomes.getInbound_a_c());
        cell3_2.setCellValue(incomes.getInbound_a());
        cell3_3.setCellValue("消费");
        cell3_4.setCellValue(incomes.getOutbound_a_c());
        cell3_5.setCellValue(incomes.getOutbound_a());
        
        cell3.setCellStyle(contentStyles);
        cell3_1.setCellStyle(contentStyles);
        cell3_2.setCellStyle(contentStyles);
        cell3_3.setCellStyle(contentStyles);
        cell3_4.setCellStyle(contentStyles);
        cell3_5.setCellStyle(contentStyles);
        
        
        Row row_4 = sheet.createRow(6);
        Cell cell4 = row_4.createCell((short) 0);
        Cell cell4_1 = row_4.createCell((short) 2);
        Cell cell4_2 = row_4.createCell((short) 4);
        Cell cell4_3 = row_4.createCell((short) 6);
        Cell cell4_4 = row_4.createCell((short) 8);
        Cell cell4_5 = row_4.createCell((short) 10);
        cell4.setCellValue("补助");
        cell4_1.setCellValue(incomes.getInbound_b_c());
        cell4_2.setCellValue(incomes.getInbound_b());
        cell4_3.setCellValue("补扣");
        cell4_4.setCellValue(incomes.getOutbound_b_c());
        cell4_5.setCellValue(incomes.getOutbound_b());
        
        cell4.setCellStyle(contentStyles);
        cell4_1.setCellStyle(contentStyles);
        cell4_2.setCellStyle(contentStyles);
        cell4_3.setCellStyle(contentStyles);
        cell4_4.setCellStyle(contentStyles);
        cell4_5.setCellStyle(contentStyles);
        
        
        Row row_5 = sheet.createRow(8);
        Cell cell5 = row_5.createCell((short) 0);
        Cell cell5_1 = row_5.createCell((short) 2);
        Cell cell5_2 = row_5.createCell((short) 4);
        Cell cell5_3 = row_5.createCell((short) 6);
        Cell cell5_4 = row_5.createCell((short) 8);
        Cell cell5_5 = row_5.createCell((short) 10);
        cell5.setCellValue("撤销消费");
        cell5_1.setCellValue(incomes.getInbound_c_c());
        cell5_2.setCellValue(incomes.getInbound_c());
       /* cell5_3.setCellValue("退款");
        cell5_4.setCellValue(incomes.getOutbound_c_c());
        cell5_5.setCellValue(incomes.getOutbound_c());*/
        
        cell5.setCellStyle(contentStyles);
        cell5_1.setCellStyle(contentStyles);
        cell5_2.setCellStyle(contentStyles);
        cell5_3.setCellStyle(contentStyles);
        cell5_4.setCellStyle(contentStyles);
        cell5_5.setCellStyle(contentStyles);
        
        
        Row row_7 = sheet.createRow(7);
        Cell cell7 = row_7.createCell((short) 0);
        Cell cell7_1 = row_7.createCell((short) 2);
        Cell cell7_2 = row_7.createCell((short) 4);
        Cell cell7_3 = row_7.createCell((short) 6);
        Cell cell7_4 = row_7.createCell((short) 8);
        Cell cell7_5 = row_7.createCell((short) 10);
        cell7.setCellValue("转入");
        cell7_1.setCellValue(incomes.getInbound_d_c());
        cell7_2.setCellValue(incomes.getInbound_d());
        cell7_3.setCellValue("转出");
        cell7_4.setCellValue(incomes.getOutbound_d_c());
        cell7_5.setCellValue(incomes.getOutbound_d());
        
        cell7.setCellStyle(contentStyles);
        cell7_1.setCellStyle(contentStyles);
        cell7_2.setCellStyle(contentStyles);
        cell7_3.setCellStyle(contentStyles);
        cell7_4.setCellStyle(contentStyles);
        cell7_5.setCellStyle(contentStyles);
        
        Row row_8 = sheet.createRow(9);
        Cell cell8 = row_8.createCell((short) 0);
        Cell cell8_1 = row_8.createCell((short) 2);
        Cell cell8_2 = row_8.createCell((short) 4);
        Cell cell8_3 = row_8.createCell((short) 6);
        Cell cell8_4 = row_8.createCell((short) 8);
        Cell cell8_5 = row_8.createCell((short) 10);
        cell8.setCellValue("退款");
        cell8_1.setCellValue(incomes.getOutbound_c_c());
        cell8_2.setCellValue(incomes.getOutbound_c());
       /* cell8_3.setCellValue("转出");
        cell8_4.setCellValue(incomes.getOutbound_d_c());
        cell8_5.setCellValue(incomes.getOutbound_d());*/
        
        cell8.setCellStyle(contentStyles);
        cell8_1.setCellStyle(contentStyles);
        cell8_2.setCellStyle(contentStyles);
        cell8_3.setCellStyle(contentStyles);
        cell8_4.setCellStyle(contentStyles);
        cell8_5.setCellStyle(contentStyles);
        
        
        Row row_6 = sheet.createRow(10);
        Cell cell6 = row_6.createCell((short) 0);
        Cell cell6_1 = row_6.createCell((short) 2);
        Cell cell6_2 = row_6.createCell((short) 4);
        Cell cell6_3 = row_6.createCell((short) 6);
        Cell cell6_4 = row_6.createCell((short) 8);
        Cell cell6_5 = row_6.createCell((short) 10);
        cell6.setCellValue("收入合计");
        cell6_1.setCellValue(incomes.getInbound_Num());
        cell6_2.setCellValue(incomes.getInbound_Money());
        cell6_3.setCellValue("支出合计");
        cell6_4.setCellValue(incomes.getOutbound_Num());
        cell6_5.setCellValue(incomes.getOutbound_Money());
        
        cell6.setCellStyle(contentStyles);
        cell6_1.setCellStyle(contentStyles);
        cell6_2.setCellStyle(contentStyles);
        cell6_3.setCellStyle(contentStyles);
        cell6_4.setCellStyle(contentStyles);
        cell6_5.setCellStyle(contentStyles);
    }

}
