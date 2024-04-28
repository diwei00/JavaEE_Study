package com.demo.kafka;

import com.sun.xml.internal.bind.v2.TODO;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * @Description 生产者
 * @Author wh
 * @Date 2024/4/28 11:15
 */
public class KafkaProducerTest {
   public static void main(String[] args) {
      HashMap<String, Object> configMap = new HashMap<>();
      // 配置属性：Kafka 服务器集群地址
      configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
      // 配置属性：Kafka 生产的数据为 KV 对，所以在生产数据进行传输前需要分别对K,V 进行对应的序列化操作
      configMap.put(
              ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
              "org.apache.kafka.common.serialization.StringSerializer");
      configMap.put(
              ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
              "org.apache.kafka.common.serialization.StringSerializer");
      // 创建kafka生产者对象，建立kafka连接
      KafkaProducer<String, String> producer = new KafkaProducer<>(configMap);

      for(int i = 0; i < 10; i++) {
         // 准备数据
         ProducerRecord<String, String> record = new ProducerRecord<String, String>(
                 "test", "key" + i, "value" + i);
         // 发送数据到kafka
         producer.send(record);
      }



      // 关闭生产者连接
      producer.close();


   }
}
