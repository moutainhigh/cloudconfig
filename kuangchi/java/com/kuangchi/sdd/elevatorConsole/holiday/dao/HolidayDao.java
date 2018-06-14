package com.kuangchi.sdd.elevatorConsole.holiday.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.elevatorConsole.holiday.model.Holiday;

/**
 * 梯控节假日模块
 * 
 * @author yuman.gao
 */
public interface HolidayDao {

	/**
	 * 根据条件查询节假日信息 (分页)
	 * 
	 * @author yuman.gao
	 */
	List<Holiday> getHolidayByParamPage(Map<String, Object> map);

	/**
	 * 根据条件查询节假日信息总数
	 * 
	 * @author yuman.gao
	 */
	int getHolidayByParamCount(Map<String, Object> map);

	/**
	 * 新增节假日
	 * 
	 * @author yuman.gao
	 */
	boolean addHoliday(Map<String, Object> map);

	/**
	 * 删除节假日
	 * 
	 * @author yuman.gao
	 */
	boolean deleteHolidayById(String holiday_ids);

	/**
	 * 修改节假日
	 * 
	 * @author yuman.gao
	 */
	boolean modifyHoliday(Map<String, Object> map);

	/**
	 * 批量导入节假日
	 * 
	 * @author yuman.gao
	 */
	public void batchAddHoliday(List<Map> holidayList);

	/**
	 * 根据编号查询节假日
	 * 
	 * @author yuman.gao
	 */
	public Holiday getHolidayByNum(Map<String, Object> map);

	/**
	 * 根据设备编号查询所有节假日
	 * 
	 * @author minting.he
	 * @return
	 */
	public List<String> getHoliByDevice(String device_num);

	/**
	 * 查询所有节假日
	 * 
	 * @author minting.he
	 * @return
	 */
	public List<String> getAllHoli();

	/**
	 * 获取已经下发的节假日
	 * 
	 * @author xuewen.deng
	 */
	public Integer getSendDateCount(String dateStr);

	void deleteHolidaybyDate(String str);

	/**
	 * 根据设备编号查询已经下发的节假日日期
	 * 
	 * @author xuewen.deng
	 * @return
	 */
	public List<String> getHoliByDevice2(String device_num);

	/**
	 * 根据设备编号查询所有节假日（删除用）
	 * 
	 * @author xuewen.deng
	 * @return
	 */
	public List<String> getHoliByDevForDel(String device_num, String ids);

}
