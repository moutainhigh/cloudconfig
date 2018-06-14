package com.kuangchi.sdd.consumeConsole.vendorType.service;

import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.vendorType.model.VendorType;

public interface IVendorTypeService {
	Grid getVendorTypeInfoByParam(Map<String,String> map);//模糊查询商户类型

	VendorType selectVendortypeByNum(String vendor_type_num);//根据编号查询商户类型

	boolean insertNewVendortype(VendorType vendorType, String create_user);//新增商户类型

	boolean updateVendorType(VendorType vendorType, String create_user);//修改商户类型

	Integer selectVendorByNum(String vendor_type_num);//根据编号查询商户是否存在

	boolean delVendorType(String num, String create_user);//伪删除商户类型
}
