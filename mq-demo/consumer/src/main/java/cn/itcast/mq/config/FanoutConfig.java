package cn.itcast.mq.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 第一种方式，基于bean（申明队列和交换机并且绑定）：
 *   将bean注入到spring容器中，即可声明交换机和队列
 *   包括绑定队列和交换机
 */
@Configuration
public class FanoutConfig {

    // 声明队列
    @Bean
    public Queue simpleQueue() {
        return new Queue("simple.queue1");
    }

    /**
     * 声明交换机
     * FanoutExchange：广播交换机，会给每个和自己绑定的队列都发送消息
     * 注意：
     *  交换机不会存储消息，如果没有队列绑定到交换机上，则消息就会丢失
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("itcast.fanout");
    }

    /**
     * 第一个队列
     * @return
     */
    @Bean
    public Queue fanoutQueue1() {
        return new Queue("fanout.queue1");
    }


    /**
     * 绑定队列到交换机
     * @param fanoutQueue1 队列
     * @param fanoutExchange 交换机
     * @return
     */

    @Bean
    public Binding bindingQueue1(Queue fanoutQueue1, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);

    }

    /**
     * 第二个队列
     * @return
     */
    @Bean
    public Queue fanoutQueue2() {
        return new Queue("fanout.queue2");
    }


    @Bean
    public Binding bindingQueue2(Queue fanoutQueue2, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);

    }


}
