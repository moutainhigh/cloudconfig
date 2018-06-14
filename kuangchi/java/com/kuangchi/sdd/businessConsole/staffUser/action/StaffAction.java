package com.kuangchi.sdd.businessConsole.staffUser.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.businessConsole.staffUser.model.Staff;
import com.kuangchi.sdd.businessConsole.staffUser.service.IStaffService;
import com.kuangchi.sdd.util.commonUtil.EncodeUtil;

@Controller("staffAction")
public class StaffAction extends BaseActionSupport {
	
	@Resource(name = "staffServiceImpl")
	private IStaffService staffService;
	@Resource(name = "employeeService")
	private EmployeeService employeeService;
	 
	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-12 下午3:05:59
	 * @功能描述:	 验证登录信息
	 * @参数描述:
	 */
	public void loginValidate(){
		try {
			JsonResult result = new JsonResult();
			HttpServletRequest request = getHttpServletRequest();
			String staff_no = request.getParameter("staff_no");
			String staff_password = EncodeUtil.encode(request.getParameter("staff_password"));
			
			Staff staff = new Staff();
			staff.setStaff_no(staff_no);
			String staff_num = employeeService.selectStaffNum(staff_no);
			staff.setStaff_num(staff_num);
			staff.setStaff_password(staff_password);
			Staff loginStaff = staffService.getLoginStaff(staff);
			
			if(loginStaff == null){
				result.setSuccess(false);
				result.setMsg("账号或密码错误");
				printHttpServletResponse(new Gson().toJson(result));
				return ;
			}
			if(loginStaff.getStaff_state().equals("2")){
				result.setSuccess(false);
				result.setMsg("该账号已被冻结");
				printHttpServletResponse(new Gson().toJson(result));
				return ;
			}
			getHttpSession().setAttribute(GlobalConstant.LOGIN_STAFF, loginStaff);
			result.setSuccess(true);
			result.setMsg("登录成功");
			printHttpServletResponse(new Gson().toJson(result));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-12 下午3:24:47
	 * @功能描述:	员工修改密码
	 * @参数描述:
	 */
	public void modifyPassword(){
		try {
			
			JsonResult result = new JsonResult();
			HttpServletRequest request = getHttpServletRequest();
			
			Staff session_staff = (Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
			if(session_staff == null){
				result.setSuccess(false);
				result.setMsg("用户信息失效，请重新登录");
				printHttpServletResponse(new Gson().toJson(result));
				return;
			}
			String staff_num = session_staff.getStaff_num();
			String old_password = EncodeUtil.encode(request.getParameter("old_password"));
			String new_password = EncodeUtil.encode(request.getParameter("new_password"));
			
			Staff staff = new Staff();
			staff.setStaff_num(staff_num);
			staff.setStaff_password(old_password);
			int resultCode = staffService.modifyPassword(staff, new_password);
			
			if(resultCode == 1){
				result.setSuccess(false);
				result.setMsg("密码错误,请重输");
				printHttpServletResponse(new Gson().toJson(result));
				return ;
			}
			if(resultCode == 2){
				result.setSuccess(false);
				result.setMsg("该账号已被冻结，无法修改密码");
				printHttpServletResponse(new Gson().toJson(result));
				return ;
			}
			result.setSuccess(true);
			result.setMsg("修改密码成功");
			printHttpServletResponse(new Gson().toJson(result));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-13 下午5:15:41
	 * @功能描述: 获得登陆用户的信息
	 * @参数描述:
	 */
	public void getLoginStaff(){
		
		Staff session_staff = (Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		printHttpServletResponse(new Gson().toJson(session_staff));
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-13 下午5:33:23
	 * @功能描述: 用户退出系统
	 * @参数描述:
	 */
	public void logout(){
		HttpSession session = getHttpServletRequest().getSession();
		session.removeAttribute(GlobalConstant.LOGIN_STAFF);
		
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		result.setMsg("退出成功");
		printHttpServletResponse(new Gson().toJson(result));
	}
}
