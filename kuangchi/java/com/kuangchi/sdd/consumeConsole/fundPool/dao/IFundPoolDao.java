package com.kuangchi.sdd.consumeConsole.fundPool.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.fund.model.FundModel;
import com.kuangchi.sdd.consumeConsole.fundPool.model.FundPoolModel;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-16 上午11:19:40
 * @功能描述: 企业资金池模块-dao
 */
public interface IFundPoolDao {
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-16 上午11:20:09
	 * @功能描述: 根据参数查询资金池[分页]
	 * @参数描述:
	 */
	public List<FundPoolModel> getFundPoolByParamPage(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-16 上午11:20:09
	 * @功能描述: 根据参数查询资金池
	 * @参数描述:
	 */
	public List<FundPoolModel> getFundPoolByParam(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-16 上午11:38:50
	 * @功能描述: 根据参数查询资金池[总数]
	 * @参数描述:
	 */
	public Integer getFundPoolByParamCount(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-16 下午2:09:08
	 * @功能描述: 新增机构资金池
	 * @参数描述:
	 */
	public boolean addFundPool(FundPoolModel fundPoolModel);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-16 下午2:09:08
	 * @功能描述: 更新机构资金池（充值）
	 * @参数描述:
	 */
	public boolean updateFundPool(FundPoolModel fundPoolModel);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-16 下午2:40:48
	 * @功能描述: 根据编号查询资金池（判断编号是否已存在）
	 * @参数描述:
	 */
	public List<FundPoolModel> getFundPoolByNum(String organiztion_num);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-16 下午2:40:48
	 * @功能描述: 根据ID查询资金池
	 * @参数描述:
	 */
	public List<FundPoolModel> getFundPoolById(String id);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-16 下午3:08:54
	 * @功能描述: 冻结资金池
	 * @参数描述:
	 */
	public boolean freezeFundPool(String ids);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-16 下午3:08:54
	 * @功能描述: 解冻资金池
	 * @参数描述:
	 */
	public boolean unfreezeFundPool(String ids);
	
	
}
