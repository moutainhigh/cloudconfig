package com.kuangchi.sdd.ZigBeeConsole.authorityManager.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.ZigBeeConsole.authorityManager.model.AuthorityManagerModel;
import com.kuangchi.sdd.ZigBeeConsole.authorityManager.model.ZigbeeTaskModel;


/**
 * 光子锁权限管理 - dao
 * @author chudan.guo
 */
public interface AuthorityManagerDao {
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-18 
	 * @功能描述: 根据参数查询所有员工[分页]
	 */
	public List<Map> getAllStaffToZigBee(Map<String, Object> map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-18
	 * @功能描述: 根据参数查询员工[总数]
	 */
	public Integer getAllStaffToZigBeeCount(Map<String, Object> map);
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
	
	
	 // 新增权限
//	 public boolean addPeopleAuthority(AuthorityManagerModel model);
	 public boolean addPeopleAuthority(List<AuthorityManagerModel> authList);
	
	 // 查询该设备有效权限总数
	 public Integer selectAuthorityCount(String device_id, String light_id);
	 
	 // 新增授权任务
//	 public boolean insertZigbeeTask(AuthorityManagerModel model);
	 public boolean insertZigbeeTask(List<AuthorityManagerModel> taskList);
	 
	 // 新增删除权限任务
	 public boolean insertDeleteTask(List<AuthorityManagerModel> taskList);
	 
	 // 删除已执行的任务
	 public boolean removeTask(Map<String, Object> map);
	 
	 // 新增历史任务记录
	 public boolean insertHisTask(Map<String, Object> map);
	 
	 // 查询任务尝试次数
	 public Integer getTaskTryTimes(Map<String, Object> map);
	 
	 // 将旧权限更改为无效
	 public boolean removeOldAuthority(Map<String, Object> map);
	 
	 // 更新任务表
	 public void updateTask(Map<String, Object> map);
	 
	 // 按创建时间升序查询下发任务（用于定时执行任务）
	 public List<Map> getZBTasks(int taskCount);
	 
	 
	 // 按创建时间升序查询删除任务（用于定时执行任务）
	 public List<Map> getDelTasks();

	 // 根据卡号查询有效权限
	 public List<AuthorityManagerModel> getAuthorityByCards(Map<String, Object> map);
	 
	 // 根据卡号和设备查询待下载权限
	 public List<AuthorityManagerModel> getWaitAuthorityByCards(Map<String, Object> map);
	 
	 // 使指定卡号所有权限失效（人员同步时用于覆盖旧权限）
	 public boolean removeAllAuthByCard(Map<String, Object> map);
	 
	 /**
	  * @创建人　: chudan.guo
	  * @创建时间: 2016-10-20
	  * @功能描述: 根据设备id查询光子锁设备IP和网关
	  */
	 public Map getZigbeeIpMap(String device_id);
	 
	 /**
	  * @创建人　: chudan.guo
	  * @创建时间: 2016-10-20
	  * @功能描述: 修改权限状态
	  */
	 public void updateAuthorityState(Map map);
	 
	 /**
	  * @创建人　: chudan.guo
	  * @创建时间: 2016-10-20
	  * @功能描述: 批量修改权限状态
	  */
	 public void updateSomeAuthorityState(Map map);
	
	 /**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-24
	 * @功能描述: [根据设备id]查找时限到期的用户
	 */
	public List<AuthorityManagerModel> getExpireAuthority(Map<String, Object> map);
	/**
	  * @创建人　: chudan.guo
	  * @功能描述: 自动下发设置
	  */
	public boolean autoIssuedAuthority(Map<String, Object> map);
	//删除自动下发
	public void deleAutoIssuedAuthority();
	//查找已有的自动下发任务
	public List<Map> getAutoIssuedAuthority();
	//修改自动下发
	/*public boolean updateAutoIssuedAuthority(String device_id);*/
	
	// 查询自动下发信息	by yuman.gao
	public List<Map> getAutoTask();
	
	// 根据光钥匙ID查询对应卡号   by yuman.gao
	public Map<String, Object> getCardByRemoteId(String remote_staff_id);
	
	// 查询所有公司名称	by yuman.gao
	public List<Map> getCompany();
	
	// 查询定时更新公司名称	by yuman.gao
	public List<Map> getTimerCompany();
	
	// 将指定公司设为定时更新对象
	public boolean setTimerObject(String company_num);
	
	// 将其他公司重置为非定时对象
	public boolean setNotTimerObject(String company_num);
	
	// 删除全部公司
	public boolean removeCompany();
	
	// 新增公司
	public boolean addCompany(Map<String, Object> map);
}
