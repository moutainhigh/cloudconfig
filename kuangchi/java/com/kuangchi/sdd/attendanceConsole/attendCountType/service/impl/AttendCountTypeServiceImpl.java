package com.kuangchi.sdd.attendanceConsole.attendCountType.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.attendCountType.dao.AttendCountTypeDao;
import com.kuangchi.sdd.attendanceConsole.attendCountType.model.AttendCountTypeModel;
import com.kuangchi.sdd.attendanceConsole.attendCountType.model.StaffAttendCount;
import com.kuangchi.sdd.attendanceConsole.attendCountType.service.AttendCountTypeService;
import com.kuangchi.sdd.base.model.easyui.Grid;

@Service("attendCountTypeServiceImpl")
public class AttendCountTypeServiceImpl implements AttendCountTypeService {
	
	@Resource(name="attendCountTypeDaoImpl")	
	private AttendCountTypeDao attendCountTypeDao;

	
	@Override
	public List<AttendCountTypeModel> getLeaveStatis(AttendCountTypeModel model) {

		List<AttendCountTypeModel> attendModel = attendCountTypeDao.getLeaveStatis(model);
		List<AttendCountTypeModel> attendList=new ArrayList<AttendCountTypeModel>();
		
		AttendCountTypeModel attendModel2=new AttendCountTypeModel();
		StringBuffer sb=new StringBuffer();
		int leaveTotalNumber=0;
		String leaveTypeName="";
		String totalNumber="";
		for(int i=0;i<attendModel.size();i++){
			if(i==0 ||attendModel.get(i).getStaff_num().equals(attendModel.get(i-1).getStaff_num())){
				attendModel2.setBm_dm((String)attendModel.get(i).getBm_dm());
				attendModel2.setBm_mc((String)attendModel.get(i).getBm_mc());
				attendModel2.setStaff_name((String)attendModel.get(i).getStaff_name());
				attendModel2.setStaff_no((String)attendModel.get(i).getStaff_no());
				attendModel2.setStaff_num((String)attendModel.get(i).getStaff_num());
				attendModel2.setException_type((String)attendModel.get(i).getException_type());//Exception_type是查出旷工总次数，用不到了
				leaveTypeName=(String)attendModel.get(i).getLeaveType();
				totalNumber=(String)attendModel.get(i).getTotalNumber();
				leaveTotalNumber +=Integer.valueOf(totalNumber);
				sb.append(leaveTypeName+totalNumber+"次，");
			} else {
				attendModel2.setLeaveTotalNumber(String.valueOf(leaveTotalNumber));
				attendModel2.setTotalNumber(sb.substring(0, sb.length()-1));
				attendList.add(attendModel2);
				sb.setLength(0);
				leaveTotalNumber=0;
				leaveTypeName="";
				totalNumber="";
				if(i<attendModel.size()){
					attendModel2=new AttendCountTypeModel();
					attendModel2.setBm_dm((String)attendModel.get(i).getBm_dm());
					attendModel2.setBm_mc((String)attendModel.get(i).getBm_mc());
					attendModel2.setStaff_name((String)attendModel.get(i).getStaff_name());
					attendModel2.setStaff_no((String)attendModel.get(i).getStaff_no());
					attendModel2.setStaff_num((String)attendModel.get(i).getStaff_num());
					attendModel2.setException_type((String)attendModel.get(i).getException_type());
					leaveTypeName=(String)attendModel.get(i).getLeaveType();
					totalNumber=(String)attendModel.get(i).getTotalNumber();
					leaveTotalNumber +=Integer.valueOf(totalNumber);
					sb.append(leaveTypeName+totalNumber+"次，");
				}
			}
		}
		if(sb.length()!=0){
			attendModel2.setLeaveTotalNumber(String.valueOf(leaveTotalNumber));
			attendModel2.setTotalNumber(sb.substring(0, sb.length()-1));
			attendList.add(attendModel2);
		}
		/*  当exception_type=3或4，time_interval>0就是旷工*/   //员工考情统计不需要查询旷工人员了，该方法启用
		/*List<AttendCountTypeModel> attendModel3 = attendCountTypeDao.getAbsenteeism(model);
		attendList.addAll(attendModel3);*/
		
		return attendList;
	}

