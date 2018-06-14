package com.kuangchi.sdd.consumeConsole.vendor.service;

import java.util.List;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.vendor.model.Vendor;
import com.kuangchi.sdd.consumeConsole.vendorType.model.VendorType;

public interface IVendorService {

	List<VendorType> getVendorType();//查询商户类型

	Grid getVendorInfoByParam(Vendor vendor, String page, String rows);//模糊查询

	List<Vendor> getAllVendor();//无参查询所有商户信息列表

	Vendor selectVendorByNum(String vendor_num);//验证商户编号唯一性

	boolean insertNewVendor(Vendor vendor, String create_user);//新增商户

	boolean updateVendor(Vendor vendor, String create_user);//修改商户

	Vendor selectVendorInfoByNum(String vendor_num);//通过商户编号查询信息

	Integer selectDeviceByVendorNum(String vendor_num);//根据商户编号查询设备

	boolean delVendor(String vendor_num, String create_user);//伪删除商户

}
