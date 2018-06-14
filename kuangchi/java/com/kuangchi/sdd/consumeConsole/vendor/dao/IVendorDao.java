package com.kuangchi.sdd.consumeConsole.vendor.dao;

import java.util.List;

import com.kuangchi.sdd.consumeConsole.vendor.model.Vendor;
import com.kuangchi.sdd.consumeConsole.vendorType.model.VendorType;

public interface IVendorDao {

	List<VendorType> getVendorType();//查询商户类型

	List<Vendor> getVendorInfoByParam(Vendor vendor, String page, String rows);//模糊查询商户信息

	Integer getVendorInfoCount(Vendor vendor);//查询商户信息总数

	List<Vendor> getAllVendor();//无参查询所有商户信息列表

	Vendor selectVendorByNum(String vendor_num);//验证商户编号唯一性

	boolean insertNewVendor(Vendor vendor);//新增商户

	boolean updateVendor(Vendor vendor);//修改商户

	Vendor selectVendorInfoByNum(String vendor_num);//根据商户编号查询信息

	Integer selectDeviceByVendorNum(String vendor_num);//根据商户编号查询设备

	boolean delVendor(String vendor_num);//伪删除商户

}
