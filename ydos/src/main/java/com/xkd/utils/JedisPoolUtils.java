package com.xkd.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Create by @author: wanghuijiu; @date: 18-3-5;
 */
public class JedisPoolUtils {

    private static JedisPool pool;

    private static void createJedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxWaitMillis(1000);

        config.setMaxIdle(10);

        pool = new JedisPool(config, "192.168.1.8", 6379);
    }

    private static synchronized void poolInit(){
        if (pool == null){
            createJedisPool();
        }
    }

    public static Jedis getJedis(){
        if (pool == null){
            poolInit();
        }
        return pool.getResource();
    }

    public static void returnRes(Jedis jedis){
        jedis.close();
    }

    public static String getBoxStatus(String H, String key){
        Jedis jedis = getJedis();
        String str = jedis.hget(H, key);
        returnRes(jedis);
        return str;
    }

}
