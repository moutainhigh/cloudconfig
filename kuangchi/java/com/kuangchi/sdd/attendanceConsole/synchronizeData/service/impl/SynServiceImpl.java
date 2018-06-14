package com.kuangchi.sdd.attendanceConsole.synchronizeData.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.dutyuser.service.DutyUserService;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDuty;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDutyUser;
import com.kuangchi.sdd.attendanceConsole.statistic.service.StatisticCalService;
import com.kuangchi.sdd.attendanceConsole.statistic.service.impl.StatisticServiceImpl;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.AttendData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerBrushCardLog;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerCheckData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOrgDepartmentData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOrgUserAccountData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOutData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.service.AttendFileService;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.service.CheckDataService;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.service.OrgDataService;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.service.SynService;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.baseConsole.books.model.Books;
import com.kuangchi.sdd.baseConsole.books.service.IBooksService;
import com.kuangchi.sdd.baseConsole.books.service.impl.BooksServiceImpl;
import com.kuangchi.sdd.businessConsole.department.model.Department;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.employee.dao.EmployeeDao;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.businessConsole.process.dao.ProcessInstanceDao;
import com.kuangchi.sdd.businessConsole.process.model.LeaveModel;
import com.kuangchi.sdd.businessConsole.process.model.OutWorkModel;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.role.service.impl.RoleServiceImpl;
import com.kuangchi.sdd.businessConsole.station.dao.IStationDao;
import com.kuangchi.sdd.businessConsole.station.model.Station;
import com.kuangchi.sdd.businessConsole.station.service.IStationService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.doorAccessConsole.authority.service.PeopleAuthorityInfoService;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.action.LoginUserSyncAction;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.ServletUtil;

@Service("synService")
public class SynServiceImpl  implements SynService{
	
	public static final Logger LOG = Logger.getLogger(SynServiceImpl.class);
	   @Resource(name = "departmentServiceImpl")
	    private IDepartmentService departmentService;
	   
	    @Resource(name = "stationServiceImpl")
		private IStationService stationService;
	    @Resource(name="orgDataService")
	    private OrgDataService orgDataService;
	   
	    @Resource(name="checkDataService")
	    CheckDataService  checkDataService;
	    
		@Resource(name = "employeeService")
		EmployeeService employeeService;
		
		@Resource(name="booksServiceImpl")
		IBooksService booksServiceImpl;
		@Resource(name="statisticCalService")
		StatisticCalService statisticCalService;
		@Resource(name="processInstanceDao")
		ProcessInstanceDao processInstanceDao;
		@Resource(name="attendFileService")
		AttendFileService attendFileService;
		
		@Resource(name="stationDaoImpl")
		private IStationDao stationDao;
		
		
		@Resource(name="dutyUserServiceImpl")
		DutyUserService dutyUserService;
		
		@Resource(name="peopleAuthorityService")
		PeopleAuthorityInfoService peopleAuthorityInfoService;
		
		@Resource(name="roleServiceImpl")
		IRoleService roleService;
		
