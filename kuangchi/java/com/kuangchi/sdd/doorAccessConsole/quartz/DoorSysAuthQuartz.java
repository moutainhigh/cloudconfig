package com.kuangchi.sdd.doorAccessConsole.quartz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.doorAccessConsole.authority.service.PeopleAuthorityInfoService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;

public class DoorSysAuthQuartz {
	@Resource(name = "cronServiceImpl")
	private ICronService cronService;

	static Semaphore semaphoreA = new Semaphore(1);

	PeopleAuthorityInfoService peopleAuthorityInfoService;

	public PeopleAuthorityInfoService getPeopleAuthorityInfoService() {
		return peopleAuthorityInfoService;
	}

	public void setPeopleAuthorityInfoService(
			PeopleAuthorityInfoService peopleAuthorityInfoService) {
		this.peopleAuthorityInfoService = peopleAuthorityInfoService;
	}

	MjCommService mjCommService;
	
	public MjCommService getMjCommService() {
		return mjCommService;
	}

	public void setMjCommService(MjCommService mjCommService) {
		this.mjCommService = mjCommService;
	}

	/**
	 * 执行授权任务表 每2秒取10条任务 by gengji.yang
	 */
	public synchronized void excuteAuthTasks() {
		boolean getResource = false;
		try {
			getResource = semaphoreA.tryAcquire(1, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		try {
			if (getResource) {
				boolean r = cronService.compareIP();
				if (r) {

					List<Map> list = peopleAuthorityInfoService.getAuthTasks();
					for (Map map : list) {
						if ((Integer) map.get("flag") == 0) {// 添加权限
							addAuth(map);
						} else if((Integer) map.get("flag") == 2){//黑名单，设备删除权限，权限表假删除
							updAuth(map);
						}else {// 删除权限	
							delAuth(map);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			semaphoreA.release();
		}
	}

	/**
	 * 添加权限 by gengji.yang
	 */
	public synchronized void addAuth(Map m) {
		Integer b=getTryTimes(m);
		if (getTryTimes(m) < 3) {
			addTryTimes(m);
			boolean isSuccess = true;
		/*	Map<String, String> map = PropertiesToMap
					.propertyToMap("comm_interface.properties");*/
			String deviceNum=mjCommService.getTkDevNumByMac((String) m.get("deviceMac"));
/*			String delOldLimUrl ="http://localhost:8080/comm/"
					+ "gateLimit/delGateLimit.do?";*/
/*			String gateLimUrl = "http://localhost:8080/comm/"
					+ "gateLimit/setGateLimit.do?";*/
			
			
	/*		String delOldLimUrl = mjCommService.getMjCommUrl(deviceNum)
					+ "gateLimit/delGateLimit.do?";*/
			
			String gateLimUrl = mjCommService.getMjCommUrl(deviceNum)
					+ "gateLimit/setGateLimit.do?";
			// 准备参数9
			String mac = (String) m.get("deviceMac");
			String deviceType = (String) m.get("deviceType");
			String gateId = (String) m.get("doorNum");
			String s = (String) m.get("cardNum");
			Integer a = Integer.parseInt(s);
			String cardId = a.toHexString(a);
			String start = (String) m.get("startTime");
			String end = (String) m.get("endTime");
			String group = null;
			if (!"".equals((String) m.get("groupNum"))
					&& null != (String) m.get("groupNum")) {
				group = Integer.toHexString(Integer.parseInt((String) m
						.get("groupNum")));
			}
			// 发送请求
//			String delOldLimStr = HttpRequest.sendPost(delOldLimUrl, "mac="
//					+ mac + "&cardId=" + cardId + "&gateId=" + gateId
//					+ "&device_type=" + deviceType);// {"result_code":"1","result_msg":"删除门禁权限失败"}
			String gateLimStr = HttpRequest.sendPost(gateLimUrl, "mac=" + mac
					+ "&cardId=" + cardId + "&gateId=" + gateId + "&start="
					+ start + "&end=" + end + "&group=" + group
					+ "&device_type=" + deviceType);
			// 处理响应
//			Map delOldLimMap = GsonUtil.toBean(delOldLimStr, HashMap.class);
			Map gateLimMap = GsonUtil.toBean(gateLimStr, HashMap.class);
			if (/*delOldLimMap != null && */gateLimMap != null) {
				if (/*"1".equals(delOldLimMap.get("result_code"))
						||*/ "1".equals(gateLimMap.get("result_code"))) {
					isSuccess = false;
				}
			} else {
				isSuccess = false;
			}
			if (isSuccess) {
				// 记录任务成功历史
				makeTaskHis(m, 0, 0, getTryTimes(m));
				// 删除任务
				delTask(m);
				//更新task_state 02:新增权限失败 
				updateTskSte(m,"01");
			} else {
				// 判断次数是否大于2
				if (getTryTimes(m) >= 3) {// 大于2
					// 停止下发权限，认定下发权限失败，记录任务失败历史
					makeTaskHis(m, 0, 1, getTryTimes(m));
					// 删除任务
					delTask(m);
					//更新task_state 02:新增权限失败
					updateTskSte(m,"02");
				}
			}
		}/*else{
			// 停止下发权限，认定下发权限失败，记录任务失败历史
			makeTaskHis(m, 0, 1, getTryTimes(m));
			// 删除任务
			delTask(m);
			//更新task_state 02:新增权限失败
			updateTskSte(m,"02");
		}*/
	}

	/**
	 * 删除权限 by gengji.yang
	 */
	public synchronized void delAuth(Map m) {
		if (getTryTimes(m) < 3) {
			addTryTimes(m);
			boolean isSuccess = true;
		/*	Map<String, String> map = PropertiesToMap
					.propertyToMap("comm_interface.properties");*/
			String deviceNum=mjCommService.getTkDevNumByMac((String) m.get("deviceMac"));
/*			String delOldLimUrl = "http://localhost:8080/comm/"
					+ "gateLimit/delGateLimit.do?";*/
			
			String delOldLimUrl = mjCommService.getMjCommUrl(deviceNum)
					+ "gateLimit/delGateLimit.do?";
			// 准备参数
			String mac = (String) m.get("deviceMac");
			String deviceType = (String) m.get("deviceType");
			String gateId = (String) m.get("doorNum");
			String s = (String) m.get("cardNum");
			Integer a = Integer.parseInt(s);
			String cardId = a.toHexString(a);
			// 发送请求
			String delOldLimStr = HttpRequest.sendPost(delOldLimUrl, "mac="
					+ mac + "&cardId=" + cardId + "&gateId=" + gateId
					+ "&device_type=" + deviceType);// {"result_code":"1","result_msg":"删除门禁权限失败"}
			// 处理响应
			Map delOldLimMap = GsonUtil.toBean(delOldLimStr, HashMap.class);
			if (delOldLimMap != null) {
				if ("1".equals(delOldLimMap.get("result_code"))) {
					isSuccess = false;
				}
			} else {
				isSuccess = false;
			}
			if (isSuccess) {
				// 记录任务成功历史
				makeTaskHis(m, 1, 0, getTryTimes(m));
				// 删除任务
				delTask(m);
				// 删除权限表
				delAuthRecord(m);
			} else {
				// 判断次数是否大于2
				if (getTryTimes(m) >= 3) {// 大于2
					// 停止删除权限，认定删除权限失败，记录删除任务失败历史
					makeTaskHis(m, 0, 1, getTryTimes(m));
					// 删除任务
					delTask(m);
					//++更新权限状态  task_state=12,删除失败
					updateTskSte(m,"12");
				}
			}
		}/*else{
			// 停止删除权限，认定删除权限失败，记录删除任务失败历史
			makeTaskHis(m, 0, 1, getTryTimes(m));
			// 删除任务
			delTask(m);
			//++更新权限状态  task_state=12,删除失败
			updateTskSte(m,"12");
		}*/
	}
	
	/**
	 *  更新权限 by gengji.yang
	 */
	public synchronized void updAuth(Map m) {
		if (getTryTimes(m) < 3) {
			addTryTimes(m);
			boolean isSuccess = true;
			/*Map<String, String> map = PropertiesToMap
					.propertyToMap("comm_interface.properties");*/
			String deviceNum=mjCommService.getTkDevNumByMac((String) m.get("deviceMac"));
			String delOldLimUrl = mjCommService.getMjCommUrl(deviceNum)
					+ "gateLimit/delGateLimit.do?";
			// 准备参数
			String mac = (String) m.get("deviceMac");
			String deviceType = (String) m.get("deviceType");
			String gateId = (String) m.get("doorNum");
			String s = (String) m.get("cardNum");
			Integer a = Integer.parseInt(s);
			String cardId = a.toHexString(a);
			// 发送请求
			String delOldLimStr = HttpRequest.sendPost(delOldLimUrl, "mac="
					+ mac + "&cardId=" + cardId + "&gateId=" + gateId
					+ "&device_type=" + deviceType);// {"result_code":"1","result_msg":"删除门禁权限失败"}
			// 处理响应
			Map delOldLimMap = GsonUtil.toBean(delOldLimStr, HashMap.class);
			if (delOldLimMap != null) {
				if ("1".equals(delOldLimMap.get("result_code"))) {
					isSuccess = false;
				}
			} else {
				isSuccess = false;
			}
			if (isSuccess) {
				// 记录任务成功历史
				makeTaskHis(m, 1, 0, getTryTimes(m));
				// 删除任务
				delTask(m);
				// 删除权限表
				updAuthRecord(m);
			} else {
				// 判断次数是否大于2
				if (getTryTimes(m) >= 3) {// 大于2
					// 停止删除权限，认定删除权限失败，记录删除任务失败历史
					makeTaskHis(m, 0, 1, getTryTimes(m));
					// 删除任务
					delTask(m);
					//++更新权限状态  task_state=12,删除失败
					updateTskSte(m,"12");
				}
			}
		}/*else{
			// 停止删除权限，认定删除权限失败，记录删除任务失败历史
			makeTaskHis(m, 0, 1, getTryTimes(m));
			// 删除任务
			delTask(m);
			//++更新权限状态  task_state=12,删除失败
			updateTskSte(m,"12");
		}*/
	}

	/**
	 * 获取单个授权任务信息 by gengji.yang
	 */
	public Map getTask(Map map) {
		return peopleAuthorityInfoService.getTask(map);
	}

	/**
	 * 尝试次数+1 by gengji.yang
	 */
	public void addTryTimes(Map map) {
		peopleAuthorityInfoService.updateTryTimes(map);
	}

	/**
	 * 获取尝试次数 by gengji.yang
	 */
	public Integer getTryTimes(Map map) {
		return peopleAuthorityInfoService.getTryTimes(map);
	}

	/**
	 * 删除任务 by gengji.yang
	 */
	public void delTask(Map map) {
		peopleAuthorityInfoService.delAuthTask(map);
	}

	/**
	 * 记录任务执行历史 actionFlag 0:授权;1:删除权限 resultFlag 0:成功;1:失败 by gengji.yang
	 */
	public void makeTaskHis(Map map, Integer actionFlag, Integer resultFlag,
			Integer tryTimes) {
		map.put("actionFlag", actionFlag);
		map.put("resultFlag", resultFlag);
		map.put("tryTimes", tryTimes);
		peopleAuthorityInfoService.addAuthTaskHis(map);
	}

	/**
	 * 插入权限表 by gengji.yang
	 */
	public void addAuthRecord(Map map) {
		peopleAuthorityInfoService.delAuthRecord(map);
		peopleAuthorityInfoService.addAuthRecord(map);
	}

	/**
	 * 删除权限表 by gengji.yang
	 */
	public void delAuthRecord(Map map) {
		peopleAuthorityInfoService.delAuthRecord(map);
	}
	
	/**
	 * 更新权限表 by gengji.yang
	 */
	public void updAuthRecord(Map map){
		peopleAuthorityInfoService.updAuth(map);
	}
	
	/**
	 * 更新权限
	 */
	public void updateTskSte(Map map,String state){
		map.put("state", state);
		peopleAuthorityInfoService.quzUpdateTskState(map);
	}
}
