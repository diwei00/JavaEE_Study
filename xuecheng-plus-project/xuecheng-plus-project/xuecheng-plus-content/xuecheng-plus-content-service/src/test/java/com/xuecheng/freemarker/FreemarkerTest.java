package com.xuecheng.freemarker;

import com.xuecheng.content.model.vo.CoursePreviewVO;
import com.xuecheng.service.ICoursePublishService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class FreemarkerTest {
    @Autowired
    private ICoursePublishService coursePublishService;

    //测试页面静态化
    @Test
    public void testGenerateHtmlByTemplate() throws IOException, TemplateException {
        //配置 freemarker
        Configuration configuration = new Configuration(Configuration.getVersion());
        //加载模板
        //选指定模板路径,classpath 下 templates 下
        //得到 classpath 路径
        String classpath = this.getClass().getResource("/").getPath();
        configuration.setDirectoryForTemplateLoading(new File("E:\\javaCode\\JavaEE_Study\\xuecheng-plus-project\\xuecheng-plus-project\\xuecheng-plus-content\\xuecheng-plus-content-service\\src\\main\\resources\\templates\\"));
        //设置字符编码
        configuration.setDefaultEncoding("utf-8");
        //指定模板文件名称
        Template template = configuration.getTemplate("course_template.ftl");
        //准备数据
        CoursePreviewVO coursePreviewInfo = coursePublishService.getCoursePreviewInfo(1L);
        Map<String, Object> map = new HashMap<>();
        map.put("model", coursePreviewInfo);
        //静态化
        //参数 1：模板，参数 2：数据模型
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        System.out.println(content);
        //将静态化内容输出到文件中
        InputStream inputStream = IOUtils.toInputStream(content);
        //输出流
        FileOutputStream outputStream = new FileOutputStream("E:\\tmp\\b\\test.html");
        IOUtils.copy(inputStream, outputStream);
    }
}


