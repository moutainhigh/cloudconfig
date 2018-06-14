package com.kuangchi.sdd.consumeConsole.goodType.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.goodType.model.GoodType;
import com.kuangchi.sdd.consumeConsole.goodType.model.PriceHistoryModel;

public interface IGoodTypeService {

	Grid getGoodtypeInfoByParam(Map<String, String> map);//模糊查询商品类别

	GoodType selectTypeByNum(String type_num);//按编号查询类别是否存在

	boolean insertNewGoodtype(GoodType goodType, String create_user);//新增商品类别

	boolean modifyGood(GoodType goodType, String create_user);//修改商品类别

	boolean delGoodtypes(String nums, String create_user);//删除商品类别

	Integer selectGoodByNum(String string);//根据类别编号查询商品表中是否存在记录

	List<GoodType> getAllGoodType();
	
	GoodType getGoodTypeByNum(String type_num);

	Integer selectDeviceByTypeNum(String string);//根据类别编号查询设备

	void insertNewPriceHistory(PriceHistoryModel priceHistoryModel);//新增历史单价记录

	Grid getPriceHistoryList(Map<String, Object> map);//查询历史价格
	
	/**
	 * 查询商品类型是否有单价
	 * @author minting.he
	 * @param type_num
	 * @return
	 */
	public String validPrice(String type_num);
}
