package com.example.demo.common;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
public class LogoTools {

    public static void printLogo(String path) {
        if(!StringUtils.hasLength(path)) {
            log.warn("缺少文件路径！");
        }
        File file = new File(path);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            StringBuilder name = new StringBuilder();
            int result = 0;
            while ((result = inputStream.read()) != -1) {
                name.append((char) result);
            }
            System.out.println(name);


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
