package com.xkd.task;

import com.xkd.mapper.ScheduleUserMapper;
import com.xkd.model.UserAction;
import com.xkd.utils.DateUtils;
import com.xkd.utils.PropertiesUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
public class PushTask {

	@Autowired
	private ScheduleUserMapper ScheduleUserMapper;

	public final static String url = PropertiesUtil.DB_URL;
	public final static String username = PropertiesUtil.DB_USER;
	public final static String password = PropertiesUtil.DB_PWD;

	//@Scheduled(cron = "0 * 15 * * ?") //在每天10点到10:59期间的每1分钟触发
	//@Scheduled(cron = "0 0 9 ? * *") //每天上午9點触发

	
	@Scheduled(cron = "0 0 9 ? * MON")//每周一凌晨9点
	private void toUserSchedule() {
		String week[] = {"","一","二","三","四","五","六"};

		System.out.println("客户消息推送定時任務來了------------------------------------");
		String time = DateUtils.currtime();
		System.out.println("时间："+time);
		String month = DateUtils.getYearMoth();
		Connection con = null;
		Statement statement = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(url, username, password);
				statement = con.createStatement();
				String sql = "select t2.userId,count(1) as count from (select * from ("+
							" select d.companyAdviserId as userId,d.id as companyId from dc_company d   where  d.status=0  and (d.latestContactTime<'"+month+"' or d.latestContactTime is null)  and    d.companyAdviserId is not null and d.companyAdviserId!=''   and d.pcCompanyId='3' and d.userLevel='A类'"+
							" union all "+
							" select d.companyDirectorId as userId, d.id as companyId    from dc_company d  where  d.status=0 and (d.latestContactTime<'"+month+"' or d.latestContactTime is null) and d.companyDirectorId is not null and d.companyDirectorId!='' and d.pcCompanyId='3'  and d.userLevel='A类'"+
							" UNION all "+
							" select ru.userId as userId,ru.companyId as companyId from dc_company_relative_user ru left join dc_company d on ru.companyId=d.id  where  d.status=0   and (d.latestContactTime<'"+time+"' or d.latestContactTime is null) and d.pcCompanyId='3'    and d.userLevel='A类'  )  t1    group by concat(userId,companyId)"+
							" ) t2 group by t2.userId";
				System.out.println(sql);
				ResultSet rs = statement.executeQuery(sql);
				UserAction userAction = new UserAction();
				userAction.setActionType("2");
				userAction.setCreateDate(time);
				userAction.setReadStatus("0");
				userAction.setActionTitle("客户通知");
				String date = DateUtils.getYearMoth().replaceFirst("-","年")+"月";
				while (rs.next()){
					String userId = rs.getString("userId");

					userAction.setActionContent(" 今天是"+date+"第"+week[DateUtils.getWeek(time)]+"周，截止目前，仍有 "+rs.getString("count")+" 个A级客户未沟通。");
					userAction.setActionId(userId);
					userAction.setActionUserId(userId);
					userAction.setCreatedBy(userId);
					ScheduleUserMapper.saveUserAction(userAction);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					statement.close();
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
	}

	public static void main(String[] args) throws ParseException {
		System.out.println( DateUtils.getYearMoth().replaceFirst("-","年")+"月");
		Map<String,String> week = new HashedMap();
		week.put("1","一");
		week.put("2","二");
		week.put("3","三");
		week.put("4","四");
		week.put("5","五");
		week.put("6","六");
		System.out.println(DateUtils.getWeek("2018-01-08"));
		System.out.println(DateUtils.getWeek("2018-01-31"));
		System.out.println(DateUtils.getWeek("2018-02-01"));
		System.out.println(DateUtils.getWeek("2018-02-06"));



	}



}
