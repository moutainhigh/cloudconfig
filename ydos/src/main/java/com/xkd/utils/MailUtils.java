package com.xkd.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.StringUtils;

public class MailUtils {

	/**
	 * 发送邮件
	 * @param to_mail  接收方邮箱地址
	 * @param title	   邮件名称
	 * @param content 邮件内容
	 */
	public static void send(String to_mail, String title, String content) {
		
		
		
		//配置发送邮件的环境属性
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");//表示以SMTP方式发送邮件
		
		props.setProperty("mail.smtp.host", PropertiesUtil.MAIL_SMTP_HOST);//163邮箱发送
//		props.setProperty("mail.smtp.port", "25");//发送邮箱端口
		props.setProperty("mail.smtp.port", "465");//发送邮箱端口       SSL
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  //SSL  需要
		props.setProperty("mail.smtp.socketFactory.port", "465");   //SSL 需要
		props.setProperty("mail.smtp.auth", "true");//启用SSL 
		props.setProperty("mail.smtp.starttls.enable", "true");	
		props.setProperty("mail.smtp.auth", "true");// 需要进行身份验证
		// 构建授权信息，用于进行SMTP进行身份验证
		
		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(PropertiesUtil.MAIL_USER_CODE,PropertiesUtil.MAIL_USER_PWD);
			}
			
		};
		//Session session = Session.getDefaultInstance(props, auth);
		// 使用环境属性和授权信息，创建邮件会话
		Session session = Session.getInstance(props, auth);
		// 创建邮件消息
		Message message = new MimeMessage(session);
		Address from;
		Address to;
		try {
			
			if(StringUtils.isBlank(to_mail)){
				
				throw  new Exception("邮箱不能为空！");
			}
			
			// 设置发件人
			from = new InternetAddress(PropertiesUtil.MAIL_USER_CODE);
			message.setFrom(from);
			// 设置抄送
			to = new InternetAddress(to_mail);
			message.setRecipient(RecipientType.TO, to);
			message.setSubject(title);
			// 设置邮件的内容体
			BodyPart bodyPart = new MimeBodyPart();
			bodyPart.setContent(content, "text/html;charset=UTF-8");
			MimeMultipart multipart = new MimeMultipart();
			multipart.addBodyPart(bodyPart);
			message.setContent(multipart);
			message.setSentDate(new Date());
			Transport tran = session.getTransport();
			Transport.send(message);// 发送邮件
			System.out.println("发送成功="+to_mail+"   "+content);
			tran.close();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		MailUtils.send("745550948@qq.com", "a", "a");
	}
	
}
