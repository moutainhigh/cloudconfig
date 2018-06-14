package com.kuangchi.sdd.consumeConsole.vendorType.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.vendorType.dao.IVendorTypeDao;
import com.kuangchi.sdd.consumeConsole.vendorType.model.VendorType;
@Repository("vendorTypeDaoImpl")
public class VendorTypeDaoImpl extends BaseDaoImpl<VendorType> implements IVendorTypeDao {
	@Override
	public String getNameSpace() {
		return "common.VendorType";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<VendorType> getVendorTypeInfoByParam(Map<String, String> map) {
		Integer page=Integer.valueOf(map.get("page"));
		Integer rows=Integer.valueOf(map.get("rows"));
		Map<String,Object> mapParam=new HashMap<String,Object>();
		mapParam.put("page", (page-1)*rows);
		mapParam.put("rows", rows);
		mapParam.put("vendor_type_num", map.get("vendor_type_num"));
		mapParam.put("vendor_type_name", map.get("vendor_type_name"));
		return getSqlMapClientTemplate().queryForList("getVendortypeInfoByParam", mapParam);
	}

	@Override
	public Integer getVendorTypeInfoCount(Map<String, String> map) {
		return queryCount("getVendorTypeInfoCount",map);
	}

	@Override
	public VendorType selectVendortypeByNum(String vendor_type_num) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("vendor_type_num", vendor_type_num);
		return (VendorType) this.getSqlMapClientTemplate().queryForObject("selectVendortypeByNum",map);
	}

	@Override
	public boolean insertNewVendortype(VendorType vendorType) {
		if(getSqlMapClientTemplate().insert("addNewVendortype", vendorType)!=null){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean updateVendorType(VendorType vendorType) {
		if(getSqlMapClientTemplate().update("updateVendorType", vendorType)>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Integer selectVendorByNum(String vendor_type_num) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("vendor_type_num", vendor_type_num);
		return queryCount("selectVendorByNum",map);
	}

	@Override
	public boolean delVendorType(String num) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vendor_type_num", num);
		Integer i=(Integer)this.getSqlMapClientTemplate().update("delVendortypes", map);
		if(i>0){
			return true;
		}else{
			return false;
		}
	}

	
}
