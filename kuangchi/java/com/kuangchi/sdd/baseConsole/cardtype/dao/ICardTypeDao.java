package com.kuangchi.sdd.baseConsole.cardtype.dao;

import java.util.List;
import com.kuangchi.sdd.baseConsole.cardtype.model.CardType;
import com.kuangchi.sdd.baseConsole.cardtype.model.ConModel;
/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-3-30下午6:11:49
 * @功能描述:卡片类型管理-Dao
 * @参数描述:
 */
public interface ICardTypeDao {
		
		public void addCardType(CardType cardType);
		
		
		public void delCardType(String ct_id);
			
		
		public void updateCardType(CardType cardType);
		
		
		public CardType getCTById(String ct_id);
		
		List<CardType> getAllCT(ConModel conModel);
		
		
		List<CardType> getCTByParam(ConModel conModel);
		
		
		public Integer getCount(ConModel conModel);
		
		
		public CardType getByName(String name);
		
		//删除前先查询是否被引用
		public List<CardType> getListCardInfoByDM(String ct_id);
}
