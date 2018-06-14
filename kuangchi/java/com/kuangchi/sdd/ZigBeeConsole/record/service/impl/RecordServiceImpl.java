package com.kuangchi.sdd.ZigBeeConsole.record.service.impl;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.ZigBeeConsole.record.dao.IRecordDao;
import com.kuangchi.sdd.ZigBeeConsole.record.service.IRecordService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;


/**
 * 光子锁记录 - service实现类
 * @author yuman.gao
 */
@Transactional
@Service("ZigBeeRecordServiceImpl")
public class RecordServiceImpl implements IRecordService{
	
	@Resource(name = "ZigBeeRecordDaoImpl")
	private IRecordDao recordDao;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public List<Map> getRecordByParamPage(Map<String, Object> map) {
		List<Map> recordList = recordDao.getRecordByParamPage(map);
		for (Map record : recordList) {
			StringBuffer sb = new StringBuffer();
			if(record.get("staff_name") != null){
				sb.append("员工："+record.get("staff_name") + "  ");
			}
			if(record.get("light_id") != null){
				sb.append("卡号："+record.get("light_id") + "  ");
			}
			if(record.get("staff_mobile") != null){
				sb.append("刷卡手机号："+record.get("staff_mobile") + " ");
			}
			record.put("swipe_info", sb.toString());
		}
		return recordList;
	}

	@Override
	public Integer getRecordByParamCount(Map<String, Object> map) {
		return recordDao.getRecordByParamCount(map);
	}
	
	@Override
	public List<Map> getWarningRecordByParamPage(Map<String, Object> map) {
		List<Map> recordList = recordDao.getWarningRecordByParamPage(map);
		
		return recordList;
	}
	
	@Override
	public Integer getWarningRecordByParamCount(Map<String, Object> map) {
		return recordDao.getWarningRecordByParamCount(map);
	}

	
	
}
