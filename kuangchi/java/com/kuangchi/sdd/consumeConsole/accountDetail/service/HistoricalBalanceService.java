package com.kuangchi.sdd.consumeConsole.accountDetail.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.accountDetail.model.HistoricalBalanceModel;

public interface HistoricalBalanceService {
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-29 下午2:10:11
	 * @功能描述:历史余额信息查询 
	 * @参数描述:
	 */
	public Grid<HistoricalBalanceModel> getAllHistoricalBalance(Map map);
	
	public List<HistoricalBalanceModel> getHistoricalBalanceList();
}
