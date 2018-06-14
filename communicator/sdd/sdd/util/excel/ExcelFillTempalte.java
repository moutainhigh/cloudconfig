package com.kuangchi.sdd.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * excel 占位符填充
 * @author work-admin
 *
 */
public class ExcelFillTempalte {
	
	
	public static void main(String[] args) throws IOException {
		File file = new File("F://a.xls");
		File file1 = new File("F://a111.xls");
		
		FileInputStream in = new FileInputStream(file);
		
		Workbook workbook =  new HSSFWorkbook(in);
		
		Sheet sheet = workbook.getSheetAt(0);
		
		
		//ExcelFillTempalte.fillValue(sheet, 2, 2, "fdfs");
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("name", "dfdsfsdf");
		map.put("age", "12sdd");
		map.put("title", "dfdf111");
		
		ExcelFillTempalte.fillTem(sheet, map);
		
		in.close();
		FileOutputStream out = new FileOutputStream(file1);
		workbook.write(out);
		
		out.close();
		
		
		
	}
	
	
	public static void fillValue(Sheet sheet, int rowIndex ,int cloumIndex,String value){
		Row row  = sheet.getRow(rowIndex);
		
		Cell cell = row.getCell(cloumIndex);
		
		cell.setCellValue(value);
	}
	
	public static void fillTem(Sheet sheet,Map<String,String> values){
		Iterator<Row>  iterator=  sheet.iterator();
		
	label:	while(iterator.hasNext()){
			Row tmpRow = iterator.next();
			
			 Iterator<Cell> cellInIterator =  tmpRow.cellIterator();
			 
			 while (cellInIterator.hasNext()) {
				 
				 if(values.isEmpty()){
						break label;
					}
				 
				 
				Cell cellTmp =  cellInIterator.next();
				String value = cellTmp.getStringCellValue();
				
				String v = ExcelFillTempalte.getStr(value);
				
				if(null != v){
					String keyValue = values.get(v);
					
					if(null != keyValue ){
						cellTmp.setCellValue(keyValue);
						values.remove(keyValue);
						
					}
				}
				
				
			}
			
		}
	}
	
	public static String getStr(String str){
		if(null == str ||"".equals(str) || str.length() < 3 ){
			return null;
		}

		return str.substring(1, str.length()-1);

	}
	

}
