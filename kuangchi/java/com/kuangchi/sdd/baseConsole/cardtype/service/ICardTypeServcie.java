package com.kuangchi.sdd.baseConsole.cardtype.service;

import java.util.List;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.cardtype.model.CardType;
import com.kuangchi.sdd.baseConsole.cardtype.model.ConModel;


/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-3-30下午6:14:13
 * @功能描述:卡片类型管理-业务接口
 * @参数描述:
 */


public interface ICardTypeServcie {
	/**
	 * Description:新增卡片类型
	 * date:2016年3月24日
	 * @param cardType
	 */
	public void addCardType(CardType cardType);
	
	/**
	 * Description:删除卡片类型
	 * date:2016年3月24日
	 * @param ct_id
	 */
	public void delCardType(String ct_id);
		
	/**
	 * Description:修改卡片类型信息 
	 * date:2016年3月24日
	 * @param ct_id
	 */
	public void updateCardType(CardType cardType);
	
	/**
	 * Description：根据ID查看卡片类型信息
	 * date:2016年3月24日
	 * @param ct_id
	 */
	public CardType getCTById(String ct_id);
	
	/**
	 * Description:获得所有卡片类型的信息
	 * date:2016年3月24日
	 */
	Grid<CardType> getAllCT(ConModel conModel);
	
	/**
	 * Description:根据查询条件查询卡片类型信息
	 * date:2016年3月24日
	 * @param conModel
	 */
	Grid<CardType> getCTByParam(ConModel conModel);
	
	/**
	 * 根据name查询
	 */
	public CardType getByName(String name);
	
	/**删除前先查询是否被引用*/
	public List<CardType> getListCardInfoByDMService(String ct_id);
}
