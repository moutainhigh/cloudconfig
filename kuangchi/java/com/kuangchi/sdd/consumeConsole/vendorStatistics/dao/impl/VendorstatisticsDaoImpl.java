package com.kuangchi.sdd.consumeConsole.vendorStatistics.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.vendorStatistics.dao.IVendorstatisticsDao;
import com.kuangchi.sdd.consumeConsole.vendorStatistics.model.VendorStatistics;


@Repository("vendorstatisticsDaoImpl")
public class VendorstatisticsDaoImpl extends BaseDaoImpl<VendorStatistics> implements IVendorstatisticsDao {

	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	

	/**
	 * 查询所有承包商消费信息
	 * guibo.chen
	 */
	@Override
	public List<VendorStatistics> selectAllVendorStatistics(
			VendorStatistics vendor_record, String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("page", (page-1)*rows);
		mapState.put("rows", rows);
		mapState.put("vendor_num", vendor_record.getVendor_num());
		mapState.put("vendor_name", vendor_record.getVendor_name());
		mapState.put("begin_time", vendor_record.getBegin_time());
		mapState.put("end_time", vendor_record.getEnd_time());
		return this.getSqlMapClientTemplate().queryForList("selectAllVendorStatistics", mapState);
	}
	/**
	 * 查询总条数
	 * guibo.chen
	 */
	@Override
	public Integer getAllVendorStatisticsCount(VendorStatistics vendor_record) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("vendor_num", vendor_record.getVendor_num());
		mapState.put("vendor_name", vendor_record.getVendor_name());
		mapState.put("begin_time", vendor_record.getBegin_time());
		mapState.put("end_time", vendor_record.getEnd_time());
		return queryCount("getAllVendorStatisticsCount",mapState);
	}
	/**
	 * 导出承包商消费信息
	 * guibo.chen
	 */
	@Override
	public List<VendorStatistics> exportAllVendorstatistics(
			VendorStatistics vendor_record) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("vendor_num", vendor_record.getVendor_num());
		mapState.put("vendor_name", vendor_record.getVendor_name());
		mapState.put("begin_time", vendor_record.getBegin_time());
		mapState.put("end_time", vendor_record.getEnd_time());
		return this.getSqlMapClientTemplate().queryForList("exportAllVendorstatistics", mapState);
	}
	
	/**
	 * 查询所有商户编号
	 * guibo.chen
	 */
	@Override
	public List<VendorStatistics> selectByVendor() {
		return this.getSqlMapClientTemplate().queryForList("selectByVendor", "");
	}
	
	/**
	 * 查询所有商户编号带分页
	 * guibo.chen
	 */
	@Override
	public List<VendorStatistics> selectByVendors(String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("page", (page-1)*rows);
		mapState.put("rows", rows);
		return this.getSqlMapClientTemplate().queryForList("selectByVendors", mapState);
	}
	
	
	/**
	 * 查询承包商收支报表信息
	 * guibo.chen
	 */
	@Override
	public List<VendorStatistics> selectVendorStatistics(
			VendorStatistics vendor_record) {
		/*int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);*/
		Map<String, Object> mapState = new HashMap<String, Object>();
	/*	mapState.put("page", (page-1)*rows);
		mapState.put("rows", rows);*/
		mapState.put("vendor_num", vendor_record.getVendor_num());
		//mapState.put("vendor_name", vendor_record.getVendor_name());
		mapState.put("begin_time", vendor_record.getBegin_time());
		mapState.put("end_time", vendor_record.getEnd_time());
		return this.getSqlMapClientTemplate().queryForList("selectVendorStatistics", mapState);
	}

	@Override
	public Integer selectVendorStatisticsCount(VendorStatistics vendor_record) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("vendor_name", vendor_record.getVendor_name());
		mapState.put("begin_time", vendor_record.getBegin_time());
		mapState.put("end_time", vendor_record.getEnd_time());
		return queryCount("selectVendorStatisticsCount",mapState);
	}
	
	/**
	 * 导出部门信息报表汇总
	 * guibo.chen
	 */
	@Override
	public List<VendorStatistics> ExportVendorStatistics(
			VendorStatistics vendor_record) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("vendor_name", vendor_record.getVendor_name());
		mapState.put("begin_time", vendor_record.getBegin_time());
		mapState.put("end_time", vendor_record.getEnd_time());
		return this.getSqlMapClientTemplate().queryForList("ExportVendorStatistics", mapState);
		
	}
	
	
	
	
}
