package com.kuangchi.sdd.util.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

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

	public ExcelExportTemplate() {
	
		this.workbook =  new HSSFWorkbook();
		
		style = (HSSFCellStyle) workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.RED.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
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
	
	private void createTitleRowCell(TitleRowCell t,Cell cell){
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(t.getTitleName());
		
		if(t.getSuggest().size() > 0){
			createSuggest(cell,t.getSuggest());
		}
		
		if (t.isRequire()) {
			createCellType(cell);
		}
		
	}
	
	private HSSFCellStyle style;
	
	private void createCellType(Cell cell) {
		
		cell.setCellStyle(style);
		
	}

	private void createSuggest(Cell cell,List<String> sugs){
		int index = cell.getColumnIndex();
		CellRangeAddressList  cellRangeAddressList = new CellRangeAddressList(1, 65535, index, index);
		
		DVConstraint constraint = DVConstraint.createExplicitListConstraint(sugs.toArray(new String[]{}));
		
		HSSFDataValidation dataValidation = new HSSFDataValidation(cellRangeAddressList, constraint);
		
		sheet.addValidationData(dataValidation);
	
	}
	
	
	public static void main(String[] args) throws IOException {
		File file = new  File("F://a.xls");
		
		ExcelExportTemplate e = new ExcelExportTemplate();
		
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
