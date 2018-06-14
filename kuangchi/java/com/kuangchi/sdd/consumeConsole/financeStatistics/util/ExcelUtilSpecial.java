package com.kuangchi.sdd.consumeConsole.financeStatistics.util;


import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import com.kuangchi.sdd.consumeConsole.financeStatistics.model.FinanceStatistics;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;



/**
 * Created by jianhui.wu on 2016/2/19.
 */
public class ExcelUtilSpecial {
    private static final int MAX_NUM = 65535;

     /**
      * 
      * 
      * columnsTitles为列标题, 和columnsKeys该列中数据所对应的键, 依次对应 			String colTitles[],
      * 
      * **/
    public static Workbook exportExcel(String sheetName,List<FinanceStatistics> list) throws Exception {
        Workbook wb = new HSSFWorkbook();
            generateSheet(sheetName,list,wb);
        return wb;
    }

    public static void generateSheet(String sheetName,List<FinanceStatistics> finance, Workbook workbook) {
        Sheet sheet = workbook.createSheet(sheetName);
        //标题
        Row row0 = sheet.createRow(0);
        Cell cell00 = row0.createCell(0);
        cell00.setCellValue(sheetName);
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,14));
        
        
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
        CellStyle columnTitleStyle=workbook.createCellStyle();
        columnTitleStyle.setFont(columnTitleFont);
        columnTitleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        columnTitleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        columnTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);
        
        CellStyle contentStyle = workbook.createCellStyle();
        contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
        
        //内容样式
        CellStyle contentStyles = workbook.createCellStyle();
        Font font = workbook.createFont();    
        font.setFontName("黑体");    
        font.setFontHeightInPoints((short) 14);//设置字体大小  
        contentStyles.setFont(font);
        contentStyles.setAlignment(CellStyle.ALIGN_CENTER);  
        
        //备注样式
        CellStyle contentStyles1 = workbook.createCellStyle();
        Font font1 = workbook.createFont();    
        font1.setFontName("黑体");  
        font1.setFontHeightInPoints((short) 14);//设置字体大小  
        contentStyles1.setFont(font1);
        
      //时间红色样式
        CellStyle contentStyles2 = workbook.createCellStyle();
        Font font2 = workbook.createFont();    
        font2.setColor(HSSFColor.RED.index);    
        font2.setFontHeightInPoints((short) 14);//设置字体大小  
        contentStyles2.setFont(font2);
        contentStyles2.setAlignment(CellStyle.ALIGN_CENTER);  
    
        
        //第一行数据
        Row row_1 = sheet.createRow(1);
        Cell cell_10 = row_1.createCell((short) 0);
        Cell cell_12 = row_1.createCell((short) 2);
        Cell cell_14 = row_1.createCell((short) 4);
        Cell cell_15 = row_1.createCell((short) 5);
        cell_10.setCellValue("统计时间段");
        cell_12.setCellValue(finance.get(0).getBegin_time());
        cell_14.setCellValue("to");
        cell_15.setCellValue(finance.get(0).getEnd_time());
        cell_10.setCellStyle(contentStyles2);
        cell_12.setCellStyle(contentStyles2);
        cell_14.setCellStyle(contentStyles2);
        cell_15.setCellStyle(contentStyles2);
        sheet.addMergedRegion(new CellRangeAddress(1,1,0,1));
        sheet.addMergedRegion(new CellRangeAddress(1,1,2,3));
        sheet.addMergedRegion(new CellRangeAddress(1,1,5,6));
        
        //第二行数据
        Row row_d = sheet.createRow(2);
        Cell cell_no = row_d.createCell((short) 0);
        Cell cell_nos = row_d.createCell((short) 6);
        Cell cell_name = row_d.createCell((short) 9);
        Cell cell_names = row_d.createCell((short) 12);
     
        cell_no.setCellValue("统计项目");
        cell_nos.setCellValue("统计人数（人次）");
        cell_name.setCellValue("统计金额（元）");
        cell_names.setCellValue("备注");
        
        cell_no.setCellStyle(contentStyles);
        cell_nos.setCellStyle(contentStyles);
        cell_name.setCellStyle(contentStyles);
        cell_names.setCellStyle(contentStyles);
        
        sheet.addMergedRegion(new CellRangeAddress(2,2,0,5));
        sheet.addMergedRegion(new CellRangeAddress(2,2,6,8));
        sheet.addMergedRegion(new CellRangeAddress(2,2,9,11));
        sheet.addMergedRegion(new CellRangeAddress(2,2,12,14));
 
       //第三、四、五行数据
        Row row3 = sheet.createRow(3);
        Row row4 = sheet.createRow(4);
        Row row5 = sheet.createRow(5);
        Cell cell_30 = row3.createCell((short) 0);
        Cell cell_31 = row3.createCell((short) 3);
        Cell cell_32 = row3.createCell((short) 6);
        Cell cell_33 = row3.createCell((short) 9);
        Cell cell_34 = row3.createCell((short) 12);
        Cell cell_41 = row4.createCell((short) 3);
        Cell cell_42 = row4.createCell((short) 6);
        Cell cell_43 = row4.createCell((short) 9);
        Cell cell_44 = row4.createCell((short) 12);
        Cell cell_51 = row5.createCell((short) 3);
        Cell cell_52 = row5.createCell((short) 6);
        Cell cell_53 = row5.createCell((short) 9);
        Cell cell_54 = row5.createCell((short) 12);
        cell_30.setCellValue("期初期末统计");
        cell_31.setCellValue("期初账户总数和余额(元)");
        cell_32.setCellValue(finance.get(0).getBegin_accountCount());
        cell_33.setCellValue(finance.get(0).getBegin_accountMoney());
        cell_34.setCellValue("反映期初状况");
        cell_41.setCellValue("期末账户总数和余额(元)");
        cell_42.setCellValue(finance.get(0).getEnd_accountCount());
        cell_43.setCellValue(finance.get(0).getEnd_accountMoney());
        cell_44.setCellValue("反映期末状况");
        cell_51.setCellValue("总发生额(元)");
        cell_52.setCellValue(finance.get(0).getEnd_accountCount()-finance.get(0).getBegin_accountCount());
        cell_53.setCellValue(finance.get(0).getEnd_accountMoney()-finance.get(0).getBegin_accountMoney());
        cell_54.setCellValue("期末-期初 差额(元)");
        
        cell_30.setCellStyle(contentStyles);
        cell_31.setCellStyle(contentStyles);
        cell_32.setCellStyle(contentStyles);
        cell_33.setCellStyle(contentStyles);
        cell_34.setCellStyle(contentStyles1);
        cell_41.setCellStyle(contentStyles);
        cell_42.setCellStyle(contentStyles);
        cell_43.setCellStyle(contentStyles);
        cell_44.setCellStyle(contentStyles1);
        cell_51.setCellStyle(contentStyles);
        cell_52.setCellStyle(contentStyles);
        cell_53.setCellStyle(contentStyles);
        cell_54.setCellStyle(contentStyles1);
       
        sheet.addMergedRegion(new CellRangeAddress(3,5,0,2));
        
        
        //合并各行三个单元格
        for (int i = 3; i < 25; i++) {
        	 sheet.addMergedRegion(new CellRangeAddress(i,i,3,5));
		}
        
       //第六七八九十行数据
        Row row6 = sheet.createRow(6);
        Row row7 = sheet.createRow(7);
        Row row8 = sheet.createRow(8);
        Row row9 = sheet.createRow(9);
        Row row10 = sheet.createRow(10);
        
        Cell cell_60 = row6.createCell((short) 0);
        Cell cell_61 = row6.createCell((short) 3);
        Cell cell_62 = row6.createCell((short) 6);
        Cell cell_63 = row6.createCell((short) 9);
        Cell cell_64 = row6.createCell((short) 12);
        Cell cell_71 = row7.createCell((short) 3);
        Cell cell_72 = row7.createCell((short) 6);
        Cell cell_73 = row7.createCell((short) 9);
        Cell cell_74 = row7.createCell((short) 12);
        Cell cell_81 = row8.createCell((short) 3);
        Cell cell_82 = row8.createCell((short) 6);
        Cell cell_83 = row8.createCell((short) 9);
        Cell cell_84 = row8.createCell((short) 12);
        Cell cell_91 = row9.createCell((short) 3);
        Cell cell_92 = row9.createCell((short) 6);
        Cell cell_93 = row9.createCell((short) 9);
        Cell cell_94 = row9.createCell((short) 12);
        Cell cell_101 = row10.createCell((short) 3);
        Cell cell_102 = row10.createCell((short) 6);
        Cell cell_103 = row10.createCell((short) 9);
        Cell cell_104 = row10.createCell((short) 12);
        cell_60.setCellValue("现金统计(元)");
        cell_61.setCellValue("充值(元)");
        cell_62.setCellValue(finance.get(0).getInbound_a_c());
        cell_63.setCellValue(finance.get(0).getInbound_a());
        cell_64.setCellValue("反映充值发生额和次数");
        cell_71.setCellValue("退款(元)");
        cell_72.setCellValue(finance.get(0).getOutbound_c_c());
        cell_73.setCellValue(finance.get(0).getOutbound_c());
        cell_74.setCellValue("反映退款发生额和次数");
        cell_81.setCellValue("收卡押金(元)");
        cell_82.setCellValue(finance.get(0).getCard_pledge_count());
        cell_83.setCellValue(finance.get(0).getCard_pledge_money());
        cell_84.setCellValue("合计卡押金金额(元)");
        cell_91.setCellValue("退卡押金(元)");
        cell_92.setCellValue(finance.get(0).getRefund_pledge_count());
        cell_93.setCellValue(finance.get(0).getRefund_pledge_money());
        cell_94.setCellValue("合计退卡押金金额(元)");
        cell_101.setCellValue("现金发生额(元)");
        cell_102.setCellValue(finance.get(0).getInbound_a_c()+
        		finance.get(0).getOutbound_c_c()+
        		finance.get(0).getCard_pledge_count()+
        		finance.get(0).getRefund_pledge_count());
        cell_103.setCellValue(finance.get(0).getInbound_a()-
        		finance.get(0).getOutbound_c()+
        		finance.get(0).getCard_pledge_money()-
        		finance.get(0).getRefund_pledge_money()
        		);
        cell_104.setCellValue("次数=充值+退款+收卡押金+退卡押金," +
        		"发生额=充值-退款+收卡押金-退卡押金");

        sheet.addMergedRegion(new CellRangeAddress(6,10,0,2));
        cell_60.setCellStyle(contentStyles);
        cell_61.setCellStyle(contentStyles);
        cell_62.setCellStyle(contentStyles);
        cell_63.setCellStyle(contentStyles);
        cell_64.setCellStyle(contentStyles1);
        cell_71.setCellStyle(contentStyles);
        cell_72.setCellStyle(contentStyles);
        cell_73.setCellStyle(contentStyles);
        cell_74.setCellStyle(contentStyles1);
        cell_81.setCellStyle(contentStyles);
        cell_82.setCellStyle(contentStyles);
        cell_83.setCellStyle(contentStyles);
        cell_84.setCellStyle(contentStyles1);
        cell_91.setCellStyle(contentStyles);
        cell_92.setCellStyle(contentStyles);
        cell_93.setCellStyle(contentStyles);
        cell_94.setCellStyle(contentStyles1);
        cell_101.setCellStyle(contentStyles);
        cell_102.setCellStyle(contentStyles);
        cell_103.setCellStyle(contentStyles);
        cell_104.setCellStyle(contentStyles1);
      //第11、12、13、14、15行数据
        Row row11 = sheet.createRow(11);
        Row row12 = sheet.createRow(12);
        Row row13 = sheet.createRow(13);
        Row row14 = sheet.createRow(14);
        Row row15 = sheet.createRow(15);
        Cell cell_110 = row11.createCell((short) 0);
        Cell cell_111 = row11.createCell((short) 3);
        Cell cell_112 = row11.createCell((short) 6);
        Cell cell_113 = row11.createCell((short) 9);
        Cell cell_114 = row11.createCell((short) 12);
        Cell cell_121 = row12.createCell((short) 3);
        Cell cell_122 = row12.createCell((short) 6);
        Cell cell_123 = row12.createCell((short) 9);
        Cell cell_124 = row12.createCell((short) 12);
        Cell cell_131 = row13.createCell((short) 3);
        Cell cell_132 = row13.createCell((short) 6);
        Cell cell_133 = row13.createCell((short) 9);
        Cell cell_134 = row13.createCell((short) 12);
        Cell cell_141 = row14.createCell((short) 3);
        Cell cell_142 = row14.createCell((short) 6);
        Cell cell_143 = row14.createCell((short) 9);
        Cell cell_144 = row14.createCell((short) 12);
        Cell cell_151 = row15.createCell((short) 3);
        Cell cell_152 = row15.createCell((short) 6);
        Cell cell_153 = row15.createCell((short) 9);
        Cell cell_154 = row15.createCell((short) 12);
        cell_110.setCellValue("卡内交易统计");
        cell_111.setCellValue("消费(元)");
        cell_112.setCellValue(finance.get(0).getOutbound_a_c());
        cell_113.setCellValue(finance.get(0).getOutbound_a());
        cell_114.setCellValue("反映消费发生额和次数");
        cell_121.setCellValue("撤销消费(元)");
        cell_122.setCellValue(finance.get(0).getInbound_c_c());
        cell_123.setCellValue(finance.get(0).getInbound_c());
        cell_124.setCellValue("反映撤销消费发生额和次数");
        cell_131.setCellValue("补助(元)");
        cell_132.setCellValue(finance.get(0).getInbound_b_c());
        cell_133.setCellValue(finance.get(0).getInbound_b());
        cell_134.setCellValue("反映补助发生额和次数");
        cell_141.setCellValue("补扣(元)");
        cell_142.setCellValue(finance.get(0).getOutbound_b_c());
        cell_143.setCellValue(finance.get(0).getOutbound_b());
        cell_144.setCellValue("反映补扣发生额和次数");
        cell_151.setCellValue("总发生额(元)");
        cell_152.setCellValue(finance.get(0).getInbound_a_c()+
        		finance.get(0).getOutbound_c_c()+
        		finance.get(0).getInbound_c_c()+
        		finance.get(0).getOutbound_a_c()+
        		finance.get(0).getInbound_b_c()+
        		finance.get(0).getOutbound_b_c()
        		);
        cell_153.setCellValue(finance.get(0).getInbound_a()+
        		finance.get(0).getInbound_b()+
        		finance.get(0).getInbound_c()-
        		finance.get(0).getOutbound_a()-
        		finance.get(0).getOutbound_b()-
        		finance.get(0).getOutbound_c());
        cell_154.setCellValue("次数=充值+退款+消费+撤销+补助+补扣," +
        		"发生额=充值+撤销+补助-消费-退款-补扣");
        sheet.addMergedRegion(new CellRangeAddress(11,15,0,2));
        cell_110.setCellStyle(contentStyles);
        cell_111.setCellStyle(contentStyles);
        cell_112.setCellStyle(contentStyles);
        cell_113.setCellStyle(contentStyles);
        cell_114.setCellStyle(contentStyles1);
        cell_121.setCellStyle(contentStyles);
        cell_122.setCellStyle(contentStyles);
        cell_123.setCellStyle(contentStyles);
        cell_124.setCellStyle(contentStyles1);
        cell_131.setCellStyle(contentStyles);
        cell_132.setCellStyle(contentStyles);
        cell_133.setCellStyle(contentStyles);
        cell_134.setCellStyle(contentStyles1);
        cell_141.setCellStyle(contentStyles);
        cell_142.setCellStyle(contentStyles);
        cell_143.setCellStyle(contentStyles);
        cell_144.setCellStyle(contentStyles1);
        cell_151.setCellStyle(contentStyles);
        cell_152.setCellStyle(contentStyles);
        cell_153.setCellStyle(contentStyles);
        cell_154.setCellStyle(contentStyles1);
        
      //第16、17、18、19、20、21、22、23、24行数据
        Row row16 = sheet.createRow(16);
        Row row17 = sheet.createRow(17);
        Row row18 = sheet.createRow(18);
        Row row19 = sheet.createRow(19);
        Row row20 = sheet.createRow(20);
        Row row21 = sheet.createRow(21);
        Row row22 = sheet.createRow(22);
        Row row23 = sheet.createRow(23);
        Row row24 = sheet.createRow(24);
        Cell cell_160 = row16.createCell((short) 0);
        Cell cell_161 = row16.createCell((short) 3);
        Cell cell_162 = row16.createCell((short) 6);
        Cell cell_171 = row17.createCell((short) 3);
        Cell cell_172 = row17.createCell((short) 6);
        Cell cell_181 = row18.createCell((short) 3);
        Cell cell_182 = row18.createCell((short) 6);
        Cell cell_191 = row19.createCell((short) 3);
        Cell cell_192 = row19.createCell((short) 6);
        Cell cell_201 = row20.createCell((short) 3);
        Cell cell_202 = row20.createCell((short) 6);
        Cell cell_211 = row21.createCell((short) 3);
        Cell cell_212 = row21.createCell((short) 6);
        Cell cell_221 = row22.createCell((short) 3);
        Cell cell_222 = row22.createCell((short) 6);
        Cell cell_231 = row23.createCell((short) 3);
        Cell cell_232 = row23.createCell((short) 6);
        Cell cell_241 = row24.createCell((short) 3);
        Cell cell_242 = row24.createCell((short) 6);
        cell_160.setCellValue("非金额类交易统计");
        cell_161.setCellValue("未绑定");
        cell_162.setCellValue(finance.get(0).getCount00());
        cell_171.setCellValue("未发卡");
        cell_172.setCellValue(finance.get(0).getCount10());
        cell_181.setCellValue("正常");
        cell_182.setCellValue(finance.get(0).getCount20());
        cell_191.setCellValue("解挂");
        cell_192.setCellValue(finance.get(0).getCount50());
        cell_201.setCellValue("挂失");
        cell_202.setCellValue(finance.get(0).getCount40());
        cell_211.setCellValue("报损");
        cell_212.setCellValue(finance.get(0).getCount30());
        cell_221.setCellValue("退卡");
        cell_222.setCellValue(finance.get(0).getCount60());
        cell_231.setCellValue("发卡不成功");
        cell_232.setCellValue(finance.get(0).getCount100());
        cell_241.setCellValue("非金额交易次数合计");
        int sum=finance.get(0).getCount00()+
        		finance.get(0).getCount10()+
        		finance.get(0).getCount20()+
        		finance.get(0).getCount30()+
        		finance.get(0).getCount40()+
        		finance.get(0).getCount50()+
        		finance.get(0).getCount60()+
        		finance.get(0).getCount100();
        cell_242.setCellValue(sum);
        sheet.addMergedRegion(new CellRangeAddress(16,24,0,2));
     
        cell_160.setCellStyle(contentStyles);
        cell_161.setCellStyle(contentStyles);
        cell_162.setCellStyle(contentStyles);
        cell_171.setCellStyle(contentStyles);
        cell_172.setCellStyle(contentStyles);
        cell_181.setCellStyle(contentStyles);
        cell_182.setCellStyle(contentStyles);
        cell_191.setCellStyle(contentStyles);
        cell_192.setCellStyle(contentStyles);
        cell_201.setCellStyle(contentStyles);
        cell_202.setCellStyle(contentStyles);
        cell_211.setCellStyle(contentStyles);
        cell_212.setCellStyle(contentStyles);
        cell_221.setCellStyle(contentStyles);
        cell_222.setCellStyle(contentStyles);
        cell_231.setCellStyle(contentStyles);
        cell_232.setCellStyle(contentStyles);
        cell_241.setCellStyle(contentStyles);
        cell_242.setCellStyle(contentStyles);
        
        
        //表头
      /* Row row1 = sheet.createRow(3);
        for (int i=0;i<6;i++){
          Cell cell=  row1.createCell(i*2);
            cell.setCellValue(columnsTitles[i]);
           cell.setCellStyle(columnTitleStyle);
        }*/
        
        for(int i=3;i<25;i++){
        	  sheet.addMergedRegion(new CellRangeAddress(i,i,6,8));
              sheet.addMergedRegion(new CellRangeAddress(i,i,9,11));
              sheet.addMergedRegion(new CellRangeAddress(i,i,12,14));
             
        }
      
    }
    
}