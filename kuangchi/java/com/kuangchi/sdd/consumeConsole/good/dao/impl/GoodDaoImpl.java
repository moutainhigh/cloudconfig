package com.kuangchi.sdd.consumeConsole.good.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.good.dao.IGoodDao;
import com.kuangchi.sdd.consumeConsole.good.model.Discount;
import com.kuangchi.sdd.consumeConsole.good.model.Good;
import com.kuangchi.sdd.consumeConsole.good.model.Vendor;
import com.kuangchi.sdd.consumeConsole.goodType.model.GoodType;
import com.kuangchi.sdd.consumeConsole.goodType.model.PriceHistoryModel;

@Repository("goodDaoImpl")
public class GoodDaoImpl extends BaseDaoImpl<Good> implements IGoodDao {

	@Override
	public List<GoodType> getGoodType() {
		return this.getSqlMapClientTemplate().queryForList("getGoodTypes");
	}

	@Override
	public String getNameSpace() {
		return "common.Good";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Good> getGoodInfoByParam(Map<String, String> map) {
		int page = Integer.valueOf(map.get("page"));
		int rows = Integer.valueOf(map.get("rows"));
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("page", (page - 1) * rows);
		mapParam.put("rows", rows);
		mapParam.put("good_name", map.get("good_name"));
		mapParam.put("good_type_num", map.get("good_type_num"));
		mapParam.put("start_time", map.get("start_time"));
		mapParam.put("end_time", map.get("end_time"));
		
		return this.getSqlMapClientTemplate().queryForList("getGoodInfoByParam", mapParam);
	}

	@Override
	public List<Vendor> getVendornum() {
		return this.getSqlMapClientTemplate().queryForList("getVendornums");
	}

	@Override
	public List<Discount> getDiscountnum() {
		return this.getSqlMapClientTemplate().queryForList("getDiscountnums");
	}

	@Override
	public boolean insertNewGood(Good good) {
		if(getSqlMapClientTemplate().insert("addNewGood", good)!=null){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public Good selectGoodByNum(String good_num) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("good_num", good_num);
		return (Good)this.getSqlMapClientTemplate().queryForObject("isExistGoodnum", map);
	}

	@Override
	public boolean modifyGood(Good good) {
		Object o=this.getSqlMapClientTemplate().update("modifyGood", good);
		if(o==null){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean delGoods(String num) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("good_num", num);
		Integer i=(Integer)this.getSqlMapClientTemplate().update("delGoods", mapParam);
		if(i>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Good selectGoodInfoByNum(String good_num) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("good_num", good_num);
		return (Good) this.getSqlMapClientTemplate().queryForObject("selectGoodInfoByNum", map);
	}

	@Override
	public Integer getGoodInfoCount(Map<String, String> map) {
		return queryCount("getGoodInfoCount",map);
	}

	@Override
	public List<Good> getAllGood() {
		return this.getSqlMapClientTemplate().queryForList("getAllGood");
	}
	
	public Good getGoodByNum(String good_num) {
		return (Good) this.getSqlMapClientTemplate().queryForObject("getGoodByNum", good_num);
	}

	@Override
	public Integer selectDeviceByGoodNum(String good_num) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("good_num", good_num);
		return queryCount("selectDeviceByGoodnum",map);
	}

	@Override
	public void insertPriceHistory(PriceHistoryModel priceHistoryModel) {
		getSqlMapClientTemplate().update("insertPriceHistory", priceHistoryModel);
	}

	@Override
	public List<PriceHistoryModel> getPriceHistoryList(Map<String, Object> map) {
		return getSqlMapClientTemplate().queryForList("getPriceHistory", map);
	}

	@Override
	public Integer getPriceHistoryCount(Map<String, Object> map) {
		return queryCount("getPriceHistoryListCount",map);
	}

	@Override
	public Discount selectAvailableTimeByNum(String discount_num) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("discount_num", discount_num);
		return (Discount) getSqlMapClientTemplate().queryForObject("selectAvailableTimeByNum", map);
	}
	
	@Override
	public List<Map<String, Object>> getGoodByVendor(String vendor_num){
		return getSqlMapClientTemplate().queryForList("getGoodByVendor", vendor_num);
	}

}
