package com.kuangchi.sdd.consumeConsole.nameList.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.cardtype.model.CardType;
import com.kuangchi.sdd.consumeConsole.nameList.model.NameList;

public interface NameListService {
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-5 
	 * @功能描述: 获取下发名单全部记录-Service
	 */
	public Grid<NameList> getNameList(Map map);
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-6 
	 * @功能描述: 获取所有卡类型-Dao
	 */
 	public List<CardType> getAllCardType();
	
}
