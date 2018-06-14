package com.kuangchi.sdd.consumeConsole.consumeType.action;

import java.util.ArrayList;
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
import com.kuangchi.sdd.consumeConsole.consumeGroup.model.ConsumeGroupModel;
import com.kuangchi.sdd.consumeConsole.consumeGroup.service.IConsumeGroupService;
import com.kuangchi.sdd.consumeConsole.consumeType.model.ConsumeTypeModel;
import com.kuangchi.sdd.consumeConsole.consumeType.service.IConsumeTypeService;
import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;


/**
 * @创建人　: 高育漫
 * @创建时间: 2016-7-27 下午2:49:36
 * @功能描述: 消费类型管理-action
 */
@Controller("consumeTypeAction")
public class ConsumeTypeAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource(name = "consumeTypeServiceImpl")
	private IConsumeTypeService consumeTypeService;
	
	@Resource(name = "consumeGroupServiceImpl")
	private IConsumeGroupService consumeGroupService;
	
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午2:17:10
	 * @功能描述: 跳转到消费类型管理页面
	 * @参数描述:
	 */
	public String toConsumeTypePage(){
		return "success";
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午2:58:48
	 * @功能描述: 根据参数查询消费类型[分页]
	 * @参数描述:
	 */
	public void getConsumeTypeByParamPage(){
		
		HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		ConsumeTypeModel consumeType = GsonUtil.toBean(data, ConsumeTypeModel.class);
		String consume_type_name = consumeType.getConsume_type_name();
		String meal_num=consumeType.getMeal_num();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("consume_type_name", consume_type_name);
		map.put("meal_num", meal_num);
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<ConsumeTypeModel> ConsumeTypeList = consumeTypeService.getConsumeTypeByParamPage(map);
		Integer ConsumeTypeCount = consumeTypeService.getConsumeTypeByParamCount(map);
		
		Grid<ConsumeTypeModel> grid = new Grid<ConsumeTypeModel>();
		grid.setRows(ConsumeTypeList);
		grid.setTotal(ConsumeTypeCount);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-29 下午3:38:07
	 * @功能描述: 根据参数查询消费类型
	 * @参数描述:
	 */
	public void getConsumeTypeByParam(){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<ConsumeTypeModel> ConsumeTypeList = consumeTypeService.getConsumeTypeByParam(map);
		printHttpServletResponse(new Gson().toJson(ConsumeTypeList));
	}
	
	/**
	 * @创建人　: guofei.lian
	 * @创建时间: 2016-08-29 
	 * @功能描述: 初始化餐次编号下拉框
	 * @参数描述:
	 */
	public void getMealNum(){
		List<Map> list=new ArrayList<Map>();
		List<MealModel> mealList=consumeTypeService.getMealNum();
		for(MealModel mm:mealList){
			Map<String,String> map=new HashMap<String,String>();
			map.put("VALUE", mm.getMeal_num());
			map.put("TEXT", mm.getMeal_name());
			list.add(map);
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午3:57:25
	 * @功能描述: 新增消费类型
	 * @参数描述:
	 */
	public void addConsumeType(){
		
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		
		String loginUserName = loginUser.getYhMc();
		String data = request.getParameter("data");
		ConsumeTypeModel consumeType = GsonUtil.toBean(data, ConsumeTypeModel.class);
		
		boolean addResult = consumeTypeService.addConsumeType(consumeType, loginUserName);
		if(addResult){
			result.setSuccess(true);
			result.setMsg("新增成功");
		} else {
			result.setSuccess(false);
			result.setMsg("新增失败");
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午7:22:35
	 * @功能描述: 修改消费类型
	 * @参数描述:
	 */
	public void modifyConsumeType(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		
		String loginUserName = loginUser.getYhMc();
		String data = request.getParameter("data");
		ConsumeTypeModel consumeType = GsonUtil.toBean(data, ConsumeTypeModel.class);
		
		boolean modifyResult = consumeTypeService.modifyConsumeType(consumeType, loginUserName);
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
	 * @创建时间: 2016-7-27 下午8:12:02
	 * @功能描述: 删除消费类型
	 * @参数描述:
	 */
	public void removeConsumeType(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		
		String loginUserName = loginUser.getYhMc();
		String delete_ids = request.getParameter("delete_ids");
		String consume_type_nums = request.getParameter("consume_type_nums");
		
		StringBuffer boundTypes = new StringBuffer();
		for (String consume_type_num : consume_type_nums.split(",")) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("consume_type_num", consume_type_num);
			List<ConsumeGroupModel> boundGroups = consumeGroupService.getConsumeGroupByParam(map);
			if(boundGroups != null && boundGroups.size() != 0){
				String consume_type_name = consumeTypeService.getConsumeTypeByNum(consume_type_num)
						.get(0).getConsume_type_name();
				boundTypes.append(consume_type_name+",");
			}
		}
		if(boundTypes.length()!=0){
			boundTypes.setLength(boundTypes.length()-1);
			result.setSuccess(false);
			result.setMsg("存在类型被消费组使用，无法删除！"+"\n"+"被使用类型："+boundTypes);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		
		boolean removeResult = consumeTypeService.removeConsumeType(delete_ids, loginUserName);
		if(removeResult){
			result.setSuccess(true);
			result.setMsg("删除成功");
		} else {
			result.setSuccess(false);
			result.setMsg("删除失败");
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-29 下午4:42:01
	 * @功能描述: 根据代码查询消费类型（判断该类型代码是否已存在）
	 * @参数描述:
	 */
	public void getConsumeTypeByNum(){
		HttpServletRequest request = getHttpServletRequest();
		
		String consume_type_num = request.getParameter("consume_type_num");
		List<ConsumeTypeModel> consumeTypeList = consumeTypeService.getConsumeTypeByNum(consume_type_num);
		
		if(consumeTypeList != null && consumeTypeList.size() != 0){
			printHttpServletResponse(new Gson().toJson(1));
		} else {
			printHttpServletResponse(new Gson().toJson(0));
		}
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-29 下午4:42:01
	 * @功能描述: 根据id查询消费类型
	 * @参数描述:
	 */
	public void getConsumeTypeById(){
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");
		ConsumeTypeModel consumeType = consumeTypeService.getConsumeTypeById(id);
		printHttpServletResponse(new Gson().toJson(consumeType));
	}
	
	/**
	 * 根据参数查询消费类型[分页]
	 * @author minting.he
	 */
	public void getConsumeSameType(){
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		ConsumeTypeModel consumeType = GsonUtil.toBean(data, ConsumeTypeModel.class);
		String consume_type_num = consumeType.getConsume_type_num();
		String consume_type_name = consumeType.getConsume_type_name();
		String meal_num=consumeType.getMeal_num();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("consume_type_num", consume_type_num);
		map.put("consume_type_name", consume_type_name);
		map.put("meal_num", meal_num);
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<ConsumeTypeModel> ConsumeTypeList = consumeTypeService.getConsumeSameType(map);
		Integer ConsumeTypeCount = consumeTypeService.getConsumeSameTypeCount(map);
		Grid<ConsumeTypeModel> grid = new Grid<ConsumeTypeModel>();
		grid.setRows(ConsumeTypeList);
		grid.setTotal(ConsumeTypeCount);
		printHttpServletResponse(new Gson().toJson(grid));
	}
}
