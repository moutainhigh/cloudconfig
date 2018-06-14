package com.kuangchi.sdd.consumeConsole.vendorStatistics.service;


import java.util.List;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.vendorStatistics.model.VendorStatistics;



public interface IVendorstatisticsService {
	
	public Grid selectAllVendorStatistics(VendorStatistics vendor_record,
		String page, String size);//模糊查询承包商消费所有信息
	
	public List<VendorStatistics> exportAllVendorstatistics(VendorStatistics vendor_record);//导出承包商收支信息
	
	public Grid selectVendorStatistics(VendorStatistics vendor_record,String Page, String size);//查询承包商收支报表信息
	
	public List<VendorStatistics> ExportVendorStatistics(VendorStatistics vendor_record);//导出承包商报表汇总
	
	public List<VendorStatistics> selectByVendor();//查询所有商户编号
}
