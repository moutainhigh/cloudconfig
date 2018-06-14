package com.kuangchi.sdd.consumeConsole.consumeGroup.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.consumeConsole.consumeGroup.dao.IConsumeGroupDao;
import com.kuangchi.sdd.consumeConsole.consumeGroup.model.ConsumeGroupModel;
import com.kuangchi.sdd.consumeConsole.consumeGroup.service.IConsumeGroupService;


/**
 * @创建人　: 高育漫
 * @创建时间: 2016-7-27 下午2:48:51
 * @功能描述: 消费组管理-业务实现层
 */
@Transactional
@Service("consumeGroupServiceImpl")
public class ConsumeGroupServiceImpl implements IConsumeGroupService{
	
	@Resource(name = "consumeGroupDaoImpl")
	private IConsumeGroupDao consumeGroupDao;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public List<ConsumeGroupModel> getConsumeGroupByParamPage(Map<String, Object> map) {
		return consumeGroupDao.getConsumeGroupByParamPage(map);
	}
	
	@Override
	public List<ConsumeGroupModel> getConsumeGroupByParam(Map<String, Object> map) {
		return consumeGroupDao.getConsumeGroupByParam(map);
	}

	@Override
	public boolean addConsumeGroup(ConsumeGroupModel consumeGroup, String loginUserName) {
		
		boolean result = consumeGroupDao.addConsumeGroup(consumeGroup);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "消费组管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", loginUserName);
		log.put("V_OP_MSG", "新增消费组");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
		return result;
	}

	@Override
	public boolean modifyConsumeGroup(ConsumeGroupModel consumeGroup,String loginUserName) {
		
		boolean result = consumeGroupDao.modifyConsumeGroup(consumeGroup);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "消费组管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", loginUserName);
		log.put("V_OP_MSG", "修改消费组");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
		return result;
	}

	@Override
	public boolean removeConsumeGroup(String delete_ids, String loginUserName) {
		
		boolean result = consumeGroupDao.removeConsumeGroup(delete_ids);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "消费组管理");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", loginUserName);
		log.put("V_OP_MSG", "删除消费组");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
		return result;
	}

	@Override
	public List<ConsumeGroupModel> getConsumeGroupByNum(String group_num) {
		return consumeGroupDao.getConsumeGroupByNum(group_num);
	}
	
	@Override
	public ConsumeGroupModel getConsumeGroupById(String id) {
		return consumeGroupDao.getConsumeGroupById(id);
	}

	@Override
	public Integer getConsumeGroupByParamCount(Map<String, Object> map) {
		return consumeGroupDao.getConsumeGroupByParamCount(map);
	}

	@Override
	public List<ConsumeGroupModel> getNonDefaultGroup(Map<String, Object> map) {
		return consumeGroupDao.getNonDefaultGroup(map);
	}
	
	@Override
	public boolean ifTypeSameMeal(String old_type_num, String new_type_num){
		try{
			String old_meal = consumeGroupDao.getMealNumByType(old_type_num);
			String new_meal = consumeGroupDao.getMealNumByType(new_type_num);
			if(old_meal.equals(new_meal)){
				return true;
			}else {
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	

}