		static String offPost="离职";
	    static String  morning="上午";
	    static String afternoon="下午";
	    static String wholeDay="整天";
	    
		
		
		
		public Timestamp getSysTimestamp()
		  {
		    return DateUtil.getSysTimestamp();
		  }





	    
	    //同步部门数据
	@Override
	public void synSqlServerOrgDepartmentData() {
		LOG.info("同步部门数据开始.............."+new Date());
		//获取远程数据
		 List<SqlServerOrgDepartmentData>  list=orgDataService.getSqlServerOrgDepartmentData();
		 List<String> adminList=attendFileService.selectAdminDM();
		 for (SqlServerOrgDepartmentData data:list) {
			   //查询某一个部门在本地是否存在
			  Department depart=departmentService.getDepartmentDet(data.getId());
			  if (depart==null) {//如果不存在，则插入一条新的
				  Department dept = new Department();
				    if ("00".equals(data.getId())) {//如果是分行，则将其挂到我们的根节点下
						dept.setSjbmDm("1");
								}else{
						dept.setSjbmDm(data.getUpperid());
								}
				    dept.setLrSj(getSysTimestamp());
				    dept.setLrryDm(adminList.get(0));
				    dept.setBmNo(data.getId());
				    dept.setBmDm(data.getId());
				    dept.setBmMc(data.getName());
				    dept.setBmMcJ(data.getName());
			        //添加部门，自动添加该部门的默认岗位
			        Station station=new Station();
			        station.setBmDm(dept.getBmDm());
			        station.setGwDm(UUID.randomUUID().toString());
			        station.setGwMc("员工");
			        station.setLrSj(getSysTimestamp());
			        station.setDefaultGw("0"); //默认岗位  0是，1否 
			        departmentService.addDepartment(dept,adminList.get(0));
			        stationService.addStation(station,adminList.get(0));	
			        //添加分层角色 关系
				    roleService.addDeptGrant("0", new String[] {dept.getBmDm()});

			}else{  //如果存在，则修改
				if ("00".equals(data.getId())) {//如果是分行，则将其挂到我们的根节点下
					depart.setSjbmDm("1");
							}else{
								depart.setSjbmDm(data.getUpperid());
				}
				depart.setLrSj(getSysTimestamp());
				depart.setLrryDm(adminList.get(0));
				depart.setBmNo(data.getId());
				depart.setBmDm(data.getId());
				depart.setBmMc(data.getName());
				depart.setBmMcJ(data.getName());
				   //添加分层角色 关系
				 roleService.addDeptGrant("0", new String[] {depart.getBmDm()});
				departmentService.modifyDepartment(depart,adminList.get(0));
			}
			 
			 
			   	
		}
			LOG.info("同步部门数据结束 .............."+new Date());
		
	}
	
	
	


//同步用户数据
	@Override
	public void synSqlServerOrgUserAccountData() {
		LOG.info("同步员工数据开始.............."+new Date());
		//获取本地数据库的所有部门
		 List<Department>  deptList=departmentService.getAllDepart();	
		 List<String> adminList=attendFileService.selectAdminDM();
		 //迭代每个部门，按部门进行员工数据同步,以避免数据量过大
		for ( Department dept:deptList) {
			//获取远程下该部门下的员工列表
			   List<SqlServerOrgUserAccountData> list=orgDataService.getSqlServerOrgUserAccountData(dept.getBmDm());
			   //迭代员工列表，逐条与本地员工列表进行对比，如果有则修改属性，如果没有 则插入到本地
			   for (SqlServerOrgUserAccountData data:list) {
				      Employee employee=employeeService.getEmployeeDetail(data.getWorkno());
				      //查看本地数据库某部门下是否有和员工岗位相匹配的岗位名
					  List<Station> stationList=stationService.getGwByDeptNumAndName(data.getPostname(),data.getSubdept());
					  if (stationList.size()==0) {
						//如果没有相匹配的岗位名，则还要先加岗位到本地库
				        Station station=new Station();
				        station.setBmDm(dept.getBmDm());
				        String uuid=UUID.randomUUID().toString();
				        station.setGwDm(uuid);
				        station.setGwMc(data.getPostname());
				        station.setLrSj(getSysTimestamp());
				        station.setDefaultGw("1"); //默认岗位  0是，1否 
				        stationService.addStation(station,adminList.get(0));		
					   }
					  stationList=stationService.getGwByDeptNumAndName(data.getPostname(),data.getSubdept() );
                      if (null==employee) { 
						 Employee newEmployee=new Employee();
						 newEmployee.setYhNo(data.getWorkno());
						 newEmployee.setYhDm(data.getWorkno());
						 newEmployee.setYhMc(data.getName());
						 newEmployee.setBmDm(dept.getBmDm()); 
						 if (offPost.equals(data.getPayoffflag())) {
							newEmployee.setStaff_hire_state("1");
						}else{
							newEmployee.setStaff_hire_state("0");
						}
						 newEmployee.setGwDm(stationList.get(0).getGwDm());
	 
						 employeeService.addEmployee(newEmployee);
						 //如果新增员工，则继承部门排班
						 dutyUserService.getDutyFromDept(newEmployee.getBmDm(), newEmployee.getYhDm());
	                     
					}else{
						String oldDept=employee.getBmDm();
						//如果本地已经存在了该员工，这里就只更新其属性。
						employee.setYhNo(data.getWorkno());
						employee.setYhDm(data.getWorkno());
						employee.setYhMc(data.getName());
						employee.setBmDm(dept.getBmDm()); 
						 if (offPost.equals(data.getPayoffflag())) {
							 employee.setStaff_hire_state("1");
						}else{
							employee.setStaff_hire_state("0");
						}
						 employee.setGwDm(stationList.get(0).getGwDm());
				          User loginUser=new User();
				          loginUser.setYhDm(adminList.get(0));
						 employeeService.modifyEmployee(employee, loginUser);
						      //如果员工部门变了需要重新
							 if (!dept.getBmDm().equals(oldDept)) { 
								 try {
									peopleAuthorityInfoService.chgDutyOnDeptChg(dept.getBmDm(), oldDept, employee.getYhDm(), DateUtil.getDateString(new Date()));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}												
					}	
                      
                      
                      
                      
			}
	          
			
			
		}
		LOG.info("同步员工数据结束.............."+new Date());
		
		
		


	}

	 //同步 请假数据将数据从远程库更新到本地库
		@Override
		public void synSqlServerCheckData(String startDate, String endDate) {
			LOG.info("同步请假数据开始.............."+new Date());
			 List<SqlServerCheckData>  remoteList=orgDataService.getSqlServerCheckData(startDate, endDate);
			 for (int i = 0; i < remoteList.size(); i++) {
				 attendFileService.deleteAttendFile(remoteList.get(i).getId());
			    attendFileService.insertAttendFile(remoteList.get(i));
			}
			 LOG.info("同步请假数据结束 .............."+new Date());
		}

	   //同步外出数据 参数日期格式 2012-10-11
		@Override
		public void synSqlServerOutData(String startDate, String endDate) {
			LOG.info("同步外出数据开始.............."+new Date());
			List<SqlServerOutData> remoteList= orgDataService.getSqlServerOutData(startDate, endDate);
			for (int i = 0; i <remoteList.size(); i++) {
				SqlServerOutData sqlServerOutData=remoteList.get(i);
				attendFileService.deleteAttendFile2(sqlServerOutData.getId());
				attendFileService.insertAttendFile2(sqlServerOutData);
			}
			LOG.info("同步外出数据结束.............."+new Date());
		}
		
		

		//同步打卡数据格式2012-10-11
			@Override
			public void synSqlServerBrushCardLog(String startDate, String endDate) {
				attendFileService.deleteDataRecordByTime(startDate, endDate);
			   List<Employee>  employeeList=	 employeeService.getAllEmployee(null);
			   for (Employee employee:employeeList) {
				   LOG.info("同步员工："+employee.getYhMc()+"的打卡数据开始.............."+new Date());
				   List<SqlServerBrushCardLog> list=checkDataService.getBrushCardLog(startDate, endDate,employee.getYhDm());
				    for (int i = 0; i < list.size(); i++) {
				    	SqlServerBrushCardLog log=list.get(i);
						AttendData data=new AttendData();
						data.setChecktime(log.getDtTime());
						data.setCreate_time(DateUtil.getDateString(new Date()));
						data.setDevice_id(log.getnDevice());
						//data.setDevice_name(log.getStrDeviceName());
						data.setDoor_name(log.getStrDeviceName());
						data.setStaff_num(log.getStrEmployeeID());
						data.setStaff_name(log.getStrPersonName());
						data.setFlag_status("0");
						attendFileService.insertAttendDataRecord(data);
					}
			}
			    
				
			}

	    //将本地的attendfile2中的有效数据抽取出来存到我们外出记录表kc_attend_outwork表中
			@Override
		public void shuffleSqlServerOutData(String startDate,String endDate,String employeeId){
			LOG.info("整理employeeId"+employeeId+"的外出数据开始.........."+new Date());
			  List<SqlServerOutData> outList= attendFileService.selectAttendfile2(startDate, endDate,employeeId);
			  for (int i = 0; i <outList.size(); i++) {
				    SqlServerOutData ssod=outList.get(i);
					  attendFileService.deleteOutworkByInstanceId(ssod.getId());

				   OutWorkModel outWorkModel=new OutWorkModel();
			       outWorkModel.setStaffNum(ssod.getWorkno());
			       outWorkModel.setProcessInstanceId(ssod.getId());
			       outWorkModel.setReason(ssod.getKindname());
			       outWorkModel.setFromTime(DateUtil.getDateString(getSimpleDate(ssod.getBegindate().trim()+" "+ssod.getBeginhour().trim())));
			       outWorkModel.setToTime(DateUtil.getDateString(getSimpleDate(ssod.getEnddate().trim()+" "+ssod.getEndhour().trim())));//2016-07-08 15:03:00
			       processInstanceDao.outApplication(outWorkModel);	
			}
			  LOG.info("整理employeeId"+employeeId+"的外出数据结束.........."+new Date());
			  
		}
		

	
	
	//从本地库中的attendfile表中抽取出有效数据存在我们的kc_attend_leavetime表中
	//2016-10-11格式
			@Override
	public void shuffleSqlServerCheckData(String startDate,String endDate,String employeeId){
		LOG.info("整理employeeId"+employeeId+"的请假数据开始.........."+new Date());
		//先删除区间的请假 信息
		     //获取本地的请假信息
		     List<SqlServerCheckData>  list=attendFileService.selectAttendfile(startDate,endDate,employeeId);
		     List<String> adminList=attendFileService.selectAdminDM();
		     for (int i = 0; i < list.size(); i++) {
				SqlServerCheckData sqlServerCheckData=list.get(i);				

			    attendFileService.deleteLeaveTimeByInstanceId(sqlServerCheckData.getId());

				
				
				
				
				
					//将时间段拆成一天一天的
					  List<Date> dateList=getDateList(DateUtil.getDate(sqlServerCheckData.getBegindate()), DateUtil.getDate(sqlServerCheckData.getEnddate()));
					  for (int j = 0; j < dateList.size(); j++) {
						  Date date=dateList.get(j);
						  Map<String, String> attendanceDutyUserMap=new HashMap<String, String>();
						  attendanceDutyUserMap.put("staffNum", sqlServerCheckData.getWorkno());
						  attendanceDutyUserMap.put("startTime", DateUtil.getDateString(date));
						  attendanceDutyUserMap.put("endTime",DateUtil.getDateTimeString(date)+" 23:59:59" );
						  //查找某一天的排班
						  List<AttendanceDutyUser >  attendanceDutyUserList= statisticCalService.getAttendanceDutyUserListByStaffAndTime(attendanceDutyUserMap);
						  //如果有排班,注意每天最多一条排班记录
						  if (attendanceDutyUserList.size()>0) {
							  //attendanceDutyUserList最多只有一条记录
							  AttendanceDutyUser adu=attendanceDutyUserList.get(0);
							  Map<String,Integer> attendanceDutyMap=new HashMap<String, Integer>();
							  attendanceDutyMap.put("id", adu.getDuty_id());
							  //获取班次信息
							  AttendanceDuty attendanceDuty=statisticCalService.getAttendanceDutyById(attendanceDutyMap);  
							      
							  //获取本地数据库请类型字典值, 
							 List<Books>  bookList= booksServiceImpl.getWorkBookByNameAndType(sqlServerCheckData.getKindname(), "2");
							 //如果没有该值,则添加
							 if (bookList.size()==0) {
								 Books book=new Books();
								 book.setCreate_user(adminList.get(0));
								 book.setWordbook_type("2");
								 book.setWordbook_name(sqlServerCheckData.getKindname());
								 book.setDescription("");
								 book.setWordbook_value(UUID.randomUUID().toString());
								 booksServiceImpl.addNewBooks(book);
							}
							 
							 bookList=booksServiceImpl.getWorkBookByNameAndType(sqlServerCheckData.getKindname(), "2");
							
							   
							   
							   LeaveModel leaveModel=new LeaveModel();
					           leaveModel.setStaffNum(sqlServerCheckData.getWorkno());
					           leaveModel.setProcessInstanceId(sqlServerCheckData.getId());
					           //根据某一天的请假是上午，下午，还是全天，转化成具体的时间点
					           if (1==attendanceDuty.getIsElastic()) {
					        	   leaveModel.setFromTime(DateUtil.getDateString(getDutyTime(attendanceDuty, 1, DateUtil.getDateTimeString(date))));
							        leaveModel.setToTime(DateUtil.getDateString(getDutyTime(attendanceDuty, 4, DateUtil.getDateTimeString(date)))); 	
							}else{
								if (morning.equals(sqlServerCheckData.getDaykindname())) {
						        	   leaveModel.setFromTime(DateUtil.getDateString(getDutyTime(attendanceDuty, 1, DateUtil.getDateTimeString(date))));
							           leaveModel.setToTime(DateUtil.getDateString(getDutyTime(attendanceDuty, 2, DateUtil.getDateTimeString(date))));
								}else if (afternoon.equals(sqlServerCheckData.getDaykindname())) {
									   leaveModel.setFromTime(DateUtil.getDateString(getDutyTime(attendanceDuty, 3, DateUtil.getDateTimeString(date))));
							           leaveModel.setToTime(DateUtil.getDateString(getDutyTime(attendanceDuty, 4, DateUtil.getDateTimeString(date))));
								}else{
									leaveModel.setFromTime(DateUtil.getDateString(getDutyTime(attendanceDuty, 1, DateUtil.getDateTimeString(date))));
							        leaveModel.setToTime(DateUtil.getDateString(getDutyTime(attendanceDuty, 4, DateUtil.getDateTimeString(date)))); 								
								}
							}
					       
					           leaveModel.setReason("");
					           leaveModel.setLeaveCategory(bookList.get(0).getWordbook_value());
					           processInstanceDao.insertLeaveRecord(leaveModel);
	 
						}
						  
						  
					}
					
					  
			}
		
		     LOG.info("整理employeeId"+employeeId+"的请假数据结束.........."+new Date());
	}
	
	//获取日期  yyyy-MM-dd HH:mm
			private static Date getSimpleDate(String dateString){
				try {
			        return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString);

				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			
	          public static String getDateStringYMDHM(Date date){
	        	 SimpleDateFormat sdf=new  SimpleDateFormat("yyyy-MM-dd HH:mm");
	        	 return sdf.format(date);	        	  
	          }
	
	public List<AttendanceDutyUser>   splitAttendanceDutyUserList(List<AttendanceDutyUser> list,Date startDate,Date endDate){
		List<AttendanceDutyUser > newList=new ArrayList<AttendanceDutyUser>();
		for (int i = 0; i < list.size(); i++) {
			AttendanceDutyUser newAdu=new AttendanceDutyUser();
			AttendanceDutyUser adu=list.get(i);
			 Date aduStart=getSimpleDate(adu.getBegin_time());
			 Date aduEnd=getSimpleDate(adu.getEnd_time());	
			 
			 if (aduStart.after(startDate)) {
				newAdu.setBegin_time(getDateStringYMDHM(aduStart));
			}else{
				newAdu.setBegin_time(getDateStringYMDHM(startDate));
			}
			 
			 if (aduEnd.before(endDate)) {
				newAdu.setEnd_time(getDateStringYMDHM(aduEnd));
			}else{
				newAdu.setEnd_time(getDateStringYMDHM(endDate));
			}
			 
			 newList.add(newAdu);
			 
			 
		}
		return newList;
		
	}
	
	
	
	//将两个日期之间的值折成一天天的
	static	List<Date>   getDateList(Date startDate,Date endDate){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(startDate);
		List<Date> list=new ArrayList<Date>();
		
	    while (!calendar.getTime().after(endDate)) {
		    Date date=new Date(calendar.getTimeInMillis());
		    list.add(date);
		    calendar.setTimeInMillis(calendar.getTimeInMillis()+1000*3600*24);   
		}
	return list;	
	}
	
	
	public static void main(String[] args) {
		List<Date> list=getDateList(DateUtil.getDate("2016-10-11"), DateUtil.getDate("2016-10-11"));
		for (int i = 0; i < list.size(); i++) {
			LOG.info(list.get(i));
		}
	}








	public String getHHmm(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
		return sdf.format(date);
	}
	
	
	
	
	
	
	//获取上班班时间点 point 为 1,2,3,4表示上午上班，中午下班，下午上班，下午下班
	public  Date getDutyTime(AttendanceDuty attendanceDuty,Integer point,String dateStr){
		 if (attendanceDuty.getIsElastic()==0) {
	           Date now=getSimpleDate(dateStr+" "+attendanceDuty.getDutyTime1());

			 
			  //开始和结束统计时间点
			    Date statisticStartCheckPoint=StatisticServiceImpl.getDutyStartCheckPoint(now,attendanceDuty);
			    
			    //当日班次的上下班时间点
			    Date statisticDutyTime1=StatisticServiceImpl.getDutyTime1(now, attendanceDuty);
			    Date statisticDutyTime2=StatisticServiceImpl.getDutyTime2(now, attendanceDuty);
			    Date statisticDutyTime3=StatisticServiceImpl.getDutyTime3(now, attendanceDuty);
			    Date statisticDutyTime4=StatisticServiceImpl.getDutyTime4(now, attendanceDuty);

			    //考勤时间点
			    //Date statisticDutyTime1=StatisticServiceImpl.getDutyTime1(now,attendanceDuty);

			    //如果前面的时间大于后面的时间则前面的时间要减一天
			    if(statisticStartCheckPoint.after(statisticDutyTime1)){
			    	statisticStartCheckPoint=StatisticServiceImpl.getPreviousDayTime(statisticStartCheckPoint);
			    }
			    
			    //如果前面的时间大于后面的时间则前面的时间要减一天
			    if(statisticDutyTime1.after(statisticDutyTime1)){
			    	statisticDutyTime1=StatisticServiceImpl.getPreviousDayTime(statisticDutyTime1);
			    } 
			    
			  //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
			    if(statisticDutyTime2.before(statisticDutyTime1)){
			    	statisticDutyTime2=StatisticServiceImpl.getNextDayTime(statisticDutyTime2);
			    }
			    
			    //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
			    if(statisticDutyTime3.before(statisticDutyTime1)){
			    	statisticDutyTime3=StatisticServiceImpl.getNextDayTime(statisticDutyTime3);
			    }
			    //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
			    if(statisticDutyTime4.before(statisticDutyTime1)){
			    	statisticDutyTime4=StatisticServiceImpl.getNextDayTime(statisticDutyTime4);
			    }

             if (point==1) {
				return statisticDutyTime1;
			}else if(point==2){
				return statisticDutyTime2;
			}else if(point==3){
				return statisticDutyTime3;
			}else if(point==4){
				return statisticDutyTime4;
			}else{
				return null;
			}
			  
		}else{
			Date now=getSimpleDate(dateStr+" "+attendanceDuty.getDutyStartCheckPoint());
			//弹性工作制开始和结束统计时间点
		    Date statisticStartCheckPoint=StatisticServiceImpl.getDutyStartCheckPoint(now,attendanceDuty);
		
		    //弹性工作制默认上下班的时间点
		    Date statisticElasticDefaultDutyTime1=StatisticServiceImpl.getElasticDefaultDutyTime1(now, attendanceDuty);
		    Date statisticElasticDefaultDutyTime2=StatisticServiceImpl.getElasticDefaultDutyTime2(now, attendanceDuty);
		    
		
		 
		    
		  //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
		    if(statisticElasticDefaultDutyTime1.before(statisticStartCheckPoint)){
		    	statisticElasticDefaultDutyTime1=StatisticServiceImpl.getNextDayTime(statisticElasticDefaultDutyTime1);
		    }
		    
		    //如果后面的时候小于前面的时间，说明跨天了,后面的时间要加一天
		    if(statisticElasticDefaultDutyTime2.before(statisticStartCheckPoint)){
		    	statisticElasticDefaultDutyTime2=StatisticServiceImpl.getNextDayTime(statisticElasticDefaultDutyTime2);
		    }
			
			if(point==1) {
				return statisticElasticDefaultDutyTime1;
			}else if(point==4) {
				return statisticElasticDefaultDutyTime2;
			}else{
				return null;
			}
			
		}
	}





  
	
	
	
	
	



























}
