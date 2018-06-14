package com.kuangchi.sdd.consumeConsole.mealConsumeReport.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;
import com.kuangchi.sdd.consumeConsole.mealConsumeReport.dao.MealConsumeReportDao;
import com.kuangchi.sdd.consumeConsole.mealConsumeReport.model.MealConsumeReportModel;
import com.kuangchi.sdd.consumeConsole.mealConsumeReport.service.MealConsumeReportService;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-09-06 
 * @功能描述: 餐次消费报表-业务实现层
 */
@Transactional
@Service("mealConsumeReportServiceImpl")
public class MealConsumeReportServiceImpl implements MealConsumeReportService{

	
	@Resource(name="mealConsumeReportDaoImpl")
	private MealConsumeReportDao mealConsumeReportDao;
	
	
	
	@Override
	public List<MealConsumeReportModel> getMealConsumeReport(
			Map<String, Object> map) {
		return mealConsumeReportDao.getMealConsumeReport(map);
	}

	@Override
	public Integer getMealConsumeReportCount(Map<String, Object> map) {
		return mealConsumeReportDao.getMealConsumeReportCount(map);
	}

	@Override
	public List<MealConsumeReportModel> getMealConsumeReportNoLimit(
			Map<String, Object> map) {
		return mealConsumeReportDao.getMealConsumeReportNoLimit(map);
	}

	@Override
	public List<MealConsumeReportModel> countMealConsumeReport(
			Map<String, Object> map) {
		return mealConsumeReportDao.countMealConsumeReport(map);
	}

	@Override
	public Integer countMealConsumeReportCount(Map<String, Object> map) {
		return mealConsumeReportDao.countMealConsumeReportCount(map);
	}

	@Override
	public List<MealConsumeReportModel> countMealConsumeReportNoLimit(
			Map<String, Object> map) {
		return mealConsumeReportDao.countMealConsumeReportNoLimit(map);
	}

	@Override
	public List<MealModel> getMeal() {
		return mealConsumeReportDao.getMeal();
	}

}
