package com.kuangchi.sdd.ZigBeeConsole.gateway.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFDataValidationHelper;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.kuangchi.sdd.util.excel.TitleRowCell;

/**
 * 导入模板
 * @author work-admin
 *
 */
public class ExcelExportTemplate {
	
	private Workbook workbook;
	
	private HSSFSheet sheet;
	
	/**
	 * 写入文件
	 * @param xlsFile
	 * @throws IOException
	 */
	public void writeToFile(File xlsFile) throws IOException{
		FileOutputStream out = new FileOutputStream(xlsFile);
		
		try {
			workbook.write(out);
		} catch (IOException e) {
			out.close();
			throw new IOException("",e);
		}
		
		out.close();
		
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}

	public ExcelExportTemplate() {
	
		this.workbook =  new HSSFWorkbook();
		
	/*	style = (HSSFCellStyle) workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);*/
		/*for(int i=0;i<titleRowCells.length; i++){
        	
        	//sheet.createRow(i).setHeightInPoints(1000);
        }
		sheet.setColumnWidth();*/
	}
	
	/**
	 * 创建列
	 * @param titleRowCells
	 */
	public void createTitleRow(List<TitleRowCell> titleRowCells){
		
		sheet =  (HSSFSheet) workbook.createSheet();
		HSSFRow row = sheet.createRow(0);
		for(int i=0;i <titleRowCells.size();i++ ){
			HSSFCell cell = row.createCell(i);
			
			createTitleRowCell(titleRowCells.get(i),cell);
		}
	}
	
	
	public void setBorder(CellRangeAddress cellRangeAddress, Sheet sheet,  
            Workbook wb) throws Exception {  
        RegionUtil.setBorderLeft(1, cellRangeAddress, sheet, wb);  
        RegionUtil.setBorderBottom(1, cellRangeAddress, sheet, wb);  
        RegionUtil.setBorderRight(1, cellRangeAddress, sheet, wb);  
        RegionUtil.setBorderTop(1, cellRangeAddress, sheet, wb);  
          
} 
	
