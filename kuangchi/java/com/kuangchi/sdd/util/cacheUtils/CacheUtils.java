package com.kuangchi.sdd.util.cacheUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.kuangchi.sdd.util.commonUtil.DES;
import com.sun.mail.handlers.message_rfc822;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class CacheUtils<T> {
	  public static JedisPool pool = null;

		public CacheUtils(String ip, int prot) {
			if (pool == null) {
				JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxTotal(500);
				config.setMaxIdle(5);
				config.setTestOnBorrow(true);
				pool = new JedisPool(config, ip, prot, 100000);
			}
		}
 
		public void saveObject(String key,T t){
			RedisCacheAdaptee<T> adaptee=new RedisCacheAdaptee<T>();
			adaptee.saveObject(key, t,pool);
		}

		
		public T getObject(String key){
			RedisCacheAdaptee<T> adaptee=new RedisCacheAdaptee<T>();
			return adaptee.getObject(key, pool);
		}
		
		
		public void pushObject(String key,T t){
			RedisCacheAdaptee<T> adaptee=new RedisCacheAdaptee<T>();
			adaptee.pushObject(key, t, pool);
			
		}
		
		
		
		public T pullObject(String key){
			RedisCacheAdaptee<T> adaptee=new RedisCacheAdaptee<T>();
			return adaptee.pullObject(key, pool);			
		}
		
		
		public List<String> keysLike(String key){
			RedisCacheAdaptee<T> adaptee=new RedisCacheAdaptee<T>();
			return adaptee.keysLike(key, pool);
		}
		public List<T> rangeObjects(String key){
			RedisCacheAdaptee<T> adaptee=new RedisCacheAdaptee<T>();
			return adaptee.rangeObjects(key, pool);
		}
		
		
		
		
		
	public static void main(String[] args) {
//      CacheUtils<List<String>> cacheUtils=new CacheUtils<List<String>>("192.168.12.89", 6379);
      
//      List<String> ll=new ArrayList<String>();
//      ll.add("ggggu");
//      ll.add("llll");
//      cacheUtils.saveObject("ggg",ll);
		    
//	System.out.println(cacheUtils.getObject( "ggg"));
      
      CacheUtils<String> cacheUtils=new CacheUtils<String>("192.168.12.89", 6379);
      cacheUtils.pushObject("num", "45");
      cacheUtils.pushObject("num", "2");
      cacheUtils.pushObject("num", "3");
      cacheUtils.pushObject("num", "4");
      cacheUtils.pushObject("num", "5");
      
      
//      System.out.println(cacheUtils.pullObject("num"));
//      System.out.println(cacheUtils.pullObject("num"));
//      System.out.println(cacheUtils.pullObject("num"));
//      System.out.println(cacheUtils.pullObject("num"));
//      System.out.println(cacheUtils.pullObject("num"));
//      System.out.println(cacheUtils.pullObject("num"));
//      System.out.println(cacheUtils.pullObject("num")==null);
      System.out.println(cacheUtils.rangeObjects("num"));
      System.out.println(cacheUtils.keysLike("num")+"....");
      

	}
}
