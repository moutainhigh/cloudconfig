package com.kuangchi.sdd.doorAccessConsole.authority.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.attendanceConsole.dutyuser.dao.DutyUserDao;
import com.kuangchi.sdd.attendanceConsole.dutyuser.model.DutyUser;
import com.kuangchi.sdd.attendanceConsole.dutyuser.service.DutyUserService;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.devicePosition.dao.DevicePositionDao;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.employee.dao.EmployeeDao;
import com.kuangchi.sdd.doorAccessConsole.authority.dao.PeopleAuthorityInfoDao;
import com.kuangchi.sdd.doorAccessConsole.authority.exception.ImportException;
import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityByCardModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityByDoorModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityByStaffModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityTask;
import com.kuangchi.sdd.doorAccessConsole.authority.model.CardModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DeviceModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DoorModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.PeopleAuthorityInfoModel;
import com.kuangchi.sdd.doorAccessConsole.authority.service.PeopleAuthorityInfoService;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.service.IDepartmentGrantService;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.service.PeopleAuthorityManagerService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;


@Service("peopleAuthorityService")
public class PeopleAuthorityInfoServiceImpl  implements PeopleAuthorityInfoService{
	@Resource(name="peopleAuthorityDao")	
	PeopleAuthorityInfoDao peopleAuthorityDao;
	
	@Resource(name = "employeeDao")
    EmployeeDao employeeDao;
	@Resource(name ="devicePositionDaoImpl")
	private DevicePositionDao devicePositionDao;
	
	@Resource(name="dutyUserServiceImpl")
	DutyUserService dutyUserService;
	
	@Resource(name = "dutyUserDaoImpl")
	DutyUserDao dutyUserDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	@Override
	public Grid<PeopleAuthorityInfoModel> getPeopleAuthorityInfoByStaffNum(
			String staffNum,Integer skip, Integer rows) {
	
        Grid<PeopleAuthorityInfoModel> grid = new Grid<PeopleAuthorityInfoModel>();
        List<PeopleAuthorityInfoModel> resultList = peopleAuthorityDao.getPeopleAuthorityInfoByStaffNum(staffNum, skip, rows);
        grid.setRows(resultList);
        if (null != resultList) {
            grid.setTotal(peopleAuthorityDao.getPeopleAuthorityInfoByStaffNumCount(staffNum));
        } else {
            grid.setTotal(0);
        }
        return grid;
	}



	@Override
	public Grid<PeopleAuthorityInfoModel> searchAuthority(String doorNum,String doorName,String staffName,String staffNo,String cardNum,Integer skip, Integer rows) {
		  Grid<PeopleAuthorityInfoModel> grid = new Grid<PeopleAuthorityInfoModel>();
	        List<PeopleAuthorityInfoModel> resultList = peopleAuthorityDao.searchAuthority( doorNum, doorName, staffName, staffNo, cardNum, skip, rows);
	        grid.setRows(resultList);
	        if (null != resultList) {
	            grid.setTotal(peopleAuthorityDao.searchAuthorityCount( doorNum, doorName, staffName, staffNo, cardNum));
	        } else {
	            grid.setTotal(0);
	        }
	        return grid;
		
	}

	
	
