package com.kuangchi.sdd.attendanceConsole.statistic.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.activiti.engine.impl.cmd.NewTaskCmd;
import org.apache.log4j.Logger;
import org.apache.struts2.components.If;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.kuangchi.sdd.attendanceConsole.attend.service.IAttendHandleService;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceCheck;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDataDate;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDuty;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDutyUser;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceException;
import com.kuangchi.sdd.attendanceConsole.statistic.model.DeptNoCheckSet;
import com.kuangchi.sdd.attendanceConsole.statistic.model.Holiday;
import com.kuangchi.sdd.attendanceConsole.statistic.model.LeaveTime;
import com.kuangchi.sdd.attendanceConsole.statistic.model.NoCheckSet;
import com.kuangchi.sdd.attendanceConsole.statistic.model.OutWork;
import com.kuangchi.sdd.attendanceConsole.statistic.service.StatisticCalService;
import com.kuangchi.sdd.attendanceConsole.statistic.service.StatisticService;
import com.kuangchi.sdd.attendanceConsole.statistic.service.VeryDeptMonthStatisticService;
import com.kuangchi.sdd.attendanceConsole.statistic.service.VeryMonthStatisticService;
import com.kuangchi.sdd.businessConsole.department.dao.IDepartmentDao;
import com.kuangchi.sdd.businessConsole.department.model.Department;
import com.kuangchi.sdd.businessConsole.department.service.impl.DepartmentServiceImpl;
import com.kuangchi.sdd.businessConsole.employee.dao.EmployeeDao;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.util.commonUtil.DateUtil;


@Service("statisticService")
public class StatisticServiceImpl implements StatisticService{
	 public static final Logger LOG = Logger.getLogger(StatisticServiceImpl.class);
		@Resource(name="employeeDao")
		EmployeeDao employeeDao;
		@Resource(name="statisticCalService")
		StatisticCalService statisticCalService;
		@Resource(name="departmentServiceImpl")
		DepartmentServiceImpl departmentServiceImpl;
		@Resource(name = "departmentDaoImpl")
	    private IDepartmentDao departmentDao;

		
		 public final static String lock="lock";
		
		
		//将leaveTimeList时间列表取并集合并成相互之间没有交集的新的列表
		public static List<LeaveTime> getLeaveTimeListWithNoCascade(List<LeaveTime> leaveTimeList){
			List<LeaveTime> newList=new ArrayList<LeaveTime>();
			for(int i=0;i<leaveTimeList.size();i++){
				newList.add(leaveTimeList.get(i));
			}
	
			int i=0;
			int end=newList.size();
			for(i=0;i<end;i++){
				 for(int j=0;j<end;j++){
					 calculateMultipleLeaveTimeDate(newList,i);
				 }
			}
			return newList;
		}
		
		
		
		
		//请leaveTimeList，outworkList列表合并成相互之间没有交集的新的列表
		public static List<LeaveTime> getLeaveTimeOutworkListWithNoCascade(List<LeaveTime> leaveTimeList,List<OutWork> outworkList){
			List<LeaveTime> newList=new ArrayList<LeaveTime>();
			for (int i = 0; i < outworkList.size(); i++) {
				LeaveTime ltime=new LeaveTime();
				ltime.setFromTime(outworkList.get(i).getFromTime());
				ltime.setToTime(outworkList.get(i).getToTime());
				newList.add(ltime);
			}
			
			for(int i=0;i<leaveTimeList.size();i++){
				newList.add(leaveTimeList.get(i));
			}
			
	
			int i=0;
			int end=newList.size();
			for(i=0;i<end;i++){
				 for(int j=0;j<end;j++){
					 calculateMultipleLeaveTimeDate(newList,i);
				 }
			}
			return newList;
		}
		
		
		
		
		//将leaveTimeList, startDate, endDate时间合并，其中startDate, endDate也作为集合的一部分
				public static List<LeaveTime> getLeaveTimeListStartEndWithNoCascade(List<LeaveTime> leaveTimeList,Date startDate,Date endDate){
					List<LeaveTime> newList=new ArrayList<LeaveTime>();
					
					LeaveTime ltime=new LeaveTime();
					
					ltime.setFromTime(DateUtil.getDateString(startDate));
					ltime.setToTime(DateUtil.getDateString(endDate));
					newList.add(ltime);
					
					for(int i=0;i<leaveTimeList.size();i++){
						newList.add(leaveTimeList.get(i));
					}
					
			
					int i=0;
					int end=newList.size();
					for(i=0;i<end;i++){
						 for(int j=0;j<end;j++){
							 calculateMultipleLeaveTimeDate(newList,i);
						 }
					}
					return newList;
				}
		
				
				
		
		public static void calculateMultipleLeaveTimeDate(List<LeaveTime> leaveTimeList,Integer index){
			 if(index>=leaveTimeList.size()){
				 return;
			 }
			 LeaveTime leaveTime=leaveTimeList.get(index);
			 Date startDate=DateUtil.getDateTime(leaveTime.getFromTime());
    		 Date endDate=DateUtil.getDateTime(leaveTime.getToTime());
    		 String dateSmall=leaveTime.getFromTime();
    		 String dateBigger=leaveTime.getToTime();
    		 LeaveTime obj=null; 
			 for(int i=0;i<leaveTimeList.size();i++){
				 if(i>0){
					 LeaveTime lt=leaveTimeList.get(i); 
					 Date ltStart=DateUtil.getDateTime(lt.getFromTime());
		    		 Date ltEnd=DateUtil.getDateTime(lt.getToTime());
		    		 if((ltStart.compareTo(startDate)>=0&&ltStart.compareTo(endDate)<=0)
		    			||(ltEnd.compareTo(startDate)>=0&&ltEnd.compareTo(endDate)<=0)
		    			||(ltEnd.compareTo(endDate)>=0&&ltStart.compareTo(startDate)<=0)
		    			||(ltEnd.compareTo(endDate)<=0&&ltStart.compareTo(startDate)>=0)
		    				 ){
		    			 	if(ltStart.compareTo(startDate)<=0){
		    			 		dateSmall=lt.getFromTime();
		    			 	}
		    			 	if(ltEnd.compareTo(endDate)>=0){
		    			 		dateBigger=lt.getToTime();
		    			 	}	
		    			 	
		    			  obj=lt;
		    			 	break;
		    		 }
				 }			    	
			 }
			 
			 if(obj!=null){
				 LeaveTime leaveT=new LeaveTime();
				 leaveT.setFromTime(dateSmall);
				 leaveT.setToTime(dateBigger);
				 leaveTimeList.remove(leaveTime);
				 leaveTimeList.remove(obj);
				 leaveTimeList.add(leaveT);
			 }
		
			 
		}
		
		
		
		
		//将外出申请列表重叠的时间合并
		public static List<OutWork> getOutWorkListWithNoCascade(List<OutWork> outWorkList){
					
					List<OutWork> newList=new ArrayList<OutWork>();
					for(int i=0;i<outWorkList.size();i++){
						newList.add(outWorkList.get(i));
					}
					
					int i=0;
					int end=newList.size();
					for(i=0;i<end;i++){
						 for(int j=0;j<end;j++){
							 calculateMultipleOutWorkDate(newList,i);
						 }
					}
					return newList;
				}
		
		//将加班申请列表重叠的时间合并 by gengji.yang
		public static List<Map> getOtListWithNoCascade(List<Map> otList){
			
			List<Map> newList=new ArrayList<Map>();
			for(int i=0;i<otList.size();i++){
				newList.add(otList.get(i));
			}
			
			int i=0;
			int end=newList.size();
			for(i=0;i<end;i++){
				for(int j=0;j<end;j++){
						calculateMultipleOtTime(newList,i);
				}
			}
			return newList;
		}
				
		//将外出，startWorkDate,endWorkDate统计时间列表重叠的时间合并
				public static List<OutWork> geOutWorkListStartEndWithNoCascadeAsWorkNoIntegrate(List<OutWork> outWorkList,Date startWorkDate,Date endWorkDate){
									List<OutWork> newList=new ArrayList<OutWork>();
									
									OutWork outwork=new OutWork();
									
									outwork.setFromTime(DateUtil.getDateString(startWorkDate));
									outwork.setToTime(DateUtil.getDateString(endWorkDate));
									newList.add(outwork);
									
									for(int i=0;i<outWorkList.size();i++){
										OutWork ow=new OutWork();
										ow.setFromTime(outWorkList.get(i).getFromTime());
										ow.setToTime(outWorkList.get(i).getToTime());
										newList.add(ow);
									}
									
							
									int i=0;
									int end=newList.size();
									for(i=0;i<end;i++){
										 for(int j=0;j<end;j++){
											 calculateMultipleOutWorkDate(newList,i);
										 }
									}
									
									return newList;
								}
				
				//将外出列表，startWorkDate,endWorkDate这两个时间也作为一个外出记录，添加到外出列表中，同时将列表重叠的时间合并,并限制每条列表中的时间在startWorkDate和endWorkDate之间
		public static List<OutWork> geOutWorkListStartEndWithNoCascadeAsWork(List<OutWork> outWorkList,Date startWorkDate,Date endWorkDate){
							List<OutWork> newList=new ArrayList<OutWork>();
							
							OutWork outwork=new OutWork();
							
							outwork.setFromTime(DateUtil.getDateString(startWorkDate));
							outwork.setToTime(DateUtil.getDateString(endWorkDate));
							newList.add(outwork);
							
							for(int i=0;i<outWorkList.size();i++){
								OutWork ow=new OutWork();
								ow.setFromTime(outWorkList.get(i).getFromTime());
								ow.setToTime(outWorkList.get(i).getToTime());
								newList.add(ow);
							}
							
					
							int i=0;
							int end=newList.size();
							for(i=0;i<end;i++){
								 for(int j=0;j<end;j++){
									 calculateMultipleOutWorkDate(newList,i);
								 }
							}
							for(OutWork ow:newList){
								 Date fromTime=DateUtil.getDateTime(ow.getFromTime());
								 Date toTime=DateUtil.getDateTime(ow.getToTime());
								 if(fromTime.compareTo(startWorkDate)<=0&&toTime.compareTo(startWorkDate)>=0){
									 ow.setFromTime(DateUtil.getDateString(startWorkDate));
								 }
								 if(toTime.compareTo(endWorkDate)>=0&&fromTime.compareTo(endWorkDate)<=0){
									 ow.setToTime(DateUtil.getDateString(endWorkDate));
								 }
								 if(toTime.compareTo(startWorkDate)<=0||fromTime.compareTo(endWorkDate)>=0){
									 ow.setFromTime(ow.getToTime());
								 }
							}
							return newList;
						}
				
		
		
		//外出申请合并并限制小于时间界限
				public static List<OutWork> geOutWorkListStartEndWithNoCascade(List<OutWork> outWorkList,Date startWorkDate,Date endWorkDate){
									List<OutWork> newList=new ArrayList<OutWork>();

									for(int i=0;i<outWorkList.size();i++){
										OutWork ow=new OutWork();
										ow.setFromTime(outWorkList.get(i).getFromTime());
										ow.setToTime(outWorkList.get(i).getToTime());
										newList.add(ow);
									}
									
							
									int i=0;
									int end=newList.size();
									for(i=0;i<end;i++){
										 for(int j=0;j<end;j++){
											 calculateMultipleOutWorkDate(newList,i);
										 }
									}
									for(OutWork ow:newList){
										 Date fromTime=DateUtil.getDateTime(ow.getFromTime());
										 Date toTime=DateUtil.getDateTime(ow.getToTime());
										 if(fromTime.compareTo(startWorkDate)<=0&&toTime.compareTo(startWorkDate)>=0){
											 ow.setFromTime(DateUtil.getDateString(startWorkDate));
										 }
										 if(toTime.compareTo(endWorkDate)>=0&&fromTime.compareTo(endWorkDate)<=0){
											 ow.setToTime(DateUtil.getDateString(endWorkDate));
										 }
										 if(toTime.compareTo(startWorkDate)<=0||fromTime.compareTo(endWorkDate)>=0){
											 
											 ow.setFromTime(ow.getToTime());
										 }
										 
									}
									return newList;
								}
		
		
		public static void calculateMultipleOutWorkDate(List<OutWork> outWorkList,Integer index){
					 if(index>=outWorkList.size()){
						 return;
					 }
					 OutWork outWork=outWorkList.get(index);
					 Date startDate=DateUtil.getDateTime(outWork.getFromTime());
		    		 Date endDate=DateUtil.getDateTime(outWork.getToTime());
		    		 String dateSmall=outWork.getFromTime();
		    		 String dateBigger=outWork.getToTime();
		    		 OutWork obj=null; 
					 for(int i=0;i<outWorkList.size();i++){
						 if(i>0){
							 OutWork lt=outWorkList.get(i); 
							 Date ltStart=DateUtil.getDateTime(lt.getFromTime());
				    		 Date ltEnd=DateUtil.getDateTime(lt.getToTime());
				    		 if((ltStart.compareTo(startDate)>=0&&ltStart.compareTo(endDate)<=0)
				    			||(ltEnd.compareTo(startDate)>=0&&ltEnd.compareTo(endDate)<=0)
				    			||(ltEnd.compareTo(endDate)>=0&&ltStart.compareTo(startDate)<=0)
				    			||(ltEnd.compareTo(endDate)<=0&&ltStart.compareTo(startDate)>=0)
				    				 ){
				    			 	if(ltStart.compareTo(startDate)<=0){
				    			 		dateSmall=lt.getFromTime();
				    			 	}
				    			 	if(ltEnd.compareTo(endDate)>=0){
				    			 		dateBigger=lt.getToTime();
				    			 	}	
				    			 	
				    			  obj=lt;
				    			 	break;
				    		 }
						 }			    	
					 }
					 
					 if(obj!=null){
						 OutWork outW=new OutWork();
						 outW.setFromTime(dateSmall);
						 outW.setToTime(dateBigger);
						 outWorkList.remove(outWork);
						 outWorkList.remove(obj);
						 outWorkList.add(outW);
					 }
				
					 
				}
		
		public static void calculateMultipleOtTime(List<Map> otTimeList,Integer index){
			if(index>=otTimeList.size()){
				return;
			}
			Map otMap=otTimeList.get(index);
			Date startDate=DateUtil.getDateTime((String)otMap.get("ot_begin")+":00");
			Date endDate=DateUtil.getDateTime((String)otMap.get("ot_end")+":00");
			String dateSmall=(String)otMap.get("ot_begin");
			String dateBigger=(String)otMap.get("ot_end");
			Map obj=null; 
			for(int i=0;i<otTimeList.size();i++){
				if(i>0){
					Map lt=otTimeList.get(i); 
					Date ltStart=DateUtil.getDateTime((String)lt.get("ot_begin")+":00");
					Date ltEnd=DateUtil.getDateTime((String)lt.get("ot_end")+":00");
					if((ltStart.compareTo(startDate)>=0&&ltStart.compareTo(endDate)<=0)
							||(ltEnd.compareTo(startDate)>=0&&ltEnd.compareTo(endDate)<=0)
							||(ltEnd.compareTo(endDate)>=0&&ltStart.compareTo(startDate)<=0)
							||(ltEnd.compareTo(endDate)<=0&&ltStart.compareTo(startDate)>=0)
							){
						if(ltStart.compareTo(startDate)<=0){
							dateSmall=(String)lt.get("ot_begin");
						}
						if(ltEnd.compareTo(endDate)>=0){
							dateBigger=(String)lt.get("ot_end");
						}	
						
						obj=lt;
						break;
					}
				}			    	
			}
			
			if(obj!=null){
				Map m=new HashMap();
				m.put("ot_begin",dateSmall);
				m.put("ot_end",dateBigger);
				otTimeList.remove(otMap);
				otTimeList.remove(obj);
				otTimeList.add(m);
			}
			
			
		}
		
		
				 
			
		
		//----------------------日期函数起-----------------------
		//获取时间的年月日
		public static String getSimpleDateString(Date date){
	        return new SimpleDateFormat("yyyy-MM-dd").format(date);
		}
		//获取时间的年月
		public static String getYearMonth(Date date){
	        return new SimpleDateFormat("yyyy-MM").format(date);
		}
		
