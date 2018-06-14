package com.kuangchi.sdd.attendanceConsole.staffManage.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.attendanceConsole.staffManage.model.StaffManage;
import com.kuangchi.sdd.attendanceConsole.staffManage.service.StaffManageService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.algorithm.MD5;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("staffManageAction")
public class StaffManageAction extends BaseActionSupport {
	
	@Resource(name="staffManageServiceImpl")
	private StaffManageService staffManageService;
	
	@Resource(name = "roleServiceImpl")
	private IRoleService roleService;
	
	@Resource(name = "departmentServiceImpl")
	private IDepartmentService departmentService;
	
	@Override
	public Object getModel() {
		return null;
	}
	
	public void getAllStaff(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");//获取前台序列化的数据
		StaffManage staffManage = GsonUtil.toBean(beanObject, StaffManage.class);
		
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		/*boolean isLayer = roleService.isLayer();
		if(isLayer){
			Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
			staffManage.setJsDm(role.getJsDm());
			
			User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
			staffManage.setYhDm(user.getYhDm());
		} else {
			staffManage.setJsDm("0");
		}*/
		
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		staffManage.setLayerDeptNum(layerDeptNum);
		
		staffManage.setStaff_name(staffManage.getStaff_name());
		staffManage.setStaff_no((staffManage.getStaff_no()));
		Grid<StaffManage> staffManageInfo=staffManageService.getAllStaff(staffManage, page, rows);
		printHttpServletResponse(GsonUtil.toJson(staffManageInfo));
	}
	
	public void modifyPasswordManager(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String staff_num=request.getParameter("staff_num");
		String staff_password=request.getParameter("staff_password");
		
		if (EmptyUtil.atLeastOneIsEmpty(staff_num, staff_password)) {
	    	result.setMsg("必须的参数不能为空");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
	   }
		String md5Password=MD5.getInstance().encryption(staff_password);
		StaffManage staffManage =new StaffManage();
		staffManage.setStaff_num(staff_num);
		staffManage.setStaff_password(md5Password);
		
		User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		staffManageService.modifyStaffPassword(staffManage,loginUser.getYhMc());
		result.setMsg("修改成功");
		result.setSuccess(true);
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	/*
	public void modifyPasswordStaff(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String staff_num=request.getParameter("staff_num");
		String old_password=request.getParameter("old_password");
		String new_password=request.getParameter("new_password");
		
		String getPassword=staffManageService.getStaffPassword(staff_num);
		if(old_password!=getPassword){
			result.setMsg("原密码不正确");
			result.setSuccess(true);
			return;
		}else{
		
		StaffManage staffManage =new StaffManage();
		staffManage.setStaff_num(staff_num);
		staffManage.setStaff_password(new_password);
		staffManageService.modifyStaffPassword(staffManage);
		result.setMsg("修改成功");
		result.setSuccess(true);
		}
		
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	*/
	
	//主页面显示
    public String toStaffManagePage(){
    	return "success";
    }
}
