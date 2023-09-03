package cn.itcast.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器，需要实现GlobalFilter接口，重写filter方法
 * 争对所有请求路由都生效，可以自定义逻辑
 *
 * 例如：
 * 登录状态判断
 * 权限校验
 * 请求限流
 * 处理请求
 * ...
 */
// 请求路由后，会将当前路由过滤器和DefaultFilter、GlobalFilter，合并到一个过滤器链（集合）中，排序后依次执行每个过滤器
// 手动指定过滤器顺序，一定要指定order值，需要明确过滤器执行顺序（order值越小，执行时机越早）
// 路由过滤器和defaultFilter的order由Spring指定，默认是按照声明顺序从1递增。
// 当过滤器的order值一样时，会按照 defaultFilter > 路由过滤器 > GlobalFilter的顺序执行。
@Order(-1)
@Component // 注入到spring容器中
public class AuthorizeFilter implements GlobalFilter {
    /**
     *
     * @param exchange 获取请求参数
     * @param chain 请求拦截
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求参数，结果是一个map
        MultiValueMap<String, String> params = exchange.getRequest().getQueryParams();
        // 获取value值
        String auth = params.getFirst("authorization");
        if("admin".equals(auth)) {
            // 放行，直接传递过滤器链，调用下一个过滤器。就等于是放行了
            return chain.filter(exchange);
        }
        // 拦截请求
        // 设置状态码（禁止访问）
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete(); // 进行拦截请求


    }
}
