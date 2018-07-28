package com.wjh.utils.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Date;
import java.util.Properties;

/**
 * Created by think on 2018/5/31.
 */
public class Producer {


    public static void produce(String bootstrapServers, String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", StringSerializer.class);

        props.put("value.serializer", StringSerializer.class);
        KafkaProducer<String, String> producer = new KafkaProducer(props);

        long start = new Date().getTime();
        for (int i = 0; i < 10000; i++) {
            String msg = "message" + i;
            System.out.println("produce:" + msg);
            producer.send(new ProducerRecord<String, String>(topic, Integer.toString(i), msg));
        }
        long end = new Date().getTime();
        int interval = Long.valueOf((end - start) / 1000L).intValue();
        System.out.println("耗时"+interval + "s");
        producer.close();
    }


    public static void main(String[] args) throws InterruptedException {
        produce("127.0.0.1:9092", "test");
    }
}
