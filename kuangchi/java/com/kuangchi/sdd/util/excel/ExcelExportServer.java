package com.kuangchi.sdd.util.excel;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelExportServer {
		
	private int total;
	private boolean isOneSheet;
	
	private String[] fields;
	
	public static final Logger LOG = Logger.getLogger(ExcelExportServer.class);
	
	public ExcelExportServer(int total,boolean isOneSheet) {
		
		this.total = total;
		this.isOneSheet = isOneSheet;
		
		LOG.info("合计条数:" +this.total);
	}
	
	
	/**
	 * 导出的字段
	 * @param fields
	 */
	public void setfields(String[] fields){
		this.fields = fields;
	}
	
	
	private Field[] getClassField(Class target) throws SecurityException, NoSuchFieldException{
		
		Field[] targetFields = new Field[fields.length];
		
		for(int i=0;i<fields.length;i++){	
			targetFields[i] = target.getDeclaredField(fields[i]);
		}
		return targetFields;
	}





	public void createSheet(Workbook workbook, String sheetName,
			String[] columnsTitle, List<?> data,int start,int end) throws Exception {
		
		LOG.info("start:" +start+"　end:"+end);
		Sheet sheet = null;
		
		try{
			sheet = workbook.createSheet(sheetName);

		}catch(Exception e){
			sheet = workbook.createSheet();
		}
		
		//创建标题	
		Row titleRow = sheet.createRow(0);
		
	/*	for(int i=0;i<columnsTitle.length;i++){
			Cell cell = titleRow.createCell(i);
			cell.setCellValue(columnsTitle[i]);
		}*/
		
		createSheetTitle(titleRow,columnsTitle);
		
		//创建内容	
		Row bodyRow = null;
		Field[] fields = null;
		for(int j =start;j<end;j++ ){
			//System.out.println("＝＝＝＝＝"+(j+1-start));

			 bodyRow = sheet.createRow(j+1-start);
			 
			 //设置行高
			 bodyRow.setHeightInPoints(25f);
			
			Object tmp = data.get(j); 
			
			if(fields == null){
				Class c = tmp.getClass();
				
				fields = c.getDeclaredFields();
			}
			
			Cell cell = null;
			for(int k=0;k<fields.length;k++){
				cell =  bodyRow.createCell(k);
	
				Field field = fields[k];
				dealWithCell(cell,field,tmp);
				

			}
		}
		
		
		if(end<total && isOneSheet == false){
			int nextStart = end + 1;
			int nextEnd = end+ ExcelExportUtil.MAX_NUM > total ? total:end+ ExcelExportUtil.MAX_NUM +1;
	
			createSheet(workbook,sheetName,columnsTitle,data,nextStart,nextEnd);
		}

	}
	

	
	
	private void createSheetTitle(Row titleRow, String[] columnsTitle) {
		
		//设置标题行高
		
		titleRow.setHeightInPoints(25f);
		
		Cell cell  = null;
		for(int i=0;i<columnsTitle.length;i++){
			cell = titleRow.createCell(i);
			cell.setCellValue(columnsTitle[i]);
		}
		
	}





	private void dealWithCell(Cell cell, Field field,Object tmp) throws Exception {
		
		field.setAccessible(true);
		String fileType = field.getType().getName();
		
		if(fileType.equals(String.class.getName())){
			try {
				String strResult = field.get(tmp).toString();
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(strResult);
			} catch (IllegalArgumentException e) {
				
				e.printStackTrace();
			} catch (IllegalAccessException e) {
			
				e.printStackTrace();
			}
		}
		
		
		
		else if(fileType.equals("int")){
			try {
				int intResult = field.getInt(tmp);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(intResult);
			} catch (IllegalArgumentException e) {
			
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				
				e.printStackTrace();
			}
		}
		
		
		
		
		else if(fileType.equals("float")){
			try {
				float floatResult = field.getFloat(tmp);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(floatResult);
			} catch (IllegalArgumentException e) {
				
				e.printStackTrace();
			} catch (IllegalAccessException e) {
			
				e.printStackTrace();
			}
		}
		
		
		else if(fileType.equals("double")){
			try {
				double doubleResult = field.getDouble(tmp);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(doubleResult);
			} catch (IllegalArgumentException e) {
				
				e.printStackTrace();
			} catch (IllegalAccessException e) {
			
				e.printStackTrace();
			}
		}
		
		
		else if(fileType.equals(Date.class.getName())){
			try {
				Date dateResult = (Date) field.get(tmp);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(formatDate(dateResult));
			} catch (IllegalArgumentException e) {
				
				e.printStackTrace();
			} catch (IllegalAccessException e) {
			
				e.printStackTrace();
			}
		}
		
		else if(fileType.equals("long")){
			try {
				long longResult = field.getLong(tmp);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(longResult);
			} catch (IllegalArgumentException e) {
				
				e.printStackTrace();
			} catch (IllegalAccessException e) {
			
				e.printStackTrace();
			}
		}else{
			throw new Exception("类型不支持");
		}

		
	}





	/**
	 * 格式化导出时间格式
	 * @param date
	 * @return
	 */
	private String formatDate(Date date){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		return format.format(date);
	}
	


}