	/**
	 * 创建列
	 * @param titleRowCells  超过255Asccii长度人下拉框使用该方法
	 */
	public void createLongTitleRow(List<TitleRowCell> titleRowCells){
		String sheetName = "网关表";//表头
		sheet =  (HSSFSheet) workbook.createSheet(sheetName);
		for(int i=0;i<titleRowCells.size(); i++){
			sheet.setColumnWidth(i, 6000);
		}
		/*设置表头开始*/
		Row row0 = sheet.createRow(0);
        Cell cell00 = row0.createCell(0);
        cell00.setCellValue(sheetName);
        int columnsNum=titleRowCells.size();
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,columnsNum-1));//设置表头所占的行
        Font titleFont0=workbook.createFont();
        titleFont0.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleFont0.setFontHeightInPoints((short) (12));
        Font titleFont=workbook.createFont();
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleFont.setFontHeightInPoints((short) (18));
        CellStyle titleCellStyle=workbook.createCellStyle();
        titleCellStyle.setFont(titleFont);
        titleCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        titleCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        titleCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        cell00.setCellStyle(titleCellStyle);
        /*设置表头结束*/
        
        //表格注释
        for(int i=1;i<6;i++){
        	HSSFRow row1 = sheet.createRow(i);
        	 Cell cell0 = row1.createCell(0);
        	 CellRangeAddress region = new CellRangeAddress(i, i, 0, 4); // 参数都是从O开始   
             sheet.addMergedRegion(region); 
            //  titleFont0.setBoldweight(Font.BOLDWEIGHT_BOLD);
              CellStyle titleCellStyle0=workbook.createCellStyle();
              titleCellStyle0.setFont(titleFont0);
              titleCellStyle0.setWrapText(true);
              titleCellStyle0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
              titleCellStyle0.setAlignment(CellStyle.ALIGN_LEFT);
              cell0.setCellStyle(titleCellStyle0);
        	if(i==1){
        		cell0.setCellValue("说明:1.请仔细输入网关参数，若网关当前不在线，系统会在空闲时间设置Pan_id");
        	}else if(i==2){
        		cell0.setCellValue("          2.网关pan_id不可重复，且需在0-65535范围内 ");
        	}else if(i==3){
        		cell0.setCellValue("          3.IP格式如 1.2.3.4");
        	}else if(i==4){
        		cell0.setCellValue("          4.端口格式如8080 ");
        	}else if(i==5){
        		cell0.setCellValue("          5.浅绿色标题栏下的项目为必填项 ");
        	}
        }
       
        
		HSSFRow row = sheet.createRow(6);
		
		int suggestIndex = 0;
		for(int i=0;i<titleRowCells.size();i++ ){
			CellStyle titleCellStyle2=workbook.createCellStyle();
		 	HSSFDataFormat format = (HSSFDataFormat) workbook.createDataFormat();  
		    titleCellStyle2.setDataFormat(format.getFormat("@"));  
			sheet.setDefaultColumnStyle(i,titleCellStyle2);
		
			CellStyle titleCellStyle1=workbook.createCellStyle();
			Font titleFont1=workbook.createFont();
	        titleFont1.setBoldweight(Font.BOLDWEIGHT_BOLD);
			titleCellStyle1.setFont(titleFont0);
	        titleCellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
	        titleCellStyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        if(titleRowCells.get(i).isRequire()){
	        	
	        	titleCellStyle1.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
	        }else{
	        	titleCellStyle1.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
	        }
	        titleCellStyle1.setBorderBottom((short)1);
	        titleCellStyle1.setBorderLeft((short)1);
	        titleCellStyle1.setBorderRight((short)1);
	        titleCellStyle1.setBorderTop((short)1);
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(titleCellStyle1);
			// 抛异常时执行另一个方法，加载超过255长度的下拉框
			try {
				createTitleRowCell(titleRowCells.get(i),cell);
			} catch (IllegalArgumentException e) {
				if(titleRowCells.get(i).getSuggest().size() > 0){
					suggestIndex++ ; // suggestIndext 指过长下拉框下标，主要为了加载过长下拉框时 隐藏下拉框表单
				}
				createLongTitleRowCell(titleRowCells.get(i),cell,suggestIndex);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	

	private void createTitleRowCell(TitleRowCell t,Cell cell){
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(t.getTitleName());
		if(t.getSuggest().size() > 0){
			createSuggest(cell,t.getSuggest());
		}
	}
	/**
	 * 创建列
	 * @param titleRowCells  超过255Asccii长度人下拉框使用该方法
	 */
	private void createLongTitleRowCell(TitleRowCell t,Cell cell, int suggestIndex){
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(t.getTitleName());
		
		if(t.getSuggest().size() > 0){
			
			createLongSuggest(cell,t.getSuggest(),suggestIndex);
			
		}
		
	
	}
	
/*	private HSSFCellStyle style;*/
	

	private void createSuggest(Cell cell,List<String> sugs){
		int index = cell.getColumnIndex();
		CellRangeAddressList  cellRangeAddressList = new CellRangeAddressList(6, 65535, index, index);
		
		DVConstraint constraint = DVConstraint.createExplicitListConstraint(sugs.toArray(new String[]{}));
		
		HSSFDataValidation dataValidation = new HSSFDataValidation(cellRangeAddressList, constraint);
		
		sheet.addValidationData(dataValidation);
	
	}
	
	
	private void createLongSuggest(Cell cell,List<String> sugs, int suggestIndex){
		    
		   int index = cell.getColumnIndex();
		    Sheet hidden = workbook.createSheet("hidden"+index);
		    workbook.setSheetHidden(suggestIndex, true);
		    String[] datas=sugs.toArray(new String[]{});
		    Row rowH = null;
		    Cell cellH = null;
		    for (int i = 0; i < datas.length; i++) {
		    	rowH = hidden.createRow(i);
		      cellH = rowH.createCell(index);
		      cellH.setCellValue(datas[i]);
		    }
		    Name namedCell = workbook.createName();
		    namedCell.setNameName("hidden"+index);
		    namedCell.setRefersToFormula("hidden"+index+"!A$1:A$" + datas.length);
		    DVConstraint constraint = DVConstraint
		        .createFormulaListConstraint("hidden"+index);
		    CellRangeAddressList  addressList = new CellRangeAddressList(6, 65535, index, index);
		     HSSFDataValidationHelper helpler=  new HSSFDataValidationHelper(sheet);
		    DataValidation validation =   helpler.createValidation(constraint, addressList);
		      sheet.addValidationData(validation);
	}
	
	

}
