package com.kuangchi.sdd.base.config.action;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;

@Controller("configAction")
public class ConfigAction extends BaseActionSupport {
	
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 获取配置信息
	 * by gengji.yang
	 */
	public void getConfig(){
		Map<String, String> map = PropertiesToMap.propertyToMap("photoncard_interface.properties");
		Map<String, String> map1 = PropertiesToMap.propertyToMap("manager.properties");
		Properties prop = new Properties();  
		Map<String,String> pros=new HashMap<String,String>();
    	InputStream in = PropertiesToMap.class.getClassLoader().getResourceAsStream("/conf/ibatis/"+"dbType.properties");
	        try {
				prop.load(in);
		        Iterator<String> it=prop.stringPropertyNames().iterator();
		        while(it.hasNext()){
		            String key=it.next();
		            String value=prop.getProperty(key);
		            pros.put(key, value);
		          }
		        in.close(); 
			} catch (IOException e) {
				e.printStackTrace();
			}                
			map.putAll(pros);
			map.putAll(map1);
			printHttpServletResponse(GsonUtil.toJson(map));
	}
	
	/**
	 * 保存配置信息
	 * by gengji.yang
	 */
	public void saveConf(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		FileInputStream in;
		String basePath=PropertiesToMap.class.getClassLoader().getResource("/").getPath().substring(1);
		String card_recordName=basePath+"conf/properties/photoncard_interface.properties";
		String managerName=basePath+"conf/properties/manager.properties";
		String dbTypePropertiesName=basePath+"conf/ibatis/dbType.properties";
		Properties p=new Properties();
		Properties p1=new Properties();
		Properties p2=new Properties();
		Gson g=new Gson();
		JsonResult result=new JsonResult();
		Map m=g.fromJson(data, HashMap.class);//{username=root, data_syn_driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver, data_syn_username=sa1, data_syn_password=wanyi, driverClassName=com.mysql.jdbc.Driver, password=123456, url=jdbc:mysql://192.168.210.89:3306/photoncard?characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull, comm_url=@{url}, data_syn_url=jdbc:sqlserver://192.168.10.67; DatabaseName=GMDB_gengji}
		try{
			for(Object o:m.keySet()){
				if("url".equals(o)){
					in=new FileInputStream(card_recordName);
					p.load(in);
					in.close();
					p.setProperty((String)o, (String)m.get(o));
					FileOutputStream out=new FileOutputStream(card_recordName);
					p.store(out,"");
					out.flush();
					out.close();
				}else{
					if("driverClassName".equals(o)||"url".equals(o)||"username".equals(o)||"password".equals(o)){
						in=new FileInputStream(dbTypePropertiesName);
						p1.load(in);
						in.close();
						p1.setProperty((String)o, (String)m.get(o));
						FileOutputStream out=new FileOutputStream(dbTypePropertiesName);
						p1.store(out,"");
						out.flush();
						out.close();
					}else{
						in=new FileInputStream(managerName);
						p2.load(in);
						in.close();
						p2.setProperty((String)o, (String)m.get(o));
						FileOutputStream out=new FileOutputStream(managerName);
						p2.store(out,"");
						out.flush();
						out.close();
					}
				}
			}
			result.setSuccess(true);
			printHttpServletResponse(GsonUtil.toJson(result));
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}

		
}
