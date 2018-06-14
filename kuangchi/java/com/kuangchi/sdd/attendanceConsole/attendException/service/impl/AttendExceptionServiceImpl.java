package com.kuangchi.sdd.attendanceConsole.attendException.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.attendanceConsole.attendException.dao.IAttendExceptionDao;
import com.kuangchi.sdd.attendanceConsole.attendException.model.AttendException;
import com.kuangchi.sdd.attendanceConsole.attendException.model.Param;
import com.kuangchi.sdd.attendanceConsole.attendException.model.ToEmailAddr;
import com.kuangchi.sdd.attendanceConsole.attendException.service.IAttendExceptionService;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
@Transactional
@Service("attendExceptionServiceImpl")
public class AttendExceptionServiceImpl implements IAttendExceptionService{
	
	@Resource(name = "attendExceptionDaoImpl")
	private IAttendExceptionDao attendExceptionDao;
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public Grid getAllAttExcpByParam(Param param, String page, String size) {
		Integer count=0;
		List<AttendException> allException1=null;
		List<AttendException> allException2=new ArrayList<AttendException>();//用来保存处理后的结果
		double val;//用来保存处理后的Time_interval的值
		try{
		 count=attendExceptionDao.getAllAttExcCount(param);//去数据库中查符合条件的记录条数
		 allException1 =this.attendExceptionDao.getAllAttExcpByParam(param,page,size);//根据条件查询考勤异常记录
		 for(AttendException exception1:allException1){//将数字转换成对应的用户看的懂的中文显示
			 
			 exception1.getTime_interval();
			 
			/* if("0".equals(exception1.getDeal_state())){
				 exception1.setDeal_state("未处理");
			 }else{
				 exception1.setDeal_state("已处理");
			 }*/
			 val=(Double.valueOf(exception1.getTime_interval()))/60.0;
			 DecimalFormat df = new DecimalFormat("#.00");
					
			 //转换考勤异常时间间隔
			 		if(val==0){
			 			 exception1.setTime_interval("--");
			 		}
			 		else if(val<1){
						exception1.setTime_interval("0"+df.format(val));
					}
					else{
					    exception1.setTime_interval(df.format(val));
					}
					
				//转换为迟到早退或忘记打卡
					if ("1".equals(exception1.getException_type())) {
						exception1.setException_type("迟到");
						exception1.setDuty_type("迟到");
					} else if ("2".equals(exception1.getException_type())) {
						exception1.setException_type("早退");
						exception1.setDuty_type("早退");
					} else  if("3".equals(exception1.getException_type())){
						exception1.setException_type("缺打卡");
						exception1.setDuty_type("旷工");
						
					/*	if(val == 0){
							exception1.setDuty_type("--");
						}*/
					} else  if("4".equals(exception1.getException_type())){
						if("1".equals(exception1.getDuty_time_type()) || "3".equals(exception1.getDuty_time_type())){
							exception1.setException_type("迟到");
							exception1.setDuty_type("旷工");
						}else {
							exception1.setException_type("早退");
							exception1.setDuty_type("旷工");
						}
						
						
					/*	exception1.setException_type("不足规定小时数");
						exception1.setDuty_type("旷工");
						
						if(val == 0){
							exception1.setDuty_type("--");
						}*/
					}
					
					
				//转换成上下班类型
				if ("1".equals(exception1.getDuty_time_type())) {
					exception1.setDuty_time_type("上班");
				} else if ("2".equals(exception1.getDuty_time_type())) {
					exception1.setDuty_time_type("中途下班");
				} else if ("3".equals(exception1.getDuty_time_type())) {
					exception1.setDuty_time_type("中途上班");
				}  else {
					exception1.setDuty_time_type("下班");
				} 
				
				if("0".equals(exception1.getDeal_state())){
					exception1.setDeal_state("否");
				}else{
					exception1.setDeal_state("是");
				}
					
			 allException2.add(exception1);
		 }
		}catch(Exception e){
			e.printStackTrace();
		}
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(allException2);
		return grid;
	}

	@Override
	public boolean sendEmailById(String id) {
		return false;
	}

	@Override
	public List<ToEmailAddr> getEmailAddr(String staff_num) {
		try{
		return this.attendExceptionDao.getEmailAddr(staff_num);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void setDealState(String staff_num) {
		this.attendExceptionDao.setDealState(staff_num);
	}

	@Override
	public List<String> getStaffNumByParam(Param param) {
		try{
			
			return this.attendExceptionDao.getStaffNumByParam(param);//根据条件查询考勤异常记录
		}
		catch(Exception e){
			return null;
		}
	}

	@Override
	public boolean setSendMailRegular(String sign) {
		// TODO Auto-generated method stub
		
		
		return false;
	}
    /*导出异常考勤信息*/
	@Override
	public List<AttendException> exportAttExcpByParam(Param param) {
		List<AttendException> allException1=null;
		List<AttendException> allException2=new ArrayList<AttendException>();//用来保存处理后的结果
		double val;//用来保存处理后的Time_interval的值
		try{
		 allException1 =this.attendExceptionDao.exportAttExcpByParam(param);//根据条件查询考勤异常记录
      //根据条件查询考勤异常记录
		 for(AttendException exception1:allException1){//将数字转换成对应的用户看的懂的中文显示
			 
			 exception1.getTime_interval();
			 
		
			 val=(Double.valueOf(exception1.getTime_interval()))/60.0;
			 DecimalFormat df = new DecimalFormat("#.00");
					
			 //转换考勤异常时间间隔
			 		if(val==0){
			 			 exception1.setTime_interval("--");
			 		}
			 		else if(val<1){
						exception1.setTime_interval("0"+df.format(val));
					}
					else{
					    exception1.setTime_interval(df.format(val));
					}
					
				//转换为迟到早退或忘记打卡
					if ("1".equals(exception1.getException_type())) {
						exception1.setException_type("迟到");
						exception1.setDuty_type("迟到");
					} else if ("2".equals(exception1.getException_type())) {
						exception1.setException_type("早退");
						exception1.setDuty_type("早退");
					} else  if("3".equals(exception1.getException_type())){
						exception1.setException_type("缺打卡");
						exception1.setDuty_type("旷工");
					} else  if("4".equals(exception1.getException_type())){
						if("1".equals(exception1.getDuty_time_type()) || "3".equals(exception1.getDuty_time_type())){
							exception1.setException_type("迟到");
							exception1.setDuty_type("旷工");
						}else {
							exception1.setException_type("早退");
							exception1.setDuty_type("旷工");
						}
						
					}
					
					
				//转换成上下班类型
					if ("1".equals(exception1.getDuty_time_type())) {
						exception1.setDuty_time_type("上班");
					} else if ("2".equals(exception1.getDuty_time_type())) {
						exception1.setDuty_time_type("中途下班");
					} else if ("3".equals(exception1.getDuty_time_type())) {
						exception1.setDuty_time_type("中途上班");
					}  else {
						exception1.setDuty_time_type("下班");
					} 
					
					if("0".equals(exception1.getDeal_state())){
						exception1.setDeal_state("否");
					}else{
						exception1.setDeal_state("是");
					}
					
			 allException2.add(exception1);
		 }
		}catch(Exception e){
			e.printStackTrace();
		}
       return  allException2;
	}

}