		//获取当天的日期年月日
		public static String getCurrentYearMonthDay(Date now){
			return getSimpleDateString(now) ;
		}
		
		
		//获取日期  yyyy-MM-dd HH:mm
		public static Date getSimpleDate(String dateString){
			try {
		        return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public static String getyyyyMMddHHmm(Date date){
			return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
		}
		
		
		 
	     //获取某一日期所在月的天数
		public static int getDayCountInMonthByDate(Date date){
	    	  Calendar aCalendar = Calendar.getInstance();
	    	  aCalendar.setTime(date);
			  int day=aCalendar.getActualMaximum(Calendar.DATE);
			  return day;
	      }
	     
	      
	      
	      
	      //获取时间间隔分钟
		private int getMinutesInterval(Date smallDate,Date biggerDate){
	     	 Calendar smallCalendar=Calendar.getInstance();
	     	 smallCalendar.setTime(smallDate);
	     	 Calendar bigCalendar=Calendar.getInstance();
	     	 bigCalendar.setTime(biggerDate);
	     	 long small=smallCalendar.getTimeInMillis();
	     	 long big=bigCalendar.getTimeInMillis();
	     	 long intervalMinutes= (big-small)/(1000*60);
	     	 return Math.abs(Long.valueOf(intervalMinutes).intValue());
	     	 
	      } 
		
		
	     //获取时间间隔分钟
			private int getSecondsInterval(Date smallDate,Date biggerDate){
		     	 Calendar smallCalendar=Calendar.getInstance();
		     	 smallCalendar.setTime(smallDate);
		     	 Calendar bigCalendar=Calendar.getInstance();
		     	 bigCalendar.setTime(biggerDate);
		     	 long small=smallCalendar.getTimeInMillis();
		     	 long big=bigCalendar.getTimeInMillis();
		     	 long intervalMinutes= (big-small)/(1000);
		     	 return Math.abs(Long.valueOf(intervalMinutes).intValue());
		     	 
		      } 
			
		
		
	    //----------------------日期函数止-----------------------
		
	   //-----------------------时间点函数起-----------------------   
		
				//获取考勤开始统计时间
		public static Date getDutyStartCheckPoint(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getDutyStartCheckPoint();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}
		
		
		//获取考勤结束统计时间
		public static Date getDutyEndCheckPoint(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getDutyEndCheckPoint();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}
		
				
				//获取本班次的上午上班时间
		public static Date getDutyTime1(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getDutyTime1();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}
				
				//获取本班次的上午下班时间
		public static Date getDutyTime2(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getDutyTime2();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}
				
				//获取本班次的下午上班时间
		public static Date getDutyTime3(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getDutyTime3();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}
				
				//获取本班次的上午下班时间
		public static Date getDutyTime4(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getDutyTime4();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}
				
					
				//如果是弹性工作制，获取弹 性工作制必须上班时间开始值
		public static Date getElasticDutyTime1(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getElasticDutyTime1();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}
				
				//如果是弹性工作制，获取弹 性工作制必须上班时间结束值
		public static Date getElasticDutyTime2(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getElasticDutyTime2();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}
				
				//如果是弹性工作制，获取弹 性工作制默认上班时间开始值
		public static Date getElasticDefaultDutyTime1(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getElasticDefaultDutyTime1();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}
				
				//如果是弹性工作制，获取弹 性工作制默认下班时间开始值
		public static Date getElasticDefaultDutyTime2(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getElasticDefaultDutyTime2();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}
				
				
				
				
		//获取考勤上午上班打卡时间
		public static Date getDutyTime1PointDate(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getDutyTime1Point();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}
		//获取考勤上午下班打卡时间
		public static Date getDutyTime2PointDate(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getDutyTime2Point();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}		
				
		//获取考勤下午上班打卡时间
		public static Date getDutyTime3PointDate(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getDutyTime3Point();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}
		//获取考勤下午下班打卡时间
		public static Date getDutyTime4PointDate(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getDutyTime4Point();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}
		//获取考勤上午迟到旷工打卡时间
		public static Date getDutyTime1Absent(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getDutyTime1Absent();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}		
		//获取考勤上午早退旷工时间点
		public static Date getDutyTime2Absent(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getDutyTime2Absent();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}				
		//获取考勤下午迟到旷工时间点
		public static Date getDutyTime3Absent(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getDutyTime3Absent();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}		
		//获取考勤下午早退旷工时间点
		public static Date getDutyTime4Absent(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getDutyTime4Absent();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}
		//获取考勤加班开始时间
		public static Date getOverTimeStart(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getOverTimeStart();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}			
		//获取考勤加班结束时间
		public static Date getOverTimeEnd(Date date,AttendanceDuty attendanceDuty){
					String hourMinute=attendanceDuty.getOverTimeEnd();
					if(hourMinute==null||"".equals(hourMinute)){
						return null;
					}
					String currentYearMonthDay=getCurrentYearMonthDay(date);
					String dateStr=currentYearMonthDay+" "+hourMinute;
					return getSimpleDate(dateStr);
				}		
				//-----------------------时间点函数止----------------------- 
				//-----------------------时间差函数起------------------------
		//获取前一天的时间
		public static Date getPreviousDayTime(Date date){
						if(date==null){
							return null;
						}
						 Calendar calendar =Calendar.getInstance();
						 calendar.setTime(date);
						 calendar.add(Calendar.DAY_OF_MONTH, -1);
						 return calendar.getTime();
				}			
		//获取后一天的时间
		public static Date getNextDayTime(Date date){
						if(date==null){
							return null;
						}
						 Calendar calendar =Calendar.getInstance();
						 calendar.setTime(date);
						 calendar.add(Calendar.DAY_OF_MONTH,  1);
						 return calendar.getTime();
				}
		//获取下一个月的时间
		public static Date getNextMonth(Date date){
			Calendar c=Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.MONTH, 1);
			return c.getTime();
		}
				//-----------------------时间差函数起------------------------
				
		//判断某个日期是否在公休日里		
		private boolean isVocation(AttendanceDuty attendanceDuty,Date date){
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(date);
		    Integer week=calendar.get(Calendar.DAY_OF_WEEK)-1;
		    String vocation=attendanceDuty.getVocation();
		    if(vocation==null){
		    	return false;
		    }
		    if(vocation.contains(week+"")){
		    	return true;
		    }
			return false;
		}		
 
     //获取补打卡次数
		private int getCardReCheckCount(List<AttendanceCheck> list){
    	 int count=0;
    	 for(AttendanceCheck ac:list){
    		 if("1".equals(ac.getFlagStatus())){
    			 count++;
    		 }
    	 }
    	 return count;
     }
     
     
     //获取当天班次上午的考勤打卡时间
		private  Date getDutyTime1CheckDate(List<AttendanceCheck> list,Date statisticDutyTime1Absent ){
    	  Date dutyTime1CheckDate=null;
    	  for(AttendanceCheck ac:list){
    		   Date date=DateUtil.getDateTime(ac.getChecktime());
    		   if(date.compareTo(statisticDutyTime1Absent)<=0){
    			   if(dutyTime1CheckDate!=null){
    				   if(date.compareTo(dutyTime1CheckDate)<0){
    					   dutyTime1CheckDate=date;
    				   }
    			   }else{
    				   dutyTime1CheckDate=date;  
    			   }
    		   }
    	  }
    	  return dutyTime1CheckDate;
     }
     
   //获取当天班次上午下班的考勤打卡时间
		private  Date getDutyTime2CheckDate(List<AttendanceCheck> list,Date statisticDutyTime2Absent,Date statisticDutyTime2,Date statisticDutyTime3,Date statisticDutyTime3Absent){
    	return getDutyTime2Time3CheckDate(list, statisticDutyTime2Absent,statisticDutyTime2,statisticDutyTime3, statisticDutyTime3Absent).get("dutyTime2CheckDate");
     }
     
  
     //获取当天班次下午上班的考勤打卡时间
		private  Date getDutyTime3CheckDate(List<AttendanceCheck> list,Date statisticDutyTime2Absent,Date statisticDutyTime2,Date statisticDutyTime3,Date statisticDutyTime3Absent){
    	return getDutyTime2Time3CheckDate(list, statisticDutyTime2Absent,statisticDutyTime2,statisticDutyTime3, statisticDutyTime3Absent).get("dutyTime3CheckDate");
     }
     
     
     //获取当天班次中午下班和上班的考勤打卡时间
		private  Map<String,Date> getDutyTime2Time3CheckDate(List<AttendanceCheck> list,Date statisticDutyTime2Absent,Date statisticDutyTime2,Date statisticDutyTime3,Date statisticDutyTime3Absent){
    	  //上午下班到下午上班的最早打卡时间
		  Date dutyTime2CheckDate=null;
		  //上午下班到下午上班的最晚打卡时间
    	  Date dutyTime3CheckDate=null;
    	  //上午旷工点到上午下班的最晚打卡时间
    	  Date dutyTime2EarlyDate=null;
    	  //下午上班到下午旷工的最早打卡时间
    	  Date dutyTime3LateDate=null;
    	  
    	  
    	  Map<String,Date> dateMap=new HashMap<String, Date>();
    	  for(AttendanceCheck ac:list){
    		   Date date=DateUtil.getDateTime(ac.getChecktime());
    		   if(date.compareTo(statisticDutyTime2)>0&&date.compareTo(statisticDutyTime3)<0){
    			   if(dutyTime2CheckDate!=null){
    				   if(date.compareTo(dutyTime2CheckDate)<0){
    					   dutyTime2CheckDate=date;
    				   }
    			   }else{
    				   dutyTime2CheckDate=date;  
    			   } 
    		   }
    	  }
    	  
    	  for(AttendanceCheck ac:list){
   		   Date date=DateUtil.getDateTime(ac.getChecktime());
   		   if(date.compareTo(statisticDutyTime2)>0&&date.compareTo(statisticDutyTime3)<0){
   			   if(dutyTime3CheckDate!=null){
   				   if(date.compareTo(dutyTime3CheckDate)>0){
   					   dutyTime3CheckDate=date;
   				   }
   			   }else{
   				   dutyTime3CheckDate=date;  
   			   }
   		   }
   	  }
    	  
    	  for(AttendanceCheck ac:list){
   		   Date date=DateUtil.getDateTime(ac.getChecktime());
   		   if(date.compareTo(statisticDutyTime2Absent)>=0&&date.compareTo(statisticDutyTime2)<=0){
   			   if(dutyTime2EarlyDate!=null){
   				   if(date.compareTo(dutyTime2EarlyDate)>0){
   					dutyTime2EarlyDate=date;
   				   }
   			   }else{
   				dutyTime2EarlyDate=date;  
   			   } 
   		   }
   	  }  
    	  
    	  
    	  for(AttendanceCheck ac:list){
      		   Date date=DateUtil.getDateTime(ac.getChecktime());
      		   if(date.compareTo(statisticDutyTime3)>=0&&date.compareTo(statisticDutyTime3Absent)<=0){
      			   if(dutyTime3LateDate!=null){
      				   if(date.compareTo(dutyTime3LateDate)<0){
      					 dutyTime3LateDate=date;
      				   }
      			   }else{
      				 dutyTime3LateDate=date;  
      			   }
      		   }
      	  }

    	  //如果两个值是相同的则说明是同一个打卡记录,说明下午上班没有打卡，需要修正结果
      if(dutyTime2CheckDate!=null&&dutyTime3CheckDate!=null&&dutyTime2CheckDate.compareTo(dutyTime3CheckDate)==0){
    	  if(dutyTime2EarlyDate==null&&dutyTime3LateDate!=null){
    		  dateMap.put("dutyTime2CheckDate", dutyTime2CheckDate);
        	  dateMap.put("dutyTime3CheckDate", dutyTime3LateDate); 
    	  }else if(dutyTime2EarlyDate!=null&&dutyTime3LateDate==null){
    		  dateMap.put("dutyTime2CheckDate", dutyTime2EarlyDate);
        	  dateMap.put("dutyTime3CheckDate", dutyTime3CheckDate); 
    	  }else if(dutyTime2EarlyDate==null&&dutyTime3LateDate==null){
    		  dateMap.put("dutyTime2CheckDate", dutyTime2CheckDate);
        	  dateMap.put("dutyTime3CheckDate", null);
    	  }else{
    		  dateMap.put("dutyTime2CheckDate", dutyTime2CheckDate);
        	  dateMap.put("dutyTime3CheckDate", dutyTime3LateDate);
    	  }
    	  
      }else if(dutyTime2CheckDate!=null&&dutyTime3CheckDate!=null&&dutyTime2CheckDate.compareTo(dutyTime3CheckDate)!=0){
    	  dateMap.put("dutyTime2CheckDate", dutyTime2CheckDate);
    	  dateMap.put("dutyTime3CheckDate", dutyTime3CheckDate); 
      }else{
    	  dateMap.put("dutyTime2CheckDate", dutyTime2EarlyDate);
    	  dateMap.put("dutyTime3CheckDate", dutyTime3LateDate);
      }

      return dateMap;
     }
     
   //获取当天班次下午下班的考勤打卡时间
		private  Date getDutyTime4CheckDate(List<AttendanceCheck> list,Date statisticDutyTime4Absent){
    	  Date dutyTime4CheckDate=null;
    	  for(AttendanceCheck ac:list){
    		   Date date=DateUtil.getDateTime(ac.getChecktime());
    		   if(date.compareTo(statisticDutyTime4Absent)>=0){
    			   if(dutyTime4CheckDate!=null){
    				   if(date.compareTo(dutyTime4CheckDate)>0){
    					   dutyTime4CheckDate=date;
    				   }
    			   }else{
    				   dutyTime4CheckDate=date;  
    			   }
    		   }
    	  }
    	  return dutyTime4CheckDate;
     }
     
		
		 //获取上午下班前打卡的最早值
		private  Date getMorningEarlyestCheckDate(List<AttendanceCheck> list,Date statisticDutyTime2){
    	  Date morningEarlyestDate=null;
    	  for(AttendanceCheck ac:list){
    		   Date date=DateUtil.getDateTime(ac.getChecktime());
    		   if(date.compareTo(statisticDutyTime2)<0){
    			   if(morningEarlyestDate!=null){
    				   if(date.compareTo(morningEarlyestDate)<0){
    					   morningEarlyestDate=date;
    				   }
    			   }else{
    				   morningEarlyestDate=date;  
    			   }
    		   }
    	  }
    	  return morningEarlyestDate;
     }
		
		
		
		
		
		 //获取下午上班前打卡的最晚值
			private  Date getMorningLatestCheckDateI(List<AttendanceCheck> list,Date statisticDutyTime1,Date statisticDutyTime3){
	    	  Date morningLatestDate=null;
	    	  for(AttendanceCheck ac:list){
	    		   Date date=DateUtil.getDateTime(ac.getChecktime());
	    		   if(date.compareTo(statisticDutyTime3)<0&&date.compareTo(statisticDutyTime1)>0){
	    			   if(morningLatestDate!=null){
	    				   if(date.compareTo(morningLatestDate)>0){
	    					   morningLatestDate=date;
	    				   }
	    			   }else{
	    				   morningLatestDate=date;  
	    			   }
	    		   }
	    	  }
	    	  return morningLatestDate;
	     }
		
			//获取上午下班后的最小值
		private Date getAfternoonEarlyestCheckDateI(List<AttendanceCheck> list,Date statisticDutyTime2,Date statisticDutytime4){
			  Date afternoonEarlyestCheckDate=null;
	    	  for(AttendanceCheck ac:list){
	    		   Date date=DateUtil.getDateTime(ac.getChecktime());
	    		   if(date.compareTo(statisticDutyTime2)>0&&date.compareTo(statisticDutytime4)<0){
	    			   if(afternoonEarlyestCheckDate!=null){
	    				   if(date.compareTo(afternoonEarlyestCheckDate)<0){
	    					   afternoonEarlyestCheckDate=date;
	    				   }
	    			   }else{
	    				   afternoonEarlyestCheckDate=date;  
	    			   }
	    		   }
	    	  }
	    	  return afternoonEarlyestCheckDate;
			
		}	
			
		//获取下午下班的最大值
		private Date getAfternoonLatestCheckDate(List<AttendanceCheck> list,Date statisticDutyTime3){
			  Date afternoonEarlyestCheckDate=null;
	    	  for(AttendanceCheck ac:list){
	    		   Date date=DateUtil.getDateTime(ac.getChecktime());
	    		   if(date.compareTo(statisticDutyTime3)>0){
	    			   if(afternoonEarlyestCheckDate!=null){
	    				   if(date.compareTo(afternoonEarlyestCheckDate)>0){
	    					   afternoonEarlyestCheckDate=date;
	    				   }
	    			   }else{
	    				   afternoonEarlyestCheckDate=date;  
	    			   }
	    		   }
	    	  }
	    	  return afternoonEarlyestCheckDate;
			
		}		
		
		
		
		 //获取下午上班前打卡的最晚值
		private  Date getMorningLatestCheckDate(List<AttendanceCheck> list,Date statisticDutyTime1,Date statisticDutyTime2,Date statisticDutyTime3,Date statisticDutyTime4){
	    	  return getMorningLatestAfternoonEarlyestCheckDate(list, statisticDutyTime1, statisticDutyTime2, statisticDutyTime3, statisticDutyTime4).get("morningLatestCheckDate");

     }
	
		//获取上午下班后的最小值
	private Date getAfternoonEarlyestCheckDate(List<AttendanceCheck> list,Date statisticDutyTime1,Date statisticDutyTime2,Date statisticDutyTime3,Date statisticDutyTime4){
		 
    	  return getMorningLatestAfternoonEarlyestCheckDate(list, statisticDutyTime1, statisticDutyTime2, statisticDutyTime3, statisticDutyTime4).get("afternoonEarlyestCheckDate");
		
	}
		
	
	
		
		private Map<String, Date>  getMorningLatestAfternoonEarlyestCheckDate(List<AttendanceCheck> list,Date statisticDutyTime1,Date statisticDutyTime2,Date statisticDutyTime3,Date statisticDutyTime4){
			Date morningLatestCheckDate=getMorningLatestCheckDateI(list, statisticDutyTime1, statisticDutyTime3);
			Date afternoonEarlyestCheckDate=getAfternoonEarlyestCheckDateI(list, statisticDutyTime2, statisticDutyTime4);
			Map<String, Date> map=new HashMap<String, Date>();
			map.put("morningLatestCheckDate", null);
			map.put("afternoonEarlyestCheckDate", null);
			if (morningLatestCheckDate!=null&&afternoonEarlyestCheckDate!=null) {
				 if (morningLatestCheckDate.compareTo(afternoonEarlyestCheckDate)>0) {
					 //此处翻转
					map.put("morningLatestCheckDate", afternoonEarlyestCheckDate);
					map.put("afternoonEarlyestCheckDate", morningLatestCheckDate);
				} else if(morningLatestCheckDate.compareTo(afternoonEarlyestCheckDate)==0){
					
					map.put("morningLatestCheckDate", afternoonEarlyestCheckDate);
					map.put("afternoonEarlyestCheckDate", null);
				}else{
					map.put("morningLatestCheckDate", morningLatestCheckDate);
					map.put("afternoonEarlyestCheckDate", afternoonEarlyestCheckDate);
				}
			}else{
				if (morningLatestCheckDate==null&&afternoonEarlyestCheckDate!=null) {
					map.put("morningLatestCheckDate", afternoonEarlyestCheckDate);
				}else if(morningLatestCheckDate!=null&&afternoonEarlyestCheckDate==null){
					map.put("morningLatestCheckDate", morningLatestCheckDate);
				} 
			}
			return map;
		}
     
		
		
		
		
     
     //获取弹性工作制上班打卡时间
		private Date getElasticDutyTime1CheckDate(List<AttendanceCheck> list,Date statisticElasticDutyTime1,Date statisticElasticDutyTime2){
    	 return getElasticDutyTime1Time2CheckDate(list, statisticElasticDutyTime1, statisticElasticDutyTime2).get("elasticdutyTime1CheckDate");
     }
     
   //获取弹性工作制下班打卡时间
		private Date getElasticDutyTime2CheckDate(List<AttendanceCheck> list,Date statisticElasticDutyTime1,Date statisticElasticDutyTime2){
    	 return getElasticDutyTime1Time2CheckDate(list, statisticElasticDutyTime1, statisticElasticDutyTime2).get("elasticdutyTime2CheckDate");
     }
     
     
     
   //获取弹性工作制当天班次上午上班时间打卡记录
		private   Map<String,Date> getElasticDutyTime1Time2CheckDate(List<AttendanceCheck> list,Date statisticElasticDutyTime1,Date statisticElasticDutyTime2){
    	  Date elasticdutyTime1CheckDate=null;
    	  Date elasticdutyTime2CheckDate=null;
    	  Map<String,Date> dateMap=new HashMap<String, Date>();
    	  for(AttendanceCheck ac:list){
    		   Date date=DateUtil.getDateTime(ac.getChecktime());
    		   if(date.compareTo(statisticElasticDutyTime1)<=0){
    			   if(elasticdutyTime1CheckDate!=null){
    				   if(date.compareTo(elasticdutyTime1CheckDate)<0){
    					   elasticdutyTime1CheckDate=date;
    				   }
    			   }else{
    				   elasticdutyTime1CheckDate=date;  
    			   }
    		   }
    	  }
    	  for(AttendanceCheck ac:list){
    		  Date date=DateUtil.getDateTime(ac.getChecktime());
    		   if(date.compareTo(statisticElasticDutyTime2)>=0){
    			   if(elasticdutyTime2CheckDate!=null){
    				   if(date.compareTo(elasticdutyTime2CheckDate)>0){
    					   elasticdutyTime2CheckDate=date;
    				   }
    			   }else{
    				   elasticdutyTime2CheckDate=date;  
    			   }
    		   }
    	  }
    	  
    	  dateMap.put("elasticdutyTime1CheckDate", elasticdutyTime1CheckDate);
    	  dateMap.put("elasticdutyTime2CheckDate", elasticdutyTime2CheckDate);
    	  //如果下班打卡时间和上班打卡时间相同说明只打了一次卡，下班没有打卡
    	  if(elasticdutyTime1CheckDate!=null&&elasticdutyTime2CheckDate!=null&&elasticdutyTime2CheckDate.compareTo(elasticdutyTime1CheckDate)==0){
    		  dateMap.put("elasticdutyTime2CheckDate", null);
    	  }
    	  return dateMap;
     }
     
     
     //获取最早打卡记录
		private Date getEarlyestCheckTime(List<AttendanceCheck> list){
    	 return getVocationCheckTime(list).get("date1");
     }
     //获取最晚打卡记录
		private Date getLatestCheckTime(List<AttendanceCheck> list){
    	 return getVocationCheckTime(list).get("date2");
     }
     
     
     
     //找出最早打卡记录和最晚打卡记录
		private Map<String, Date> getVocationCheckTime(List<AttendanceCheck> list){
          Map<String, Date> map=new HashMap<String, Date>();
          Date date1=null;
          Date date2=null;
        
          if (list.size()>0) {
			date1=DateUtil.getDateTime(list.get(0).getChecktime());
			date2=DateUtil.getDateTime(list.get(0).getChecktime());
			for(AttendanceCheck ad:list){
				Date date=DateUtil.getDateTime(ad.getChecktime());
				if(date1.compareTo(date)>0){
					date1=date;
				}
			}
			
			for(AttendanceCheck ad:list){
				Date date=DateUtil.getDateTime(ad.getChecktime());
				if(date2.compareTo(date)<0){
					date2=date;
				}
			}
		}
          map.put("date1", date1);
          map.put("date2", date2);
          if(date2!=null&&date1!=null){
	          if(date2.compareTo(date1)==0){
	        	  map.put("date1", date1);
	              map.put("date2", null);
	          }
          }
        return map;  
          
     }
     
     
      
     
     //获取每天应该打卡的次数,如果中间的值没有设置，则说明中午不需要考勤
		private int getMustCheckCount(AttendanceDuty attendanceDuty){
    	 if(attendanceDuty.getDutyTime2Point()==null||"".equals(attendanceDuty.getDutyTime2Point())){
    		 return 2;
    	 }
    	 return 4;
     }
     
     //判断某个时间点是否在请假时间内
		private boolean dateInLeaveTime(Date date,List<LeaveTime> leaveTimeList){
    	 for(LeaveTime lt:leaveTimeList){
    		 Date startDate=DateUtil.getDateTime(lt.getFromTime());
    		 Date endDate=DateUtil.getDateTime(lt.getToTime());
    		 if(date.compareTo(startDate)>=0&&date.compareTo(endDate)<=0){
    			 return true;
    		 }
    	 }
    	 return false;
     }
     
     //判断某一个时间点是否在外出申请范围内
		private boolean dateInOutWorkTime(Date date,List<OutWork> outWorkList){
    	 for(OutWork ow:outWorkList){
    		 Date startDate=DateUtil.getDateTime(ow.getFromTime());
    		 Date endDate=DateUtil.getDateTime(ow.getToTime());
    		 if(date.compareTo(startDate)>=0&&date.compareTo(endDate)<=0){
    			 return true;
    		 }
    	 }
    	 return false;
     }
    
       
     //获取请假时间 单位分钟
		private Integer getTodayLeaveTimeTotal(List<LeaveTime> list,Date start,Date end){
    	  List<LeaveTime> newList=getLeaveTimeListWithNoCascade(list);
    	  Integer total=0;
    	  for(LeaveTime leaveTime:newList){
    		  Date leaveStart=DateUtil.getDateTime(leaveTime.getFromTime());
    		  Date leaveEnd=DateUtil.getDateTime(leaveTime.getToTime());
    		  if(start.compareTo(leaveStart)<0&&end.compareTo(leaveEnd)>0){
    			  total=total+getSecondsInterval(leaveStart,leaveEnd);
    		  }else if(start.compareTo(leaveStart)<=0&&end.compareTo(leaveEnd)<=0&&end.compareTo(leaveStart)>0){
    			  total=total+getSecondsInterval(leaveStart,end);
    		  }else if(start.compareTo(leaveStart)>=0&&start.compareTo(leaveEnd)<0&&end.compareTo(leaveEnd)>=0){
    			  total=total+getSecondsInterval(start,leaveEnd);
    		  }else if(start.compareTo(leaveStart)>=0&&end.compareTo(leaveEnd)<=0){
    			  total=total+getSecondsInterval(start,end);
    		  }
    	  }
    	  return total;
      }
     
      
      //获取请假并集时间
		private Integer getTodayLeaveTimeTotal(List<LeaveTime> list){
    	  List<LeaveTime> newList=getLeaveTimeListWithNoCascade(list);
    	  Integer total=0;
    	  for(LeaveTime leaveTime:newList){
    		  Date leaveStart=DateUtil.getDateTime(leaveTime.getFromTime());
    		  Date leaveEnd=DateUtil.getDateTime(leaveTime.getToTime());
    		  total= total+ getSecondsInterval(leaveStart, leaveEnd);
    	  }
    	  return total;
      }
      
		 //获取外出并集累计时间
		private Integer getTodayOutWorkTotal(List<OutWork> list){
    	  List<OutWork> newList=getOutWorkListWithNoCascade(list);
    	  Integer total=0;
    	  for(OutWork outwork:newList){
    		  Date outworkStart=DateUtil.getDateTime(outwork.getFromTime());
    		  Date outworkEnd=DateUtil.getDateTime(outwork.getToTime());
    		  total= total+ getSecondsInterval(outworkStart, outworkEnd);
    	  }
    	  return total;
      }
		
      
    //获取外出总时间 单位分钟
		private Integer getTodayOutWorkTimeTotal(List<OutWork> list,Date start,Date end){
    	  List<OutWork> newList=getOutWorkListWithNoCascade(list);
    	  Integer total=0;
    	  for(OutWork outWork:newList){
    		  Date outWorkStart=DateUtil.getDateTime(outWork.getFromTime());
    		  Date outWorkEnd=DateUtil.getDateTime(outWork.getToTime());
    		  if(start.compareTo(outWorkStart)<0&&end.compareTo(outWorkEnd)>0){
    			  total=total+getSecondsInterval(outWorkStart,outWorkEnd);
    		  }else if(start.compareTo(outWorkStart)<=0&&end.compareTo(outWorkEnd)<=0&&end.compareTo(outWorkStart)>0){
    			  total=total+getSecondsInterval(outWorkStart,end);
    		  }else if(start.compareTo(outWorkStart)>=0&&start.compareTo(outWorkEnd)<0&&end.compareTo(outWorkEnd)>=0){
    			  total=total+getSecondsInterval(start,outWorkEnd);
    		  }else if(start.compareTo(outWorkStart)>=0&&end.compareTo(outWorkEnd)<=0){
    			  total=total+getSecondsInterval(start,end);
    		  }
    	  }
    	  return total;
      }
		
		//获取加班总时间 单位分钟 by gengji.yang
		private Integer getTodayOtTimeTotal(List<Map> list,Date start,Date end){
			List<Map> newList=getOtListWithNoCascade(list);
			Integer total=0;
			for(Map map:newList){
				Date otStart=DateUtil.getDateTime((String)map.get("ot_begin")+":00");
				Date otEnd=DateUtil.getDateTime((String)map.get("ot_end")+":00");
				if(start.compareTo(otStart)<0&&end.compareTo(otEnd)>0){
					total=total+getSecondsInterval(otStart,otEnd);
				}else if(start.compareTo(otStart)<=0&&end.compareTo(otEnd)<=0&&end.compareTo(otStart)>0){
					total=total+getSecondsInterval(otStart,otEnd);
				}else if(start.compareTo(otStart)>=0&&start.compareTo(otEnd)<0&&end.compareTo(otEnd)>=0){
					total=total+getSecondsInterval(start,otEnd);
				}else if(start.compareTo(otStart)>=0&&end.compareTo(otEnd)<=0){
					total=total+getSecondsInterval(start,otEnd);
				}
			}
			return total;
		}
      
      
		//合并员工免打卡设置，部门免打卡设置,如果员工在某一个时间区间内有单独设置，则以员工单独设置的为准，其次是以部门设置的为准 
		public NoCheckSet  getNoCheckSet(List<NoCheckSet> staffNoCheckSetList,List<DeptNoCheckSet>  deptNoCheckSetList){
			NoCheckSet  noCheckSet=new NoCheckSet();;
			if (staffNoCheckSetList.size()==0&&deptNoCheckSetList.size()==0) {
				return noCheckSet;
			}else{
				
				if (staffNoCheckSetList.size()>0) { //如果个人有免打卡设置，以个人为准 ,实际上该List最多只有一条记录，即该时间区间里的最新值					
						noCheckSet.setCheck_point(staffNoCheckSetList.get(0).getCheck_point());
						noCheckSet.setGenerate_record(staffNoCheckSetList.get(0).getGenerate_record());	//generate_record标识该员工如果设置了免打卡，是否还需要产生统计记录				
				}else{//如果个人没有 免打卡设置，部门有设置 ，以部门为准 ,该deptNoCheckSetList最多只有一条记录,即最新的值 
					if (deptNoCheckSetList.size()>0) {
						noCheckSet.setCheck_point(deptNoCheckSetList.get(0).getCheck_point());
						noCheckSet.setGenerate_record(deptNoCheckSetList.get(0).getGenerate_record());
					}
				}
			}
			  return noCheckSet;
		}
		
		 
	      
	      //往上找出某个人被设置免打卡的的第一个部门
		@Override
		public List<DeptNoCheckSet> getFirstNoCheckSetParentDepartmentsByBmDm(String bmDm,String checkDate) {
	         List<DeptNoCheckSet> list=new ArrayList<DeptNoCheckSet>();
	         //找出这个人的直接归属部门免打卡设置
	          list=statisticCalService.getNoCheckSetByDeptNum(bmDm, checkDate, "0");
	        if (list.size()==0) {
	        	//如果这个人的直接归属部门没有免打卡设置的话，就往上找第一个具有继承免打卡设置的部门
	    		  getDepartmentByBmDm(bmDm, checkDate, list);
			}
			return  list;
		}
		
		//递归查上级部门免打卡设置并且属性herited_to_sub_dept为1的记录
		public void getDepartmentByBmDm(String bmDm,String checkDate,List<DeptNoCheckSet> list){
		    if (null==bmDm) {
				return;
			}
			
			Department depart=	departmentDao.getDepartmentDet(bmDm);
			if (depart==null) {
				return ;
			}
			
			List<DeptNoCheckSet> deptNoCheckSetList=statisticCalService.getNoCheckSetByDeptNum(depart.getBmDm(), checkDate,"1");
			   if (deptNoCheckSetList.size()>0) {
				    list.clear();
				    list.addAll(deptNoCheckSetList);
			    }else{	
			    		getDepartmentByBmDm(depart.getSjbmDm(), checkDate, list);
			    }		 
		}
	      
		
		
		
		
		//获取两个时间段的交集,如果没有交集返回空值
		public Map<String, Date> getIntersectDate(Date start1,Date end1,Date start2,Date end2){
			Map<String, Date>  map=new HashMap<String, Date>();
		    Date newStart=null;
		    Date newEnd=null;
		    if (start1!=null&&end1!=null&&start2!=null&&end2!=null) {
				
			
		    
		    if (start1.compareTo(start2)<0) {
				newStart=start2;
			}else{
				newStart=start1;
			}
		    
		    if (end2.compareTo(end1)<=0) {
				newEnd=end2;
			}else{
				newEnd=end1;
			}
		    
		    if (newStart.compareTo(newEnd)>=0) {
				newStart=null;
				newEnd=null;
			}
		    map.put("start", newStart);
		    map.put("end", newEnd);
		    	}else{
		    		 map.put("start", null);
		 		    map.put("end",null);
			}
		    return map;
		}
		
		
		//获取异常列表
		List<AttendanceException>  getAttendanceExceptionListByPoint(String timePoint,String exceptionType,String staffNum  ){
			Map<String, String>  attendanceExceptionConditionMap=new HashMap<String, String>();
			  attendanceExceptionConditionMap.put("timePoint",timePoint );
			  attendanceExceptionConditionMap.put("exceptionType", exceptionType);
			  attendanceExceptionConditionMap.put("staffNum", staffNum);
			 return statisticCalService.getAttendanceExceptionList(attendanceExceptionConditionMap);
		}
		
		
		
		//获取节假日
		List<Holiday> getHolidayListByPoint(Date date,String gender,String bmDm  ){
			Map<String, String> holidayConditionMap=new HashMap<String, String>();
		    holidayConditionMap.put("startTime",DateUtil.getDateString(date) );
		    if ("1".equals(gender)) {   //因节假日表中的性别规定和常量表中的性别值是反的，这里颠倒一下
				gender="0";
			}else if("0".equals(gender)){
				gender="1";
			}
		    holidayConditionMap.put("gender",gender );
		    holidayConditionMap.put("dept_num", ","+bmDm+",");
		    return statisticCalService.getHolidayList(holidayConditionMap);
			
		}
		
		//获取打卡记录
		
		List<AttendanceCheck> getAttendanceCheckListByPoint(String staffNum,Date date1,Date date2 ){
			    Map<String,String> attendanceCheckConditionMap=new HashMap<String, String>();
			    attendanceCheckConditionMap.put("staffNum", staffNum);
			    attendanceCheckConditionMap.put("startTime", DateUtil.getDateString(date1));
			    attendanceCheckConditionMap.put("endTime", getyyyyMMddHHmm(date2)+":59");
			    return statisticCalService.getAttendanceCheckListByStaff(attendanceCheckConditionMap);			
		}
	   
	    		
		
		
		//获取补班
		List<Holiday> getExtralWorkListByPoint(Date date,String gender,String bmDm ){
		    Map<String, String> extralWorkConditionMap=new HashMap<String, String>();
		    extralWorkConditionMap.put("startTime",DateUtil.getDateString(date) );
		    if ("1".equals(gender)) {   //因节假日表中的性别规定和常量表中的性别值是反的，这里颠倒一下
				gender="0";
			}else if("0".equals(gender)){
				gender="1";
			}
		    extralWorkConditionMap.put("gender",gender);
		    extralWorkConditionMap.put("dept_num", ","+bmDm+",");
		    return statisticCalService.getExtralWorkDayList(extralWorkConditionMap);
		}
		//获取外出记录
      public List<OutWork> getOutWorkiListByPoint(String staffNum,Date date1,Date date2 ){
    	  
  	    Map<String,String> outWorkConditionMap=new HashMap<String, String>();
  	    outWorkConditionMap.put("staffNum", staffNum);
  	    outWorkConditionMap.put("startTime", DateUtil.getDateString(date1));
  	    outWorkConditionMap.put("endTime", DateUtil.getDateString(date2));
  	    return statisticCalService.getOutWorkListByStaff(outWorkConditionMap);
      }
      
      //获取请假记录
      public  List<LeaveTime> getLeaveTimeListByPoint(String staffNum,Date date1, Date date2){
    	  Map<String,String> leaveTimeConditionMap=new HashMap<String, String>();
  	    leaveTimeConditionMap.put("staffNum", staffNum);
  	    leaveTimeConditionMap.put("startTime", DateUtil.getDateString(date1));
  	    leaveTimeConditionMap.put("endTime",  DateUtil.getDateString(date2));
  	    return statisticCalService.getLeaveTimeListByStaff(leaveTimeConditionMap);
    	  
      }
      
      
      //获取外出记录
      public  List<Map> getOtListByPoint(String staffNum,Date date1,Date date2){
    	  Map<String,String> otConditionMap=new HashMap<String, String>();
  	    otConditionMap.put("staffNum", staffNum);
  	    otConditionMap.put("startTime", DateUtil.getDateString(date1));
  	    otConditionMap.put("endTime",  DateUtil.getDateString(date2));
  	    return statisticCalService.getOtListByStaff(otConditionMap);
      }
      
	    
		

		
		
      public void personalAttendanceDayStatistic(){
    	     
    	  synchronized(lock){
			LOG.info( "....................考勤统计线程启动,开始统计考勤,开始时间"+DateUtil.getDateString(new Date())+".....................");
				List<Employee> employeeList=employeeDao.getAllWorkingEmployee(null);//这里只统计在职员工
				Date now=new Date();
				    
				
				
				for(Employee employee:employeeList){	
					reStatisticByStaff(now, now, employee.getYhDm());		//直接调用这个接口就行了				    
				}
			LOG.info( "....................考勤统计线程启动,结束统计考勤,结束时间"+DateUtil.getDateString(new Date())+".....................");
    	  }
      }
      
      

      
      
     
				
		//员工考勤每天统计
		@Override
		public void personalAttendanceDayStatistic(Date now,Employee employee,AttendanceDuty attendanceDuty){
			LOG.info("开始统计员工"+employee.getYhMc()+",员工工号:"+employee.getYhNo()+",考勤日期:"+DateUtil.getDateString(now,"yyyy-MM-dd"));
			     //如果统计时间大于系统时间,则说明实际上还没有到统计时间，则不统计
			 	 Date sysDate=new Date();
			     if(now.after(sysDate)){
			    	 return;
			     }
			      

			    //只有员工排班了才去统计
				    if(null!=attendanceDuty){
				    	//====================================计算班次时间轴开始=================================
				    	//如果不是弹性工作制
				    	if(attendanceDuty.getIsElastic()==0){
                        
					    //开始和结束统计时间点
					    Date statisticStartCheckPoint=getDutyStartCheckPoint(now,attendanceDuty);
					    Date statisticEndCheckPoint=getDutyEndCheckPoint(now,attendanceDuty);
					    
					    //当日班次的上下班时间点
					    Date statisticDutyTime1=getDutyTime1(now, attendanceDuty);
					    Date statisticDutyTime2=getDutyTime2(now, attendanceDuty);
					    Date statisticDutyTime3=getDutyTime3(now, attendanceDuty);
					    Date statisticDutyTime4=getDutyTime4(now, attendanceDuty);

					    //考勤时间点
					    Date statisticDutyTime1PointDate=getDutyTime1PointDate(now,attendanceDuty);
					    Date statisticDutyTime2PointDate=getDutyTime2PointDate(now,attendanceDuty);
					    Date statisticDutyTime3PointDate=getDutyTime3PointDate(now,attendanceDuty);
					    Date statisticDutyTime4PointDate=getDutyTime4PointDate(now,attendanceDuty);
					    //考勤旷工时间点
					    Date statisticDutyTime1Absent=getDutyTime1Absent(now,attendanceDuty);
					    Date statisticDutyTime2Absent=getDutyTime2Absent(now,attendanceDuty);
					    Date statisticDutyTime3Absent=getDutyTime3Absent(now,attendanceDuty);
					    Date statisticDutyTime4Absent=getDutyTime4Absent(now,attendanceDuty);
					    //考勤加班时间点
					    Date statisticOverTimeStart=getOverTimeStart(now,attendanceDuty);
					    Date statisticOverTimeEnd=getOverTimeEnd(now,attendanceDuty);
					    //如果还没有到当天的考勤时间分布确认点，则考勤时间确认点往前推一天
					    if(now.compareTo(statisticDutyTime1PointDate)<0){
					    	     statisticStartCheckPoint=getPreviousDayTime(statisticStartCheckPoint);
							     statisticEndCheckPoint=getPreviousDayTime(statisticEndCheckPoint);
							     
							     //当日班次的上下班时间点
							     statisticDutyTime1=getPreviousDayTime(statisticDutyTime1);
							     statisticDutyTime2=getPreviousDayTime(statisticDutyTime2);
							     statisticDutyTime3=getPreviousDayTime(statisticDutyTime3);
							     statisticDutyTime4=getPreviousDayTime(statisticDutyTime4);
    
							    //考勤时间点
							     statisticDutyTime1PointDate=getPreviousDayTime(statisticDutyTime1PointDate);
							     statisticDutyTime2PointDate=getPreviousDayTime(statisticDutyTime2PointDate);
							     statisticDutyTime3PointDate=getPreviousDayTime(statisticDutyTime3PointDate);
							     statisticDutyTime4PointDate=getPreviousDayTime(statisticDutyTime4PointDate);
							    //考勤旷工时间点
							     statisticDutyTime1Absent=getPreviousDayTime(statisticDutyTime1Absent);
							     statisticDutyTime2Absent=getPreviousDayTime(statisticDutyTime2Absent);
							     statisticDutyTime3Absent=getPreviousDayTime(statisticDutyTime3Absent);
							     statisticDutyTime4Absent=getPreviousDayTime(statisticDutyTime4Absent);
							    //考勤加班时间点
							     statisticOverTimeStart=getPreviousDayTime(statisticOverTimeStart);
							     statisticOverTimeEnd=getPreviousDayTime(statisticOverTimeEnd);
	
		 			    }
					    //上面只是将所有的考勤时间点往前推了一天，下面解决跨天时间点问题
					    //如果前面的时间大于后面的时间则前面的时间要减一天
					    if(statisticStartCheckPoint.after(statisticDutyTime1)){
					    	statisticStartCheckPoint=getPreviousDayTime(statisticStartCheckPoint);
					    }
					    
					  //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
					    if(statisticDutyTime1PointDate.before(statisticDutyTime1)){
					    	statisticDutyTime1PointDate=getNextDayTime(statisticDutyTime1PointDate);
					    } 
					    
					  //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
					    if(statisticDutyTime2.before(statisticDutyTime1)){
					    	statisticDutyTime2=getNextDayTime(statisticDutyTime2);
					    }
					    
					    //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
					    if(statisticDutyTime3.before(statisticDutyTime1)){
					    	statisticDutyTime3=getNextDayTime(statisticDutyTime3);
					    }
					    //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
					    if(statisticDutyTime4.before(statisticDutyTime1)){
					    	statisticDutyTime4=getNextDayTime(statisticDutyTime4);
					    }
					    
					    //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
					    if(statisticDutyTime2PointDate!=null&&statisticDutyTime2PointDate.before(statisticDutyTime1)){
					    	statisticDutyTime2PointDate=getNextDayTime(statisticDutyTime2PointDate);
					    }
					    //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
					    if(statisticDutyTime3PointDate!=null&&statisticDutyTime3PointDate.before(statisticDutyTime1)){
					    	statisticDutyTime3PointDate=getNextDayTime(statisticDutyTime3PointDate);
					    }
					    //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
					    if(statisticDutyTime4PointDate.before(statisticDutyTime1)){
					    	statisticDutyTime4PointDate=getNextDayTime(statisticDutyTime4PointDate);
					    }
					    //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
					    if(statisticDutyTime1Absent.before(statisticDutyTime1)){
					    	statisticDutyTime1Absent=getNextDayTime(statisticDutyTime1Absent);
					    }
					  //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
					    if(statisticDutyTime2Absent!=null&&statisticDutyTime2Absent.before(statisticDutyTime1)){
					    	statisticDutyTime2Absent=getNextDayTime(statisticDutyTime2Absent);
					    }
					  //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
					    if(statisticDutyTime3Absent!=null&&statisticDutyTime3Absent.before(statisticDutyTime1)){
					    	statisticDutyTime3Absent=getNextDayTime(statisticDutyTime3Absent);
					    }
					  //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
					    if(statisticDutyTime4Absent.before(statisticDutyTime1)){
					    	statisticDutyTime4Absent=getNextDayTime(statisticDutyTime4Absent);
					    }
					    
					    //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
					    if(statisticOverTimeStart!=null&&statisticOverTimeStart.before(statisticDutyTime1)){
					    	statisticOverTimeStart=getNextDayTime(statisticOverTimeStart);
					    }
					  //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
					    if(statisticOverTimeEnd!=null&&statisticOverTimeEnd.before(statisticDutyTime1)){
					    	statisticOverTimeEnd=getNextDayTime(statisticOverTimeEnd);
					    }
					    
					    //如果后面的时候小于前面的时间，说明跨天了，后面的时间要加一天
					    if(statisticEndCheckPoint.before(statisticDutyTime1) ){
					    	statisticEndCheckPoint=getNextDayTime(statisticEndCheckPoint);
					    }
		             
					  //====================================计算班次时间轴结束=================================  
					    
					    //删除该区间的旧的异常，以便重新统计
					    attendanceService.toDelExceptionAfterApply(employee.getYhDm(), DateUtil.getDateString(statisticStartCheckPoint), DateUtil.getDateString(statisticEndCheckPoint));

					    //==============================获取影响考勤的条件开始============================
					    //获取补班列表
					   List<Holiday>  extralWorkList=getExtralWorkListByPoint(statisticDutyTime1, employee.getXb(), employee.getBmDm());
					    
					    
					    
					    
					    //获取当前的节假日
					   List<Holiday>  holidayList=getHolidayListByPoint(statisticDutyTime1, employee.getXb() , employee.getBmDm());
					    
					    //获取当天的考勤打卡记录
					    List<AttendanceCheck> attendanceCheckList=getAttendanceCheckListByPoint(employee.getYhDm(), statisticStartCheckPoint, statisticEndCheckPoint);
					    
					     //获取当天的外出申请记录
					    List<OutWork> outWorkList=getOutWorkiListByPoint(employee.getYhDm(), statisticDutyTime1PointDate, statisticDutyTime4PointDate);
					    //获取当前的请假记录

					    List<LeaveTime> leaveTimeList=getLeaveTimeListByPoint(employee.getYhDm(), statisticDutyTime1PointDate, statisticDutyTime4PointDate);
					    //by gengji.yang 获取当天的加班申请记录

					    List<Map> otList= getOtListByPoint(employee.getYhDm(), statisticDutyTime1PointDate, statisticDutyTime4PointDate);
					    
					    //获取员工免打卡设置
					    List<NoCheckSet> staffNoCheckSetList=statisticCalService.getNoCheckSetByStaffNum(employee.getYhDm(),getSimpleDateString(statisticDutyTime1));
					    //获取部门免打卡设置
					    List<DeptNoCheckSet> deptNoCheckSetList=getFirstNoCheckSetParentDepartmentsByBmDm(attendanceDuty.getDept_num(), getSimpleDateString(statisticDutyTime1));
					    
					    //当天统计日期归属,如2016-11-11,即下面的统计属性哪一天的考勤数据
					    String yearMonthDay=getSimpleDateString(statisticDutyTime1);
					    
					    
					    //下面的代码就是将员工免打卡设置和部门免打卡设置取并集,以保证某一个部门新添加的员工可以继承部门的免打卡设置
					    NoCheckSet noCheckSet= getNoCheckSet(staffNoCheckSetList, deptNoCheckSetList);
					    if (noCheckSet.getGenerate_record()!=null&&noCheckSet.getGenerate_record()==1) {  //如果免打卡设置后不产生该人的统计记录
					    	//如果不产生记录，则要删除某人当天的历史遗留的记录
					    	statisticCalService.deleteAttendanceDataDateByStaffNumAndDate(employee.getYhDm(), yearMonthDay);
							return;
						}
					    
					    //==============================获取影响考勤的条件结束============================
					    
					    	 //获取统计对象,即数据库中是否已经存在当天的统计记录
					    	    Map<String, String> attendanceDataDateConditionMap=new HashMap<String, String>();
							    attendanceDataDateConditionMap.put("staffNum", employee.getYhDm());							    
							    attendanceDataDateConditionMap.put("everyDate", yearMonthDay);
							    AttendanceDataDate attendanceDataDate=statisticCalService.getAttendanceDataDateByStaffAndEveryDate(attendanceDataDateConditionMap);
							    //如果没有当天的统计记录，则添加一条
							    if(null==attendanceDataDate){
							    	attendanceDataDate=new AttendanceDataDate();
									attendanceDataDate.setStaff_num(employee.getYhDm());
									attendanceDataDate.setStaff_name(employee.getYhMc());								
									attendanceDataDate.setEvery_date(yearMonthDay);
									attendanceDataDate.setDept_num(attendanceDuty.getDept_num());
									attendanceDataDate.setStaff_no(employee.getYhNo());
									attendanceDataDate.setDept_name(attendanceDuty.getDept_name());
									attendanceDataDate.setSex(employee.getXb()==null?null:Integer.parseInt(employee.getXb()));
									statisticCalService.insertAttendanceDataDate(attendanceDataDate);
							    }
							    //经过上面的插入之后，现在需要重新获取数据库的持久化对象用于后面的计算
							    attendanceDataDate=statisticCalService.getAttendanceDataDateByStaffAndEveryDate(attendanceDataDateConditionMap);
							    
							    //当日的免打卡设置,如果没有免打卡设置，则默认为零

								 attendanceDataDate.setNo_check_set(noCheckSet.getCheck_point());
								
							    
							    
							    //设置离职状态
							    attendanceDataDate.setIn_work(Integer.parseInt(employee.getZfBj()));
							    
							    
						    	//获取上午作为考勤的最早打卡记录
						    	Date dutyTime1CheckDate=getDutyTime1CheckDate(attendanceCheckList, statisticDutyTime1Absent);
						    	Date dutyTime4CheckDate=getDutyTime4CheckDate(attendanceCheckList, statisticDutyTime4Absent); 
							    Date dutyTime2CheckDate=null;
							    Date dutyTime3CheckDate=null;
						    	//如果中午也要考勤
						    	if(getMustCheckCount(attendanceDuty)==4){
							    	 dutyTime2CheckDate=getDutyTime2CheckDate(attendanceCheckList,  statisticDutyTime2Absent, statisticDutyTime2,statisticDutyTime3, statisticDutyTime3Absent);
							    	 dutyTime3CheckDate=getDutyTime3CheckDate(attendanceCheckList,   statisticDutyTime2Absent, statisticDutyTime2,statisticDutyTime3, statisticDutyTime3Absent);
						    	}
					              //一种特例是当上班旷工时间点设为下班时间，下班旷工时间点设为上班时间时，所得的四个打卡时间可能会重复，因此需要进一步去除重复数据
						    	//将dutyTime1CheckDate和dutyTime2CheckDate,dutyTime3CheckDate,dutyTime4CheckDate比较
						    	//将dutyTime2CheckDate和dutyTime3CheckDate,dutyTime4CheckDate比较
						    	//将dutyTime3CheckDate和dutyTime4CheckDate比较
						    	if (dutyTime1CheckDate!=null&&dutyTime2CheckDate!=null) {
						    		if (dutyTime1CheckDate.compareTo(dutyTime2CheckDate)==0) {  				
										dutyTime2CheckDate=null;
									}
								}
                                 if (dutyTime1CheckDate!=null&&dutyTime3CheckDate!=null) {
									if (dutyTime1CheckDate.compareTo(dutyTime3CheckDate)==0) {
										dutyTime3CheckDate=null;
									}
								}
                                 
                                 if (dutyTime1CheckDate!=null&&dutyTime4CheckDate!=null) {
									if (dutyTime1CheckDate.compareTo(dutyTime4CheckDate)==0) {
										dutyTime4CheckDate=null;
									}
								}
                                 
                                 if (dutyTime2CheckDate!=null&&dutyTime3CheckDate!=null) {
									if (dutyTime2CheckDate.compareTo(dutyTime3CheckDate)==0) {
										dutyTime3CheckDate=null;
									}
								}
						    	if (dutyTime2CheckDate!=null&&dutyTime4CheckDate!=null) {
									if (dutyTime2CheckDate.compareTo(dutyTime4CheckDate)==0) {
										dutyTime4CheckDate=null;
									}
								}
						    	if (dutyTime3CheckDate!=null&&dutyTime4CheckDate!=null) {
									if (dutyTime3CheckDate.compareTo(dutyTime4CheckDate)==0) {
										dutyTime4CheckDate=null;
									}
								}
						    	 
                                    	//设置上午上，下班时间和下午上,下班时间
									    attendanceDataDate.setWork_begin_time1(DateUtil.getDateString(dutyTime1CheckDate));
									    attendanceDataDate.setWork_end_time1(DateUtil.getDateString(dutyTime2CheckDate));
									    attendanceDataDate.setWork_begin_time2(DateUtil.getDateString(dutyTime3CheckDate));
									    attendanceDataDate.setWork_end_time2(DateUtil.getDateString(dutyTime4CheckDate));
						    	     
                                

					             //上午迟到早退时间
					            Integer morningLateEarlyTime=0;
					            //下午迟到早退时间
					            Integer afternoonLateEarlyTime=0; 
					            //旷工时间
					            Integer kgTime=0;
					            //旷工次数
					            Integer kgCount=0;
					            //缺卡次数
					            Integer cardNotCount=0;
					            //早退次数
					            Integer earlyTimeCount=0;
					            //早退时间
					            Integer earlyTimeTimeTotal=0;
					            //迟到次数
					            Integer lateTimeCount=0;
					            //迟到时间
					            Integer lateTimeTimeTotal=0;
					            //今日考勤是否异常   1正常    0 异常
					            Integer exception_flag=1;
					            
					            //今天是否是节假日
					            boolean isVocation=isVocation(attendanceDuty, statisticDutyTime1PointDate);
					            //上午上班考勤点是否落在放假时间内
					            boolean statisticDutyTime1PointDateInLeaveTime=dateInLeaveTime(statisticDutyTime1PointDate, leaveTimeList);
					            //上午上班考勤点是否落在外出时间内
					            boolean statisticDutyTime1PointDateDateInOutWorkTime=dateInOutWorkTime(statisticDutyTime1PointDate, outWorkList);
					            //上午下班考勤点是否落在请假时间内
					            boolean statisticDutyTime2PointDateInLeaveTime=false;
					            //上午下班考勤点是否落在外出时间内
					            boolean statisticDutyTime2PointDateInOutWork=false;
					            if (statisticDutyTime2PointDate!=null) {
						             statisticDutyTime2PointDateInLeaveTime= dateInLeaveTime(statisticDutyTime2PointDate, leaveTimeList);						           
						             statisticDutyTime2PointDateInOutWork= dateInOutWorkTime(statisticDutyTime2PointDate, outWorkList);
					            }
					            //下午上班考勤点是否落在请假时间内
					            boolean statisticDutyTime3PointDateInLeaveTime=false;
					            //下午上班考勤点是否落在外出时间内
					            boolean statisticDutyTime3PointDateInOutWork=false;
					            if(statisticDutyTime3PointDate!=null){
						             statisticDutyTime3PointDateInLeaveTime=dateInLeaveTime(statisticDutyTime3PointDate, leaveTimeList);				            
						             statisticDutyTime3PointDateInOutWork=dateInOutWorkTime(statisticDutyTime3PointDate, outWorkList);	
					            }

					            //下午下班考勤点是否落在请假时间内
					            boolean statisticDutyTime4PointDateInLeaveTime=dateInLeaveTime(statisticDutyTime4PointDate, leaveTimeList);
					          //下午下班考勤点是否落在外出时间内
					            boolean statisticDutyTime4PointDateInOutWork= dateInOutWorkTime(statisticDutyTime4PointDate, outWorkList);
					            //上午应上班时间
					             Integer statisticDutyTime1DutyTime2Interval=getSecondsInterval( statisticDutyTime1,statisticDutyTime2);
					             //下午应上班时间
					             Integer statisticDutyTime3DutyTime4Interval=getSecondsInterval( statisticDutyTime3,statisticDutyTime4);
					             
					             //旷工类型   从右往左，分别用第1,2位表示上午旷工，下午旷工,如00000001表示上午旷工，00000010表示下午旷工,00000011表示全天旷工
					             Integer kg_type=0;
					             
					             //上午不是公休日或节假日
					             boolean morningIsNotVocationOrHoliday=(!isVocation&(holidayList.size()==0||(holidayList.size()>0&&(holidayList.get(0).getTime_period()&1)==0)));  
					            //下午不是公休日或节假日
					             boolean afternoonIsNotVocationOrHoliday=(!isVocation&(holidayList.size()==0||(holidayList.size()>0&&(holidayList.get(0).getTime_period()&2)==0)));  
  					             
					             
					             
					            //如果是工作日或补班
					            if(morningIsNotVocationOrHoliday||afternoonIsNotVocationOrHoliday||extralWorkList.size()>0){
					            	
					            	
					            	//===================计算旷工类型开始===================================
					            	
					            	//如果上午不是公休日或节假日.,或者需要补班
						             if(morningIsNotVocationOrHoliday||extralWorkList.size()>0){
								                      //上午上班缺勤,过了上午上班考勤旷工时间点还没有打卡
											    	if(null==dutyTime1CheckDate){
											    		//如果早上没有免打卡设置
											    		if (noCheckSet==null||(noCheckSet!=null&&((noCheckSet.getCheck_point()&1)==0))) {
												    		if(now.compareTo(statisticDutyTime1Absent)>=0){
												    		//如果没有请假
												    		if(!statisticDutyTime1PointDateInLeaveTime){
												    			//如果又没有外出申请
												    			if(!statisticDutyTime1PointDateDateInOutWorkTime){
												    				  if (getMustCheckCount(attendanceDuty)==4) {
												    					  //如果中途考勤，则该次缺卡会导致上午旷工
													    				  kg_type=kg_type|1;
																	  }else{
												    					  //如果中途不考勤，则该次缺卡会导致全天旷工
																		  kg_type=kg_type|3;   
																	  }
																    	
												    			}
												    		}
												    	}
											    	
						                           }
											    	}
											    	
											    	
						            }
											    	    //如果上午下班和下午上班也要打卡的话
												    	if(getMustCheckCount(attendanceDuty)==4){
												    		if(morningIsNotVocationOrHoliday||extralWorkList.size()>0){
													    		//上午下班班缺勤，过了下午上班时间，上午的下班卡还没有打
													    		if(null==dutyTime2CheckDate){
														    		//如果没有免打卡设置
														    		if (noCheckSet==null||(noCheckSet!=null&&((noCheckSet.getCheck_point()&2)==0))) {
													    			if(now.compareTo(statisticDutyTime3PointDate)>=0){
														    		//如果没有请假
														    		if(!statisticDutyTime2PointDateInLeaveTime){
														    			//如果又没有外出申请
														    			if(!statisticDutyTime2PointDateInOutWork){
														    				//该次缺卡导致上午旷工
														    				  kg_type=kg_type|1; 
														    			}
													    		}
													    		}
														    		}
													    	}
												    	}
												    		
												    		
												    	if(afternoonIsNotVocationOrHoliday||extralWorkList.size()>0){											    		
													    	//下午上班缺勤，过了下午考勤旷工记录点还没有打卡
													    	if(null==dutyTime3CheckDate){
													    		//如果没有免打卡设置
													    		if (noCheckSet==null||(noCheckSet!=null&&((noCheckSet.getCheck_point()&4)==0))) {
														    		if(now.compareTo(statisticDutyTime3Absent)>=0){
															    		//如果没有请假
															    		if(!statisticDutyTime3PointDateInLeaveTime){
															    			//如果又没有外出申请
															    			if(!statisticDutyTime3PointDateInOutWork){
															    				//该次缺卡导致下午旷工
															    				  kg_type=kg_type|2;
															    			}
														    		}
														    	}
													    		}
													    	}
												    	}
											    	
											    }
												    	
											   if(afternoonIsNotVocationOrHoliday||extralWorkList.size()>0){ 	
											    	//下午下班缺勤,过了下班统计时间，还没有打卡
											    	if(null==dutyTime4CheckDate){
											    		//如果没有免打卡设置
											    		if (noCheckSet==null||(noCheckSet!=null&&((noCheckSet.getCheck_point()&8)==0))) {
											    		//过了考勤统计结束时间还没有打卡
												    		if(now.compareTo(statisticEndCheckPoint)>=0){
												    		//如果也没有请假
												    		if(!statisticDutyTime4PointDateInLeaveTime){
												    			//如果又没有外出工作
												    			if(!statisticDutyTime4PointDateInOutWork){
												    				  if (getMustCheckCount(attendanceDuty)==4) {
												    					  //如果中途也要考勤，则该缺卡会导致下午旷工
													    				  kg_type=kg_type|2;
																	  }else{
																		  //如果中途不考勤，则该次缺卡会导致全天旷工
																		  kg_type=kg_type|3;
																	  }
												    			}
												    		}
												    	}
											    		}
											    	}
						            }
								 //===================计算旷工类型结束===================================
					            	
					            	
					            	
					            	
					            	
					            	//旷工算到哪一个点   用位表示,从右往左，第 1,2,3,4位分别表示上午上班，上午下班，下午上班，下午下班，如00000001表示计算到上午上班,00001001表示计算到上午上班和下午下班
					           int kg_cal_type=0; 	
					            	
					            	
					            	
					            	
					            	
					            	//如果上午不是节假日或公休日或者要补班
					             if(morningIsNotVocationOrHoliday||extralWorkList.size()>0){
							                      //上午上班缺勤,过了上午上班考勤旷工时间点还没有打卡
										    	if(null==dutyTime1CheckDate){
										    		//如果早上没有免打卡设置
										    		if (noCheckSet==null||(noCheckSet!=null&&((noCheckSet.getCheck_point()&1)==0))) {
											    		if(now.compareTo(statisticDutyTime1Absent)>=0){
											    		//如果没有请假
											    		if(!statisticDutyTime1PointDateInLeaveTime){
											    			//如果又没有外出申请
											    			if(!statisticDutyTime1PointDateDateInOutWorkTime){
											    				//timepoint表示异常时间点
											    				  String timePoint=DateUtil.getDateString(statisticDutyTime1PointDate);
											    				  String exceptionType="3";//3表示缺卡
											    				  //获取某人某一个时间点的旧有的考勤异常
											    				  List<AttendanceException> attendanceExceptionList=getAttendanceExceptionListByPoint(timePoint, exceptionType, employee.getYhDm());
															    		AttendanceException attendanceException=new AttendanceException();
															    		//统计到上午上班时间点所在的日期中去
															    		attendanceException.setDuty_time_type("1");//1表示上午上班
															    		//哪一天的异常
															    		attendanceException.setEveryday_time(yearMonthDay);
															    		attendanceException.setException_type(exceptionType);
															    		attendanceException.setStaff_name(employee.getYhMc());
															    		attendanceException.setStaff_num(employee.getYhDm());
															    		exception_flag=exception_flag&0;  //&0表示说明今天考勤异常
															    		//旷工时间计算到上午上班处
															    		kg_cal_type=kg_cal_type|1;
															    		//旷工时间
															    		if(getMustCheckCount(attendanceDuty)==4){
															    			//如果中午也要考勤
															    			attendanceException.setTime_interval(statisticDutyTime1DutyTime2Interval);
															    			//旷工时间+上午
															    			kgTime=kgTime+statisticDutyTime1DutyTime2Interval;
															    		}else{
															    			//如果中午不考勤
															    			if (afternoonIsNotVocationOrHoliday) {//如果下午不是节假日，则计算全天的时间作为旷工时间
															    				attendanceException.setTime_interval(statisticDutyTime1DutyTime2Interval+statisticDutyTime3DutyTime4Interval);
															    				//旷工时间+上午+下午
																    			kgTime=kgTime+statisticDutyTime1DutyTime2Interval+statisticDutyTime3DutyTime4Interval;
															    				
																			}else{//如果下午是节假日，则计算上午的时间作为旷工时间
																				attendanceException.setTime_interval(statisticDutyTime1DutyTime2Interval);
																    			kgTime=kgTime+statisticDutyTime1DutyTime2Interval;
																			}
															    			
															    			
															    		}
															    		//缺卡次数+1
														    			cardNotCount++;
															    		attendanceException.setTime_point(timePoint);
															    		 //如果没有插入则插入，如果已经插了记录，则不要再插了
													    				if(attendanceExceptionList.size()==0){
													    					statisticCalService.insertAttendanceException(attendanceException);
													    				}
											    			}
											    		}
											    	}
										    	
					                           }
										    	}else{
										    		//如果没有免打卡设置
										    		if (noCheckSet==null||(noCheckSet!=null&&((noCheckSet.getCheck_point()&1)==0))) {
										    	   //不缺勤
										    		//如果上午上班迟到了
										    		if(dutyTime1CheckDate.compareTo(statisticDutyTime1PointDate)>0){
										    			//如果没有请假
											    		if(!statisticDutyTime1PointDateInLeaveTime){
											    			//如果又没有外出申请
											    			if(!statisticDutyTime1PointDateDateInOutWorkTime){  
											    				//timepoint表示考勤异常时间点
												    				  String timePoint=DateUtil.getDateString(dutyTime1CheckDate);
												    				  String exceptionType="1";//1表示迟到
												    				  //获取某个人某一个时间点的旧有考勤异常
												    				  List<AttendanceException> attendanceExceptionList=getAttendanceExceptionListByPoint(timePoint, exceptionType, employee.getYhDm());
															    			AttendanceException attendanceException=new AttendanceException();
																    		attendanceException.setDuty_time_type("1");//1表示上午上班
																    		//统计到上午上班时间点所在的日期中去
																    		attendanceException.setEveryday_time(yearMonthDay);
																    		attendanceException.setException_type(exceptionType);
																    		attendanceException.setStaff_name(employee.getYhMc());
																    		attendanceException.setStaff_num(employee.getYhDm());
																    		exception_flag=exception_flag&0; //&0表示说明今天考勤异常
																    		//获取上班打卡点和刷卡时间之前的时间减去该时间段内的请假时间作为迟到时间													    				   
													    				     Integer statisticDutyTime1PointDateDutyTime1CheckDate=getSecondsInterval(statisticDutyTime1PointDate, dutyTime1CheckDate)-getTodayLeaveTimeTotal(leaveTimeList, statisticDutyTime1PointDate, dutyTime1CheckDate);
																    		//迟到时间
																    		attendanceException.setTime_interval(statisticDutyTime1PointDateDutyTime1CheckDate);
																    		
																    		if ((kg_type&1)==0) {//如果没有旷工，则统计迟到才有意义
																    			//上午迟到早退时间累计
																	    		morningLateEarlyTime=morningLateEarlyTime+statisticDutyTime1PointDateDutyTime1CheckDate;
																	    		lateTimeTimeTotal=lateTimeTimeTotal+statisticDutyTime1PointDateDutyTime1CheckDate;
																	    		//迟到次数+1
																	    		lateTimeCount++;
																			}
																    		attendanceException.setTime_point(timePoint);
																    		//如果没有插入则插入，如果已经插了记录，则不要再插了
														    				if(attendanceExceptionList.size()==0){
														    					statisticCalService.insertAttendanceException(attendanceException);
														    				  }
											    			}
									    				  }
										    		}
										    	}
										    	}
										    	
										    	
					            }
										    	    //如果中途也要考勤的话
											    	if(getMustCheckCount(attendanceDuty)==4){
											    		if(morningIsNotVocationOrHoliday||extralWorkList.size()>0){
												    		//上午下班班缺勤，过了下午上班时间，上午的下班卡还没有打
												    		if(null==dutyTime2CheckDate){
													    		//如果没有免打卡设置
													    		if (noCheckSet==null||(noCheckSet!=null&&((noCheckSet.getCheck_point()&2)==0))) {
												    			if(now.compareTo(statisticDutyTime3PointDate)>=0){
													    		//如果没有请假
													    		if(!statisticDutyTime2PointDateInLeaveTime){
													    			//如果又没有外出申请
													    			if(!statisticDutyTime2PointDateInOutWork){
													    				//timepoint表示考勤异常点
													    				  String timePoint=DateUtil.getDateString(statisticDutyTime2PointDate);
													    				  String exceptionType="3";//3表示缺卡
													    				  //获取某一个人某一个时间点旧有的考勤异常
													    				  List<AttendanceException> attendanceExceptionList=getAttendanceExceptionListByPoint(timePoint, exceptionType, employee.getYhDm());
																    		AttendanceException attendanceException=new AttendanceException();
																    		attendanceException.setDuty_time_type("2");//2表示早退
																    		//统计到上午上班时间点所在的日期中去
																    		attendanceException.setEveryday_time(yearMonthDay);
																    		attendanceException.setException_type(exceptionType);
																    		attendanceException.setStaff_name(employee.getYhMc());
																    		attendanceException.setStaff_num(employee.getYhDm());
																    		exception_flag=exception_flag&0; //&0表示说明今天考勤异常
																    		//如果旷工时间已经计算到了上午上班中去了
																    		if ((kg_cal_type&1)>0) {
																    			attendanceException.setTime_interval(0);	
																			}else{//如果旷工时间没有计算到上午上班中去,则把上午旷工时间计算到上午下班处
																				kg_cal_type=kg_cal_type|2;
																				attendanceException.setTime_interval(statisticDutyTime1DutyTime2Interval);
																				//旷工时间累加
																    			kgTime=kgTime+statisticDutyTime1DutyTime2Interval;
																			}
																    		//缺卡次数+1
															    			cardNotCount++;
																    		attendanceException.setTime_point(timePoint);
																    		//如果没有插入则插入，如果已经插了记录，则不要再插了
														    				  if(attendanceExceptionList.size()==0){
														    					  statisticCalService.insertAttendanceException(attendanceException);
														    				  }
													    			}
												    		}
												    		}
													    		}
												    	}else{
												    		//如果没有免打卡设置
												    		if (noCheckSet==null||(noCheckSet!=null&&((noCheckSet.getCheck_point()&2)==0))) {
													    	   //不缺勤
													    		//如果上午下班早退了
													    		if(dutyTime2CheckDate.compareTo(statisticDutyTime2PointDate)<0){
													    			//如果没有请假
														    		if(!statisticDutyTime2PointDateInLeaveTime){
														    			//如果又没有外出申请
														    			if(!statisticDutyTime2PointDateInOutWork){
														    				//异常时间点
																    			  String timePoint=DateUtil.getDateString(dutyTime2CheckDate);
																    			  String exceptionType="2";//2 表示早退
																    			  //获取某人某一个时间点旧有的考勤异常
															    				  List<AttendanceException> attendanceExceptionList=getAttendanceExceptionListByPoint(timePoint, exceptionType, employee.getYhDm());
																		    			AttendanceException attendanceException=new AttendanceException();
																			    		attendanceException.setDuty_time_type("2");//2表示上午下班
																			    		//统计到上午上班时间点所在的日期中去
																			    		attendanceException.setEveryday_time(yearMonthDay);
																			    		attendanceException.setException_type(exceptionType);
																			    		attendanceException.setStaff_name(employee.getYhMc());
																			    		attendanceException.setStaff_num(employee.getYhDm());
																			    		exception_flag=exception_flag&0; //&0表示说明今天考勤异常
																			    		//计算上午下班刷卡时间和上午下班考勤点之间除去请假后的早退时间
																			    		 Integer dutyTime2CheckDateStatisticDutyTime2PointDateInterval =getSecondsInterval(dutyTime2CheckDate,statisticDutyTime2PointDate)-getTodayLeaveTimeTotal(leaveTimeList, dutyTime2CheckDate, statisticDutyTime2PointDate);
																			    		
																			    		//早退时间
																			    		attendanceException.setTime_interval(dutyTime2CheckDateStatisticDutyTime2PointDateInterval);
																			    		if ((kg_type&1)==0) { //如果没有旷工，统计早退时间才有意义
																			    			//早上迟到早退时间累计
																				    		morningLateEarlyTime=morningLateEarlyTime+dutyTime2CheckDateStatisticDutyTime2PointDateInterval;
																				    		//早退时间
																				    		earlyTimeTimeTotal=earlyTimeTimeTotal+dutyTime2CheckDateStatisticDutyTime2PointDateInterval;
																				    		//早退时间+1
																				    		earlyTimeCount++;	
																						}
																			    		
																			    		attendanceException.setTime_point(timePoint);
																			    		//如果没有插入则插入，如果已经插了记录，则不要再插了
																	    				if(attendanceExceptionList.size()==0){
																	    					statisticCalService.insertAttendanceException(attendanceException);	
																	    				  	}
														    			}
												    				  }
													    		}
												    		}
													    	}
											    	}
											    		
											    		//如果下午不是公休日和节假日，或者补班
											    	if(afternoonIsNotVocationOrHoliday||extralWorkList.size()>0){											    		
												    	//下午上班缺勤，过了下午考勤旷工记录点还没有打卡
												    	if(null==dutyTime3CheckDate){
												    		//如果没有免打卡设置
												    		if (noCheckSet==null||(noCheckSet!=null&&((noCheckSet.getCheck_point()&4)==0))) {
													    		if(now.compareTo(statisticDutyTime3Absent)>=0){
														    		//如果没有请假
														    		if(!statisticDutyTime3PointDateInLeaveTime){
														    			//如果又没有外出申请
														    			if(!statisticDutyTime3PointDateInOutWork){
														    				//异常时间点
														    				 String timePoint=DateUtil.getDateString(statisticDutyTime3PointDate);
														    				 String exceptionType="3";//表示缺卡
														    				 //获取某一个人某一个时间点的考勤异常
														    				  List<AttendanceException> attendanceExceptionList=getAttendanceExceptionListByPoint(timePoint, exceptionType, employee.getYhDm());
																			    		AttendanceException attendanceException=new AttendanceException();
																			    		attendanceException.setDuty_time_type("3");//表示是下午上班
																			    		//统计到上午上班时间点所在的日期中去
																			    		attendanceException.setEveryday_time(yearMonthDay);
																			    		attendanceException.setException_type(exceptionType);
																			    		attendanceException.setStaff_name(employee.getYhMc());
																			    		attendanceException.setStaff_num(employee.getYhDm());
																			    		exception_flag=exception_flag&0; //&0表示说明今天考勤异常
																			    		//下午旷工时间计算到下午上班点中去 
																			    		kg_cal_type=kg_cal_type|4;
																			  			attendanceException.setTime_interval(statisticDutyTime3DutyTime4Interval);
																			  			//旷工时间累加
																		    			kgTime=kgTime+statisticDutyTime3DutyTime4Interval;
																			    		
																			    	    //缺卡次数+1
																			    		cardNotCount++;
																			    		attendanceException.setTime_point(timePoint);
																			    		//如果没有插入则插入，如果已经插了记录，则不要再插了
																	    				  if(attendanceExceptionList.size()==0){
																	    					  statisticCalService.insertAttendanceException(attendanceException);
																	    				  }
														    			}
													    		}
													    	}
												    		}
												    	}else{
												    		//如果没有免打卡设置
												    		if (noCheckSet==null||(noCheckSet!=null&&((noCheckSet.getCheck_point()&4)==0))) {
													    	   //不缺勤
													    		//如果下午上班迟到了
													    		if(dutyTime3CheckDate.compareTo(statisticDutyTime3PointDate)>0){
													    			//如果没有请假
														    		if(!statisticDutyTime3PointDateInLeaveTime){
														    			//如果又没有外出申请
														    			if(!statisticDutyTime3PointDateInOutWork){
														    				//timepoint为异常时间点
															    			  String timePoint=DateUtil.getDateString(dutyTime3CheckDate);
															    			  String exceptionType="1";//1表示是迟到
															    			  //获取某一个人某一个时间点旧有的考勤异常。
														    				  List<AttendanceException> attendanceExceptionList=getAttendanceExceptionListByPoint(timePoint, exceptionType, employee.getYhDm());
																		    			AttendanceException attendanceException=new AttendanceException();
																			    		attendanceException.setDuty_time_type("3");//3表示是下午上班
																			    		//统计到上午上班时间点所在的日期中去
																			    		attendanceException.setEveryday_time(yearMonthDay);
																			    		attendanceException.setException_type(exceptionType);
																			    		attendanceException.setStaff_name(employee.getYhMc());
																			    		attendanceException.setStaff_num(employee.getYhDm());
																			    		exception_flag=exception_flag&0; //&0表示说明今天考勤异常
																			    		
																			    		//计算下午上班考勤点时间与下午上班刷卡时间间除去请假时间的迟到时间
																			    		
																			    		Integer statisticDutyTime3PointDateDutyTime3CheckDateInterval=getSecondsInterval(statisticDutyTime3PointDate,dutyTime3CheckDate)-getTodayLeaveTimeTotal(leaveTimeList, statisticDutyTime3PointDate, dutyTime3CheckDate);
																			    		
																			    		//迟到时间
																			    		attendanceException.setTime_interval(statisticDutyTime3PointDateDutyTime3CheckDateInterval);
																			    		if ((kg_type&2)==0) { //如果没有旷工，统计迟到时间才有意义
																				    		//下午迟到早退统计
																				    		afternoonLateEarlyTime=afternoonLateEarlyTime+statisticDutyTime3PointDateDutyTime3CheckDateInterval;
																				    		//迟到时间累计
																				    		lateTimeTimeTotal=lateTimeTimeTotal+statisticDutyTime3PointDateDutyTime3CheckDateInterval;
																				    		//迟到次数加1
																				    		lateTimeCount++;
																			    		}
																			    		attendanceException.setTime_point(timePoint);
																			    		//如果没有插入则插入，如果已经插了记录，则不要再插了
																	    				if(attendanceExceptionList.size()==0){
																	    					statisticCalService.insertAttendanceException(attendanceException);
																	    				  	}
														    			}
												    				  }
													    		}
													    	}
												    	}
											    	}
										    	
										    }
											    	
										   if(afternoonIsNotVocationOrHoliday||extralWorkList.size()>0){ 	
										    	//下午下班缺勤,过了下班统计时间，还没有打卡
										    	if(null==dutyTime4CheckDate){
										    		//如果没有免打卡设置
										    		if (noCheckSet==null||(noCheckSet!=null&&((noCheckSet.getCheck_point()&8)==0))) {
										    		//过了考勤统计结束时间还没有打卡
											    		if(now.compareTo(statisticEndCheckPoint)>=0){
											    		//如果也没有请假
											    		if(!statisticDutyTime4PointDateInLeaveTime){
											    			//如果又没有外出工作
											    			if(!statisticDutyTime4PointDateInOutWork){
											    				//timepoint为异常时间点
											    				  String timePoint=DateUtil.getDateString(statisticDutyTime4PointDate);
											    				  String exceptionType="3";//3表示缺卡
											    				  //查询某个人在某一个时间点旧有的考勤异常
											    				  List<AttendanceException> attendanceExceptionList=getAttendanceExceptionListByPoint(timePoint, exceptionType, employee.getYhDm());
															    		AttendanceException attendanceException=new AttendanceException();
															    		attendanceException.setDuty_time_type("4");//4表示下午下班
															    		//统计到上午上班时间点所在的日期中去
															    		attendanceException.setEveryday_time(yearMonthDay);
															    		attendanceException.setException_type(exceptionType);
															    		attendanceException.setStaff_name(employee.getYhMc());
															    		attendanceException.setStaff_num(employee.getYhDm());
															    		exception_flag=exception_flag&0; //&0表示说明今天考勤异常
															    		//旷工时间
															    		if(getMustCheckCount(attendanceDuty)==4){
															    			if ((kg_cal_type&4)>0) {
																    			//说明下午旷工时间已经计算到下午上班的时间点去了
															    				attendanceException.setTime_interval(0);
																			}else{//把下午旷工时间计算到下午下班点去
																				kg_cal_type=kg_cal_type|8;
																	    		attendanceException.setTime_interval(statisticDutyTime3DutyTime4Interval);
																	    		kgTime=kgTime+statisticDutyTime3DutyTime4Interval;
																			}
															    			 
//															    			
															    		}else{
															    			 
															    			if ((kg_cal_type&1)>0) {
																    			//对于中途不考勤时，当旷工时间已经计算到上午上班中去时
															    				attendanceException.setTime_interval(0);
																			}else{
																				
																				
																				if (morningIsNotVocationOrHoliday) {//如果早上不是节假日 ，则旷工时间算 全天
																					attendanceException.setTime_interval(statisticDutyTime1DutyTime2Interval+statisticDutyTime3DutyTime4Interval);
																    				kgTime=kgTime+statisticDutyTime1DutyTime2Interval+statisticDutyTime3DutyTime4Interval;
																				}else{ //如果早上是节假日，则旷工时间只算下午的时间
																					attendanceException.setTime_interval(statisticDutyTime3DutyTime4Interval);
																    				kgTime=kgTime+statisticDutyTime3DutyTime4Interval;
																				} 
																				
																			}
															    			
															    			
															    		}
															    		//缺打卡数+1
															    		cardNotCount++;
															    		attendanceException.setTime_point(timePoint);
															    		//如果没有插入则插入，如果已经插了记录，则不要再插了
													    				  if(attendanceExceptionList.size()==0){
													    					  statisticCalService.insertAttendanceException(attendanceException);
													    				  }
											    			}
											    		}
											    	}
										    		}
										    	}else{
										    		//如果没有免打卡设置
										    		if (noCheckSet==null||(noCheckSet!=null&&((noCheckSet.getCheck_point()&8)==0))) {
											    	     //不缺勤
											    		//如果下午下班早退了
											    		if(dutyTime4CheckDate.compareTo(statisticDutyTime4PointDate)<0){
											    			//如果没有请假
												    		if(!statisticDutyTime4PointDateInLeaveTime){
												    			//如果又没有外出申请
												    			if(!statisticDutyTime4PointDateInOutWork){
													    			  String timePoint=DateUtil.getDateString(dutyTime4CheckDate);//timepoint表示异常时间点
													    			  String exceptionType="2";//2 表示早退
													    			  //查询某个人某一个时间点的原有异常列表
												    				  List<AttendanceException> attendanceExceptionList=getAttendanceExceptionListByPoint(timePoint, exceptionType, employee.getYhDm());
															    			AttendanceException attendanceException=new AttendanceException();
																    		attendanceException.setDuty_time_type("4");//4表示下午下班
																    		//统计到上午上班时间点所在的日期中去
																    		attendanceException.setEveryday_time(yearMonthDay);
																    		attendanceException.setException_type(exceptionType);
																    		attendanceException.setStaff_name(employee.getYhMc());
																    		attendanceException.setStaff_num(employee.getYhDm());
																    		exception_flag=exception_flag&0; //&0表示说明今天考勤异常
																    		//计算下午下班考勤点与下午下班刷卡记录之间除去请假的早退时间
																    		Integer dutyTime4CheckDateStatisticDutyTime4PointDateInterval=getSecondsInterval(dutyTime4CheckDate,statisticDutyTime4PointDate)-getTodayLeaveTimeTotal(leaveTimeList, dutyTime4CheckDate, statisticDutyTime4PointDate);
																    		//迟到时间
																    		attendanceException.setTime_interval(dutyTime4CheckDateStatisticDutyTime4PointDateInterval);
																    		
																    		if ((kg_type&2)==0) { //如果没有旷工，统计早退时间才有意义
																	    		//下午迟到早退累计
																	    		afternoonLateEarlyTime=afternoonLateEarlyTime+dutyTime4CheckDateStatisticDutyTime4PointDateInterval;
																	    		//早退时间
																	    		earlyTimeTimeTotal=earlyTimeTimeTotal+dutyTime4CheckDateStatisticDutyTime4PointDateInterval;
																	    		//早退次数+1
																	    		earlyTimeCount++;
																    		}
																    		attendanceException.setTime_point(timePoint);
																    		//如果没有插入则插入，如果已经插了记录，则不要再插了
														    				 if(attendanceExceptionList.size()==0){
														    					 statisticCalService.insertAttendanceException(attendanceException);	
														    				  }
												    			}
										    				  }
											    		}
											    	}
										    	}
					            }
			 
					           
					    	
					    	
						    
						    //设置旷工次数=====计算开始======
										   
						    if(getMustCheckCount(attendanceDuty)==4){//如果中午也要打卡
						    	if (kg_type==1||kg_type==2) {
						    		//如果是上午或下午旷工，则旷工一次
						    		attendanceDataDate.setKg_count(1);
								}else if(kg_type==3){
									//如果中途也要考勤，旷工类型为全天，则旷工次数为二次
								   attendanceDataDate.setKg_count(2);
								}
						    	
						    }else{
						   
						    	if (kg_type==3) {
						    		//如果中途不考勤，旷工类型为全天，则旷工次数为一次
						    		attendanceDataDate.setKg_count(1);
								}
						    }
					    	
						    //设置旷工类型 
						    attendanceDataDate.setKg_type(kg_type);
						    
						    //设置异常类型
						    attendanceDataDate.setException_flag(exception_flag);
						    
						   
							   
						    
						    //晚上加班时间计算,如果下午打卡时间在加班开始计算之后 ，则说明加过班了，计算加班时间
						   
						    Double overTimeTotal=0.0; 
						    { 
						    	 //是否可以计算加班
								   boolean cancaculateOvertime=false;

								   if (getMustCheckCount(attendanceDuty)==4) {
									   //如果下午不旷工，晚上才有资格计算加班
									   	if ((kg_type&2)==0) {
											cancaculateOvertime=true;
										}
							     	}else{
							     		//如果没有旷工才有资格计算加班
							     		if ((kg_type&3)==0) {
											cancaculateOvertime=true;
										}
							     	}
						    	
						    	//如果晚上有资格计算加班
						    	if (cancaculateOvertime) {	
										    	//块1  晚上加班时间计算
										    	Date outworkStartDate=statisticOverTimeStart;
										    Date outworkEndDate=statisticOverTimeStart;
										    //计算加班时间统计界限
										       if(null!=dutyTime4CheckDate&&statisticOverTimeStart!=null){
										    	   if(dutyTime4CheckDate.after(statisticOverTimeStart)){
										    		   if(dutyTime4CheckDate.after(statisticOverTimeEnd)){
										    			   outworkEndDate=statisticOverTimeEnd;
											    	   }else{
											    		   outworkEndDate= dutyTime4CheckDate;
											    	   }
										    	   } 
										    	   
												    {
												      //请假时间并集列表与加班时间界限outworkStartDate,outworkEndDate的并集A
						                      	    List<LeaveTime> leaveTimeStartEndList=	getLeaveTimeListStartEndWithNoCascade(leaveTimeList, outworkStartDate, outworkEndDate);
						                      	    //计算并集A的时间总和
											    	  Integer leaveTimeStartEndTotal=  getTodayLeaveTimeTotal(leaveTimeStartEndList);
											    	  //获取请假的并集组成集合B
											    	  List<LeaveTime> leaveTimeNoCascadeList=getLeaveTimeListWithNoCascade(leaveTimeList);
											    	  //获取集合B的时间总和
											    	  Integer leaveTimeTotal=  getTodayLeaveTimeTotal(leaveTimeNoCascadeList);
											    	  //计算加班时间
											    	  if(otList.size()>0){
											    		  //如果提了加班申请，则按加班申请流程计算加班时间
											    		  overTimeTotal=getTodayOtTimeTotal(otList,statisticStartCheckPoint,statisticEndCheckPoint)*1.0/60/60;
											    	  }else{
											    		  //如果没有提加班申请，则计算加班时间
											    		  overTimeTotal=(leaveTimeStartEndTotal-leaveTimeTotal)*1.0/60/60.0;
											    	  }
												    } 
										       }
						    	}
					            }
						     
						       //计算上午加班时间
						    if (!(morningIsNotVocationOrHoliday||extralWorkList.size()>0)) {
						    	
						    	
						       { //上午加班时间计算 
						    	   Date outworkStartDate=null;
							       Date outworkEndDate=null;
							    
							    //如果中午也要考勤 
							    if (getMustCheckCount(attendanceDuty)==4) {
							    	//如果上午都不缺卡
									 if (dutyTime1CheckDate!=null&&dutyTime2CheckDate!=null) {
										outworkStartDate=dutyTime1CheckDate;
										outworkEndDate=dutyTime2CheckDate;
									}
								}else{
									//如果中午不考勤 
									 if (dutyTime1CheckDate!=null) {  
										outworkStartDate=dutyTime1CheckDate;
										outworkEndDate=statisticDutyTime2;
									}
								}
							    
							    
							    
							    //计算加班时间统计界限
							       if(null!=outworkStartDate&&outworkEndDate!=null){
							    	  
							    	   
									    {
									      //取请假外出时间的并集列表与加班时间界限outworkStartDate, outworkEndDate的并集A
			                      	    List<LeaveTime> leaveTimeStartEndList=	getLeaveTimeListStartEndWithNoCascade(leaveTimeList, outworkStartDate, outworkEndDate);
			                      	    //计算并集A的时间总和
								    	  Integer leaveTimeStartEndTotal=  getTodayLeaveTimeTotal(leaveTimeStartEndList);
								    	  //获取请假的并集集合B
								    	  List<LeaveTime> leaveTimeNoCascadeList=getLeaveTimeListWithNoCascade(leaveTimeList);
								    	  //获取请假的并集组成集合B的时间总和
								    	  Integer leaveTimeTotal=  getTodayLeaveTimeTotal(leaveTimeNoCascadeList);
								    	  //计算加班时间
								    	  if(otList.size()>0){
								    		  //如果提了加班申请，则按加班申请流程计算加班时间
								    		  overTimeTotal=getTodayOtTimeTotal(otList,statisticStartCheckPoint,statisticEndCheckPoint)*1.0/60/60;
								    	  }else{
								    		  //如果没有提加班申请，则计算加班时间
								    		  overTimeTotal=(leaveTimeStartEndTotal-leaveTimeTotal)*1.0/60/60.0;
								    	  }
									    } 
							       }
						       }
					            }
						       
						       
						    
						    
						    //计算下午加班时间
						    if (!(afternoonIsNotVocationOrHoliday||extralWorkList.size()>0)) {
						       { //上午加班时间计算 
						    	   Date outworkStartDate=null;
							    Date outworkEndDate=null;
							    
							    //如果中午也要考勤 
							    if (getMustCheckCount(attendanceDuty)==4) {
							    	//如果下午都不缺卡
									 if (dutyTime3CheckDate!=null&&dutyTime4CheckDate!=null) {
										outworkStartDate=dutyTime3CheckDate;
										outworkEndDate=dutyTime4CheckDate;
									}
								}else{
									//如果中午不考勤 
									 if (dutyTime4CheckDate!=null) {  
										outworkStartDate=statisticDutyTime3;
										outworkEndDate=dutyTime4CheckDate;
									}
								}
							    
							    
							    
							    //计算加班时间统计界限
							       if(null!=outworkStartDate&&outworkEndDate!=null){
							    	  
							    	   
									    {
									      //取请假时间的并集列表与加班时间界限outworkStartDate, outworkEndDate的并集A
			                      	    List<LeaveTime> leaveTimeStartEndList=	getLeaveTimeListStartEndWithNoCascade(leaveTimeList, outworkStartDate, outworkEndDate);
			                      	    //计算并集A的时间总和
								    	  Integer leaveTimeStartEndTotal=  getTodayLeaveTimeTotal(leaveTimeStartEndList);
								    	  //获取请假，外出的并集组成集合B
								    	  List<LeaveTime> leaveTimeNoCascadeList=getLeaveTimeListWithNoCascade(leaveTimeList);
								    	  //集合B的时间总和
								    	  Integer leaveTimeTotal=  getTodayLeaveTimeTotal(leaveTimeNoCascadeList);
								    	  //计算加班时间
								    	  if(otList.size()>0){
								    		  //如果提了加班申请，则按加班申请流程计算加班时间
								    		  overTimeTotal=getTodayOtTimeTotal(otList,statisticStartCheckPoint,statisticEndCheckPoint)*1.0/60/60;
								    	  }else{
								    		//如果没有提加班申请，则计算加班时间
								    		  overTimeTotal=(leaveTimeStartEndTotal-leaveTimeTotal)*1.0/60/60.0;
								    	  }
									    } 
							       }
						       }
					            }
						    
						       //设置加班时间
						        attendanceDataDate.setOver_time(overTimeTotal) ;
						        //设置缺卡次数
						        attendanceDataDate.setCard_not(cardNotCount);
						        //设置迟到次数
						        attendanceDataDate.setLater_count(lateTimeCount);
						        //设置迟到时间总数
							    attendanceDataDate.setLater_time(lateTimeTimeTotal/60.0/60.0);
							    //设置早退次数
							    attendanceDataDate.setEarly_count(earlyTimeCount);
							    //设置早退时间总数
							    attendanceDataDate.setEarly_time(earlyTimeTimeTotal/60.0/60.0);
							    
							    
							    
							    
							    
							  //修正旷工时间,要去掉请假的时间
							    if(kg_type==1){
							    	//如果是上午旷工，上 午的请假时间不应算作旷工
							    	int morningLeaveTime=getTodayLeaveTimeTotal(leaveTimeList, statisticDutyTime1, statisticDutyTime2);
							    	kgTime=kgTime-morningLeaveTime;
							    }else if(kg_type==2){
							    	//如下 下午旷工，则下午的请假时间不应算作 旷工
							    	int afternoonLeaveTime=getTodayLeaveTimeTotal(leaveTimeList, statisticDutyTime3, statisticDutyTime4);
							    	kgTime=kgTime-afternoonLeaveTime;
							    	
							    }else if(kg_type==3){
							    	//如果全天旷工，则今天 的请假时间不应算作旷工
							    	int leaveTime=getTodayLeaveTimeTotal(leaveTimeList, statisticDutyTime1, statisticDutyTime2)+getTodayLeaveTimeTotal(leaveTimeList, statisticDutyTime3, statisticDutyTime4);
							    	kgTime=kgTime-leaveTime;
							    }
							    
							    
							    
							    
							    
							    
							    //设置旷工时间总数
							    Double kg_time=kgTime/60.0/60.0;
							    attendanceDataDate.setKg_time(kg_time);
			
							    
							  //统计当天外出时间
							    Double outTotalTime=0.0; 
							    
							    
							        //如果早上不是公休日也没有假期或当日要补班
							        if (morningIsNotVocationOrHoliday||extralWorkList.size()>0) {
							    		//获取外出时间的并集A,并使集合中的每个元素限制在时间statisticDutyTime1,statisticDutyTime2范围内
								    	  List<OutWork> outWorkStartEndList=	geOutWorkListStartEndWithNoCascade(outWorkList,statisticDutyTime1,statisticDutyTime2);
	                                      //迭代外出列表，刨掉每个外出时间中的请假部分
								    	  for(OutWork ow:outWorkStartEndList){
	                                    	  
	                                    	  Date owStartDate=DateUtil.getDateTime(ow.getFromTime());
	                                    	  Date owEndDate=DateUtil.getDateTime(ow.getToTime());
	                                    	  //获取集合A的一个元素与请假时间列表的并集B
	                                    	  List<LeaveTime> leaveTimeStartEndList=	getLeaveTimeListStartEndWithNoCascade(leaveTimeList, owStartDate, owEndDate);
	                                    	  //并集B组成的时间总和
	    							    	  Integer leaveTimeOutWorkTotal=  getTodayLeaveTimeTotal(leaveTimeStartEndList);
	    							    	  //获取请假列表的集合
	    							    	  List<LeaveTime> leaveTimeOutWorkTimeList=getLeaveTimeListWithNoCascade(leaveTimeList);
	    							    	  //获取请假列表的集合时间总和
	    							    	  Integer leaveTimeTotal=  getTodayLeaveTimeTotal(leaveTimeOutWorkTimeList);
	    							    	  //刨掉外出时间中的请假部分
	    							    	  Double totalM=(leaveTimeOutWorkTotal-leaveTimeTotal)*1.0/60/60.0;
	    							    	  //累加
	    							    	  outTotalTime=outTotalTime+totalM;  
	                                      }
							        }
							        //如果下午不是公休日也没有假期或当日要补班
							        if (afternoonIsNotVocationOrHoliday||extralWorkList.size()>0) {
							        	//获取外出时间并集A,并使集合中的每个元素限制在时间statisticDutyTime3,statisticDutyTime4范围内
								    	  List<OutWork> outWorkStartEndList2=	geOutWorkListStartEndWithNoCascade(outWorkList,statisticDutyTime3,statisticDutyTime4);
								    	   //  迭代外出列表，刨掉每个外出时间中的请假部分
								    	  for(OutWork ow:outWorkStartEndList2){
	                                    	  
	                                    	  Date owStartDate=DateUtil.getDateTime(ow.getFromTime());
	                                    	  Date owEndDate=DateUtil.getDateTime(ow.getToTime());
	                                    	//获取集合A的一个元素与请假时间列表的并集B
	                                    	  List<LeaveTime> leaveTimeStartEndList=	getLeaveTimeListStartEndWithNoCascade(leaveTimeList, owStartDate, owEndDate);
	                                    	  //并集B组成的时间总和
	    							    	  Integer leaveTimeOutWorkTotal=  getTodayLeaveTimeTotal(leaveTimeStartEndList);
	    							    	  //获取请假列表的集合
	    							    	  List<LeaveTime> leaveTimeOutWorkTimeList=getLeaveTimeListWithNoCascade(leaveTimeList);
	    							    	  //获取请假列表的集合时间总和
	    							    	  Integer leaveTimeTotal=  getTodayLeaveTimeTotal(leaveTimeOutWorkTimeList);
	    							    	  //刨掉外出时间中的请假部分
	    							    	  Double totalM=(leaveTimeOutWorkTotal-leaveTimeTotal)*1.0/60/60;
	    							    	  //累加
	    							    	  outTotalTime=outTotalTime+totalM;  
	                                      }
									}
                                    
                                      
                                      
							    attendanceDataDate.setOut_total_time(outTotalTime);
							    
 
							    
							    
							    
							    
							    //统计当天请假时间      
							    Double leaveTimeTotalTime=0.0;
							    //如果上午不是公休日也不是节假日，或当日需要补班
							    if (morningIsNotVocationOrHoliday||extralWorkList.size()>0) {
									leaveTimeTotalTime=leaveTimeTotalTime+(getTodayLeaveTimeTotal(leaveTimeList,statisticDutyTime1,statisticDutyTime2))*1.0/60.0/60;
								}
							    //如果下午不是公休日也不是节假日，或需要补 班
							    if (afternoonIsNotVocationOrHoliday||extralWorkList.size()>0) {
									leaveTimeTotalTime=leaveTimeTotalTime+(getTodayLeaveTimeTotal(leaveTimeList,statisticDutyTime3,statisticDutyTime4))*1.0/60.0/60;
								}
							    attendanceDataDate.setLeave_total_time(leaveTimeTotalTime);
							  //今天应该上满多少小时数
							    Double mustKeepHour=0.0;
							    if(morningIsNotVocationOrHoliday||extralWorkList.size()>0){
							    	//将秒转化为小时,累加
								     mustKeepHour=mustKeepHour+(statisticDutyTime1DutyTime2Interval)*1.0/60.0/60;

							    }
							    //如果当天下午不是公休日或节假日或当天为补班
							    if (afternoonIsNotVocationOrHoliday||extralWorkList.size()>0) {
								     mustKeepHour=mustKeepHour+(getSecondsInterval(statisticDutyTime3,statisticDutyTime4))*1.0/60.0/60;

								}
							    
							    
							    
							    attendanceDataDate.setShould_work_time(mustKeepHour);
							    
							    
							    //=========================================
							    
							  //定义实际工作时间变量
							    Double inFactKeepHour=0.0;
							    
							    
							    //确定默认计算实际工作时间界限，如果这四个值分别初始为上午上班，上午下班，下午上班，下午下班的刷卡记录时间
							    //早上上班
							    Date morningLeaveOutStartDate=dutyTime1CheckDate;
							    //早上下班
							    Date morningLeaveOutEndDate=dutyTime2CheckDate;
							    //下午上班
							    Date afternoonLeaveOutStartDate=dutyTime3CheckDate;
							    //下午下班
							    Date afternoonLeaveOutEndDate=dutyTime4CheckDate;
//							    
							    

							    
							    //如果早于上午上班时间，则取上午上班时间
							    if((null!=morningLeaveOutStartDate&&morningLeaveOutStartDate.before(statisticDutyTime1))){  
							    	morningLeaveOutStartDate=statisticDutyTime1;
							    }
							    //如果早于上午下班时间，则取上午下时间
							    if((null!=morningLeaveOutEndDate&&morningLeaveOutEndDate.after(statisticDutyTime2))){  
							    	morningLeaveOutEndDate=statisticDutyTime2;
							    }
							    //如果早于下午上班时间，则取下午上班时间
							    if((null!=afternoonLeaveOutStartDate&&afternoonLeaveOutStartDate.before(statisticDutyTime3))){
							    	afternoonLeaveOutStartDate=statisticDutyTime3;
							    }
							  //如果早于下午下班时间，则取下午下班时间
							    if((null!=afternoonLeaveOutEndDate&&afternoonLeaveOutEndDate.after(statisticDutyTime4))){
							    	afternoonLeaveOutEndDate=statisticDutyTime4;
							    }
							    //如果早上是外出或免打卡,则默认取上午上班时间
							    if(statisticDutyTime1PointDateInLeaveTime||statisticDutyTime1PointDateDateInOutWorkTime||(noCheckSet!=null&&((noCheckSet.getCheck_point()&1)>0))){//noCheckSet为免打卡设置
							    	morningLeaveOutStartDate=statisticDutyTime1;
							    }
							    //如果中午要考勤
							    if(getMustCheckCount(attendanceDuty)==4){
							    	 //如果上午下班是外出或免打卡,则默认取上午下班时间
							    	if(statisticDutyTime2PointDateInLeaveTime||statisticDutyTime2PointDateInOutWork||(noCheckSet!=null&&((noCheckSet.getCheck_point()&2)>0))){
							    		morningLeaveOutEndDate=statisticDutyTime2;	
							    	}
							    	 //如果下午上班是请假或外出或免打卡，则取下午上班时间
							    	if(statisticDutyTime3PointDateInLeaveTime||statisticDutyTime3PointDateInOutWork||(noCheckSet!=null&&((noCheckSet.getCheck_point()&4)>0))){
							    		afternoonLeaveOutStartDate=statisticDutyTime3;	
							    	}
							    }else{
							    	//如果中途不考勤，则默认取上午下班时间
							    	morningLeaveOutEndDate=statisticDutyTime2;	
							    	//如果中途不考勤，则默认取下午上班时间
							    	afternoonLeaveOutStartDate=statisticDutyTime3;	
							    }
							    //如果下午下班是请假或外出，或免打卡，则取下午下班时间
							    if(statisticDutyTime4PointDateInLeaveTime||statisticDutyTime4PointDateInOutWork||(noCheckSet!=null&&((noCheckSet.getCheck_point()&8)>0))){
							    	afternoonLeaveOutEndDate=statisticDutyTime4;
							    }
							    
							    
							    
							    
	                             //如果上午不是公休日或者当天补班
							    
							    if ((kg_type&1)==0) {//如果上午没有旷工，则计算上午正班工作时间	
									    if (morningIsNotVocationOrHoliday||extralWorkList.size()>0) {	
										    	//如果上午没有旷工,则需要计算除去请假的实际工作时间
										    	if(morningLeaveOutStartDate!=null&&morningLeaveOutEndDate!=null){	
										    		//获取相互之间没有交集的外出申请,同时将工作时间段也作为外出申请的一部分进行统计得到集合A
											    	  List<OutWork> morningOutWorkStartEndList=	geOutWorkListStartEndWithNoCascadeAsWork(outWorkList, morningLeaveOutStartDate, morningLeaveOutEndDate);
											    	  //刨掉集合A中的每一个元素中的请假时间，得到每个元素中实际工作时间,然后累加和就是实际工作时间
				                                      for(OutWork ow:morningOutWorkStartEndList){
				                                    	  
				                                    	  Date owStartDate=DateUtil.getDateTime(ow.getFromTime());
				                                    	  Date owEndDate=DateUtil.getDateTime(ow.getToTime());
				                                    	  //获取每个独立的外出申请与所有假期组成的交集List
				                                    	  List<LeaveTime> leaveTimeStartEndList=	getLeaveTimeListStartEndWithNoCascade(leaveTimeList, owStartDate, owEndDate);
				                                    	  //获取每个独立的外出申请与所有假期组成的交集List的时间间距总和
				    							    	  Integer leaveTimeOutWorkTotal=  getTodayLeaveTimeTotal(leaveTimeStartEndList);
				    							    	  //获取请假列表的集合
				    							    	  List<LeaveTime> leaveTimeOutWorkTimeList=getLeaveTimeListWithNoCascade(leaveTimeList);
				    							    	  //获取请假列表的集合时间间距总和
				    							    	  Integer leaveTimeTotal=  getTodayLeaveTimeTotal(leaveTimeOutWorkTimeList);
				    							    	  //计算每个独立的外出申请记录与所有假期的差集时间间距
				    							    	  Double totalM=(leaveTimeOutWorkTotal-leaveTimeTotal)*1.0/60/60;
				    							    	  //累加
				    							    	  inFactKeepHour=inFactKeepHour+totalM;  
				                                      }
										    	}
									    
							            }
							    }
							    
							    if ((kg_type&2)==0) {  //如果下午不旷工，则计算下午的正班工作时间	,如果旷工了下午的正班工作时间就不用计算了，应该为0
									    if (afternoonIsNotVocationOrHoliday||extralWorkList.size()>0) {
									    	//如果下午没有旷工,则需要计算除去请假的实际工作时间
									    	if(afternoonLeaveOutStartDate!=null&&afternoonLeaveOutEndDate!=null){	
									    		//获取相互之间没有交集的外出申请,同时将工作时间段也作为外出申请的一部分进行统计
										    	  List<OutWork> morningOutWorkStartEndList=	geOutWorkListStartEndWithNoCascadeAsWork(outWorkList, afternoonLeaveOutStartDate, afternoonLeaveOutEndDate);
			                                      for(OutWork ow:morningOutWorkStartEndList){
			                                    	  
			                                    	  Date owStartDate=DateUtil.getDateTime(ow.getFromTime());
			                                    	  Date owEndDate=DateUtil.getDateTime(ow.getToTime());
			                                    	  //获取每个独立的外出申请与所有假期组成的交集List
			                                    	  List<LeaveTime> leaveTimeStartEndList=	getLeaveTimeListStartEndWithNoCascade(leaveTimeList, owStartDate, owEndDate);
			                                    	  //获取每个独立的外出申请与所有假期组成的交集List的时间间距总和
			    							    	  Integer leaveTimeOutWorkTotal=  getTodayLeaveTimeTotal(leaveTimeStartEndList);
			    							    	  //获取请假列表的集合
			    							    	  List<LeaveTime> leaveTimeOutWorkTimeList=getLeaveTimeListWithNoCascade(leaveTimeList);
			    							    	  //获取请假列表的集合时间间距总和
			    							    	  Integer leaveTimeTotal=  getTodayLeaveTimeTotal(leaveTimeOutWorkTimeList);
			    							    	  //计算每个独立的外出申请记录与所有假期的差集时间间距
			    							    	  Double totalM=(leaveTimeOutWorkTotal-leaveTimeTotal)*1.0/60/60;
			    							    	  //累加
			    							    	  inFactKeepHour=inFactKeepHour+totalM;  
			                                      }
									    	}	
										}
							    }
						    	 attendanceDataDate.setWork_time(inFactKeepHour); 
							    //===============================================

							    
							    //统计今天是上班还是放假
							    attendanceDataDate.setToday_work("1");
						            
		
					    
					    
				    	}else{
						    //获取当天最早打卡时间
						     Date earlyest=getEarlyestCheckTime(attendanceCheckList); 
						     //获取当天最晚打卡时间
						     Date latest=getLatestCheckTime(attendanceCheckList);
     
						     //下面是对attendanceDataDate的上下班时间进行修正,主要原因是节假日公休日全天记录都可以参与考勤计算,之前的基于班次算出来的上下班时间就需要修正===================开始
						     if (earlyest!=null) {
						    	
								if (earlyest.compareTo(statisticDutyTime2)<=0) {
									 //如果是上午下班时间之前来的,则earlyest作为上午上班时间
								     attendanceDataDate.setWork_begin_time1(DateUtil.getDateString(earlyest)); 
								}else if(earlyest.compareTo(statisticDutyTime3)>=0){
									//如果是下午上班之后来的,则earlyest作为下午上班时间
									attendanceDataDate.setWork_begin_time2(DateUtil.getDateString(earlyest));
								}
							 }else{
								 //如果不存在最早打卡时间，说明缺卡，认为上午上班，下午上班都没有打卡
								 attendanceDataDate.setWork_begin_time1(null);
								 attendanceDataDate.setWork_begin_time2(null);
							 } 

						     //计算这个最晚时间落在哪一个点
						      if (latest!=null) {
								if (latest.compareTo(statisticDutyTime2)<=0) {
									//如果是上午下班之前走的，则latest作为上午下班时间
									attendanceDataDate.setWork_end_time1(DateUtil.getDateString(latest));
								}else if(latest.compareTo(statisticDutyTime3)>=0){
									//如果是下午上班之后走的，则latest作为下午下班时间
									attendanceDataDate.setWork_end_time2(DateUtil.getDateString(latest));
								}
							}else{
								//如果不存在最晚打卡时间，则说明缺卡，认为上午下班，下午下班都没有打卡
								attendanceDataDate.setWork_end_time1(null);
								attendanceDataDate.setWork_end_time2(null);
							}
						     
    						 //将latest 作为下午下班时间  进行修正
						     attendanceDataDate.setWork_end_time2(DateUtil.getDateString(latest));
						     //=========================================================================修正结束===============

                             int overTimeTotal=0;
                             Date date1=null;
						     Date date2=null;

						     Date date3=null;
						     Date date4=null;
						     Date date5=null;
						     Date date6=null;
						     //如果上下班都有打卡
						     if (earlyest!=null&&latest!=null) {
						    	         //获取两个时间段的交集,如果没有交集返回空值
								     Map<String, Date>  map1=getIntersectDate(statisticDutyTime1,statisticDutyTime2, earlyest, latest);
								      date1=map1.get("start");
								      date2=map1.get("end");
								    //获取两个时间段的交集,如果没有交集返回空值
								     Map<String, Date>  map2=getIntersectDate(statisticDutyTime3,statisticDutyTime4, earlyest, latest);
		
								      date3=map2.get("start");
								      date4=map2.get("end");
								    //获取两个时间段的交集,如果没有交集返回空值
								     Map<String, Date>  map3=getIntersectDate(statisticOverTimeStart,statisticOverTimeEnd, earlyest, latest);
								      date5=map3.get("start");
								      date6=map3.get("end");
						     }
								     
		
								     //上午加班时间计算
		                             {
								     //获取外出列表的并集A,并使集合A中的每一个元素限制在上午上班时间statisticDutyTime1,上午下班时间statisticDutyTime2范围内
								     List<OutWork> outWorkStartEndList=	geOutWorkListStartEndWithNoCascade(outWorkList, statisticDutyTime1, statisticDutyTime2);
								     if(date1!=null&&date2!=null){
								    	  //如果上午不缺打卡
								    	  //获取并集A与时间界限date1,date2的并集B，该并集B作为上午实际工作时间						    	 
								    	  List<OutWork> outWorkStartEndWorkList=geOutWorkListStartEndWithNoCascadeAsWorkNoIntegrate(outWorkStartEndList,date1,date2);
								    	  //累加上午工作时间
								    	  overTimeTotal=  overTimeTotal+  getTodayOutWorkTotal(outWorkStartEndWorkList);
								     }else{
								    	 //如果上午缺打卡,获取并集A时间区间之和作为上午实际工作时间
									   	  overTimeTotal= overTimeTotal+ getTodayOutWorkTotal(outWorkStartEndList);
								     }
								     
		                             }
								     
		                           //下午加班时间计算
		                             {
		                            	 //获取外出列表的并集A,并使集合A中的每一个元素限制在下午上班时间statisticDutyTime3,下午下班时间statisticDutyTime4范围内
								     List<OutWork> outWorkStartEndList=	geOutWorkListStartEndWithNoCascade(outWorkList, statisticDutyTime3, statisticDutyTime4);
								     if(date3!=null&&date4!=null){
								    	  //如果下午不缺打卡
								    	  //获取并集A与时间界限date3,date4的并集B，该并集B作为下午实际工作时间								    	 
								    	  List<OutWork> outWorkStartEndWorkList=geOutWorkListStartEndWithNoCascadeAsWorkNoIntegrate(outWorkStartEndList,date3,date4);
								    	  overTimeTotal=  overTimeTotal+  getTodayOutWorkTotal(outWorkStartEndWorkList);
								     }else{
								    	  //如果下午缺打卡,获取并集A时间区间之和作为下午实际工作时间
									   	  overTimeTotal= overTimeTotal+ getTodayOutWorkTotal(outWorkStartEndList);
								     }
								     
		                             }
		                             
		                             
		                           //晚上加班时间计算
		                             {
		                            	//如果晚上算作加班的话
		                            	 if (statisticOverTimeStart!=null&&statisticOverTimeEnd!=null) {
		                            		 //获取外出列表的并集A,并使集合A中的每一个元素限制在开始计算加班时间点statisticOverTimeStart,结束计算加班时间点statisticOverTimeEnd范围内
										     List<OutWork> outWorkStartEndList=	geOutWorkListStartEndWithNoCascade(outWorkList, statisticOverTimeStart, statisticOverTimeEnd);
										     if(date5!=null&&date6!=null){
										    	  //如果晚上加班不缺卡	
										    	  //获取并集A与时间界限date5,date6的并集B，该并集B作为晚上的实际工作时间		
										    	  List<OutWork> outWorkStartEndWorkList=geOutWorkListStartEndWithNoCascadeAsWorkNoIntegrate(outWorkStartEndList,date5,date6);
										    	  overTimeTotal=  overTimeTotal+  getTodayOutWorkTotal(outWorkStartEndWorkList);
										     }else{
										    	//如果晚上缺打卡,获取并集A时间区间之和作为晚上实际工作时间
											   	  overTimeTotal= overTimeTotal+ getTodayOutWorkTotal(outWorkStartEndList);
										     }
		                            		}
		                             }
		                             
		                             
		                             
		                             
						     
                             
							  attendanceDataDate.setOver_time(overTimeTotal*1.0/60.0/60); 
							  attendanceDataDate.setShould_work_time(0.0);
							  attendanceDataDate.setWork_time(0.0);
							 
							 //设置统计值
								attendanceDataDate.setKg_time(0.0);
								attendanceDataDate.setKg_count(0); 
					            attendanceDataDate.setLater_count(0); 
					            attendanceDataDate.setLater_time(0.0);
					            attendanceDataDate.setEarly_count(0);
					            attendanceDataDate.setEarly_time(0.0);
					            attendanceDataDate.setCard_not(0);
					            attendanceDataDate.setLeave_total_time(0.0);
					            attendanceDataDate.setOut_total_time(0.0);
					  }
					          //统计补打卡次数
							    if(attendanceCheckList.size()>0){
							    	int reCheckCount=getCardReCheckCount(attendanceCheckList);
							    	attendanceDataDate.setCard_status(reCheckCount);
							    }
					            
					            
					            //计算节假日信息
							    if(holidayList.size()>0){
							       attendanceDataDate.setIsholiday(1) ;
							    }else{
							    	 attendanceDataDate.setIsholiday(0) ;
							    }
							    
							    //统计今天是上班还是放假
							    if(isVocation){
							    	attendanceDataDate.setIsvocation(1);
							    }else{
							    	attendanceDataDate.setIsvocation(0);
							    }
							    
							    //统计今天是否签到
							    if(attendanceCheckList.size()>0){
							    	attendanceDataDate.setToday_work("1")	;
							    }else{
							    	attendanceDataDate.setToday_work("0")	;
							    }        
					            
		                //设置班次Id
					    attendanceDataDate.setDuty_id(attendanceDuty.getId());
					    attendanceDataDate.setDept_num(attendanceDuty.getDept_num());//重新更新某一天的统计记录中的冗余部门
					    attendanceDataDate.setDept_name(attendanceDuty.getDept_name());
					    attendanceDataDate.setStaff_no(employee.getYhNo());
					    
					    //如果今天是节假日或公休日并且是补班的,设置补班标志位
					    if ((isVocation||holidayList.size()>=0)&&extralWorkList.size()>0) {
							attendanceDataDate.setIs_extralwork(1);
						}else{
							
							attendanceDataDate.setIs_extralwork(0);
						}
					    
					    
					    
					    
					    
					    
					    
					    
					    
					    
					    
					    //更新当天考勤计算结果
					    statisticCalService.updateAttendanceDataDate(attendanceDataDate);
				    }else{
//===========================================弹性工作制=================================================================================
						//================================计算时间轴开始=================
				    	//弹性工作制开始和结束统计时间点
					    Date statisticStartCheckPoint=getDutyStartCheckPoint(now,attendanceDuty);
					    Date statisticEndCheckPoint=getDutyEndCheckPoint(now,attendanceDuty);
					    //弹性工作制必须上班的时间点
					    Date statisticElasticDutyTime1=getElasticDutyTime1(now, attendanceDuty);
					    Date statisticElasticDutyTime2=getElasticDutyTime2(now, attendanceDuty);
					    //弹性工作制默认上下班的时间点
					    Date statisticElasticDefaultDutyTime1=getElasticDefaultDutyTime1(now, attendanceDuty);
					    Date statisticElasticDefaultDutyTime2=getElasticDefaultDutyTime2(now, attendanceDuty);
					    
					    //如果还没有到当天 的统计时间则统计昨天的
					    if(now.compareTo(statisticStartCheckPoint)<0){
					    	statisticStartCheckPoint=getPreviousDayTime(statisticStartCheckPoint);
					    	statisticEndCheckPoint=getPreviousDayTime(statisticEndCheckPoint);
					    	statisticElasticDutyTime1=getPreviousDayTime(statisticElasticDutyTime1);
					    	statisticElasticDutyTime2=getPreviousDayTime(statisticElasticDutyTime2);
					    	statisticElasticDefaultDutyTime1=getPreviousDayTime(statisticElasticDefaultDutyTime1);
					    	statisticElasticDefaultDutyTime2=getPreviousDayTime(statisticElasticDefaultDutyTime2);
					    }
					  //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
					    if(statisticEndCheckPoint.before(statisticStartCheckPoint)){
					    	statisticEndCheckPoint=getNextDayTime(statisticEndCheckPoint);
					    }
					  //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
					    if(statisticElasticDutyTime1.before(statisticStartCheckPoint)){
					    	statisticElasticDutyTime1=getNextDayTime(statisticElasticDutyTime1);
					    }
					  //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
					    if(statisticElasticDutyTime2.before(statisticStartCheckPoint)){
					    	statisticElasticDutyTime2=getNextDayTime(statisticElasticDutyTime2);
					    }
					    
					  //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
					    if(statisticElasticDefaultDutyTime1.before(statisticStartCheckPoint)){
					    	statisticElasticDefaultDutyTime1=getNextDayTime(statisticElasticDefaultDutyTime1);
					    }
					    
					    //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
					    if(statisticElasticDefaultDutyTime2.before(statisticStartCheckPoint)){
					    	statisticElasticDefaultDutyTime2=getNextDayTime(statisticElasticDefaultDutyTime2);
					    }
					    
					  //================================计算时间轴结束=================
					    
					    
					    //删除该区间的旧的异常，以便重新统计
					    attendanceService.toDelExceptionAfterApply(employee.getYhDm(), DateUtil.getDateString(statisticStartCheckPoint), DateUtil.getDateString(statisticEndCheckPoint));

					    
                        //===========================================获取考勤计算所需的条件开始====================
					    //获取补班设置列表		
						   List<Holiday>  extralWorkList=getExtralWorkListByPoint(statisticStartCheckPoint, employee.getXb(), employee.getBmDm());

					    
					    
					    //获取当日相交的节假日列表
						   List<Holiday>  holidayList=getHolidayListByPoint(statisticStartCheckPoint, employee.getXb() , employee.getBmDm());

					    
					    //获取当天的考勤打卡记录
					    List<AttendanceCheck> attendanceCheckList=getAttendanceCheckListByPoint(employee.getYhDm(), statisticStartCheckPoint, statisticEndCheckPoint);
					    
					     //获取与当天的默认上下班时间有交集的外出列表
					    List<OutWork> outWorkList=getOutWorkiListByPoint(employee.getYhDm(), statisticElasticDefaultDutyTime1, statisticElasticDefaultDutyTime2);

					    //获取与当天的默认上下班时间有交集的请假列表

					    List<LeaveTime> leaveTimeList=getLeaveTimeListByPoint(employee.getYhDm(), statisticElasticDefaultDutyTime1, statisticElasticDefaultDutyTime2);
					    //by gengji.yang 获取当天的加班申请记录 
					    List<Map> otList=getOtListByPoint(employee.getYhDm(), statisticElasticDefaultDutyTime1, statisticElasticDefaultDutyTime2);
					    
					    //获取个人当日免打卡设置
					    List<NoCheckSet> staffNoCheckSetList=statisticCalService.getNoCheckSetByStaffNum(employee.getYhDm(),getSimpleDateString(statisticStartCheckPoint));
					    //获取部门当日免打卡设置
					    List<DeptNoCheckSet> deptNoCheckSetList=getFirstNoCheckSetParentDepartmentsByBmDm(attendanceDuty.getDept_num(), getSimpleDateString(statisticStartCheckPoint));  
					    
					    //将员工免打卡设置和部门免打卡设置取并集,以保证某一个部门新添加的员工可以继承部门的免打卡设置
					    NoCheckSet noCheckSet=getNoCheckSet(staffNoCheckSetList, deptNoCheckSetList);
					  //===========================================获取考勤计算所需的条件结束====================
					    //统计数据日期归属，即是某人哪一天的统计数据
					    String yearMonthDay=getSimpleDateString(statisticStartCheckPoint);
                         
					    
					  //如果免打卡设置后不产生该人的统计记录
					    if (noCheckSet.getGenerate_record()!=null&&noCheckSet.getGenerate_record()==1) {
					    	//如果不产生记录，则要删除某人当天的历史遗留数据
					    	statisticCalService.deleteAttendanceDataDateByStaffNumAndDate(employee.getYhDm(), yearMonthDay);
							return;
						}
					    
					    
					    
					
					  
					    
					    	 //获取统计对象
					    	    Map<String, String> attendanceDataDateConditionMap=new HashMap<String, String>();
							    attendanceDataDateConditionMap.put("staffNum", employee.getYhDm());
							    attendanceDataDateConditionMap.put("everyDate",yearMonthDay );
							    AttendanceDataDate attendanceDataDate=statisticCalService.getAttendanceDataDateByStaffAndEveryDate(attendanceDataDateConditionMap);
							    if(null==attendanceDataDate){//如果数据库中不存在，则插入
							    	attendanceDataDate=new AttendanceDataDate();
									attendanceDataDate.setStaff_num(employee.getYhDm());
									attendanceDataDate.setStaff_name(employee.getYhMc());								
									attendanceDataDate.setEvery_date(yearMonthDay);
									attendanceDataDate.setDept_num(attendanceDuty.getDept_num());
									attendanceDataDate.setStaff_no(employee.getYhNo());
									attendanceDataDate.setDept_name(attendanceDuty.getDept_name());
									attendanceDataDate.setSex(employee.getXb()==null?null:Integer.parseInt(employee.getXb()));
									statisticCalService.insertAttendanceDataDate(attendanceDataDate);
							    }
					    	    //重新获取一次
							    attendanceDataDate=statisticCalService.getAttendanceDataDateByStaffAndEveryDate(attendanceDataDateConditionMap);
							    
							    
							    //当日的免打卡设置,如果没有设置免打卡赋值0
								    attendanceDataDate.setNo_check_set(noCheckSet.getCheck_point());
								
							    
							    //设置离职状态
							    attendanceDataDate.setIn_work(Integer.parseInt(employee.getZfBj()));
							    
							    
							    //获取弹性工作制的上班打卡记录
						    	Date elasticDutyTime1CheckDate=getElasticDutyTime1CheckDate(attendanceCheckList, statisticElasticDutyTime1, statisticElasticDutyTime2);
							    //获取弹性工作制的下班打卡记录
						    	Date elasticDutyTime2CheckDate=getElasticDutyTime2CheckDate(attendanceCheckList, statisticElasticDutyTime1, statisticElasticDutyTime2);
						    	
					    
							    
							 
							    
							    //默认应该上满 多少小时.,取默认上下班时间的间隔
							    Double mustKeepHour=(getSecondsInterval(statisticElasticDefaultDutyTime1, statisticElasticDefaultDutyTime2)*1.0/60/60);
							    attendanceDataDate.setShould_work_time(mustKeepHour);
							    
							    

					            //旷工时间
					            Integer kgTime=0;
					            //缺卡次数
					            Integer cardNotCount=0;
					            //早退次数
					            Integer earlyTimeCount=0;
					            //早退时间
					            Integer earlyTimeTimeTotal=0;
					            //迟到次数
					            Integer lateTimeCount=0;
					            //迟到时间
					            Integer lateTimeTimeTotal=0;
					            //旷工类型    对于弹性班制来说只有全天旷工，即值为3表示全天旷工
					            Integer kg_type=0;
					            
					            //今天考勤是否异常,1 正常  0  异常
					            Integer exception_flag=1;
					            
					            //今天是否是节假日 
					            boolean isVocation=isVocation(attendanceDuty, statisticStartCheckPoint);
					            
					            //上班时间是否落在请假范围内
					            boolean statisticElasticDutyTime1InLeaveTime= dateInLeaveTime(statisticElasticDutyTime1, leaveTimeList);
					            //上午时间是否落在外出范围内
							    boolean statisticElasticDutyTime1InOutWork=  dateInOutWorkTime(statisticElasticDutyTime1, outWorkList);
							  //下班时间是否落在请假范围内
							    boolean statisticElasticDutyTime2InLeaveTime= dateInLeaveTime(statisticElasticDutyTime2, leaveTimeList);
							  //下班时间是否落在请假范围内
							    boolean statisticElasticDutyTime2InOutWork=dateInOutWorkTime(statisticElasticDutyTime2, outWorkList);
							  
							    /*--新增修改--：对于不满规定小时数新增条件判断*/
							    //默认上班时间是否落在请假范围内
							    boolean statisticElasticDefaultDutyTime1InLeaveTime= dateInLeaveTime(statisticElasticDefaultDutyTime1, leaveTimeList);
							    //默认上午时间是否落在外出范围内
							    boolean statisticElasticDefaultDutyTime1InOutWork=  dateInOutWorkTime(statisticElasticDefaultDutyTime1, outWorkList);
							    //默认下班时间是否落在请假范围内
							    boolean statisticElasticDefaultDutyTime2InLeaveTime= dateInLeaveTime(statisticElasticDefaultDutyTime2, leaveTimeList);
							    //默认下班时间是否落在请假范围内
							    boolean statisticElasticDefaultDutyTime2InOutWork=dateInOutWorkTime(statisticElasticDefaultDutyTime2, outWorkList);
							   /*----------------------------------*/
							    
							    
							    
							    
							    
							    
							    //====================================计算旷工类型开始================================
							    //如果是工作日或补班
					            if((!isVocation&&holidayList.size()==0)||extralWorkList.size()>0){ 
				                      //上午上班缺勤
							    	if(null==elasticDutyTime1CheckDate){
							    		//如果没有免打卡设置
							    		if (noCheckSet==null||(noCheckSet!=null&&((noCheckSet.getCheck_point()&1)==0))) {
								    		if(now.compareTo(statisticElasticDutyTime2)>=0){
								    		//如果必须在岗时间开始时间不在请假时间内，上班又没有打卡
								    		if(!statisticElasticDutyTime1InLeaveTime){
								    			//如果必须在岗时间开始时间不在外出申请时间内，按旷工计算
								    			if(!statisticElasticDutyTime1InOutWork){
								    				kg_type=kg_type|3;
								    			}
								    		}
								    	}
							    		}
							    	}
							    	

							    	//下午下班缺勤,过了下班统计时间，还没有打卡
							    	if(null==elasticDutyTime2CheckDate){
							    		//如果没有免打卡设置
							    		if (noCheckSet==null||(noCheckSet!=null&&((noCheckSet.getCheck_point()&8)==0))) {
								    		if(now.compareTo(statisticEndCheckPoint)>=0){
								    		//如果必须在岗时间结束时间不在请假时间内，
								    		if(!statisticElasticDutyTime2InLeaveTime){
								    			//必须在岗时间结束时间不在外出申请范围内,则算旷工
								    			if(!statisticElasticDutyTime2InOutWork){
								    				  kg_type=kg_type|3;
								    				 
								    			}
								    		}
								    	}
							    		}
							    	}

					       }
							    
							    
							    
							    //====================================计算旷工类型结束================
							    
					            
				            	//旷工算到哪一个点   用位表示,从右往左，第 1,2,3,4位分别表示上午上班，上午下班，下午上班，下午下班，如00000001表示计算到上午上班,00001001表示计算到上午上班和下午下班
					            int kg_cal_type=0;
					            
  
							    
					            //如果是工作日或补班
					            if((!isVocation&&holidayList.size()==0)||extralWorkList.size()>0){ 
				                      //上午上班缺勤
							    	if(null==elasticDutyTime1CheckDate){
							    		//如果没有免打卡设置
							    		if (noCheckSet==null||(noCheckSet!=null&&((noCheckSet.getCheck_point()&1)==0))) {
								    		if(now.compareTo(statisticElasticDutyTime2)>=0){
								    		//如果核心上班时间上班不在请假时间内，上班又没有打卡
								    		if(!statisticElasticDutyTime1InLeaveTime){
								    			//如果核心上班时间不在外出申请时间内，又没有打卡，按缺勤
								    			if(!statisticElasticDutyTime1InOutWork){
								    				//timepoint表示异常时间点
								    				  String timePoint=DateUtil.getDateString(statisticElasticDefaultDutyTime1);
								    				  String exceptionType="3";//3表示缺卡
								    				  //获取某一个人某一个时间点的旧的异常记录
								    				  List<AttendanceException> attendanceExceptionList=getAttendanceExceptionListByPoint(timePoint, exceptionType, employee.getYhDm());
												    		AttendanceException attendanceException=new AttendanceException();
												    		attendanceException.setDuty_time_type("1");//1表示上午上班
												    		//统计到上午上班时间点所在的日期中去
												    		attendanceException.setEveryday_time(yearMonthDay);
												    		attendanceException.setException_type(exceptionType);
												    		attendanceException.setStaff_name(employee.getYhMc());
												    		attendanceException.setStaff_num(employee.getYhDm());
												    		exception_flag=exception_flag&0; //&0表示说明今天考勤异常
												    		//异常时长为默认应工作时间
													    	attendanceException.setTime_interval(Double.valueOf(mustKeepHour*60*60).intValue());
													        //将旷工时间计算到上午上班时间点去
													    	kg_cal_type=kg_cal_type|1;
													    	//旷工时间累加
													    	kgTime=kgTime+Double.valueOf(mustKeepHour*60*60).intValue();
													    	//缺卡次数+1
													    	cardNotCount++;
												    		attendanceException.setTime_point(timePoint);
												    		//如果没有插入则插入，如果已经插了记录，则不要再插了
										    				if(attendanceExceptionList.size()==0){
										    					statisticCalService.insertAttendanceException(attendanceException);
										    				  }
								    			}
								    		}
								    	}
							    		}
							    	}else{
							    		//如果没有免打卡设置
							    		if (noCheckSet==null||(noCheckSet!=null&&((noCheckSet.getCheck_point()&1)==0))) {
							    	   //不缺勤
							    		//如果上午打卡的时间超过了必须上班时间点,则按迟到计算
							    		if(elasticDutyTime1CheckDate.compareTo(statisticElasticDutyTime1)>0){
							    			//如果核心上班时间上班不在请假时间内，又没有打卡
								    		if(!statisticElasticDutyTime1InLeaveTime){
								    			//如果核心上班时间不在外出申请时间内，又没有打卡
										    			if(!statisticElasticDutyTime1InOutWork){
										    				//timepoint表示的是异常时间点
										    				  String timePoint=DateUtil.getDateString(elasticDutyTime1CheckDate);
										    				  String exceptionType="1";//1表示迟到
										    				  //获取某一个人某一个时间点的旧的异常记录
										    				  List<AttendanceException> attendanceExceptionList=getAttendanceExceptionListByPoint(timePoint, exceptionType, employee.getYhDm());
													    			AttendanceException attendanceException=new AttendanceException();
														    		attendanceException.setDuty_time_type("1");//1 表示上午上班
														    		//统计到上午上班时间点所在的日期中去
														    		attendanceException.setEveryday_time(yearMonthDay);
														    		attendanceException.setException_type(exceptionType);
														    		attendanceException.setStaff_name(employee.getYhMc());
														    		attendanceException.setStaff_num(employee.getYhDm());
														    		exception_flag=exception_flag&0; //&0表示说明今天考勤异常
														    		//计算弹性上班必须在岗时间点开始时间与弹性上班打卡之间除去请假时间
														    		Integer statisticElasticDutyTime1ElasticDutyTime1CheckDateInterval= getSecondsInterval(statisticElasticDutyTime1, elasticDutyTime1CheckDate)-getTodayLeaveTimeTotal(leaveTimeList, statisticElasticDutyTime1, elasticDutyTime1CheckDate);
														    		attendanceException.setTime_interval(statisticElasticDutyTime1ElasticDutyTime1CheckDateInterval);
														    		if ((kg_type&1)==0) { //如果没有旷工，统计迟到时间才有意义
															    		lateTimeCount++;
															    		lateTimeTimeTotal=lateTimeTimeTotal+statisticElasticDutyTime1ElasticDutyTime1CheckDateInterval;
														    		}
														    		attendanceException.setTime_point(timePoint);
														    		//如果没有插入则插入，如果已经插了记录，则不要再插了
												    				  if(attendanceExceptionList.size()==0){
												    					  statisticCalService.insertAttendanceException(attendanceException);
												    				  }
										    			}
						    				  }
							    		}
							    		}
							    	}
							    	

							    	//下午下班缺勤,过了下班统计时间，还没有打卡
							    	if(null==elasticDutyTime2CheckDate){
							    		//如果没有免打卡设置
							    		if (noCheckSet==null||(noCheckSet!=null&&((noCheckSet.getCheck_point()&8)==0))) {
								    		if(now.compareTo(statisticEndCheckPoint)>=0){
								    		//如果下班没有打卡，下班时间又不在请假时间内，
								    		if(!statisticElasticDutyTime2InLeaveTime){
								    			//如果下班没有打卡,如果又没有外出申请,则算缺勤
								    			if(!statisticElasticDutyTime2InOutWork){
								    				//timepoint表示的是异常时间点
								    				  String timePoint=DateUtil.getDateString(statisticElasticDefaultDutyTime2);
								    				  String exceptionType="3";//3表示是缺卡
								    				  List<AttendanceException> attendanceExceptionList=getAttendanceExceptionListByPoint(timePoint, exceptionType, employee.getYhDm());
												    		AttendanceException attendanceException=new AttendanceException();
												    		attendanceException.setDuty_time_type("4");//表示的是下午下班
												    		//统计到上午上班时间点所在的日期中去
												    		attendanceException.setEveryday_time(yearMonthDay);
												    		attendanceException.setException_type(exceptionType);
												    		attendanceException.setStaff_name(employee.getYhMc());
												    		attendanceException.setStaff_num(employee.getYhDm());
												    		exception_flag=exception_flag&0; //&0表示说明今天考勤异常
												    		
												    		//如果旷工时间已经计算到了上班中去了
												    		if ((kg_cal_type&1)>0) {
												    			attendanceException.setTime_interval(0);
															}else{
																kg_cal_type=kg_cal_type|2;
																//异常时长为默认上班时间间隔
																attendanceException.setTime_interval(Double.valueOf(mustKeepHour*60*60).intValue());		
														    	//旷工时间累加
																kgTime=kgTime+Double.valueOf(mustKeepHour*60*60).intValue();
															}
												    		
												    		
												    		 //缺打卡次数+1
												    		cardNotCount++;
												    		attendanceException.setTime_point(timePoint);
												    		//如果没有插入则插入，如果已经插了记录，则不要再插了
										    				  if(attendanceExceptionList.size()==0){
										    					  statisticCalService.insertAttendanceException(attendanceException);
										    				  }
								    			}
								    		}
								    	}
							    		}
							    	}else{
							    		//如果没有免打卡设置
							    		if (noCheckSet==null||(noCheckSet!=null&&((noCheckSet.getCheck_point()&8)==0))) {
								    	     //不缺勤
								    		//如果下午下班早退了
								    		if(elasticDutyTime2CheckDate.compareTo(statisticElasticDutyTime2)<0){
								    			//如果核心下班时间下班没有打卡，下班时间又不在请假时间内，
									    		if(!statisticElasticDutyTime2InLeaveTime){
									    			//如果核心下班时间下班没有打卡,又不在外出申请时间内,则算早退
									    			if(!statisticElasticDutyTime2InOutWork){
									    				//timePoint异常时间点
										    			  String timePoint=DateUtil.getDateString(elasticDutyTime2CheckDate);
										    			  String exceptionType="2";//2表示早退
									    				  List<AttendanceException> attendanceExceptionList=getAttendanceExceptionListByPoint(timePoint, exceptionType, employee.getYhDm());

												    			AttendanceException attendanceException=new AttendanceException();
													    		attendanceException.setDuty_time_type("4");//4表示下午下班
													    		//统计到上午上班时间点所在的日期中去
													    		attendanceException.setEveryday_time(getSimpleDateString(statisticStartCheckPoint));
													    		attendanceException.setException_type(exceptionType);
													    		attendanceException.setStaff_name(employee.getYhMc());
													    		attendanceException.setStaff_num(employee.getYhDm());
													    		exception_flag=exception_flag&0; //&0表示说明今天考勤异常
													    		//下行刷卡时间与必须在岗结束时间之间的时间刨去该区间内的请假时间
													    		Integer elasticDutyTime2CheckDateStatisticElasticDutyTime2Interval=  getSecondsInterval(elasticDutyTime2CheckDate,statisticElasticDutyTime2)-getTodayLeaveTimeTotal(leaveTimeList, elasticDutyTime2CheckDate, statisticElasticDutyTime2);
													    		attendanceException.setTime_interval(elasticDutyTime2CheckDateStatisticElasticDutyTime2Interval);
													    		if ((kg_type&2)==0) { //如果没有旷工，统计早退时间才有意义
													    			//早退次数加1
														    		earlyTimeCount++;
														    	//早退时间累计
														    		earlyTimeTimeTotal=earlyTimeTimeTotal+elasticDutyTime2CheckDateStatisticElasticDutyTime2Interval;
													    		}
													    		attendanceException.setTime_point(timePoint);
													    		//如果没有插入则插入，如果已经插了记录，则不要再插了
											    				if(attendanceExceptionList.size()==0){
											    					statisticCalService.insertAttendanceException(attendanceException);	
									    				  		}
									    			}
							    				  }
								    		}
							    		}
								    	}
					 
							    	
							     	//计算一天中请假和外出时用的时间起点,初始值是默认上班时间
								    Date leaveOutStartDate=statisticElasticDefaultDutyTime1;
								 	//计算一天中请假和外出时用的时间终点,初始值是默认下班时间
								    Date leaveOutEndDate=statisticElasticDefaultDutyTime2;
								    
								    //如果上午免打卡,也没有请假
								    if (!statisticElasticDutyTime1InLeaveTime&&noCheckSet!=null&&((noCheckSet.getCheck_point()&1)>0)) {
										 // 如果上午免打卡则什么也不做,上班时间按默认时间
									}else{//如果上午没有免打卡
										
										if(elasticDutyTime1CheckDate==null){
								    		//如果核心上班打卡时间没有请假，也没有外出，也没有免打卡设置，则按旷工，如果请了假则不用打卡，上班时间按默认上班时间计算
								    		if(!statisticElasticDutyTime1InLeaveTime&&!statisticElasticDutyTime1InOutWork){
								    			leaveOutStartDate=null;	 
										    }
		  	
								    	}else{
								    		 //如果上午打了卡,则取打卡记录时间
								    		leaveOutStartDate=elasticDutyTime1CheckDate;
								    	}
								    	
									}
								    
								    
							    	
								    //如果下午免打卡也没有请假
								    if (!statisticElasticDutyTime2InLeaveTime&&noCheckSet!=null&&((noCheckSet.getCheck_point()&8)>0)) {
										  //如果下午免打卡，则什么也不用做，下班时间按默认下班时间算
									}else{//如果下午没有免打卡
										
										if(elasticDutyTime2CheckDate==null){
								    		//如果核心下班打卡时间没有请假，也没有外出申请，也没有免打卡设置,则按旷工计算， 如果请了假则不用打卡，下班时间按默认下班时间计算
								    		if(!statisticElasticDutyTime2InLeaveTime&&!statisticElasticDutyTime2InOutWork){
								    			leaveOutEndDate=null;	 
										    }
		  	
								    	}else{
								    		//如果下午打了卡，则取下午打卡记录时间
								    		leaveOutEndDate=elasticDutyTime2CheckDate;
								    	}
									}
								    
							    	
							    	
								  //设置上下班打卡时间
								    attendanceDataDate.setWork_begin_time1(DateUtil.getDateString(elasticDutyTime1CheckDate));
								    attendanceDataDate.setWork_end_time2(DateUtil.getDateString(elasticDutyTime2CheckDate));	

								    //设置旷工类型
								    attendanceDataDate.setKg_type(kg_type);
							    	//统计当天外出时间
								    Double outTotalTime=0.0;
								    
							    	
							    	if(leaveOutStartDate!=null&&leaveOutEndDate!=null){	
							    		//获取外出申请列表的并集A，并保证A中的元素全部限制在leaveOutStartDate,leaveOutEndDate的时间范围内
								    	  List<OutWork> outWorkStartEndList=	geOutWorkListStartEndWithNoCascade(outWorkList,leaveOutStartDate,leaveOutEndDate);
                                          //迭代集合A,刨去A中的每一个元素中的请假时间
								    	  for(OutWork ow:outWorkStartEndList){
                                        	  
                                        	  Date owStartDate=DateUtil.getDateTime(ow.getFromTime());
                                        	  Date owEndDate=DateUtil.getDateTime(ow.getToTime());
                                        	  //获取A中的元素与请假列表之间的并集B
                                        	  List<LeaveTime> leaveTimeStartEndList=	getLeaveTimeListStartEndWithNoCascade(leaveTimeList, owStartDate, owEndDate);
                                        	  //获取集合B中的时间总和
        							    	  Integer leaveTimeOutWorkTotal=  getTodayLeaveTimeTotal(leaveTimeStartEndList);
        							    	  //获取请假列表的集合
        							    	  List<LeaveTime> leaveTimeOutWorkTimeList=getLeaveTimeListWithNoCascade(leaveTimeList);
        							    	  //获取请假列表的集合时间总和
        							    	  Integer leaveTimeTotal=  getTodayLeaveTimeTotal(leaveTimeOutWorkTimeList);
        							    	  //刨去外出记录中的请假时间
        							    	  Double totalM=(leaveTimeOutWorkTotal-leaveTimeTotal)*1.0/60/60;
        							    	  //累加
        							    	  outTotalTime=outTotalTime+totalM;  
                                          }
							    	} 
								    attendanceDataDate.setOut_total_time(outTotalTime);
								    
								    
								    //实际工作时间
								    Double inFactKeepHour=0.0;
								    
								    
							    	//如果没有旷工,则需要计算除去请假的实际工作时间
							    	if(leaveOutStartDate!=null&&leaveOutEndDate!=null){	
							    		/*
							    		 * 描述：--新加修改--（解决请假后，弹性班统计异常问题）：加判断如果弹性工作制的上班打卡记录和弹性工作制的下班打卡记录为空且请假列表时间总和小于必须在岗时间，则该员工当天为旷工，实际工作时间为0 
							    		 * 时间：    2017-04-14
							    		 * 修改人：huixian.pan
							    		 * */
							    		if(elasticDutyTime1CheckDate ==null && elasticDutyTime2CheckDate==null){
							    		   //获取请假列表并集
      							    	   List<LeaveTime> leaveTimeOutWorkTimeList=getLeaveTimeListWithNoCascade(leaveTimeList);
      							    	   //获取请假列表时间总和
      							    	   Integer leaveTimeTotal=  getTodayLeaveTimeTotal(leaveTimeOutWorkTimeList);
      							    	    /*判断请假列表时间总和是否小于必须在岗时间*/
							    			if((leaveTimeTotal*1.0/60.0/60)<attendanceDuty.getElasticTimeAbsentTime()){
							    				/*如果请假列表时间总和小于必须在岗时间则不做操作*/
							    			}else{
							    				//获取外出申请集合与时间leaveOutStartDate,leaveOutEndDate之间的并集A,同时使A中的元素都在leaveOutStartDate,leaveOutEndDate范围内，实际上该区间内的元素是一个个工作时间区间块
										    	  List<OutWork> outWorkStartEndList=	geOutWorkListStartEndWithNoCascadeAsWork(outWorkList, leaveOutStartDate, leaveOutEndDate);
		                                          //迭代集合A中的元素，刨去请假记录
										    	  for(OutWork ow:outWorkStartEndList){
		                                        	  
		                                        	  Date owStartDate=DateUtil.getDateTime(ow.getFromTime());
		                                        	  Date owEndDate=DateUtil.getDateTime(ow.getToTime());
		                                        	  //获取A中的元素与请假列表之间的并集B
		                                        	  List<LeaveTime> leaveTimeStartEndList=	getLeaveTimeListStartEndWithNoCascade(leaveTimeList, owStartDate, owEndDate);
		                                        	  //获取并集B的时间总和
		        							    	  Integer leaveTimeOutWorkTotal=  getTodayLeaveTimeTotal(leaveTimeStartEndList);
		        							    	  //从A元素中刨去请假时间
		        							    	  Double totalM=(leaveTimeOutWorkTotal-leaveTimeTotal)*1.0/60/60;
		        							    	  //累加
		        							    	  inFactKeepHour=inFactKeepHour+totalM;  
		                                          }
							    			}
							    			
							    		}else{
							    			/*
								    		 * 描述：--新加修改--
								    		 * 时间：    2017-04-20
								    		 * 修改人：huixian.pan
								    		 * */
							    			if(elasticDutyTime1CheckDate ==null){
								    			// 如果弹性工作制的上班打卡记录为空，但leaveOutStartDate不等于空，则证明有请假区间在核心上班时间点，获取外出申请集合与时间statisticElasticDutyTime1,leaveOutEndDate之间的并集A,同时使A中的元素都在statisticElasticDutyTime1,leaveOutEndDate范围内，实际上该区间内的元素是一个个工作时间区间块
										    	  List<OutWork> outWorkStartEndList=	geOutWorkListStartEndWithNoCascadeAsWork(outWorkList, statisticElasticDutyTime1, leaveOutEndDate);
		                                          //迭代集合A中的元素，刨去请假记录
										    	  for(OutWork ow:outWorkStartEndList){
		                                        	  
		                                        	  Date owStartDate=DateUtil.getDateTime(ow.getFromTime());
		                                        	  Date owEndDate=DateUtil.getDateTime(ow.getToTime());
		                                        	  //获取A中的元素与请假列表之间的并集B
		                                        	  List<LeaveTime> leaveTimeStartEndList=	getLeaveTimeListStartEndWithNoCascade(leaveTimeList, owStartDate, owEndDate);
		                                        	  //获取并集B的时间总和
		        							    	  Integer leaveTimeOutWorkTotal=  getTodayLeaveTimeTotal(leaveTimeStartEndList);
		        							    	  //获取请假列表并集
		        							    	  List<LeaveTime> leaveTimeOutWorkTimeList=getLeaveTimeListWithNoCascade(leaveTimeList);
		        							    	  //获取请假列表时间总和
		        							    	  Integer leaveTimeTotal=  getTodayLeaveTimeTotal(leaveTimeOutWorkTimeList);
		        							    	  //从A元素中刨去请假时间
		        							    	  Double totalM=(leaveTimeOutWorkTotal-leaveTimeTotal)*1.0/60/60;
		        							    	  //累加
		        							    	  inFactKeepHour=inFactKeepHour+totalM; 
		                                     }
										    	  
							    			}else if(elasticDutyTime2CheckDate==null){
							    				/*
									    		 * 描述：--新加修改--
									    		 * 时间：    2017-04-20
									    		 * 修改人：huixian.pan
									    		 * */
							    				// 如果弹性工作制的下班打卡记录为空，但leaveOutEndDate不等于空，则证明有请假区间在核心下班时间点，获取外出申请集合与时间leaveOutStartDate,statisticElasticDutyTime2之间的并集A,同时使A中的元素都在leaveOutStartDate,statisticElasticDutyTime2范围内，实际上该区间内的元素是一个个工作时间区间块
										    	  List<OutWork> outWorkStartEndList=	geOutWorkListStartEndWithNoCascadeAsWork(outWorkList, leaveOutStartDate, statisticElasticDutyTime2);
		                                          //迭代集合A中的元素，刨去请假记录
										    	  for(OutWork ow:outWorkStartEndList){
		                                        	  
		                                        	  Date owStartDate=DateUtil.getDateTime(ow.getFromTime());
		                                        	  Date owEndDate=DateUtil.getDateTime(ow.getToTime());
		                                        	  //获取A中的元素与请假列表之间的并集B
		                                        	  List<LeaveTime> leaveTimeStartEndList=	getLeaveTimeListStartEndWithNoCascade(leaveTimeList, owStartDate, owEndDate);
		                                        	  //获取并集B的时间总和
		        							    	  Integer leaveTimeOutWorkTotal=  getTodayLeaveTimeTotal(leaveTimeStartEndList);
		        							    	  //获取请假列表并集
		        							    	  List<LeaveTime> leaveTimeOutWorkTimeList=getLeaveTimeListWithNoCascade(leaveTimeList);
		        							    	  //获取请假列表时间总和
		        							    	  Integer leaveTimeTotal=  getTodayLeaveTimeTotal(leaveTimeOutWorkTimeList);
		        							    	  //从A元素中刨去请假时间
		        							    	  Double totalM=(leaveTimeOutWorkTotal-leaveTimeTotal)*1.0/60/60;
		        							    	  //累加
		        							    	  inFactKeepHour=inFactKeepHour+totalM; 
		                                     }
							    			}/*else{
							    				------------修改前原来的代码     起--------------------------------------
								    			//获取外出申请集合与时间leaveOutStartDate,leaveOutEndDate之间的并集A,同时使A中的元素都在leaveOutStartDate,leaveOutEndDate范围内，实际上该区间内的元素是一个个工作时间区间块
										    	  List<OutWork> outWorkStartEndList=	geOutWorkListStartEndWithNoCascadeAsWork(outWorkList, leaveOutStartDate, leaveOutEndDate);
		                                          //迭代集合A中的元素，刨去请假记录
										    	  for(OutWork ow:outWorkStartEndList){
		                                        	  
		                                        	  Date owStartDate=DateUtil.getDateTime(ow.getFromTime());
		                                        	  Date owEndDate=DateUtil.getDateTime(ow.getToTime());
		                                        	  //获取A中的元素与请假列表之间的并集B
		                                        	  List<LeaveTime> leaveTimeStartEndList=	getLeaveTimeListStartEndWithNoCascade(leaveTimeList, owStartDate, owEndDate);
		                                        	  //获取并集B的时间总和
		        							    	  Integer leaveTimeOutWorkTotal=  getTodayLeaveTimeTotal(leaveTimeStartEndList);
		        							    	  //获取请假列表并集
		        							    	  List<LeaveTime> leaveTimeOutWorkTimeList=getLeaveTimeListWithNoCascade(leaveTimeList);
		        							    	  //获取请假列表时间总和
		        							    	  Integer leaveTimeTotal=  getTodayLeaveTimeTotal(leaveTimeOutWorkTimeList);
		        							    	  //从A元素中刨去请假时间
		        							    	  Double totalM=(leaveTimeOutWorkTotal-leaveTimeTotal)*1.0/60/60;
		        							    	  //累加
		        							    	  inFactKeepHour=inFactKeepHour+totalM; 
		        							    ------------修改前原来的代码     止--------------------------------------
		                                     }
							    			}*/
							    		
							    		}
							    		
							    		
							    	}
							    	
							    	 //统计今天实际上了多长时间正班
								    if(inFactKeepHour>mustKeepHour){
								    	//如果实际上班时间>默认工作时间，则取默认工作时间
								    	attendanceDataDate.setWork_time(mustKeepHour);
								    }else{
								    	//如果实际工作时间<默认工作时间，则取实际工作时间
								    	attendanceDataDate.setWork_time(inFactKeepHour);	
								    }
								    
									    //设置今日考勤是否异常
								        attendanceDataDate.setException_flag(exception_flag);
								        //设置缺卡次数
								        attendanceDataDate.setCard_not(cardNotCount);
								        //设置迟到次数
								        attendanceDataDate.setLater_count(lateTimeCount);
								        //设置迟到时间总数
									    attendanceDataDate.setLater_time(lateTimeTimeTotal/60.0/60);
									    //设置早退次数
									    attendanceDataDate.setEarly_count(earlyTimeCount);
									    //设置早退时间总数
									    attendanceDataDate.setEarly_time(earlyTimeTimeTotal/60.0/60);
									    
									    //设置旷工次数
									    attendanceDataDate.setKg_count(kgTime>0?1:0);
									    
									    
									   
									    
									    
									    //统计当天请假时间 
									    Double leaveTimeTotalTime=0.0;
										leaveTimeTotalTime=getTodayLeaveTimeTotal(leaveTimeList,statisticElasticDefaultDutyTime1,statisticElasticDefaultDutyTime2)*1.0/60.0/60;
									    attendanceDataDate.setLeave_total_time(leaveTimeTotalTime);
								    	
									    //修正旷工时间  减去请假时间
									    if(kg_type>0){
									    	//旷工时间中可能包含请假时间，这里要去掉请假时间
									    	kgTime=kgTime-Double.valueOf(leaveTimeTotalTime*60*60).intValue();
									    	if(kgTime<0){
									    		kgTime=0;
									    	} 
									    }
									    
									    //设置旷工时间
									    attendanceDataDate.setKg_time(kgTime/60.0/60);
									    
									   
									   
									   
									    //如果未上满规定小时数,本日按旷工处理
									    if((inFactKeepHour+leaveTimeTotalTime)<attendanceDuty.getElasticTimeAbsentTime()){									    	
									    	//默认应工作时间-请假时间
									    	double kgT=mustKeepHour-leaveTimeTotalTime;
									    	if(kgT<0){
									    		kgT=0;
									    	}
									    	attendanceDataDate.setKg_time(kgT);
									    	
									    	exception_flag=exception_flag&0; //&0表示说明今天考勤异常
									    	
									    	//如果上下班都打卡了
									    	if(elasticDutyTime1CheckDate!=null&&elasticDutyTime2CheckDate!=null){
									    		//timepoint表示异常时间点
						    				  String timePoint=DateUtil.getDateString(statisticElasticDefaultDutyTime1);
							    			  String exceptionType="4";//4表示不足规定小时数转旷工
							    			  exception_flag=exception_flag&0; //&0表示说明今天考勤异常
							    			  //获取某个某一个时间点的考勤异常
						    				  List<AttendanceException> attendanceExceptionList=getAttendanceExceptionListByPoint(timePoint, exceptionType, employee.getYhDm());
									    			AttendanceException attendanceException=new AttendanceException();
										    		attendanceException.setDuty_time_type("1");//1表示上午上班
										    		//统计到上午上班时间点所在的日期中去
										    		attendanceException.setEveryday_time(getSimpleDateString(statisticStartCheckPoint));
										    		attendanceException.setException_type(exceptionType);
										    		attendanceException.setStaff_name(employee.getYhMc());
										    		attendanceException.setStaff_num(employee.getYhDm());
										    		attendanceException.setTime_interval(Double.valueOf(mustKeepHour*60*60).intValue());
										    		attendanceException.setTime_point(timePoint);
										    		//如果没有插入则插入，如果已经插了记录，则不要再插了
								    				if(attendanceExceptionList.size()==0){
								    					statisticCalService.insertAttendanceException(attendanceException);	
						    				  		}    
									    	}
									    	/*  --------------------------------新增修改  起----------------------------------
									    	 * 描述：多加对核心上下班点是否在请假，外出时间范围内
									    	 * 时间：2017-04-19
									    	 * 修改人：huixian.pan 
									    	 * */
						    				//如果核心上班时间在请假时间内且默认上班时间不在请假范围内且弹性班上班打卡记录不等于默认上班时间
								    		if(statisticElasticDutyTime1InLeaveTime && !statisticElasticDefaultDutyTime1InLeaveTime){
							    			  if(elasticDutyTime1CheckDate ==null){
							    				  String timePoint=DateUtil.getDateString(statisticElasticDefaultDutyTime1);
								    			  String exceptionType="4";//4表示不足规定小时数转旷工
								    			 // exception_flag=exception_flag&0; //&0表示说明今天考勤异常
								    			  //获取某个某一个时间点的考勤异常
							    				  List<AttendanceException> attendanceExceptionList=getAttendanceExceptionListByPoint(timePoint, exceptionType, employee.getYhDm());
										    			AttendanceException attendanceException=new AttendanceException();
											    		attendanceException.setDuty_time_type("1");//1表示上午上班
											    		//统计到上午上班时间点所在的日期中去
											    		attendanceException.setEveryday_time(getSimpleDateString(statisticStartCheckPoint));
											    		attendanceException.setException_type(exceptionType);
											    		attendanceException.setStaff_name(employee.getYhMc());
											    		attendanceException.setStaff_num(employee.getYhDm());
											    		attendanceException.setTime_interval(Double.valueOf(mustKeepHour*60*60).intValue());
											    		attendanceException.setTime_point(timePoint);
											    		//如果没有插入则插入，如果已经插了记录，则不要再插了
									    				if(attendanceExceptionList.size()==0){
									    					statisticCalService.insertAttendanceException(attendanceException);	
							    				  		} 
							    			  }else if(!(elasticDutyTime1CheckDate.compareTo(statisticElasticDefaultDutyTime1)==0)){
							    				  String timePoint=DateUtil.getDateString(statisticElasticDefaultDutyTime1);
								    			  String exceptionType="4";//4表示不足规定小时数转旷工
								    			 // exception_flag=exception_flag&0; //&0表示说明今天考勤异常
								    			  //获取某个某一个时间点的考勤异常
							    				  List<AttendanceException> attendanceExceptionList=getAttendanceExceptionListByPoint(timePoint, exceptionType, employee.getYhDm());
										    			AttendanceException attendanceException=new AttendanceException();
											    		attendanceException.setDuty_time_type("1");//1表示上午上班
											    		//统计到上午上班时间点所在的日期中去
											    		attendanceException.setEveryday_time(getSimpleDateString(statisticStartCheckPoint));
											    		attendanceException.setException_type(exceptionType);
											    		attendanceException.setStaff_name(employee.getYhMc());
											    		attendanceException.setStaff_num(employee.getYhDm());
											    		attendanceException.setTime_interval(Double.valueOf(mustKeepHour*60*60).intValue());
											    		attendanceException.setTime_point(timePoint);
											    		//如果没有插入则插入，如果已经插了记录，则不要再插了
									    				if(attendanceExceptionList.size()==0){
									    					statisticCalService.insertAttendanceException(attendanceException);	
							    				  		} 
						    				  }
								    		     
								    		}
								    		//如果核心下班时间在请假时间内且默认下班时间不在请假时间内且弹性班下班打卡记录不等于默认下班时间
								    		if(statisticElasticDutyTime2InLeaveTime && !statisticElasticDefaultDutyTime2InLeaveTime){		
								    			if(elasticDutyTime2CheckDate == null){
								    				String timePoint2=DateUtil.getDateString(statisticElasticDefaultDutyTime2);
								    				String exceptionType2="4";//4表示不足规定小时数转旷工
								    				//获取某个某一个时间点的考勤异常
								    				List<AttendanceException> attendanceExceptionList2=getAttendanceExceptionListByPoint(timePoint2, exceptionType2, employee.getYhDm());
								    				AttendanceException attendanceException2=new AttendanceException();
								    				attendanceException2.setDuty_time_type("4");//1表示下午下班
								    				//统计到上午上班时间点所在的日期中去
								    				attendanceException2.setEveryday_time(getSimpleDateString(statisticStartCheckPoint));
								    				attendanceException2.setException_type(exceptionType2);
								    				attendanceException2.setStaff_name(employee.getYhMc());
								    				attendanceException2.setStaff_num(employee.getYhDm());
								    				attendanceException2.setTime_interval(Double.valueOf(mustKeepHour*60*60).intValue());
								    				attendanceException2.setTime_point(timePoint2);
								    				//如果没有插入则插入，如果已经插了记录，则不要再插了
								    				if(attendanceExceptionList2.size()==0){
								    					statisticCalService.insertAttendanceException(attendanceException2);	
								    				}
								    			}else if(!(elasticDutyTime2CheckDate.compareTo(statisticElasticDefaultDutyTime2)==0)){
								    				String timePoint2=DateUtil.getDateString(statisticElasticDefaultDutyTime2);
								    				String exceptionType2="4";//4表示不足规定小时数转旷工
								    				//获取某个某一个时间点的考勤异常
								    				List<AttendanceException> attendanceExceptionList2=getAttendanceExceptionListByPoint(timePoint2, exceptionType2, employee.getYhDm());
								    				AttendanceException attendanceException2=new AttendanceException();
								    				attendanceException2.setDuty_time_type("4");//1表示下午下班
								    				//统计到上午上班时间点所在的日期中去
								    				attendanceException2.setEveryday_time(getSimpleDateString(statisticStartCheckPoint));
								    				attendanceException2.setException_type(exceptionType2);
								    				attendanceException2.setStaff_name(employee.getYhMc());
								    				attendanceException2.setStaff_num(employee.getYhDm());
								    				attendanceException2.setTime_interval(Double.valueOf(mustKeepHour*60*60).intValue());
								    				attendanceException2.setTime_point(timePoint2);
								    				//如果没有插入则插入，如果已经插了记录，则不要再插了
								    				if(attendanceExceptionList2.size()==0){
								    					statisticCalService.insertAttendanceException(attendanceException2);	
								    				}
								    			}
								    			
								    		}
								    				
								    		//如果核心上班时间在外出时间内且默认上班时间不在外出时间内且弹性班上班打卡记录不等于默认上班时间
								    		if(statisticElasticDutyTime1InOutWork && !statisticElasticDefaultDutyTime1InOutWork){
								    			 if(elasticDutyTime1CheckDate ==null){
								    				  String timePoint=DateUtil.getDateString(statisticElasticDefaultDutyTime1);
									    			  String exceptionType="4";//4表示不足规定小时数转旷工
									    			 // exception_flag=exception_flag&0; //&0表示说明今天考勤异常
									    			  //获取某个某一个时间点的考勤异常
								    				  List<AttendanceException> attendanceExceptionList=getAttendanceExceptionListByPoint(timePoint, exceptionType, employee.getYhDm());
											    			AttendanceException attendanceException=new AttendanceException();
												    		attendanceException.setDuty_time_type("1");//1表示上午上班
												    		//统计到上午上班时间点所在的日期中去
												    		attendanceException.setEveryday_time(getSimpleDateString(statisticStartCheckPoint));
												    		attendanceException.setException_type(exceptionType);
												    		attendanceException.setStaff_name(employee.getYhMc());
												    		attendanceException.setStaff_num(employee.getYhDm());
												    		attendanceException.setTime_interval(Double.valueOf(mustKeepHour*60*60).intValue());
												    		attendanceException.setTime_point(timePoint);
												    		//如果没有插入则插入，如果已经插了记录，则不要再插了
										    				if(attendanceExceptionList.size()==0){
										    					statisticCalService.insertAttendanceException(attendanceException);	
								    				  		} 
								    			  }else if(!(elasticDutyTime1CheckDate.compareTo(statisticElasticDefaultDutyTime1)==0)){
								    				  String timePoint=DateUtil.getDateString(statisticElasticDefaultDutyTime1);
									    			  String exceptionType="4";//4表示不足规定小时数转旷工
									    			 // exception_flag=exception_flag&0; //&0表示说明今天考勤异常
									    			  //获取某个某一个时间点的考勤异常
								    				  List<AttendanceException> attendanceExceptionList=getAttendanceExceptionListByPoint(timePoint, exceptionType, employee.getYhDm());
											    			AttendanceException attendanceException=new AttendanceException();
												    		attendanceException.setDuty_time_type("1");//1表示上午上班
												    		//统计到上午上班时间点所在的日期中去
												    		attendanceException.setEveryday_time(getSimpleDateString(statisticStartCheckPoint));
												    		attendanceException.setException_type(exceptionType);
												    		attendanceException.setStaff_name(employee.getYhMc());
												    		attendanceException.setStaff_num(employee.getYhDm());
												    		attendanceException.setTime_interval(Double.valueOf(mustKeepHour*60*60).intValue());
												    		attendanceException.setTime_point(timePoint);
												    		//如果没有插入则插入，如果已经插了记录，则不要再插了
										    				if(attendanceExceptionList.size()==0){
										    					statisticCalService.insertAttendanceException(attendanceException);	
								    				  		} 
							    				  }
								    		}
								    		//如果核心下班时间在外出时间内且默认下班时间不在外出时间内且弹性班下班打卡记录不等于默认下班时间
								    		if(statisticElasticDutyTime2InOutWork && !statisticElasticDefaultDutyTime2InOutWork){		
								    			if(elasticDutyTime2CheckDate == null){
								    				String timePoint2=DateUtil.getDateString(statisticElasticDefaultDutyTime2);
								    				String exceptionType2="4";//4表示不足规定小时数转旷工
								    				//获取某个某一个时间点的考勤异常
								    				List<AttendanceException> attendanceExceptionList2=getAttendanceExceptionListByPoint(timePoint2, exceptionType2, employee.getYhDm());
								    				AttendanceException attendanceException2=new AttendanceException();
								    				attendanceException2.setDuty_time_type("4");//1表示下午下班
								    				//统计到上午上班时间点所在的日期中去
								    				attendanceException2.setEveryday_time(getSimpleDateString(statisticStartCheckPoint));
								    				attendanceException2.setException_type(exceptionType2);
								    				attendanceException2.setStaff_name(employee.getYhMc());
								    				attendanceException2.setStaff_num(employee.getYhDm());
								    				attendanceException2.setTime_interval(Double.valueOf(mustKeepHour*60*60).intValue());
								    				attendanceException2.setTime_point(timePoint2);
								    				//如果没有插入则插入，如果已经插了记录，则不要再插了
								    				if(attendanceExceptionList2.size()==0){
								    					statisticCalService.insertAttendanceException(attendanceException2);	
								    				}
								    			}else if(!(elasticDutyTime2CheckDate.compareTo(statisticElasticDefaultDutyTime2)==0)){
								    				String timePoint2=DateUtil.getDateString(statisticElasticDefaultDutyTime2);
								    				String exceptionType2="4";//4表示不足规定小时数转旷工
								    				//获取某个某一个时间点的考勤异常
								    				List<AttendanceException> attendanceExceptionList2=getAttendanceExceptionListByPoint(timePoint2, exceptionType2, employee.getYhDm());
								    				AttendanceException attendanceException2=new AttendanceException();
								    				attendanceException2.setDuty_time_type("4");//1表示下午下班
								    				//统计到上午上班时间点所在的日期中去
								    				attendanceException2.setEveryday_time(getSimpleDateString(statisticStartCheckPoint));
								    				attendanceException2.setException_type(exceptionType2);
								    				attendanceException2.setStaff_name(employee.getYhMc());
								    				attendanceException2.setStaff_num(employee.getYhDm());
								    				attendanceException2.setTime_interval(Double.valueOf(mustKeepHour*60*60).intValue());
								    				attendanceException2.setTime_point(timePoint2);
								    				//如果没有插入则插入，如果已经插了记录，则不要再插了
								    				if(attendanceExceptionList2.size()==0){
								    					statisticCalService.insertAttendanceException(attendanceException2);	
								    				}
								    			}
								    		}
								    		/*-------------------------------------新增修改 止-------------------------------------------*/
								    				
									    	//如果未上满 规定小时数转化为旷工时，早退时间，早退次数，迟到时间，迟到次数不能统计
									    	attendanceDataDate.setEarly_count(0);
									    	attendanceDataDate.setEarly_time(0.0);
									    	attendanceDataDate.setLater_count(0);
									    	attendanceDataDate.setLater_time(0.0);
									    	attendanceDataDate.setWork_time(0.0);
									    	attendanceDataDate.setOver_time(0.0);
									    	//设置今日考勤是否异常
									        attendanceDataDate.setException_flag(exception_flag);
									    	
									    }
									    
									    
									    
									    
									    
									    
									    
									    
									    
									    
									    //统计加班时间
									    Double overWorkHour=0.0;
									    if(otList.size()>0){
									    	//如果有加班申请的话，则加班时间取开始统计打卡时间和结束统计打卡时间之间的加班时间
									    	 overWorkHour=getTodayOtTimeTotal(otList, statisticStartCheckPoint, statisticEndCheckPoint)*1.0/60/60;
									    }else{
									    	//否则计算实际上班时间-默认应工作时间
									    	 overWorkHour=(inFactKeepHour-mustKeepHour)>0?(inFactKeepHour-mustKeepHour):0;
									    }
									    attendanceDataDate.setOver_time(overWorkHour);
									    
									    //统计今天是上班还是放假
									    attendanceDataDate.setToday_work("1");

					       }else{
					    	     //最早打卡日期
							     Date earlyest=getEarlyestCheckTime(attendanceCheckList);
							     //最晩打卡日期
							     Date latest=getLatestCheckTime(attendanceCheckList);		  
							     
							     //设置上班时间为earlyest
							     attendanceDataDate.setWork_begin_time1(DateUtil.getDateString(earlyest));
							     //设置下班时间为latest
							     attendanceDataDate.setWork_end_time2(DateUtil.getDateString(latest)); 
							      //获取外出时间的并集，并保证集合中的元素都落在statisticElasticDefaultDutyTime1, statisticElasticDefaultDutyTime2范围内
							     List<OutWork> outWorkStartEndList=	geOutWorkListStartEndWithNoCascade(outWorkList, statisticElasticDefaultDutyTime1, statisticElasticDefaultDutyTime2);
                                 int overTimeTotal=0;
							     if(earlyest!=null&&latest!=null){//如果上班下班都有打卡的话
							    	   		//获取外出时间(已经被修剪在默认上下班时间范围内)与earlyest,latest的并集				    	 
							    	  List<OutWork> outWorkStartEndWorkList=geOutWorkListStartEndWithNoCascadeAsWorkNoIntegrate(outWorkStartEndList,earlyest,latest);
							    	  overTimeTotal=  getTodayOutWorkTotal(outWorkStartEndWorkList);
									  attendanceDataDate.setOver_time(overTimeTotal*1.0/60.0/60); 
							     }else{
							    	 //计算外出时间累计值
								   	  overTimeTotal=  getTodayOutWorkTotal(outWorkStartEndList);
									  attendanceDataDate.setOver_time(overTimeTotal*1.0/60.0/60); 
							     }
							     
								 attendanceDataDate.setShould_work_time(0.0);
								 attendanceDataDate.setWork_time(0.0);
								 
								 //设置统计值
									attendanceDataDate.setKg_time(0.0);
									attendanceDataDate.setKg_count(0); 
						            attendanceDataDate.setLater_count(0); 
						            attendanceDataDate.setLater_time(0.0);
						            attendanceDataDate.setEarly_count(0);
						            attendanceDataDate.setEarly_time(0.0);
						            attendanceDataDate.setCard_not(0);
						            attendanceDataDate.setLeave_total_time(0.0);
						            attendanceDataDate.setOut_total_time(0.0);
					       }
					            
					            
					          //统计补打卡次数
							    if(attendanceCheckList.size()>0){
							    	int reCheckCount=getCardReCheckCount(attendanceCheckList);
							    	attendanceDataDate.setCard_status(reCheckCount);
							    }
					            
					            //统计节假日信息
							    if(holidayList.size()>0){
							       attendanceDataDate.setIsholiday(1) ;
							    }else{
							    	attendanceDataDate.setIsholiday(0) ;
							    }
							    
							  
 
							    
							    //统计今天是上班还是放假
							    if(isVocation){
							    	attendanceDataDate.setIsvocation(1);
							    }else{
							    	attendanceDataDate.setIsvocation(0);
							    }
							    
							    
							    //统计今天是否签到
							    if(attendanceCheckList.size()>0){
							    	attendanceDataDate.setToday_work("1");
							    }else{
							    	attendanceDataDate.setToday_work("0");
							    }
							    
				                //设置班次Id
							    attendanceDataDate.setDuty_id(attendanceDuty.getId());
							    attendanceDataDate.setDept_num(attendanceDuty.getDept_num());   //重新更新某一天的统计记录中的冗余部门
							    attendanceDataDate.setDept_name(attendanceDuty.getDept_name());
							    attendanceDataDate.setStaff_no(employee.getYhNo());
							    //如果是节假日或公休日，并且是补班的
							    if ((isVocation||holidayList.size()>=0)&&extralWorkList.size()>0) {
									attendanceDataDate.setIs_extralwork(1);
								}else{
									attendanceDataDate.setIs_extralwork(0);
								}
							    statisticCalService.updateAttendanceDataDate(attendanceDataDate);
							    
				    }
				
			}
			
			
			
			
			
			
			
 		}
		
		//员工考勤月统计
		@Override
			public void personalAttendanceMonthStatistic(){
 			}
		
			//部门考勤月统计
		@Override
			public void departmentAttendanceMonthStatistic(){
 			}


		
		//通过传入请假开始时间，结束时间和员工编号，计算该人在这一段时间内工作班次内的请假时间是多少,要扣除节假日，公休日等
		/**首先会获取startTime和endTime之间的某一个人的所有排班，例如：请假时间是2017-01-10 08:00:00----2017-01-18 18:00:00,排班情况 为
		 * 2017-01-01 08:00:00---2017-01-15 18:00:00为A班次,   2017-01-16 09:00:00-----2017-01-20 19:00:00为B班次
		 * 这样我们获取的排班情况就有A，B两个班次，由于A，B两个班次的上下班时间不一样，因此请假计算时就要分开计算
		 * 具体计算方法如下:
		 * 		计算A班次的排班区间与请假时间的交集,例如本例交集为2017-01-10 08:00:00-----2017-01-15 18:00:00
		 * 		按天循环这个交集，计算这个交集中的每一天的请假时间，当然当天如果有节假日或公休日时，节假日，公休日不能算入请假时间
		 * 		累计每一天的请假时间
		 *      对B班次的算法同A班次。
		 *      将所有计算的请假时间累计后就得到了总的请假时间了
		 *      
		 *    
		*/
		@Override
		public Double getLeaveTimeTotal(Date startTime, Date endTime,String staffNum) {
			  Map<String, String> attendanceDutyUserMap=new HashMap<String, String>();
			  attendanceDutyUserMap.put("staffNum", staffNum);
			  attendanceDutyUserMap.put("startTime", DateUtil.getDateString(startTime));
			  attendanceDutyUserMap.put("endTime", DateUtil.getDateString(endTime));
			  
			  
			  //查询员工详情
			  Employee employee=employeeDao.getEmployeeDetail(staffNum);
			  
			  
			  LeaveTime leaveTime=new LeaveTime();
			  leaveTime.setFromTime(DateUtil.getDateString(startTime));
			  leaveTime.setToTime(DateUtil.getDateString(endTime));
              //查询该时间段内的排班
			  List<AttendanceDutyUser> attendanceDutyUserList=statisticCalService.getAttendanceDutyUserListByStaffAndTime(attendanceDutyUserMap);
			  if(attendanceDutyUserList.size()==0){
				  return null;
			  }
			  Double leaveTimeTotalHour=0.0;
			  //迭代员工排班,因此不同班次内的上班时间可能不一样，所以计算请假时间时需要按班次计算
			  for(AttendanceDutyUser adu:attendanceDutyUserList){
				  Map<String,Integer> attendanceDutyMap=new HashMap<String, Integer>();
				  attendanceDutyMap.put("id", adu.getDuty_id());
				  //查询班次
				  AttendanceDuty ad=statisticCalService.getAttendanceDutyById(attendanceDutyMap);
				
				  
				  //获取请假与该排班开始时间和结束时间的交集
				  LeaveTime lt= getLeaveTimeWithNoCascadeWithAttendanceDutyUser(leaveTime, adu);

				  //构建一个要传入getTodayLeaveTimeTotal方法的参数
				  List<LeaveTime> ltList=new ArrayList<LeaveTime>();
				  ltList.add(lt);
				  
				  //循环开始时间
				  Date dutyBeginTime=getPreviousDayTime(DateUtil.getDateTime(lt.getFromTime()));
				  //循环结束时间
				  Date dutyEndTime=DateUtil.getDateTime(lt.getToTime());
				  
				  
				  //计算排班跨了几天，即计算循环次数
				  int daysInterval=getDaysInterval(dutyBeginTime, dutyEndTime);
				  int start=0;
				  
			      //如果是非弹性工作制
				  if(ad.getIsElastic()==0){
					      //排班开始于哪一天
						  String yearMonthDay=getSimpleDateString(dutyBeginTime);
						  
						  String dutyStartCheckPointStr=yearMonthDay+" "+ad.getDutyStartCheckPoint();
						  
						  String dutyTime1CheckPointdateStr=yearMonthDay+" "+ad.getDutyTime1Point();
						  String dutyTime4CheckPointdateStr=yearMonthDay+" "+ad.getDutyTime4Point();
	                      

						  String dutyTime1Str=yearMonthDay+" "+ad.getDutyTime1();
						  String dutyTime2Str=yearMonthDay+" "+ad.getDutyTime2();
						  String dutyTime3Str=yearMonthDay+" "+ad.getDutyTime3();
						  String dutyTime4Str=yearMonthDay+" "+ad.getDutyTime4();
						  
						  //班次开始统计时间
						  Date dutyStartCheckPoint=getSimpleDate(dutyStartCheckPointStr);
						  
						  //上午上班打卡时间 
						  Date dutyTime1CheckPoint= getSimpleDate(dutyTime1CheckPointdateStr); 
						  //下午下班打卡时间
						  Date dutyTime4CheckPoint=getSimpleDate(dutyTime4CheckPointdateStr);
						  
						  Date dutyTime1=getSimpleDate(dutyTime1Str);
						  Date dutyTime2=getSimpleDate(dutyTime2Str);
						  Date dutyTime3=getSimpleDate(dutyTime3Str);
						  Date dutyTime4=getSimpleDate(dutyTime4Str);
						  
						  
						  //=======================================计算时间轴开始======================================
						  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
						  if(dutyStartCheckPoint.compareTo(dutyTime1CheckPoint)>0){
							  dutyTime1CheckPoint=getNextDayTime(dutyTime1CheckPoint);
						  }
						  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
						  if(dutyStartCheckPoint.compareTo(dutyTime4CheckPoint)>0){
							  dutyTime4CheckPoint=getNextDayTime(dutyTime4CheckPoint);
						  }
						  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
						  if(dutyStartCheckPoint.compareTo(dutyTime1)>0){
							  dutyTime1=getNextDayTime(dutyTime1);
						  }
						  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
						  if(dutyStartCheckPoint.compareTo(dutyTime2)>0){
							  dutyTime2=getNextDayTime(dutyTime2);
						  }
						  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
						  if(dutyStartCheckPoint.compareTo(dutyTime3)>0){
							  dutyTime3=getNextDayTime(dutyTime3);
						  }
						  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
						  if(dutyStartCheckPoint.compareTo(dutyTime4)>0){
							  dutyTime4=getNextDayTime(dutyTime4);
						  }
						  //=======================================计算时间轴结束======================================

						  
						  //获取补班列表

							   List<Holiday>  extralWorkList=getExtralWorkListByPoint(dutyTime1CheckPoint, employee.getXb(), employee.getBmDm());

						  //获取当前的节假日
							   List<Holiday>  holidayList=getHolidayListByPoint(dutyTime1CheckPoint, employee.getXb() , employee.getBmDm());

						    //早上是否是公休日和节假日
				             boolean morningIsNotVocationOrHoliday=(!isVocation(ad, dutyTime1CheckPoint)&(holidayList.size()==0||(holidayList.size()>0&&(holidayList.get(0).getTime_period()&1)==0)));  
				             //下午是否是公休日和节假日
				             boolean afternoonIsNotVocationOrHoliday=(!isVocation(ad, dutyTime1CheckPoint)&(holidayList.size()==0||(holidayList.size()>0&&(holidayList.get(0).getTime_period()&2)==0)));  

				             //如果上午不是公休日也不是节假日,或者是补班时
						    if (morningIsNotVocationOrHoliday||extralWorkList.size()>0) {
								  leaveTimeTotalHour=leaveTimeTotalHour+(getTodayLeaveTimeTotal(ltList , dutyTime1, dutyTime2))*1.0/60.0/60;
							}
						    //如果下午不是公休日也不是节假日,或者是补班时
						   if(afternoonIsNotVocationOrHoliday||extralWorkList.size()>0){
							  leaveTimeTotalHour=leaveTimeTotalHour+(getTodayLeaveTimeTotal(ltList , dutyTime3, dutyTime4))*1.0/60.0/60;
						  }
						  start++;
						  
						  
						  //循环
						  while (start<=daysInterval) {  
							     dutyTime1CheckPoint=getNextDayTime(dutyTime1CheckPoint);
							     dutyTime4CheckPoint=getNextDayTime(dutyTime4CheckPoint);
							     dutyTime1=getNextDayTime(dutyTime1);
							     dutyTime2=getNextDayTime(dutyTime2);
							     dutyTime3=getNextDayTime(dutyTime3);
							     dutyTime4=getNextDayTime(dutyTime4);
							     
							   //获取补班列表

								    extralWorkList=getExtralWorkListByPoint(dutyTime1CheckPoint, employee.getXb(), employee.getBmDm());

							     
								  //获取当前的节假日 
								  holidayList=getHolidayListByPoint(dutyTime1CheckPoint, employee.getXb() , employee.getBmDm());
							     //上午是否是公休日或节假日
								  morningIsNotVocationOrHoliday=(!isVocation(ad, dutyTime1CheckPoint)&(holidayList.size()==0||(holidayList.size()>0&&(holidayList.get(0).getTime_period()&1)==0)));  
					              //下午是否是公休日或节假日
								  afternoonIsNotVocationOrHoliday=(!isVocation(ad, dutyTime1CheckPoint)&(holidayList.size()==0||(holidayList.size()>0&&(holidayList.get(0).getTime_period()&2)==0))); 
							     
					              //如果上午不是公休日也不是节假日
								    if (morningIsNotVocationOrHoliday||extralWorkList.size()>0) {
								    	//累加上午的请假时间
										  leaveTimeTotalHour=leaveTimeTotalHour+(getTodayLeaveTimeTotal(ltList , dutyTime1, dutyTime2))*1.0/60.0/60;
									}
								    //如果下午不是公休日也不是节假日
								   if(afternoonIsNotVocationOrHoliday||extralWorkList.size()>0){
									   //累加下午的请假时间
									  leaveTimeTotalHour=leaveTimeTotalHour+(getTodayLeaveTimeTotal(ltList , dutyTime3, dutyTime4))*1.0/60.0/60;
								  }
							  start++;
						} 
  
				  }else{//如果是弹性工作制
					//排班开始于哪一天
					  String yearMonthDay=getSimpleDateString(dutyBeginTime);
					  String statisticStartCheckPointStr=yearMonthDay+" "+ad.getDutyStartCheckPoint();
					  String statisticEndCheckPointStr=yearMonthDay+" "+ad.getDutyEndCheckPoint();
	
					  String statisticElasticDefaultDutyTime1Str=yearMonthDay+" "+ad.getElasticDefaultDutyTime1();
					  String statisticElasticDefaultDutyTime2Str=yearMonthDay+" "+ad.getElasticDefaultDutyTime2();
					  //开始统计打卡时间
					  Date statisticStartCheckPoint=getSimpleDate(statisticStartCheckPointStr);
					  //结束统计打卡时间
					  Date statisticEndCheckPoint=getSimpleDate(statisticEndCheckPointStr);
					  //默认上班时间
					  Date statisticElasticDefaultDutyTime1=getSimpleDate(statisticElasticDefaultDutyTime1Str);
					  //默认下班时间
					  Date statisticElasticDefaultDutyTime2=getSimpleDate(statisticElasticDefaultDutyTime2Str);
					 //======================================计算时间轴开始========================
					  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
					  if(statisticStartCheckPoint.compareTo(statisticEndCheckPoint)>0){
						  statisticEndCheckPoint=getNextDayTime(statisticEndCheckPoint);
					  }
					  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
					  if(statisticStartCheckPoint.compareTo(statisticElasticDefaultDutyTime1)>0){
						  statisticElasticDefaultDutyTime1=getNextDayTime(statisticElasticDefaultDutyTime1);
					  }
					  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
					  if(statisticStartCheckPoint.compareTo(statisticElasticDefaultDutyTime2)>0){
						  statisticElasticDefaultDutyTime2=getNextDayTime(statisticElasticDefaultDutyTime2);
					  }
					  //======================================计算时间轴结束========================
					  
					  //获取补班列表

					  List<Holiday>  extralWorkList=getExtralWorkListByPoint(statisticStartCheckPoint, employee.getXb(), employee.getBmDm());

					  
					//获取当前的节假日
					    
					    List<Holiday>  holidayList=getHolidayListByPoint(statisticStartCheckPoint, employee.getXb() , employee.getBmDm());

					    
					    //如果不是公休日,节假日或补班
					    if((!isVocation(ad, statisticStartCheckPoint)&&holidayList.size()==0)||extralWorkList.size()>0){
							  leaveTimeTotalHour=leaveTimeTotalHour+(getTodayLeaveTimeTotal(ltList , statisticElasticDefaultDutyTime1, statisticElasticDefaultDutyTime2))*1.0/60.0/60;
						  }
						  start++;
						  //循环
						  while (start<=daysInterval) { 
							  
							  statisticStartCheckPoint=getNextDayTime(statisticStartCheckPoint);
							  statisticEndCheckPoint=getNextDayTime(statisticEndCheckPoint);
							  statisticElasticDefaultDutyTime1=getNextDayTime(statisticElasticDefaultDutyTime1);
							  statisticElasticDefaultDutyTime2=getNextDayTime(statisticElasticDefaultDutyTime2);
							  
							  //获取补班列表
							    extralWorkList=getExtralWorkListByPoint(statisticStartCheckPoint, employee.getXb(), employee.getBmDm());

							  
							  
								  //获取当前的节假日
					
							    holidayList=getHolidayListByPoint(statisticStartCheckPoint, employee.getXb() , employee.getBmDm());
							  //如果是工作日或补班
							  if((!isVocation(ad, statisticStartCheckPoint)&&holidayList.size()==0)||extralWorkList.size()>0){
								  //计算当天加班时间，并累加
								  leaveTimeTotalHour=leaveTimeTotalHour+(getTodayLeaveTimeTotal(ltList , statisticElasticDefaultDutyTime1, statisticElasticDefaultDutyTime2))*1.0/60.0/60;
							  }
							  start++;
						} 
				  } 
			  }
			  
			  return leaveTimeTotalHour;
			  
		   
		}
		
		
		
		//通过传入请假开始时间，结束时间和员工编号，计算该人在这一段时间内工作班次内的请假时间是多少,要扣除节假日，公休日等
				/**首先会获取startTime和endTime之间的某一个人的所有排班，例如：请假时间是2017-01-10 08:00:00----2017-01-18 18:00:00,排班情况 为
				 * 2017-01-01 08:00:00---2017-01-15 18:00:00为A班次,   2017-01-16 09:00:00-----2017-01-20 19:00:00为B班次
				 * 这样我们获取的排班情况就有A，B两个班次，由于A，B两个班次的上下班时间不一样，因此请假计算时就要分开计算
				 * 具体计算方法如下:
				 * 		计算A班次的排班区间与请假时间的交集,例如本例交集为2017-01-10 08:00:00-----2017-01-15 18:00:00
				 * 		按天循环这个交集，计算这个交集中的每一天的请假时间，当然当天如果有节假日或公休日时，节假日，公休日不能算入请假时间
				 * 		累计每一天的请假时间
				 *      对B班次的算法同A班次。
				 *      将所有计算的请假时间累计后就得到了总的请假时间了
				 *      
				 *    
				*/
			
				public Integer getLeaveTimeTotalInSeconds(Date startTime, Date endTime,String staffNum) {
					  Map<String, String> attendanceDutyUserMap=new HashMap<String, String>();
					  attendanceDutyUserMap.put("staffNum", staffNum);
					  attendanceDutyUserMap.put("startTime", DateUtil.getDateString(startTime));
					  attendanceDutyUserMap.put("endTime", DateUtil.getDateString(endTime));
					  
					  
					  Employee employee=employeeDao.getEmployeeDetail(staffNum);

					  
					  LeaveTime leaveTime=new LeaveTime();
					  leaveTime.setFromTime(DateUtil.getDateString(startTime));
					  leaveTime.setToTime(DateUtil.getDateString(endTime));
		              //获取startTime,endTime之间的所有排班
					  List<AttendanceDutyUser> attendanceDutyUserList=statisticCalService.getAttendanceDutyUserListByStaffAndTime(attendanceDutyUserMap);
					  if(attendanceDutyUserList.size()==0){
						  return null;
					  }
					  Integer leaveTimeTotalSeconds=0;
					  //按班次计算对应排班区间下的请假时间累计,所以在此迭代班次
					  for(AttendanceDutyUser adu:attendanceDutyUserList){
						  Map<String,Integer> attendanceDutyMap=new HashMap<String, Integer>();
						  attendanceDutyMap.put("id", adu.getDuty_id());
						  AttendanceDuty ad=statisticCalService.getAttendanceDutyById(attendanceDutyMap);
						
						  
						  //获取假期与该排班开始时间和结束时间的交集，因为我们某一个排班内的请假按该排班的时间计算
						  LeaveTime lt= getLeaveTimeWithNoCascadeWithAttendanceDutyUser(leaveTime, adu);

						  //构建一个要传入getTodayLeaveTimeTotal方法的参数
						  List<LeaveTime> ltList=new ArrayList<LeaveTime>();
						  ltList.add(lt);
						  
						  //循环开始时间
						  Date dutyBeginTime=getPreviousDayTime(DateUtil.getDateTime(lt.getFromTime()));
						  //循环结束时间
						  Date dutyEndTime=DateUtil.getDateTime(lt.getToTime());
						  
						  
						  //计算循环次数
						  int daysInterval=getDaysInterval(dutyBeginTime, dutyEndTime);
						  int start=0;
						  
					      //非弹性工作制
						  if(ad.getIsElastic()==0){
								  String yearMonthDay=getSimpleDateString(dutyBeginTime);
								  
								  String dutyStartCheckPointStr=yearMonthDay+" "+ad.getDutyStartCheckPoint();
								  
								  String dutyTime1CheckPointdateStr=yearMonthDay+" "+ad.getDutyTime1Point();
								  String dutyTime4CheckPointdateStr=yearMonthDay+" "+ad.getDutyTime4Point();
			                      

								  String dutyTime1Str=yearMonthDay+" "+ad.getDutyTime1();
								  String dutyTime2Str=yearMonthDay+" "+ad.getDutyTime2();
								  String dutyTime3Str=yearMonthDay+" "+ad.getDutyTime3();
								  String dutyTime4Str=yearMonthDay+" "+ad.getDutyTime4();
								  
								  //班次开始统计时间
								  Date dutyStartCheckPoint=getSimpleDate(dutyStartCheckPointStr);
								  
								  //上午上班考勤时间 
								  Date dutyTime1CheckPoint= getSimpleDate(dutyTime1CheckPointdateStr); 
								  //下午下班考勤时间
								  Date dutyTime4CheckPoint=getSimpleDate(dutyTime4CheckPointdateStr);
								  
								  Date dutyTime1=getSimpleDate(dutyTime1Str);
								  Date dutyTime2=getSimpleDate(dutyTime2Str);
								  Date dutyTime3=getSimpleDate(dutyTime3Str);
								  Date dutyTime4=getSimpleDate(dutyTime4Str);
								  
								  
								  
								  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
								  if(dutyStartCheckPoint.compareTo(dutyTime1CheckPoint)>0){
									  dutyTime1CheckPoint=getNextDayTime(dutyTime1CheckPoint);
								  }
								  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
								  if(dutyStartCheckPoint.compareTo(dutyTime4CheckPoint)>0){
									  dutyTime4CheckPoint=getNextDayTime(dutyTime4CheckPoint);
								  }
								  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
								  if(dutyStartCheckPoint.compareTo(dutyTime1)>0){
									  dutyTime1=getNextDayTime(dutyTime1);
								  }
								  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
								  if(dutyStartCheckPoint.compareTo(dutyTime2)>0){
									  dutyTime2=getNextDayTime(dutyTime2);
								  }
								  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
								  if(dutyStartCheckPoint.compareTo(dutyTime3)>0){
									  dutyTime3=getNextDayTime(dutyTime3);
								  }
								  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
								  if(dutyStartCheckPoint.compareTo(dutyTime4)>0){
									  dutyTime4=getNextDayTime(dutyTime4);
								  }
								  
								  //获取补班列表 
									   List<Holiday>  extralWorkList=getExtralWorkListByPoint(dutyTime1CheckPoint, employee.getXb(), employee.getBmDm());

								  //获取当前的节假日

								    List<Holiday>  holidayList=getHolidayListByPoint(dutyTime1CheckPoint, employee.getXb() , employee.getBmDm());

								    boolean morningIsNotVocationOrHoliday=(!isVocation(ad, dutyTime1CheckPoint)&(holidayList.size()==0||(holidayList.size()>0&&(holidayList.get(0).getTime_period()&1)==0)));  
						             boolean afternoonIsNotVocationOrHoliday=(!isVocation(ad, dutyTime1CheckPoint)&(holidayList.size()==0||(holidayList.size()>0&&(holidayList.get(0).getTime_period()&2)==0)));  

						             //如果上午不是公休日也不是节假日
								    if (morningIsNotVocationOrHoliday||extralWorkList.size()>0) {
										  leaveTimeTotalSeconds=leaveTimeTotalSeconds+(getTodayLeaveTimeTotal(ltList , dutyTime1, dutyTime2));
									}
								    //如果下填缝剂是公休日也不是节假日
								   if(afternoonIsNotVocationOrHoliday||extralWorkList.size()>0){
										  leaveTimeTotalSeconds=leaveTimeTotalSeconds+(getTodayLeaveTimeTotal(ltList , dutyTime3, dutyTime4));

								  }
								   
								  start++;
								  
								  
								  //循环
								  while (start<=daysInterval) {  
									     dutyTime1CheckPoint=getNextDayTime(dutyTime1CheckPoint);
									     dutyTime4CheckPoint=getNextDayTime(dutyTime4CheckPoint);
									     dutyTime1=getNextDayTime(dutyTime1);
									     dutyTime2=getNextDayTime(dutyTime2);
									     dutyTime3=getNextDayTime(dutyTime3);
									     dutyTime4=getNextDayTime(dutyTime4);
									     
									     
									     //获取补班列表
										
										  extralWorkList=getExtralWorkListByPoint(dutyTime1CheckPoint, employee.getXb(), employee.getBmDm());

										  //获取当前的节假日

										 
										  holidayList=getHolidayListByPoint(dutyTime1CheckPoint, employee.getXb() , employee.getBmDm());  
									      morningIsNotVocationOrHoliday=(!isVocation(ad, dutyTime1CheckPoint)&(holidayList.size()==0||(holidayList.size()>0&&(holidayList.get(0).getTime_period()&1)==0)));  
							              afternoonIsNotVocationOrHoliday=(!isVocation(ad, dutyTime1CheckPoint)&(holidayList.size()==0||(holidayList.size()>0&&(holidayList.get(0).getTime_period()&2)==0)));  

							             //如果上午不是公休日也不是节假日
									    if (morningIsNotVocationOrHoliday||extralWorkList.size()>0) {
											  leaveTimeTotalSeconds=leaveTimeTotalSeconds+(getTodayLeaveTimeTotal(ltList , dutyTime1, dutyTime2));
										}
									    //如果下填缝剂是公休日也不是节假日
									   if(afternoonIsNotVocationOrHoliday||extralWorkList.size()>0){
											  leaveTimeTotalSeconds=leaveTimeTotalSeconds+(getTodayLeaveTimeTotal(ltList , dutyTime3, dutyTime4));

									  }

									  start++;
								} 
		  
						  }else{
							  String yearMonthDay=getSimpleDateString(dutyBeginTime);
							  String statisticStartCheckPointStr=yearMonthDay+" "+ad.getDutyStartCheckPoint();
							  String statisticEndCheckPointStr=yearMonthDay+" "+ad.getDutyEndCheckPoint();
			
							  String statisticElasticDefaultDutyTime1Str=yearMonthDay+" "+ad.getElasticDefaultDutyTime1();
							  String statisticElasticDefaultDutyTime2Str=yearMonthDay+" "+ad.getElasticDefaultDutyTime2();
							  
							  Date statisticStartCheckPoint=getSimpleDate(statisticStartCheckPointStr);
							  Date statisticEndCheckPoint=getSimpleDate(statisticEndCheckPointStr);
							  Date statisticElasticDefaultDutyTime1=getSimpleDate(statisticElasticDefaultDutyTime1Str);
							  Date statisticElasticDefaultDutyTime2=getSimpleDate(statisticElasticDefaultDutyTime2Str);
							 
							  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
							  if(statisticStartCheckPoint.compareTo(statisticEndCheckPoint)>0){
								  statisticEndCheckPoint=getNextDayTime(statisticEndCheckPoint);
							  }
							  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
							  if(statisticStartCheckPoint.compareTo(statisticElasticDefaultDutyTime1)>0){
								  statisticElasticDefaultDutyTime1=getNextDayTime(statisticElasticDefaultDutyTime1);
							  }
							  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
							  if(statisticStartCheckPoint.compareTo(statisticElasticDefaultDutyTime2)>0){
								  statisticElasticDefaultDutyTime2=getNextDayTime(statisticElasticDefaultDutyTime2);
							  }
							  
							//获取补班列表
							
								   List<Holiday>  extralWorkList=getExtralWorkListByPoint(statisticStartCheckPoint, employee.getXb(), employee.getBmDm());

							  
							//获取当前的节假日
							  
							    List<Holiday>  holidayList=getHolidayListByPoint(statisticStartCheckPoint, employee.getXb() , employee.getBmDm());

							    //如果不是公休日也不是节假日，或都是补 班
							    if((!isVocation(ad, statisticStartCheckPoint)&&holidayList.size()==0)||extralWorkList.size()>0){
							    	//计算请假时间并累计
							    	leaveTimeTotalSeconds=leaveTimeTotalSeconds+(getTodayLeaveTimeTotal(ltList , statisticElasticDefaultDutyTime1, statisticElasticDefaultDutyTime2));
								  }
								  start++;
								  //循环
								  while (start<=daysInterval) {  
									  statisticStartCheckPoint=getNextDayTime(statisticStartCheckPoint);
									  statisticEndCheckPoint=getNextDayTime(statisticEndCheckPoint);
									  statisticElasticDefaultDutyTime1=getNextDayTime(statisticElasticDefaultDutyTime1);
									  statisticElasticDefaultDutyTime2=getNextDayTime(statisticElasticDefaultDutyTime2);
									  
									  //获取补班列表
									 extralWorkList=getExtralWorkListByPoint(statisticStartCheckPoint, employee.getXb(), employee.getBmDm());

									  
									  
										  //获取当前的节假日
										holidayList=getHolidayListByPoint(statisticStartCheckPoint, employee.getXb() , employee.getBmDm());
										//如果不是公休日，节假日 或都是补班
									  if((!isVocation(ad, statisticStartCheckPoint)&&holidayList.size()==0)||extralWorkList.size()>0){
										
										  //计算请假时间并累计
										  leaveTimeTotalSeconds=leaveTimeTotalSeconds+(getTodayLeaveTimeTotal(ltList , statisticElasticDefaultDutyTime1, statisticElasticDefaultDutyTime2));
									  }
									  start++;
								} 
						  } 
					  }
					  
					  return leaveTimeTotalSeconds;
					  
				   
				}
		
		
		public Map<String, Object> getLeaveTimeTotal_new(Date startTime, Date endTime,String staffNum){
			  Map<String, String> attendanceDutyUserMap=new HashMap<String, String>();
			  attendanceDutyUserMap.put("staffNum", staffNum);
			  attendanceDutyUserMap.put("startTime", DateUtil.getDateString(startTime));
			  attendanceDutyUserMap.put("endTime", DateUtil.getDateString(endTime));
			  List<AttendanceDutyUser> attendanceDutyUserList=statisticCalService.getAttendanceDutyUserListByStaffAndTime(attendanceDutyUserMap);
			  List<Map<String, String>> list=new ArrayList<Map<String,String>>();
			  Double days=0.0;
			  DecimalFormat df=new DecimalFormat("#.##"); //格式化小时，保留两位小数，最后位是舍入
			  Map<String, Object> m=new HashMap<String, Object>();
              for (int i = 0; i < attendanceDutyUserList.size(); i++) {
				  AttendanceDutyUser adu=attendanceDutyUserList.get(i);
				  //计算请假时间和某一个排班的交集
				  Date st=startTime;
				  Date et=endTime;
				  if (startTime.before(getSimpleDate(adu.getBegin_time()))) {
					  st=getSimpleDate(adu.getBegin_time());
				  } 
				  if(endTime.after(getSimpleDate(adu.getEnd_time()))){
					  et=getSimpleDate(adu.getEnd_time());
				  }
				  
				  Integer leaveTimeInSeconds=getLeaveTimeTotalInSeconds(st, et, staffNum);
				  if (leaveTimeInSeconds==null) {
					return null;
				   }
				  //构建查询条件map
				  Map<String,Integer> attendanceDutyMap=new HashMap<String, Integer>();
				  attendanceDutyMap.put("id", adu.getDuty_id());
				  AttendanceDuty ad=statisticCalService.getAttendanceDutyById(attendanceDutyMap);  
				  Integer shouldWorkSeconds=8*60*60; //应该工作时间
				  Map<String, String> map=new HashMap<String, String>();
				  //以下为计算某一个班次每天应该工作的时间
				  //非弹性工作制
				  if (ad.getIsElastic()==0) {
					  String yearMonthDay=getSimpleDateString(new Date());
					  String dutyStartCheckPointStr=yearMonthDay+" "+ad.getDutyStartCheckPoint();

					  String dutyTime1Str=yearMonthDay+" "+ad.getDutyTime1();
					  String dutyTime2Str=yearMonthDay+" "+ad.getDutyTime2();
					  String dutyTime3Str=yearMonthDay+" "+ad.getDutyTime3();
					  String dutyTime4Str=yearMonthDay+" "+ad.getDutyTime4();
					  
					  //班次开始统计时间
					  Date dutyStartCheckPoint=getSimpleDate(dutyStartCheckPointStr);
					  

					  Date dutyTime1=getSimpleDate(dutyTime1Str);
					  Date dutyTime2=getSimpleDate(dutyTime2Str);
					  Date dutyTime3=getSimpleDate(dutyTime3Str);
					  Date dutyTime4=getSimpleDate(dutyTime4Str);
					  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
					  if(dutyStartCheckPoint.compareTo(dutyTime1)>0){
						  dutyTime1=getNextDayTime(dutyTime1);
					  }
					  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
					  if(dutyStartCheckPoint.compareTo(dutyTime2)>0){
						  dutyTime2=getNextDayTime(dutyTime2);
					  }
					  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
					  if(dutyStartCheckPoint.compareTo(dutyTime3)>0){
						  dutyTime3=getNextDayTime(dutyTime3);
					  }
					  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
					  if(dutyStartCheckPoint.compareTo(dutyTime4)>0){
						  dutyTime4=getNextDayTime(dutyTime4);
					  }
					  shouldWorkSeconds=getSecondsInterval(dutyTime1, dutyTime2)+getSecondsInterval(dutyTime3, dutyTime4);
				  }else{
					  String yearMonthDay=getSimpleDateString(new Date());
					  String statisticStartCheckPointStr=yearMonthDay+" "+ad.getDutyStartCheckPoint();
	
					  String statisticElasticDefaultDutyTime1Str=yearMonthDay+" "+ad.getElasticDefaultDutyTime1();
					  String statisticElasticDefaultDutyTime2Str=yearMonthDay+" "+ad.getElasticDefaultDutyTime2();
					  
					  Date statisticStartCheckPoint=getSimpleDate(statisticStartCheckPointStr);
					  Date statisticElasticDefaultDutyTime1=getSimpleDate(statisticElasticDefaultDutyTime1Str);
					  Date statisticElasticDefaultDutyTime2=getSimpleDate(statisticElasticDefaultDutyTime2Str);
					 

					  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
					  if(statisticStartCheckPoint.compareTo(statisticElasticDefaultDutyTime1)>0){
						  statisticElasticDefaultDutyTime1=getNextDayTime(statisticElasticDefaultDutyTime1);
					  }
					  //如果后面的时间比前面的小，说明跨天了,则后面的时间要加一天
					  if(statisticStartCheckPoint.compareTo(statisticElasticDefaultDutyTime2)>0){
						  statisticElasticDefaultDutyTime2=getNextDayTime(statisticElasticDefaultDutyTime2);
					  }
					  
					  shouldWorkSeconds=getSecondsInterval(statisticElasticDefaultDutyTime1, statisticElasticDefaultDutyTime2);
				  }
				  days=days+leaveTimeInSeconds*1.0/shouldWorkSeconds;// 计算请假累计时间,按天算
				  map.put("dutyName",ad.getDutyName());
				  map.put("days_s",String.valueOf(leaveTimeInSeconds/shouldWorkSeconds) );//某一个班次类内的请假天数
				  map.put("hours_s",String.valueOf(df.format((leaveTimeInSeconds%shouldWorkSeconds)/60/60.0)) );//某一个班次内的请假小时数
				  list.add(map);
			}
              
              
              m.put("days", String.valueOf(df.format(Double.valueOf(days))));
              m.put("list", list);
              return m;
			  
		}
		
		
		
		private LeaveTime getLeaveTimeWithNoCascadeWithAttendanceDutyUser(LeaveTime leaveTime,AttendanceDutyUser attendanceDutyUser){
		 Date leaveStart=DateUtil.getDateTime(leaveTime.getFromTime());
		 Date leaveEnd=DateUtil.getDateTime(leaveTime.getToTime());

			 Date adStart=getSimpleDate(attendanceDutyUser.getBegin_time());
			 Date adEnd=getSimpleDate(attendanceDutyUser.getEnd_time());	 
			 LeaveTime lt=new LeaveTime();
			 if(leaveStart.compareTo(adStart)>=0&&leaveStart.compareTo(adEnd)<0&&leaveEnd.compareTo(adEnd)>=0){
					 lt.setFromTime(DateUtil.getDateString(leaveStart));
					 lt.setToTime(DateUtil.getDateString(adEnd)); 				
			 }else if(leaveEnd.compareTo(adStart)>0&&leaveEnd.compareTo(adEnd)<=0&&leaveStart.compareTo(adStart)<=0){				 
					 lt.setFromTime(DateUtil.getDateString(adStart));
					 lt.setToTime(DateUtil.getDateString(leaveEnd));				
			 }else if(leaveEnd.compareTo(adEnd)>0&&leaveStart.compareTo(adStart)<0){
				 lt.setFromTime(DateUtil.getDateString(adStart));
				 lt.setToTime(DateUtil.getDateString(adEnd));
			 }else if(leaveEnd.compareTo(adStart)<0||leaveStart.compareTo(adEnd)>0){
				 //如果没有交集则，请假无用，所以把开始时间和结束时间都设为一样的，来表示没有请假
				 lt.setFromTime(DateUtil.getDateString(leaveStart));
				 lt.setToTime(DateUtil.getDateString(leaveStart));
			 }else{
				 lt.setFromTime(DateUtil.getDateString(leaveStart));
				 lt.setToTime(DateUtil.getDateString(leaveEnd));
			 }
	
		 return lt;
	 }
	
	@Autowired	
	 private VeryDeptMonthStatisticService veryDeptMonthStatisticService;
	@Autowired
	private VeryMonthStatisticService veryMonthStatisticService;
	//获取时间间隔天数
		public static int getDaysInterval(Date smallDate,Date biggerDate){
    	 Calendar smallCalendar=Calendar.getInstance();
    	 smallCalendar.setTime(smallDate);
    	 Calendar bigCalendar=Calendar.getInstance();
    	 bigCalendar.setTime(biggerDate);
    	 long small=smallCalendar.getTimeInMillis();
    	 long big=bigCalendar.getTimeInMillis();
    	 long left=(big-small)%(1000*60*60*24);
    	 int intervalDays=0;
    	 if(left==0){
    		 intervalDays=Math.abs(Long.valueOf((big-small)/(1000*60*60*24)).intValue());
    	 }else{
    		 intervalDays=Math.abs(Long.valueOf((big-small)/(1000*60*60*24)).intValue())+1;
    	 }
    	 return intervalDays;    	 
     } 
		
		@Resource(name = "attendHandleServiceImpl")
		private IAttendHandleService attendanceService;
		
        //计算某一个人在某一个时间区间内的考勤,该方法为统计方法总入口，该方法会在以下场景被调用:  
		/*   1.被该类personalAttendanceDayStatistic内部方法调用 
		   2.手动统计
		   3.工作流请假，销假，外出申请，忘打卡，加班申请完成后都会自动统计
		   4.补打卡，删除考勤记录时自动统计 
		   5.其它凡是涉及到影响某人考勤的单员工考勤数据修改时都应调用该方法重新统计
		    6.而对于导入考勤数据等批量操作，建议不调用，而直接引导用户进行手动统计*/
		@Override
		public void reStatisticByStaff(Date startTime, Date endTime,
				String staffNum) {
			   
			
			  try {
				
	
			
 			  Employee employee=employeeDao.getEmployeeDetail(staffNum);  
 			  
 			  //往前一天
			  Date sDate=getPreviousDayTime(startTime);
			  //往后一天
			  Date eDate=getNextDayTime(endTime);
			  //迭代两个时间区间内的每一天
			  while (sDate.compareTo(eDate)<=0) {
				  // 获取某一天一整天的时间范围
				  String yearMonthDay=getSimpleDateString(sDate);
				  String startYearMonthDayTime=yearMonthDay+" 00:00:00";
				  String endYearMonthDayTime=yearMonthDay+" 23:59:59";
				  
				
				  
				  Date start=DateUtil.getDateTime(startYearMonthDayTime);
				  Date end=DateUtil.getDateTime(endYearMonthDayTime);
				  //获取这一整天时间范围有交集的班次
				  Map<String, String> attendanceDutyUserMap=new HashMap<String, String>();
				  attendanceDutyUserMap.put("staffNum", staffNum);
				  attendanceDutyUserMap.put("startTime", getyyyyMMddHHmm(start));
				  attendanceDutyUserMap.put("endTime", getyyyyMMddHHmm(end));
				  //获取某人某一天的排班
				  List<AttendanceDutyUser> attendanceDutyUserList=statisticCalService.getAttendanceDutyUserListByStaffAndTime(attendanceDutyUserMap);
				  //如果有排班
				  if(attendanceDutyUserList.size()>0){
					  //认为第一个有效
                	  AttendanceDutyUser adu=attendanceDutyUserList.get(0);
                	  Map<String,Integer> attendanceDutyMap=new HashMap<String, Integer>();
					  attendanceDutyMap.put("id", adu.getDuty_id());
					  //获取当天班次
					  AttendanceDuty ad=statisticCalService.getAttendanceDutyById(attendanceDutyMap);   
					  ad.setDept_num(adu.getDept_num());//要将部门编号赋于它，这样如果员工换了部门,以保证统计的时候统计到员工当时所在部门。
					  Department department=departmentDao.getDepartByBmDm(adu.getDept_num());
					  ad.setDept_name(department.getBmMc()); //将当时员工所在的部门名称也传进 去
					  
					  
					  
					  
					  
					  
					  
					  
					  
					  
					  
					  String flagString=yearMonthDay+" "+ad.getDutyStartCheckPoint();//默认取开始统计时间
					  if (!(ad.getIsElastic()==1)) {//如果不是弹性班次，取上午上班时间
						flagString=yearMonthDay+" "+ad.getDutyTime1();
					}
					  
					  //结束统计打卡时间
					  String dateString=yearMonthDay+" "+ad.getDutyEndCheckPoint();
					  
					  Date flagDate= getSimpleDate(flagString);
					  
					  Date endDate=getSimpleDate(dateString);
					  
					 if(flagDate.compareTo(endDate)>0){//说明跨天了，endDate要加1 天
						 endDate=getNextDayTime(endDate);   
					 } 
					 //endDate相当于某一天结束统计打卡时间
					 personalAttendanceDayStatistic(endDate, employee, ad);
                  }
                  
				sDate=getNextDayTime(sDate);
			  }		
			  
			  //=====================================重新统计部门每月，员工每月开始=======================================
			  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");

			   startTime=DateUtil.getDate(sdf.format(startTime)+"-01");
			   endTime=DateUtil.getDate(sdf.format(endTime)+"-01");

			  while(startTime.compareTo(endTime)<=0){
				  String yMonthStr=sdf.format(startTime);
				  
				  //获取员工本月各排班期间所在的部门
				  List<String> deptNumList=statisticCalService.getAttendanceDutyUserDeptsByStaffNum(staffNum, yMonthStr);
				  //重新统计员工每月
				  for (int i = 0; i < deptNumList.size(); i++) {
					  veryMonthStatisticService.updateVeryMonthStatistic(staffNum, yMonthStr,deptNumList.get(i));
				}
				  
				 
				  
				  //因为一个员工可能会更换部门，所以我们统计部门每月考勤的时候需要将该员工本月所在的所有部门找出来进行统计
				  for (int i = 0; i < deptNumList.size(); i++) {
					//重新统计部门每月
					  veryDeptMonthStatisticService.updateDeptMonthStatistic(deptNumList.get(i), yMonthStr);
				  }
				  
				  startTime=getNextMonth(startTime);
			  }	
			  
			  } catch (Exception e) {
					e.printStackTrace();
		    }
		}

		
		
		
		

		@Override
		public void addStaffOt(Map map) {
			statisticCalService.addStaffOt(map);
		}

		@Override
		public void delStaffLeaveRecord(String oldId) {
			statisticCalService.delStaffLeaveRecord(oldId);
		}

		@Override
		public void insertCancelRecord(Map map) {
			statisticCalService.insertCancelRecord(map);
		} 
}
