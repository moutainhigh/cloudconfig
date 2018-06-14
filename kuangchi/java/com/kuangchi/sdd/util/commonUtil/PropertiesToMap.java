package com.kuangchi.sdd.util.commonUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class PropertiesToMap {
	public static Map<String,String> propertyToMap(String propertyName){
		Properties prop = new Properties();  
		Map<String,String> pros=new HashMap<String,String>();
    	//String path=PropertiesToMap.class.getResource("/").getPath();
    	
    	InputStream in = PropertiesToMap.class.getClassLoader().getResourceAsStream("/conf/properties/"+propertyName);
    	//String propertyPath=path.substring(1, path.length())+"/conf/properties/"+propertyName;
        //读取属性文件
        //InputStream in;
		try {
			//in = new BufferedInputStream (new FileInputStream(propertyPath));
			//加载属性列表
	        prop.load(in);                
	        Iterator<String> it=prop.stringPropertyNames().iterator();
	        while(it.hasNext()){
	            String key=it.next();
	            String value=prop.getProperty(key);
	            //System.out.println(key+":"+prop.getProperty(key));
	            pros.put(key, value);
	        }
	        in.close(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
    		if (in != null){
    			try {
    				in.close();
    				} 
    			catch (IOException e) 
    			{
    				e.printStackTrace();
    			}
    		}
    	}
                   
        return pros;
	}
    public static void main(String[] args) { 
    	Map<String,String> map=PropertiesToMap.propertyToMap("kft.properties");
    	System.out.println();
    } 
}
