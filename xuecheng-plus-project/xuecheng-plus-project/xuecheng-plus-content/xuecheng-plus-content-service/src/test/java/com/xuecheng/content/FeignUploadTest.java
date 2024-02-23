package com.xuecheng.content;

import com.xuecheng.config.MultipartSupportConfig;
import com.xuecheng.feign.MediaServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@SpringBootTest
public class FeignUploadTest {

    @Autowired
    private MediaServiceClient mediaServiceClient;
    //远程调用，上传文件
    @Test
    public void test() {
        MultipartFile multipartFile =
                MultipartSupportConfig.getMultipartFile(new File("E:\\tmp\\b\\test.html"));
        mediaServiceClient.upload(multipartFile,"course","test.html");
    }
}
