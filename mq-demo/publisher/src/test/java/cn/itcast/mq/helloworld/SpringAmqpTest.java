package cn.itcast.mq.helloworld;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {

    // 这个对用与操作mq
    @Autowired
    private RabbitTemplate rabbitTemplate;


    // 简单队列
    @Test
    public void testSimpleQueue() {

        // 队列名称
        String queueName = "simple.queue";

        // 消息
        String message = "hello mq";

        // 发送消息（mq中需要存在这个队列，不会自动创建队列，如果没有队列，消息就会丢失）
        rabbitTemplate.convertAndSend(queueName, message);
    }

    // 工作队列，队列同时绑定多个消费者
    @Test
    public void testWorkQueue() throws InterruptedException {

        String queueName = "simple.queue";

        String message = "hello message_";
        for(int i = 0; i < 50; i++) {
            rabbitTemplate.convertAndSend(queueName, message + i);
            Thread.sleep(20);
        }

    }


    /**
     * 消息发送，只需要将消息发送到交换机上，具体消息转发由交换机决定
     *
     *
     */
    @Test
    public void testFanoutExchange() {
        // 交换机名称
        String exchangeName = "itcast.fanout";

        // 消息
        String message = "hello fanout";

        rabbitTemplate.convertAndSend(exchangeName, "", message);


    }

    @Test
    public void testDirectExchange() {
        // 交换机名称
        String exchangeName = "itcast.direct";

        // 消息
        String massage = "hello direct";
        rabbitTemplate.convertAndSend(exchangeName, "", massage);

    }

    @Test
    public void testTopicExchange() {
        // 交换机名称
        String exchangeName = "itcast.topic";

        // 消息
        String massage = "hello topic";
        rabbitTemplate.convertAndSend(exchangeName, "aaa.china.news", massage);

    }
}
