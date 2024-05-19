package com.demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 消费者
 * @Author wh
 * @Date 2024/4/28 11:25
 */
public class KafkaConsumerTest {
    public static void main(String[] args) {
        // kafka配置
        Map<String, Object> configMap = new HashMap<>();
        // 配置属性：Kafka 集群地址
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // 配置属性: Kafka 传输的数据为 KV 对，所以需要对获取的数据分别进行反序列化
        configMap.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        configMap.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        // 配置属性: 读取数据的位置 ，取值为 earliest（最早），latest（最晚）
        configMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // 配置属性: 消费者组
        configMap.put("group.id", "atguigu");
        // 配置属性: 自动提交偏移量
        configMap.put("enable.auto.commit", "true");

        // 消费者对象
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(configMap);
        // 消费者订阅指定主题
        consumer.subscribe(Collections.singletonList("test"));
        // 消费数据，主动从kafka拉取数据
        while (true) {
            // 一次可拉取多条
            // 此处对象可遍历，实现了Iterable
            ConsumerRecords<String, String> poll = consumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord<String, String> records : poll) {
                System.out.println(records);
            }
        }
    }
}
