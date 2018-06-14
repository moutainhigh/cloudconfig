package com.kuangchi.sdd.businessConsole.randomCode.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.businessConsole.randomCode.dao.RandomCodeDao;
import com.kuangchi.sdd.businessConsole.randomCode.model.RandomCode;

@Repository("randomCodeDaoImpl")
public class RandomCodeDaoImpl extends BaseDaoImpl<Object> implements RandomCodeDao{

	@Override
	public boolean insertRandCodeInfo(Map map) {
		return insert("insertRandCodeInfo",map);
	}
	
	public List<RandomCode> getRandCodeByUserMail(String staffMail){
		return   getSqlMapClientTemplate().queryForList("getRandCodeByUserMail",staffMail);
		
	}

	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public Integer getCountByRandCode(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getCountByRandCode", map);
	}
	
}
