package com.xkd.utils;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.xkd.model.Operate;

public class OperateCacheUtil {
	static ConcurrentHashMap<String, List<Operate>> concurrentHashMap=new ConcurrentHashMap<>();
	private static List<Operate> allOpereateList;
	static DelayQueue<DelayObject> delayQueue=new DelayQueue<>();
	
	private static class DelayObject implements Delayed{

        private  long delay;

        private  long expire; //到期时间
        
        private String key;

		public  DelayObject(String key,long delay) {
			this.key=key;
			this.delay = delay;
			expire = Calendar.getInstance().getTimeInMillis() + delay;
		}


		@Override
		public int compareTo(Delayed o) {
			  return (int) (this.delay - o.getDelay(TimeUnit.MILLISECONDS));
 		}

		@Override
		public long getDelay(TimeUnit unit) {
			 
			return expire - Calendar.getInstance().getTimeInMillis();
		}


		public String getKey() {
			return key;
		}


		public void setKey(String key) {
			this.key = key;
		}
	
	} 

	
	
	static {
		new Thread(){
			 @Override
			public void run() {
				 while (true) {
					 DelayObject delayed= delayQueue.poll();	  //移除超时对象
					if (delayed!=null) {
						concurrentHashMap.remove(delayed.getKey());
						
					}
					 try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		 }.start();;
		 
	}
	
	//保存缓存
	public static void putUserOperates(String key,List<Operate> operateList){
		DelayObject delayObject=new DelayObject(key,5*60*1000);
		delayQueue.put(delayObject);
		concurrentHashMap.put(key, operateList);
	}
	//获取缓存
	public static List<Operate> getUserOperates(String key){
		return concurrentHashMap.get(key);
	}
	
	public  static  List<Operate> getAllOperates(){
		return allOpereateList;
	}
	
	
	public static void putAllOperate(List<Operate> operateList){
		allOpereateList=operateList;
	}
	
	public static void clear(){
		allOpereateList=null;
		delayQueue.clear();
		concurrentHashMap.clear();
		
	}
	
	public static void clear(String token){
		concurrentHashMap.remove(token);
		delayQueue.remove(token);
	}
	
}
