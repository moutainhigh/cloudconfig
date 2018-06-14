package com.kuangchi.sdd.util.mail.service.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.kuangchi.sdd.attendanceConsole.attendException.model.ToEmailAddr;
import com.kuangchi.sdd.attendanceConsole.attendException.service.IAttendExceptionService;
import com.kuangchi.sdd.util.mail.service.MailService;
import com.opensymphony.xwork2.ActionContext;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @创建人:邓学文<br>
 * @创建时间:2016-5-13 <br>
 * @功能描述:邮件业务实现类 <br>
 */
@Service("MailServiceImpl")
public class MailServiceImpl implements MailService {

	private Logger logger = Logger.getLogger(MailServiceImpl.class);

	@Resource(name = "mailSender")
	private JavaMailSender mailSender;

	@Resource(name = "freeMarker")
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Resource(name = "simpleMailMessage")
	private SimpleMailMessage simpleMailMessage;

	@Resource(name = "attendExceptionServiceImpl")
	private IAttendExceptionService attendExceptionService;

	/**
	 * @创建人:邓学文<br>
	 * @创建时间:2016-5-13 <br>
	 * @功能描述:发送验证码邮件 <br>
	 * @参数描述:@to 收件人 @parameter freemarker模板参数
	 */
	@Override
	public String send_Check_Email(ToEmailAddr toEmailAddr, Map parameter) {
		MimeMessage msg = null;
		MimeMessageHelper helper = null;
		try {
			msg = mailSender.createMimeMessage();
			helper = new MimeMessageHelper(msg, true, "GBK");
			helper.setFrom(MimeUtility.encodeText("系统管理员") + "<"
					+ simpleMailMessage.getFrom() + ">");// 提取发件人账户（昵称+<账户>）
			helper.setTo(toEmailAddr.getStaff_email());
			helper.setSubject(MimeUtility.encodeText("考勤异常"));
			helper.setText(getMailHtml("check_Email.ftl", parameter), true); // true表示text的内容为html
			mailSender.send(msg);
			attendExceptionService.setDealState(toEmailAddr.getStaff_num());
			return "0";
		} catch (Exception e) {
			logger.error("发送：[考勤异常邮件 ] 失败！收件人账户为："
					+ toEmailAddr.getStaff_email() + "");
			logger.error(e.getMessage(), e);
			return "1";
		}
	}

	/**
	 * @创建人:杨金林 <br>
	 * @创建时间:2015-4-24 <br>
	 * @修改人:邓学文<br>
	 * @修改时间:2016-5-13 <br>
	 * @功能描述:使用freemarker模板生成html邮件内容 <br>
	 * @参数描述:@TemplateName 模板文件名 @parameter freemarker模板参数
	 */
	private String getMailHtml(String TemplateName, Map parameter)
			throws IOException, TemplateException {
		// 通过指定模板名获取FreeMarker模板实例
		Template tpl = freeMarkerConfigurer.getConfiguration().getTemplate(
				TemplateName);
		// 解析模板并替换动态数据，最终转为html格式内容
		String html = FreeMarkerTemplateUtils.processTemplateIntoString(tpl,
				parameter);
		return html;
	}

	@Override
	public String send_RandCode_Email(String userEmail, Map parameter) {
		MimeMessage msg = null;
		MimeMessageHelper helper = null;
		try {
			msg = mailSender.createMimeMessage();
			helper = new MimeMessageHelper(msg, true, "GBK");
			helper.setFrom(MimeUtility.encodeText("系统管理员") + "<"
					+ simpleMailMessage.getFrom() + ">");// 提取发件人账户（昵称+<账户>）
			helper.setTo(userEmail);
			helper.setSubject(MimeUtility.encodeText("用户找回密码操作"));
			helper.setText(getMailHtml("randomCodeEmail.ftl", parameter), true); // true表示text的内容为html
			mailSender.send(msg);
			return "0";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "1";
		}
		
	}

	@Override
	public void sendFkOverTimeEmail(String emailAddr, Map parameter) {
			InetAddress addr;
			try {
				addr = InetAddress.getLocalHost();
		final String messageText=parameter.get("name")+"先生/小姐:</br/></br/>" +
		          "&nbsp;&nbsp;&nbsp;&nbsp;您好!目前已有"+parameter.get("total")+"位访客访问出现超时现象，请登录访客系统查阅详情。" +
		          		"</br/></br/>系统邮件，请勿回复！";
		final String receiverEmail=emailAddr;
		new Thread(new Runnable(){
			public void run(){
				MimeMessage msg = null;
				MimeMessageHelper helper = null;
				try {
					msg = mailSender.createMimeMessage();
					helper = new MimeMessageHelper(msg, true, "UTF-8");
					helper.setFrom(MimeUtility.encodeText("系统管理员") + "<"
							+ simpleMailMessage.getFrom() + ">");
					helper.setTo(receiverEmail);
					helper.setSubject(MimeUtility.encodeText("访客超时提醒"));
					helper.setText(messageText, true); 
					mailSender.send(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
	}

}
