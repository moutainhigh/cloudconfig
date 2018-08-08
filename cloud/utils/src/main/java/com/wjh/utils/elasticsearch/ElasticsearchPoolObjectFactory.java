package com.wjh.utils.elasticsearch;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

public class ElasticsearchPoolObjectFactory implements PooledObjectFactory<TransportClient> {

    private String ipAddress;
    private int port;


    public ElasticsearchPoolObjectFactory(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public PooledObject<TransportClient> makeObject() throws Exception {

        Settings settings = Settings.builder() // 设置集群名
                .put("client.transport.ignore_cluster_name", true) // 忽略集群名字验证, 打开后集群名字不对也能连接上
                .build();


        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(ipAddress), port));
        System.out.println("......makeObject");
        return new DefaultPooledObject(client);
    }

    @Override
    public void destroyObject(PooledObject<TransportClient> pooledObject) throws Exception {
        pooledObject.getObject().close();
        System.out.println("......destroyObject");
    }

    @Override
    public boolean validateObject(PooledObject<TransportClient> pooledObject) {
        System.out.println("......validateObject");
        return true;
    }

    @Override
    public void activateObject(PooledObject<TransportClient> pooledObject) throws Exception {
        System.out.println("......activateObject");
    }

    @Override
    public void passivateObject(PooledObject<TransportClient> pooledObject) throws Exception {
        System.out.println("......passivateObject");
    }
}