package com.roadjava.mockito;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.roadjava.mockito.mapper")
public class MockitoApp {
    public static void main(String[] args) {
        SpringApplication.run(MockitoApp.class,args);
    }
}
