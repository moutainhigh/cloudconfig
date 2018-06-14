package com.kuangchi.sdd.consumeConsole.meal.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;
import com.kuangchi.sdd.consumeConsole.meal.service.IMealService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-19 上午11:13:39
 * @功能描述: 餐次维护模块-action
 */
@Controller("mealAction")
public class MealAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource(name = "mealServiceImpl")
	private IMealService mealService;
	
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-18 上午9:50:27
	 * @功能描述: 根据参数查询餐次[分页]
	 * @参数描述:
	 */
	public void getMealByParamPage(){
		
		HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		MealModel meal = GsonUtil.toBean(data, MealModel.class);
		
		String meal_name = meal.getMeal_name();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("meal_name", meal_name);
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<MealModel> mealList =  mealService.getMealByParamPage(map);
		Integer mealCount = mealService.getMealByParamCount(map);
		
		Grid<MealModel> grid = new Grid<MealModel>();
		grid.setRows(mealList);
		grid.setTotal(mealCount);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午7:22:35
	 * @功能描述: 修改餐次
	 * @参数描述:
	 */
	public void modifyMeal(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		
		String loginUserName = loginUser.getYhMc();
		String data = request.getParameter("data");
		MealModel meal = GsonUtil.toBean(data, MealModel.class);
		
		boolean modifyResult = mealService.modifyMeal(meal, loginUserName);
		
		if(modifyResult){
			result.setSuccess(true);
			result.setMsg("修改成功");
		} else {
			result.setSuccess(false);
			result.setMsg("修改失败");
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-29 下午4:42:01
	 * @功能描述: 根据id查询餐次（用于修改弹窗数据显示）
	 * @参数描述:
	 */
	public void getMealById(){
		HttpServletRequest request = getHttpServletRequest();
		
		String id = request.getParameter("id");
		MealModel meal = mealService.getMealById(id);
		
		printHttpServletResponse(new Gson().toJson(meal));
		
	}
	
	/**
	 * 查询时间交叉的餐次（判断餐次时间段是否重合）
	 * @author yuman.gao
	 */
	public void getCrossMeal(){
		HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		Map<String,Object> map = GsonUtil.toBean(data,Map.class);
		
		List<String> mealNames = mealService.getCrossMeal(map);
		StringBuffer sb = new StringBuffer();
		for (String mealName : mealNames) {
			sb.append(mealName + ",");
		}
		
		if(sb.length() != 0){
			sb.setLength(sb.length()-1);
			printHttpServletResponse(new Gson().toJson(sb));
		} else {
			printHttpServletResponse(null);
		}
	}
	
}
