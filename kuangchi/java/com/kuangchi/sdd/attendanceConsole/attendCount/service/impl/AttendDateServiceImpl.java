package com.kuangchi.sdd.attendanceConsole.attendCount.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.attendCount.dao.AttendDateDao;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendDateModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendDetailInfoModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.DutyInfoModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.LeaveTimeModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.OutWorkModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.service.AttendDateService;
import com.kuangchi.sdd.attendanceConsole.attendRecord.dao.IAttendRecordDao;
import com.kuangchi.sdd.attendanceConsole.attendRecord.model.AttendRecordModel;
import com.kuangchi.sdd.base.model.easyui.Grid;

@Service("attendDateService")
public class AttendDateServiceImpl implements AttendDateService {
	
	@Resource(name="attendDateDao")	
	private AttendDateDao attendDateDao;

	@Resource(name="attendRecordDao")	
	private IAttendRecordDao attendRecordDao;

	@Override
	public Grid<AttendDateModel> getAllAttendDate(Map<String, Object> map) {
		
		
		List<AttendDateModel> attendDateModels = attendDateDao.getAllAttendDate1(map);
		
		for (AttendDateModel attendDateModel : attendDateModels) {
			/*
			// 封装请假时段和因公外出时间段
			Map<String, Object> leaveParam = new HashMap<String, Object>();
			leaveParam.put("staff_no", attendDateModel.getStaffNo());
			leaveParam.put("everyDate", attendDateModel.getEveryDate());
			List<Map> leaveResultList = attendDateDao.getLeaveInfo(leaveParam);
			for (int i = 0; i <leaveResultList.size(); i++) {
				Map leaveResult=leaveResultList.get(i);
				attendDateModel.setLeaveType( (attendDateModel.getLeaveType()==null?",":attendDateModel.getLeaveType()+" ,")+(String)leaveResult.get("leaveType")+"  "+(String)leaveResult.get("leaveTimePeriod"));
				//attendDateModel.setLeaveTimePeriod((String)leaveResult.get("leaveTimePeriod"));
			}
			List<Map> outResultList = attendDateDao.getOutInfo(leaveParam);
			for (int i = 0; i < outResultList.size(); i++) {
				Map outResult=outResultList.get(i);
				attendDateModel.setOutTimePeriod((attendDateModel.getOutTimePeriod()==null?",":attendDateModel.getOutTimePeriod()+" ,")+(String)outResult.get("beginhour") + "-" + (String)outResult.get("endhour")+" ");
			}*/
			
			attendDateModel.setWorkTime(attendDateModel.getWorkTime() + attendDateModel.getOverTime());
		}
		//设置每条数据考勤异常情况
		/*for (AttendDateModel attendDateModel : attendDateModels) {
				if(attendDateModel.getAttendType()!=null){
					attendDateModel.setAttendType("0");
				}else{
					attendDateModel.setAttendType("1");
				}
		}*/
		
		Integer total=attendDateDao.countAllAttendDate1(map);
		Grid<AttendDateModel> grid=new Grid<AttendDateModel>();
		grid.setRows(attendDateModels);
		grid.setTotal(total);
		return grid;
	}

