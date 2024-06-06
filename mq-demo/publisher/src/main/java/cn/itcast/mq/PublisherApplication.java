package cn.itcast.mq;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PublisherApplication {

    public static void main(String[] args) {
        SpringApplication.run(PublisherApplication.class);
    }

    /**
     * 消息转换器：
     * spring默认的对对象转换方式（JDK序列化），比较复杂，数据也比较长，传输消耗资源就比较多
     * 利用这个bean覆盖soring源码中的转换方式
     * 使用json格式进行对象的序列化和反序列化
     *
     * 注意：
     *   发送方和接收方需要使用相同的消息转换器
     * @return
     */
//    @Bean
//    public MessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }

    /**
     * 业务幂等性方案：
     *  1）
     *    这里配置消息id，用于做业务幂等性，防止同一条消息被重复消费
     *    消费者可以存储消息id，当接收到消息后，可验证该消息是否处理过
     *  2）
     *    基于业务进行判断，例如可以用某个业务状态字段判断该消息是否需要处理
     *
     * 消费者可靠性兜底方案：
     *  不可避免MQ的消息丢失，所以需要消费者进行兜底方案，防止消息丢失后，业务数据不一致
     *  可以使用定时任务轮询数据库，定时查询业务状态，如果业务状态为未处理，则进行业务处理
     *
     */
    @Bean
    public MessageConverter messageConverter(){
        // 1.定义消息转换器
        Jackson2JsonMessageConverter jjmc = new Jackson2JsonMessageConverter();
        // 2.配置自动创建消息id，用于识别不同消息，也可以在业务中基于ID判断是否是重复消息
        jjmc.setCreateMessageIds(true);
        return jjmc;
    }

}
