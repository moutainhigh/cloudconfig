package com.kuangchi.sdd.util.commonUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenerateRandomTime {
	
	/**
	 * create by gengji yang
	 * @param date "2016-05-17"
	 * @param startTime "00:00:00"
	 * @param endTime "23:59:59"
	 * @param amount 要生成多少个该日期下，该时间范围内的随机时间
	 * @return
	 */
	public static List<String> getRandomDateTime(String date,String startTime,String endTime,Integer amount){
		
		List<String> list=new ArrayList();
		for(int i=0;i<amount;i++){
			Date d=randomDate(startTime,endTime);
			String hour=d.getHours()+"";
			if(hour.length()==1){
				hour="0"+hour;
			}
			String minute=d.getMinutes()+"";
			if(minute.length()==1){
				minute="0"+minute;
			}
			String second=d.getSeconds()+"";
			if(second.length()==1){
				second="0"+second;
			}
			
			String dateTime=date+" "+hour+":"+minute+":"+second;
			list.add(dateTime);
		}
		return list;
	}
	
	
	
	
	private static Date randomDate(String beginDate,String endDate){
		SimpleDateFormat format=new SimpleDateFormat("hh:mm:ss");
		try {
			Date start=format.parse(beginDate);
			Date end=format.parse(endDate);
			if(start.getTime()>=end.getTime()){
				return null;
			}
			
			long date=random(start.getTime(),end.getTime());
			return new Date(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	private static long random(long begin,long end){
		long rtn=begin+(long)(Math.random()*(end-begin));
		//如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
		if(rtn==begin||rtn==end){
			return random(begin,end);
		}
		return rtn;
	}
}
