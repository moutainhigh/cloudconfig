package com.kuangchi.sdd.businessConsole.randomCode.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.businessConsole.randomCode.model.JsonResult;
import com.kuangchi.sdd.businessConsole.randomCode.model.RandomCode;
import com.kuangchi.sdd.businessConsole.randomCode.service.RandomCodeService;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.staffUser.model.Staff;
import com.kuangchi.sdd.util.algorithm.MD5;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GenerateRandomCode;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.mail.service.MailService;

@Controller("randomCodeAction")
public class RandomCodeAction extends BaseActionSupport {

	 @Resource(name = "employeeService")
	 EmployeeService employeeService;
	 @Resource(name = "roleServiceImpl")
	 private IRoleService roleService;
	 @Resource(name = "MailServiceImpl")
	 private MailService mailService;
	 
	 @Resource(name="randomCodeServiceImpl")
	 private RandomCodeService randomCodeService; 
	 
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-19 上午10:04:18
	 * @功能描述:校验用户是否有绑定邮箱 
	 * @参数描述:
	 */
	public void checkUserMailByStaffNo(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult jsonResult = new JsonResult();
		Staff staff = (Staff) request.getSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		String staffNo = request.getParameter("staffNo");//未登录时
		if(null!=staffNo){
			Employee employee = employeeService.checkStaffMailByStaffNo(staffNo);//在点击忘记密码时，查看用户是否绑定邮箱
			if(null==employee){
				jsonResult.setMsg("该用户工号不存在");
				jsonResult.setSuccess(false);
				jsonResult.setNotExist(true);
			}else{
				jsonResult.setNotExist(false);
			if(null!=employee.getDzyj()){
				jsonResult.setMsg("用户已绑定邮箱");
				jsonResult.setSuccess(true);
				jsonResult.setCode(employee.getDzyj());//把邮箱返回页面
			}else{
				jsonResult.setMsg("用户尚未绑定邮箱");
				jsonResult.setSuccess(false);
			}
			}
		}else{
			if(null!=staff){             //用户已登录
					Employee employee = employeeService.checkStaffMailByStaffNo(staff.getStaff_no());
					if(null!=employee.getDzyj()){
						jsonResult.setMsg("用户已绑定邮箱");
						jsonResult.setSuccess(true);
						jsonResult.setCode(staff.getStaff_email());//把邮箱返回页面
					}else{
						jsonResult.setMsg("用户尚未绑定邮箱");
						jsonResult.setSuccess(false);
					}
				}
		}
		printHttpServletResponse(GsonUtil.toJson(jsonResult));
	}
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-22 上午11:40:48
	 * @功能描述:确认用户邮箱是否唯一 
	 * @参数描述:
	 */
	public void checkStaffMailUnique(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult jsonResult = new JsonResult();
		
		String staffMail = request.getParameter("staffMail");
		Integer count = employeeService.checkStaffMailUnique(staffMail);
		if(count>0){
			jsonResult.setMsg("邮箱已被用户绑定");
			jsonResult.setSuccess(true);
		}else{
			jsonResult.setMsg("该邮箱尚未绑定");
			jsonResult.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(jsonResult));
	}
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-16 下午5:32:23
	 * @功能描述: 发送6位随机验证码
	 * @参数描述:
	 */
	public void sendRandomCode(){
		HttpServletRequest request = getHttpServletRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		String randCode = GenerateRandomCode.getRandomCode();
		JsonResult jsonResult = new JsonResult();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String validTime = df.format(new Date());//发送验证码的时间
		String staffMail = request.getParameter("staffMail");//获取用户的邮箱
		if(null==staffMail||"".equals(staffMail)){
			jsonResult.setMsg("请输入邮箱地址!");
			jsonResult.setSuccess(false);
		}else{
		map.put("staffMail", staffMail);
		map.put("randCode",randCode);
		map.put("validTime", validTime);
		String result = mailService.send_RandCode_Email(staffMail, map);
		if("0".equals(result)){
			jsonResult.setMsg("验证码发送成功，请您查看邮箱");
			randomCodeService.insertRandCodeInfo(map);
			jsonResult.setSuccess(true);
		}else{
			jsonResult.setMsg("验证码发送失败!");
			jsonResult.setSuccess(false);
		}
		}
		printHttpServletResponse(GsonUtil.toJson(jsonResult));
	}
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-16 下午5:35:36
	 * @功能描述: 校验验证码输入是否正确
	 * @参数描述:
	 */
	public void checkRandomCode(){
		JsonResult jsonResult = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String randCode = request.getParameter("randCode");//从页面获取用户输入的验证码
		String staffMail = request.getParameter("staffMail");//从获取页面的用户邮箱
		Map map = new  HashMap();
		map.put("randCode", randCode);
		map.put("staffMail", staffMail);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	  if(null==randCode||"".equals(randCode))
		{
			jsonResult.setMsg("请输入验证码！");
			jsonResult.setSuccess(false);
			
		}else{
			Integer count = randomCodeService.getCountByRandCode(map);
			if(count>0){
				List<RandomCode> randomCode = randomCodeService.getRandCodeByUserMail(staffMail);//根据用户邮箱判断是否存在最新的验证码信息
					double diff ; 
					try {
						SimpleDateFormat df0= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
						if(randCode.equalsIgnoreCase(randomCode.get(0).getRandCode())){
							String nowValidTime = df0.format(new Date());// new Date()为获取当前系统时间
							Date nowValidTimeDate = df0.parse(nowValidTime);
							Date beforeValidTimeDate = df.parse(randomCode.get(0).getValidTime());
							long beforeValidTime = beforeValidTimeDate.getTime();
							long nowValidedTime = nowValidTimeDate.getTime();
							diff = nowValidedTime-beforeValidTime;
							if(diff/(1000*60)>30){
								jsonResult.setMsg("验证码输入已超时");
								jsonResult.setOverTime(true);
								jsonResult.setSuccess(false);
							}else{
								jsonResult.setOverTime(false);
								jsonResult.setMsg("验证码输入正确");
								jsonResult.setSuccess(true);
							}
						}else{
								jsonResult.setMsg("验证码已失效");
								jsonResult.setInvalid(true);
								jsonResult.setSuccess(false);
						}
					}catch (ParseException e) {
								e.printStackTrace();
					}
			}else{
				jsonResult.setMsg("验证码输入不正确");
				jsonResult.setSuccess(false);
			}
	}
	printHttpServletResponse(GsonUtil.toJson(jsonResult));
}
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-16 下午8:08:36
	 * @功能描述: 根据用户工号重置用户密码 
	 * @参数描述:
	 */
	public void resetPassword(){
		JsonResult jsonResult = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String staff_email = request.getParameter("staffMail");
		String staff_password = request.getParameter("password");//从页面获取用户输入的验证码
		if (EmptyUtil.atLeastOneIsEmpty(staff_email, staff_password)) {
			jsonResult.setMsg("必须的参数不能为空");
			jsonResult.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(jsonResult));
			return;
	   }
		String md5Password=MD5.getInstance().encryption(staff_password);
		Map map = new HashMap();
		map.put("staff_email", staff_email);
		map.put("staff_password", md5Password);
		boolean result = employeeService.resetPassword(map);
		if(result){
			jsonResult.setMsg("重置密码成功");
			jsonResult.setSuccess(true);
		}else{
			jsonResult.setMsg("重置密码失败");
			jsonResult.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(jsonResult));
	}
	
	@Override
	public Object getModel() {
		return null;
	}
}
