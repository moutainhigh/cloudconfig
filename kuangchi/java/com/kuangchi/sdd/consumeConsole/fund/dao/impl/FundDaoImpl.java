package com.kuangchi.sdd.consumeConsole.fund.dao.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.attendException.dao.impl.AttendExceptionDaoImpl;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.fund.dao.IFundDao;
import com.kuangchi.sdd.consumeConsole.fund.model.FundModel;

@Repository("fundDaoImpl")
public class FundDaoImpl extends BaseDaoImpl<Object> implements IFundDao{

	@Override
	public String getNameSpace() {
		return "common.Fund";
	}

	@Override
	public String getTableName() {
		return null;
	}
	
	@Override
	public boolean addFund(FundModel fundModel) {
		return this.insert("addFund", fundModel);
	}

	@Override
	public List<FundModel> getFundByParamPage(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getFundByParamPage", map);
	}

	@Override
	public Integer getFundByParamCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getFundByParamCount", map);
	}

}
