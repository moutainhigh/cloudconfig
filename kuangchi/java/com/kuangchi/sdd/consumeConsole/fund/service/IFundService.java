package com.kuangchi.sdd.consumeConsole.fund.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.fund.model.FundModel;



/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-17 上午10:31:55
 * @功能描述: 资金流水表模块-业务层
 */
public interface IFundService {
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-17 上午11:02:29
	 * @功能描述: 根据参数查询资料流水信息[分页]
	 * @参数描述:
	 */
	public List<FundModel> getFundByParamPage(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-17 上午11:03:26
	 * @功能描述: 根据参数查询资料流水信息[总数]
	 * @参数描述:
	 */
	public Integer getFundByParamCount(Map<String, Object> map);
}
