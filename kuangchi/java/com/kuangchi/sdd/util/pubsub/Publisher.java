package com.kuangchi.sdd.util.pubsub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public abstract class Publisher {


	public abstract void publish(JedisPool jedisPool,String channel);
}
