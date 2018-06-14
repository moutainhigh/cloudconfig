package com.kuangchi.sdd.consumeConsole.accountDetail.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.accountDetail.model.HistoricalBalanceModel;

public interface HistoricalBalanceDao {
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-29 下午2:11:05
	 * @功能描述:获取全部历史余额信息 
	 * @参数描述:
	 */
	public List<HistoricalBalanceModel> getHistoricalBalanceInfoList(Map map);
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-29 下午2:11:30
	 * @功能描述:获取历史余额信息总数 
	 * @参数描述:
	 */
	public Integer getHistoricalBalanceInfoCount(Map map);

	public List<HistoricalBalanceModel> getHistoricalBalanceList();
}
