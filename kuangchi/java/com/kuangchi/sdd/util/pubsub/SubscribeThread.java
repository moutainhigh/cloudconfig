package com.kuangchi.sdd.util.pubsub;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class SubscribeThread extends Thread {
    JedisPool jedisPool;
    SubscriberProxy subscriber;
    String channel;
	public SubscribeThread(JedisPool jedisPool, SubscriberProxy subscriber,String channel) {
		super();
		this.jedisPool = jedisPool;
		this.subscriber = subscriber;
		this.channel=channel;
	}
    
   @Override
public void run() {
	   Jedis jedis=null;
	   try {
		    jedis=jedisPool.getResource();
	        jedis.subscribe(subscriber,channel );  
	} catch (Exception e) {
		  e.printStackTrace();
	}finally{
		if (jedis!=null) {
			jedis.close();			
		}
	}
	     
        
}
	
	
	
}
