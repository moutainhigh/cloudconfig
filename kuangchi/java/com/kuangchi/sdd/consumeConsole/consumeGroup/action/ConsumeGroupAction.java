package com.kuangchi.sdd.consumeConsole.consumeGroup.action;


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
import com.kuangchi.sdd.consumeConsole.consumeGroupMap.model.GroupMapModel;
import com.kuangchi.sdd.consumeConsole.consumeGroupMap.service.IGroupMapService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;


/**
 * @创建人　: 高育漫
 * @创建时间: 2016-7-27 下午2:49:36
 * @功能描述: 消费组管理-action
 */
@Controller("consumeGroupAction")
public class ConsumeGroupAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource(name = "consumeGroupServiceImpl")
	private IConsumeGroupService consumeGroupService;
	
	@Resource(name = "groupMapServiceImpl")
	private IGroupMapService groupMapService;
	
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午2:17:10
	 * @功能描述: 跳转到消费组管理页面
	 * @参数描述:
	 */
	public String toConsumeGroupPage(){
		return "success";
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午2:58:48
	 * @功能描述: 根据参数查询消费组[分页]
	 * @参数描述:
	 */
	public void getConsumeGroupByParamPage(){
		
		HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		ConsumeGroupModel consumeGroup = GsonUtil.toBean(data, ConsumeGroupModel.class);
		String consume_type_name = consumeGroup.getConsume_type_name();
		String group_name = consumeGroup.getGroup_name();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("consume_type_name", consume_type_name);
		map.put("group_name", group_name);
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<ConsumeGroupModel> ConsumeGroupList = consumeGroupService.getConsumeGroupByParamPage(map);
		Integer consumeGroupCount = consumeGroupService.getConsumeGroupByParamCount(map);
		
		Grid<ConsumeGroupModel> grid = new Grid<ConsumeGroupModel>();
		grid.setRows(ConsumeGroupList);
		grid.setTotal(consumeGroupCount);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	/**
	 * 查询非默认的消费组
	 * @author yuman.gao
	 */
	public void getNonDefaultGroup(){
		HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		ConsumeGroupModel consumeGroup = GsonUtil.toBean(data, ConsumeGroupModel.class);
		String consume_type_name = consumeGroup.getConsume_type_name();
		String group_name = consumeGroup.getGroup_name();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("consume_type_name", consume_type_name);
		map.put("group_name", group_name);
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<ConsumeGroupModel> ConsumeGroupList = consumeGroupService.getNonDefaultGroup(map);
		Integer consumeGroupCount = consumeGroupService.getConsumeGroupByParamCount(map);
		
		Grid<ConsumeGroupModel> grid = new Grid<ConsumeGroupModel>();
		grid.setRows(ConsumeGroupList);
		grid.setTotal(consumeGroupCount);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午3:57:25
	 * @功能描述: 新增消费组
	 * @参数描述:
	 */
	public void addConsumeGroup(){
		
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		
		String loginUserName = loginUser.getYhMc();
		String data = request.getParameter("data");
		ConsumeGroupModel consumeGroup = GsonUtil.toBean(data, ConsumeGroupModel.class);
		
		boolean addResult = consumeGroupService.addConsumeGroup(consumeGroup, loginUserName);
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
	 * @功能描述: 修改消费组
	 * @参数描述:
	 */
	public void modifyConsumeGroup(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		
		String loginUserName = loginUser.getYhMc();
		String data = request.getParameter("data");
		ConsumeGroupModel consumeGroup = GsonUtil.toBean(data, ConsumeGroupModel.class);
		
		boolean modifyResult = consumeGroupService.modifyConsumeGroup(consumeGroup, loginUserName);
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
	 * @功能描述: 删除消费组
	 * @参数描述:
	 */
	public void removeConsumeGroup(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		
		String loginUserName = loginUser.getYhMc();
		String delete_ids = request.getParameter("delete_ids");
		String group_nums = request.getParameter("group_nums");
		
		StringBuffer boundGroups = new StringBuffer();
		for (String group_num : group_nums.split(",")) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("group_num", group_num);
			List<GroupMapModel> boundMap = groupMapService.getGroupMapByParam(map);
			if(boundMap != null && boundMap.size() != 0){
				boundGroups.append(boundMap.get(0).getGroup_name()+",");
			}
		}
		if(boundGroups.length()!=0){
			boundGroups.setLength(boundGroups.length()-1);
			result.setSuccess(false);
			result.setMsg("存在消费组绑定了员工，无法删除！"+"\n"+"被绑定消费组："+boundGroups);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		
		boolean removeResult = consumeGroupService.removeConsumeGroup(delete_ids, loginUserName);
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
	 * @功能描述: 根据组代码查询消费组（判断该代码是否已存在）
	 * @参数描述:
	 */
	public void getConsumeGroupByNum(){
		HttpServletRequest request = getHttpServletRequest();
		
		String group_num = request.getParameter("group_num");
		List<ConsumeGroupModel> ConsumeGroupList = consumeGroupService.getConsumeGroupByNum(group_num);
		
		if(ConsumeGroupList != null && ConsumeGroupList.size() != 0){
			printHttpServletResponse(new Gson().toJson(1));
		} else {
			printHttpServletResponse(new Gson().toJson(0));
		}
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-29 下午4:42:01
	 * @功能描述: 根据ID查询消费组
	 * @参数描述:
	 */
	public void getConsumeGroupById(){
		
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");
		ConsumeGroupModel consumeGroup = consumeGroupService.getConsumeGroupById(id);
		printHttpServletResponse(new Gson().toJson(consumeGroup));
	}
	
	/**
	 * 判断两个类型是否相连同一餐次
	 * @author minting.he
	 */
	public void ifTypeSameMeal(){
		HttpServletRequest request = getHttpServletRequest();
		String old_type_num = request.getParameter("old_type_num");
		String new_type_num = request.getParameter("new_type_num");
		boolean result = consumeGroupService.ifTypeSameMeal(old_type_num, new_type_num);
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
}
