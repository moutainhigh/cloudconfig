package com.kuangchi.sdd.consumeConsole.incomeStatistics.service.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;

import com.kuangchi.sdd.consumeConsole.incomeStatistics.dao.IIncomestatisticsDao;
import com.kuangchi.sdd.consumeConsole.incomeStatistics.model.IncomeStatistics;
import com.kuangchi.sdd.consumeConsole.incomeStatistics.service.IIncomestatisticsService;
@Transactional
@Service("IncomestatisticsServiceImpl")
public class IncomestatisticsServiceImpl  implements IIncomestatisticsService {
	private static final int CARD_ID_LENGTH = 6;
	
	@Resource(name = "IncomestatisticsDaoImpl")
	private IIncomestatisticsDao incomestatisticsDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	


	/**
	 * 查询个人收支信息
	 */
	@Override
	public Grid selectAllConsumeRecords(IncomeStatistics income_record,
			String page, String size) {
		if(income_record.getEnd_time()!=null){
			SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date=format.parse(income_record.getEnd_time());
				date.setHours(date.getHours()+24);
				income_record.setEnd_time(format.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Integer count=incomestatisticsDao.getAllIncomeStatisticsCount(income_record);
		List<IncomeStatistics> incomeInfo=incomestatisticsDao.selectAllIncomeStatistics(income_record, page, size);
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(incomeInfo);
		return grid;
	}



	@Override
	public List<IncomeStatistics> exportIncomeStatistics(
			IncomeStatistics income_record) {
		if(income_record.getEnd_time()!=null){
			SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date=format.parse(income_record.getEnd_time());
				date.setHours(date.getHours()+24);
				income_record.setEnd_time(format.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		List<IncomeStatistics> incomeInfo=incomestatisticsDao.exportAllIncomestatistics(income_record);
		return incomeInfo;
	}




	@Override
	public Grid selectStatistics(
			IncomeStatistics income_record,String page, String size) {
		
		if(income_record.getEnd_time()!=null){
			SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date=format.parse(income_record.getEnd_time());
				date.setHours(date.getHours()+24);
				income_record.setEnd_time(format.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		Integer count=incomestatisticsDao.selectStatisticsCount(income_record);
		List<IncomeStatistics> incomeInfo=incomestatisticsDao.selectStatistics(income_record,page,size);
		List<IncomeStatistics> incomeInfos=new ArrayList<IncomeStatistics>();
		for (IncomeStatistics incomeStatistics : incomeInfo) {
			if(incomeStatistics.getType().equals("0")){
				incomeStatistics.setInbound_a(incomeStatistics.getInbound());
			}else if(incomeStatistics.getType().equals("1")){
				incomeStatistics.setOutbound_a(incomeStatistics.getOutbound());
			}else if(incomeStatistics.getType().equals("2")){
				incomeStatistics.setInbound_b(incomeStatistics.getInbound());
			}else if(incomeStatistics.getType().equals("3")){
				incomeStatistics.setOutbound_b(incomeStatistics.getOutbound());
			}else if(incomeStatistics.getType().equals("4")){
				incomeStatistics.setInbound_c(incomeStatistics.getInbound());
			}else if(incomeStatistics.getType().equals("5")){
				incomeStatistics.setOutbound_c(incomeStatistics.getOutbound());
			}else if(incomeStatistics.getType().equals("6")){
				incomeStatistics.setInbound_d(incomeStatistics.getInbound());
			}else if(incomeStatistics.getType().equals("7")){
				incomeStatistics.setOutbound_d(incomeStatistics.getOutbound());
			}else if(incomeStatistics.getType().equals("8")){
				continue;
			}
			incomeInfos.add(incomeStatistics);
		}
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(incomeInfo);
		return grid;
		
	}


	//导出报表汇总
	@Override
	public IncomeStatistics ExportAllStatistics(
			IncomeStatistics income_record) {
		if(income_record.getEnd_time()!=null){
			SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date=format.parse(income_record.getEnd_time());
				date.setHours(date.getHours()+24);
				income_record.setEnd_time(format.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		List<IncomeStatistics> incomeInfo=incomestatisticsDao.ExportAllStatistics(income_record);
		IncomeStatistics incomes=new IncomeStatistics();
		for (IncomeStatistics incomeStatistics : incomeInfo) {
			if(incomeStatistics.getType().equals("0")){
				double a=incomes.getInbound_a();
				incomes.setInbound_a(a=a+incomeStatistics.getInbound());
				if(incomeStatistics.getInbound()!=0){
					incomes.setInbound_a_c(incomes.getInbound_a_c()+1);
				}
				
			}else if(incomeStatistics.getType().equals("1")){
				double a=incomes.getOutbound_a();
				incomes.setOutbound_a(a=a+incomeStatistics.getOutbound());
				if(incomeStatistics.getOutbound()!=0){
					incomes.setOutbound_a_c(incomes.getOutbound_a_c()+1);
				}
				
			}else if(incomeStatistics.getType().equals("2")){
				double a=incomes.getInbound_b();
				incomes.setInbound_b(a=a+incomeStatistics.getInbound());
				if(incomeStatistics.getInbound()!=0){
					incomes.setInbound_b_c(incomes.getInbound_b_c()+1);
				}
				
			}else if(incomeStatistics.getType().equals("3")){
				double a=incomes.getOutbound_b();
				incomes.setOutbound_b(a=a+incomeStatistics.getOutbound());
				if(incomeStatistics.getOutbound()!=0){
					incomes.setOutbound_b_c(incomes.getOutbound_b_c()+1);
				}
			
			}else if(incomeStatistics.getType().equals("4")){
				double a=incomes.getInbound_c();
				incomes.setInbound_c(a=a+incomeStatistics.getInbound());
				if(incomeStatistics.getInbound()!=0){
					incomes.setInbound_c_c(incomes.getInbound_c_c()+1);
				}
				//注意退款应该为收入
			}else if(incomeStatistics.getType().equals("5")){
				double a=incomes.getOutbound_c();
				incomes.setOutbound_c(a=a+incomeStatistics.getInbound());
				if(incomeStatistics.getInbound()!=0){
					incomes.setOutbound_c_c(incomes.getOutbound_c_c()+1);
				}
				
			}else if(incomeStatistics.getType().equals("6")){
				double a=incomes.getInbound_d();
				incomes.setInbound_d(a=a+incomeStatistics.getInbound());
				if(incomeStatistics.getInbound()!=0){
					incomes.setInbound_d_c(incomes.getInbound_d_c()+1);
				}
				
			}else if(incomeStatistics.getType().equals("7")){
				double a=incomes.getOutbound_d();
				incomes.setOutbound_d(a=a+incomeStatistics.getOutbound());
				if(incomeStatistics.getOutbound()!=0){
					incomes.setOutbound_d_c(incomeStatistics.getOutbound_d_c()+1);
				}
				
			}else{
				continue;
			}
			incomes.setStaff_no(incomeStatistics.getStaff_no());
			incomes.setStaff_name(incomeStatistics.getStaff_name());
			incomes.setDept_name(incomeStatistics.getDept_name());
			//收入总合计
			incomes.setInbound_Money(incomes.getInbound_a()
					+incomes.getInbound_b()+incomes.getInbound_c()
					+incomes.getInbound_d()+incomes.getOutbound_c()
					);
			//支出总合计
			incomes.setOutbound_Money(incomes.getOutbound_a()
					+incomes.getOutbound_b()
					+incomes.getOutbound_d());
			//收入总次数
			incomes.setInbound_Num(incomes.getInbound_a_c()
					+incomes.getInbound_b_c()+incomes.getInbound_c_c()
					+incomes.getInbound_d_c()+incomes.getOutbound_c_c());
			//支出总次数
			incomes.setOutbound_Num(incomes.getOutbound_a_c()
					+incomes.getOutbound_b_c()
					+incomes.getOutbound_d_c());
		}
		return incomes;
	}



	@Override
	public List<IncomeStatistics> selectByStaffno() {
		List<IncomeStatistics> incomeInfo=incomestatisticsDao.selectByStaffno();
		return incomeInfo;
	}
	
	
	
}