	/**
	 * 查询单位考勤汇总
	 */
	@Override
	public List<AttendCountTypeModel> getAttendExceptionTotalTime(AttendCountTypeModel model) {
		List<AttendCountTypeModel> attendModel = attendCountTypeDao.getAttendExceptionTotalTime(model);
		List<AttendCountTypeModel> attendList=new ArrayList<AttendCountTypeModel>();
		
		for(int i=0;i<attendModel.size();i++){
			AttendCountTypeModel attendModel2=new AttendCountTypeModel();
			attendModel2.setBm_dm((String)attendModel.get(i).getBm_dm());
			attendModel2.setBm_mc((String)attendModel.get(i).getBm_mc());
			attendModel2.setSjbm_mc((String)attendModel.get(i).getSjbm_mc());
			attendModel2.setStaff_name((String)attendModel.get(i).getStaff_name());
			attendModel2.setStaff_no((String)attendModel.get(i).getStaff_no());
			attendModel2.setTotalNumber((String)attendModel.get(i).getTotalNumber());
			
			attendModel2.setException_type(attendModel.get(i).getExceptionTypeName());
			
			/*String typeName=(String)attendModel.get(i).getExceptionTypeName();
			if("迟到".equals(typeName)){
				attendModel2.setException_type("迟到");
			}else if("早退".equals(typeName)){
				attendModel2.setException_type("早退");
			}else if("旷工".equals(typeName)){
				attendModel2.setException_type("旷工");
			}else if("缺卡".equals(typeName)){
				attendModel2.setException_type("缺卡");
			}*/
		//	if(!"缺卡".equals(typeName)){
			
				List<AttendCountTypeModel> modelList = attendCountTypeDao.getExceptionTotalTimeByStaffNum((String)attendModel.get(i).getStaff_num(), (String)attendModel.get(i).getException_type(),
						model.getBegin_time(),model.getEnd_time());
				
				StringBuffer sb=new StringBuffer();
//				String everyday_time="";
//				String duty_time_type1="上午";
//				String duty_time_type4="下午";
//				String time_interval1="0";
//				String time_interval4="0";
				
				for(int n=0;n<modelList.size();n++){
					/*if(n==0 ||modelList.get(n).getEveryday_time().equals(modelList.get(n-1).getEveryday_time())){
					
						
						everyday_time=(String)modelList.get(n).getEveryday_time();
						if("1".equals((String)modelList.get(n).getDuty_time_type())){
							time_interval1=(String)modelList.get(n).getTime_interval();
						}
						if("4".equals((String)modelList.get(n).getDuty_time_type())){
							time_interval4=(String)modelList.get(n).getTime_interval();
						}
					}else{
						if(!"0".equals(time_interval1)&&!"0".equals(time_interval4)){
							sb.append(everyday_time+duty_time_type1+"，"+everyday_time+duty_time_type4+"，");
						}else{
							sb.append(everyday_time+"，");
						}
						 time_interval1="0";
						 time_interval4="0";
						everyday_time=(String)modelList.get(n).getEveryday_time();
						if("1".equals((String)modelList.get(n).getDuty_time_type())){
							time_interval1=(String)modelList.get(n).getTime_interval();
						}
						if("4".equals((String)modelList.get(n).getDuty_time_type())){
							time_interval4=(String)modelList.get(n).getTime_interval();
						}
					}*/
					
					String everyday_time = modelList.get(n).getEveryday_time();
					String duty_time_type = modelList.get(n).getDuty_time_type();
					String kg_type = modelList.get(n).getKg_type();
					Integer kg_count = modelList.get(n).getKg_count();
					if("3".equals(kg_type) && kg_count < 2){
						sb.append(everyday_time+"全天，");
					} else if ("1".equals(duty_time_type) || "2".equals(duty_time_type)){
						sb.append(everyday_time+"上午"+"，");
					} else if ("3".equals(duty_time_type) || "4".equals(duty_time_type)){
						sb.append(everyday_time+"下午"+"，");
					}
					
					
				}
				/*if(!"0".equals(time_interval1)&&!"0".equals(time_interval4)){
					sb.append(everyday_time+duty_time_type1+"，"+everyday_time+duty_time_type4+"，");
				}else{
					sb.append(everyday_time+"，");
				}*/
				
				attendModel2.setTotalTime(sb.substring(0, sb.length()-1));
				attendList.add(attendModel2);
		}
		return attendList;
	}


