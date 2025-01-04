package cn.itcast.mq.helloworld;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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
        rabbitTemplate.convertAndSend(exchangeName, "red", massage);

    }

    @Test
    public void testTopicExchange() {
        // 交换机名称
        String exchangeName = "itcast.topic";

        // 消息
        String massage = "hello topic";
        rabbitTemplate.convertAndSend(exchangeName, "aaa.china.news", massage);

    }

    @Test
    public void testSendMap() {
        String queueName = "simple.queue1";

        Map<String, String> message = new HashMap<>();
        message.put("name", "wuhao");
        message.put("age", "21");
        rabbitTemplate.convertAndSend(queueName, message);
    }

    /**
     * 发送消息，并开启消息发送的回调
     */
    @Test
    public void testPublisherConfirm() {

        // 注意：
        //    由于每个消息发送时的处理逻辑不一定相同，因此ConfirmCallback需要在每次发消息时定义。
        //    具体来说，是在调用RabbitTemplate中的convertAndSend方法时，多传递一个参数：


        // 1.创建CorrelationData
        CorrelationData cd = new CorrelationData();
        // 2.给Future添加ConfirmCallback
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                // 2.1.Future发生异常时的处理逻辑，基本不会触发
                log.error("send message fail", ex);
            }
            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                // 2.2.Future接收到回执的处理逻辑，参数中的result就是回执内容
                if(result.isAck()){
                    // result.isAck()，boolean类型，true代表ack回执，false 代表 nack回执
                    log.debug("发送消息成功，收到 ack!");
                }else{
                    // result.getReason()，String类型，返回nack时的异常描述
                    log.error("发送消息失败，收到 nack, reason : {}", result.getReason());
                }
            }
        });
        // 3.发送消息
        rabbitTemplate.convertAndSend("itcast.direct", "red", "hello", cd);
    }

    @Test
    public void testSendTTLMessage() {

        rabbitTemplate.convertAndSend("test.direct", "hi", "hi wh", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        });
        log.info("发送延时消息");

    }
}
