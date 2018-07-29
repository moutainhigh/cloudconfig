package com.wjh.utils.activemq;

import org.apache.activemq.jms.pool.PooledConnectionFactory;

import javax.jms.*;

public class Consumer {
    static class ConsumerThread extends Thread {
        @Override
        public void run() {
            try {
                //获取连接池工厂
                PooledConnectionFactory pooledConnectionFactory = MQPooledConnectionFactory.getPooledConnectionFactory();
                //从连接池中获取一个连接
                Connection connection = pooledConnectionFactory.createConnection();
                connection.start();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                //创建队列，如果没有就创建，有就直接用
                Queue queue = session.createQueue("ABC");
                MessageConsumer consumer = session.createConsumer(queue);
                while (true) {
                    Thread.sleep(1000);
                    TextMessage msg = (TextMessage) consumer.receive();
                    if (msg != null) {
                        //向broker发送确认消息
                        msg.acknowledge();
                        System.out.println(Thread.currentThread().getName() + ": Consumer:我是消费者，我正在消费Msg" + msg.getText());
                    } else {
                        break;
                    }
                }

                //关闭session 如果不关闭，那么该连接会一直被占用，其它线程无法使用连接
                session.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new ConsumerThread().start();
        }
    }


}
