package com.example.poidemo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Description 用于从容器中获取对象
 * @Author wh
 * @Date 2024/5/26 17:32
 */
@Component
public class SpringBeanUtil {

   private static ApplicationContext applicationContext;

   @Autowired
   public void setApplicationContext(ApplicationContext applicationContext) {
       SpringBeanUtil.applicationContext = applicationContext;
   }


   public static <T> T getBean(Class<T> tClass){
       return applicationContext.getBean(tClass);
   }

    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }


}
