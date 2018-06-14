package com.kuangchi.sdd.consumeConsole.vendorStatistics.service.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;


import com.kuangchi.sdd.consumeConsole.vendorStatistics.dao.IVendorstatisticsDao;
import com.kuangchi.sdd.consumeConsole.vendorStatistics.model.VendorStatistics;
import com.kuangchi.sdd.consumeConsole.vendorStatistics.service.IVendorstatisticsService;

@Transactional
@Service("vendorstatisticsServiceImpl")
public class VendorstatisticsServiceImpl  implements IVendorstatisticsService {
	private static final int CARD_ID_LENGTH = 6;
	
	@Resource(name = "vendorstatisticsDaoImpl")
	private IVendorstatisticsDao vendorstatisticsDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	


	/**
	 * 查询承包商收支信息
	 * guibo.chen
	 */
	@Override
	public Grid selectAllVendorStatistics(VendorStatistics vendor_record,
			String page, String size) {
		if(vendor_record.getEnd_time()!=null){
			SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date=format.parse(vendor_record.getEnd_time());
				date.setHours(date.getHours()+24);
				vendor_record.setEnd_time(format.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Integer count=vendorstatisticsDao.getAllVendorStatisticsCount(vendor_record);
		List<VendorStatistics> vendorInfo=vendorstatisticsDao.selectAllVendorStatistics(vendor_record, page, size);
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(vendorInfo);
		return grid;
	}


	/**
	 * 导出承包商消费信息
	 */
	@Override
	public List<VendorStatistics> exportAllVendorstatistics(
			VendorStatistics vendor_record) {
		if(vendor_record.getEnd_time()!=null){
			SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date=format.parse(vendor_record.getEnd_time());
				date.setHours(date.getHours()+24);
				vendor_record.setEnd_time(format.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		List<VendorStatistics> vendorInfo=vendorstatisticsDao.exportAllVendorstatistics(vendor_record);
		return vendorInfo;
	}


	/**
	 * 查询统计后的部门消费信息
	 * guibo.chen
	 */
	@Override
	public Grid selectVendorStatistics(
			VendorStatistics dept_record,String Page, String size) {
		if(dept_record.getEnd_time()!=null){
			SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date=format.parse(dept_record.getEnd_time());
				date.setHours(date.getHours()+24);
				dept_record.setEnd_time(format.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if(!"".equals(dept_record.getVendor_num())){
			List<VendorStatistics> vendorInfo=new ArrayList<VendorStatistics>();
			VendorStatistics verdots=new VendorStatistics();
			//Integer count=vendorstatisticsDao.selectVendorStatisticsCount(dept_record);
			List<VendorStatistics> vendor=vendorstatisticsDao.selectVendorStatistics(dept_record);
			try {
				for (VendorStatistics vendorStatistics2 : vendor) {
					 if(vendorStatistics2.getType().equals("1")){
						double a=verdots.getOutbound_a();
						verdots.setOutbound_a(a+vendorStatistics2.getOutbound());
					}else if(vendorStatistics2.getType().equals("2")||vendorStatistics2.getType().equals("4")){
						double a=verdots.getInbound_b();
						verdots.setInbound_b(a+vendorStatistics2.getInbound());
					}
					verdots.setInbound_money(verdots.getInbound_b());
					verdots.setOutbound_money(verdots.getOutbound_a());
					verdots.setVendor_num(vendorStatistics2.getVendor_num());
					verdots.setVendor_name(vendorStatistics2.getVendor_name());
					verdots.setVendor_dealer_name(vendorStatistics2.getVendor_dealer_name());
				}
			} catch (Exception e) {
			}
			if(verdots.getVendor_num()!=null){
				vendorInfo.add(verdots);
			}
			Grid grid=new Grid();
			grid.setTotal(1);
			grid.setRows(vendorInfo);
			return grid;
		}else{
		List<VendorStatistics> vendorInfo=new ArrayList<VendorStatistics>();
		//Integer count=vendorstatisticsDao.selectVendorStatisticsCount(dept_record);
		List<VendorStatistics> vendornum=vendorstatisticsDao.selectByVendors(Page,size);
		//List<VendorStatistics> ven=vendorstatisticsDao.selectByVendor();
		//Integer count=ven.size();
		Integer count=0;
		for (int i=0;i<vendornum.size();i++) {
			VendorStatistics verdots=new VendorStatistics();
			verdots.setVendor_num(vendornum.get(i).getVendor_num());
			verdots.setVendor_name(vendornum.get(i).getVendor_name());
			verdots.setVendor_dealer_name(vendornum.get(i).getVendor_dealer_name());
			dept_record.setVendor_num(vendornum.get(i).getVendor_num());
			List<VendorStatistics> vendorInfos=vendorstatisticsDao.selectVendorStatistics(dept_record);
			if(vendorInfos.size()!=0){
				try {
					for (VendorStatistics vendorStatistics2 : vendorInfos) {
					 if(vendorStatistics2.getType().equals("1")){
							double a=verdots.getOutbound_a();
							verdots.setOutbound_a(a+vendorStatistics2.getOutbound());
						}else if(vendorStatistics2.getType().equals("2")||vendorStatistics2.getType().equals("4")){
							double a=verdots.getInbound_b();
							verdots.setInbound_b(a+vendorStatistics2.getInbound());
						}
						verdots.setInbound_money(verdots.getInbound_b());
						verdots.setOutbound_money(verdots.getOutbound_a());
					}
				} catch (Exception e) {
				}
			}
			
			if(verdots.getVendor_num()!=null){
				vendorInfo.add(verdots);
				 count=vendorInfo.size();
			}
		}
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(vendorInfo);
		return grid;
		}
	}


	/**
	 * 导出承包商报表汇总
	 * guibo.chen
	 */
	@Override
	public List<VendorStatistics> ExportVendorStatistics(
			VendorStatistics dept_record) {
		
		if(dept_record.getEnd_time()!=null){
			SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date=format.parse(dept_record.getEnd_time());
				date.setHours(date.getHours()+24);
				dept_record.setEnd_time(format.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if(!"".equals(dept_record.getVendor_num())){
			List<VendorStatistics> vendorInfo=new ArrayList<VendorStatistics>();
			VendorStatistics verdots=new VendorStatistics();
			//Integer count=vendorstatisticsDao.selectVendorStatisticsCount(dept_record);
			List<VendorStatistics> vendor=vendorstatisticsDao.selectVendorStatistics(dept_record);
			try {
				for (VendorStatistics vendorStatistics2 : vendor) {
					 if(vendorStatistics2.getType().equals("1")){
						double a=verdots.getOutbound_a();
						verdots.setOutbound_a(a+vendorStatistics2.getOutbound());
					}else if(vendorStatistics2.getType().equals("2")||vendorStatistics2.getType().equals("4")){
						double a=verdots.getInbound_b();
						verdots.setInbound_b(a+vendorStatistics2.getInbound());
					}
					verdots.setInbound_money(verdots.getInbound_b());
					verdots.setOutbound_money(verdots.getOutbound_a());
					verdots.setVendor_num(vendorStatistics2.getVendor_num());
					verdots.setVendor_name(vendorStatistics2.getVendor_name());
					verdots.setVendor_dealer_name(vendorStatistics2.getVendor_dealer_name());
				}
			} catch (Exception e) {
			}
			
			if(verdots.getVendor_num()!=null){
				vendorInfo.add(verdots);
			}
			return vendorInfo;
		}else{
		//List<VendorStatistics> vendorInfos=vendorstatisticsDao.selectVendorStatistics(dept_record);
		List<VendorStatistics> vendorInfo=new ArrayList<VendorStatistics>();
		//Integer count=vendorstatisticsDao.selectVendorStatisticsCount(dept_record);
		List<VendorStatistics> vendornum=vendorstatisticsDao.selectByVendor();
		for (int i=0;i<vendornum.size();i++) {
			VendorStatistics verdots=new VendorStatistics();
			verdots.setVendor_num(vendornum.get(i).getVendor_num());
			verdots.setVendor_name(vendornum.get(i).getVendor_name());
			verdots.setVendor_dealer_name(vendornum.get(i).getVendor_dealer_name());
			dept_record.setVendor_num(vendornum.get(i).getVendor_num());
			List<VendorStatistics> vendorInfos=vendorstatisticsDao.selectVendorStatistics(dept_record);
			try {
				for (VendorStatistics vendorStatistics2 : vendorInfos) {
					 if(vendorStatistics2.getType().equals("1")){
						double a=verdots.getOutbound_a();
						verdots.setOutbound_a(a+vendorStatistics2.getOutbound());
					}else if(vendorStatistics2.getType().equals("2")||vendorStatistics2.getType().equals("4")){
						double a=verdots.getInbound_b();
						verdots.setInbound_b(a+vendorStatistics2.getInbound());
					}
					verdots.setInbound_money(verdots.getInbound_b());
					verdots.setOutbound_money(verdots.getOutbound_a());
				}
			} catch (Exception e) {
			}
			if(verdots.getVendor_num()!=null){
				vendorInfo.add(verdots);
			}
		}
		return vendorInfo;
		}
	}


	@Override
	public List<VendorStatistics> selectByVendor() {
		List<VendorStatistics> vendornum=vendorstatisticsDao.selectByVendor();
		return vendornum;
	}



	
	
}
