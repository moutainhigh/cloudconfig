package com.kuangchi.sdd.elevatorConsole.floorsgroup.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.elevatorConsole.floorsgroup.model.FloorsGroupModel;
import com.kuangchi.sdd.elevatorConsole.floorsgroup.service.FloorsGroupService;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("floorsGroupAction")
public class FloorsGroupAction extends BaseActionSupport {
	private static final  Logger LOG = Logger.getLogger(FloorsGroupAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	@Resource(name = "FloorsGroupServiceImpl")
	private FloorsGroupService floorsGroupService;
	
	private FloorsGroupModel model;
	public FloorsGroupAction(){
		model=new FloorsGroupModel();
	}
	
	public Object getModel() {
		return model;
	}
	
	//跳转到门信息主页面
	public String toMyFloorsGroup(){
		return "success";
	}
	
	//查询页面全部信息
		public void getSelfloorsGroups(){
			String page=getHttpServletRequest().getParameter("page");
			String rows=getHttpServletRequest().getParameter("rows");
			String data= getHttpServletRequest().getParameter("data");
			FloorsGroupModel floor_info=GsonUtil.toBean(data,FloorsGroupModel.class);
			Grid allDiscount=floorsGroupService.SelectAllFloorGroups(floor_info, page, rows);
			printHttpServletResponse(GsonUtil.toJson(allDiscount));
		
		}
	
		//添加楼层组信息
		public void AddfloorsGroups(){
			String data= getHttpServletRequest().getParameter("data");
			FloorsGroupModel floor_info=GsonUtil.toBean(data,FloorsGroupModel.class);
			JsonResult result = new JsonResult();
			List<FloorsGroupModel> list=floorsGroupService.selectByfloorgroupnum(floor_info.getFloor_group_name());
			if(list.size()!=0){
				result.setMsg("楼层组名称重复,请重新输入");
	  			result.setSuccess(false);
			}else{
				User loginUser = (User) getHttpServletRequest().getSession().getAttribute(
						GlobalConstant.LOGIN_USER);
				if (loginUser == null) {
					result.setSuccess(false);
					result.setMsg("操作失败，请先登录");
				} else {
					String login_user = loginUser.getYhMc();
					String max=floorsGroupService.getfloorgroupnum();
					if(max==null){
						floor_info.setFloor_group_num("1");
					}else{
						floor_info.setFloor_group_num(String.valueOf(Integer.valueOf(max)+1));
					}
					
					Boolean obj=floorsGroupService.insertFloorGroup(floor_info, login_user);
					if(obj){
						result.setMsg("添加成功");
			  			result.setSuccess(true);
					}else{
						result.setMsg("添加失败");
			  			result.setSuccess(false);
					}
				}
			}
			printHttpServletResponse(GsonUtil.toJson(result));	
		}
	
		//修改楼层组信息
		public void EditfloorsGroups(){
			String data= getHttpServletRequest().getParameter("data");
			FloorsGroupModel floor_info=GsonUtil.toBean(data,FloorsGroupModel.class);
			JsonResult result = new JsonResult();
			if(!floor_info.getOld_floor_group_name().equals(floor_info.getFloor_group_name())){
				List<FloorsGroupModel> list=floorsGroupService.selectByfloorgroupnum(floor_info.getFloor_group_name());
				if(list.size()!=0){
					result.setMsg("楼层组名称重复,请重新输入");
		  			result.setSuccess(false);
				}else{
					User loginUser = (User) getHttpServletRequest().getSession().getAttribute(
							GlobalConstant.LOGIN_USER);
					if (loginUser == null) {
						result.setSuccess(false);
						result.setMsg("操作失败，请先登录");
					} else {
						String login_user = loginUser.getYhMc();
						Boolean obj=floorsGroupService.updateFloorgroupname(floor_info, login_user);	
						if(obj){
							result.setMsg("修改成功");
				  			result.setSuccess(true);
						}else{
							result.setMsg("修改失败");
				  			result.setSuccess(false);
						}
					}
				}
			}else{
				User loginUser = (User) getHttpServletRequest().getSession().getAttribute(
						GlobalConstant.LOGIN_USER);
				if (loginUser == null) {
					result.setSuccess(false);
					result.setMsg("操作失败，请先登录");
				} else {
					String login_user = loginUser.getYhMc();
					Boolean obj=floorsGroupService.updateFloorgroupname(floor_info, login_user);	
					if(obj){
						result.setMsg("修改成功");
			  			result.setSuccess(true);
					}else{
						result.setMsg("修改失败");
			  			result.setSuccess(false);
					}
				}
			}
			
			printHttpServletResponse(GsonUtil.toJson(result));	
		}
		//删除楼层组信息
		public void DeletefloorsGroups(){
			String floor_group_num= getHttpServletRequest().getParameter("floor_group_num");
			JsonResult result = new JsonResult();	
			String [] str= floor_group_num.split(",");
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			if (loginUser == null) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				String login_user = loginUser.getYhMc();
				for (int i = 0; i < str.length; i++) {
					List<FloorsGroupModel> list=floorsGroupService.selectByAuthority(str[i]);
					if(list.size()==0){
						Boolean obj=floorsGroupService.updateDeleteFlag(str[i], login_user);
						if(obj){
							 result.setMsg("删除成功");
						    result.setSuccess(true);
						   
						}else{
							 result.setMsg("删除失败");
						    result.setSuccess(false);
						}
					}else{
						result.setMsg("该楼层组正在使用,不能删除");
			  			result.setSuccess(false);
			  			printHttpServletResponse(GsonUtil.toJson(result));
					}
					
				}
			}
			printHttpServletResponse(GsonUtil.toJson(result));
		}	
		
		
	//分发页面
		public String toPageEditAndSelect(){
			String floorgroupnum= getHttpServletRequest().getParameter("floor_group_num");
			String flag= getHttpServletRequest().getParameter("flag");
			if ("edit".equals(flag)) {
				List<FloorsGroupModel> list=floorsGroupService.selectByfloor(floorgroupnum);
				for (FloorsGroupModel floorsGroupModel : list) {
					getHttpServletRequest().setAttribute("floor", floorsGroupModel);
				}
				return "edit";
			}else{
				List<FloorsGroupModel> list=floorsGroupService.selectByfloor(floorgroupnum);
				for (FloorsGroupModel floorsGroupModel : list) {
					getHttpServletRequest().setAttribute("floor", floorsGroupModel);
				}
				return "view";
			}
		}
		//查询楼层组名称
		public void selectByFloorName(){
			List<Map> list = new ArrayList<Map>();
			List<FloorsGroupModel> lists=floorsGroupService.selectByFloorName();
		for (FloorsGroupModel floorsGroupModel : lists) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("TEXT", floorsGroupModel.getFloor_group_name());// 绑定到页面combobox中的下拉框
			map.put("ID", floorsGroupModel.getFloor_group_num());// 绑定到页面combobox中的下拉框
			list.add(map);
		}
			printHttpServletResponse(GsonUtil.toJson(list));
		}
		
		/**
		 * 查询楼层组信息
		 * @author minting.he
		 */
		public void getFloorGroupInfo(){
			List<FloorsGroupModel> list = floorsGroupService.getFloorGroupInfo();
			printHttpServletResponse(GsonUtil.toJson(list));
		}
		
		/**
		 * 查询组对应楼层
		 * @author minting.he
		 */
		public void getFloorByGroup(){
			HttpServletRequest request = getHttpServletRequest();
			String floor_group_num = request.getParameter("floor_group_num");
			List<String> list = floorsGroupService.getFloorByGroup(floor_group_num);
			printHttpServletResponse(GsonUtil.toJson(list));
		}
		
	
}
