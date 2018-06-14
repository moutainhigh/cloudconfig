package com.kuangchi.sdd.consum.container;


import java.util.HashMap;
import java.util.Map;

import com.kuangchi.sdd.consum.bean.ParameterUpResponse;
//获取的消费机的参数会放到该类的parameters Map中
public class ParameterPool {
	 public static final Map<String, ParameterUpResponse> parameters=new HashMap<String, ParameterUpResponse>();
	 
	 public static void  addParameter(String machine,ParameterUpResponse parameterUpResponse ){
		 parameters.put(machine, parameterUpResponse);
	 }
	 
	 public synchronized static ParameterUpResponse   getParameter(String machine){
		 return parameters.get(machine);
	 }
}

