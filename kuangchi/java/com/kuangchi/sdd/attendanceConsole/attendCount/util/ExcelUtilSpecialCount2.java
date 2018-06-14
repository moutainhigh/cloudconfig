package com.kuangchi.sdd.attendanceConsole.attendCount.util;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * 
 * @功能描述:考勤统计，导出功能的加大单元格宽带和自动换行 
 * 
 */
public class ExcelUtilSpecialCount2 {
//    private static final int MAX_NUM = 65535;
    private static final int MAX_NUM = 1048575;

     /**
      * columnsTitles为列标题, 和columnsKeys该列中数据所对应的键, 依次对应
      **/
    public static Workbook exportExcel(String sheetName, String[] columnsTitles,String[] columnsKeys, List<Map<String, Object>> data) throws Exception {
//        Workbook wb = new HSSFWorkbook();
    	SXSSFWorkbook wb = new SXSSFWorkbook();
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
        Sheet sheet = workbook.createSheet(sheetName); //建一个文件名为sheetName的Excel工作簿
        
        for(int i=0;i<100; i++){
        	 //第一个参数代表列id(从0开始),第2个参数代表宽度值    
           if(i==columnsKeys.length-1){
        		sheet.setColumnWidth(i, 24000);
        	}else{
        		sheet.setColumnWidth(i, 6000);
        	}
        	//sheet.createRow(i).setHeightInPoints(1000);
        }
        
        int columnsNum=columnsKeys.length;
        //标题
        Row row0 = sheet.createRow(0); // 创建新行(row)
        Cell cell00 = row0.createCell(0); //将单元格(cell)放入其中
        cell00.setCellValue(sheetName); //设置单元格内容
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,columnsNum-1));  //根据单元格的多少合并标题格
        // 设置字体
        Font titleFont=workbook.createFont();
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD); //宽度
        titleFont.setFontHeightInPoints((short) (24));//字体大小
      // 设置单元格类型
        CellStyle titleCellStyle=workbook.createCellStyle();
        titleCellStyle.setFont(titleFont);
        titleCellStyle.setAlignment(CellStyle.ALIGN_CENTER); //水平布局：居中
        titleCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中
        titleCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); //设置填充方式
        titleCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex()); //前景颜色
        titleCellStyle.setBorderBottom((short)1);//下边框
        titleCellStyle.setBorderLeft((short)1);//左边框
        titleCellStyle.setBorderRight((short)1);
        titleCellStyle.setBorderTop((short)1);
        cell00.setCellStyle(titleCellStyle);

        Font columnTitleFont=workbook.createFont();
        columnTitleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        CellStyle columnTitleStyle=workbook.createCellStyle();
        columnTitleStyle.setFont(columnTitleFont);
        columnTitleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        columnTitleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        columnTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);//水平居中
        columnTitleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中
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
        contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中
        contentStyle.setWrapText(true);//设置自动换行



        //表头
        Row row1 = sheet.createRow(1);
        for (int i=0;i<columnsNum;i++){
           Cell cell=   row1.createCell(i);
            cell.setCellValue(columnsTitles[i]);  //设置单元格内容
            cell.setCellStyle(columnTitleStyle); //设置单元格样式
        }

		for (int i = 0; i < MAX_NUM - 2; i++) {
			if (start + i >= data.size()) {
				break;
			}
			Row row = sheet.createRow(i + 2);
			for (int j = 0; j < columnsNum; j++) {
				Cell cell = row.createCell(j);
				cell.setCellStyle(contentStyle);
				Object d = data.get(i).get(columnsKeys[j]);
				if (d != null) {
					cell.setCellValue(d.toString());

				}
			}
		}
	}




}

