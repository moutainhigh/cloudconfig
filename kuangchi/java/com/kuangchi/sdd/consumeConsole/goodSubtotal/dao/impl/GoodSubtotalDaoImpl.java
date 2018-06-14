package com.kuangchi.sdd.consumeConsole.goodSubtotal.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.goodSubtotal.dao.IGoodSubtotalDao;
import com.kuangchi.sdd.consumeConsole.goodSubtotal.model.GoodSubtotal;

/**
 * 商品清算DaoImpl
 * @author minting.he
 *
 */
@Repository("goodSubtotalDao")
public class GoodSubtotalDaoImpl extends BaseDaoImpl<GoodSubtotal> implements IGoodSubtotalDao {

	@Override
	public String getNameSpace() {
		return "common.GoodSubtotal";
	}

	@Override
	public String getTableName() {
		return null;
	}
	
	@Override
	public List<GoodSubtotal> getSubtotalByParam(String vendor_name,
			String start_date, String end_date, Integer skip, Integer rows) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("vendor_name", vendor_name);
		map.put("start_date", start_date);
		map.put("end_date", end_date);
		map.put("skip", skip);
		map.put("rows", rows);
		return getSqlMapClientTemplate().queryForList("getSubtotalByParam", map);
	}

	@Override
	public Integer getSubtotalByParamCount(String vendor_name,
			String start_date, String end_date) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("vendor_name", vendor_name);
		map.put("start_date", start_date);
		map.put("end_date", end_date);
		return (Integer) getSqlMapClientTemplate().queryForObject("getSubtotalByParamCount", map);
	}

	@Override
	public List<GoodSubtotal> exportAllGoodSubtotal() {
		return this.getSqlMapClientTemplate().queryForList("exportAllGoodSubtotal");
	}

	@Override
	public List<Map> getVendorSubInfo(){
		return this.getSqlMapClientTemplate().queryForList("getVendorSubInfo");
	}
	
	@Override
	public List<Map> getConsumeByVendor(Map map){
		return this.getSqlMapClientTemplate().queryForList("getConsumeByVendor", map);
	}
	
	@Override
	public boolean insertVendorSub(GoodSubtotal total){
		return insert("insertVendorSub", total);
	}
	
	@Override
	public boolean updateVendorPreTime(Map map){
		return update("updateVendorPreTime", map);
	}

}
