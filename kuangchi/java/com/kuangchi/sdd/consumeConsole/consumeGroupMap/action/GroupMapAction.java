package com.kuangchi.sdd.consumeConsole.consumeGroupMap.action;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.consumeGroupMap.model.GroupMapModel;
import com.kuangchi.sdd.consumeConsole.consumeGroupMap.service.IGroupMapService;
import com.kuangchi.sdd.util.commonUtil.BeanUtil;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.sun.tools.xjc.generator.bean.field.SingleField;


/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-1 下午2:49:36
 * @功能描述: 消费分组管理-action
 */
@Controller("groupMapAction")
public class GroupMapAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource(name = "groupMapServiceImpl")
	private IGroupMapService groupMapService;
	
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-1 下午2:17:10
	 * @功能描述: 跳转到消费分组管理页面
	 * @参数描述:
	 */
	public String toGroupMapPage(){
		return "success";
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-1 下午2:58:48
	 * @功能描述: 根据参数查询消费分组[分页]
	 * @参数描述:
	 */
	public void getGroupMapByParamPage(){
		
		HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		GroupMapModel groupMap = GsonUtil.toBean(data, GroupMapModel.class);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("group_name", groupMap.getGroup_name());
		map.put("staff_name", groupMap.getStaff_name());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<GroupMapModel> groupMapList = groupMapService.getGroupMapByParamPage(map);
		Integer groupMapCount = groupMapService.getGroupMapByParamCount(map);
		
		Grid<GroupMapModel> grid = new Grid<GroupMapModel>();
		grid.setTotal(groupMapCount);
		grid.setRows(groupMapList);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-1 下午3:57:25
	 * @功能描述: 新增消费分组
	 * @参数描述:
	 */
	public void addGroupMap(){
		
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		
		String loginUserName = loginUser.getYhMc();
		String data = request.getParameter("data");
		GroupMapModel groupMap = GsonUtil.toBean(data, GroupMapModel.class);
		String[] staffNums = groupMap.getStaff_num().split(",");
		
		boolean addResult = false;
		for (String staffNum : staffNums) {
			groupMap.setStaff_num(staffNum);
			addResult = groupMapService.addGroupMap(groupMap, loginUserName);
		}
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
	 * @创建时间: 2016-8-1 下午7:22:35
	 * @功能描述: 修改消费分组
	 * @参数描述:
	 */
	public void modifyGroupMap(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		
		String loginUserName = loginUser.getYhMc();
		String data = request.getParameter("data");
		GroupMapModel groupMap = GsonUtil.toBean(data, GroupMapModel.class);
		
		boolean modifyResult = groupMapService.modifyGroupMap(groupMap, loginUserName);
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
	 * 对未分组员工进行分组
	 * @author yuman.gao
	 */
	public void newGroupMap(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		
		String loginUserName = loginUser.getYhMc();
		String data = request.getParameter("data");
		GroupMapModel groupMap = GsonUtil.toBean(data, GroupMapModel.class);
		
		boolean newResult = groupMapService.newGroupMap(groupMap, loginUserName);
		if(newResult){
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
	 * @创建时间: 2016-8-1 下午8:12:02
	 * @功能描述: 删除消费分组
	 * @参数描述:
	 */
	public void removeGroupMap(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		
		String loginUserName = loginUser.getYhMc();
		String delete_ids = request.getParameter("delete_ids");
		
		boolean removeResult = groupMapService.removeGroupMap(delete_ids, loginUserName);
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
	 * @功能描述: 根据员工编号查询姓名
	 * @参数描述:
	 */
	public void getStaffNameByNums(){
		
		HttpServletRequest request = getHttpServletRequest();
		String staffNums = request.getParameter("staffNums");
		List<GroupMapModel> groupMaps = groupMapService.getStaffNameByNums(staffNums);
		
		List<String> names = new ArrayList<String>();
		for (GroupMapModel groupMap : groupMaps) {
			names.add(groupMap.getStaff_name());
		}
		printHttpServletResponse(new Gson().toJson(names));
	}
	
	/**
	 * 查询未分组员工
	 * @author yuman.gao
	 */
	public void searchEmployee() {
        HttpServletRequest request = getHttpServletRequest();
        String beanObject = request.getParameter("data");
        DepartmentPage departmentPage = GsonUtil.toBean(beanObject, DepartmentPage.class);    //将数据转化为javabean
        departmentPage.setPage(Integer.parseInt(request.getParameter("page")));
        departmentPage.setRows(Integer.parseInt(request.getParameter("rows")));
        Grid<Employee> employeeGrid = groupMapService.searchEmployee(departmentPage);
        printHttpServletResponse(GsonUtil.toJson(employeeGrid));
    }
	
	/**
	 * 新增消费分组
	 * @author minting.he
	 */
	public void insertGroupMap(){
		JsonResult result = new JsonResult();
		try{
			HttpServletRequest request = getHttpServletRequest();
			String data = request.getParameter("data");
			GroupMapModel groupMap = GsonUtil.toBean(data, GroupMapModel.class);
			if (BeanUtil.isEmpty(groupMap)) {
				result.setSuccess(false);
				result.setMsg("新增失败，数据不合法");
			} else{
				User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
				if (BeanUtil.isEmpty(loginUser)) {
					result.setSuccess(false);
					result.setMsg("新增失败，请先登录");
				} else {
					String login_user = loginUser.getYhMc();
					boolean r = groupMapService.insertGroupMap(groupMap, login_user);
					if(r){
						result.setSuccess(true);
						result.setMsg("新增成功");
					} else {
						result.setSuccess(false);
						result.setMsg("新增失败");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("新增失败");
		}finally{
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * 删除消费分组
	 * @author minting.he
	 */
	public void deleteGroupMap(){
		JsonResult result = new JsonResult();
		try{
			HttpServletRequest request = getHttpServletRequest();
			String group_num = request.getParameter("group_num");
			String staff_num = request.getParameter("staff_num");
			if (EmptyUtil.atLeastOneIsEmpty(group_num, staff_num)) {
				result.setSuccess(false);
				result.setMsg("删除失败，数据不合法");
			} else{
				User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
				if (BeanUtil.isEmpty(loginUser)) {
					result.setSuccess(false);
					result.setMsg("删除失败，请先登录");
				} else {
					String login_user = loginUser.getYhMc();
					boolean r = groupMapService.deleteGroupMap(group_num, staff_num, login_user);
					if(r){
						result.setSuccess(true);
						result.setMsg("删除成功");
					} else {
						result.setSuccess(false);
						result.setMsg("删除失败");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("删除失败");
		}finally{
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * 查询消费组对应的餐次
	 * @author minting.he
	 */
	public void getMealsByGroup(){
		HttpServletRequest request = getHttpServletRequest();
		String group_num = request.getParameter("group_num");
		List<String> meal_nums= groupMapService.getMealsByGroup(group_num);
		printHttpServletResponse(GsonUtil.toJson(meal_nums));
	}
	
}
