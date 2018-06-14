package com.kuangchi.sdd.ZigBeeConsole.task.service.impl;





import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.ZigBeeConsole.task.dao.ITaskDao;
import com.kuangchi.sdd.ZigBeeConsole.task.service.ITaskService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;


/**
 * 光子锁任务管理 - service实现类
 * @author yuman.gao
 */
@Transactional
@Service("ZigBeeTaskServiceImpl")
public class TaskServiceImpl implements ITaskService{
	
	@Resource(name = "ZigBeeTaskDaoImpl")
	private ITaskDao taskDao;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;

	@Override
	public List<Map> getZBTaskByParamPage(Map<String, Object> param) {
		return taskDao.getZBTaskByParamPage(param);
	}

	@Override
	public Integer getZBTaskByParamCount(Map<String, Object> param) {
		return taskDao.getZBTaskByParamCount(param);
	}

	@Override
	public List<Map> getZBTaskHisByParamPage(Map<String, Object> param) {
		return taskDao.getZBTaskHisByParamPage(param);
	}

	@Override
	public Integer getZBTaskHisByParamCount(Map<String, Object> param) {
		return taskDao.getZBTaskHisByParamCount(param);
	}
	
	@Override
	public List<Map> getAuthorityPage(Map<String, Object> param) {
		return taskDao.getAuthorityPage(param);
	}
	
	@Override
	public Integer getAuthorityPageCount(Map<String, Object> param) {
		return taskDao.getAuthorityPageCount(param);
	}


}
