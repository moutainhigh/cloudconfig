package com.kuangchi.sdd.consum.util;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * 
 * <p>Description: [JSON工具类，主要通过GSON进行解析，名称为JsonUtil2,预留JsonUtil与原框架对接]</p>
 * @author  <a href="mailto: yaoyu@neusoft.com">姚宇</a>
 * @version $Revision$
 */
public class GsonUtil
{
    
    private static final Gson gson = new Gson();
    
    public static Gson getGson(){
        return gson;
    }
    
    public static String toJson(Object obj){
        return gson.toJson(obj);
    }
    
    /**
     * Gson gson = new Gson();
       Collection<Integer> ints = Lists.immutableList(1,2,3,4,5);

      (Serialization)
       String json = gson.toJson(ints); ==> json is [1,2,3,4,5]

     * <p>Discription:[将集合类型转化为Json字符串]</p>
     * @param collection 待转换的集合
     * @return
     * @author:[姚宇]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    
    public static String toJson(Collection<?> collection){
        return gson.toJson(collection);
    }
    /**
     * 
     * <p>Discription:[将JSON字符串转化为相应的对象]</p>
     * <p>示例:LightUser userFromJson = JsonUtil2.toBean(json,LightUser.class);</p>
     * @param json    待转化的JSON格式字符串
     * @param typeOfT 待转化对象的类型
     * @return
     * @author:[姚宇]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    @SuppressWarnings("unchecked")
    public static <T> T toBean(String json,Class<T> classOfT){
        return gson.fromJson(json, classOfT);
    }
    
    /**
     * 
     * <p>Discription:[将Json字符串转化为包含特定类型对象的集合]</p>
     * @param json     待转化的字符串
     * @param listType 待转化集合中所包含对象的类型
     * @return
     * @author:[姚宇]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static List<?> getListFromJson(String json,Class<?> listType){
        return  (ArrayList<?>)gson.fromJson(json, listType);
    }
    
    
    public static String toJsonWithDateFormat(Object obj){
    	
    	GsonBuilder gBuilder = new GsonBuilder();
    	gBuilder.registerTypeAdapter(Date.class, new DateSerializer());
  	
        return gBuilder.create().toJson(obj);
    }
    
    private static class DateSerializer implements JsonSerializer<Date>{

		@Override
		public JsonElement serialize(Date src, Type typeOfSrc,
				JsonSerializationContext context) {
			
			return new JsonPrimitive(DateUtil.getDateString(src));
		}
    }
    
}
