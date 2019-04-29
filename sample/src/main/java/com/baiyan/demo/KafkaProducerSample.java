package com.baiyan.demo;

import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @Auther: peilongcheng
 * @Date: 2019/4/16 09:59
 * @Description:
 */
public class KafkaProducerSample {
    private final static Logger logger = LoggerFactory.getLogger(KafkaProducerSample.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties properties;
        Producer producer;
        //新建Producer对象
        properties = new Properties();
        properties.put("bootstrap.servers", "172.21.10.53:6667");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>(properties);
        //新建ProducerRecord对象
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("CustomerCountry",
                "Precision Products", "Inspired by Hello Kitty and the latest seasonal shades, ColourPop x Hello Kitty is a full cosmetics collection of sweet colors for the eyes, lips and face. The assortment consists of singles, sets of four eye shadow shades, and a face kit that pairs three eye shadows, blush, highlighter and signature ColourPop ultra matte and ultra gloss lippies.");
        //简单发送
        producerWithSimple(producer, record);
        //同步
//        producerWithSynchronization(producer,record);
        //异步
//        producerWithASynchronization(producer,record);
    }

    //最简单的方式发送数据，注意要加上producer.flush();
    public static void producerWithSimple(Producer producer, ProducerRecord record) {
        producer.send(record);
        //注意直接send会导致数据存在缓冲区，可能无法在客户端实时显示，所以最好用同步的或者异步的方法，或者加一个刷新操作
        producer.flush();
        producer.close();
    }

    //同步发送
    public static void producerWithSynchronization(Producer producer, ProducerRecord record) {
        try {
            producer.send(record).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //异步发送
    public static void producerWithASynchronization(Producer producer, ProducerRecord record) {
        class DemoProducerCallBack implements Callback {
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e != null) {
                    e.printStackTrace();
                }
            }
        }
        producer.send(record,new DemoProducerCallBack());
        producer.close();
    }
}
