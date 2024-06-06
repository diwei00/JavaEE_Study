package cn.itcast.mq.listener;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.stereotype.Component;

import javax.lang.model.type.ExecutableType;
import java.time.LocalDateTime;
import java.util.Map;

// 消费队列中的消息

@Slf4j
@Component
public class SpringRabbitListener {


//
//     监听simple.queue队列
//     队列中只要有消息，这里就可以拿到消息
    // 直接使用引用类型，消息转换器可以反序列化为对象
    @RabbitListener(queues = "simple.queue1")
    public void listenSimpleQueueMessage(Map<String, String> msg) {
        System.out.println("消费到消息："  + msg);
        System.out.println(msg.get("name"));
        System.out.println(msg.get("age"));

    }


    /**
     * 工作队列，消息会平均分配给消费者（不会考虑消费者处理消息的能力）
     * 能力强的则处理的快，能力弱的则处理的慢（两者处理的消息数都是一致的）
     * 多个消费者绑定到同一个队列，一个消息只熊被一个消费者消费
     *
     * 可配置yml参数，每次只能获取一条消息，处理完成才能获取下一个消息
     * 能者多劳（能力强的多处理点，能力弱的少处理点）
     * @param msg
     * @throws InterruptedException
     */
    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueueMessage1(String msg) throws InterruptedException {

        System.out.println("消费到消息(1)："  + msg + " "+ LocalDateTime.now());
        Thread.sleep(20);

    }
    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueueMessage2(String msg) throws InterruptedException {

        System.out.println("消费到消息(2)："  + msg + "__________________" + LocalDateTime.now());
        Thread.sleep(200);


    }


    /**
     * 监听fanout.queue1队列
     * 队列中只要有消息，这里就可以拿到
     * fanout:
     *  广播交换机
     *  该交换机会给每个绑定到自己上的队列发送消息
     * @param msg
     */
    @RabbitListener(queues = "fanout.queue1")
    public void listenFanoutQueue1(String msg) {
        System.out.println("接收到消息(1)：" + msg);
    }

    @RabbitListener(queues = "fanout.queue2")
    public void listenFanoutQueue2(String msg) {
        System.out.println("接收到消息(2)：" + msg);
    }



    /**
     * 第二种方式，基于注解：（申明队列和交换机并且绑定）
     * 基于注解声明队列和交换机，同时指定队列的 routing key
     *
     * 注意：
     *   这里会自动创建队列以及交换机并且两者具有绑定关系
     *
     * Direct：
     *  根据routing key 选择具体消息发送的队列
     *  消息发送时指定routing key 交换机会选择绑定到自己队列设定同样routing key 的队列上
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1"),
            exchange = @Exchange(name = "itcast.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "blue"}
    ))
    public void listenDirectQueue1(String msg) {
        log.info("接收到消息(1)：" + msg);
        if(true) {
            throw new RuntimeException("故意的");
        }
//        System.out.println("接收到消息(1)：" + msg);
        log.info("消息处理完成");
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2"),
            exchange = @Exchange(name = "itcast.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "yellow"}
    ))
    public void listenDirectQueue2(String msg) {
        System.out.println("接收到消息(2)：" + msg);
    }


    /**
     * topic:
     *  同样也是用 routing key把消息路由到不同队列
     *  `Topic`类型`Exchange`可以让队列在绑定`Routing key` 的时候使用通配符！
     *
     *  `Routingkey` 一般都是有一个或多个单词组成，多个单词之间以”.”分割，例如： `item.insert`
     *  通配符规则：  #：匹配一个或多个词   *：只能匹配一个词
     *
     *  注意：
     *      topic与direct相比，routing key是可以使用通配符的
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue1"),
            exchange = @Exchange(name = "itcast.topic", type = ExchangeTypes.TOPIC),
            key = "china.#"
    ))
    public void listenTopicQueue1(String msg) {
        System.out.println("接收到消息(1)：" + msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue2"),
            exchange = @Exchange(name = "itcast.topic", type = ExchangeTypes.TOPIC),
            key = "#.news"
    ))
    public void listenTopicQueue2(String msg) {
        System.out.println("接收到消息(2)：" + msg);
    }
}
