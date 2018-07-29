package com.wjh.utils.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;

public class MQPooledConnectionFactory {


    private static PooledConnectionFactory pooledConnectionFactory;

    static {
        try {
            // 需要创建一个链接工厂然后设置到连接池中
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            activeMQConnectionFactory.setUserName("admin");
            activeMQConnectionFactory.setPassword("admin");
            activeMQConnectionFactory.setBrokerURL("tcp://127.0.0.1:61616");
            // 如果将消息工厂作为属性设置则会有类型不匹配的错误，虽然Spring配置文件中是这么配置的，这里必须在初始化的时候设置进去
            pooledConnectionFactory = new PooledConnectionFactory(activeMQConnectionFactory);
            // 链接最大活跃数，为了在测试中区别我们使用的到底是不是一个对象和看是否能控制连接数(实际上是会话数)，我们在这里设置为1
            int maximumActive = 1;
            pooledConnectionFactory.setMaximumActiveSessionPerConnection(maximumActive);
            //最大连接数
            pooledConnectionFactory.setMaxConnections(10);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得链接池工厂
     */
    public static PooledConnectionFactory getPooledConnectionFactory() {
        return pooledConnectionFactory;
    }

    /**
     * 对象回收销毁时停止链接
     */
    @Override
    protected void finalize() throws Throwable {
        pooledConnectionFactory.stop();
        super.finalize();
    }
}
