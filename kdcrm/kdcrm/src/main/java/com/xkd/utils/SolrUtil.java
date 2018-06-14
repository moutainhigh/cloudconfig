package com.xkd.utils;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.solr.client.solrj.impl.HttpSolrClient;

public class SolrUtil {
    static Lock lock = new ReentrantLock();
    static HttpSolrClient httpSolrClient;

    

    //单例模式
    public static HttpSolrClient getHttpSolrClient() {
        lock.lock();
        try {
            if (httpSolrClient == null) {
                httpSolrClient = new HttpSolrClient(PropertiesUtil.SOLR_INDEX_URL);
                httpSolrClient.setMaxTotalConnections(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return httpSolrClient;
    }


 


}
