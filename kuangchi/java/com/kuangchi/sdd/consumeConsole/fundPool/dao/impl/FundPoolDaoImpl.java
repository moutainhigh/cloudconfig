package com.kuangchi.sdd.consumeConsole.fundPool.dao.impl;



import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.fund.model.FundModel;
import com.kuangchi.sdd.consumeConsole.fundPool.dao.IFundPoolDao;
import com.kuangchi.sdd.consumeConsole.fundPool.model.FundPoolModel;

@Repository("fundPoolDaoImpl")
public class FundPoolDaoImpl extends BaseDaoImpl<Object> implements IFundPoolDao{

	@Override
	public String getNameSpace() {
		return "common.FundPool";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<FundPoolModel> getFundPoolByParamPage(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getFundPoolByParamPage", map);
	}

	@Override
	public List<FundPoolModel> getFundPoolByParam(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getFundPoolByParam", map);
	}
	
	public Integer getFundPoolByParamCount(Map<String, Object> map){
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getFundPoolByParamCount", map);
	}

	@Override
	public boolean addFundPool(FundPoolModel fundPoolModel) {
		return this.insert("addFundPool", fundPoolModel);
	}

	@Override
	public List<FundPoolModel> getFundPoolByNum(String organiztion_num) {
		return this.getSqlMapClientTemplate().queryForList("getFundPoolByNum", organiztion_num);
	}

	@Override
	public boolean freezeFundPool(String ids) {
		return this.update("freezeFundPool", ids);
	}

	@Override
	public boolean unfreezeFundPool(String ids) {
		return this.update("unfreezeFundPool", ids);
	}

	@Override
	public List<FundPoolModel> getFundPoolById(String id) {
		return this.getSqlMapClientTemplate().queryForList("getFundPoolById", id);
	}

	@Override
	public boolean updateFundPool(FundPoolModel fundPoolModel) {
		return this.update("updateFundPool", fundPoolModel);
	}


}
