package com.xuecheng.feign;

import com.xuecheng.config.MultipartSupportConfig;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 指定调用服务名称
 */
@FeignClient(value = "media-api",configuration = MultipartSupportConfig.class, fallbackFactory =
        MediaServiceClientFallbackFactory.class)
public interface MediaServiceClient {

    /**
     * 定义远程调用接口
     * @param filedata
     * @param folder
     * @param objectName
     * @return
     */
    @RequestMapping(value = "/media/upload/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String upload(@RequestPart("filedata") MultipartFile filedata,
                                     @RequestParam(value = "folder", required = false) String folder,
                                     @RequestParam(value = "objectName", required = false) String objectName);

}
