package com.xkd.task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xkd.utils.DateUtils;
import com.xkd.utils.MailUtils;
import com.xkd.utils.PropertiesUtil;
import com.xkd.utils.SmsApi;

import javax.mail.MessagingException;

@Component
public class ScheduleTask {
	
	public final static String url = PropertiesUtil.DB_URL;
	public final static String username = PropertiesUtil.DB_USER;
	public final static String password = PropertiesUtil.DB_PWD;
	


	//@Scheduled(cron = "0 * 15 * * ?") //在每天10点到10:59期间的每1分钟触发
	//@Scheduled(cron = "0 0 9 ? * *") //每天上午9點触发 
	
	@Scheduled(cron = "0 0,30 * * * ?")//每2分钟触发
	private void toUserSchedule() {}


}
