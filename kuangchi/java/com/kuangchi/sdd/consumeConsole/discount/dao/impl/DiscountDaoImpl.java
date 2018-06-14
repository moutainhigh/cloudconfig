package com.kuangchi.sdd.consumeConsole.discount.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.doorinfo.dao.IDoorInfoDao;
import com.kuangchi.sdd.baseConsole.doorinfo.model.DoorInfoModel;
import com.kuangchi.sdd.consumeConsole.discount.dao.IDiscountDao;
import com.kuangchi.sdd.consumeConsole.discount.model.Discount;
@Repository("DiscountDaoImpl")
public class DiscountDaoImpl extends BaseDaoImpl<Discount> implements IDiscountDao {

	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 新增折扣信息
	 */
	@Override
	public Boolean insertDiscount(Discount discount_info) {
		Object obj=this.getSqlMapClientTemplate().insert("insertDiscount", discount_info);
		if(obj==null){
			return true;
		}
		return false;
	}
	
	/**
	 * 修改折扣信息
	 */
	@Override
	public Boolean updateDiscount(Discount discount_info) {
		Object obj=this.getSqlMapClientTemplate().update("updateDiscount", discount_info);
		if(obj==null){
			return false;
		}
		return true;
	}
	
	/**
	 * 查询所有折扣信息
	 */
	@Override
	public List<Discount> selectAllDiscounts(Discount discount_info,
			String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("page", (page-1)*rows);
		mapState.put("rows", rows);
		mapState.put("discount_name",discount_info.getDiscount_name());
		mapState.put("start_date", discount_info.getStart_date());
		mapState.put("end_date", discount_info.getEnd_date());
		return this.getSqlMapClientTemplate().queryForList("selectAllDiscounts", mapState);
	}
	
	/**
	 * 查询总条数
	 */
	@Override
	public Integer getAllDiscountCount(Discount discount_info) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("discount_name",discount_info.getDiscount_name());
		mapState.put("start_date", discount_info.getStart_date());
		mapState.put("end_date", discount_info.getEnd_date());
		return queryCount("getAllDiscountCount",mapState);
	}
	
	/**
	 * 根据折扣代码查询
	 */
	@Override
	public List<Discount> selectDiscountById(Integer id) {
		return this.getSqlMapClientTemplate().queryForList("selectDiscountById",id);
		
	}
	
	/**
	 * 删除折扣信息
	 */
	@Override
	public Integer deleteDiscount(String id) {
		Integer obj=(Integer)this.getSqlMapClientTemplate().update("deleteDiscount", id);
		if(obj==0){
			return 0;
		}
		return 1;
	}
	
	/**
	 * 根据折扣代码查询折扣信息
	 */
	@Override
	public List<Discount> selectDiscountByNum(Discount discount_info) {
		return this.getSqlMapClientTemplate().queryForList("selectDiscountByNum",discount_info);
	}
	
	/**
	 * 商品表中是否有折扣代码
	 */
	@Override
	public Integer selectGoodByNumCount(String discount_num) {
		return queryCount("selectGoodByNumCount",discount_num);
	}

	@Override
	public Integer selectGoodTypeByNumCount(String discount_num) {
		return queryCount("selectGoodTypeByNumCount",discount_num);
	}
	
	
}
