package com.kuangchi.sdd.attendanceConsole.synchronizeData.service.impl;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.statistic.service.impl.StatisticServiceImpl;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.service.SynService;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;


@Service("ontimeSyn")
public class OntimeSyn {
	
	
	@Resource(name="cronServiceImpl")
	ICronService cronService;
	@Resource(name = "synService")
	SynService synService;
	        //同步组织机构人员信息，请假外出信息
			public void onTimeSynOrg(){
				boolean result=cronService.compareIP();
				
				if(result){
				
		    	  synchronized(StatisticServiceImpl.lock){	
						Date now=new Date();
						//前一天的时间
						Date previousDay=getPreviousDayTime(now);
						//前两天的时间
						Date previousTwoDay =getPreviousDayTime(getPreviousDayTime(now));
						//前一天的日期
						String priviousDayStr=DateUtil.getDateTimeString(previousDay);
						//前二天的日期
						String priviousTwoDayStr=DateUtil.getDateTimeString(previousTwoDay);
						synService.synSqlServerOrgDepartmentData();
						synService.synSqlServerOrgUserAccountData();
						synService.synSqlServerCheckData(priviousTwoDayStr, priviousDayStr);
						synService.synSqlServerOutData(priviousTwoDayStr, priviousDayStr);				
		    	  }
				}
			}
			//同步刷卡记录
			public void onTimeSynBrush(){
				boolean result=cronService.compareIP();
				if (result) {
			    	  synchronized(StatisticServiceImpl.lock){
						Date now=new Date();
						//前一天的时间
						Date previousDay=getPreviousDayTime(now);
						//前两天的时间
						Date previousTwoDay =getPreviousDayTime(getPreviousDayTime(now));
						//前一天的日期
						String priviousDayStr=DateUtil.getDateTimeString(previousDay);
						//前二天的日期
						String priviousTwoDayStr=DateUtil.getDateTimeString(previousTwoDay);
						synService.synSqlServerBrushCardLog(priviousTwoDayStr, priviousDayStr);
					
			    	  }
				}
			}
			
			
			//获取前一天的时间
			public static Date getPreviousDayTime(Date date){
							if(date==null){
								return null;
							}
							 Calendar calendar =Calendar.getInstance();
							 calendar.setTime(date);
							 calendar.add(Calendar.DAY_OF_MONTH, -1);
							 return calendar.getTime();
					}	
			
}
