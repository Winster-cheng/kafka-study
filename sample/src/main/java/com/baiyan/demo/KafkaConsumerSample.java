package com.baiyan.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;


/**
 * @Auther: peilongcheng
 * @Date: 2019/4/16 10:28
 * @Description:
 */
public class KafkaConsumerSample {
    private final static Logger logger = LoggerFactory.getLogger(KafkaConsumerSample.class);

    public static void main(String[] args) {
        HashMap<String, Integer> custCountryMap = new HashMap();
        Properties properties;
        //新建消费者
        properties = new Properties();
        properties.put("bootstrap.servers", "172.21.10.53:6667");
        properties.put("group.id", "CountryCounter");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        //订阅主题
        consumer.subscribe(Collections.singletonList("CustomerCountry"));
        //轮询
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(("\ntopic=" + record.topic() + " ,partition=" + record.partition() + " ,offset=" + record.offset() + " ,customer=" + record.key() + " ,country=" + record.value() + ""));
//                    logger.info("topic=%s ,partition=%s ,offset= %d ,customer=%s ,country=%s\n", record.topic(),
//                            record.partition(), record.offset(), record.key(), record.value());
                    int updatedCount = 1;
                    if (custCountryMap.containsKey(record.value())) {
                        updatedCount = custCountryMap.get(record.value()) + 1;
                    }
                    custCountryMap.put(record.value(), updatedCount);
                    System.out.println(custCountryMap.toString());
                }
            }
        } catch (Exception e) {

        } finally {
            consumer.close();
        }
    }
}
