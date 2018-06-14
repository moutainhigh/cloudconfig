package com.kuangchi.sdd.consumeConsole.mealConsumeReport.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;
import com.kuangchi.sdd.consumeConsole.mealConsumeReport.model.MealConsumeReportModel;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-09-06 
 * @功能描述: 人员充值报表-service
 */
public interface MealConsumeReportService {

	/*获取所有餐次消费报表信息（分页）*/
	public List<MealConsumeReportModel> getMealConsumeReport(Map<String, Object> map);
		
	/*获取所有餐次消费报表信息的总数量*/
	public Integer getMealConsumeReportCount(Map<String, Object> map);

	/*获取所有餐次消费报表信息(导出用)*/
	public List<MealConsumeReportModel> getMealConsumeReportNoLimit(Map<String, Object> map);
	
	/*按照筛选条件统计餐次消费报表信息（分页）*/
	public List<MealConsumeReportModel> countMealConsumeReport(Map<String, Object> map);
	
	/*按照筛选条件统计餐次消费报表信息的总数量*/
	public Integer countMealConsumeReportCount(Map<String, Object> map);
	
	/*按照筛选条件统计餐次消费报表信息(导出用)*/
	public List<MealConsumeReportModel> countMealConsumeReportNoLimit(Map<String, Object> map);
	
	/*获取餐次编号和餐次名称*/
	public List<MealModel> getMeal();
}
