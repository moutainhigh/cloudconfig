package com.kuangchi.sdd.consumeConsole.nameList.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.cardtype.model.CardType;
import com.kuangchi.sdd.consumeConsole.nameList.dao.NameListDao;
import com.kuangchi.sdd.consumeConsole.nameList.model.NameList;
@Repository("nameListDaoImpl")
public class NameListDaoImpl extends BaseDaoImpl<NameList> implements NameListDao {

	@Override
	public List<NameList> getNameList(Map map) {
		return getSqlMapClientTemplate().queryForList("getNameList", map);
	}

	@Override
	public Integer getNameListCount(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getNameListCount", map);
	}

	@Override
	public String getNameSpace() {
		return "common.NameList";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<CardType> getAllCardType() {
		return this.getSqlMapClientTemplate().queryForList("getAllCard_Type");
	}

}
