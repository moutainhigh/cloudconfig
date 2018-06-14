package com.kuangchi.sdd.consumeConsole.consumeType.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.consumeConsole.consumeType.dao.IConsumeTypeDao;
import com.kuangchi.sdd.consumeConsole.consumeType.model.ConsumeTypeModel;
import com.kuangchi.sdd.consumeConsole.consumeType.service.IConsumeTypeService;
import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;


/**
 * @创建人　: 高育漫
 * @创建时间: 2016-7-27 下午2:48:51
 * @功能描述: 消费类型管理-业务实现层
 */
@Transactional
@Service("consumeTypeServiceImpl")
public class ConsumeTypeServiceImpl implements IConsumeTypeService{
	
	@Resource(name = "consumeTypeDaoImpl")
	private IConsumeTypeDao consumeTypeDao;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public List<ConsumeTypeModel> getConsumeTypeByParamPage(Map<String, Object> map) {
		return consumeTypeDao.getConsumeTypeByParamPage(map);
	}
	
	@Override
	public List<ConsumeTypeModel> getConsumeTypeByParam(Map<String, Object> map) {
		return consumeTypeDao.getConsumeTypeByParam(map);
	}

	@Override
	public boolean addConsumeType(ConsumeTypeModel consumeType, String loginUserName) {
		
		boolean result = consumeTypeDao.addConsumeType(consumeType);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "消费类型管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", loginUserName);
		log.put("V_OP_MSG", "新增消费类型");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
		return result;
	}

	@Override
	public boolean modifyConsumeType(ConsumeTypeModel consumeType,String loginUserName) {
		
		boolean result = consumeTypeDao.modifyConsumeType(consumeType);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "消费类型管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", loginUserName);
		log.put("V_OP_MSG", "修改消费类型");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
		return result;
	}

	@Override
	public boolean removeConsumeType(String delete_ids, String loginUserName) {
		
		boolean result = consumeTypeDao.removeConsumeType(delete_ids);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "消费类型管理");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", loginUserName);
		log.put("V_OP_MSG", "删除消费类型");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
		return result;
	}

	@Override
	public List<ConsumeTypeModel> getConsumeTypeByNum(String consume_type_num) {
		return consumeTypeDao.getConsumeTypeByNum(consume_type_num);
	}
	
	@Override
	public ConsumeTypeModel getConsumeTypeById(String id) {
		return consumeTypeDao.getConsumeTypeById(id);
	}

	@Override
	public Integer getConsumeTypeByParamCount(Map<String, Object> map) {
		return consumeTypeDao.getConsumeTypeByParamCount(map);
	}

	@Override
	public List<MealModel> getMealNum() {
		return consumeTypeDao.getMealNum();
	}
	
	@Override
	public List<ConsumeTypeModel> getConsumeSameType(Map<String, Object> map) {
		try{
			return consumeTypeDao.getConsumeSameType(map);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Integer getConsumeSameTypeCount(Map<String, Object> map) {
		try{
			return consumeTypeDao.getConsumeSameTypeCount(map);
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	

}
