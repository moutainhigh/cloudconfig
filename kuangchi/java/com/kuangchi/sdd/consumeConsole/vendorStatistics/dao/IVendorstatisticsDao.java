package com.kuangchi.sdd.consumeConsole.vendorStatistics.dao;


import java.util.List;

import com.kuangchi.sdd.consumeConsole.vendorStatistics.model.VendorStatistics;



public interface IVendorstatisticsDao {
	
	public List<VendorStatistics> selectAllVendorStatistics(VendorStatistics vendor_record,String page, String size);//模糊查询部门收支信息
	
	public Integer getAllVendorStatisticsCount(VendorStatistics vendor_record);//查询总的行数
	
	public List<VendorStatistics> exportAllVendorstatistics(VendorStatistics vendor_record);//导出部门收支信息
	
	public List<VendorStatistics> selectByVendor();//查询所有商户编号
	
	public List<VendorStatistics> selectByVendors(String Page, String size);//查询所有商户编号带分页
	
	public List<VendorStatistics> selectVendorStatistics(VendorStatistics vendor_record);//查询部门收支报表信息,String page, String size
	
	public Integer selectVendorStatisticsCount(VendorStatistics vendor_record);//查询总的行数
	
	public List<VendorStatistics> ExportVendorStatistics(VendorStatistics vendor_record);//导出部门报表汇总
}
