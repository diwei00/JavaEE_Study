package cn.itcast.order;

import cn.itcast.feign.clients.UserClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@MapperScan("cn.itcast.order.mapper")
@SpringBootApplication
// 配置defaultConfiguration参数，feign日志级别全局生效
// basePackages：声明项目启动时需要扫描的包。示例：basePackages = "cn.itcast.feign.clients"
// clients：指定具体要注入的bean，更推荐这种做法
@EnableFeignClients(clients = {UserClient.class})  // 项目启动注册feign到spring容器中
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