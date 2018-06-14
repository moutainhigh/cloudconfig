package com.kuangchi.sdd.consumeConsole.meal.dao.impl;



import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.meal.dao.IMealDao;
import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-18 下午7:12:39
 * @功能描述: 餐次维护模块-dao实现层
 */
@Repository("mealDaoImpl")
public class MealDaoImpl extends BaseDaoImpl<Object> implements IMealDao{

	@Override
	public String getNameSpace() {
		return "common.Meal";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<MealModel> getMealByParamPage(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getMealByParamPage", map);
	}
	
	@Override
	public Integer getMealByParamCount(Map<String, Object> map){
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getMealByParamCount", map);
	}

	@Override
	public boolean modifyMeal(MealModel meal) {
		return this.update("modifyMeal", meal);
	}

	@Override
	public MealModel getMealById(String id) {
		return (MealModel) this.getSqlMapClientTemplate().queryForObject("getMealById", id);
	}

	@Override
	public List<String> getCrossMeal(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getCrossMeal", map);
	}
	

}
