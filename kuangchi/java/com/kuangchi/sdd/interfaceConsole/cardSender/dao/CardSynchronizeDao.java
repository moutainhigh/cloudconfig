package com.kuangchi.sdd.interfaceConsole.cardSender.dao;

import java.util.List;

import com.kuangchi.sdd.interfaceConsole.cardSender.model.CardSyncModel;

public interface CardSynchronizeDao {
	/**
	 * 获取所有绑卡信息
	 * 
	 * @author xuewen.deng
	 * @return
	 */
	public List<CardSyncModel> queryBoundCardInfo();

	/**
	 * 根据卡号获取卡片条数
	 * 
	 * @author xuewen.deng
	 * @return
	 */
	public Integer getCardCount(String cardNum);

	/**
	 * 根据卡号获取绑卡信息
	 * 
	 * @author xuewen.deng
	 * @return
	 */
	public List<CardSyncModel> queryIDByCardNum(String cardNum);

	/**
	 * 根据人卡绑定id获取卡号
	 * 
	 * @author xuewen.deng
	 */
	public String getCardNumByBoundID(String boundCardId);

}
