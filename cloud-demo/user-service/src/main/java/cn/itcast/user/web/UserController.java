package cn.itcast.user.web;

import cn.itcast.user.config.PatternProperties;
import cn.itcast.user.pojo.User;
import cn.itcast.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/user")
@RefreshScope // 这个类实现读取nacos中配置的热跟新，搭配@value注解使用
public class UserController {

    @Autowired
    private UserService userService;

    // 读取nacos中的配置文件
//    @Value("${pattern.dateformat}")
//    private String dateformat;

    @Autowired
    private PatternProperties properties;


    @GetMapping("prop")
    public PatternProperties properties() {
        return properties;
    }

    @GetMapping("/now")
    public String now() {

        // 将当前时间按照指定格式输出
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(properties.getDateformat()));
    }

    /**
     * 路径： /user/110
     *
     * @param id 用户id
     * @return 用户
     */
    @GetMapping("/{id}")
    public User queryById(@PathVariable("id") Long id) {
        return userService.queryById(id);
    }
}
