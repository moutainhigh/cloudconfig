package com.wjh.utils.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

/**
 * Created by think on 2018/5/31.
 */
public class Consumer  {

    /**
     *
     * @param bootstrapServers
     * 多个以逗号分隔
     *
     */
    public static void  consume(String bootstrapServers,String groupId,String topic){

        Properties props=new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", groupId);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.intervals.ms", "1000");
        props.put("key.deserializer", StringDeserializer.class);

        props.put("value.deserializer", StringDeserializer.class);
        props.put("auto.offset.reset", "latest");


        KafkaConsumer<String, String> consumer=new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(topic));

        while (true) {
            ConsumerRecords<String, String> records=consumer.poll(1000);
            for (ConsumerRecord<String, String> record:records) {
                System.out.println("consume:"+record.key()+" "+record.value());
            }
        }

    }



    public static void main(String[] args) {

            consume("127.0.0.1:9092","group1","test");


            }


}
