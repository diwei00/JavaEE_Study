package cn.itcast.feign.clients;
// feign是一个声明式调用。这里只需要声明需要发送的请求，feign会自动扫描，发送请求


import cn.itcast.feign.pojo.User;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * feign客户端，调用接口，feign就可以发送请求，进行远程调用
 * configuration：配置这个属性，争对某个服务配置日志级别
 */
@FeignClient(value = "user-server")  // 服务名称，feign会自动做负载均衡
public interface UserClient {
    // feign是基于springMVC注解实现远程调用信息
    @GetMapping("/user/{id}")
    User findById(@PathVariable("id") Long id);

}
