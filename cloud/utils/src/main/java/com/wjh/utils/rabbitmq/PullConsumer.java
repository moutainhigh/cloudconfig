package com.wjh.utils.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 拉取消费
 */
public class PullConsumer {


    public static void main(String[] args) throws IOException, InterruptedException {

        new PullConsumer().defaultConsume();
    }


    public void defaultConsume() throws IOException {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();

        String queueName="test_queue";

        // 声明队列(如果你已经明确的知道有这个队列,那么下面这句代码可以注释掉,如果不注释掉的话,也可以理解为消费者必须监听一个队列,如果没有就创建一个)
        channel.queueDeclare(queueName, false, false, false, null);
        boolean auto_ack = false;

        //auto_ack表示是否自动提交，如果false，那么消费者消费了以后，这些消息状态会变成unacked状态，不过等消费者连接断开后，这些消息又会恢复回ready状态，下次连接后还会被消费

        while (true) {
            GetResponse response = channel.basicGet(queueName, auto_ack);
            if (response != null) {
                String message = new String(response.getBody(), "UTF-8");
                System.out.println("收到：" + message);
                channel.basicAck(response.getEnvelope().getDeliveryTag(), true);


//                channel.basicReject(response.getEnvelope().getDeliveryTag(), true);    //重新放入队列

            }
        }
    }


    /**
     * header 消费时，当消息中的header达到一个匹配条件时才会收到消息
     *
     * @throws IOException
     */
    public void headerConsume() throws IOException {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();
        Map<String,Object> headers=new HashMap();

        String exchangeName="exchange_header";

        headers.put("province","hubei");
        headers.put("city","wuhan");
        headers.put("x-match","all");    //当所有header都匹配是才算匹配上
//        headers.put("x-match","any");  //匹配任何一个header属性就算匹配上
        String queueName=channel.queueDeclare().getQueue();

        channel.queueBind(queueName,exchangeName,"",headers);
        boolean auto_ack=false;

        //auto_ack表示是否自动提交，如果false，那么当消费者消费了以后也不确认时，这些消息状态会变成unacked状态，
        // 等消费者连接断开后，这些消息又会恢复回ready状态，下次连接后还会被消费

        while (true){
            GetResponse response     =   channel.basicGet(queueName,auto_ack);
            if (response!=null) {
                String message = new String(response.getBody(), "UTF-8");
                System.out.println("收到："+message);
                //手动ack
                channel.basicAck(response.getEnvelope().getDeliveryTag(),true);



                //拒绝ack
//              channel.basicReject(response.getEnvelope().getDeliveryTag(), true);    //重新放入队列

            }



        }

    }

}