	@Override
	public void dispatchAuthority(List<PeopleAuthorityInfoModel> peopleAuthorityInfoModelList,String createUser) {
		
		for(PeopleAuthorityInfoModel peopleAuthorityInfoModel:peopleAuthorityInfoModelList){
			
		  peopleAuthorityDao.insertAuthority(peopleAuthorityInfoModel.getCardNum(), peopleAuthorityInfoModel.getDoorNum(), peopleAuthorityInfoModel.getDeviceNum(), DateUtil.getDateString(new Date()), createUser, "",
				  peopleAuthorityInfoModel.getValidStartTime(),peopleAuthorityInfoModel.getValidEndTime(),"".equals(peopleAuthorityInfoModel.getTimeGroupNum())?null:peopleAuthorityInfoModel.getTimeGroupNum(),
						 null );
		}
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "单门多门权限新增");
	    log.put("V_OP_FUNCTION", "新增");
	    log.put("V_OP_ID", createUser);
	    log.put("V_OP_MSG", "单门多门权限新增");
	    log.put("V_OP_TYPE", "业务");
	    logDao.addLog(log);
	}

	@Override
	public void copyAuthority(List<String> beCopiedCardNumList,
			String copyCardNum,String createUser) {
		List<Map> allAuthListHasRepeat=new ArrayList<Map>();
		for (int i = 0; i < beCopiedCardNumList.size(); i++) {
			List<Map> doorList = peopleAuthorityDao.getBeCopyCardDoors(beCopiedCardNumList.get(i));
			allAuthListHasRepeat.addAll(doorList);
		}
		
		//删除目标卡中可能与被复制的卡重叠的权限
		for(Map ele:allAuthListHasRepeat){
			ele.put("cardNum", copyCardNum);
			ele.put("createUser", createUser);
			peopleAuthorityDao.delOldDoorAuth(copyCardNum,(String)ele.get("doorNum"),(String)ele.get("deviceNum"));
			peopleAuthorityDao.myInsertIntoTable(ele);
		}
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "权限复制");
	    log.put("V_OP_FUNCTION", "新增");
	    log.put("V_OP_ID", createUser);
	    log.put("V_OP_MSG", "权限复制");
	    log.put("V_OP_TYPE", "业务");
	    logDao.addLog(log);
	}
	
		/**
		 * 按卡导入权限
		 */
		@Override
		public void batchAddAuthorityByCard(
				List<AuthorityByCardModel> authorityByCardModelList,String createUser) throws ImportException {
			for (AuthorityByCardModel model :authorityByCardModelList){
				String cardNum=model.getCardNum();
				 //判断卡号是否存在
				 String card = peopleAuthorityDao.selectCardNum(cardNum);
				 String doorNum=model.getDoorNum();
				 String startTime = model.getValidStartTime();
				 String endTime = model.getValidEndTime();
				 List<String> deviceNum1=peopleAuthorityDao.getDeviceNumByDoorNum(doorNum);
	    		 String deviceNum=model.getDeviceNum();
	    		 String taskState = model.getTaskState();
	    		 String timeGroupName=model.getTimeGroupName();
				 String timeGroupNum=peopleAuthorityDao.getTimesGroupNumByTimesGroupName(timeGroupName);
				 if(card == null || "".equals(card)){
					 throw new ImportException("导入失败，请检查卡编号"+cardNum+"是否正确！"); 
				 }else if(deviceNum1.size()==0){
					 throw new ImportException("导入失败，请检查门编号"+doorNum+"是否正确！");
				 }else{
					 for(int i=0; i<deviceNum1.size(); i++){
	    				 if(deviceNum1.get(i).equals(deviceNum)){
	    					DeviceInfo device = devicePositionDao.getDeviceByDeviceNum(deviceNum);
	    					if(device==null){
	    						throw new ImportException("导入权限失败，请检查设备编号"+deviceNum+"是否正确！");
	    					}else {
	    						//连接接口
	    						/*String url = PropertiesToMap.propertyToMap("comm_interface.properties").get("comm_url"); 
	    						String message = HttpRequest.sendPost(url+ "gateLimit/setGateLimit.do?", "mac="+ device.getDevice_mac() +"&device_type=" + device.getDevice_type() 
		    							+"&gateId="+doorNum +"&cardId="+cardNum +"&start="+startTime +"&end="+endTime +"&group="+timeGroupNum);
		    					Result result = GsonUtil.toBean(message, Result.class);
		    					if("0".equals(result.getResult_code())){	//连接成功
			    					 
		    					}else
		    						throw new ImportException("导入权限失败，网络异常！");*/
	    						
	    						//新增权限任务
	    						AuthorityTask task = new AuthorityTask();
	    						task.setCard_num(cardNum);
	    						task.setDoor_num(doorNum);
	    						task.setDevice_num(deviceNum);
	    						task.setDevice_mac(device.getDevice_mac());
	    						task.setDevice_type(device.getDevice_type());
	    						task.setValid_start_time(startTime);
	    						task.setValid_end_time(endTime);
	    						task.setTime_group_num(timeGroupNum);
	    						task.setFlag("0");
	    						boolean result = peopleAuthorityDao.insertAuthorityTask(task);
	    						if(result){
	    							//删除已经存在的权限再插入，防止权限重复
			    					peopleAuthorityDao.delAuthority(doorNum, cardNum,deviceNum);
			    					peopleAuthorityDao.insertAuthority(cardNum, doorNum, deviceNum, DateUtil.getDateString(new Date()), createUser, "",startTime,endTime,timeGroupNum,taskState);
	    						}
	    					 }
	    				 }
		    		 }
				 }
			 }
			Map<String, String> log = new HashMap<String, String>();
			log.put("V_OP_NAME", "按卡批量导入权限");
		    log.put("V_OP_FUNCTION", "新增");
		    log.put("V_OP_ID", createUser);
		    log.put("V_OP_MSG", "按卡批量导入权限");
		    log.put("V_OP_TYPE", "业务");
		    logDao.addLog(log);
		}
		
		/**
		 * 按人导入权限
		 */
		@Override
		public void batchAddAuthorityByStaff(
				List<AuthorityByStaffModel> authorityByStaffModelList,String createUser) throws ImportException {
		    for(AuthorityByStaffModel model:authorityByStaffModelList){
		    	String timeGroupName=model.getTimeGroupName();
		    	String staffNum=model.getStaffNum();
		    	String staffNo=model.getStaffNo();
		    	String startTime = model.getValidStartTime();
				String endTime = model.getValidEndTime();
				String taskState = model.getTaskState();
		    	 List<String> cardNumList=peopleAuthorityDao.getCardNumByStaffNum(staffNum);
		    	 if(cardNumList.size()== 0 || cardNumList==null){
		    		 throw new ImportException("导入失败，请检查员工编号"+staffNo+"是否正确！");
		    	 }
		    	 for(String cardNum:cardNumList){
		    		 String doorNum=model.getDoorNum();
		    		 String timeGroupNum=peopleAuthorityDao.getTimesGroupNumByTimesGroupName(timeGroupName);
		    		 List<String> deviceNum1=peopleAuthorityDao.getDeviceNumByDoorNum(doorNum);
		    		 String deviceNum=model.getDeviceNum();
		    		 if(deviceNum1.size()==0){
		    			 throw new ImportException("导入失败，请检查门编号"+doorNum+"是否正确！");
		    		 }else {
						 for(int i=0; i<deviceNum1.size(); i++){
		    				 if(deviceNum1.get(i).equals(deviceNum)){
		    					DeviceInfo device = devicePositionDao.getDeviceByDeviceNum(deviceNum);
		    					if(device==null){
		    						throw new ImportException("导入权限失败，请检查设备编号"+deviceNum+"是否正确！");
		    					}else {
		    						//连接接口
									/*String url = PropertiesToMap.propertyToMap("comm_interface.properties").get("comm_url"); 
		    						String message = HttpRequest.sendPost(url+ "gateLimit/setGateLimit.do?", "mac="+ device.getDevice_mac() +"&device_type=" + device.getDevice_type() 
			    							+"&gateId="+doorNum +"&cardId="+cardNum +"&start="+startTime +"&end="+endTime +"&group="+timeGroupNum);
			    					Result result = GsonUtil.toBean(message, Result.class);
			    					if("0".equals(result.getResult_code())){	//连接成功
				    					 
			    					}else {
			    						throw new ImportException("导入权限失败，网络异常！");
			    					}*/
		    						
		    						//新增权限任务
		    						AuthorityTask task = new AuthorityTask();
		    						task.setCard_num(cardNum);
		    						task.setDoor_num(doorNum);
		    						task.setDevice_num(deviceNum);
		    						task.setDevice_mac(device.getDevice_mac());
		    						task.setDevice_type(device.getDevice_type());
		    						task.setValid_start_time(startTime);
		    						task.setValid_end_time(endTime);
		    						task.setTime_group_num(timeGroupNum);
		    						task.setFlag("0");
		    						boolean result = peopleAuthorityDao.insertAuthorityTask(task);
		    						if(result){
		    							//删除已经存在的权限再插入，防止权限重复
			    						peopleAuthorityDao.delAuthority(doorNum, cardNum, deviceNum);
			    						peopleAuthorityDao.insertAuthority(cardNum, doorNum, deviceNum, DateUtil.getDateString(new Date()), createUser, "",startTime,endTime,timeGroupNum,taskState);
		    						}
		    					 }
		    				 }
			    		 }
		    		 }
		    	 }
		    }
			Map<String, String> log = new HashMap<String, String>();
			log.put("V_OP_NAME", "按人批量导入权限");
		    log.put("V_OP_FUNCTION", "新增");
		    log.put("V_OP_ID", createUser);
		    log.put("V_OP_MSG", "按人批量导入权限");
		    log.put("V_OP_TYPE", "业务");
		    logDao.addLog(log);
		}
		
		/**
		 * 按门导入权限
		 */
		@Override
		public void batchAddAuthorityByDoor(List<AuthorityByDoorModel> authorityByDoorModelList,String createUser) throws ImportException {
			for(AuthorityByDoorModel authorityByDoorModel:authorityByDoorModelList){
				String timeGroupName=authorityByDoorModel.getTimeGroupName();
				String doorNum = authorityByDoorModel.getDoorNum();
				String startTime = authorityByDoorModel.getValidStartTime();
				String endTime = authorityByDoorModel.getValidEndTime();
			    String timeGroupNum=peopleAuthorityDao.getTimesGroupNumByTimesGroupName(timeGroupName);
			    List<String> deviceNum1=peopleAuthorityDao.getDeviceNumByDoorNum(doorNum);
			    String deviceNum = authorityByDoorModel.getDeviceNum();
			    
			    if(deviceNum1.size()==0){
	    			 throw new ImportException("导入失败，请检查门编号"+doorNum+"是否正确！");
	    		 }else {
	    			 List<String> cardList = peopleAuthorityDao.selAllCardNum();
					 for(int i=0; i<deviceNum1.size(); i++){
	    				 if(deviceNum1.get(i).equals(deviceNum)){
	    					DeviceInfo device = devicePositionDao.getDeviceByDeviceNum(deviceNum);
	    					if(device==null){
	    						throw new ImportException("导入权限失败，请检查设备编号"+deviceNum+"是否正确！");
	    					}else {
	    						for(int j=0; j<cardList.size(); j++){
	    							//连接接口
	    	    					/*String url = PropertiesToMap.propertyToMap("comm_interface.properties").get("comm_url");  
	    							message = HttpRequest.sendPost(url+ "gateLimit/setGateLimit.do?", "mac="+ device.getDevice_mac() +"&device_type=" + device.getDevice_type() 
			    							+"&gateId="+doorNum +"&cardId="+cardList.get(j) +"&start="+startTime +"&end="+endTime +"&group="+timeGroupNum);
			    					Result result = GsonUtil.toBean(message, Result.class);
			    					if("1".equals(result.getResult_code())){
			    						throw new ImportException("导入权限失败，网络异常！");
			    					}else if(j==cardList.size()-1){	//到最后一个都连接成功
			    						 
			    					}*/
			    					
	    							//新增权限任务
		    						AuthorityTask task = new AuthorityTask();
		    						task.setCard_num(cardList.get(i));
		    						task.setDoor_num(doorNum);
		    						task.setDevice_num(deviceNum);
		    						task.setDevice_mac(device.getDevice_mac());
		    						task.setDevice_type(device.getDevice_type());
		    						task.setValid_start_time(startTime);
		    						task.setValid_end_time(endTime);
		    						task.setTime_group_num(timeGroupNum);
		    						task.setFlag("0");
		    						peopleAuthorityDao.insertAuthorityTask(task);
	    						}
	    						//删除已经存在的权限再插入，防止权限重复
	    						peopleAuthorityDao.delAuthorityByDoor(doorNum,deviceNum);
	    			   		    peopleAuthorityDao.addAuthorityByDoor(doorNum, deviceNum, createUser, "",startTime,endTime,timeGroupNum);
	    					 }
	    				 }
		    		 }
	    		 }
			}
			Map<String, String> log = new HashMap<String, String>();
			log.put("V_OP_NAME", "按门批量导入权限");
		    log.put("V_OP_FUNCTION", "新增");
		    log.put("V_OP_ID", createUser);
		    log.put("V_OP_MSG", "按门批量导入权限");
		    log.put("V_OP_TYPE", "业务");
		    logDao.addLog(log);
		}

		/*增加对门编号与门名称的匹配
		 * by weixuan.lu
		 * 增加获取设备编号和dao的传入参数  2016/4/25
		 * */ 
		@Override
		public boolean batchDelAuthorityByCard(
				List<AuthorityByCardModel> authorityByCardModelList,String createUser) throws Exception {
			for(AuthorityByCardModel authorityByCardModel:authorityByCardModelList){
				String timeGroupName=authorityByCardModel.getTimeGroupName();
				String cardNum=authorityByCardModel.getCardNum();
				String card = peopleAuthorityDao.selectCardNum(cardNum);
				String doorNum=authorityByCardModel.getDoorNum();
				String doorName1=authorityByCardModel.getDoorName();
				String deviceNum = authorityByCardModel.getDeviceNum();
				String doorName = peopleAuthorityDao.getDoorNameByDoorNum(doorNum,deviceNum);
				String startTime = authorityByCardModel.getValidStartTime();
				String endTime = authorityByCardModel.getValidEndTime();
				String i=authorityByCardModel.getGroupId();//行数
			    String timeGroupNum=peopleAuthorityDao.getTimesGroupNumByTimesGroupName(timeGroupName);
				try {
					 if(card == null || "".equals(card)){
						 throw new Exception("导入失败，请检查第"+i+"行卡号是否正确！"); 
					 }else if(doorName!=null){
						DeviceInfo device = devicePositionDao.getDeviceByDeviceNum(deviceNum);;
						if(device==null){
							throw new Exception("导入失败，请检查第"+i+"行设备编号是否正确！"); 
						}
						if(doorName1!=null && !"".equals(doorName1)){
							if(doorNum != null && doorName.equals(doorName1)){
								/*//远程调用common项目的action
								Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");
								String delAthorityUrl=map.get("comm_url")+"gateLimit/delGateLimit.do?";
								String  delAthorityByCardRespStr=HttpRequest.sendPost(delAthorityUrl, "gateId="+doorNum+"&cardId="+cardNum+"&device_type="+device.getDevice_type()+"&mac="+device.getDevice_mac());
								Map delAthorityByCard=GsonUtil.toBean(delAthorityByCardRespStr,HashMap.class);*/
							/*	if("0".equals(delAthorityByCard.get("result_code"))){*/
								//删除权限任务
	    						AuthorityTask task = new AuthorityTask();
	    						task.setCard_num(cardNum);
	    						task.setDoor_num(doorNum);
	    						task.setDevice_num(deviceNum);
	    						task.setDevice_mac(device.getDevice_mac());
	    						task.setDevice_type(device.getDevice_type());
	    						task.setValid_start_time(startTime);
	    						task.setValid_end_time(endTime);
	    						task.setTime_group_num(timeGroupNum);
	    						task.setFlag("1");
	    						peopleAuthorityDao.insertAuthorityTask(task);	
								peopleAuthorityDao.deleteAuthority(doorNum, cardNum,deviceNum);
									
								/*}else{
									throw new Exception("导入权限失败，网络异常！");
								}*/
							}else{
								throw new Exception("删除权限失败！请查看是否有门名称"+doorName1+"与门编号"+doorNum+"不匹配");
							}
						}else if(doorNum != null &&!"".equals(doorNum)){
							//远程调用common项目的action
							/*Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");
							String delAthorityUrl=map.get("comm_url")+"gateLimit/delGateLimit.do?";
							String  delAthorityByCardRespStr=HttpRequest.sendPost(delAthorityUrl, "gateId="+doorNum+"&cardId="+cardNum+"&device_type="+device.getDevice_type()+"&mac="+device.getDevice_mac());
							Map delAthorityByCard=GsonUtil.toBean(delAthorityByCardRespStr,HashMap.class);
							if("0".equals(delAthorityByCard.get("result_code"))){*/
							//删除权限任务
    						AuthorityTask task = new AuthorityTask();
    						task.setCard_num(cardNum);
    						task.setDoor_num(doorNum);
    						task.setDevice_num(deviceNum);
    						task.setDevice_mac(device.getDevice_mac());
    						task.setDevice_type(device.getDevice_type());
    						task.setValid_start_time(startTime);
    						task.setValid_end_time(endTime);
    						task.setTime_group_num(timeGroupNum);
    						task.setFlag("1");
    						peopleAuthorityDao.insertAuthorityTask(task);
							peopleAuthorityDao.deleteAuthority(doorNum, cardNum,deviceNum);
							/*}else{
								throw new Exception("导入权限失败，网络异常！");
							}*/
						}else{
							return false;
						}
					}else{
						throw new Exception("删除权限失败！请查看设备编号"+deviceNum+"或门编号"+doorNum+"是否正确");
					}
				} catch (Exception e) {
                    e.printStackTrace();
                    throw new Exception(e.getMessage());
				}
			}
			Map<String, String> log = new HashMap<String, String>();
			log.put("V_OP_NAME", "按卡批量删除权限");
		    log.put("V_OP_FUNCTION", "删除业务");
		    log.put("V_OP_ID", createUser);
		    log.put("V_OP_MSG", "按卡批量删除权限");
		    log.put("V_OP_TYPE", "业务");
		    logDao.addLog(log);
		    return true;
		}
		
		/*增加对员工编号与员工姓名的匹配
		 * 增加对门编号与门名称的匹配
		 * by weixuan.lu
		 * */ 
		@Override
		public boolean  batchDelAuthorityByStaff(
				List<AuthorityByStaffModel> authorityByStaffModelList,String createUser) throws Exception {
			 for (AuthorityByStaffModel authorityByStaffModel:authorityByStaffModelList) {
				 String timeGroupName=authorityByStaffModel.getTimeGroupName();
				 String staffNo = authorityByStaffModel.getStaffNum();
                 String staffNum = employeeDao.selectStaffNum(staffNo);
                 authorityByStaffModel.setStaffNum(staffNum);
				 //拿页面的人号
				 String staffNum1=authorityByStaffModel.getStaffNum();
				 //拿卡号
				 List<String> cardNumList=peopleAuthorityDao.getCardNumByStaffNum(staffNum1);
				 //拿页面的人名
				 String staffName1 = authorityByStaffModel.getStaffName();
				 //拿页面的门号
				 String doorNum=authorityByStaffModel.getDoorNum();
				 //拿页面的门名
				 //String doorName1=authorityByStaffModel.getDoorName();
				 //拿表格的设备编号
				 String deviceNum = authorityByStaffModel.getDeviceNum();
				 //拿人名
				 String staffName = peopleAuthorityDao.getStaffNameByStaffNum(staffNum1);
				 //拿门名
				 String doorName = peopleAuthorityDao.getDoorNameByDoorNum(doorNum,deviceNum);
				 //行号
				 String i=authorityByStaffModel.getStaffNo();
				 
				 String startTime = authorityByStaffModel.getValidStartTime();
				 String endTime = authorityByStaffModel.getValidEndTime();
			     String timeGroupNum=peopleAuthorityDao.getTimesGroupNumByTimesGroupName(timeGroupName);
				    
				 try {
					
					    DeviceInfo device = devicePositionDao.getDeviceByDeviceNum(deviceNum);;
					    if(device==null){
							throw new Exception("导入失败，请检查第"+i+"行“设备编号”是否正确！"); 
						}
					    if(staffNum !=null && !"".equals(staffNum)){
					    	if(null==staffName1||"".equals(staffName1)){
					    		  if(cardNumList.size()== 0 || cardNumList==null){
										 throw new ImportException("导入失败，第"+i+"行员工下无绑定卡");
									 }else{
										for(String cardNum:cardNumList){
											//远程调用common项目的action
											/*Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");
											String delAthorityUrl=map.get("comm_url")+"gateLimit/delGateLimit.do?";
											String  delAthorityByCardRespStr=HttpRequest.sendPost(delAthorityUrl, "gateId="+doorNum+"&cardId="+cardNum+"&device_type="+device.getDevice_type()+"&mac="+device.getDevice_mac());
											Map delAthorityByCard=GsonUtil.toBean(delAthorityByCardRespStr,HashMap.class);	
										
											if("0".equals(delAthorityByCard.get("result_code"))){*/
											//删除权限任务
				    						AuthorityTask task = new AuthorityTask();
				    						task.setCard_num(cardNum);
				    						task.setDoor_num(doorNum);
				    						task.setDevice_num(deviceNum);
				    						task.setDevice_mac(device.getDevice_mac());
				    						task.setDevice_type(device.getDevice_type());
				    						task.setValid_start_time(startTime);
				    						task.setValid_end_time(endTime);
				    						task.setTime_group_num(timeGroupNum);
				    						task.setFlag("1");
				    						peopleAuthorityDao.insertAuthorityTask(task);	
											peopleAuthorityDao.deleteAuthority(doorNum, cardNum,deviceNum);
										
											/*	}else{
												throw new Exception("导入权限失败，网络异常！");
											}*/
									}
								} 
					    	}else if(staffName1.equals(staffName)){
					    		  if(cardNumList.size()== 0 || cardNumList==null){
										 throw new ImportException("导入失败，第"+i+"行员工下无绑定卡");
									 }else{
										for(String cardNum:cardNumList){
											//远程调用common项目的action
											/*Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");
											String delAthorityUrl=map.get("comm_url")+"gateLimit/delGateLimit.do?";
											String  delAthorityByCardRespStr=HttpRequest.sendPost(delAthorityUrl, "gateId="+doorNum+"&cardId="+cardNum+"&device_type="+device.getDevice_type()+"&mac="+device.getDevice_mac());
											Map delAthorityByCard=GsonUtil.toBean(delAthorityByCardRespStr,HashMap.class);	
										
											if("0".equals(delAthorityByCard.get("result_code"))){*/
											//删除权限任务
				    						AuthorityTask task = new AuthorityTask();
				    						task.setCard_num(cardNum);
				    						task.setDoor_num(doorNum);
				    						task.setDevice_num(deviceNum);
				    						task.setDevice_mac(device.getDevice_mac());
				    						task.setDevice_type(device.getDevice_type());
				    						task.setValid_start_time(startTime);
				    						task.setValid_end_time(endTime);
				    						task.setTime_group_num(timeGroupNum);
				    						task.setFlag("1");
				    						peopleAuthorityDao.insertAuthorityTask(task);	
											peopleAuthorityDao.deleteAuthority(doorNum, cardNum,deviceNum);
										
											/*	}else{
												throw new Exception("导入权限失败，网络异常！");
											}*/
									}
								} 
					    	  }else{
					    		  throw new Exception("导入失败，第"+i+"行的“员工工号”与“员工姓名”不匹配");
					    	  }
					    }else{
					    	throw new Exception("导入失败，请检查第"+i+"行“员工工号”是否正确！"); 
					    }
					
					} catch (Exception e) {
	                    e.printStackTrace();
	                    throw new Exception(e.getMessage());
					}
				
				
			}
				Map<String, String> log = new HashMap<String, String>();
				log.put("V_OP_NAME", "按人批量删除权限");
			    log.put("V_OP_FUNCTION", "删除业务");
			    log.put("V_OP_ID", createUser);
			    log.put("V_OP_MSG", "按人批量删除权限");
			    log.put("V_OP_TYPE", "业务");
			    logDao.addLog(log);
			return true;
		}
		
		/* 增加对门编号与门名称的匹配
		 * by weixuan.lu
		 * */ 
		@Override
		public boolean batchDelAuthorityByDoor(List<AuthorityByDoorModel> authorityByDoorModelList,String createUser) throws Exception {
			
		 for(AuthorityByDoorModel authorityByDoorModel:authorityByDoorModelList){
			 String timeGroupName=authorityByDoorModel.getTimeGroupName();
			 String doorNum = authorityByDoorModel.getDoorNum();
			 //String doorName1=authorityByDoorModel.getDoorName();
			 String deviceName = authorityByDoorModel.getDeviceName();
			 String deviceMac = authorityByDoorModel.getDeviceMac();
			 String startTime = authorityByDoorModel.getValidStartTime();
			 String endTime = authorityByDoorModel.getValidEndTime();
			 String i=authorityByDoorModel.getStaffNum();//行号
		     String timeGroupNum=peopleAuthorityDao.getTimesGroupNumByTimesGroupName(timeGroupName);
			 
			 try {
					    Map  map= peopleAuthorityDao.getDeviceByNumMac(deviceName,deviceMac);
					    if(map.size()==0){
							throw new Exception("导入失败，请检查第"+i+"行“设备名称”是否正确！"); 
						}else{
						    String doorName = peopleAuthorityDao.getDoorNameByDoorNum(doorNum,map.get("device_num").toString());
							if(doorName ==null && "".equals(doorName)){
								   throw new Exception("导入失败，请检查第"+i+"行“门名称”是否正确！"); 
							}else{
								List<String> cardNums=peopleAuthorityDao.getCardNumByDoorNum(doorNum, map.get("device_num").toString());
								if(cardNums.size()>0){
									for(int j=0;j<cardNums.size();j++){
										/*Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");
										String delAthorityUrl=map.get("comm_url")+"gateLimit/delGateLimit.do?";
										String  delAthorityByCardRespStr=HttpRequest.sendPost(delAthorityUrl, "gateId="+doorNum+"&cardId="+cardNums.get(j)+"&device_type="+device.getDevice_type()+"&mac="+device.getDevice_mac());
										Map delAthorityByCard=GsonUtil.toBean(delAthorityByCardRespStr,HashMap.class);	
									    
										if("1".equals(delAthorityByCard.get("result_code"))){
				    						throw new Exception("导入权限失败，网络异常！");
				    						
				    					}else if(j==cardNums.size()-1){*/	//到最后一个都连接成功
				    					
				    					//删除权限任务
			    						AuthorityTask task = new AuthorityTask();
			    						task.setCard_num(cardNums.get(j));
			    						task.setDoor_num(doorNum);
			    						task.setDevice_num(map.get("device_num").toString());
			    						task.setDevice_mac(deviceMac);
			    						task.setDevice_type(map.get("device_type").toString());
			    						task.setValid_start_time(startTime);
			    						task.setValid_end_time(endTime);
			    						task.setTime_group_num(timeGroupNum);
			    						task.setFlag("1");
			    						peopleAuthorityDao.insertAuthorityTask(task);
										peopleAuthorityDao.deleteAuthority(doorNum, null,map.get("device_num").toString());
									    /*}*/
									}
								}else{
									throw new Exception("导入权限失败，请查看第"+i+"行设备或门下是否没有绑定卡!");
								}
							}
						} 
					  
					    
				} catch (Exception e) {
	              e.printStackTrace();
	              throw new Exception(e.getMessage());
				}
		 
		 }
			Map<String, String> log = new HashMap<String, String>();
			log.put("V_OP_NAME", "按门批量删除权限");
		    log.put("V_OP_FUNCTION", "删除业务");
		    log.put("V_OP_ID", createUser);
		    log.put("V_OP_MSG", "按门批量删除权限");
		    log.put("V_OP_TYPE", "业务");
		    logDao.addLog(log);
			return true;
		}



	@Override
	public List<CardModel> getCardListByStaffNum(String staffNum) {
	    
		return peopleAuthorityDao.getCardListByStaffNum(staffNum);
	}



	@Override
	public List<DoorModel> getDoorList() {

		return peopleAuthorityDao.getDoorList();
	}



	@Override
	public Integer getCardCountByCardNum(String cardNum) {
		
		return peopleAuthorityDao.getCardCountByCardNum(cardNum);
	}



	@Override
	public List<Map> getDoorModelByCardNum(String cardNum,Integer skip, Integer rows) {
	
		return peopleAuthorityDao.getDoorModelByCardNum(cardNum,skip,rows);
	}



	@Override
	public List<DeviceModel> getDeviceModelByCardNum(String cardNum) {
		
		return peopleAuthorityDao.getDeviceModelByCardNum(cardNum);
	}



	@Override
	public List<Map> getDoorModelByDeviceNum(String deviceNum,String cardNum) {
	 
		return peopleAuthorityDao.getDoorModelByDeviceNum(deviceNum,cardNum);
	}

	@Override
	public Tree getDoorTree() {
		List<DoorModel> allDoors=peopleAuthorityDao.getDoorList();
		List<DeviceModel> allDevices=peopleAuthorityDao.getAllDeviceModel();
		Tree tree=new Tree();
		 Map map=new HashMap();
	     map.put("isDevice",0);
	     tree.setAttributes(map);
	     tree.setChecked(true);
	     tree.setId("-1");
	     tree.setState("open");
	     tree.setText("设备组");
		for(DeviceModel deviceModel:allDevices){
		     Tree tr=new Tree();  
		     Map m=new HashMap();
		     m.put("isDevice",0);
		    tr.setAttributes(m);
		    tr.setState("open");
		    tr.setId(deviceModel.getDeviceNum());
		    tr.setText(deviceModel.getDeviceName());
		    tr.setIconCls("icon-device");
		    tree.getChildren().add(tr);
		    for(DoorModel doorModel:allDoors){
		    	 if(tr.getId().equals(doorModel.getDeviceNum())){
		    		 Tree tre=new Tree();
		    		 Map ma=new HashMap();
		   		     ma.put("isDevice",1);
		   		     tre.setAttributes(ma);
		   		     tre.setId(doorModel.getDoorNum());
		   		     tre.setText(doorModel.getDoorName());
		   		     tre.setIconCls("icon-door");
		   		     tre.setState("open");
		   		     tr.getChildren().add(tre); 
		    	 }
		    } 	
		    
		    
		}		
		return tree;
	}




	@Override
	public List<PeopleAuthorityInfoModel> searchAuthorityDownLoad(String doorNum,
			String doorName, String staffName, String staffNo, String cardNum) {
		return peopleAuthorityDao.searchAuthorityDownLoad(doorNum, doorName, staffName, staffNo, cardNum);
	}



	@Override
	public CardModel getDesCard(String cardNum) {
		return peopleAuthorityDao.getDesCard(cardNum);
	}



	@Override
	public List<Map> getTimesGroup(Map m) {
		return peopleAuthorityDao.getTimesGroup(m);
	}



	@Override
	public DoorModel getDoorModelByDoorNum(String doorNum) {
		return peopleAuthorityDao.getDoorModelByDoorNum(doorNum);
	}



	@Override
	public int getDoorModelCountByCardNum(String cardNums) {
		return peopleAuthorityDao.getDoorModelCountByCardNum(cardNums);
	}

	@Override
	public List<CardModel> getDesCardList(String cardNum,Integer skips,Integer rows) {
		return peopleAuthorityDao.getDesCardList(cardNum,skips,rows) ;
	}



	@Override
	public int getDesCardListCount(String desCardNum) {
		return peopleAuthorityDao.getDesCardListCount(desCardNum);
	}



	@Override
	public void delOldDoorAuth(List<PeopleAuthorityInfoModel> peopleAuthorityInfoModelList) {
		for(PeopleAuthorityInfoModel model:peopleAuthorityInfoModelList){
			  peopleAuthorityDao.delOldDoorAuth(model.getCardNum(), model.getDoorNum(),model.getDeviceNum());
			  }
	}



	@Override
	public List<Map> getTimeGroupByDeviceNum(String deviceNum) {
		return peopleAuthorityDao.getTimeGroupByDeviceNum(deviceNum);
	}



	@Override
	public List<Map> getBeCopyCardDoors(String cardNum) {
		return peopleAuthorityDao.getBeCopyCardDoors(cardNum);
	}



	@Override
	public List<Map> getDevicesOnCardNum(String cardNum) {
		return peopleAuthorityDao.getDevicesOnCardNum(cardNum);
	}



	@Override
	public void delAuthByCards(String cards) {
		peopleAuthorityDao.delAuthByCards(cards);
	}



	@Override
	public Grid<Map> getDoorsInfoDynamic(Map map) {
		Grid<Map> grid=new Grid<Map>();
		grid.setRows(peopleAuthorityDao.getDoorsInfoDynamic(map));
		grid.setTotal(peopleAuthorityDao.countDoorsInfoDynamic(map));
		return grid;
	}



	@Override
	public List<Map> getAllCards(List groupList) {
		if(groupList!=null){
			String groupIds=Arrays.toString(groupList.toArray()).substring(1, Arrays.toString(groupList.toArray()).length()-1);
			return peopleAuthorityDao.getAllCards(groupIds);
		}else{
			return new ArrayList<Map>();
		}
	}



	@Override
	public Grid<Map> getDescDoors(String doorNum) {
		Grid<Map> grid=new Grid<Map>();
		grid.setRows(peopleAuthorityDao.getDescDoors(doorNum));
		grid.setTotal(peopleAuthorityDao.countDescDoors(doorNum));
		return grid;
	}



	@Override
	public List<Map> getAuthOfDoor(Map map) {
		return peopleAuthorityDao.getAuthOfDoor(map);
	}


