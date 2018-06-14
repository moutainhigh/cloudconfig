package com.kuangchi.sdd.baseConsole.holiday.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.baseConsole.holiday.dao.HolidayDao;
import com.kuangchi.sdd.baseConsole.holiday.model.Holiday;
import com.kuangchi.sdd.baseConsole.holiday.model.HolidayType;
import com.kuangchi.sdd.baseConsole.holiday.model.ObjectTime;
import com.kuangchi.sdd.baseConsole.holiday.service.HolidayService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;

/**
 * @创建人　: 杨金林
 * @创建时间: 2016-3-24 16:03:51
 * @功能描述: 节假日模块-业务实现类
 */
@Transactional
@Service("holidayServiceImpl")
public class HolidayServiceImpl implements HolidayService {

	@Resource(name = "holidayDaoImpl")
	private HolidayDao holidayDao;
	
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;

	@Override
	public boolean addHoliday(Holiday holiday) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = holidayDao.addHoliday(holiday);
		log.put("V_OP_NAME", "节假日信息管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", holiday.getCreate_user());
		log.put("V_OP_MSG", "新增节假日信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}

	@Override
	public boolean addHolidayType(HolidayType holidayType) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = holidayDao.addHolidayType(holidayType);
		log.put("V_OP_NAME", "节假日类型管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", holidayType.getCreate_user());
		log.put("V_OP_MSG", "新增节假日类型");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}

	@Override
	public boolean deleteHolidayById(String holiday_ids,String create_user) {
		boolean result = holidayDao.deleteHolidayById(holiday_ids);
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "节假日信息管理");
	    log.put("V_OP_FUNCTION", "删除");
	    log.put("V_OP_ID", create_user);
	    log.put("V_OP_MSG", "删除节假日信息");
	   
	    if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
	    logDao.addLog(log);
		return result;
	}

	@Override
	public boolean deleteHolidayTypeById(String holiday_type_ids,String create_user) {
		boolean result = holidayDao.deleteHolidayTypeById(holiday_type_ids);
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "节假日类型管理");
	    log.put("V_OP_FUNCTION", "删除");
	    log.put("V_OP_ID", create_user);
	    log.put("V_OP_MSG", "删除节假日类型");
	    if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
	    logDao.addLog(log);
		return result;
	}

	@Override
	public boolean modifyHoliday(Holiday holiday,String login_User) {
		boolean result = holidayDao.modifyHoliday(holiday);
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "节假日信息管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", login_User);
		log.put("V_OP_MSG", "修改节假日信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}

	@Override
	public boolean modifyHolidayType(HolidayType holidayType,String login_User) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = holidayDao.modifyHolidayType(holidayType);
		log.put("V_OP_NAME", "节假日类型管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", login_User);
		log.put("V_OP_MSG", "修改节假日类型");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}

	@Override
	public List<Holiday> getHolidayByParam(Holiday holiday) {
		return holidayDao.getHolidayByParam(holiday);
	}

	@Override
	public List<Holiday> getHolidayByParamPage(Holiday holiday,int page, int size) {
		return holidayDao.getHolidayByParamPage(holiday,page,size);
	}

	@Override
	public int getHolidayByParamCount(Holiday holiday) {
		return holidayDao.getHolidayByParamCount(holiday);
	}

	@Override
	public List<HolidayType> getHolidayTypeByParam(HolidayType holidayType) {
		return holidayDao.getHolidayTypeByParam(holidayType);
	}

	@Override
	public List<HolidayType> getHolidayTypeByParamPage(HolidayType holidayType,int page, int size,String begin,String end) {
		return holidayDao.getHolidayTypeByParamPage(holidayType,page,size,begin,end);
	}

	@Override
	public int getHolidayTypeByParamCount(HolidayType holidayType,String begin, String end) {
		return holidayDao.getHolidayTypeByParamCount(holidayType,begin,end);
	}

	@Override
	public List<ObjectTime> getObjectTimeInfo(String object_nums) {
		return holidayDao.getObjectTimeInfo(object_nums);
	}

	@Override
	public void batchAddHoliday(List<Holiday> holidayList) {
		for (int i = 0; i < holidayList.size(); i++) {
			Holiday holiday=holidayList.get(i);
			holidayDao.addHoliday(holiday);
		}
	}
	
	@Override
	public List<Holiday> getHolidayByType(Holiday holiday) {
		return holidayDao.getHolidayByType(holiday);
	}

	@Override
	public List<HolidayType> getHolidayTypeByName(HolidayType holidayType) {
		return holidayDao.getHolidayTypeByName(holidayType);
	}

	@Override
	public List<String> getAllHolidayNameType() {
		return holidayDao.getAllHolidayNameType();
	}

	@Override
	public Holiday getHolidayById(String holiday_id) {
		return holidayDao.getHolidayById(holiday_id);
	}

	@Override
	public Integer getCrossHoliday(String holiday_begin_date,
			String holiday_end_date, String holiday_id) {
		return holidayDao.getCrossHoliday(holiday_begin_date, holiday_end_date, holiday_id);
	}
	
}
