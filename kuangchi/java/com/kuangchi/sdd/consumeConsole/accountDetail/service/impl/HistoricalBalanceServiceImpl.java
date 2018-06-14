package com.kuangchi.sdd.consumeConsole.accountDetail.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.accountDetail.dao.HistoricalBalanceDao;
import com.kuangchi.sdd.consumeConsole.accountDetail.model.HistoricalBalanceModel;
import com.kuangchi.sdd.consumeConsole.accountDetail.service.HistoricalBalanceService;

@Service("historicalBalanceServiceImpl")
public class HistoricalBalanceServiceImpl implements HistoricalBalanceService {
	
	@Resource(name="historicalBalanceDaoImpl")
	private HistoricalBalanceDao historicalBalanceDao;
	
	@Override
	public Grid<HistoricalBalanceModel> getAllHistoricalBalance(Map map) {
		Grid<HistoricalBalanceModel> grid = new Grid<HistoricalBalanceModel>();
		List<HistoricalBalanceModel> list = historicalBalanceDao.getHistoricalBalanceInfoList(map);
		grid.setRows(list);
		if(null!=list){
		Integer count =	historicalBalanceDao.getHistoricalBalanceInfoCount(map);
		grid.setTotal(count);
		}else{
			grid.setTotal(0);
		}
		return grid;
	}

	@Override
	public List<HistoricalBalanceModel> getHistoricalBalanceList() {
		return historicalBalanceDao.getHistoricalBalanceList();
	}
}
