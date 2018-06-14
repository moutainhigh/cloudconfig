package com.kuangchi.sdd.baseConsole.list.blacklist.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.card.model.ResultMsg;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.device.model.TimeResultMsg;
import com.kuangchi.sdd.baseConsole.devicePosition.dao.DevicePositionDao;
import com.kuangchi.sdd.baseConsole.holiday.dao.HolidayDao;
import com.kuangchi.sdd.baseConsole.list.blacklist.dao.BlackListDao;
import com.kuangchi.sdd.baseConsole.list.blacklist.model.BlackList;
import com.kuangchi.sdd.baseConsole.list.blacklist.service.BlackListService;
import com.kuangchi.sdd.baseConsole.list.whitelist.model.WhiteList;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.employee.model.BoundCard;
import com.kuangchi.sdd.doorAccessConsole.authority.dao.PeopleAuthorityInfoDao;
import com.kuangchi.sdd.doorAccessConsole.authority.model.PeopleAuthorityInfoModel;
import com.kuangchi.sdd.doorAccessConsole.authority.service.PeopleAuthorityInfoService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
@Service("blackListServiceImpl")
public class BlackListServiceImpl implements BlackListService{

	@Resource(name="peopleAuthorityDao")	
	PeopleAuthorityInfoDao peopleAuthorityDao;

	
	@Resource(name="peopleAuthorityService")	
	PeopleAuthorityInfoService peopleAuthorityService;
	
	@Autowired
	PeopleAuthorityInfoService peopleAuthorityInfoService;
	
	@Resource(name ="devicePositionDaoImpl")
	private DevicePositionDao devicePositionDao;
	
	@Resource(name = "blackListDaoImpl")
	BlackListDao blackListDao;
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Resource(name = "mjCommServiceImpl")
	private MjCommService mjCommService;
	
	
	
	/**
	 * 删除用户卡权限
	 */
	public void deleteAutorityCardByStaffNum(List<PeopleAuthorityInfoModel> authorityList){
	
		List<Map> list = new ArrayList<Map>();
		Integer flag = 2;
		for(PeopleAuthorityInfoModel authorityModel:authorityList){
			Map<String, String> map = new HashMap<String, String>();
			map.put("cardNum", authorityModel.getCardNum());
			map.put("doorNum", authorityModel.getDoorNum());
			map.put("deviceNum", authorityModel.getDeviceNum());
			map.put("id", authorityModel.getAuthorityId());
			DeviceInfo device = devicePositionDao.getDeviceByDeviceNum(authorityModel.getDeviceNum());
			map.put("deviceMac", device.getDevice_mac());
			map.put("deviceType", device.getDevice_type());
			map.put("startTime",authorityModel.getValidStartTime());
			map.put("endTime",authorityModel.getValidEndTime());
			map.put("groupNum",device.getDevice_group_num());
			list.add(map);
		}
		updateAuthState(1,list);
		peopleAuthorityService.addAuthTasks(list, flag);
		/*for(int i=0;i<list.size();i++){
			blackListDao.insertDelAuthorityInfo(list.get(i));
		}*/
		}
		
	
	public void updateAuthState(int flag,List<Map> authList){
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
	public void addWorker(List<Map>authList,String state){
		for(Map map:authList){
			map.put("state",state);
			peopleAuthorityInfoService.delAuthRecord(map);
			peopleAuthorityInfoService.addAuthRecord(map);
		}
	}
	
	
	public void updateWorker(List<Map> authList,String state){
		for(Map map:authList){
			map.put("state", state);
			peopleAuthorityInfoService.updateTskState(map);
		}
	}
	
	/*	 String cardNum =Integer.toHexString(Integer.parseInt(authorityModel.getCardNum())) ;
	 String doorNum = authorityModel.getDoorNum();
	 String deviceNum = authorityModel.getDeviceNum();
	 DeviceInfo device = devicePositionDao.getDeviceByDeviceNum(deviceNum);
	 Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");
	 String delAthorityUrl=mjCommService.getMjCommUrl(deviceNum)+"gateLimit/delGateLimit.do?";
	 String str = HttpRequest.sendPost(delAthorityUrl, "gateId="+doorNum+"&cardId="+cardNum+"&device_type="+device.getDevice_type()+"&mac="+device.getDevice_mac());
	 if(!"".equals(str) && null != str){
		 result = GsonUtil.toBean(str, ResultMsg.class);
		 if("0".equals(result.getResult_code())){
			 result.setResult_msg("移入黑名单成功");
		 }else{
			 result.setResult_msg("移入黑名单失败");
			 break;
		 }
	 }else{
			result.setResult_msg("移入黑名单失败,连接异常");
			result.setResult_code("1");
			break;
		}
	}	*/

		@Override
	public boolean addBlackList(BoundCard boundCard,String yhDm,String description, String create_user) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = false;
		BlackList blackList=new BlackList();
		String card_num=boundCard.getCard_num();
		blackList.setCard_num(card_num);
		blackList.setStaff_num(yhDm);
		blackList.setCreate_user(create_user);
		blackList.setDescription(description);
		Map map = new HashMap();
		map.put("yhDm", yhDm);
		map.put("card_num",	card_num);
		/*Integer count = blackListDao.getBlackListByStaffNumAndCardNum(yhDm,card_num);*/
		Integer count = blackListDao.getBlackListByStaffNumAndCardNum(map);//校验是否存在数据库
		if(count>0){
			result =  blackListDao.updateBlackListByStaffNumAndCardNum(map);//是，则更改标志位
		}else{
			result = blackListDao.addBlackList(blackList);//不存在，新增
		}
		log.put("V_OP_NAME", "移入黑名单");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID",create_user);
		log.put("V_OP_MSG", "移入黑名单");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
			
		}
		logDao.addLog(log);
		return result;
		}
		//员工没有绑定卡片
		/*Integer count = blackListDao.getBlackListByStaffNum(yhDm);
		blackList.setCard_num("");
		blackList.setStaff_num(yhDm);
		blackList.setCreate_user(create_user);
		if(count>0){//黑名单表中已存在该数据，标示位为1
			result =  blackListDao.updateBlackListByStaffNum(yhDm);
		}else{//黑名单表中不存在该数据
			result = blackListDao.addBlackList(blackList);
		}
*/

		@Override
		public boolean addBlackListNoBoundCard(String yhDm,String description, String create_user) {
			Map<String, String> log = new HashMap<String, String>();
			boolean result = false;
			BlackList blackList=new BlackList();
			blackList.setStaff_num(yhDm);
			blackList.setCard_num("");
			blackList.setCreate_user(create_user);
			blackList.setDescription(description);
			Integer count = blackListDao.getBlackListByStaffNum(yhDm);
			if(count>0){//黑名单表中已存在该数据，标示位为1
				result =  blackListDao.updateBlackListByStaffNum(yhDm);
			}else{//黑名单表中不存在该数据
				result = blackListDao.addBlackList(blackList);
			}
			log.put("V_OP_NAME", "移入黑名单");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID",create_user);
			log.put("V_OP_MSG", "移入黑名单");
			if(result){
				log.put("V_OP_TYPE", "业务");
			}else{
				log.put("V_OP_TYPE", "异常");
				
			}
			logDao.addLog(log);
			return result;
		}
	
	@Override
	public void insertAutorityCardByStaffNum(List<PeopleAuthorityInfoModel> authorityList)	{
		List<Map> list = new ArrayList<Map>();
		Integer flag = 0;
		for(PeopleAuthorityInfoModel authorityModel:authorityList){
			Map<String, String> map = new HashMap<String, String>();
			map.put("cardNum", authorityModel.getCardNum());
			map.put("doorNum", authorityModel.getDoorNum());
			map.put("deviceNum", authorityModel.getDeviceNum());
			map.put("id", authorityModel.getAuthorityId());
			DeviceInfo device = devicePositionDao.getDeviceByDeviceNum(authorityModel.getDeviceNum());
			map.put("deviceMac", device.getDevice_mac());
			map.put("deviceType", device.getDevice_type());
			map.put("startTime",authorityModel.getValidStartTime());
			map.put("endTime",authorityModel.getValidEndTime());
			map.put("groupNum",device.getDevice_group_num());
			list.add(map);
		}
		updateAuthState(0,list);
		peopleAuthorityService.addAuthTasks(list, flag);
		}
