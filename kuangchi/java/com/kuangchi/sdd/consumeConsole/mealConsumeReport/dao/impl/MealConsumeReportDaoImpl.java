package com.kuangchi.sdd.consumeConsole.mealConsumeReport.dao.impl;

import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;
import com.kuangchi.sdd.consumeConsole.mealConsumeReport.dao.MealConsumeReportDao;
import com.kuangchi.sdd.consumeConsole.mealConsumeReport.model.MealConsumeReportModel;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-09-06 
 * @功能描述: 餐次消费报表-dao
 */
@Repository("mealConsumeReportDaoImpl")
public class MealConsumeReportDaoImpl extends BaseDaoImpl<Object> implements MealConsumeReportDao{

	/*获取所有餐次消费报表信息（分页）*/
	@Override
	public List<MealConsumeReportModel> getMealConsumeReport(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getMealConsumeReport", map);
	}

	/*获取所有餐次消费报表信息的总数量*/
	@Override
	public Integer getMealConsumeReportCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getMealConsumeReportCount", map);
	}

	/*获取所有餐次消费报表信息(导出用)*/
	@Override
	public List<MealConsumeReportModel> getMealConsumeReportNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getMealConsumeReportNoLimit", map);
	}

	/*按照筛选条件统计餐次消费报表信息（分页）*/
	@Override
	public List<MealConsumeReportModel> countMealConsumeReport(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countMealConsumeReport", map);
	}

	/*按照筛选条件统计餐次消费报表信息的总数量*/
	@Override
	public Integer countMealConsumeReportCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("countMealConsumeReportCount", map);
	}

	/*按照筛选条件统计餐次消费报表信息(导出用)*/
	@Override
	public List<MealConsumeReportModel> countMealConsumeReportNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countMealConsumeReportNoLimit", map);
	}
	
	/*获取餐次编号和餐次名称*/
	@Override
	public List<MealModel> getMeal() {
		return this.getSqlMapClientTemplate().queryForList("getMeal");
	}

	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