	@Override
	public List<AttendCountTypeModel> getAttendExceptionTotalNumber(AttendCountTypeModel model) {
		List<AttendCountTypeModel> attendModel = attendCountTypeDao.getAttendExceptionTotalNumber(model);
		List<AttendCountTypeModel> attendList=new ArrayList<AttendCountTypeModel>();
		
		AttendCountTypeModel attendModel2=new AttendCountTypeModel();
		for(int i=0;i<attendModel.size();i++){
			
			if(i==0 ||attendModel.get(i).getStaff_num().equals(attendModel.get(i-1).getStaff_num())){
				attendModel2.setBm_dm((String)attendModel.get(i).getBm_dm());
				attendModel2.setBm_mc((String)attendModel.get(i).getBm_mc());
				attendModel2.setSjbm_mc((String)attendModel.get(i).getSjbm_mc());
				attendModel2.setStaff_name((String)attendModel.get(i).getStaff_name());
				attendModel2.setStaff_no((String)attendModel.get(i).getStaff_no());
				attendModel2.setStaff_num((String)attendModel.get(i).getStaff_num());
				String exceptionTypeName=(String)attendModel.get(i).getExceptionTypeName();
				String totalNumber=(String)attendModel.get(i).getTotalNumber();
				if("迟到".equals(exceptionTypeName)){
					attendModel2.setException_type1(totalNumber);
				}else if("早退".equals(exceptionTypeName)){
					attendModel2.setException_type2(totalNumber);
				}else if("旷工".equals(exceptionTypeName)){
					attendModel2.setException_type4(totalNumber);
				}
			} else {
				attendList.add(attendModel2);
				if(i<attendModel.size()){
					attendModel2=new AttendCountTypeModel();
					attendModel2.setBm_dm((String)attendModel.get(i).getBm_dm());
					attendModel2.setBm_mc((String)attendModel.get(i).getBm_mc());
					attendModel2.setSjbm_mc((String)attendModel.get(i).getSjbm_mc());
					attendModel2.setStaff_name((String)attendModel.get(i).getStaff_name());
					attendModel2.setStaff_no((String)attendModel.get(i).getStaff_no());
					attendModel2.setStaff_num((String)attendModel.get(i).getStaff_num());
					String exceptionTypeName=(String)attendModel.get(i).getExceptionTypeName();
					String totalNumber=(String)attendModel.get(i).getTotalNumber();
					if("迟到".equals(exceptionTypeName)){
						attendModel2.setException_type1(totalNumber);
					}else if("早退".equals(exceptionTypeName)){
						attendModel2.setException_type2(totalNumber);
					}else if("旷工".equals(exceptionTypeName)){
						attendModel2.setException_type4(totalNumber);
					}
					
				}
			}
		}
		if(attendModel.size()!=0 ){
			attendList.add(attendModel2);
		}
		return attendList;
		
	}

	
	
	

	@Override
	public List<AttendCountTypeModel> getDeptExceptionTotalNumber(AttendCountTypeModel model) {
		
		List<AttendCountTypeModel> newDeptExceptionList = new ArrayList<AttendCountTypeModel>();
		for (String deptNum : model.getBm_dm().split(",")) {
			
			Map<String, Object> deptParam = new HashMap<String, Object>();
			deptParam.put("deptNums", deptNum.replace("'", ""));
			deptParam.put("begin_time", model.getBegin_time());
			deptParam.put("end_time", model.getEnd_time());
			List<AttendCountTypeModel> deptExceptionList = attendCountTypeDao.getExceptionByDept(deptParam);
			
			if(deptExceptionList != null && deptExceptionList.size() != 0){
				AttendCountTypeModel newDeptException = new AttendCountTypeModel();
				for (AttendCountTypeModel deptException : deptExceptionList) {
					newDeptException.setBm_dm((String)deptException.getBm_dm());
					newDeptException.setBm_mc((String)deptException.getBm_mc());
					newDeptException.setSjbm_mc((String)deptException.getSjbm_mc());
					String exceptionTypeName=(String)deptException.getExceptionTypeName();
					String totalNumber=(String)deptException.getTotalNumber();
					if("迟到".equals(exceptionTypeName)){
						newDeptException.setException_type1(totalNumber);
					}else if("早退".equals(exceptionTypeName)){
						newDeptException.setException_type2(totalNumber);
					}else if("旷工".equals(exceptionTypeName)){
						newDeptException.setException_type4(totalNumber);
					}
				}
				newDeptExceptionList.add(newDeptException);
			}
		}
		
		return newDeptExceptionList;
		
	}
	
	
	
