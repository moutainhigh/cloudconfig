package com.kuangchi.sdd.elevatorConsole.times.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.elevatorConsole.times.dao.TimesPageDao;
import com.kuangchi.sdd.elevatorConsole.times.model.TimesPageModel;
import com.kuangchi.sdd.elevatorConsole.times.service.TimesPageService;

@Service("timesPageServiceImpl")
public class TimesPageServiceImpl implements TimesPageService {
	
	@Resource(name="timesPageDaoImpl")
	private TimesPageDao timesPageDao;
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public boolean motifyTimesPage(List<TimesPageModel> list, String create_user) {
		boolean result = false;
		for(TimesPageModel timesPageModel: list){
			result = timesPageDao.motifyTimesPage(timesPageModel);
		}
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "时段组管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", create_user);
		log.put("V_OP_MSG", "修改时段组信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		return result;
	}

	@Override
	public boolean insertTimesPage(List<TimesPageModel> list, String create_user) {
		boolean result = false;
		for(TimesPageModel timesPageModel: list){
			result = timesPageDao.insertTimesPage(timesPageModel);
		}
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "时段组管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", create_user);
		log.put("V_OP_MSG", "新增时段信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		return result;
	}

	@Override
	public Integer getTimesPageCountByTimesGroupNum(String time_group_num) {
		return timesPageDao.getTimesPageCountByTimesGroupNum(time_group_num);
	}

}
