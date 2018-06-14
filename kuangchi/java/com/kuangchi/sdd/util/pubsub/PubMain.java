package com.kuangchi.sdd.util.pubsub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class PubMain {
public static void main(String[] args) {
	  String ip="192.168.12.89";
	  int port=6379;
	  PubSubUtils.initilize(ip, port);
	  String channel="weather";	   
	  Publisher publisher=new Publisher() {
		
		@Override
		public void publish(JedisPool jedisPool, String channel) {
			try {
	    	  BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"utf-8"));
	    	  Jedis jedis=jedisPool.getResource();
	    	   while (true) {
				 String line=null;
					line=br.readLine();
				 if ("quit".equals(line)) {
					break;
				}
				 jedis.publish(channel, line);
				
			}
	    	   jedis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	};
	  PubSubUtils.publish(publisher, channel); 
}
}
