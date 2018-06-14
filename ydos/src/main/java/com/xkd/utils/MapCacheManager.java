package com.xkd.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MapCacheManager {

    private static Map<String,String> cacheMap = new ConcurrentHashMap<>();

    public static synchronized void putCacheMap(String key,String value){
        long now = System.currentTimeMillis();
        cacheMap.put(key,value+","+now);
    }

    public static String  getCacheMap(String key){
        String value = cacheMap.get(key);
        if(value == null){
            return null;
        }
        String[] temts = value.split(",");
        long before = Long.valueOf(temts[1]);
        //如果大于6个小时就让缓存失效
        if((System.currentTimeMillis() - before) > 6*60*60*1000){
            return null;
        }
        return temts[0];
    }

    /*
        有效期1分钟
     */
    public static String getCacheMapFiveMinute(String key){
        String value = cacheMap.get(key);
        if(value == null){
            return null;
        }
        String[] temts = value.split(",");
        long before = Long.valueOf(temts[1]);
        //如果大于6个小时就让缓存失效
        if((System.currentTimeMillis() - before) > 5*60*1000){
            return null;
        }
        return temts[0];
    }

    public static synchronized void updateCacheMapExpire(String key,String value){
        MapCacheManager.putCacheMap(key,value);
    }


    public static synchronized void removeCacheMap(String key){
        cacheMap.remove(key);
    }

    public static synchronized void removeCacheMapByValue(String value){
        if(value == null){
            return;
        }
       Set<Map.Entry<String,String>> set =  cacheMap.entrySet();
       Iterator<Map.Entry<String,String>> iterator =  set.iterator();
       while (iterator.hasNext()){
           Map.Entry<String,String> map = iterator.next();
           String mvalue = map.getValue();
           if(mvalue != null){
               String[] temts = mvalue.split(",");
               if(value.equals(temts[0])){
                   String mkey = map.getKey();
                   cacheMap.remove(mkey);
               }
           }
       }
    }


    public static void main(String args[]){

        MapCacheManager.putCacheMap("key1", "value1");
        MapCacheManager.putCacheMap("key2", "value2");
        MapCacheManager.putCacheMap("key3", "value3");

        MapCacheManager.removeCacheMapByValue("value2");
        MapCacheManager.removeCacheMap("key1");

        System.out.println(MapCacheManager.getCacheMap("key1"));
        System.out.println(MapCacheManager.getCacheMap("key2"));
        System.out.println(MapCacheManager.getCacheMap("key3"));

    }





}
