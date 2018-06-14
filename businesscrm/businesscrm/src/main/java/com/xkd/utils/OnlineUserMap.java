package com.xkd.utils;

import java.util.HashMap;
import java.util.Map;

public class OnlineUserMap {
	public static OnlineUserMap instance;

	public static synchronized OnlineUserMap getInstance() {
		if (instance == null) {
			instance = new OnlineUserMap();
		}
		return instance;
	}

	// 定义在线用户
	public Map<String, String> onlineuser = new HashMap<String, String>();

	/**
	 * @return 返回在线用户
	 */
	public Map<String, String> getOnlineUser() {
		return onlineuser;
	}

	/**
	 * 添加在线用户
	 * 
	 * @param userid
	 * @param sessionid
	 */
	public void addOnlineUser(String userName, String sessionid) {
		onlineuser.put(userName, sessionid);
	}

	/**
	 * @param userName
	 * @return 返回SessionId
	 */
	public String getSessionId(String userName) {
		return onlineuser.get(userName);
	}

	/**
	 * @param userName
	 * @return 登录返回TRUE，未登录返回false
	 */
	public boolean isLogin(String userName, String sessionid) {
		if (!onlineuser.containsKey(userName))
			return false;
		return onlineuser.get(userName).equals(sessionid);
	}

	/**
	 * @param userName
	 *            移除退出的用户
	 */
	public void removeUser(String userName) {
		onlineuser.remove(userName);
	}

}
