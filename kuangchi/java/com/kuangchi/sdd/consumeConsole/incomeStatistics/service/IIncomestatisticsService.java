package com.kuangchi.sdd.consumeConsole.incomeStatistics.service;


import java.util.List;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.incomeStatistics.model.IncomeStatistics;


public interface IIncomestatisticsService {
	
	public Grid selectAllConsumeRecords(IncomeStatistics income_record,
		String page, String size);//模糊查询消费所有信息
	
	public List<IncomeStatistics> exportIncomeStatistics(IncomeStatistics income_record);//导出个人收支信息
	
	public Grid selectStatistics(IncomeStatistics income_record,String page, String size);//查询个人收支报表信息
	
	public IncomeStatistics ExportAllStatistics(IncomeStatistics income_record);//导出报表汇总
	
	public List<IncomeStatistics> selectByStaffno();//查询所有员工编号
}
