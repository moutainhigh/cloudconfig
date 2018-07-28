package com.wjh.utils.mqtt;

import org.eclipse.paho.client.mqttv3.*;


public class PushCallback implements MqttCallback {


    MqttClient client;

    public PushCallback(MqttClient client) {
        this.client = client;
    }

    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        System.out.println("连接断开，可以做重连");

        try {
            client.close();
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }

    public void deliveryComplete(IMqttDeliveryToken token) {
//        System.out.println("deliveryComplete---------" + token.isComplete());

    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        System.out.println(this.hashCode() + "接收消息主题 : " + topic);
        System.out.println(this.hashCode() + "接收消息Qos : " + message.getQos());
        System.out.println(this.hashCode() + "接收消息内容 : " + new String(message.getPayload()));
    }
}