package com.xkd.utils;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaUtil {

	private static ConcurrentHashMap<String, Producer<String, String>> producerMap = new ConcurrentHashMap();

	private static synchronized Producer<String, String> getProducuer(String topic) {
		Producer<String, String> producer = producerMap.get(topic);
		if (producer == null) {
			Properties props = new Properties();
			props.put("bootstrap.servers", PropertiesUtil.KAFKA_BOOTSTRAP_SERVERS);
			props.put("key.serializer", StringSerializer.class);
			props.put("value.serializer", StringSerializer.class);
			props.put("acks", "all");
			props.put("retries", 1);
			props.put("batch.size", 1024);
			props.put("linger.ms", 1);
			props.put("buffer.memory", 1024*10);
			producer = new KafkaProducer(props);
			producerMap.put(topic, producer);
		}

		return producerMap.get(topic);
	}

	public static  void sendMessage(String topic, String message) {
		Producer<String, String> producer = getProducuer(topic);
		producer.send(new ProducerRecord<String, String>(topic, String.valueOf(new Date().getTime()), message));
	}

	
	 
 

}
