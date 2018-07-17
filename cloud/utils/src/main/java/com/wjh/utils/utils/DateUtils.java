package com.wjh.utils.utils;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author xiall
 * 
 */
public class DateUtils {
	
	
	/**
	 * 计算两个日期之间相差的天数
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 * test aa
	 */
	public static int daysBetween(Date smdate, Date bdate) {
		long between_days = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			smdate = sdf.parse(sdf.format(smdate));
			bdate = sdf.parse(sdf.format(bdate));
			Calendar cal = Calendar.getInstance();
			cal.setTime(smdate);
			long time1 = cal.getTimeInMillis();
			cal.setTime(bdate);
			long time2 = cal.getTimeInMillis();
			between_days = (time2 - time1) / (1000 * 3600 * 24);
		} catch (ParseException e) {
			System.out.println(e);
		}
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 计算两个日期之间相差的天数
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 * test aa
	 */
	public static int yearsBetween(Date smdate, Date bdate) {
 		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			smdate = sdf.parse(sdf.format(smdate));
			bdate = sdf.parse(sdf.format(bdate));
			Calendar cal = Calendar.getInstance();
			cal.setTime(smdate);
			Integer smYear=cal.get(Calendar.YEAR);
			cal.setTime(bdate);
			Integer bdYear=cal.get(Calendar.YEAR);
			return bdYear-smYear;
 		} catch (ParseException e) {
			System.out.println(e);
		}
		 return 0;
	}

	/**
	 * 计算两个日期之间相差的小时数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int hoursBetween(Date smdate, Date bdate) {
		long between_days = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			smdate = sdf.parse(sdf.format(smdate));
			bdate = sdf.parse(sdf.format(bdate));
			Calendar cal = Calendar.getInstance();
			cal.setTime(smdate);
			long time1 = cal.getTimeInMillis();
			cal.setTime(bdate);
			long time2 = cal.getTimeInMillis();
			between_days = (time2 - time1) / (1000 * 3600);
		} catch (ParseException e) {
			System.out.println(e);
		}
		return Integer.parseInt(String.valueOf(between_days));
	}
	public static String dateToString(Date date, String format) {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.format(date);
	}
	public static Date currtime(String date) throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.parse(date);
	}
	
	/**
	 * 得到当前时间,格式:yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String currtime() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(new Date());
	}

	/**
	 * 得到当前时间,格式:yyyy-MM
	 *
	 * @return
	 */
	public static String getYearMoth() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
		return sf.format(new Date());
	}

	/**
	 * 得到当前时间,格式:yyyy-MM
	 *
	 * @return
	 */
	public static int getWeek(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
		return weekOfMonth;
	}
	
	/**
	 * 得到当前时间,格式:yyyyMMddhhmmssSSS
	 * 
	 * @return
	 */
	public static long currtimeTolong19() {
		return Long.parseLong(dateToString(new Date(), "yyyyMMddhhmmssSSS"));
	}
	
	/**
	 * 得到当前时间,格式:yyyyMMddHHmmss
	 * 
	 * @return
	 */
	public static String currtimeToString14() {
		return dateToString(new Date(), "yyyyMMddHHmmss");
	}
	
	/**
	 * 得到当前时间,格式:yyMMddHHmmss
	 * 
	 * @return
	 */
	public static String currtimeToString12() {
		return dateToString(new Date(), "yyMMddHHmmss");
	}
	
	/**
	 * 得到当前时间,格式:yyyyMMdd
	 * 
	 * @return
	 */
	public static String currtimeToString8() {
		return dateToString(new Date(), "yyyyMMdd");
	}
	
	/**
	 * 得到当前时间,格式:yyMMdd
	 * 
	 * @return
	 */
	public static String currtimeToString6() {
		return dateToString(new Date(), "yyMMdd");
	}

	/**
	 * 得到当前时间指定格式的字符串
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurrTime(String format) {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.format(new Date());
	}
	
	
	/**
	 * 得到指定日期，几天前或几天后的日期
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date getAddDaysTime(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}
	
	/**
	 * 得到指定日期，几个小时前、后的日期
	 * @param date
	 * @param hours
	 * @return
	 */
	public static Date getAddHourseTime(Date date, int hours) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, hours);
		return cal.getTime();
	}
	
	/**
	 * 将字符串日期转换成Date
	 * @param dateStr
	 * @return
	 */
	public static Date getDateByDateString(String dateStr){
		
		
		if(StringUtils.isBlank(dateStr)){
			
			return null;
		}
		
		
		try {
			
			if(dateStr.contains("T")){
				
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				return simpleDateFormat.parse(dateStr);
			}
			
			if(!dateStr.contains("T")){
				
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return simpleDateFormat.parse(dateStr);
			}
			
		} catch (Exception e) {

			try{

				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
				return simpleDateFormat.parse(dateStr);

			}catch (Exception e1){
				e1.printStackTrace();
				return null;
			}
		}
		
		return null;
	}
	/** 
	* 获得该月第一天 
	* @param year 
	* @param month 
	* @return 
	*/  
	public static String getFirstDayOfMonth(int year,int month){  
	        Calendar cal = Calendar.getInstance();  
	        //设置年份  
	        cal.set(Calendar.YEAR,year);  
	        //设置月份  
	        cal.set(Calendar.MONTH, month-1);  
	        //获取某月最小天数  
	        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);  
	        //设置日历中月份的最小天数  
	        cal.set(Calendar.DAY_OF_MONTH, firstDay); 
	        cal.add(Calendar.DAY_OF_MONTH, -7);
	        //格式化日期  
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	        String firstDayOfMonth = sdf.format(cal.getTime()); 
	        System.out.println(firstDay);
	        return firstDayOfMonth;  
	    }  
	
	/** 
	* 获得该月第一天 
	* @param dateLong
	* @return
	*/  
	public static Date getDateByLong(long dateLong){  
	       
	        return new Date(dateLong);  
	    }  
	  
	/** 
	* 获得该月最后一天 
	* @param year 
	* @param month 
	* @return 
	*/  
	public static String getLastDayOfMonth(int year,int month){  
	        Calendar cal = Calendar.getInstance();  
	        //设置年份  
	        cal.set(Calendar.YEAR,year);  
	        //设置月份  
	        cal.set(Calendar.MONTH, month-1);  
	        //获取某月最大天数  
	        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);  
	        //设置日历中月份的最大天数  
	        cal.set(Calendar.DAY_OF_MONTH, lastDay);  
	        cal.add(Calendar.DAY_OF_MONTH, 7);
	        //格式化日期  
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	        String lastDayOfMonth = sdf.format(cal.getTime());  
	        return lastDayOfMonth;  
	    }





	public static Date stringToDate(String dateStr,String format){
		if (StringUtils.isBlank(dateStr)){
			return null;
		}
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
		try {
			Date date=simpleDateFormat.parse(dateStr);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取当月第一天
	 * @return
	 */
	public static String getMonthFirstDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		SimpleDateFormat firstDay= new SimpleDateFormat("yyyy-MM-dd");
		return  firstDay.format(calendar.getTime());
	}

	public static void main(String[] args) {
//		System.out.println("DateUtils.main()-------------"+getFirstDayOfMonth(2017,2));
//		System.out.println("DateUtils.main()-------------"+getLastDayOfMonth(2017,2));
////		String  dateStr= getDateByFormat("2017-05-28T16:00:00.000Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
////		Date date=getDateByFormat("2017-05-28T16:00:00.000Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
////		System.out.println(DateUtils.get);
//
//		System.out.println(yearsBetween(stringToDate("2012-10-11","yyyy-MM-dd"),new Date()));
		System.out.println(getMonthFirstDay());

	}

}
