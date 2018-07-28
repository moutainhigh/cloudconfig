package com.wjh.utils.mail;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 
 * Java邮件发送类
 * 
 * */


public class JavaMailUtil {
	//邮件服务器
	private static String mail_host="smtp.yeah.net";
	//邮件服务器端口
	private static int port=25;
	//邮件协议
	private static String mail_transport_protocol="smtp";
	//需要smtp验证
	private static String mail_smtp_auth="true";
	//发送人用户名
	private static String username="wujianhui321@yeah.net";
	//发送人密码
	private static String password="xxxxxx";
	
	
	
	//发送纯文本邮件
	public static boolean sendSimpleMail(String from,String to,String subject,String content) {
		boolean result=true;
		try {
		Properties prop=new Properties();
		prop.setProperty("mail.host", mail_host);
		prop.setProperty("mail.transport.protocol", mail_transport_protocol);
		prop.setProperty("mail.smtp.auth", mail_smtp_auth);
		Session session=Session.getInstance(prop);
		//设置为debug模式 ，生产环境应该关掉
		session.setDebug(true);
		//获得transport对象
		Transport ts=session.getTransport();
		
		//连上邮件服务器
		ts.connect(mail_host,port, username,password);
		//创建邮件
		MimeMessage message=new MimeMessage(session);
		//设置发送人
		message.setFrom(new InternetAddress(from));
		
		//设置接收人
		message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
		//设置主题
		message.setSubject(subject);
		//设置内容
		message.setContent(content,"text/html;charset=UTF-8");	
		//发送邮件
		ts.sendMessage(message, message.getAllRecipients());
		//关闭连接
		ts.close();
		} catch (Exception e) {
			e.printStackTrace();
			result=false;
		}
		
	    return result;
	}
	
	
	//发送带图片的邮件
	public static boolean sendImageMail(String from,String to,String subject,String absoluteImagePath) throws Exception{
		boolean result=true;
		
		try {
		
		//获取图片后缀	
		String suffix=absoluteImagePath.substring(absoluteImagePath.lastIndexOf(".")+1,absoluteImagePath.length())	;
		//定义contentId,图片到达邮件服务器后会有一个contentId,然后html中可以引用这个contentId显示该图片。
		String contentId="1."+suffix;
		//内容模板
		String content="带图片的邮件<img src='cid:"+contentId+"'/>";
	
		Properties prop=new Properties();
		prop.setProperty("mail.host", mail_host);
		prop.setProperty("mail.transport.protocol", mail_transport_protocol);
		prop.setProperty("mail.smtp.auth", mail_smtp_auth);
		Session session=Session.getInstance(prop);
		//设置为debug模式 ，生产环境应该关掉
		session.setDebug(true);
		//获得transport对象
		Transport ts=session.getTransport();
		//连上邮件服务器
		ts.connect(mail_host,port, username,password);
		//创建邮件
		MimeMessage message=new MimeMessage(session);
		//设置发送人
		message.setFrom(new InternetAddress(from));
		//设置接收人
		message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
		//设置标题
		message.setSubject(subject);
		//创建正文
		MimeBodyPart text=new MimeBodyPart();
		//设置内容
		text.setContent(content,"text/html;charset=UTF8");
		//创建一个图片
		MimeBodyPart image=new MimeBodyPart();
		//从本地absoluteImagePath文件读取
		DataHandler dh=new DataHandler(new FileDataSource(absoluteImagePath));
		image.setDataHandler(dh);
		//设置图片的contentId
		image.setContentID(contentId);
		//创建容器描述数据关系
		javax.mail.internet.MimeMultipart mm=new MimeMultipart();
		//容器添加内容
		mm.addBodyPart(text);
		//容器添加图片
		mm.addBodyPart(image);
		mm.setSubType("related");
		message.setContent(mm);
		message.saveChanges();
		//存储创建的邮件
		message.writeTo(new FileOutputStream("F:/imagemail.eml"));
		ts.sendMessage(message, message.getAllRecipients());
		ts.close();
		} catch (Exception e) {
			e.printStackTrace();
			result=false;
		}
		
	    return result;
	}
	
	
	
	//发送带附件的邮件
	public static boolean sendAttachMail(String from,String to,String subject,String content,List<String>  absoluteFilePathList) throws Exception{
		boolean result=true;
		try {
		Properties prop=new Properties();
		prop.setProperty("mail.host", mail_host);
		prop.setProperty("mail.transport.protocol", mail_transport_protocol);
		prop.setProperty("mail.smtp.auth", mail_smtp_auth);
		Session session=Session.getInstance(prop);
		//设置为debug模式 ，生产环境应该关掉
		session.setDebug(true);
		//获得transport对象
		Transport ts=session.getTransport();
		//连接邮件服务器
		ts.connect(mail_host,port, username,password);
		//创建邮件
		MimeMessage message=new MimeMessage(session);
		//设置发送人
		message.setFrom(new InternetAddress(from));
		//设置接收人
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		//设置主题
		message.setSubject(subject);
		//创建正文
		MimeBodyPart text=new MimeBodyPart();
		//设置正文内容
		text.setContent(content,"text/html;charset=UTF-8");
		//创建容器描述关系
		MimeMultipart mp=new MimeMultipart();
		//添加正文
		mp.addBodyPart(text);
		
		for(int i=0;i<absoluteFilePathList.size();i++){
			//创建附件
			MimeBodyPart attach=new MimeBodyPart();
			DataHandler dh=new DataHandler(new FileDataSource(absoluteFilePathList.get(i)));
			attach.setDataHandler(dh);
			attach.setFileName(dh.getName());
			//容器添加附件
			mp.addBodyPart(attach);
		}
		mp.setSubType("mixed");
		
		message.setContent(mp);
		message.saveChanges();
		//保存邮件至F盘
		message.writeTo(new FileOutputStream("F:/attachMail.eml"));
		ts.sendMessage(message, message.getAllRecipients());
		ts.close();
		} catch (Exception e) {
			e.printStackTrace();
			result=false;
		}
		
	    return result;
	}
	
	
	
	
	
	
	public static void main(String[] args) throws Exception {
	   sendSimpleMail("wujianhui321@yeah.net", "wujianhui321@yeah.net", "你好", "你好呀");
	   sendImageMail("wujianhui321@yeah.net", "wujianhui321@yeah.net", "带图片的邮件", "F:/test.gif");
		List list=new ArrayList<String>();
		list.add("F:/test.gif");
		list.add("F:/sparkTest.zip");
		sendAttachMail("wujianhui321@yeah.net", "wujianhui321@yeah.net", "带附件的邮件" ,"内容", list);
	}
	
}
