package com.kuangchi.sdd.consumeConsole.vendor.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.vendor.dao.IVendorDao;
import com.kuangchi.sdd.consumeConsole.vendor.model.Vendor;
import com.kuangchi.sdd.consumeConsole.vendorType.model.VendorType;
@Repository("vendorDaoImpl")
public class VendorDaoImpl extends BaseDaoImpl<Vendor> implements IVendorDao {

	@Override
	public String getNameSpace() {
		return "common.Vendor";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<VendorType> getVendorType() {
		return getSqlMapClientTemplate().queryForList("getVendorTypes");
	}

	@Override
	public List<Vendor> getVendorInfoByParam(Vendor vendor,String Page, String Rows) {
		int page=Integer.valueOf(Page);
		int rows=Integer.valueOf(Rows);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("page", (page-1)*rows);
		map.put("rows", rows);
		map.put("vendor_num", vendor.getVendor_num());
		map.put("vendor_name", vendor.getVendor_name());
		map.put("vendor_type_num", vendor.getVendor_type_num());
		map.put("vendor_dealer_name", vendor.getVendor_dealer_name());
		return getSqlMapClientTemplate().queryForList("selectAllVendorinfos", map);
	}

	@Override
	public Integer getVendorInfoCount(Vendor vendor) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("vendor_num", vendor.getVendor_num());
		map.put("vendor_name", vendor.getVendor_name());
		map.put("vendor_type_num", vendor.getVendor_type_num());
		map.put("vendor_dealer_name", vendor.getVendor_dealer_name());
		return queryCount("getVendorInfoCount",map);
	}

	@Override
	public List<Vendor> getAllVendor() {
		return getSqlMapClientTemplate().queryForList("getAllVendorList");
	}

	@Override
	public Vendor selectVendorByNum(String vendor_num) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("vendor_num", vendor_num);
		return (Vendor) getSqlMapClientTemplate().queryForObject("selectVendornum", map);
	}

	@Override
	public boolean insertNewVendor(Vendor vendor) {
		if(getSqlMapClientTemplate().insert("insertNewVendor", vendor)!=null){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean updateVendor(Vendor vendor) {
		if(getSqlMapClientTemplate().update("updateVendor", vendor)==1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Vendor selectVendorInfoByNum(String vendor_num) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("vendor_num", vendor_num);
		return (Vendor) getSqlMapClientTemplate().queryForObject("selectVendorInfoByNum", map);
	}

	@Override
	public Integer selectDeviceByVendorNum(String vendor_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("vendor_num", vendor_num);
		return queryCount("selectDeviceByVendorNum",map);
	}

	@Override
	public boolean delVendor(String vendor_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("vendor_num", vendor_num);
		Integer count=getSqlMapClientTemplate().update("delVendor", map);
		if(count==1){
			return true;
		}else{
			return false;
		}
	}

}
