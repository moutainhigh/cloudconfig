package com.kuangchi.sdd.consumeConsole.goodType.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.goodType.dao.IGoodTypeDao;
import com.kuangchi.sdd.consumeConsole.goodType.model.GoodType;
import com.kuangchi.sdd.consumeConsole.goodType.model.PriceHistoryModel;

@Repository("goodTypeDaoImpl")
public class GoodTypeDaoImpl extends BaseDaoImpl<GoodType> implements IGoodTypeDao {

	@Override
	public String getNameSpace() {
		return "common.GoodType";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<GoodType> getGoodtypeInfoByParam(Map<String, String> map) {
		int page = Integer.valueOf(map.get("page"));
		int rows = Integer.valueOf(map.get("rows"));
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("page", (page - 1) * rows);
		mapParam.put("rows", rows);
		mapParam.put("type_name", map.get("type_name"));
		mapParam.put("discount_num", map.get("discount_num"));
		return this.getSqlMapClientTemplate().queryForList("getGoodtypeInfoByParam", mapParam);
	}

	@Override
	public GoodType selectTypeByNum(String type_num) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type_num", type_num);
		return (GoodType) this.getSqlMapClientTemplate().queryForObject("selectTypeByNum",map);
	}

	@Override
	public boolean insertNewGoodype(GoodType goodType) {
		if(getSqlMapClientTemplate().insert("addNewGoodtype", goodType)!=null){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean modifyGoodType(GoodType goodType) {
		Object o=this.getSqlMapClientTemplate().update("modifyGoodType", goodType);
		if(o!=null){
			return true;
		}else{
			
			return false;
		}
	}

	@Override
	public boolean delGoodtypes(String num) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type_num", num);
		Integer i=(Integer)this.getSqlMapClientTemplate().update("delGoodtypes", map);
		if(i>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Integer getGoodTypeInfoCount(Map<String, String> map) {
		return queryCount("getGoodTypeInfoCount",map);
	}

	@Override
	public Integer selectGoodByNum(String num) {
		Map<String, String> map = new HashMap<String,String>();
		map.put("type_num", num);
		return queryCount("selectGoodByNum",map);
	}

	@Override
	public List<GoodType> getAllGoodType() {
		return this.getSqlMapClientTemplate().queryForList("getAllGoodType");
	}

	@Override
	public GoodType getGoodTypeByNum(String type_num) {
		return (GoodType) this.getSqlMapClientTemplate().queryForObject("getGoodTypeByNum", type_num);
	}

	@Override
	public Integer selectDeviceByTypeNum(String typeNum) {
		Map<String, String> map = new HashMap<String,String>();
		map.put("type_num", typeNum);
		return queryCount("selectDeviceByTypeNum",map);
	}

	@Override
	public void insertNewPriceHistory(PriceHistoryModel priceHistoryModel) {
		getSqlMapClientTemplate().update("insertNewPriceHistory", priceHistoryModel);
		
	}

	@Override
	public List<PriceHistoryModel> getPriceHistoryList(Map<String, Object> map) {
		return getSqlMapClientTemplate().queryForList("getPriceHistoryList", map);
	}

	@Override
	public Integer getPriceHistoryCount(Map<String, Object> map) {
		return queryCount("getPriceHistoryCount",map);
	}

	@Override
	public String validPrice(String type_num) {
		return (String) getSqlMapClientTemplate().queryForObject("validPrice", type_num);
	}

}
