package com.kuangchi.sdd.businessConsole.action;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.baseConsole.card.service.ICardService;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.businessConsole.randomCode.service.RandomCodeService;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.staffUser.model.Staff;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.businessConsole.user.service.IUserService;
import com.kuangchi.sdd.consumeConsole.device.service.IDeviceService;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.PropertyUtils;
import com.kuangchi.sdd.util.mail.service.MailService;

/**
 * 后台首页
 * 
 * @author ccdt
 *
 */
@Controller("adminAction")
public class AdminAction extends BaseActionSupport {

	private static final long serialVersionUID = -4551270484793566334L;
	
	 @Resource(name = "roleServiceImpl")
	 private IRoleService roleService;
	 @Resource(name = "MailServiceImpl")
	 private MailService mailService;
	 @Resource(name = "employeeService")
	 EmployeeService employeeService;
	 @Resource(name = "userServiceImpl")
	 private IUserService userService;
	 
	 @Resource(name="randomCodeServiceImpl")
	 private RandomCodeService randomCodeService; 
	 
	@Override
	public Object getModel() {
		return null;
	}

	public String showIndex() {
		//返回登录用户所有角色
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		Role role=(Role)request.getSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
		String roleDm=role.getJsDm();
		List<String> xtList=userService.getRoleXtAuths(roleDm);//拥有的系统权限
		List<Role> userRoles = roleService.getUserRoles(loginUser.getYhDm());
		List<String> absList=getAbsentXt(roleDm,xtList);//缺少的系统权限
		//request.setAttribute("userRoles",userRoles);
		request.getSession().setAttribute("userRoles",userRoles);
		request.getSession().setAttribute("ownXt",xtList);
		request.getSession().setAttribute("absentXt",absList);
		return SUCCESS;
	}
	
