package com.kuangchi.sdd.util.cacheUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.itextpdf.text.pdf.hyphenation.TernaryTree.Iterator;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisCacheAdaptee<T>{
	
 
	
	
	 ObjectSerializer<T> objectSerializer=new ObjectSerializer<T>();

	public void saveObject(String key,T t,JedisPool pool) {
		Jedis jedis = pool.getResource();
		byte[] value=objectSerializer.serialize(t);
		try {
			jedis.set(key.getBytes("UTF-8"), value);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}finally{
			if (jedis!=null) {
				jedis.close();
			}
		}
		
	}



	public T getObject(String key,JedisPool pool) {
		Jedis jedis = pool.getResource();
		try {
			return objectSerializer.deserialize(jedis.get(key.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}finally{
			if (jedis!=null) {
				jedis.close();
			}
		}
	}
	
	
	public void pushObject(String key,T t,JedisPool pool){
		Jedis jedis = pool.getResource();
		byte[] value=objectSerializer.serialize(t);
		try {
			jedis.lpush(key.getBytes("UTF-8"), value);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}finally{
			if (jedis!=null) {
				jedis.close();
			}
		}
		
	}
	
	
	
	public T pullObject(String key,JedisPool pool){
		Jedis jedis = pool.getResource();
		try {
			return objectSerializer.deserialize(jedis.rpop(key.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}finally{
			if (jedis!=null) {
				jedis.close();
			}
		}
		
		
	}
	
	
	public List<T> rangeObjects(String key,JedisPool pool){
		Jedis jedis = pool.getResource();
		try {
			List<byte[]> objects=jedis.lrange(key.getBytes("UTF-8"),0,-1);
			List<T> lists=new ArrayList<T>();
			for(byte[] o: objects){
			   lists.add(objectSerializer.deserialize(o));
			} 
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if (jedis!=null) {
				jedis.close();
			}
		}
		
	}
	
	public List<String> keysLike(String key,JedisPool pool){
		Jedis jedis = pool.getResource();
		try {
			Set<byte[]> objects=jedis.keys(key.getBytes("UTF-8"));
			java.util.Iterator<byte[]> it=objects.iterator();
			List<String> lists=new ArrayList<String>();
			while (it.hasNext()) {
			  String keyStr=new String(it.next());
			  lists.add(keyStr);		
			}
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if (jedis!=null) {
				jedis.close();
			}
		}
	}
	
	

}
