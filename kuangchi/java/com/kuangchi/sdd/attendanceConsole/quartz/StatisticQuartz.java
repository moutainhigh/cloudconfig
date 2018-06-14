package com.kuangchi.sdd.attendanceConsole.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;


import com.kuangchi.sdd.attendanceConsole.attend.action.AttendHandleAction;
import com.kuangchi.sdd.attendanceConsole.statistic.service.StatisticCalService;
import com.kuangchi.sdd.attendanceConsole.statistic.service.StatisticService;
import com.kuangchi.sdd.attendanceConsole.statistic.service.VeryDeptMonthStatisticService;
import com.kuangchi.sdd.attendanceConsole.statistic.service.VeryMonthStatisticService;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;


public class StatisticQuartz {
	
	public static final Logger LOG = Logger.getLogger(StatisticQuartz.class);
	
	StatisticService statisticService;
	VeryMonthStatisticService veryMonthStatisticService;
	VeryDeptMonthStatisticService veryDeptMonthStatisticService;
	ICronService cronService;
	StatisticCalService statisticCalService;
	
	
	
	
	public ICronService getCronService() {
		return cronService;
	}

	public void setCronService(ICronService cronService) {
		this.cronService = cronService;
	}

	//员工考勤每天统计
	public void personalAttendanceDayStatistic(){

		boolean getResource=false;
		try {
			getResource=AttendHandleAction.semaphore.tryAcquire(240,TimeUnit.MINUTES); //保证和手动统计考勤是串行的
		} catch (Exception e) {
			 e.printStackTrace();
			 return;
		}
		
		if (getResource) {
			try {
				statisticService.personalAttendanceDayStatistic();
				LOG.info("=======================personalAttendanceDayStatistic");
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				AttendHandleAction.semaphore.release();	
			}
		}
		
	}
	
	//员工考勤月统计
		public  void personalAttendanceMonthStatistic(){
			
			
			
			boolean getResource=false;
			try {
				getResource=AttendHandleAction.semaphore.tryAcquire(240,TimeUnit.MINUTES); //保证和手动统计考勤是串行的
			} catch (Exception e) {
				 e.printStackTrace();
				 return;
			}
			
			if (getResource) {
			    try {
					String d=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
					 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
	
					   Date startTime=DateUtil.getDate(sdf.format(d)+"-01");
					   String yMonthStr=sdf.format(startTime);
					for(String ele:veryMonthStatisticService.getAllStaff()){
						//获取员工本月各排班期间所在的部门
						  List<String> deptNumList=statisticCalService.getAttendanceDutyUserDeptsByStaffNum(ele, yMonthStr);
						  for (int j = 0; j <deptNumList.size(); j++) {
								veryMonthStatisticService.updateVeryMonthStatistic(ele, yMonthStr,deptNumList.get(j));
	
						}
					}
					LOG.info("=======================personalAttendanceMonthStatistic");
			    } catch (Exception e) {
				      e.printStackTrace();
				}finally{
					AttendHandleAction.semaphore.release();
				}
			}

		}
	
		//部门考勤月统计
		public void departmentAttendanceMonthStatistic(){
			boolean getResource=false;
			try {
				getResource=AttendHandleAction.semaphore.tryAcquire(240,TimeUnit.MINUTES); //保证和手动统计考勤是串行的
			} catch (Exception e) {
				 e.printStackTrace();
				 return;
			}
			
			if (getResource) {
				try {
				String d=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				for(String ele:veryDeptMonthStatisticService.getAllDept()){
					if(!ele.equals("1")&&!ele.equals("2")){ //如果是不是根部门和未分组部门
						veryDeptMonthStatisticService.updateDeptMonthStatistic(ele, d);
					}
				}
				LOG.info("=======================departmentAttendanceMonthStatistic");
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					AttendHandleAction.semaphore.release();
				}
			}


		}

		public StatisticService getStatisticService() {
			return statisticService;
		}

		public void setStatisticService(StatisticService statisticService) {
			this.statisticService = statisticService;
		}

		public VeryMonthStatisticService getVeryMonthStatisticService() {
			return veryMonthStatisticService;
		}

		public void setVeryMonthStatisticService(
				VeryMonthStatisticService veryMonthStatisticService) {
			this.veryMonthStatisticService = veryMonthStatisticService;
		}

		public VeryDeptMonthStatisticService getVeryDeptMonthStatisticService() {
			return veryDeptMonthStatisticService;
		}

		public void setVeryDeptMonthStatisticService(
				VeryDeptMonthStatisticService veryDeptMonthStatisticService) {
			this.veryDeptMonthStatisticService = veryDeptMonthStatisticService;
		}

		public StatisticCalService getStatisticCalService() {
			return statisticCalService;
		}

		public void setStatisticCalService(StatisticCalService statisticCalService) {
			this.statisticCalService = statisticCalService;
		}
		
		

}
