package com.kuangchi.sdd.consumeConsole.discount.dao;


import java.util.List;

import com.kuangchi.sdd.consumeConsole.discount.model.Discount;
public interface IDiscountDao {
	public Boolean insertDiscount(Discount discount_info);//新增折扣信息

	public Boolean updateDiscount(Discount discount_info);//修改折扣信息
	
	public List<Discount> selectDiscountById(Integer id);//通过ID查信息
	
	public List<Discount> selectAllDiscounts(Discount discount_info,String page, String size);//模糊查询卡片所有信息
	
	public Integer getAllDiscountCount(Discount discount_info);//查询总的行数
	
	public Integer deleteDiscount(String id);//删除折扣信息
	
	public Integer selectGoodByNumCount(String discount_num);//商品表中是否有折扣代码
	
	public List<Discount> selectDiscountByNum(Discount discount_info);//通过折扣代码查信息
	
	public Integer selectGoodTypeByNumCount(String discount_num);//商品类别表中是否有折扣代码
	
}
