package com.kuangchi.sdd.ZigBeeConsole.authorityManager.service;



import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.ZigBeeConsole.authorityManager.model.AuthorityManagerModel;


/**
 * 光子锁权限管理 - service
 * @author chudan.guo
 */
public interface AuthorityManagerService {
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-18 
	 * @功能描述: 根据参数查询所有员工记录[分页]
	 */
	public List<Map> getAllStaffToZigBee(Map<String, Object> map);
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-18
	 * @功能描述: 根据参数查询记录[总数]
	 */
	public Integer getAllStaffToZigBeeCount(Map<String, Object> map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-18 
	 * @功能描述: 根据参数查询所有光子锁设备[分页]
	 */
	public List<Map> getZigBeeDevice(Map<String, Object> map);
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-18
	 * @功能描述: 根据参数查询所有光子锁设备[总数]
	 */
	public Integer getZigBeeDeviceCount(Map<String, Object> map);
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-19
	 * @功能描述: 查询有效权限总数
	 */
	 public Integer selectAuthorityCount(String device_id, String light_id);
	 
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-19 
	 * @功能描述: 根据员工工号和房间号查看用户权限[分页]
	 */
	public List<Map> getPeopleAuthority(Map<String, Object> map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-19
	 * @功能描述: 根据员工工号和房间号查看用户权限[总数]
	 */
	public Integer getPeopleAuthorityCount(Map<String, Object> map);
	
	
	 /**
	  * @创建人　: chudan.guo
	  * @创建时间: 2016-10-19
	  * @功能描述: 新增权限
	  */
	 public boolean addPeopleAuthority(List<AuthorityManagerModel> auths);
	
	 /**
	  * @创建人　: chudan.guo
	  * @创建时间: 2016-10-19
	  * @功能描述: 删除权限
	  */
	 public boolean delePeopleAuthority(List<AuthorityManagerModel> auths);
	 
	
	 // 执行删除权限任务（定时删除过期任务）		by yuman.gao
	 public boolean issuedDelTask(AuthorityManagerModel model);
	 
	 // 执行授权任务	by yuman.gao
	 public boolean issuedAddTask(AuthorityManagerModel model);
	
	 // 按创建时间升序查询下发任务（用于定时执行任务）	by yumam.gao
	 public List<Map> getZBTasks(int taskCount);
	 
	 // 按创建时间升序查询删除任务（用于定时执行任务）	by yumam.gao
	 public List<Map> getDelTasks();
	 
	 // 查询过期权限	by yumam.gao
	 public List<AuthorityManagerModel> getOutTimeAuthority(String device_id);
	 
	 // 根据卡号查询权限	by yumam.gao
	 public List<AuthorityManagerModel> getAuthorityByCards(Map<String, Object> map);
	 
	 // 添加删除任务	by yumam.gao
	 public boolean insertDeleteTask(List<AuthorityManagerModel> auths);
	 
	 // 添加下发任务	by yumam.gao
	 public boolean insertZigbeeTask(List<AuthorityManagerModel> auths);
	
	 // 根据卡号和设备查询待下载权限
	 public List<AuthorityManagerModel> getWaitAuthorityByCards(Map<String, Object> map);
	 
	 // 更新权限状态
	 public void updateAuthorityState(Map<String, Object> map);
	 
	 // 批量更新权限状态
	 public void updateSomeAuthorityState(Map<String, Object> map);
	 
	 // 给同步过来的员工自动授权
	 public void autoIssuedAuth(Map<String, Object> map);
	 
	 // 通过编码规则获得工号
	 public String getStaffNo();
	 
	//自动下发设置
	public boolean autoIssuedAuthority(Map<String, Object> map);
	//删除自动下发
	public void deleAutoIssuedAuthority();
	//查找已有的自动下发任务
	public List<Map> getAutoIssuedAuthority();
	
	// 根据设备查询该网关信息
	public Map getZigbeeIpMap(String device_id);
	
	// 查询所有公司名称	by yuman.gao
	public List<Map> getCompany();
	
	// 将指定公司设为定时更新对象	by yuman.gao
	public boolean setTimerObject(String company_num);
	
	// 查询定时更新公司名称	by yuman.gao
	public List<Map> getTimerCompany();
}
