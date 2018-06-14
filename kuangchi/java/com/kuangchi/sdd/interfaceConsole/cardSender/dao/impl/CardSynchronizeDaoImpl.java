package com.kuangchi.sdd.interfaceConsole.cardSender.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.interfaceConsole.cardSender.dao.CardSynchronizeDao;
import com.kuangchi.sdd.interfaceConsole.cardSender.model.CardSyncModel;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.DeptGw;

@Repository("cardSynchronizeDaoImpl")
public class CardSynchronizeDaoImpl extends BaseDaoImpl<DeptGw> implements
		CardSynchronizeDao {

	@Override
	public List<CardSyncModel> queryBoundCardInfo() {

		return getSqlMapClientTemplate().queryForList("queryBoundCardInfo", 1);

	}

	@Override
	public String getNameSpace() {

		return "common.CardSynchronize";
	}

	@Override
	public String getTableName() {

		return null;
	}

	@Override
	public Integer getCardCount(String cardNum) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"getCardCount", cardNum);
	}

	@Override
	public List<CardSyncModel> queryIDByCardNum(String cardNum) {
		return getSqlMapClientTemplate().queryForList("queryIDByCardNum",
				cardNum);
	}

	@Override
	public String getCardNumByBoundID(String boundCardId) {
		return (String) getSqlMapClientTemplate().queryForObject(
				"getCardNumByBoundID", boundCardId);
	}

}
