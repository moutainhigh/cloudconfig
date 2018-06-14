package com.kuangchi.sdd.ZigBeeConsole.quartz.service.impl;


import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.ZigBeeConsole.authorityManager.service.AuthorityManagerService;
import com.kuangchi.sdd.ZigBeeConsole.inter.service.IInterService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.baseConsole.card.service.ICardService;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.HttpUtil;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;



@Service("zBUserSynchronQuartz")
public class ZBUserSynchronQuartz {

	@Resource(name = "cardServiceImpl")
	private ICardService cardService;

	@Resource(name="cronServiceImpl")
	private ICronService cronService;
	
	@Resource(name = "ZigBeeInterServiceImpl")
	private IInterService interService;
	
	@Resource(name = "authorityManagerServiceImpl")
	private AuthorityManagerService authorityManagerService;
	
	public final static Semaphore semaphore=new Semaphore(1);
	
	/**
	 * 定时更新员工信息
	 * @author yuman.gao
	 */
	public void updateUserInfo(){
		boolean getResource=false;
		try {
			getResource=semaphore.tryAcquire(2,TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
			 return;
		}
		if(getResource){
			try {
				boolean r = cronService.compareIP();	
				if(r){
//					List<Map> timerCompany = authorityManagerService.getTimerCompany();
//					if(timerCompany != null && timerCompany.size() != 0){
//						String company_num = timerCompany.get(0).get("company_num").toString();
//						User loginUser = (User)HttpUtil.getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
//						if(loginUser != null){
							String server_ip = InetAddress.getLocalHost().getHostAddress().toString();
							interService.synchronizedUser(server_ip);
//						}
//					}
				}
			} catch (Exception e) {
				  e.printStackTrace();
			} finally {
				semaphore.release();	 
			}
		}
	}
	
	/**
	 * 获取光ID并同步到光钥匙
	 * @author yuman.gao
	 */
	public void getLightId(){
		boolean getResource=false;
		try {
			getResource=semaphore.tryAcquire(2,TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
			 return;
		}
		if(getResource){
			try {
				boolean r = cronService.compareIP();	
				if(r){
//					User loginUser = (User)HttpUtil.getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
//					if(loginUser != null){
	//					List<Map> timerCompany = authorityManagerService.getTimerCompany();
	//					if(timerCompany != null && timerCompany.size() != 0){
	//						
							// 调用接口，查询所有无光ID的员工
							Map<String, String> propMap =
									PropertiesToMap.propertyToMap("light_key_interface.properties"); 
							String lightKeyUrl = propMap.get("light_key_url");
							String server_ip = InetAddress.getLocalHost().getHostAddress().toString();
	//						String company_num = timerCompany.get(0).get("company_num").toString();
							
	//						String data = HttpRequest.sendPost(lightKeyUrl+"/app/sync/user/giveUsersByCompanyAndType.do?", "companyid="+company_num+"&type="+1);
							
							String data = HttpRequest.sendPost(lightKeyUrl+"/app/sync/user/giveUsersByCompanyAndType.do?", "server_ip="+server_ip+"&type="+1);
							List<Map> staffsResult = GsonUtil.toBean(data, ArrayList.class);
							if(staffsResult != null && staffsResult.size() != 0){
								
								// 遍历员工集合，调用发卡方法获取光ID
								List<Map> staffsParam = new ArrayList<Map>();
								for (Map<String, Object> staff : staffsResult) {
									
									// 获取卡号
									long cardNum = cardService.getCardNumber("1");
									
									// 新增员工
									staff.put("remote_staff_id", new Integer(((Double)staff.get("userid")).intValue()).toString());
									staff.put("staff_mobile", staff.get("user_phone"));
									staff.put("staff_password", staff.get("user_pwd"));
									staff.put("staff_name", staff.get("realname"));
									staff.put("staff_img",staff.get("photo"));
									staff.put("staff_hiredate", staff.get("reg_time"));
									staff.put("staff_remark", new Integer(((Double)staff.get("companyid")).intValue()).toString());
									staff.put("staff_dept", 2);
									staff.put("staff_num", UUID.randomUUID().toString());
									staff.put("staff_no", authorityManagerService.getStaffNo());
									staff.put("staff_state", 1);
									
									staff.put("card_num", String.valueOf(cardNum));
									Calendar cal = Calendar.getInstance();  // 卡片默认有效期为20年
									cal.add(Calendar.YEAR, 20);
									SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
									String date = df.format(cal.getTime());
									staff.put("card_validity", date);
									
									interService.addUserByLightKey(staff);
									
									
									// 封装新卡号以调更新接口
									Map<String, Object> remoteUser = new HashMap<String, Object>();
									remoteUser.put("user_id", staff.get("userid"));
									remoteUser.put("card_id", cardNum);
									staffsParam.add(remoteUser);
									
								}
								
								// 调用接口，更新远端员工信息
								String msg = HttpRequest.sendPost(lightKeyUrl+"/app/sync/user/updateUserByGotData.do?", "userList="+staffsParam);
								
								
								// 调用有光ID接口，确保数据同步
								if(!"success".equals(msg)){
									interService.synchronizedUser(server_ip);
								}
							}
//						}
					}
					
//				}
			} catch (Exception e) {
				  e.printStackTrace();
			} finally {
				semaphore.release();	 
			}
		}
	}
	
	
	/**
	 * 定时更新公司信息
	 * @author yuman.gao
	 */
	public void updateCompany(){
		boolean getResource=false;
		try {
			getResource=semaphore.tryAcquire(2,TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
			 return;
		}
		if(getResource){
			try {
				boolean r = cronService.compareIP();	
				if(r){
					interService.updateCompany();
				}
			} catch (Exception e) {
				  e.printStackTrace();
			} finally {
				semaphore.release();	 
			}
		}
	}


	
}
