package com.kuangchi.sdd.util.pubsub;

import org.apache.log4j.Logger;

public class SubMain {
	
	public static final Logger LOG = Logger.getLogger(SubMain.class);
	
  public static void main(String[] args) {
	  String ip="192.168.12.89";
	  int port=6379;
	  PubSubUtils.initilize(ip, port);
	  String channel="weather";	   
	 Subscriber subscriber=new Subscriber() {
		
		@Override
		public void onUnsubscribe(String channel, int subscribedChannels) {
			 LOG.info("on unsubscribe "+channel);
			
		}
		
		@Override
		public void onSubscribe(String channel, int subscribedChannels) {
			 LOG.info("on subscribe "+channel);
			
		}
		
		@Override
		public void onMessage(String channel, String message) {
			 LOG.info("on message "+channel+"   "+message);
			
		}
	};
	 PubSubUtils.subscribe(subscriber, channel) ;
}
}
