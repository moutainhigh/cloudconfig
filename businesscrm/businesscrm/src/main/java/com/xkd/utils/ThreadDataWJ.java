package com.xkd.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ThreadDataWJ extends Thread {
	Logger log= LoggerFactory.getLogger(ThreadDataWJ.class);

	private static Map<String, Object> map = null;  
	
	public ThreadDataWJ(Map<String, Object> map) {
		this.map = map;
	}

	@Override
	public void run() {
		
		try {
			String id = (String) map.get("id");//试卷id
			List<Map<String,Object>> qList = (List<Map<String, Object>>) map.get("questionList");
			Map<String, List<Map<String, String>>> dataMap = new HashMap<>();
			
			String uidKey = (String) map.get("uid");
			String meetingid = (String) map.get("meetingId");
			List<Map> list = (List<Map>) map.get("result");
			Object companyName = null;
			System.out.println("ThreadDataWJ.run()-------:"+list.toString());
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> child = (Map<String, Object>) list.get(i);
				for (Map<String,Object> obj : qList) {
					if(obj.get("id").toString().equals(child.get("qid")+"")){
						Map<String, String> colMap = new HashMap<>();
						
						List<Map<String, String>> tabList = ((List<Map<String, String>>) dataMap.get(obj.get("table_name")));
						if(tabList == null){
							tabList = new ArrayList();
						}
						int ttype = (int)child.get("ttype");
						if( dataMap.get(obj.get("table_name")+"") != null &&(ttype < 3 || ttype == 9 || ttype ==10 )){
							colMap = dataMap.get(obj.get("table_name")+"").get(0);
							String aa = colMap.get(obj.get("table_colum")+"");
							aa =StringUtils.isNotBlank(aa) ?aa+","+child.get("name"):child.get("name")+"";
							colMap.put(obj.get("table_colum")+"", aa);
						}else{
							if(ttype < 3 || ttype == 9 || ttype ==10){
								colMap.put(obj.get("table_colum")+"", child.get("name")+"");
							}else{
								colMap.put(obj.get("table_colum")+"", child.get("answer")+"");
							}
							tabList.add(colMap);
							dataMap.put(obj.get("table_name")+"", tabList);
						}
						if(obj.get("table_colum").equals("company_name")){
							companyName = child.get("answer");
						}
						continue;
					}
				}
			}
			SysUtils.executeWjDataSql(dataMap, uidKey,meetingid,map.get("mobile"),companyName);
		} catch (Exception e) {
			log.error("异常栈:",e);
			System.out.println(e);
		}

		
	}
	
    
}
