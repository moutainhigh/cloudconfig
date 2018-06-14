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
	private void toUserSchedule() {
	}
	
	//获取明天的日期
	private static String getTomorrowDate(int days) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, days);
		return sdf.format(cal.getTime());
	}
	
	//获取几分钟后
	private static String getTomorrowMinute(int minute) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, minute);
		return sdf.format(cal.getTime());
	}
	public static void main(String[] args) throws MessagingException {
		String toContent ="乔杉，您好！</br>" +
				"您有一个新的行程通知<br>" +
				"行程类型：走访企业<br>" +
				"行程名称：张三（资本军师）<br>" +
				"行程时间：2017-12-15 至 2017-12-17<br>" +
				"行程地点：广东省-深圳市<br>" +
				"行程人员：张三、李四、王五<br>" +
				"备注：带好身份证等证件<br>" +
				"创建人：张三<br>" +
				"点击以下链接登录可查看详情：<br>" +
				"<a href='"+PropertiesUtil.USER_LOGIN_HREF+"'>"+PropertiesUtil.USER_LOGIN_HREF+"(CRM链接地址)</a></br>"+
				"<div style='font-weight: bold;margin-bottom:8px;'>我的行程汇总</div>"+
				"2017-12-15 至 2017-12-17<br>"+
				"宋纪元 广东开能环保能源有限公司<br>" +
				"广东省-深圳市<br>"+
				"<div style='border-bottom:1px solid #ccc;height:1px;width:100%;margin-bottom:8px;margin-top:5px;'></div>"+
				"2017-12-15 至 2017-12-17<br>"+
				"宋纪元 广东开能环保能源有限公司<br>" +
				"广东省 - 深圳市<br>"+
				"<div style='border-bottom:1px solid #ccc;height:1px;width:100%;margin-bottom:8px;margin-top:5px;'></div>";
		MailUtils.send("1042442240@qq.com", "行程通知 ", toContent);

	}

}
