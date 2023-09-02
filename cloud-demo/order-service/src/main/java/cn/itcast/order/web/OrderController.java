package cn.itcast.order.web;

import cn.itcast.feign.clients.UserClient;
import cn.itcast.feign.pojo.User;
import cn.itcast.order.pojo.Order;
import cn.itcast.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("order")
public class OrderController {

   @Autowired
   private OrderService orderService;

    @Autowired
   private RestTemplate restTemplate;

    // 这里需要注入feign的对象，由于已经提取feign为独立模块，这里需要指定扫描的包，注入feign-api中实例到容器中
    @Autowired
    private UserClient userClient;

    @GetMapping("{orderId}")
    public Order queryOrderByUserId(@PathVariable("orderId") Long orderId) {
        // 根据id查询订单并返回
        Order order = orderService.queryOrderById(orderId);
//        String url = "http://localhost:8081/user/" + order.getUserId();

        // 负载均衡会根据服务名称查询具体服务地址端口信息，进行替换操作，访问远程服务
//        String url = "http://user-server/user/" + order.getUserId();

        // 服务调用，发送http请求，得到user对象
        // 方法会自动将json字符串解析为User对象
//        User user = restTemplate.getForObject(url, User.class);

        // 使用feign做远程调用
        User user = userClient.findById(order.getUserId());

        order.setUser(user);

        return order;
    }
}