/*
	@Override
	public List<String> getAllTimeGroups() {
		return peopleAuthorityDao.getAllTimeGroups();
	}



	@Override
	public List<String> getAllDoorNum() {
		return peopleAuthorityDao.getAllDoorNum();
	}



	@Override
	public List<String> getAllDeviceNum() {
		return peopleAuthorityDao.getAllDeviceNum();
	}*/



	@Override
	public List<PeopleAuthorityInfoModel> getAuthorityInfoByStaffNum(
			String staffNum) {
		return peopleAuthorityDao.getAuthorityInfoByStaffNum(staffNum);
	}
	
	@Override
	public List<Map> getDeviceDoorList(List<LinkedTreeMap> list) {
		List<Map> list1=new ArrayList<Map>();
		for(LinkedTreeMap map:list){
			list1.add(peopleAuthorityDao.getDeviceDoorObj(map));
		}
		return list1;
	}
	
	@Override
	public Grid getDeviceDoorList(Map map) {
		Grid grid=new Grid();
		List<Map> list1=peopleAuthorityDao.getDeviceDoorObjA(map);
		List<Map> tempL=new ArrayList<Map>();
		for(Map m:list1){
			Map mm=new HashMap();
			mm.putAll(m);
			mm.put("groupNum1",getMjTimeGroup(m.get("deviceNum").toString()));
			tempL.add(mm);
		}
		Integer total=peopleAuthorityDao.countDeviceDoorObjA(map);
		grid.setRows(tempL);
		grid.setTotal(total);
		return grid;
	}



	@Override
	public boolean hasEmp(String deptNumStr) {
		return peopleAuthorityDao.hasEmp(deptNumStr);
	}



	@Override
	public void saveOrganAuth(Map map) {
		peopleAuthorityDao.saveOrganAuth(map);
	}



	@Override
	public void deleteOrganAuth(String deptNum,String deviceNum,String doorNum) {
		Map map =new HashMap();
		map.put("deptNum", deptNum);
		map.put("deviceNum", deviceNum);
		map.put("doorNum", doorNum);
		peopleAuthorityDao.deleteOrganAuth(map);
	}



	@Override
	public List<Map> getOrganAuth(String staffNum) {
		return peopleAuthorityDao.getOrganAuth(staffNum);
	}



	@Override
	public List<Map> getOrganAuthByDept(String deptNum) {
		return peopleAuthorityDao.getOrganAuthByDept(deptNum);
	}



	@Override
	public Map getDeptNumAndHireState(String staffNum) {
		return peopleAuthorityDao.getDeptNumAndHireState(staffNum);
	}



	@Override
	public List<Map> initAuthTab(List<Map> list) {
		List<Map> newList=new ArrayList<Map>();
		for(Map map:list){
			Map m=new HashMap();
			//m.put("groupNum1",getMjTimeGroup(map.get("deviceNum").toString()));
			m.putAll(peopleAuthorityDao.getDeviceDoorObj(map));
			newList.add(m);
			
		}
		return newList;
	}



	@Override
	public void addAuthTasks(List<Map> list,Integer flag) {
		for(Map map:list){
			map.put("flag", flag);
			peopleAuthorityDao.addAuthTask(map);
		}
	}



	@Override
	public List<Map> getAuthTasks() {
		return peopleAuthorityDao.getAuthTasks();
	}



	@Override
	public void updateTryTimes(Map map) {
		peopleAuthorityDao.updateTryTimes(map);
	}



	@Override
	public Integer getTryTimes(Map map) {
		return peopleAuthorityDao.getTryTimes(map);
	}



	@Override
	public void delAuthTask(Map map) {
		peopleAuthorityDao.delAuthTask(map);
	}



	@Override
	public void addAuthTaskHis(Map map) {
		peopleAuthorityDao.addAuthTaskHis(map);
	}



	@Override
	public void addAuthRecord(Map map) {
		peopleAuthorityDao.addAuthRecord(map);
	}



	@Override
	public Map getTask(Map map) {
		return peopleAuthorityDao.getTask(map);
	}



	@Override
	public void delAuthRecord(Map map) {
		peopleAuthorityDao.delAuthRecord(map);
	}



	@Override
	public List<Map> getEmpCards(String staffNum) {
		return peopleAuthorityDao.getEmpCards(staffNum);
	}



	@Override
	public Grid searchDoorSysAuth(Map map) {
			Grid grid=new Grid();
	        List<Map> resultList = peopleAuthorityDao.searchDoorSysAuth(map);
	        grid.setRows(resultList);
	        grid.setTotal(peopleAuthorityDao.countSearchDoorSysAuth(map));
	        return grid;
	}



	@Override
	public List<Map> getMjTimeGroup(String deviceNum) {
		Map map=new HashMap();
		map.put("groupNum",-1);
		map.put("groupName","--不选择--");
		List<Map> list=peopleAuthorityDao.getMjTimeGroup(deviceNum);
		list.add(map);
		return list;
	}



	@Override
	public List<Map> searchAuthDownload(Map map) {
		return peopleAuthorityDao.searchAuthDownload(map);
	}



	@Override
	public boolean devOnLine(String deviceNum) {
		Integer flag=peopleAuthorityDao.devOnLine(deviceNum);
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}



	@Override
	public void updAuth(Map map) {
		peopleAuthorityDao.updAuth(map);
	}



	@Override
	public List<Map> allDevices() {
		return peopleAuthorityDao.allDevices();
	}



	@Override
	public List<Map> getAuthInfo(List<Map> list) {
		List<Map> newList=new ArrayList<Map>();
		for(Map map:list){
			if(peopleAuthorityDao.getAuthInfo(map)!=null){
				newList.add(peopleAuthorityDao.getAuthInfo(map));
			}
		}
		List<Map> newList1=new ArrayList<Map>();
		for(Map m:newList){
			Map mm=new HashMap();
			mm.putAll(m);
			mm.put("groupNum1",getMjTimeGroup(m.get("deviceNum").toString()));
			newList1.add(mm);
		}
		return newList1;
	}



	@Override
	public Grid getInfoOnDevDor(List<Map> list,Integer skip,Integer rows) {
		List<String> staffList=new ArrayList<String>();
		Grid grid=new Grid();
		for(Map map:list){
			List tmpList=peopleAuthorityDao.getStaffNumOnAuth(map);
			staffList=prdStaffList(staffList,tmpList);
		}
		String staffStr=btyStaffList(staffList);
		Map m=new HashMap();
		if(staffStr.length()>0){
			m.put("staffStr",staffStr);
		}else{
			m.put("staffStr","\'"+"\'");
		}
		m.put("devDorList",list);
		m.put("skip", skip);
		m.put("rows", rows);
		List<Map> result=peopleAuthorityDao.getStaffAuthOnDevDor(m);
		Integer count=peopleAuthorityDao.countStaffAuthOnDevDor(m);
		grid.setRows(result);
		grid.setTotal(count);
		return grid;
	}
	
	/**
	 * 去重复的staffNum
	 * by gengji.yang
	 */
	private  List<String> prdStaffList(List<String> staffList,List<Map> tmpList){
		for(Map m:tmpList){
			if(!staffList.contains(m.get("staffNum"))){
				staffList.add((String)m.get("staffNum"));
			}
		}
		return staffList;
	}
	
	/**
	 * 去除重复的deviceNum,
	 * 直接返回需要的格式
	 * '123-abc,'456-abc'
	 */
	public List<String> prdDeviceNumStr(List<Map> list){
		List<String> tmpList=new ArrayList<String>();
		for(Map m:list){
			if(!tmpList.contains(m.get("deviceNum"))){
				tmpList.add((String)m.get("deviceNum"));
			}
		}
		return tmpList;
	}
	
	/**
	 * 去除重复的deviceNum,
	 * 直接返回需要的格式
	 * '123-abc,'456-abc'
	 */
	public List<String> prdDoorNumStr(List<Map> list){
		List<String> tmpList=new ArrayList<String>();
		for(Map m:list){
			if(!tmpList.contains(m.get("doorNum"))){
				tmpList.add((String)m.get("doorNum"));
			}
		}
		return tmpList;
	}
	
	/**
	 * 将staffList处理成特定的格式
	 * eg:'abc-123','abc-1234'
	 * by gengji.yang
	 */
	private  String btyStaffList(List<String> staffList){
		List<String> newList=new ArrayList<String>();
		for(String e:staffList){
			newList.add("\'"+e+"\'");
		}
		String str=newList.toString();
		if(str.length()>2){//[]长度为2
			return str.substring(1,str.length()-1);
		}else{
			return "\'"+"\'";
		}
	}



	@Override
	public Grid getOrgAuth(Map map) {
		List<Map> list=peopleAuthorityDao.getOrgAuth(map);
		Integer total=peopleAuthorityDao.countOrgAuth(map);
		Grid grid=new Grid();
		grid.setRows(list);
		grid.setTotal(total);
		return grid;
	}



	@Override
	public List<Map> getOrgAuthForTree(Map map) {
		return peopleAuthorityDao.getOrgAuthForTree(map);
	}



	@Override
	public List<Map> getSrcOrgAuth(String deptStr) {
		return peopleAuthorityDao.getSrcOrgAuth(deptStr);
	}



	@Override
	public String autoProdStaffNo() {
		Integer bits=peopleAuthorityDao.getBits("staffNoRule");
		List<String> list=peopleAuthorityDao.getBitedStaffNo(bits+"");
		return makeNum(Long.parseLong(numAddAuto(list,bits)==null?(getMax(bits)+1)+"":numAddAuto(list,bits)),bits);
	}
	
	@Override
	public String autoProdDeptNo() {
		Integer bits=peopleAuthorityDao.getBits("deptNoRule");
		List<String> list=peopleAuthorityDao.getBitedDeptNo(bits+"");
		return makeNum(Long.parseLong(numAddAuto(list,bits)==null?(getMax(bits)+1)+"":numAddAuto(list,bits)),bits);
	}
	
	/**
	 *自动+1
	 *by gengji.yang 
	 */
	private String numAddAuto(List<String> list,Integer bits){
		//这里排序
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return new Long((String) o1).compareTo(new Long((String) o2));
  			}
	    });
		String num=null;
		if(list.size()>0 && list.size()<getMax(bits)){
			if(Long.parseLong(list.get(0))-Long.valueOf(1l)>0){
				num="1";
			}else{
				boolean flag=false;//默认不存在断码
				for(int i=0;i<list.size();i++){
					if((i+1)<list.size()&&(Long.parseLong(list.get(i+1))-Long.parseLong(list.get(i))>1)){
						flag=true;//存在断码
						 num=(Long.parseLong(list.get(i))+1)+"";
						 break;
					}
				}
				if(flag==false){//不存在断码
					num=(Long.parseLong(list.get(list.size()-1))+1)+"";
				}
			}
		}else if(list.size()==0){
			num="1";
		}else{
			num=null;
		}
		return num;
	}
	
	/**
	 * 返回指定长度数的最大值
	 * eg 5位数，则99999
	 * by gengji.yang
	 */
	public Long getMax(int bits){
		StringBuilder build=new StringBuilder();
		for(int i=0;i<bits;i++){
			build=build.append("9");
		}
		return Long.parseLong(build.toString());
	}

	/**
	 *返回指定长度的数字，不足长度左补0
	 *by gengji.yang 
	 */
	private String makeNum(Long num,Integer bits){
		String numStr=num+"";
		StringBuilder build=new StringBuilder();
		for(int i=0;i<bits-numStr.length();i++){
			build=build.append("0");
		}
		String result=build.append(numStr).toString();
		if(result.length()==bits){
			return result;
		}else{
			return null;
		}
	}



	@Override
	public void updateTskState(Map map) {
		peopleAuthorityDao.updateTskState(map);
	}



	@Override
	public void quzUpdateTskState(Map map) {
		peopleAuthorityDao.quzUpdateTskState(map);
	}



	@Override
	public void makeDutyByDeptNum(String deptNum,String staffNum,String today) throws Exception {
		Map map=new HashMap();
		map.put("deptNum", deptNum);
		List<Map> deptDutyList=peopleAuthorityDao.getDeptDutyInfos(map);
		if(deptDutyList!=null && deptDutyList.size()>0){
			for(Map m:deptDutyList){
				m.put("staffNum", staffNum);
				DutyUser dutyUser=buildModel(m);
				dutyUserService.insertNewDutyWithoutConflict(staffNum, today, dutyUser);
			}
		}
	}
	
	private DutyUser buildModel(Map map){
		DutyUser model=new DutyUser();
		model.setBegin_time(map.get("beginTime").toString());
		model.setEnd_time(map.get("endTime").toString());
		model.setDuty_id((Integer)map.get("dutyId"));
		model.setDept_num(map.get("deptNum").toString());
		model.setStaff_num(map.get("staffNum").toString());
		return model;
	}



	@Override
	public void chgDutyOnDeptChg(String newDept, String oldDept,String staffNum, String today) throws Exception {
		//获取跨了today 的班次，进行修改区间，eg:由 [a,b] 到 [a,today]
		DutyUser listAmongToday=dutyUserDao.getDutyUserAmongToday(staffNum, today);
		if(listAmongToday!=null){//修改区间
			dutyUserDao.updateDutyUser(fixDutyUser(listAmongToday,today));
		}
		//删除today+1天之后的所有oldDept部门排班
		delDuty(today,oldDept,staffNum);
		//继承newDept部门的排班	
		makeDutyByDeptNum(newDept,staffNum,today);
	}
	
	/**
	 * 返回修改区间后的model
	 * by gengji.yang
	 * @throws Exception 
	 */
	private DutyUser fixDutyUser(DutyUser dutyUser,String today) throws Exception{
		DutyUser model=new DutyUser();
		model=(DutyUser) dutyUser.clone();
		today=today.substring(0,10)+dutyUser.getEnd_time().substring(10, 16);
		model.setEnd_time(today);
		return model;
	}
	
	/**
	 * 删除today+1天之后的所有oldDept部门排班
	 * by gengji.yang
	 */
	private void delDuty(String today,String deptNum,String staffNum){
		Map map=new HashMap();
		map.put("today", today);
		map.put("deptNum", deptNum);
		map.put("staffNum", staffNum);
		peopleAuthorityDao.delVeryDuty(map);
	}



	@Override
	public List<PeopleAuthorityInfoModel> getDelAuthorityInfoByStaffNum(
			String staffNum) {
		return peopleAuthorityDao.getDelAuthorityInfoByStaffNum(staffNum);
	}
	//===========================================================================
	/**
	 * 删除或者新增权限时
	 * 及时修改权限状态
	 * flag 0 新增，1 删除
	 * by gengji.yang
	 */
	private void updateAuthState(int flag,List<Map> authList){
		if(flag==0){//新增 先往权限表 插权限记录 标记好 task_state=00
			addWorker(authList,"00");
		}else{//删除权限 先更新权限表 中 task_state==10
			updateWorker(authList,"10");
		}
	}
	
	/**
	 * 新增权限时
	 * 先插入权限记录，task_state=00
	 */
	private void addWorker(List<Map>authList,String state){
		for(Map map:authList){
			map.put("state",state);
			delAuthRecord(map);
			addAuthRecord(map);
		}
	}
	
	/**
	 * 删除权限前
	 * 更新每一个权限记录的worker
	 * param:state 10->待删除
	 * by gengji.yang
	 */
	private void updateWorker(List<Map> authList,String state){
		for(Map map:authList){
			map.put("state", state);
			updateTskState(map);
		}
	}
	//===========================================================================
	
	
	
	



	@Override
	public void staffLeaveDelAuth(String cardNum) {
		Map map=new HashMap();
		//伪删除梯控权限
		map.put("cardNum", cardNum);
		List<Map> authList=peopleAuthorityDao.getTkCardAuths(map);
		for(Map m:authList){
			m.put("action_flag","2");
			peopleAuthorityDao.addTkAuthTask(m);
		}
	}

	@Resource(name = "departmentGrantServiceImpl")
	private IDepartmentGrantService departmentGrantService;

	@Override
	public void staffBackAuthBack(String cardNum) {
		//恢复梯控权限
		Map map=new HashMap();
		map.put("cardNum", cardNum);
		List<Map> authList=peopleAuthorityDao.getRemainTkAuths(map);
		if(authList.size()>0){
			if(authList.size()>1){//含有人员以及组织的权限记录，需要追加的恢复权限
				String floorList=departmentGrantService.addFloorList3(authList, authList.get(0).get("floor_list").toString()).get("floorStr1").toString();
				Map tmp=authList.get(0);
				tmp.put("floor_list", floorList);
				tmp.put("action_flag","0");
				peopleAuthorityDao.addTkAuthTask(tmp);
			}else{//只含有一种权限
				for(Map m:authList){
					m.put("action_flag","0");
					peopleAuthorityDao.handleTkDelFlag(m);
					peopleAuthorityDao.addTkAuthTask(m);
				}
			}
		}
	}


}
