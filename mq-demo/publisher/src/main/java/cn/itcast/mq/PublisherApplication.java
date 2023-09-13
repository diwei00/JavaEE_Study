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
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
