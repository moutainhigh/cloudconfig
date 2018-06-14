package com.kuangchi.sdd.attendanceConsole.attend.service.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.attendanceConsole.attend.dao.IAttendHandleDao;
import com.kuangchi.sdd.attendanceConsole.attend.model.AttendModel;
import com.kuangchi.sdd.attendanceConsole.attend.model.PunchCardModel;
import com.kuangchi.sdd.attendanceConsole.attend.service.IAttendHandleService;
import com.kuangchi.sdd.attendanceConsole.duty.dao.DutyDao;
import com.kuangchi.sdd.attendanceConsole.duty.model.Duty;
import com.kuangchi.sdd.attendanceConsole.statistic.dao.StatisticDao;
import com.kuangchi.sdd.attendanceConsole.statistic.service.StatisticService;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
@Transactional
@Service("attendHandleServiceImpl")
public class AttendHandleServiceImpl  implements IAttendHandleService {
	private static final int CARD_ID_LENGTH = 6;
	
	@Autowired
	private StatisticService statisticService;
	
	@Resource(name = "attendHandleDaoImpl")
	private IAttendHandleDao attendanceDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	@Resource(name="statisticDao")
	StatisticDao statisticDao;

	@Resource(name = "dutyDaoImpl")
	DutyDao dutyDao;
	//查询个人所有考勤信息
	public List<AttendModel> getMyAttend(AttendModel attendModel) {
		return attendanceDao.getMyAttend(attendModel);
	}
	
