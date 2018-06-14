//package com.xkd.service;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import org.apache.commons.lang.StringUtils;
//import org.springframework.stereotype.Service;
//
//import com.alibaba.fastjson.JSON;
//import com.xkd.controller.UserController;
//
//
//@Service
//public class IdeaRedisService extends TimerTask {
//
//
//	@Override
//	public void run() {
//
//
//		Date date = new Date();
//		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String dateStr = sp.format(date);
//
//		System.out.println(dateStr+"------dbcenter-------update redis database  every 2 minutes-------dbcenter------");
//
//		try {
//
//			List<String> companyNames = UserController.companyMapperIdea.selectCompanyNamesList();
//			List<String> parentIndustryNames  = UserController.dictionaryMapperIdea.selectDictionaryParentValueByType("industry");
//			List<String> sonIndustryNames  = UserController.companyMapperIdea.selectSonIndustrys();
//
//			List<String> projectLabels  = UserController.dictionaryMapperIdea.selectDictionaryParentValueByType("projectLabel");
//
//			Set<String> companyList = new HashSet<>();
//
//			for(String name : companyNames){
//
//				companyList.add(name);
//			}
//
//			Set<String> allIndustry = new HashSet<>();
//			Set<String> sonIndustry = new HashSet<>();
//			Set<String> parentIndustry = new HashSet<>();
//			Set<String> projectLabelsSet = new HashSet<>();
//
//			for(String names : sonIndustryNames){
//
//				if(StringUtils.isBlank(names)){
//
//					continue;
//				}
//
//				String[] industryNames = names.split(",");
//
//				for(int i = 0;i<industryNames.length;i++){
//
//					if(StringUtils.isNotBlank(industryNames[i])){
//
//						sonIndustry.add(industryNames[i]);
//					}
//				}
//			}
//
//
//
//
//			for(String string : projectLabels){
//
//				projectLabelsSet.add(string);
//			}
//
//			allIndustry.addAll(parentIndustryNames);
//			allIndustry.addAll(sonIndustry);
//
//
//			List<String> allIndustryResult = new ArrayList<>();
//			List<String> companyListResult = new ArrayList<>();
//			List<String> sonIndustryResult = new ArrayList<>();
//			List<String> projectLabelResult = new ArrayList<>();
//
//			for(String string : allIndustry){
//
//				allIndustryResult.add(string);
//
//			}
//
//			for(String string : companyList){
//
//				companyListResult.add(string);
//
//			}
//
//			for(String string : sonIndustry){
//
//				sonIndustryResult.add(string);
//
//			}
//
//			for(String string : projectLabelsSet){
//
//				projectLabelResult.add(string);
//
//			}
//
//			String jsonCompanyList = JSON.toJSON(companyListResult).toString();
//			String jsonIndustryList = JSON.toJSON(allIndustryResult).toString();
//			String jsonProjectLabelList = JSON.toJSON(projectLabelResult).toString();
//			String jsonSonIndustryList = JSON.toJSON(sonIndustryResult).toString();
//
//			/*UserController.redisCacheIdeaIdea.setCacheObject("companyNameList",jsonCompanyList);
//			UserController.redisCacheIdeaIdea.setCacheObject("industryList",jsonIndustryList);
//			UserController.redisCacheIdeaIdea.setCacheObject("sonIndustryList",jsonSonIndustryList);
//			UserController.redisCacheIdeaIdea.setCacheObject("projectLabelList",jsonProjectLabelList);*/
//
//		} catch (Exception e) {
//
//			System.out.println(e);
//
//		}
//	}
//
//	public  void importIdeaRedis(String pcCompanyId){
//
//		Timer timer = new Timer();
//		IdeaRedisService IdeaRedisService = new IdeaRedisService();
//        //程序运行后立刻执行任务，每隔1000ms执行一次
//        timer.schedule(IdeaRedisService, 0, 2*60*1000);
//
//	}
//
//}
