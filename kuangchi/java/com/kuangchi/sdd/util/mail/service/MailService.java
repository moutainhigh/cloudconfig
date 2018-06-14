package com.kuangchi.sdd.util.mail.service;



import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.attendException.model.ToEmailAddr;

/**
 * @创建人:杨金林 <br>
 * @创建时间:2015-9-8 16:37:07 <br>
 * @修改人人:邓学文<br>
 * @创建时间:2015-5-16 <br>
 * @功能描述:发送邮件业务类 <br>
 */
public interface MailService {

	/** 发送考勤异常邮件 */
	public String send_Check_Email(ToEmailAddr toEmailAddr, Map parameter);
	
	/** 发送随机验证码邮件 */
	public String send_RandCode_Email(String userEmail,Map parameter);
	
	/**
	 * 发送访客超时提醒邮件
	 * by gengji.yang
	 */
	public void sendFkOverTimeEmail(String emailAddr,Map parameter);
}