/*		String cardNum =Integer.toHexString(Integer.parseInt(authorityModel.getCardNum())) ;
		String doorNum = authorityModel.getDoorNum();
		String startTime = authorityModel.getValidStartTime();
		String endTime = authorityModel.getValidEndTime();
		String deviceNum = authorityModel.getDeviceNum();
		String timeGroupNum = authorityModel.getTimeGroupNum();
		String url = mjCommService.getMjCommUrl(deviceNum);
		DeviceInfo device = devicePositionDao.getDeviceByDeviceNum(deviceNum);
		String getGateLimit = HttpRequest.sendPost(url+ "gateLimit/setGateLimit.do?", "mac="+ device.getDevice_mac() +"&device_type=" + device.getDevice_type() 
				+"&gateId="+doorNum +"&cardId="+cardNum +"&start="+startTime +"&end="+endTime +"&group="+timeGroupNum);
		if(!"".equals(getGateLimit) && null != getGateLimit){
			result = GsonUtil.toBean(getGateLimit, ResultMsg.class);
			if("0".equals(result.getResult_code())){
				result.setResult_msg("移出黑名单成功");
			}else{
				result.setResult_msg("移出黑名单失败");
				break;
			}
		}else{
			result.setResult_msg("移出黑名单失败,连接异常");
			result.setResult_code("1");
			break;
		}
	}
	return result;
*/	@Override	
	public boolean deleteBlackListByStaffNum(String staff_num,String create_user){
		boolean result = false;
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "移出黑名单");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID",create_user);
		log.put("V_OP_MSG", "移出黑名单");
		result = blackListDao.deleteBlackListByStaffNum(staff_num);
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
			
		}
		logDao.addLog(log);
		return result;
	}
		

	@Override
	public List<BlackList> getBlackListByParam(BlackList blackList) {
		return blackListDao.getBlackListByParam(blackList);
	}

	@Override
	public Grid<BlackList> getBlackListByParamPage(BlackList blackList) {
		Grid<BlackList> grid = new Grid<BlackList>();
        List<BlackList> resultList = blackListDao.getBlackListByParamPage(blackList);
        grid.setRows(resultList);
        if (null != resultList) {
            grid.setTotal(blackListDao.getBlackListByParamCount(blackList));
        } else {
            grid.setTotal(0);
        }
        return grid;
	}

	@Override
	public boolean deleteAutorityByCardNum(String cardNum) {
		return blackListDao.deleteAutorityByCardNum(cardNum);
	}


	



}
