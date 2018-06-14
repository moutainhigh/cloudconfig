package com.kuangchi.sdd.ZigBeeConsole.inter.service.impl;




import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.ZigBeeConsole.authorityManager.dao.AuthorityManagerDao;
import com.kuangchi.sdd.ZigBeeConsole.authorityManager.model.AuthorityManagerModel;
import com.kuangchi.sdd.ZigBeeConsole.authorityManager.service.AuthorityManagerService;
import com.kuangchi.sdd.ZigBeeConsole.inter.dao.IInterDao;
import com.kuangchi.sdd.ZigBeeConsole.inter.service.IInterService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;


/**
 * 光子锁对外接口 - service实现类
 * @author yuman.gao
 */
@Transactional
@Service("ZigBeeInterServiceImpl")
public class InterServiceImpl implements IInterService{
	
	@Resource(name = "ZigBeeInterDaoImpl")
	private IInterDao interDao;

	@Resource(name = "authorityManagerDaoImpl")
	private AuthorityManagerDao authorityManagerDao;
	
	@Resource(name = "authorityManagerServiceImpl")
	private AuthorityManagerService authorityManagerService;
	
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;

	@Override
	public boolean updateElectricity(Map<String, Object> map) {

		return interDao.updateElectricity(map);
		
	}
	
	@Override
	public boolean addRecord(Map<String, Object> map, String loginUser) {
		return interDao.addRecord(map);
	}

	@Override
	public String getStaffNamebyStaffNum(String staff_num) {
		return interDao.getStaffNamebyStaffNum(staff_num);
	}
	
	@Override
	public Map<String, Object> getDeviceInfoByDeviceId(String device_id) {
		return interDao.getDeviceInfoByDeviceId(device_id);
	}

	@Override
	public boolean addUserByLightKey(Map<String, Object> map) {
		// 增加员工
		interDao.addUserByLightKey(map);
		// 增加卡片
		interDao.addCardByLightKey(map);
		// 增加绑卡信息
		interDao.addBoundCardMap(map);
		// 自动下发
		List<Map> autoInfos = authorityManagerDao.getAutoTask();
		if(autoInfos != null && autoInfos.size() != 0){
			if("0".equals(autoInfos.get(0).get("auto_flag"))){
				Map<String, Object> autoParam = new HashMap<String, Object>();
				autoParam.put("light_id", map.get("card_num"));
				autoParam.put("flag", "add");
				autoParam.put("create_user", "自动授权");
				authorityManagerService.autoIssuedAuth(autoParam);
			}
		}
		
		return true;
	}

	@Override
	public boolean updateUserByLightKey(Map<String, Object> map) {
		// 更新员工
		interDao.updateUserByLightKey(map);
		// 若卡号改变，则更新卡片
		String user_id = (String) map.get("remote_staff_id");
		Map oldCardInfo = authorityManagerDao.getCardByRemoteId(user_id);
		if(oldCardInfo != null){
			String oldCardNum = (String)oldCardInfo.get("card_num");
			String newCardNum = (String)map.get("card_num");
			if(!oldCardNum.equals(newCardNum)){
				Map<String, Object> cardParam = new HashMap<String, Object>();
				cardParam.put("card_num", newCardNum);
				cardParam.put("card_id", oldCardInfo.get("card_id"));
				interDao.updateCardByLightKey(cardParam);
				
				cardParam.put("remote_staff_id", user_id);
				interDao.updateBoundCardByLightKey(cardParam);
			}
			// 自动下发
			List<Map> autoInfos = authorityManagerDao.getAutoTask();
			if(autoInfos != null && autoInfos.size() != 0){
				if("0".equals(autoInfos.get(0).get("auto_flag"))){
					Map<String, Object> autoParam = new HashMap<String, Object>();
					autoParam.put("flag", "update");
					autoParam.put("light_id", map.get("card_num"));
					autoParam.put("old_light_id", oldCardNum);
					autoParam.put("create_user", "自动授权");
					
					authorityManagerService.autoIssuedAuth(autoParam);
				}
			}
		}
		return true;
	}

	@Override
	public boolean freeUserByLightKey(Map<String, Object> map) {
		// 冻结员工
		interDao.freeUserByLightKey(map);
		// 冻结卡片
		interDao.freeCardByLightKey(map);
		return true;
	}
	
	@Override
	public boolean unfreeUserByLightKey(Map<String, Object> map) {
		// 解冻员工
		interDao.unfreeUserByLightKey(map);
		// 解冻卡片
		interDao.unfreeCardByLightKey(map);
		return true;
	}

