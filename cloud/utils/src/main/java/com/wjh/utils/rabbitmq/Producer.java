package com.wjh.utils.rabbitmq;


import com.rabbitmq.client.*;
import java.util.HashMap;
import java.util.Map;

public class Producer {


    public static void main(String[] argv) throws Exception {
        new Producer().topicProduce();
    }


    public void defaultProduce() throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();

        String queueName="test_queue";

        /*
         * 声明（创建）队列
         * 参数1：队列名称
         * 参数2：为true时server重启队列不会消失
         * 参数3：队列是否是独占的，如果为true只能被一个connection使用，其他连接建立时会抛出异常
         * 参数4：队列不再使用时是否自动删除（没有连接，并且没有未处理的消息)
         * 参数5：建立队列时的其他参数
         */
        channel.queueDeclare(queueName, false, false, false, null);

        // 消息内容
        String message = "Hello World!";
        /*
         * 向server发布一条消息
         * 参数1：exchange名字，若为空则使用默认的exchange
         * 参数2：routing key
         * 参数3：其他的属性
         * 参数4：消息体
         * RabbitMQ默认有一个exchange，叫default exchange，它用一个空字符串表示，它是direct exchange类型，
         * 任何发往这个exchange的消息都会被路由到routing key的名字对应的队列上，如果没有对应的队列，则消息会被丢弃
         */

        for (int i = 0; i < 10000; i++) {
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println(" [生产者] Sent '" + message + "'");
        }