	@Override
	public Grid getAttendLeaveCount(Map map) {
		Grid grid=new Grid();
		
		List<Map> leaveList = attendCountTypeDao.getAttendLeaveCountList(map);
		for (Map leaveMap : leaveList) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("process_instance_id", leaveMap.get("id"));
			List<Double> times = attendCountTypeDao.getLeaveTimeById(param);
			Double totalTime = 0.0;
			for (Double time : times) {
				totalTime = totalTime + time;
			}
			leaveMap.put("leavetime", totalTime);
		}
		
		grid.setRows(leaveList);
		grid.setTotal(attendCountTypeDao.getAttendLeaveCount(map));
		return grid;
	}
	@Override
	public Grid getAttendOutWorkCount(Map map) {
		Grid grid = new Grid();
		List<Map> modelMap = attendCountTypeDao.getAttendOutWorkCountList(map);
		List<Map> mapList = new ArrayList();
		for(Map<String, Object> map2:modelMap){
			map2.put("begin_time", map2.get("begindate").toString());
			map2.put("end_time", map2.get("enddate").toString());
			map2.put("beginhour", ((String) map2.get("beginhour"))+":00");
			map2.put("endhour", ((String) map2.get("endhour"))+":00");
			mapList.add(map2);
		}
		grid.setRows(mapList);
		grid.setTotal(attendCountTypeDao.getAttendOutWorkCount(map));
		return grid;
	}


	@Override
	public List<Map> exportAttendOutWorkCount(Map map) {
		List<Map> modelMap =attendCountTypeDao.exportAttendOutWorkCount(map);
		List<Map> mapList=new ArrayList();
		for(Map map2:modelMap){
			map2.put("begin_time", map2.get("begindate").toString()+" "+ map2.get("beginhour").toString());
			map2.put("end_time", map2.get("enddate").toString()+" "+ map2.get("endhour").toString());
			mapList.add(map2);
		}
		return mapList;
	}
	@Override
	public List<Map> exportAttendLeaveCount(Map map) {
		return attendCountTypeDao.exportAttendLeaveCount(map);
	}

	
	@Override
	public Grid<StaffAttendCount> getStaffAttendCountList(Map map) {
		Grid<StaffAttendCount> grid = new Grid<StaffAttendCount>();
		
		List<StaffAttendCount> staffAttendList = attendCountTypeDao.getStaffAttendCountList(map);
		for (StaffAttendCount staffAttend : staffAttendList) {
			StringBuffer remarks = new StringBuffer();

			if(staffAttend.getEarly_time() != 0){
				staffAttend.setIsEarly("1");
				remarks.append("早退");
			}
			
			if(staffAttend.getLater_time() != 0){
				staffAttend.setIsLater("1");
				remarks.append("迟到");
			}
			
			if(staffAttend.getKg_time() != 0){
				staffAttend.setIsKg("1");
				remarks.append("旷工");
			}
			
			if("1".equals(staffAttend.getIsholiday())){
				remarks = new StringBuffer();
				remarks.append("节假日");
			}
			
			if("1".equals(staffAttend.getIsvocation())){
				remarks = new StringBuffer();
				remarks.append("公休日");
			}
			
			staffAttend.setRemark(remarks.toString());
			
			Map<String, Object> checkParam = new HashMap<String, Object>();
			checkParam.put("staff_num", staffAttend.getStaff_num());
			checkParam.put("begin_time", staffAttend.getEvery_date() + " 00:00:00");
			checkParam.put("end_time", staffAttend.getEvery_date() + " 23:59:59");
			
			Map firstCheckResult = attendCountTypeDao.getFristCheckByTime(checkParam);
			if(firstCheckResult != null){
				staffAttend.setCheck_time1(((String)firstCheckResult.get("checktime")).split(" ")[1]);
				staffAttend.setCheck_device1((String)firstCheckResult.get("door_name"));
			}
			
			Map lastCheckResult = attendCountTypeDao.getLastCheckByTime(checkParam);
			if(lastCheckResult != null){
				staffAttend.setCheck_time2(((String)lastCheckResult.get("checktime")).split(" ")[1]);
				staffAttend.setCheck_device2((String)lastCheckResult.get("door_name"));
			}
			
		}
		
		grid.setRows(staffAttendList);
		grid.setTotal(attendCountTypeDao.getStaffAttendCount(map));
		return grid;
	}

}
