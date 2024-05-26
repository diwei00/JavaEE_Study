package com.example.poidemo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author wh
 * @Date 2024/5/26 17:32
 */

@Component
public class ApplicationContextUtil {

   private static ApplicationContext applicationContext;

   @Autowired
   public void setApplicationContext(ApplicationContext applicationContext) {
       ApplicationContextUtil.applicationContext = applicationContext;
   }


   public static <T> T getBean(Class<T> tClass){
       return applicationContext.getBean(tClass);
   }

    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }


}
