package cn.itcast.mq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootTest
public class PublisherTest {
    @Test
    public void testSendMessage() throws IOException, TimeoutException {
        // 1.建立连接
        ConnectionFactory factory = new ConnectionFactory();
        // 1.1.设置连接参数，分别是：主机名、端口号、vhost、用户名、密码
        factory.setHost("192.168.150.101");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("itcast");
        factory.setPassword("123321");
        // 1.2.建立连接
        Connection connection = factory.newConnection();

        // 2.创建通道Channel
        Channel channel = connection.createChannel();

        // 3.创建队列
        String queueName = "simple.queue";
        channel.queueDeclare(queueName, false, false, false, null);

        // 4.发送消息
        String message = "hello, rabbitmq!";
        channel.basicPublish("", queueName, null, message.getBytes());
        System.out.println("发送消息成功：【" + message + "】");

        // 5.关闭通道和连接
        channel.close();
        connection.close();

    }


    @Test
    public void testSendMessage2() throws IOException, TimeoutException {
        // 建立连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setPort(5672);
        factory.setHost("192.168.29.130");
        factory.setVirtualHost("/");  // 设置虚拟主机
        factory.setUsername("wuhao");
        factory.setPassword("123");
        Connection connection = factory.newConnection();

        // 创建通道Channel
        Channel channel = connection.createChannel();

        // 创建队列
        // 生产者和消费者都需要创建队列，防止消费者服务启动后找不到消息队列，但最终只会创建一个消息队列
        String queueName = "simple.queue";
        channel.queueDeclare(queueName, false, false, false, null);

        // 发送消息
        String message = "hello world";
        channel.basicPublish("", queueName, null, message.getBytes());
        System.out.println("消息发送成功：" + message);


        // 释放资源
        channel.close();
        connection.close();

    }

}
