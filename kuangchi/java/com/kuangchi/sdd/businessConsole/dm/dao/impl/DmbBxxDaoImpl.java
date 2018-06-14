package com.kuangchi.sdd.businessConsole.dm.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.businessConsole.dm.dao.IDmbBxxDao;
import com.kuangchi.sdd.businessConsole.dm.model.DmbBxx;
import com.kuangchi.sdd.businessConsole.dm.model.DmbLxx;


@Repository("dmbBxxDao")
public class DmbBxxDaoImpl extends BaseDaoImpl<DmbBxx> implements IDmbBxxDao {

	@Override
	public String getNameSpace() {
		
		return "xt.dmbbxx";
	}

	@Override
	public String getTableName() {
	
		return null;
	}

	@Override
	public List<DmbBxx> selectDmbBxx(DmbBxx dmbBxx) {
		return getSqlMapClientTemplate().queryForList("selectDmbBxx", dmbBxx, dmbBxx.getSkip(), dmbBxx.getRows());
	}

	@Override
	public int countDmbBxx(DmbBxx dmbBxx) {	
		return queryCount("countDmbBxx", dmbBxx);
	}

	@Override
	public int isExistDmbBmc(String dmbBmcFull) {	
		return queryCount("isExistDmbBmc", dmbBmcFull);
	}

	@Override
	public void addDmbBxx(DmbBxx dmbBxx) {
		
		getSqlMapClientTemplate().insert("addDmbBxx", dmbBxx);
		
	}

	@Override
	public void addDmblxx(List<DmbLxx> dmlList) {
		getSqlMapClientTemplate().insert("addDmblxx", dmlList);
		
	}

	@Override
	public void createTableDmb(String sql) {
		getSqlMapClientTemplate().update("createTableDmb", sql);
		
	}

}
