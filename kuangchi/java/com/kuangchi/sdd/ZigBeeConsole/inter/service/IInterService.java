package com.kuangchi.sdd.ZigBeeConsole.inter.service;



import java.util.List;
import java.util.Map;


/**
 * 光子锁对外接口 - service
 * @author yuman.gao
 */
public interface IInterService {
	
	/**
	 * 更新设备电量
	 * @author yuman.gao
	 */
	public boolean updateElectricity(Map<String, Object> map);
	
	/**
	 * 新增开锁记录
	 * @author yuman.gao
	 */
	public boolean addRecord(Map<String, Object> map,String loginUser);
	
	/**
	 * 根据员工编号查询姓名
	 * @author yuman.gao
	 */
	public String getStaffNamebyStaffNum(String staff_num);
	
	/**
	 * 查询设备信息
	 * @author yuman.gao
	 */
	public Map<String, Object> getDeviceInfoByDeviceId(String device_id);
	
	/**
	 * 光钥匙同步增加员工
	 * @author yuman.gao
	 */
	public boolean addUserByLightKey(Map<String, Object> map);
	
	/**
	 * 光钥匙同步修改员工
	 * @author yuman.gao
	 */
	public boolean updateUserByLightKey(Map<String, Object> map);
	
	/**
	 * 光钥匙同步冻结员工
	 * @author yuman.gao
	 */
	public boolean freeUserByLightKey(Map<String, Object> map);
	
	/**
	 * 光钥匙同步解冻员工
	 * @author yuman.gao
	 */
	public boolean unfreeUserByLightKey(Map<String, Object> map);
	
	
	/**
	 * 删除所有光钥匙同步的数据
	 * @author yuman.gao
	 */
	public void deleteLightKeyUser(String loginUser);
	
	
	/**
	 * 同步员工信息
	 * @author yuman.gao
	 */
	public boolean synchronizedUser(String company_num);
	
	/**
	 * 同步更新公司信息
	 * @author yuman.gao
	 */
	public boolean updateCompany();
	
}
