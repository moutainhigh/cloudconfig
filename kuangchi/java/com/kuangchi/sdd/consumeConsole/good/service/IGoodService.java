package com.kuangchi.sdd.consumeConsole.good.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.good.model.Discount;
import com.kuangchi.sdd.consumeConsole.good.model.Good;
import com.kuangchi.sdd.consumeConsole.good.model.Vendor;
import com.kuangchi.sdd.consumeConsole.goodType.model.GoodType;
import com.kuangchi.sdd.consumeConsole.goodType.model.PriceHistoryModel;

public interface IGoodService {

	List<GoodType> getGoodType();//查询商品类别

	Grid getGoodInfoByParam(Map<String, String> map);//模糊查询商品信息列表

	List<Vendor> getVendornum();//查询商户名称

	List<Discount> getDiscountnum();//查询折扣信息

	boolean insertNewGood(Good good, String create_user);//新增商品

	Good selectGoodByNum(String good_num);//根据编号查商品

	boolean modifyGood(Good good, String create_user);//修改商品

	boolean delGoods(String ids, String create_user);//删除商品

	Good selectGoodInfoByNum(String good_num);//根据商品编号查询产品详细信息用于查看功能
	
	List<Good> getAllGood();
	
	Good getGoodByNum(String good_num);

	Integer selectDeviceByGoodNum(String string);//根据商品编号查询设备

	void insertPriceHistory(PriceHistoryModel priceHistoryModel);//新增历史单价记录

	Grid getPriceHistoryList(Map<String, Object> map);//查询历史价格

	Discount selectAvailableTimeByNum(String discount_num);//通过折扣代码去查询折扣有效期
	
	/**
	 * 查询商户下的商品
	 * @author minting.he
	 * @param vendor_num
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getGoodByVendor(String vendor_num) throws Exception;
}
