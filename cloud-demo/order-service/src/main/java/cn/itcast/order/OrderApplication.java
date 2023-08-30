package cn.itcast.order;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@MapperScan("cn.itcast.order.mapper")
@SpringBootApplication
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }


    /**
     * 创建并注入spring容器，用来发送http请求
     * @return
     */
    @Bean
    @LoadBalanced // 负载均衡，拦截http请求，然后走eureka查询提供者信息，根据负载均衡策略实现远程访问具体实例
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // 定义新的IRule，整个微服务中架构中，所有其他服务都有用
    // 给所有微服务配置负载均衡规则
//    @Bean
//    public IRule randomRule() {
//        return new RandomRule();
//    }

}