	private List<String> getAbsentXt(String roleDm,List<String> xtList){
		Set xtSet=new HashSet(xtList);
		Set<String> total = new HashSet<String>();
		Set<String> result = new HashSet<String>();
		total.add("2");
		total.add("3");
		total.add("4");
		total.add("5");
		total.add("6");
		total.add("7");
		result.clear();
		result.addAll(total);
		result.removeAll(xtSet);
		List<String> resultList=new ArrayList(result);
		Collections.sort(resultList);
		return resultList;
	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-19 上午10:04:18
	 * @功能描述:根据用户工号绑定邮箱
	 * @参数描述:
	 */
	public void insertStaffMailByStaffNo(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult jsonResult = new JsonResult();
		Map<String, Object> map = new HashMap<String, Object>();
		Staff staff = (Staff) request.getSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		String staffNo = staff.getStaff_no();
		
		String staffMail = request.getParameter("staffMail");
		map.put("staffNo", staffNo);
		map.put("staffMail", staffMail);
		boolean result = employeeService.insertStaffMailByStaffNo(map);
		if(result){
			jsonResult.setMsg("绑定邮箱成功");
			jsonResult.setSuccess(true);
		}else{
			jsonResult.setMsg("绑定邮箱失败");
			jsonResult.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(jsonResult));
	}
	/*
	*//**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-19 上午10:04:18
	 * @功能描述:校验用户是否有绑定邮箱 
	 * @参数描述:
	 *//*
	public void checkUserMailByStaffNo(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult jsonResult = new JsonResult();
		
		String staffNo = request.getParameter("staffNo");
		String staffMail = employeeService.checkStaffMailByStaffNo(staffNo);
		if(null!=staffMail){
			jsonResult.setMsg("用户已绑定邮箱");
			jsonResult.setSuccess(true);
			jsonResult.setCode(staffMail);
		}else{
			jsonResult.setMsg("用户尚未绑定邮箱");
			jsonResult.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(jsonResult));
	}
	

	
	*//**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-16 下午5:32:23
	 * @功能描述: 发送6位随机验证码
	 * @参数描述:
	 *//*
	public void sendRandomCode(){
		HttpServletRequest request = getHttpServletRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		String randCode = GenerateRandomCode.getRandomCode();
		JsonResult jsonResult = new JsonResult();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String validTime = df.format(new Date());// new Date()为获取当前系统时间
		String staffMail = request.getParameter("staffMail");//获取用户的邮箱
		if(null==staffMail||"".equals(staffMail)){
			jsonResult.setMsg("请输入邮箱地址!");
			jsonResult.setSuccess(false);
		}else{
		map.put("staffMail", staffMail);
		map.put("randCode",randCode);
		map.put("validTime", validTime);
		String result = mailService.send_RandCode_Email(staffMail, map);
		randomCodeService.insertRandCodeInfo(map);
		if("0".equals(result)){
			jsonResult.setMsg("验证码发送成功，请您查看邮箱");
			jsonResult.setSuccess(true);
		}else{
			jsonResult.setMsg("验证码发送失败!");
			jsonResult.setSuccess(false);
		}
		}
		printHttpServletResponse(GsonUtil.toJson(jsonResult));
	}
	
	*//**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-16 下午5:35:36
	 * @功能描述: 校验验证码输入是否正确
	 * @参数描述:
	 *//*
	public void checkRandomCode(){
		JsonResult jsonResult = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String randCode = request.getParameter("randCode");//从页面获取用户输入的验证码
		String staffMail = request.getParameter("staffMail");//从获取页面的用户邮箱
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String validTime = df.format(new Date());// new Date()为获取当前系统时间
		if(null==randCode||"".equals(randCode))
		{
			jsonResult.setMsg("请输入验证码！");
			jsonResult.setSuccess(false);
			
		}else{
			List<RandomCode> randomCode = randomCodeService.getRandCodeByUserMail(staffMail);//根据工号判断是否存在验证码信息
			
			if(null!=randomCode||!"".equals(randomCode)){
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
				long diff ; 
				try {
					Date validatedTime = dateFormat.parse(randomCode.get(0).getValidTime());// new Date()为获取当前系统时间
					Date validTimeDate = df.parse(validTime);
					long valideTime = validTimeDate.getTime();
					long validedTime = validatedTime.getTime();
					diff = valideTime-validedTime;
				if(diff/(1000*60)>5){
					jsonResult.setMsg("验证码输入已超时");
					jsonResult.setSuccess(false);
				}else{
					if(randCode.equals(randomCode.get(0).getRandCode())){
						jsonResult.setMsg("验证码输入正确");
						jsonResult.setSuccess(true);
					}else{
						jsonResult.setMsg("验证码输入不正确");
						jsonResult.setSuccess(true);
					}
				}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}else{
				jsonResult.setMsg("验证码输入不正确");
				jsonResult.setSuccess(false);
			}
	
	}
	printHttpServletResponse(GsonUtil.toJson(jsonResult));
}
	
	*//**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-16 下午8:08:36
	 * @功能描述: 根据用户工号重置用户密码 
	 * @参数描述:
	 *//*
	public void resetPassword(){
		JsonResult jsonResult = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String staff_no = request.getParameter("staffNo");
		String staff_password = request.getParameter("password");//从页面获取用户输入的验证码
		if (EmptyUtil.atLeastOneIsEmpty(staff_no, staff_password)) {
			jsonResult.setMsg("必须的参数不能为空");
			jsonResult.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(jsonResult));
			return;
	   }
		String md5Password=MD5.getInstance().encryption(staff_password);
		Map map = new HashMap();
		map.put("staff_no", staff_no);
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
	}*/
	
	
	@Value("${systemVersion}")
	private String systemVersion;

	/**
	 * 获取系统版本
	 * @author minting.he
	 */
	public void getSystemVersion(){
		HttpServletRequest request = getHttpServletRequest();
		List<String> list = new ArrayList<String>();
		list.add(systemVersion);
		String propertyFile = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
		Properties properties = PropertyUtils.readProperties(propertyFile);
		String lisenceKey = properties.getProperty("lisenceKey");
		String[] key = lisenceKey.split(";");
		String y = key[1].substring(0, 4); 
		String m = key[1].substring(4, 6);
		String d = key[1].substring(6, 8);
		String date = y+"-"+m+"-"+d;
		list.add(date);
		//卡
		if(-1 == Integer.valueOf(key[2])){
			list.add("不限制");
		}else {
			list.add(key[2]);
		}
		//连接
		if(-1 == Integer.valueOf(key[3])){
			list.add("不限制");
		}else {
			list.add(key[3]);
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	
}
	
