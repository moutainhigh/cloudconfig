package com.kuangchi.sdd.consumeConsole.deptStatistics.service.impl;


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

import com.kuangchi.sdd.consumeConsole.deptStatistics.dao.IDeptstatisticsDao;
import com.kuangchi.sdd.consumeConsole.deptStatistics.model.DeptStatistics;
import com.kuangchi.sdd.consumeConsole.deptStatistics.service.IDeptstatisticsService;

@Transactional
@Service("deptstatisticsServiceImpl")
public class DeptstatisticsServiceImpl  implements IDeptstatisticsService {
	private static final int CARD_ID_LENGTH = 6;
	
	@Resource(name = "deptstatisticsDaoImpl")
	private IDeptstatisticsDao deptstatisticsDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	


	/**
	 * 查询部门收支信息
	 * guibo.chen
	 */
	@Override
	public Grid selectAllDeptStatistics(DeptStatistics dept_record,
			String page, String size) {
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
		Integer count=deptstatisticsDao.getAllDeptStatisticsCount(dept_record);
		List<DeptStatistics> deptInfo=deptstatisticsDao.selectAllDeptStatistics(dept_record, page, size);
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(deptInfo);
		return grid;
	}


	/**
	 * 导出部门消费信息
	 */
	@Override
	public List<DeptStatistics> exportAllDeptstatistics(
			DeptStatistics dept_record) {
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
		List<DeptStatistics> deptInfo=deptstatisticsDao.exportAllDeptstatistics(dept_record);
		return deptInfo;
	}


	/**
	 * 查询统计后的部门消费信息
	 * guibo.chen
	 */
	@Override
	public Grid selectDeptStatistics(
			DeptStatistics dept_record) {
		//Integer count=deptstatisticsDao.selectDeptStatisticsCount(dept_record);
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
		List<DeptStatistics> list= new ArrayList<DeptStatistics>();
		DeptStatistics deptInfos=new DeptStatistics();
		try {
			List<DeptStatistics> deptInfo=deptstatisticsDao.selectDeptStatistics(dept_record);
			if(deptInfo.size()!=0){
				for (DeptStatistics deptStatistics : deptInfo) {
					if(deptStatistics.getType().equals("0")){
						double a=deptInfos.getInbound_a();
						deptInfos.setInbound_a(a+deptStatistics.getInbound());
					}else if(deptStatistics.getType().equals("1")){
						double a=deptInfos.getOutbound_a();
						deptInfos.setOutbound_a(a+deptStatistics.getOutbound());
					}else if(deptStatistics.getType().equals("2")){
						double a=deptInfos.getInbound_b();
						deptInfos.setInbound_b(a+deptStatistics.getInbound());
					}else if(deptStatistics.getType().equals("3")){
						double a=deptInfos.getOutbound_b();
						deptInfos.setOutbound_b(a+deptStatistics.getOutbound());
					}else if(deptStatistics.getType().equals("4")){
						double a=deptInfos.getInbound_c();
						deptInfos.setInbound_c(a+deptStatistics.getInbound());
					}else if(deptStatistics.getType().equals("5")){
						double a=deptInfos.getOutbound_c();
						deptInfos.setOutbound_c(a+deptStatistics.getInbound());
					}else if(deptStatistics.getType().equals("6")){
						double a=deptInfos.getInbound_d();
						deptInfos.setInbound_d(a+deptStatistics.getInbound());
					}else if(deptStatistics.getType().equals("7")){
						double a=deptInfos.getOutbound_d();
						deptInfos.setOutbound_d(a+deptStatistics.getOutbound());
					}
					deptInfos.setDept_no(deptStatistics.getDept_no());
					deptInfos.setDept_name(deptStatistics.getDept_name());
				}
				
			}
		} catch (Exception e) {
		}
		if(deptInfos.getDept_no()!=null){
			list.add(deptInfos);
		}
		Grid grid=new Grid();
		grid.setTotal(1);
		grid.setRows(list);
		return grid;
		
	}


	/**
	 * 导出部门报表汇总
	 * guibo.chen
	 */
	@Override
	public List<DeptStatistics> ExportDeptStatistics(
			DeptStatistics dept_record) {
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
		List<DeptStatistics> list= new ArrayList<DeptStatistics>();
		DeptStatistics deptInfos=new DeptStatistics();
		List<DeptStatistics> deptInfo=deptstatisticsDao.selectDeptStatistics(dept_record);
		try {
			if(deptInfo!=null){
				for (DeptStatistics deptStatistics : deptInfo) {
					if(deptStatistics.getType().equals("0")){
						double a=deptInfos.getInbound_a();
						deptInfos.setInbound_a(a+deptStatistics.getInbound());
					}else if(deptStatistics.getType().equals("1")){
						double a=deptInfos.getOutbound_a();
						deptInfos.setOutbound_a(a+deptStatistics.getOutbound());
					}else if(deptStatistics.getType().equals("2")){
						double a=deptInfos.getInbound_b();
						deptInfos.setInbound_b(a+deptStatistics.getInbound());
					}else if(deptStatistics.getType().equals("3")){
						double a=deptInfos.getOutbound_b();
						deptInfos.setOutbound_b(a+deptStatistics.getOutbound());
					}else if(deptStatistics.getType().equals("4")){
						double a=deptInfos.getInbound_c();
						deptInfos.setInbound_c(a+deptStatistics.getInbound());
					}else if(deptStatistics.getType().equals("5")){
						double a=deptInfos.getOutbound_c();
						deptInfos.setOutbound_c(a+deptStatistics.getInbound());
					}
					deptInfos.setDept_no(deptStatistics.getDept_no());
					deptInfos.setDept_name(deptStatistics.getDept_name());
				}
			}
			
		} catch (Exception e) {
		}
		if(deptInfos.getDept_no()!=null){
			list.add(deptInfos);
		}
		return list;
	}

	//查询部门编号和名称
	@Override
	public List<DeptStatistics> selectByDept() {
		List<DeptStatistics> deptInfo=deptstatisticsDao.selectByDept();
		return deptInfo;
	}



	
	
}
