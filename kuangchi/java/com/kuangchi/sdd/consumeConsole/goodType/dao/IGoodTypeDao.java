package com.kuangchi.sdd.consumeConsole.goodType.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.goodType.model.GoodType;
import com.kuangchi.sdd.consumeConsole.goodType.model.PriceHistoryModel;

public interface IGoodTypeDao {

	List<GoodType> getGoodtypeInfoByParam(Map<String, String> map);//模糊查询商品类别

	GoodType selectTypeByNum(String type_num);//按编号查询类别是否存在

	boolean insertNewGoodype(GoodType goodType);//新增商品类别

	boolean modifyGoodType(GoodType goodType);//修改商品类别

	boolean delGoodtypes(String nums);//删除商品类别

	Integer getGoodTypeInfoCount(Map<String, String> map);//商品类别总数

	Integer selectGoodByNum(String num);//根据编号查询商品表中是否存在记录

	List<GoodType> getAllGoodType();
	
	GoodType getGoodTypeByNum(String type_num);

	Integer selectDeviceByTypeNum(String typeNum);//根据类别编号查询设备

	void insertNewPriceHistory(PriceHistoryModel priceHistoryModel);//新增历史单价

	List<PriceHistoryModel> getPriceHistoryList(Map<String, Object> map);//查询历史价格

	Integer getPriceHistoryCount(Map<String, Object> map);//查询历史价格记录总数
	
	/**
	 * 查询商品类型是否有单价
	 * @author minting.he
	 * @param type_num
	 * @return
	 */
	public String validPrice(String type_num);
}
