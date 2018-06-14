package com.kuangchi.sdd.consumeConsole.vendorType.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.vendorType.model.VendorType;

public interface IVendorTypeDao {
	List<VendorType> getVendorTypeInfoByParam(Map<String,String> map);//模糊查询商户类型
	Integer getVendorTypeInfoCount(Map<String,String> map);//查询商户类型数量
	VendorType selectVendortypeByNum(String vendor_type_num);//根据编号查询商户类型
	boolean insertNewVendortype(VendorType vendorType);//新增商户类型
	boolean updateVendorType(VendorType vendorType);//修改商户类型
	Integer selectVendorByNum(String vendor_type_num);//根据编号查询商户
	boolean delVendorType(String num);//伪删除商户类型
}
