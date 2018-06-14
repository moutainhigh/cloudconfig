package com.kuangchi.sdd.util.pubsub;

import redis.clients.jedis.JedisPubSub;

public class SubscriberProxy  extends JedisPubSub{
	Subscriber subscriber;
	
	   public SubscriberProxy(Subscriber subscriber) {
		super();
		this.subscriber = subscriber;
	}

	@Override
	    public void onMessage(String channel, String message) {   	 
	    	 subscriber.onMessage(channel, message);
	    }
	    
	    @Override
	    public void onSubscribe(String channel, int subscribedChannels) {
	        subscriber.onSubscribe(channel, subscribedChannels);
	    }
	   
	    @Override
	    public void onUnsubscribe(String channel, int subscribedChannels) {
	    	 subscriber.onUnsubscribe(channel, subscribedChannels);
	    }
}