	@Override
	public void deleteLightKeyUser(String loginUser) {
		interDao.deleteLightKeyUser();
	}

	
	
	
	@Override
	public boolean synchronizedUser(String server_ip) {

		// 调用接口 查询有光ID的用户
		Map<String, String> propMap =
				  PropertiesToMap.propertyToMap("light_key_interface.properties"); 
		String lightKeyUrl = propMap.get("light_key_url");
		String data = HttpRequest.sendPost(lightKeyUrl+"/app/sync/user/giveUsersByCompanyAndType.do?", "server_ip="+server_ip+"&type="+2);
		
		if(data != null && !"".equals(data)){
			List<Map> staffs = GsonUtil.toBean(data, ArrayList.class);
			
			String existRemoteIds = "";
			for (Map<String, Object> staffParam : staffs) {
				
				// 封装员工信息
				Map<String, Object> staff = new HashMap<String, Object>();
				staff.put("remote_staff_id", new Integer(((Double)staffParam.get("userid")).intValue()).toString());
				staff.put("staff_mobile", staffParam.get("user_phone"));
				staff.put("staff_password", staffParam.get("user_pwd"));
				staff.put("staff_name", staffParam.get("realname"));
				staff.put("staff_img",staffParam.get("photo"));
				
				staff.put("staff_hiredate", staffParam.get("reg_time"));
				staff.put("staff_remark", new Integer(((Double)staffParam.get("companyid")).intValue()).toString());
				staff.put("staff_dept", 2);
				staff.put("staff_state", ((Double)staffParam.get("state")).intValue()==1?2:1);
				
				//　封装卡片信息	 
				staff.put("card_num", staffParam.get("card_id"));
				Calendar cal = Calendar.getInstance();  // 卡片默认有效期为20年
				cal.add(Calendar.YEAR, 20);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String date = df.format(cal.getTime());
				staff.put("card_validity", date);
				
				// 如果该员工已存在，则更新；如果不存在，则新增；
				Integer remoteUserId = ((Double)staffParam.get("userid")).intValue();
				List<Map> existStaff = interDao.getStaffByRemoteId(remoteUserId.toString());
				if(existStaff != null && existStaff.size() != 0){
					updateUserByLightKey(staff);
				} else {
					staff.put("staff_num", UUID.randomUUID().toString());
					staff.put("staff_no", authorityManagerService.getStaffNo());
					addUserByLightKey(staff);
				}
				
				// 当前id放在存在List中，用于删除已不存在的员工；
				Integer remote_id = ((Double)staffParam.get("userid")).intValue();
				existRemoteIds = existRemoteIds + remote_id +",";
			}
			
			existRemoteIds = "".equals(existRemoteIds)?null:existRemoteIds.substring(0, existRemoteIds.length()-1);
			List<String> cardNumList = interDao.getCardByRemoteId(existRemoteIds);
			String cardNums = "";
			for (String cardNum : cardNumList) {
				cardNums = cardNums + cardNum + ",";
			}
			cardNums = "".equals(cardNums)? cardNums : cardNums.substring(0, cardNums.length()-1);
			
			// 删除已不存在的员工
			interDao.delUserByRemoteId(existRemoteIds);
			
			if(!"".equals(cardNums)){
				// 删除卡号
				interDao.removeLightCard(cardNums);
				
				// 删除卡片绑定信息
				interDao.removeBoundCard(cardNums);
				
				// 删除该卡有效权限
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("card_nums", cardNums);
				List<AuthorityManagerModel> auths = authorityManagerService.getAuthorityByCards(map);
				if(auths != null && auths.size() !=0){
					authorityManagerService.delePeopleAuthority(auths);
				}
			}
		}
		
		return true;
	}

	
	@Override
	public boolean updateCompany() {

		Map<String, String> propMap =
				  PropertiesToMap.propertyToMap("light_key_interface.properties"); 
		String lightKeyUrl = propMap.get("light_key_url");
		String data = HttpRequest.sendPost(lightKeyUrl+"/app/sync/company/getAllCompanys.do?", "data="+null);
		
		if(data != null && !"".equals(data)){
			List<Map> companys = GsonUtil.toBean(data, ArrayList.class);
			
			List<Map> timerCompany = authorityManagerDao.getTimerCompany();
			String timerCompanyNum = "";
			if(timerCompany != null && timerCompany.size() != 0){
				timerCompanyNum = timerCompany.get(0).get("company_num").toString();
			}
			
			authorityManagerDao.removeCompany();
			for (Map company : companys) {
				authorityManagerDao.addCompany(company);
			}
			if(!"".equals(timerCompanyNum)){
				authorityManagerDao.setTimerObject(timerCompanyNum);
			}
			
		}
		return true;
	}
}
