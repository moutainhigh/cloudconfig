package com.kuangchi.sdd.consumeConsole.financeStatistics.service;


import java.util.List;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.financeStatistics.model.AccountStatistics;
import com.kuangchi.sdd.consumeConsole.financeStatistics.model.FinanceStatistics;


public interface IFinancestatisticsService {
	
	
	public List<FinanceStatistics> SelectAllStatistics(FinanceStatistics finance);//查询报表汇总
	
	public Grid selectAccountStatic(AccountStatistics account,String page,String rows);//查询财务总表信息
	
	public List<FinanceStatistics> ExportAllStatistics(FinanceStatistics finance);//导出报表汇总
}