	@Override
	public List<AttendDateModel> exportAllToExcel(Map<String,Object> map) {
		return attendDateDao.exportAllDateToExcel(map);
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
	
	
	@Override
	public List<AttendDetailInfoModel> getDetailByStaffNumAndDutyId(
			String staffNum, String dutyId, String everyDate) {
		List<AttendDetailInfoModel> list=new ArrayList<AttendDetailInfoModel>();
		if(null!=dutyId){
			//拿到基本信息
			AttendDateModel attendDateModel=attendDateDao.getAttendDateByStaffNumAndDutyId(staffNum, dutyId, everyDate);
		
			//拿到班次详情
			DutyInfoModel checkPointModel=attendDateDao.getDutyModel(dutyId);
			
			//封装详细的班次 信息  “当天班次”
			
				//先判断跨天否
			SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
			String tempDate1=everyDate;
			String tempDate2=everyDate;
			String tempDate3=everyDate;
			
			String eTempDate1=everyDate;
			String eTempDate2=everyDate;
			String mTempDate1=everyDate;
			String mTempDate2=everyDate;
			
			String dutyDetailInfo="";
			
			String startDateTime="";
			String endDateTime="";
			
				try {
					if(checkPointModel.getDutyTime2()!=null&&checkPointModel.getDutyTime3()!=null){
						if(sdf.parse(checkPointModel.getDutyTime1()).getTime()>sdf.parse(checkPointModel.getDutyTime2()).getTime()){
							tempDate1=addOneDayDate(everyDate);
						}
						if(sdf.parse(checkPointModel.getDutyTime1()).getTime()>sdf.parse(checkPointModel.getDutyTime3()).getTime()){
							tempDate2=addOneDayDate(everyDate);
						}
					}
					if(checkPointModel.getDutyTime1()!=null&&checkPointModel.getDutyTime4()!=null){
						if(sdf.parse(checkPointModel.getDutyTime1()).getTime()>sdf.parse(checkPointModel.getDutyTime4()).getTime()){
							tempDate3=addOneDayDate(everyDate);
						}
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			
				
				if(checkPointModel.geteDutyTime1()!=null&&checkPointModel.geteDutyTime2()!=null){
					try {
						if(sdf.parse(checkPointModel.geteDutyTime1()).getTime()>sdf.parse(checkPointModel.geteDutyTime2()).getTime()){
							eTempDate2=addOneDayDate(everyDate);
						}
						if(sdf.parse(checkPointModel.geteDutyTime1()).getTime()>sdf.parse(checkPointModel.getMustEDutyTime1()).getTime()){
							mTempDate1=addOneDayDate(everyDate);
						}
						if(sdf.parse(checkPointModel.geteDutyTime1()).getTime()>sdf.parse(checkPointModel.getMustEDutyTime2()).getTime()){
							mTempDate2=addOneDayDate(everyDate);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			if("0".equals(checkPointModel.getIsElastic())){//非弹性班次
				if(checkPointModel.getDutyTime2()!=null){
					 dutyDetailInfo="上班: "+everyDate+" "+checkPointModel.getDutyTime1()+"; 下班:　"+tempDate1+" "+checkPointModel.getDutyTime2()+";"+"</br>"+"上班: "+tempDate2+" "+checkPointModel.getDutyTime3()+"; 下班:　"+tempDate3+" "+checkPointModel.getDutyTime4()+";";
					
				}else{
					 dutyDetailInfo=everyDate+" "+checkPointModel.getDutyTime1()+" 上班; "+tempDate3+" "+checkPointModel.getDutyTime4()+" 下班;";
				}
				
				startDateTime=everyDate+" "+checkPointModel.getDutyTime1();
				endDateTime=tempDate3+" "+checkPointModel.getDutyTime4();
				
			}else{//弹性班次
				//dutyDetailInfo=eTempDate1+" "+checkPointModel.geteDutyTime1()+" 默认上班; "+eTempDate2+" "+checkPointModel.geteDutyTime2()+" 默认下班;";
				dutyDetailInfo="默认上班: "+eTempDate1+" "+checkPointModel.geteDutyTime1()+"; 默认下班: "+eTempDate2+" "+checkPointModel.geteDutyTime2()+";</br>"+"需在岗起: "+mTempDate1+" "+checkPointModel.getMustEDutyTime1()+"; 需在岗止: "+mTempDate2+" "+checkPointModel.getMustEDutyTime2()+";";
				startDateTime=eTempDate1+" "+checkPointModel.geteDutyTime1();
				endDateTime=eTempDate2+" "+checkPointModel.geteDutyTime2();
			}
			
			
			//判断中途是否考勤
			Map m=attendDateDao.askIfMidCheck(dutyId);
			Boolean bl=true;
			for(Object o:m.values()){
				if(o==null){
					bl=false;
					break;
				}
			}
			//下面准备拿到   请假列表信息 和外出工作列表信息 
			List<LeaveTimeModel> leaveTimeModelList=attendDateDao.getLeaveDetailList(staffNum, startDateTime, endDateTime);
			List<OutWorkModel> outWorkModelList=attendDateDao.getOutDetailList(staffNum, startDateTime, endDateTime);
			
			if("1".equals(attendDateModel.getIsHoliday())||"1".equals(attendDateModel.getIsVocation())){
				attendDateModel.setDutyName(attendDateModel.getDutyName()+"(放假)");
			}
			
			
			//页面今日班次 去掉具体的默认上班和默认下班信息，只显示班次，外加按钮查看具体班次页面信息  update by chudan.guo
			//dutyDetailInfo="<center>"+attendDateModel.getDutyName()+"</center></br><center>"+dutyDetailInfo+"</center>";
			dutyDetailInfo=attendDateModel.getDutyName();
			
			//下面开始封装 返回去的list
			if(leaveTimeModelList.size()>0){
				for(int i=0;i<leaveTimeModelList.size();i++){
					AttendDetailInfoModel model=new AttendDetailInfoModel();
					model.setDutyName(dutyDetailInfo);
					buildModel(model,attendDateModel);
					String leaveDetail=leaveTimeModelList.get(i).getLeaveSTime()+" 至 "+leaveTimeModelList.get(i).getLeaveETime()+" "+leaveTimeModelList.get(i).getLeaveCatetory();
					model.setIsMidCheck(bl);
					
					model.setLeaveDetail(leaveDetail);
					model.setLeaveCatetoryName(leaveTimeModelList.get(i).getLeaveCatetory());
					model.setLeaveReason(leaveTimeModelList.get(i).getReason());
					list.add(model);
				}
			}else{
				AttendDetailInfoModel model=new AttendDetailInfoModel();
				model.setDutyName(dutyDetailInfo);
				model.setIsMidCheck(bl);
				buildModel(model,attendDateModel);
				list.add(model);
			}
			
			
			if(outWorkModelList.size()>0){
				for(int i=0;i<outWorkModelList.size();i++){
					AttendDetailInfoModel model=new AttendDetailInfoModel();
					model.setDutyName(dutyDetailInfo);
					buildModel(model,attendDateModel);
					model.setIsMidCheck(bl);
					String outDetail=outWorkModelList.get(i).getOutSTime()+" 至 "+outWorkModelList.get(i).getOutETime();
					model.setOutDetail(outDetail);
					list.add(model);
				}
			}else{
				AttendDetailInfoModel model=new AttendDetailInfoModel();
				model.setDutyName(dutyDetailInfo);
				model.setIsMidCheck(bl);
				buildModel(model,attendDateModel);
				list.add(model);
			}
		}
		return list;
	}  
	
	
	private static void buildModel(AttendDetailInfoModel model,AttendDateModel attendDateModel){
		model.setStaffNo(attendDateModel.getStaffNo());
		model.setId(attendDateModel.getId());
		//model.setDutyName(attendDateModel.getDutyName());
		model.setStaffNum(attendDateModel.getStaffNum());
		model.setStaffName(attendDateModel.getStaffName());
		model.setDutyId(attendDateModel.getDutyId());
		model.setSex(attendDateModel.getSex());
		model.setEveryDate(attendDateModel.getEveryDate());
		model.setDeptName(attendDateModel.getDeptName());
		model.setMorningworkBeginTime(attendDateModel.getMorningworkBeginTime());
		model.setMorningworkEndTime(attendDateModel.getMorningworkEndTime());
		model.setAfternoonworkBeginTime(attendDateModel.getAfternoonworkBeginTime());
		model.setAfternoonworkEndTime(attendDateModel.getAfternoonworkEndTime());
		model.setLeaveTotalTime(attendDateModel.getLeaveTotalTime());
		model.setOutTotalTime(attendDateModel.getOutTotalTime());
		model.setInWork(Integer.parseInt(attendDateModel.getInWork()));
		model.setWorkTime(attendDateModel.getWorkTime());
		model.setRemark(attendDateModel.getRemark());
		model.setTodayWork(attendDateModel.getTodayWork());
		model.setLaterTime(attendDateModel.getLaterTime());
		model.setEarlyTime(attendDateModel.getEarlyTime());
		model.setKgTime(attendDateModel.getKgTime());
		model.setCardNot(attendDateModel.getCardNot());
		model.setCardStatus(attendDateModel.getCardStatus());
		model.setOverTime(attendDateModel.getOverTime());
		model.setIsHoliday(Integer.parseInt(attendDateModel.getIsHoliday()));
		model.setIsVocation(Integer.parseInt(attendDateModel.getIsVocation()));
		model.setIsExtralwork(Integer.parseInt(attendDateModel.getIsExtralwork()));
		model.setShouldWorkTime(attendDateModel.getShouldWorkTime());
		model.setShouldWorkTimeStr(formatTime(attendDateModel.getShouldWorkTime()));
		model.setKgCount(attendDateModel.getKgCount());
		model.setEarlyCount(attendDateModel.getEarlyCount());
		model.setLaterCount(attendDateModel.getLaterCount());
		model.setLaterTimeStr(attendDateModel.getLaterTimeStr());
		model.setEarlyTimeStr(attendDateModel.getEarlyTimeStr());
		model.setLeaveTotalTimeStr(formatTime(attendDateModel.getLeaveTotalTime()));
		model.setOutTotalTimeStr(formatTime(attendDateModel.getOutTotalTime()));
		model.setEarlyTimeStr(formatTime(attendDateModel.getEarlyTime()));
		model.setLaterTimeStr(formatTime(attendDateModel.getLaterTime()));
		model.setWorkTimeStr(formatTime(attendDateModel.getWorkTime()));
		model.setKgTimeStr(formatTime(attendDateModel.getKgTime()));
		model.setKgType(attendDateModel.getKgType());
		model.setOverTimeStr(formatTime(attendDateModel.getOverTime()));//加班时间
		
		model.setNoCheckSet(attendDateModel.getNoCheckSet()); // 免打卡设置
		
	}
	
	//7.91 小时 转换成 **天**时**分**秒 的工具方法
	public static String formatTime(Double dHour){
		double d=dHour/24;//算出天数
		int day=(int)d;
		double h=(d-day)*24;//算出小时数
		int hour=(int)h;
		double m=(h-hour)*60;
		int minute=(int)m;
		double s=(m-minute)*60;
		s=Math.round(s);
		int second=(int)s;
			if(second>59){
				second=0;
				if(minute<59){
					minute=minute+1;
				}else{
					minute=0;
					if(hour<24){
						hour=hour+1;
					}else{
						hour=0;
						if(day<366){
							day=day+1;
						}
					}
				}
			}
		String timeStr=+hour+"小时"+minute+"分钟"+second+"秒";
		return timeStr;
	}
	

}
