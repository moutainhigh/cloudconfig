package com.kuangchi.sdd.attendanceConsole.attendException.action;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.attendanceConsole.attendException.model.AttendException;
import com.kuangchi.sdd.attendanceConsole.attendException.model.Param;
import com.kuangchi.sdd.attendanceConsole.attendException.model.ToEmailAddr;
import com.kuangchi.sdd.attendanceConsole.attendException.service.IAttendExceptionService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.holiday.util.ExcelUtilSpecial;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.staffUser.model.Staff;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;
import com.kuangchi.sdd.util.file.PropertyUtils;
import com.kuangchi.sdd.util.mail.service.MailService;

@Controller("attendExceptionAction")
public class AttendExceptionAction extends BaseActionSupport {

	private static final Logger LOG = Logger.getLogger(AttendException.class);

	private AttendException model;

	public AttendExceptionAction() { 
		model = new AttendException();
	}

	private static final long serialVersionUID = -4559409507804703403L;
	@Resource(name = "departmentServiceImpl")
	private IDepartmentService departmentService;
	@Resource(name = "attendExceptionServiceImpl")
	private IAttendExceptionService attendExceptionService;
	@Resource(name = "MailServiceImpl")
	private MailService mailService;
	@Resource(name="startQuertz")
	private Scheduler scheduler;
	@Resource(name = "employeeService")
	private EmployeeService employeeService;
	@Resource(name = "roleServiceImpl")
	private IRoleService roleService;
	
	@Override
	public Object getModel() {
		return model;
	}

	/**
	 * 响应前台请求
	 */
	public String toMyAttendException() {
		return "success";
	}

	/**
	 * 响应前台请求
	 */
	public String toMyAttendException2() {
		return "success";
	}

