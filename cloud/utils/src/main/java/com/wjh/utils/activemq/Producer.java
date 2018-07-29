package com.wjh.utils.activemq;

import org.apache.activemq.pool.PooledConnectionFactory;

import javax.jms.*;

public class Producer {


    static class ProducerThread extends Thread {
        @Override
        public void run() {
            //获取连接池工厂
            PooledConnectionFactory pooledConnectionFactory = MQPooledConnectionFactory.getPooledConnectionFactory();
            try {
                //从连接池中获取一个连接,如果连接池连接不够，那么等待获取连接的线程会阻塞
                Connection connection = pooledConnectionFactory.createConnection();

                connection.start();
                //创建一个事务（这里通过参数可以设置事务的级别）
                Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
                //创建一个消息队列,没有就创建，有就直接用
                Queue queue = session.createQueue("ABC");
                //消息生产者
                MessageProducer messageProducer = session.createProducer(queue);

                //创建一条消息
                TextMessage msg = session.createTextMessage(Thread.currentThread().getName() +
                        "productor:我是大帅哥，我现在正在生产东西！");
                System.out.println(Thread.currentThread().getName() +
                        "productor:我是大帅哥，我现在正在生产东西！");
                //发送消息
                messageProducer.send(msg);
                //提交事务
                session.commit();
                //提交session  一个表示一个业务终止，这个session如果不close,那么连接一直会被当前线程占用，其它线程无法获得连接，造成其它线程阻塞
                session.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new ProducerThread().start();

        }
    }
}
