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
		
		System.out.println("定時任務來了------------------------------------");
		String time = DateUtils.currtime();
		System.out.println("时间："+time);
		Connection con = null;
		Statement statement = null,statement2=null,statement3=null,statement4=null,statement5=null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(url, username, password);
				statement = con.createStatement();
				statement2 = con.createStatement();
				statement3 = con.createStatement();

				//任务推送开始
				statement4 = con.createStatement();
				statement5 = con.createStatement();
				String taskLevels [] = {"开心就好","1（不重要不紧急）","2（重要不紧急）","3（重要且紧急)"};
				String taskSql = "select *,(select uname from dc_user where id = aa.createdBy)auctionUname from (select t.id,t.taskName,t.taskLevel,t.remind,t.taskDetail,t.startDate,t.endDate,t.createdBy,date_add(t.startDate,interval -remind minute)time from dc_task t where t.status = '0' and t.remind = '30')aa where time = '"+time+"'";
				ResultSet taskRs = statement4.executeQuery(taskSql);
				while (taskRs.next()) {
					String taskUserSql = "select u.uname,u.mobile,u.email,u.id from dc_task_user tu LEFT JOIN dc_user u on u.id = tu.userId  where tu.taskId = '"+taskRs.getString("id")+"' and u.status = 0 and (u.mobile is not null or u.email is not null)";
					ResultSet taskUserRs = statement5.executeQuery(taskUserSql);
					Map<String,String> pushTaskUser = new HashMap<>();
					String uname = null;
					while (taskUserRs.next()){
						if(StringUtils.isNotBlank(taskUserRs.getString("mobile"))){
							pushTaskUser.put(taskUserRs.getString("mobile"),taskUserRs.getString("uname"));
						}
						if(StringUtils.isNotBlank(taskUserRs.getString("email"))){
							pushTaskUser.put(taskUserRs.getString("email"),taskUserRs.getString("uname"));
						}
						uname=uname == null?taskUserRs.getString("uname"):uname+","+taskUserRs.getString("uname");
					}
					String toMobile =
							"【蝌蚪智慧】您有一个任务即将开始，请提前做好安排；" +
									"任务名称："+taskRs.getString("taskName")+"；" +
									"任务级别："+taskLevels[Integer.valueOf(taskRs.getInt("taskLevel"))]+"；" +
									//(StringUtils.isBlank(taskRs.getString("taskDetail"))?"任务说明：无；":(("任务说明："+taskRs.getString("taskDetail"))+"；")) +
									"任务时间："+taskRs.getString("startDate")+" 至 "+taskRs.getString("endDate")+"；" +
									"参与人员："+uname+"；" +
									"创建人："+taskRs.getString("auctionUname")+"。" ;
					String toEamil ="，您好！</br>" +
							"您有一个任务即将开始，请提前做好安排<br>" +
							"任务名称："+taskRs.getString("taskName")+"<br>" +
							"任务级别："+taskLevels[Integer.valueOf(taskRs.getInt("taskLevel"))]+"<br>" +
							(StringUtils.isBlank(taskRs.getString("taskDetail"))?"任务说明：无<br>":(("任务说明："+taskRs.getString("taskDetail"))+"<br>")) +
							"任务时间："+taskRs.getString("startDate")+" 至 "+taskRs.getString("endDate")+"<br>" +
							"参与人员："+uname+"<br>" +
							"创建人："+taskRs.getString("auctionUname")+"<br>" +
							"点击以下链接登录可查看详情：<br>" +
							"<a href='"+PropertiesUtil.USER_LOGIN_HREF+"'>"+PropertiesUtil.USER_LOGIN_HREF+"(CRM链接地址)</a></br>";
					for (String key : pushTaskUser.keySet()) {
						if(key.contains("@")){
							MailUtils.send(key, "任务通知 ", pushTaskUser.get(key)+toEamil);
						}else{
							System.out.println("key= "+ key + " and value= " + pushTaskUser.get(key));
							System.out.println(toMobile);
							SmsApi.sendSms(key, toMobile);

						}
					}
				}
				//任务推送结束

				//行程推送开始
				//String sql = "select s.id,s.province,s.city,s.lessonType,s.lessonName,s.startDate,s.endDate from dc_schedule s  where s.startDate like '"+time+"%'  and s.status = 0";
				String sql = "select *,(select uname from dc_user where id = aa.updatedBy)auctionUname from (select s.id,s.province,s.city,s.lessonType,s.lessonName,s.startDate,s.endDate,s.scheduleDetail,s.updatedBy,date_add(s.startDate,interval -s.remind minute)time from dc_schedule s where s.remind > 0 and s.status = '0')aa where time = '"+time+"'";
				ResultSet rs = statement.executeQuery(sql);
				System.out.println(sql);
				Map<String,String> userScList = new HashMap<>();
				while (rs.next()) {
					String province = rs.getString("province");
					
					String city = rs.getString("city");
					String lessonType = rs.getString("lessonType");
					String lessonName = rs.getString("lessonName");
					String startDate = rs.getString("startDate");
					String endDate = rs.getString("endDate");
					String scheduleDetail = rs.getString("scheduleDetail");
					String loginUname = rs.getString("auctionUname");
					
	
					String sql2 = "select u.uname,u.mobile,u.email,u.id from dc_schedule_user su LEFT JOIN dc_user u on u.id = su.userId  where su.scheduleId = '"+rs.getString("id")+"' and u.status = 0 and (u.mobile is not null or u.email is not null)";
					System.out.println(sql2);
					ResultSet rs2 = statement2.executeQuery(sql2);
					Map<String, String> map = new HashMap<>();
					String uname = "";
					String jieshouUname = "";
					while (rs2.next()) {
						String email = rs2.getString("email");
						String mobile = rs2.getString("mobile");
						String userId = rs2.getString("id");
						jieshouUname = rs2.getString("uname");
						uname += jieshouUname+",";


						if(StringUtils.isNotBlank(mobile)){
							map.put(mobile, uname);
						}
						if(StringUtils.isNotBlank(email)){
							map.put(email, uname);
							if(StringUtils.isBlank(userScList.get(email))){
								String queryUserSc = "select   startDate,endDate,lessonName,province,city from dc_schedule s  where status = 0 and startDate >= now() and ( s.id in (SELECT sc.scheduleId  from dc_schedule_user sc where sc.scheduleId = s.id and sc.userId  = '"+userId+"'))order by startDate asc";
								System.out.println(queryUserSc);
								ResultSet rs3 = statement3.executeQuery(queryUserSc);
								if(rs3.next()){
									String huizong = "<div style='font-weight: bold;margin-bottom:8px;'>我的行程计划</div>";
									while (rs3.next()) {
										huizong += rs3.getString("startDate")+" 至 "+rs3.getString("endDate")+"<br>"+
												rs3.getString("lessonName")+"<br>" +
												rs3.getString("province")+" - "+rs3.getString("city")+"<br>"+
												"<div style='border-bottom:1px solid #ccc;height:1px;width:100%;margin-bottom:8px;margin-top:5px;'></div>";
									}
									userScList.put(email,huizong);
								}
							}
						}

					}
					uname = StringUtils.isNotBlank(uname)?uname.substring(0, uname.length()-1):"";

					String toMobile =
							"【蝌蚪智慧】 您有一个行程即将开始，请提前做好安排；" +
									"行程类型："+lessonType+"；" +
									"行程名称："+lessonName+"；" +
									"行程时间："+startDate+" 至 "+endDate+"；" +
									"行程地点："+province+"-"+city+"；" +
									"行程人员："+uname+"；" +
									(StringUtils.isBlank(scheduleDetail)?"备注：无":(("备注："+scheduleDetail)+"。"));
					String toEamil ="，您好！</br>" +
							"您有一个行程即将开始，请提前做好安排<br>" +
							"行程类型："+lessonType+"<br>" +
							"行程名称："+lessonName+"<br>" +
							"行程时间："+startDate+" 至 "+endDate+"<br>" +
							"行程地点："+province+" - "+city+"<br>" +
							"行程人员："+uname+"<br>" +
							(StringUtils.isBlank(scheduleDetail)?"备注：无":(("备注："+scheduleDetail)))+"<br>"+
							"创建人："+loginUname+"<br>" +
							"点击以下链接登录可查看详情：<br>" +
							"<a href='"+PropertiesUtil.USER_LOGIN_HREF+"'>"+PropertiesUtil.USER_LOGIN_HREF+"(CRM链接地址)</a></br>";
					for (String key : map.keySet()) {


						if(StringUtils.isNotBlank(key)){
							if(key.contains("@")){
								System.out.println("key= "+ key + " and value= " + map.get(key));
								MailUtils.send(key, "行程通知 ", jieshouUname+toEamil+userScList.get(key));
							}else{
								System.out.println("key= "+ key + " and value= " + map.get(key));
								SmsApi.sendSms(key, toMobile);

							}
						}
					 }
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