	//查询指定员工考勤信息
	public List<AttendModel> getAllAttend(AttendModel attendModel) {
		return attendanceDao.getAllAttend(attendModel);
	}
	
	
	private static String minusOneDayDate(String everyDate){
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
		Calendar c=Calendar.getInstance();
		try {
			c.setTime(sdf1.parse(everyDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.add(Calendar.DAY_OF_MONTH, -1);
	
		return sdf1.format(c.getTime());
	}
	private static String addOneDayDate(String everyDate){
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
		Calendar c=Calendar.getInstance();
		try {
			c.setTime(sdf1.parse(everyDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.add(Calendar.DAY_OF_MONTH, 1);
		
		return sdf1.format(c.getTime());
	}
	
	
	
	/**
	 * by gengji.yang
	 * 补卡之后 删除考勤异常记录
	 * @param staffNum
	 * @param duty 班次完整的信息
	 * @param checkDate  2016-05-20
	 * @param makeUpTimeStr  2016-05-20 12:00:00
	 * @param isElastic
	 */
	public void toDelExcepAfterMakeUp(String staffNum,Duty duty,String checkDate,String makeUpTimeStr,boolean isElastic){
		//2016-05-20 12:00:00  makeUpTimeStr 
		SimpleDateFormat sdf=new SimpleDateFormat("hh:mm");
		SimpleDateFormat sdd=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		
		//统计 起点和终点
		String startDate=checkDate;
		String endDate=checkDate;
		String startTime=duty.getDuty_start_check_point();
		String endTime=duty.getDuty_end_check_point();
		
		//统计 起止点 跨天处理
		try{
			if(sdf.parse(startTime).getTime()>sdf.parse(endTime).getTime()){
				endDate=addOneDayDate(checkDate);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//统计起止点详情
		String startTimeInfo=startDate+" "+startTime+":00";
		String endTimeInfo=endDate+" "+endTime+":00";
		//判断是否进入 当天的统计周期
		try {
			if(!(sdd.parse(makeUpTimeStr).getTime()>sdd.parse(startTimeInfo).getTime()&&sdd.parse(makeUpTimeStr).getTime()<sdd.parse(endTimeInfo).getTime())){//未进入， 则日期要减去一天，归到上一个班次
				checkDate=minusOneDayDate(checkDate);
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//坐班 日期前缀
		String tempDate1=checkDate;
		String tempDate2=checkDate;
		String tempDate3=checkDate;
		String tempDate4=checkDate;
		//弹性 日期前缀
		String eTempDate1=checkDate;
		String eTempDate2=checkDate;
		String mTempDate1=checkDate;
		String mTempDate2=checkDate;
		//坐班 时间点
		String dutyTime1=duty.getDuty_time1();
		String dutyTime2=duty.getDuty_time2();
		String dutyTime3=duty.getDuty_time3();
		String dutyTime4=duty.getDuty_time4();
		//弹性 时间点
		String eDutyTime1=duty.getElastic_default_duty_time1();
		String eDutyTime2=duty.getElastic_default_duty_time2();
		String eMDutyTime1=duty.getElastic_duty_time1();
		String eMDutyTime2=duty.getElastic_duty_time2();
		
		//坐班 跨天处理  
		try{
			if(dutyTime2!=null&&dutyTime3!=null){
				if(sdf.parse(dutyTime1).getTime()>sdf.parse(dutyTime2).getTime()){
					tempDate2=addOneDayDate(checkDate);
				}
				if(sdf.parse(dutyTime1).getTime()>sdf.parse(dutyTime3).getTime()){
					tempDate3=addOneDayDate(checkDate);
				}
			}
			if(dutyTime1!=null&&dutyTime4!=null){
				if(sdf.parse(dutyTime1).getTime()>sdf.parse(dutyTime4).getTime()){
					tempDate4=addOneDayDate(checkDate);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//弹性  跨天处理
		try{
			if(eDutyTime1!=null&&eDutyTime2!=null){
				if(sdf.parse(eDutyTime1).getTime()>sdf.parse(eDutyTime2).getTime()){
					eTempDate2=addOneDayDate(checkDate);
				}
				if(sdf.parse(eDutyTime1).getTime()>sdf.parse(eMDutyTime1).getTime()){
					mTempDate1=addOneDayDate(checkDate);
				}
				if(sdf.parse(eDutyTime1).getTime()>sdf.parse(eMDutyTime2).getTime()){
					mTempDate2=addOneDayDate(checkDate);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//坐班  2016-05-20 12:00:00
		String dutyInfo1=tempDate1+" "+dutyTime1+":00";
		String dutyInfo2=tempDate2+" "+dutyTime2+":00";
		String dutyInfo3=tempDate3+" "+dutyTime3+":00";
		String dutyInfo4=tempDate4+" "+dutyTime4+":00";
		
		//弹性班 2016-05-20 12:00:00
		String eDutyInfo1=eTempDate1+" "+eDutyTime1+":00";
		String eDutyInfo2=eTempDate2+" "+eDutyTime2+":00";
		String eMDutyInfo1=mTempDate1+" "+eMDutyTime1+":00";
		String eMDutyInfo2=mTempDate2+" "+eMDutyTime2+":00";
		
		try {
			Date makeUpTimeDate=sdd.parse(makeUpTimeStr);
			if(!isElastic){//坐班
				Integer result=0;
				if(makeUpTimeStr.equals(dutyInfo1)){
					attendanceDao.delNoElaExceRecord(staffNum,dutyInfo1, tempDate1+" "+duty.getDuty_time1_absent()+":00");
				}else if(makeUpTimeStr.equals(dutyInfo2)){
					attendanceDao.delNoElaExceRecord(staffNum, tempDate2+" "+duty.getDuty_time2_absent()+":00", dutyInfo2);
				}else if(makeUpTimeStr.equals(dutyInfo3)){
					attendanceDao.delNoElaExceRecord(staffNum, dutyInfo3, tempDate3+" "+duty.getDuty_time3_absent()+":00");
				}else{
					attendanceDao.delNoElaExceRecord(staffNum, tempDate4+" "+duty.getDuty_time4_absent()+":00", dutyInfo4);
				}
			}else{//弹性班
				Integer eResult=0;
				if(makeUpTimeDate.getTime()<=sdd.parse(eMDutyInfo1).getTime()){//A区域
					List<Map> list=attendanceDao.getUpElaExceptionList(staffNum, eMDutyInfo2);
					//[{timePoint=2016-05-09 20:00:00, exceptionType=3, dutyTimeType=1, everyDayTime=2016-05-09}, {timePoint=2016-05-10 05:00:00, exceptionType=3, dutyTimeType=4, everyDayTime=2016-05-09}, {timePoint=2016-05-10 20:00:00, exceptionType=3, dutyTimeType=1, everyDayTime=2016-05-10}, {timePoint=2016-05-11 05:00:00, exceptionType=3, dutyTimeType=4, everyDayTime=2016-05-10}, {timePoint=2016-05-11 20:00:00, exceptionType=3, dutyTimeType=1, everyDayTime=2016-05-11}, {timePoint=2016-05-12 05:00:00, exceptionType=3, dutyTimeType=4, everyDayTime=2016-05-11}, {timePoint=2016-05-12 02:57:17, exceptionType=1, dutyTimeType=1, everyDayTime=2016-05-11}, {timePoint=2016-05-12 02:11:08, exceptionType=1, dutyTimeType=1, everyDayTime=2016-05-11}, {timePoint=2016-05-13 05:00:00, exceptionType=3, dutyTimeType=4, everyDayTime=2016-05-12}, {timePoint=2016-05-13 20:00:00, exceptionType=3, dutyTimeType=1, everyDayTime=2016-05-13}, {timePoint=2016-05-14 05:00:00, exceptionType=3, dutyTimeType=4, everyDayTime=2016-05-13}, {timePoint=2016-05-14 20:00:00, exceptionType=3, dutyTimeType=1, everyDayTime=2016-05-14}, {timePoint=2016-05-15 05:00:00, exceptionType=3, dutyTimeType=4, everyDayTime=2016-05-14}, {timePoint=2016-05-15 20:00:00, exceptionType=3, dutyTimeType=1, everyDayTime=2016-05-15}, {timePoint=2016-05-16 05:00:00, exceptionType=3, dutyTimeType=4, everyDayTime=2016-05-15}, {timePoint=2016-05-16 00:27:00, exceptionType=1, dutyTimeType=1, everyDayTime=2016-05-15}, {timePoint=2016-05-14 04:00:00, exceptionType=2, dutyTimeType=4, everyDayTime=2016-05-13}, {timePoint=2016-05-17 04:00:00, exceptionType=2, dutyTimeType=4, everyDayTime=2016-05-16}, {timePoint=2016-05-02 20:00:00, exceptionType=3, dutyTimeType=1, everyDayTime=2016-05-02}, {timePoint=2016-05-03 05:00:00, exceptionType=3, dutyTimeType=4, everyDayTime=2016-05-02}, {timePoint=2016-05-03 20:00:00, exceptionType=3, dutyTimeType=1, everyDayTime=2016-05-03}, {timePoint=2016-05-04 05:00:00, exceptionType=3, dutyTimeType=4, everyDayTime=2016-05-03}, {timePoint=2016-05-04 20:00:00, exceptionType=3, dutyTimeType=1, everyDayTime=2016-05-04}, {timePoint=2016-05-05 05:00:00, exceptionType=3, dutyTimeType=4, everyDayTime=2016-05-04}, {timePoint=2016-05-05 20:00:00, exceptionType=3, dutyTimeType=1, everyDayTime=2016-05-05}, {timePoint=2016-05-06 05:00:00, exceptionType=3, dutyTimeType=4, everyDayTime=2016-05-05}, {timePoint=2016-05-06 20:00:00, exceptionType=3, dutyTimeType=1, everyDayTime=2016-05-06}, {timePoint=2016-05-07 05:00:00, exceptionType=3, dutyTimeType=4, everyDayTime=2016-05-06}, {timePoint=2016-05-20 04:00:00, exceptionType=2, dutyTimeType=4, everyDayTime=2016-05-19}, {timePoint=2016-05-21 05:00:00, exceptionType=3, dutyTimeType=4, everyDayTime=2016-05-20}, {timePoint=2016-05-21 04:00:00, exceptionType=2, dutyTimeType=4, everyDayTime=2016-05-20}]
					for(Map ele:list){//2016-05-04 11:00:00
						if(ele.get("exceptionType").equals("3")&&ele.get("everyDayTime").equals(eTempDate1)){//A域 且刚好是 上班缺勤
							attendanceDao.delElaExceRecord(staffNum,(String) ele.get("timePoint"));
						}
						if(ele.get("exceptionType").equals("1")&&ele.get("everyDayTime").equals(eTempDate1)){//A域 且刚好是 上班缺勤
							attendanceDao.delElaExceRecord(staffNum,(String) ele.get("timePoint"));
						}
					}
				}else if(makeUpTimeDate.getTime()>=sdd.parse(eMDutyInfo2).getTime()){//B区域
					List<Map> list1=attendanceDao.getDownElaExceptionList(staffNum, eMDutyInfo1);
					//[{timePoint=2016-05-21 04:00:00, exceptionType=2, dutyTimeType=4, everyDayTime=2016-05-20}, {timePoint=2016-05-21 05:00:00, exceptionType=3, dutyTimeType=4, everyDayTime=2016-05-20}]
					for(Map ele:list1){
						if(ele.get("exceptionType").equals("3")&&ele.get("everyDayTime").equals(eTempDate1)){
							attendanceDao.delElaExceRecord(staffNum,(String) ele.get("timePoint"));
						}
						if(ele.get("exceptionType").equals("2")&&ele.get("everyDayTime").equals(eTempDate1)){
							attendanceDao.delElaExceRecord(staffNum,(String) ele.get("timePoint"));
						}
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	//添加考勤信息
	public void addAttendInfo(AttendModel attendModel) throws SQLException, ParseException {
		
			attendanceDao.addAttendInfo(attendModel);
			Duty duty=getDutyInfo(attendModel);
			
			String staffNum=attendModel.getStaff_num();
			String isElastic=duty.getIs_elastic();
			String makeUpTimeStr=attendModel.getChecktime();//2016-05-20 12:00:00
			
			if("0".equals(isElastic)){//坐班
				toDelExcepAfterMakeUp(staffNum,duty,attendModel.getCheckdate(),makeUpTimeStr,false);
			}else{//弹性班
				toDelExcepAfterMakeUp(staffNum,duty,attendModel.getCheckdate(),makeUpTimeStr,true);
			}
			
			String str = attendModel.getCheckdate()+" "+ duty.getDuty_end_check_point()+":00";
			Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(str);
			statisticService.reStatisticByStaff(date,date,attendModel.getStaff_num());
			
	}

	//删除考勤信息
	public void deleteAttendInfoById(String ids) {
		attendanceDao.deleteAttendInfoById(ids);
	}
	//备份后删除月记录
	public void deleteAttendInfoByMonth(List<AttendModel> attendList) {
		attendanceDao.deleteAttendInfoByMonth(attendList);
	}
	//查询个人考勤信息总数
	public Integer getMyAttendCount(AttendModel attendModel) {
		return attendanceDao.getMyAttendCount(attendModel);
	}

	//查询指定员工考勤信息总数
	public Integer getAllAttendCount(AttendModel attendModel) {
		return attendanceDao.getAllAttendCount(attendModel);
	}

	//查询员工班次信息
	public Duty getDutyInfo(AttendModel attendModel) throws SQLException {
		return attendanceDao.getDutyInfo(attendModel);
	}

	@Override
	public void toDelExceptionAfterApply(String staffNum, String startTime,
			String endTime) {
		attendanceDao.delNoElaExceRecord(staffNum,startTime,endTime);
	}

	@Override
	public boolean removeAreaRecord(String partition_name, String loginUser) {
		try {
			attendanceDao.removeAreaRecord(partition_name);		
		} catch (Exception e) {
			 SQLException sqlException=getSqlException(e);
			 if (null!=sqlException&&sqlException.getErrorCode()==1507) {
				return true;
			}else{
				return false;
			}
		} 
		return true;
		
	}
	
	
	public SQLException getSqlException(Throwable exception){
		if (exception instanceof SQLException) {
			return (SQLException) exception;
		}
		return getSqlException(exception.getCause());
		
	}
	
	//班次树
	public Tree getClassesTree(){
		List<Tree> lists=new ArrayList<Tree>();
		List<Duty> list=dutyDao.getDutyClassesInfo();
		if(list.size()!=0){
			for (Duty duty2 : list) {
				Tree childTree=new Tree();
				childTree.setId(String.valueOf(duty2.getId()));
				childTree.setText(duty2.getDuty_name());
				childTree.setPid("0");
				childTree.setIconCls("icon-platform");
				lists.add(childTree);
			}	
		}
		
		Tree rootTree=new Tree();
		rootTree.setId("0");
		rootTree.setText("班次");
		rootTree.setState("open");
		rootTree.setIconCls("icon-platform");
		rootTree.setChildren(lists);
		return rootTree;
	}

	//查询班次到combobox
	public List<Duty> getClasses(){
		return dutyDao.getDutyClassesInfo();
	}
	
	@Override
	public Grid getDutyUserStaffById(PunchCardModel handle,String Page,String rows) {
		int count=attendanceDao.getDutyUserStaffByIdCount(handle);
		List<PunchCardModel> list=attendanceDao.getDutyUserStaffById(handle,Page,rows);
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(list);
		return grid;
	}

	@Override
	public List<PunchCardModel> getdutyId() {
		return attendanceDao.getdutyId();
	}

	@Override
	public List<PunchCardModel> getdutyTimeById(String id) {
		return attendanceDao.getdutyTimeById(id);
	}

	@Override
	public String getIsElasticById(String id) {
		return attendanceDao.getIsElasticById(id);
	}

	@Override
	public void addDutyAttendInfo(PunchCardModel handle) {
		attendanceDao.addDutyAttendInfo(handle);
		
	}

	@Override
	public PunchCardModel getCheckTimeById(String id) {
		return attendanceDao.getCheckTimeById(id);
	}
	
	//查询补打卡信息
	@Override
	public Grid<PunchCardModel> getPunchCardInfo(PunchCardModel handle,String Page,String row) {
		int count=attendanceDao.getPunchCardInfoCount(handle);
		List<PunchCardModel> list=attendanceDao.getPunchCardInfo(handle, Page, row);
		for (PunchCardModel punchCardModel : list) {
			punchCardModel.setChecktime(punchCardModel.getChecktime().substring(0, 19));
		}
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(list);
		return grid;
	}

	@Override
	public void PunchCardsAdd(List<PunchCardModel> punchList) {
		attendanceDao.PunchCardsAdd(punchList);
		
	}
	

	@Override
	public List<AttendModel> getAllExportAttend(AttendModel attendModel) {
		return attendanceDao.getAllExportAttend(attendModel);
	}

	@Override
	public Map getStaffByNO(String staff_no, String staff_name) {
		return attendanceDao.getStaffByNO(staff_no, staff_name);
	}
	
}
