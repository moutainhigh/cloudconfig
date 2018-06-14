package com.kuangchi.sdd.attendanceConsole.quartz.service.impl;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.attendException.model.Param;
import com.kuangchi.sdd.attendanceConsole.attendException.model.ToEmailAddr;
import com.kuangchi.sdd.attendanceConsole.attendException.service.IAttendExceptionService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.kuangchi.sdd.util.mail.service.MailService;

@Service("emailSendQuartzJob")
public class EmailSendQuartz {
	
	public static final Logger LOG = Logger.getLogger(EmailSendQuartz.class);

	@Resource(name = "attendExceptionServiceImpl")
	private IAttendExceptionService attendExceptionService;
	
	@Resource(name = "MailServiceImpl")
	private MailService mailService;

	@Resource(name="cronServiceImpl")
	private ICronService cronService;
	    
	public void sendEmail() {
		//集群访问时，只有与数据库中相同的IP地址可以执行页面定时器的业务操作
		boolean r = cronService.compareIP();	
		if(r){
			Map<String, String> urlMap = new HashMap<String, String>();
			urlMap = PropertiesToMap.propertyToMap("mail.properties");
			String sendAutomaticallyPerDay = urlMap.get("sendAutomaticallyPerDay");
			if ("1".equals(sendAutomaticallyPerDay)) {
	
				Param param = new Param();
				param.setStaff_num(null);// 保证参数要么有数据，要么为null
				param.setStaff_name(null);
				param.setBegin_time(null);
				param.setEnd_time(null);
				JsonResult result = new JsonResult();
				boolean existFail = false;// 标志是否存在发送失败的邮件
				boolean existSucc = false;// 标志是否存在发送成功的邮件
	
				List<String> staff_nums = attendExceptionService
						.getStaffNumByParam(param);
				String staff_nums2 = "";
				if (staff_nums != null && staff_nums.size() > 0) {
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
							.getEmailAddr((staff_nums2 == null || staff_nums2 == "") ? "''"
									: staff_nums2);// 根据员工编号获得Email地址
	
					if (toEmailAddr != null && toEmailAddr.size() > 0) {// 判断是否拿到符合查询条件的email地址
						for (ToEmailAddr emailAddr : toEmailAddr) {
							String code = "2";
							if (emailAddr.getStaff_email() != null
									&& !("".equals(emailAddr.getStaff_email()))) {
								code = mailService
										.send_Check_Email(emailAddr, null);// 发邮件
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
						} else if (existSucc == true && existFail == false) {
							result.setSuccess(true);
							result.setMsg("邮件发送成功");
						} else {
							result.setSuccess(false);
							result.setMsg("邮件发送失败(邮箱地址不正确、为空或重复发送)");
						}
					} else {
						result.setSuccess(false);
						result.setMsg("发送失败(请勿重复发送)");
					}
				} else {
					result.setSuccess(false);
					result.setMsg("找不到符合条件的员工");
				}
				LOG.info("每日提醒邮件发送结果为：" + result.getMsg());
			}else{
				LOG.info("每日自动发送邮件功能已停止");
			}
		}
	}

	
}
