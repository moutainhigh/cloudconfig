package com.kuangchi.sdd.util.pubsub;

import redis.clients.jedis.JedisPool;

public class PublisherThread extends Thread {
	JedisPool pool;
      Publisher publisher;
      String channel;	
	public PublisherThread(JedisPool pool, Publisher publisher, String channel) {
		super();
		this.pool = pool;
		this.publisher = publisher;
		this.channel = channel;
	}


	@Override
	public void run() {
		 publisher.publish(pool,channel);
	}
	
      
}
