package com.kuangchi.sdd.elevatorConsole.holiday.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.elevatorConsole.holiday.model.Holiday;

/**
 * 梯控节假日
 */
public interface HolidayService {

	/**
	 * 根据条件查询节假日信息 (分页)
	 * 
	 * @author xuewen.deng
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
	 * @author xuewen.deng
	 */
	boolean addHoliday(Map<String, Object> map, String loginUser);

	/**
	 * 删除节假日
	 * 
	 * @author yuman.gao
	 */
	boolean deleteHolidayById(String holiday_ids, String loginUser);

	/**
	 * 修改节假日
	 * 
	 * @author xuewen.deng
	 */
	boolean modifyHoliday(Map<String, Object> map, String loginUser);

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
	 * 查询节假日
	 * 
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public List<String> getHoliByDevice(String device_num);

	/**
	 * 查看节假日日期是否存在
	 * 
	 * @author xuewen.deng
	 * @param dateStr
	 * @return
	 */
	public boolean isExistHoli(String device_num, String dateStr);

	/**
	 * 判断节假日是否已经下发
	 * 
	 * @author xuewen.deng
	 * @return
	 */

	public boolean isSend(String dateStr);

	/**
	 * 根据设备编号查询已经下发的节假日日期
	 * 
	 * @author xuewen.deng
	 * @param device_num
	 * @return
	 */
	public List<String> getHoliByDevice2(String device_num);

	/**
	 * 删除节假日2
	 * 
	 * @author xuewen.deng
	 */
	boolean deleteHolidayById2(String device_num, String holiday_ids,
			String loginUser);
}
