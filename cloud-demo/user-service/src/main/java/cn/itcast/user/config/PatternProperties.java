package cn.itcast.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 用于读取nacos中的配置
 */
@Component
@Data
@ConfigurationProperties(prefix = "pattern") // 读取nacos中的配置，支持配置热跟新
public class PatternProperties {

    private String dateformat;
    private String envSharedValue;
}
