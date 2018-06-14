package com.kuangchi.sdd.util.pubsub;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class PubSubUtils {
	public static JedisPool pool = null;
	public static JedisPool initilize(String ip,int port){
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(500);
			config.setMaxIdle(5);
			config.setTestOnBorrow(true);
			pool = new JedisPool(config, ip, port, 100000);
		}
		return pool;
	}
	
 public static void subscribe(Subscriber subscriber,String channel){    	 
	 new SubscribeThread(pool, new SubscriberProxy(subscriber),channel).start();
 }
 
 public static void publish(Publisher publisher,String channel){
	  new PublisherThread(pool,publisher, channel).start();
 }
}
