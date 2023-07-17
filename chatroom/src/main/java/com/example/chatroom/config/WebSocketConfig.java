package com.example.chatroom.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * 配置  websocket 到 Spring 中
 */
@Configuration
@EnableWebSocket // 启动websocket注解
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private WebSocket webSocket;

    /**
     * 注册websocket路由
     * 403解决方案
     * spring 默认提供OriginHandshakeInterceptor的拦截器，这里需要添加拦截器，配置一下规则，才可以正常建立连接
     *
     * 配置WebSocket注意url写法
     * url地址栏和前端连接时路径分格要一致
     * "http://localhost:8080/"
     * "ws://127.0.0.1/XXXX/XXXX";
     * @param registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocket, "/message")
                // 添加拦截器，在HttpSession中setAttribute添加的键值对，同时会存储在websocket中，websocket就可以拿到user对象
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }
}
