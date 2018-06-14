package com.xkd.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheUtil<T> {

	@Autowired
	public RedisTemplate redisTemplate;

	/**
	 * 缓存基本的对象，Integer、String、实体类等
	 * 
	 * @param key
	 *            缓存的键值
	 * @param value
	 *            缓存的值
	 * @return 缓存的对象
	 */
	public <T> ValueOperations<String, T> setCacheObject(String key, T value) {
		
		ValueOperations<String, T> operation = redisTemplate.opsForValue();
		operation.set(key, value);
		return operation;
	}

	/**
	 * 获得缓存的基本对象。
	 * 
	 * @param key
	 *            缓存键值
	 * @return 缓存键值对应的数据
	 */
	public <T> T getCacheObject(String key/* ,ValueOperations<String,T> operation */) {
		ValueOperations<String, T> operation = redisTemplate.opsForValue();
		return operation.get(key);
	}

	/**
	 * 缓存List数据
	 * 
	 * @param key
	 *            缓存的键值
	 * @param dataList
	 *            待缓存的List数据
	 * @return 缓存的对象
	 */
	public <T> ListOperations<String, T> setCacheList(String key, List<T> dataList) {
		ListOperations listOperation = redisTemplate.opsForList();
		if (null != dataList) {
			int size = dataList.size();
			for (int i = 0; i < size; i++) {

				listOperation.rightPush(key, dataList.get(i));
			}
		}

		return listOperation;
	}

	/**
	 * 
	 * @author: xiaoz
	 * @2017年5月26日 
	 * @功能描述:设置时间单位是秒，如果不设置的话就是永久的
	 * @param key
	 * @param value
	 * @param times
	 * @return
	 */
	public <T> ValueOperations<String, T> setCacheObject(String key, T value, long times) {
		
		ValueOperations<String, T> operation = redisTemplate.opsForValue();
		operation.set(key, value);
		redisTemplate.expire(key, times, TimeUnit.SECONDS);
		return operation;
	}

	
	/**
	 * 获得缓存的list对象
	 * 
	 * @param key
	 *            缓存的键值
	 * @return 缓存键值对应的数据
	 */
	public <T> List<T> getCacheList(String key) {
		List<T> dataList = new ArrayList<T>();
		ListOperations<String, T> listOperation = redisTemplate.opsForList();
		Long size = listOperation.size(key);

		for (int i = 0; i < size; i++) {
			dataList.add((T) listOperation.leftPop(key));
		}

		return dataList;
	}

	/**
	 * 缓存Set
	 * 
	 * @param key
	 *            缓存键值
	 * @param dataSet
	 *            缓存的数据
	 * @return 缓存数据的对象
	 */
	public <T> BoundSetOperations<String, T> setCacheSet(String key, Set<T> dataSet) {
		BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
		/*
		 * T[] t = (T[]) dataSet.toArray(); setOperation.add(t);
		 */

		Iterator<T> it = dataSet.iterator();
		while (it.hasNext()) {
			setOperation.add(it.next());
		}

		return setOperation;
	}

	/**
	 * 获得缓存的set
	 * 
	 * @param key
	 * @return
	 */
	public Set<T> getCacheSet(String key/*
										 * ,BoundSetOperations<String,T>
										 * operation
										 */) {
		Set<T> dataSet = new HashSet<T>();
		BoundSetOperations<String, T> operation = redisTemplate.boundSetOps(key);

		Long size = operation.size();
		for (int i = 0; i < size; i++) {
			dataSet.add(operation.pop());
		}
		return dataSet;
	}

	/**
	 * 缓存Map
	 * 
	 * @param key
	 * @param dataMap
	 * @return
	 */
	public <T> HashOperations<String, String, T> setCacheMap(String key, Map<String, T> dataMap) {

		HashOperations hashOperations = redisTemplate.opsForHash();
		if (null != dataMap) {

			for (Map.Entry<String, T> entry : dataMap.entrySet()) {

				/*
				 * System.out.println("Key = " + entry.getKey() + ", Value = " +
				 * entry.getValue());
				 */
				hashOperations.put(key, entry.getKey(), entry.getValue());
			}

		}

		return hashOperations;
	}








	/**
	 * 获取集合中某一个
	 * @param key
	 * @param field
	 * @return
	 */
	public Object getCacheMapVaue(String key,String field){
		 return    redisTemplate.opsForHash().get(key,field);
 	}




	/**
	 * 获得缓存的Map
	 * 
	 * @param key
	 * @return
	 */
	public <T> Map<String, T> getCacheMap(String key/*
													 * ,HashOperations<String,String
													 * ,T> hashOperation
													 */) {
		Map<String, T> map = redisTemplate.opsForHash().entries(key);
		/* Map<String, T> map = hashOperation.entries(key); */
		return map;
	}

	/**
	 * 缓存Map
	 * 
	 * @param key
	 * @param dataMap
	 * @return
	 */
	public <T> HashOperations<String, Integer, T> setCacheIntegerMap(String key, Map<Integer, T> dataMap) {
		HashOperations hashOperations = redisTemplate.opsForHash();
		if (null != dataMap) {
			for (Map.Entry<Integer, T> entry : dataMap.entrySet()) {

				/*
				 * System.out.println("Key = " + entry.getKey() + ", Value = " +
				 * entry.getValue());
				 */
				hashOperations.put(key, entry.getKey(), entry.getValue());
			}

		}

		return hashOperations;
	}

	/**
	 * 获得缓存的Map
	 * 
	 * @param key
	 * @return
	 */
	public <T> Map<Integer, T> getCacheIntegerMap(String key/*
															 * ,HashOperations<
															 * String,String,T>
															 * hashOperation
															 */) {
		Map<Integer, T> map = redisTemplate.opsForHash().entries(key);
		/* Map<String, T> map = hashOperation.entries(key); */
		return map;
	}


	/**
	 * 删除集合名称
	 * @param key
	 */
	public void  delete(String key){
		redisTemplate.delete(key);
	}

	/**
	 * 删除集合中的某一个字段
	 * @param key
	 * @param field
	 */
	public void delete(String key,String field){
		redisTemplate.opsForHash().delete(key,field);
	}

}