package com.kuangchi.sdd.consumeConsole.incomeStatistics.dao;


import java.util.List;

import com.kuangchi.sdd.consumeConsole.incomeStatistics.model.IncomeStatistics;

public interface IIncomestatisticsDao {
	
	public List<IncomeStatistics> selectAllIncomeStatistics(IncomeStatistics income_record,String page, String size);//模糊查询个人收支信息
	
	public Integer getAllIncomeStatisticsCount(IncomeStatistics income_record);//查询总的行数
	
	public List<IncomeStatistics> exportAllIncomestatistics(IncomeStatistics income_record);//导出个人收支信息
	
	public List<IncomeStatistics> selectByStaffno();//查询所有员工编号
	
	public List<IncomeStatistics> selectStatistics(IncomeStatistics income_record,String page, String size);//查询个人收支报表信息
	
	public Integer selectStatisticsCount(IncomeStatistics income_record);//查询总的行数
	
	public List<IncomeStatistics> ExportAllStatistics(IncomeStatistics income_record);//导出报表汇总
}
