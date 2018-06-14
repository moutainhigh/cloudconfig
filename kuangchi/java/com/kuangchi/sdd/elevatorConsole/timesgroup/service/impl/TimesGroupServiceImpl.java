package com.kuangchi.sdd.elevatorConsole.timesgroup.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.elevatorConsole.timesgroup.dao.TimesGroupDao;
import com.kuangchi.sdd.elevatorConsole.timesgroup.model.TimesGroupModel;
import com.kuangchi.sdd.elevatorConsole.timesgroup.service.TimesGroupService;
@Service("timesGroupServiceImpl")
public class TimesGroupServiceImpl implements TimesGroupService{

	@Resource(name="timesGroupDaoImpl")
	private TimesGroupDao timesGroupDao;
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	
	@Override
	public Grid<TimesGroupModel> getTimesGroupPage(Map<String, Integer> map) {
		Grid<TimesGroupModel> grid = new Grid<TimesGroupModel>();
		List<TimesGroupModel> list = timesGroupDao.getTimesGroupPageList(map);
		Integer count = timesGroupDao.getTimesGroupPageCount(map);
		grid.setRows(list);
		if(null!=list){
			grid.setTotal(count);
		}else{
			grid.setTotal(0);
		}
		return grid;
	}

	@Override
	public boolean insertTimesGroup(Map map,String create_user) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = timesGroupDao.insertTimesGroup(map);
		log.put("V_OP_NAME", "时段组管理管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", create_user);
		log.put("V_OP_MSG", "新增时段组信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}

	@Override
	public Integer checkTimeGroupNumUnique(String time_group_num) {
		return timesGroupDao.checkTimeGroupNumUnique(time_group_num);
	}

}
