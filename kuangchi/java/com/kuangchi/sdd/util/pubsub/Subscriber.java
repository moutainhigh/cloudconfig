package com.kuangchi.sdd.util.pubsub;

import redis.clients.jedis.JedisPubSub;

public abstract class Subscriber   {
	    public abstract void onMessage(String channel, String message);
	    
	    public abstract void onSubscribe(String channel, int subscribedChannels) ;
	   
	    public abstract void onUnsubscribe(String channel, int subscribedChannels);
}
