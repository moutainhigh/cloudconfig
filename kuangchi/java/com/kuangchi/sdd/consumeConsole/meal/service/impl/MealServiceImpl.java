package com.kuangchi.sdd.consumeConsole.meal.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.consumeConsole.meal.dao.IMealDao;
import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;
import com.kuangchi.sdd.consumeConsole.meal.service.IMealService;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-18 下午7:10:57
 * @功能描述: 餐次维护模块-业务实现层
 */
@Transactional
@Service("mealServiceImpl")
public class MealServiceImpl implements IMealService{
	
	@Resource(name = "mealDaoImpl")
	private IMealDao mealDao;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public List<MealModel> getMealByParamPage(Map<String, Object> map) {
		return mealDao.getMealByParamPage(map);
	}

	@Override
	public Integer getMealByParamCount(Map<String, Object> map) {
		return mealDao.getMealByParamCount(map);
	}

	@Override
	public boolean modifyMeal(MealModel meal, String loginUserName) {
		
		boolean result = mealDao.modifyMeal(meal);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "餐次管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", loginUserName);
		log.put("V_OP_MSG", "修改餐次");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
		return result;
	}

	@Override
	public MealModel getMealById(String id) {
		return mealDao.getMealById(id);
	}

	@Override
	public List<String> getCrossMeal(Map<String, Object> map) {
		return mealDao.getCrossMeal(map);
	}
	
}