        //关闭通道和连接
        channel.close();
        connection.close();
    }


    /**
     * 通过routingKey来分发消息，例如队列1 和队列2 都绑定了同一个exchange的同一个routingKey,那么
     * 当向这个exchange的routingKey发送消息的时候队列1和队列2都能收到消息，队列也可以绑定不同的routingKey
     * @throws Exception
     */


    public void directProduce() throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();

        String exchangeName="exchange_direct";
        String queueName1="direct_queue1";
        String queueName2="direct_queue2";
        String routingKey="direct_key";

        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);




        /*
         * 声明（创建）队列
         * 参数1：队列名称
         * 参数2：为true时server重启队列不会消失
         * 参数3：队列是否是独占的，如果为true只能被一个connection使用，其他连接建立时会抛出异常
         * 参数4：队列不再使用时是否自动删除（没有连接，并且没有未处理的消息)
         * 参数5：建立队列时的其他参数
         */
        channel.queueDeclare(queueName1, false, false, false, null);


        channel.queueBind(queueName1, exchangeName, routingKey);   //ququeName1 绑定到routingKey


        channel.queueDeclare(queueName2, false, false, false, null);


        channel.queueBind(queueName2, exchangeName, routingKey);//ququeName2 也绑定到routingKey





        // 消息内容
        String message = "Hello direct!";
        /*
         * 向server发布一条消息
         * 参数1：exchange名字，若为空则使用默认的exchange
         * 参数2：routing key
         * 参数3：其他的属性
         * 参数4：消息体
         * RabbitMQ默认有一个exchange，叫default exchange，它用一个空字符串表示，它是direct exchange类型，
         * 任何发往这个exchange的消息都会被路由到routing key的名字对应的队列上，如果没有对应的队列，则消息会被丢弃
         */


        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties();
        for (int i = 0; i < 10000; i++) {
            //给exchangeName的routingKey发送消息，那么绑定到该exchangeName,routingKey的队列都会收到消息
            channel.basicPublish(exchangeName, routingKey, basicProperties, message.getBytes());
            System.out.println(" [生产者] Sent '" + message + "'");
        }


        //关闭通道和连接
        channel.close();
        connection.close();
    }


    /**
     *
     * topic类型是通过创建一个队列来订阅某一个exchange的topic,当发送的消息中的主题与队列订阅的主题匹配时，消息就会发送到该队列
     * @throws Exception
     */
    public void topicProduce() throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();

        String exchangeName="exchange_topic";
        String topicQueue1="topic_queue1";
        String topicQueue2="topic_queue2";
        String topic1="topic.#";   //#表示匹配多个点后的值，如 topic.abc   , topic.abc.efg, topic.h,topic.a.b.c 等
        String topic2="topic.efg.*";//* 表示只能匹配一个点后的值 ，如topic.efg.a   ,topic.efg.b等，topic.efg.a.b是匹配不到的


        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);




        /*
         * 声明（创建）队列
         * 参数1：队列名称
         * 参数2：为true时server重启队列不会消失
         * 参数3：队列是否是独占的，如果为true只能被一个connection使用，其他连接建立时会抛出异常
         * 参数4：队列不再使用时是否自动删除（没有连接，并且没有未处理的消息)
         * 参数5：建立队列时的其他参数
         */
        channel.queueDeclare(topicQueue1, false, false, false, null);

        channel.queueDeclare(topicQueue2, false, false, false, null);


        // * 表示后面只有一个点后面的值  # 表示匹配多个点后面的值
        channel.queueBind(topicQueue1, exchangeName, topic1);
        channel.queueBind(topicQueue2, exchangeName, topic2);


        // 消息内容
        String message = "Hello topic!";
        /*
         * 向server发布一条消息
         * 参数1：exchange名字，若为空则使用默认的exchange
         * 参数2：topic 名称
         * 参数3：其他的属性
         * 参数4：消息体
         * 凡是发送能和和topic匹配上的都可以收到这条消息
         */


        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties();
        for (int i = 0; i < 10000; i++) {
            channel.basicPublish(exchangeName, "topic.efg.h", basicProperties, message.getBytes());
            System.out.println(" [生产者] Sent '" + message + "'");
        }


        //关闭通道和连接
        channel.close();
        connection.close();
    }


    /**
     * fanout 类型会把消息推送到所有与该exchange绑定的队列中去，不需要通过routingKey进行筛选
     *
     * @throws Exception
     */
    public void fanoutProduce() throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();
        String exchangeName="exchange_fanout";
        String queueName1="fanout_queue1";
        String queueName2="fanout_queue2";


        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);




        /*
         * 声明（创建）队列
         * 参数1：队列名称
         * 参数2：为true时server重启队列不会消失
         * 参数3：队列是否是独占的，如果为true只能被一个connection使用，其他连接建立时会抛出异常
         * 参数4：队列不再使用时是否自动删除（没有连接，并且没有未处理的消息)
         * 参数5：建立队列时的其他参数
         */
        channel.queueDeclare(queueName1, false, false, false, null);

        channel.queueDeclare(queueName2, false, false, false, null);


        // * 表示后面只有一个点后面的值  # 表示匹配多个点后面的值
        channel.queueBind(queueName1, exchangeName, "");
        channel.queueBind(queueName2, exchangeName, "");


        // 消息内容
        String message = "Hello topic!";
        /*
         * 向server发布一条消息
         * 参数1：exchange名字，若为空则使用默认的exchange
         * 参数2：routing key
         * 参数3：其他的属性
         * 参数4：消息体
         */


        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties();
        for (int i = 0; i < 10000; i++) {
            channel.basicPublish(exchangeName, "", basicProperties, (message + i).getBytes());
            System.out.println(" [生产者] Sent '" + message + "'");
        }


        //关闭通道和连接
        channel.close();
        connection.close();
    }


    /**
     *
     * header类型的发送消息时会带一系列消息头属性，当消费者消费时只有消息头达到一定的匹配度时才会进行消费。
     * 这个匹配度可以通过在消费者中设置x-match 来设置 ，为all时要头部属性全部匹配才消费，当为any时，只要
     * 有一个匹配就可以消费
     *
     * @throws Exception
     */
    public void headerProduce() throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();
        String exchangeName="exchange_header";


        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.HEADERS);


        Map<String, Object> headers = new HashMap();
        headers.put("province", "hubei");
        headers.put("city", "wuhan");

        // 消息内容
        String message = "Hello topic!";
        /*
         * 向server发布一条消息
         * 参数1：exchange名字，若为空则使用默认的exchange
         * 参数2：routing key
         * 参数3：其他的属性
         * 参数4：消息体
         */


        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().headers(headers).build();
        for (int i = 0; i < 10000; i++) {
            channel.basicPublish(exchangeName, "", basicProperties, (message + i).getBytes());
            System.out.println(" [生产者] Sent '" + message + "'");
        }


        //关闭通道和连接
        channel.close();
        connection.close();
    }


}