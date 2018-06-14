package com.kuangchi.sdd.consumeConsole.goodSubtotal.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.goodSubtotal.model.GoodSubtotal;

/**
 * 商品清算IService
 * @author minting.he
 *
 */
public interface IGoodSubtotalService {

	/**
	 * 按条件查询清算信息
	 * @param vendor_name
	 * @param start_date
	 * @param end_date
	 * @param skip
	 * @param rows
	 * @return
	 */
	public Grid<GoodSubtotal> getGoodSubtotalByParam(String vendor_name, String start_date, String end_date, Integer skip, Integer rows);
	
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
	 * 商户清算
	 * @param vendor_num
	 * @param previous_time
	 * @param date
	 * @return
	 */
	public void vendorSub(String vendor_num, String previous_time, Date date);
	
	/**
	 * 手动清算商户
	 * @return
	 */
	public boolean manualSubVendor(String login_user);
}
