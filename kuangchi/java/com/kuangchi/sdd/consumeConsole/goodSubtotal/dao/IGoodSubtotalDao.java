package com.kuangchi.sdd.consumeConsole.goodSubtotal.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.goodSubtotal.model.GoodSubtotal;

/**
 * 商品清算IDao
 * @author minting.he
 *
 */
public interface IGoodSubtotalDao {
	
	/**
	 * 按条件查询清算信息
	 * @param vendor_name
	 * @param start_date
	 * @param end_date
	 * @param skip
	 * @param rows
	 * @return
	 */
	public List<GoodSubtotal> getSubtotalByParam(String vendor_name, String start_date, String end_date, Integer skip, Integer rows);
	
	/**
	 * 按条件查询清算信息总数
	 * @param vendor_name
	 * @param start_date
	 * @param end_date
	 * @return
	 */
	public Integer getSubtotalByParamCount(String vendor_name, String start_date, String end_date);
	
	/**
	 * 导出全部清算信息
	 * @return
	 */
	public List<GoodSubtotal> exportAllGoodSubtotal();
	
	/**
	 * 获取全部商户清算信息
	 * @return
	 */
	public List<Map> getVendorSubInfo();
	
	/**
	 * 根据商户获取消费信息
	 * @return
	 */
	public List<Map> getConsumeByVendor(Map map);
	
	/**
	 * 新增商户清算信息
	 * @param total
	 * @return
	 */
	public boolean insertVendorSub(GoodSubtotal total);
	
	/**
	 * 更新商户上次清算时间
	 * @param map
	 * @return
	 */
	public boolean updateVendorPreTime(Map map);
}