	/**
	 * Description:根据员工编号发送邮件 date:2016年5月16日
	 */
	public void sendEmailById() {
		String staff_num = getHttpServletRequest().getParameter("data_ids");
		JsonResult result = new JsonResult();
		boolean existFail = false;// 标志是否存在发送失败的邮件
		boolean existSucc = false;// 标志是否存在发送成功的邮件
		
		List<ToEmailAddr> toEmailAddr = attendExceptionService
				.getEmailAddr(staff_num);// 根据员工编号获得Email地址

		// 发邮件
		if (toEmailAddr!=null&&toEmailAddr.size()>0) {
			for (ToEmailAddr emailAddr : toEmailAddr) {
				String code = "2";
				if (emailAddr.getStaff_email() != null
						&& !("".equals(emailAddr.getStaff_email()))) {
					code = mailService.send_Check_Email(emailAddr, null);
				}
				if ("0".equals(code)) {
					existSucc = true;
				} else {
					existFail = true;
				}
			}

			if (existSucc == true && existFail == true) {
				result.setSuccess(false);
				result.setMsg("邮件部分发送失败(邮箱地址不正确或为空)");
				printHttpServletResponse(GsonUtil.toJson(result));
			} else if (existSucc == true && existFail == false) {
				result.setSuccess(true);
				result.setMsg("邮件发送成功");
				printHttpServletResponse(GsonUtil.toJson(result));
			} else {
				result.setSuccess(false);
				result.setMsg("邮件发送失败(邮箱地址不正确、为空或重复发送)");
				printHttpServletResponse(GsonUtil.toJson(result));
			}
		} else {
			result.setSuccess(false);
			result.setMsg("发送失败(请勿重复发送)");
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}

	/**
	 * Description:根据查询条件群发邮件 date:2016年5月17日
	 * @throws UnsupportedEncodingException 
	 */
	public void sendEmailByParam() throws UnsupportedEncodingException {
		String staff_no = getHttpServletRequest().getParameter("staff_no");
		String staff_name = getHttpServletRequest().getParameter("staff_name");
		String begin_time = getHttpServletRequest().getParameter("searchStartDate");
		String end_time = getHttpServletRequest().getParameter("searchEndDate");
		if(staff_no!=null||staff_no!=""){staff_no = URLDecoder.decode(staff_no,"UTF-8").trim();}//对前台传来的数据进行解码
		if(staff_name!=null||staff_name!=""){staff_name = URLDecoder.decode(staff_name,"UTF-8").trim();}//对前台传来的数据进行解码
		if(begin_time!=null||begin_time!=""){begin_time = URLDecoder.decode(begin_time,"UTF-8");}//对前台传来的数据进行解码
		if(end_time!=null||end_time!=""){end_time = URLDecoder.decode(end_time,"UTF-8");}//对前台传来的数据进行解码
		Param param = new Param();
		String staff_num = employeeService.selectStaffNum(staff_no);
		param.setStaff_num(staff_num);
		param.setStaff_no(staff_no == "" ? null : staff_no);//保证参数要么有数据，要么为null
		param.setStaff_name(staff_name == "" ? null : staff_name);
		param.setBegin_time(begin_time == "" ? null : begin_time);
		param.setEnd_time(end_time == "" ? null : end_time);
		JsonResult result = new JsonResult();
		boolean existFail = false;// 标志是否存在发送失败的邮件
		boolean existSucc = false;// 标志是否存在发送成功的邮件

		List<String> staff_nums = attendExceptionService
				.getStaffNumByParam(param);
		String staff_nums2 = "";
		if (staff_nums != null&&staff_nums.size()>0) {
			boolean flag2 = false;// 标识是第一次的逗号拼接
			for (String str : staff_nums) {// 将所有员工编号拼接成字符串
				if (flag2 == false) {// 判断是否为第一次拼接
					staff_nums2 = "'" + str + "'";
					flag2 = true;
				} else {
					staff_nums2 = staff_nums2 + ",'" + str + "'";
				}
			}
			List<ToEmailAddr> toEmailAddr = attendExceptionService
					.getEmailAddr((staff_nums2==null||staff_nums2=="")?"''":staff_nums2);// 根据员工编号获得Email地址

			
			if (toEmailAddr!=null&&toEmailAddr.size()>0) {//判断是否拿到符合查询条件的email地址
				for (ToEmailAddr emailAddr : toEmailAddr) {
					String code = "2";
					if (emailAddr.getStaff_email() != null
							&& !("".equals(emailAddr.getStaff_email()))) {
						code = mailService.send_Check_Email(emailAddr, null);// 发邮件
					}
					if ("0".equals(code)) {
						existSucc = true;
					} else {
						existFail = true;
					}

				}
				if (existSucc == true && existFail == true) {
					result.setSuccess(false);
					result.setMsg("邮件部分发送失败(邮箱地址不正确或为空)");
					printHttpServletResponse(GsonUtil.toJson(result));
				} else if (existSucc == true && existFail == false) {
					result.setSuccess(true);
					result.setMsg("邮件发送成功");
					printHttpServletResponse(GsonUtil.toJson(result));
				} else {
					result.setSuccess(false);
					result.setMsg("邮件发送失败(邮箱地址不正确、为空或重复发送)");
					printHttpServletResponse(GsonUtil.toJson(result));
				}
			} else {
				result.setSuccess(false);
				result.setMsg("发送失败(请勿重复发送)");
				printHttpServletResponse(GsonUtil.toJson(result));
			}
		} else {
			result.setSuccess(false);
			result.setMsg("找不到符合条件的员工");
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}

	
	
	// 普通员工查询考勤异常记录
	public void getAllAttExcpByParam() {
		String page = getHttpServletRequest().getParameter("page");
		String rows = getHttpServletRequest().getParameter("rows");
		String data = getHttpServletRequest().getParameter("data");
		Param param = GsonUtil.toBean(data, Param.class);
		
		Staff staff=(Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);//获得当前登陆的用户代码（即员工编号）
		String loginUserDm=staff.getStaff_num();
		
		param.setStaff_num(loginUserDm);// 设置查询条件员工编号为当前员工编号
		Grid allCard = attendExceptionService.getAllAttExcpByParam(param, page,
				rows);
		printHttpServletResponse(new Gson().toJson(allCard));
	}

	// 管理员可以查询所有员工考勤异常记录
	public void getAllAttExcpByParam2() {
		String page = getHttpServletRequest().getParameter("page");
		String rows = getHttpServletRequest().getParameter("rows");
		String data = getHttpServletRequest().getParameter("data");
		Param param = GsonUtil.toBean(data, Param.class);
		if(param.getStaff_no()!=null){param.setStaff_no(param.getStaff_no().trim());}//去掉前后空格
		if(param.getStaff_name()!=null){param.setStaff_name(param.getStaff_name().trim());}//去掉前后空格
		//String staff_no = param.getStaff_no();
		//String staff_num = employeeService.selectStaffNum(staff_no);
		//param.setStaff_num(staff_num);
		
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		/*boolean isLayer = roleService.isLayer();
		if(isLayer){
			Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
			param.setJsDm((role.getJsDm()));
			
			User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
			param.setYhDm(user.getYhDm());
		} else {
			param.setJsDm("0");
		}*/
		
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		param.setLayerDeptNum(layerDeptNum);
		
		Grid allCard = attendExceptionService.getAllAttExcpByParam(param, page,
				rows);
		printHttpServletResponse(new Gson().toJson(allCard));
	}

	/**
	 * 按条件导出考勤异常数据
	 * 
	 */
	public void reportData() {
		// 文件名获取
        HttpServletResponse response = getHttpServletResponse();
        HttpServletRequest request = getHttpServletRequest();
        String data=request.getParameter("data");
        Param param=GsonUtil.toBean(data,Param.class);
        
        if(param.getStaff_no()!=null){param.setStaff_no(param.getStaff_no().trim());}//去掉前后空格
		if(param.getStaff_name()!=null){param.setStaff_name(param.getStaff_name().trim());}//去掉前后空格
		//String staff_no = param.getStaff_no();
		//String staff_num = employeeService.selectStaffNum(staff_no);
		//param.setStaff_num(staff_num);
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		/*boolean isLayer = roleService.isLayer();
		if(isLayer){
			Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
			param.setJsDm((role.getJsDm()));
			
			User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
			param.setYhDm(user.getYhDm());
		} else {
			param.setJsDm("0");
		}*/
		
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		param.setLayerDeptNum(layerDeptNum);
		
		List<AttendException> attendExceptions=attendExceptionService.exportAttExcpByParam(param);
        String jsonList=GsonUtil.toJson(attendExceptions);
		List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
      //设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		
		cloTitleList.add("员工工号");
		cloTitleList.add("员工名称");
		cloTitleList.add("日期");
		cloTitleList.add("异常时间");
		cloTitleList.add("异常考勤点");
		cloTitleList.add("异常原因");
//		cloTitleList.add("异常类型");
//		cloTitleList.add("异常总时长（分钟）");
//		cloTitleList.add("是否已发送");
//		cloTitleList.add("邮箱地址");
		
		colList.add("staff_no");
		colList.add("staff_name");
		colList.add("everyday_time");
		colList.add("time_point");
		colList.add("duty_time_type");
		colList.add("exception_type");
//		colList.add("duty_type");
//		colList.add("time_interval");
//		colList.add("deal_state");
//		colList.add("staff_email");
		
		String[] colTitles=new String[colList.size()];
		String[] cols=new String[colList.size()];
		for(int i=0;i<colList.size();i++){
			cols[i]=colList.get(i);
			colTitles[i]=cloTitleList.get(i);
		}
		
		
		OutputStream out = null;

		try {
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName="考勤异常信息表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecial.exportExcel("考勤异常信息表",
					colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


    /**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-17 下午3:43:57
	 * @功能描述: 设置邮件每日自动提醒
	 * @参数描述:
	 */
	public void changeSendEmailAutoSign(){
		  
    	HttpServletRequest request = getHttpServletRequest();
    	String sign = request.getParameter("sendEmailAutoSign");//获取前台传过来的设置标志
    	String interval = request.getParameter("interval");
    	
    	String signPropertyFile=request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/mail.properties");//获取配置文件绝对路径
    	String propertyFile = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
    	
    	String cronExpression = "0 0 0 1/" + interval + " * ?";
        boolean flag1=com.kuangchi.sdd.util.file.PropertyUtils.setProperties(signPropertyFile, "sendAutomaticallyPerDay",sign, null); // 设置是否定时
        boolean flag2 = com.kuangchi.sdd.util.file.PropertyUtils.setProperties(propertyFile, "attendExceptionEmailSendCron", cronExpression,null); // 设置调度频率
        
        JsonResult result = new JsonResult();
    	if(flag1 && flag2){
		     result.setSuccess(true);
		     result.setMsg("设置成功");
    	}else{
			result.setSuccess(false);
			result.setMsg("设置失败");
    	}
         printHttpServletResponse(GsonUtil.toJson(result));     	
    }
	
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-17 下午4:28:39
	 * @功能描述: 获取设置标志
	 * @参数描述:
	 */
	public void getSendEmailAutoSign(){
    	
    	HttpServletRequest request = getHttpServletRequest();
    	
    	String signPropertyFile=request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/mail.properties");//获取配置文件绝对路径
    	String sign = PropertyUtils.readProperties(signPropertyFile).getProperty("sendAutomaticallyPerDay");//获取配置文件里的属性的值（在tomcat上才能跑）
    	String propertyFile=request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");//获取配置文件绝对路径
    	String cron = PropertyUtils.readProperties(propertyFile).getProperty("attendExceptionEmailSendCron");//获取配置文件里的属性的值（在tomcat上才能跑）
    	
    	LOG.info("cron:---------------------"+cron);
    	int index=cron.lastIndexOf("/");
		String interval=cron.substring(index+1, index+3);
    	if("0".equals(sign)){
    		interval = "0";
    	}
    	
    	HashMap<String,String> map=new HashMap<String,String>();
        map.put("sendEmailAutoSign", interval);
        printHttpServletResponse(GsonUtil.toJson(map));
    }
	
	
}
