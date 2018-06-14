package com.kuangchi.sdd.doorAccessConsole.authority.util;

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
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.kuangchi.sdd.util.excel.TitleRowCell;

/**
 * 导入模板
 * @author work-admin
 *
 */
public class ExcelExportDoorTemplate {
	
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

	public ExcelExportDoorTemplate() {
	
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
		String sheetName = "按门添加权限表";//表头
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
        for(int i=1;i<4;i++){
        	HSSFRow row1 = sheet.createRow(i);
        	 Cell cell0 = row1.createCell(0);
        	 Font titleFont0=workbook.createFont();
        	 CellRangeAddress region = new CellRangeAddress(i, i, 0, 4); // 参数都是从O开始   
             sheet.addMergedRegion(region); 
              titleFont0.setBoldweight(Font.BOLDWEIGHT_BOLD);
              CellStyle titleCellStyle0=workbook.createCellStyle();
              titleFont0.setFontHeightInPoints((short) (12));
              titleCellStyle0.setFont(titleFont0);
              titleCellStyle0.setWrapText(true);
              titleCellStyle0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
              titleCellStyle0.setAlignment(CellStyle.ALIGN_LEFT);
              cell0.setCellStyle(titleCellStyle0);
        	if(i==1){
        		cell0.setCellValue("说明:1.门名称：点开页面数据行,选中卡数据行查看权限，设备下有关联门的名称，门名称可重复");
        	}else if(i==2){ 
        		cell0.setCellValue("          2.有效开始、有效结束时间格式：2000-01-01 01:00 (年-月-日  时-分) (中间一个空格)");
        	} else{
        		cell0.setCellValue("          3.浅绿色标题栏下的项目为必填项");
        	}
        }
       
        
		HSSFRow row = sheet.createRow(4);
		for(int i=0;i<titleRowCells.size();i++ ){
				CellStyle titleCellStyle2=workbook.createCellStyle();
			 	HSSFDataFormat format = (HSSFDataFormat) workbook.createDataFormat();  
			    titleCellStyle2.setDataFormat(format.getFormat("@"));  
				sheet.setDefaultColumnStyle(i,titleCellStyle2);	
			
				CellStyle titleCellStyle1=workbook.createCellStyle();
				Font titleFont1=workbook.createFont();
		        titleFont1.setBoldweight(Font.BOLDWEIGHT_BOLD);
		        titleFont1.setFontHeightInPoints((short) (12));
				titleCellStyle1.setFont(titleFont1);
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
				createTitleRowCell(titleRowCells.get(i),cell);
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
	/*private void createLongTitleRowCell(TitleRowCell t,Cell cell){
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(t.getTitleName());
		
		if(t.getSuggest().size() > 0){
			
			createLongSuggest(cell,t.getSuggest());
			
		}
		
	
	}*/
	
/*	private HSSFCellStyle style;*/
	

	private void createSuggest(Cell cell,List<String> sugs){
		int index = cell.getColumnIndex();
		CellRangeAddressList  cellRangeAddressList = new CellRangeAddressList(5, 65535, index, index);
		
		DVConstraint constraint = DVConstraint.createExplicitListConstraint(sugs.toArray(new String[]{}));
		
		HSSFDataValidation dataValidation = new HSSFDataValidation(cellRangeAddressList, constraint);
		
		sheet.addValidationData(dataValidation);
	
	}
	
/*	
	private void createLongSuggest(Cell cell,List<String> sugs){
		    
		   int index = cell.getColumnIndex();
		    Sheet hidden = workbook.createSheet("hidden"+index);
		    workbook.setSheetHidden(0, false);
		    String[] datas=sugs.toArray(new String[]{});
		    Row rowH = null;
		    Cell cellH = null;
		    for (int i = 0; i < datas.length; i++) {
		    	rowH = hidden.createRow(i);
		      cellH = rowH.createCell(0);
		      cellH.setCellValue(datas[i]);
		    }
		    Name namedCell = workbook.createName();
		    namedCell.setNameName("hidden"+index);
		    namedCell.setRefersToFormula("hidden"+index+"!A$1:A$" + datas.length);
		    DVConstraint constraint = DVConstraint
		        .createFormulaListConstraint("hidden"+index);
		    CellRangeAddressList  addressList = new CellRangeAddressList(5, 65535, index, index);
		     HSSFDataValidationHelper helpler=  new HSSFDataValidationHelper(sheet);
		    DataValidation validation =   helpler.createValidation(constraint, addressList);
		      sheet.addValidationData(validation);
	}*/
	
	
	public static void main(String[] args) throws IOException {
		File file = new  File("D://a.xls");
		ExcelExportDoorTemplate e = new ExcelExportDoorTemplate();
		
		List<TitleRowCell> titleRowCells = new ArrayList<TitleRowCell>();
		
		TitleRowCell t = new TitleRowCell("城市",true);
		t.addSuggest("南京");
		t.addSuggest("徐州");
		titleRowCells.add(t);
		
		
		
		
		TitleRowCell tOne = new TitleRowCell("学历",false);
		tOne.addSuggest("本科");
		tOne.addSuggest("硕士");
		titleRowCells.add(tOne);
		
		
		TitleRowCell tOnet = new TitleRowCell("外语",false);
		
		titleRowCells.add(tOnet);
		
		
		
		TitleRowCell tOneQ = new TitleRowCell("年龄",true);
		
		titleRowCells.add(tOneQ);
		
		
		
		e.createTitleRow(titleRowCells);
		
		e.writeToFile(file);
	}
	

}
