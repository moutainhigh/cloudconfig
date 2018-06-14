package com.kuangchi.sdd.consumeConsole.meal.service;


import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;


/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-18 下午7:12:06
 * @功能描述: 餐次维护模块-业务层
 */
public interface IMealService {
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-17 上午11:02:29
	 * @功能描述: 根据参数查询餐次[分页]
	 * @参数描述:
	 */
	public List<MealModel> getMealByParamPage(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-17 上午11:03:26
	 * @功能描述: 根据参数查询餐次[总数]
	 * @参数描述:
	 */
	public Integer getMealByParamCount(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午7:06:28
	 * @功能描述: 修改餐次
	 * @参数描述:
	 */
	public boolean modifyMeal(MealModel meal, String loginUserName);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-29 下午4:30:51
	 * @功能描述: 根据id查询餐次（用于修改弹窗数据显示）
	 * @参数描述:
	 */
	public MealModel getMealById(String id);
	
	/**
	 * 查询时间交叉的餐次（判断餐次时间段是否重合）
	 * @author yuman.gao
	 */
	public List<String> getCrossMeal(Map<String, Object> map);
}
