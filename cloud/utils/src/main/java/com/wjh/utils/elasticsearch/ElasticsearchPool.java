package com.wjh.utils.elasticsearch;

import com.ibm.icu.util.StringTrieBuilder;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.elasticsearch.client.transport.TransportClient;

public class ElasticsearchPool {
    static GenericObjectPoolConfig genericObjectPoolConfig;
    static GenericObjectPool<TransportClient> genericObjectPool;
    static String LOCK="lock";

    private static   void init(String ip, Integer port,Integer totalConnection,Integer maxIdleConnection,Integer minIdleConnection,Integer maxWaitInMillis) {
        genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(totalConnection);
        genericObjectPoolConfig.setMaxIdle(maxIdleConnection);
        genericObjectPoolConfig.setMinIdle(minIdleConnection);
        genericObjectPoolConfig.setMaxWaitMillis(maxWaitInMillis);
        genericObjectPool = new GenericObjectPool<TransportClient>(new ElasticsearchPoolObjectFactory(ip,port), genericObjectPoolConfig);
    }


    /**
     * 从连接池中获取连接
     * @param ip
     * @param port
     * @param totalConnection
     * @param maxIdleConnection
     * @param minIdleConnection
     * @param maxWaitInMillis
     * @return
     */
    public static TransportClient getClient(String ip, Integer port,Integer totalConnection,Integer maxIdleConnection,Integer minIdleConnection,Integer maxWaitInMillis) {

       synchronized (LOCK){
           if (genericObjectPoolConfig==null){
               init(ip,port,totalConnection,maxIdleConnection,minIdleConnection,maxWaitInMillis);
           }
       }
        try {
            System.out.println(" before  get  ,  active:"+genericObjectPool.getNumActive()+",idle:"+genericObjectPool.getNumIdle()+",waiters:"+genericObjectPool.getNumWaiters());

            TransportClient client= genericObjectPool.borrowObject();
            System.out.println("after  get ,  active:"+genericObjectPool.getNumActive()+",idle:"+genericObjectPool.getNumIdle()+",waiters:"+genericObjectPool.getNumWaiters());
            return client;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void returnClient(TransportClient client) {
        System.out.println("before return  ,   active:"+genericObjectPool.getNumActive()+",idle:"+genericObjectPool.getNumIdle()+",waiters:"+genericObjectPool.getNumWaiters());

        genericObjectPool.returnObject(client);
        System.out.println("after return  ,   active:"+genericObjectPool.getNumActive()+",idle:"+genericObjectPool.getNumIdle()+",waiters:"+genericObjectPool.getNumWaiters());

    }





}
