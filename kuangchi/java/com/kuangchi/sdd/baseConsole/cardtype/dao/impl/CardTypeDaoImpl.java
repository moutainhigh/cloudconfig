package com.kuangchi.sdd.baseConsole.cardtype.dao.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.cardtype.dao.ICardTypeDao;
import com.kuangchi.sdd.baseConsole.cardtype.model.CardType;
import com.kuangchi.sdd.baseConsole.cardtype.model.ConModel;
/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-3-30下午6:13:19
 * @功能描述:	卡片类型管理-Dao实现类
 * @参数描述:
 */
@Repository("cardTypeDaoImpl")
public class CardTypeDaoImpl extends BaseDaoImpl<CardType> implements ICardTypeDao {

	@Override
	public void addCardType(CardType cardType) {
		this.getSqlMapClientTemplate().insert("addCardType",cardType);
	}


	@Override
	public void delCardType(String ct_id) {
		/*Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("ct_id", ct_id);*/
		this.getSqlMapClientTemplate().update("delCardType", ct_id);
	}

	@Override
	public void updateCardType(CardType cardType) {
		this.getSqlMapClientTemplate().update("updateCardType", cardType);
	}

	@Override
	public CardType getCTById(String ct_id) {
		/*Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("ct_id", ct_id);*/
		return (CardType) this.getSqlMapClientTemplate().queryForObject("getCTById", ct_id);
	}

	@Override
	public List<CardType> getAllCT(ConModel conModel) {
		
		return this.getSqlMapClientTemplate().queryForList("getAllCT",conModel);
	}

	@Override
	public List<CardType> getCTByParam(ConModel conModel) {
		return this.queryForList("getCTByParam", conModel);
	}

	@Override
	public String getNameSpace() {
		return "common.CardType";
	}
	
	@Override
	public String getTableName() {
		return null;
	}


	@Override
	public Integer getCount(ConModel conModel) {
		return queryCount("getCount",conModel);
	}


	@Override
	public CardType getByName(String name) {
		return (CardType) this.getSqlMapClientTemplate().queryForObject("getByName",name);
	}


	@Override
	public List<CardType> getListCardInfoByDM(String ct_id) {
		return this.queryForList("getListCardInfoByDM", ct_id);
	}
}
