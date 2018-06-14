package com.kuangchi.sdd.elevatorConsole.holiday.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.elevatorConsole.holiday.dao.HolidayDao;
import com.kuangchi.sdd.elevatorConsole.holiday.model.Holiday;

/**
 * 梯控节假日模块
 * 
 * @author yuman.gao
 */
@Repository("elevatorHolidayDaoImpl")
public class HolidayDaoImpl extends BaseDaoImpl<Holiday> implements HolidayDao {

	@Override
	public String getNameSpace() {
		return "common.elevatorHoliday";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<Holiday> getHolidayByParamPage(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList(
				"getElevatorHolidayByParamPage", map);
	}

	@Override
	public int getHolidayByParamCount(Map<String, Object> map) {
		return (Integer) find("getElevatorHolidayByParamCount", map);
	}

	@Override
	public boolean addHoliday(Map<String, Object> map) {
		return insert("addElevatorHoliday", map);
	}

	@Override
	public boolean deleteHolidayById(String holiday_ids) {
		return delete("deleteElevatorHolidayById", holiday_ids);
	}

	@Override
	public boolean modifyHoliday(Map<String, Object> map) {
		return update("modifyElevatorHoliday", map);
	}

	@Override
	public void batchAddHoliday(List<Map> holidayList) {
		SqlMapClient sqlMapClient = getSqlMapClient();
		try {
			sqlMapClient.startBatch();
			sqlMapClient.startTransaction();
			for (int i = 0; i < holidayList.size(); i++) {
				Map holiday = holidayList.get(i);
				holiday.put("send_state", 0);
				sqlMapClient.insert("addElevatorHoliday2", holiday);
			}
			sqlMapClient.commitTransaction();
		} catch (Exception e) {
			try {
				sqlMapClient.getCurrentConnection().rollback();
			} catch (Exception e2) {
			}
			e.printStackTrace();
		}
	}

	@Override
	public Holiday getHolidayByNum(Map<String, Object> map) {
		return (Holiday) this.getSqlMapClientTemplate().queryForObject(
				"getElevatorHolidayByNum", map);
	}

	@Override
	public List<String> getHoliByDevice(String device_num) {
		return this.getSqlMapClientTemplate().queryForList("getHoliByDevice",
				device_num);
	}

	@Override
	public List<String> getAllHoli() {
		return this.getSqlMapClientTemplate().queryForList("getAllHoli", "");
	}

	@Override
	public Integer getSendDateCount(String dateStr) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"getSendDateCount", dateStr);
	}

	@Override
	public void deleteHolidaybyDate(String str) {
		update("deleteHolidaybyDate", str);
	}

	@Override
	public List<String> getHoliByDevice2(String device_num) {
		return this.getSqlMapClientTemplate().queryForList("getHoliByDevice2",
				device_num);
	}

	@Override
	public List<String> getHoliByDevForDel(String device_num, String ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", device_num);
		map.put("ids", ids);
		return this.getSqlMapClientTemplate().queryForList(
				"getHoliByDevForDel", map);
	}

}
