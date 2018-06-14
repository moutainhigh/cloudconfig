package com.kuangchi.sdd.ZigBeeConsole.inter.dao;



import java.util.List;
import java.util.Map;


/**
 * 光子锁对外接口 - dao
 * @author yuman.gao
 */
public interface IInterDao {
	
	/**
	 * 更新电量
	 * @author yuman.gao
	 */
	public boolean updateElectricity(Map<String, Object> map);
	
	/**
	 * 新增开锁记录
	 * @author yuman.gao
	 */
	public boolean addRecord(Map<String, Object> map);
	
	/**
	 * 查询密码是否正确
	 * @author yuman.gao
	 */
	public Integer getDeviceByPas(Map<String, Object> map);
	
	/**
	 * 根据员工编号查询姓名
	 * @author yuman.gao
	 */
	public String getStaffNamebyStaffNum(String staff_num);
	
	/**
	 * 查询设备网关信息
	 * @author yuman.gao
	 */
	public Map<String, Object> getGatewayByDeviceId(String device_id);
	
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
	 * 删除所有光钥匙同步的员工信息
	 * @author yuman.gao
	 */
	public void deleteLightKeyUser();
	
	/**
	 * 删除所有光钥匙同步的卡片信息
	 * @author yuman.gao
	 */
	public void deleteLightKeyCard();
	
	
	/**
	 * 光钥匙同步增加卡片信息
	 * @author yuman.gao
	 */
	public boolean addCardByLightKey(Map<String, Object> map);
	
	/**
	 * 光钥匙同步增加绑卡信息
	 * @author yuman.gao
	 */
	public boolean addBoundCardMap(Map<String, Object> map);
	
	/**
	 * 光钥匙同步更新卡片信息
	 * @author yuman.gao
	 */
	public boolean updateCardByLightKey(Map map);
	
	
	/**
	 * 光钥匙同步冻结卡片
	 * @author yuman.gao
	 */
	public boolean freeCardByLightKey(Map<String, Object> map);
	
	/**
	 * 光钥匙同步解冻卡片
	 * @author yuman.gao
	 */
	public boolean unfreeCardByLightKey(Map<String, Object> map);
	
	/**
	 * 根据远端ID查询员工
	 * @author yuman.gao
	 */
	public List<Map> getStaffByRemoteId(String remote_staff_id);
	
	/**
	 * 根据remote_id删除不存在的员工
	 * @author yuman.gao
	 */
	public boolean delUserByRemoteId(String remote_id);
	
	/**
	 * 根据remote_id删除不存在的卡号
	 * @author yuman.gao
	 */
	public boolean removeLightCard(String card_nums);
	
	/**
	 * 根据remote_id删除不存在的卡号
	 * @author yuman.gao
	 */
	public List<String> getCardByRemoteId(String remote_id);
	
	/**
	 * 更新卡片绑定信息
	 * @author yuman.gao
	 */
	public boolean updateBoundCardByLightKey(Map<String, Object> map);
	
	/**
	 * 删除卡片绑定信息
	 * @author yuman.gao
	 */
	public boolean removeBoundCard(String card_nums);
	
}
