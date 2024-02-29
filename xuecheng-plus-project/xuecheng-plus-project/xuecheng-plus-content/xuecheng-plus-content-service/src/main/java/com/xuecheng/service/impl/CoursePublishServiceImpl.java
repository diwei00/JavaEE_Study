package com.xuecheng.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.utils.StringUtils;
import com.xuecheng.base.exception.CommonError;
import com.xuecheng.base.exception.ServiceException;
import com.xuecheng.config.MultipartSupportConfig;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.model.po.CoursePublishPre;
import com.xuecheng.content.model.vo.CourseBaseInfoVO;
import com.xuecheng.content.model.vo.CoursePreviewVO;
import com.xuecheng.content.model.vo.TeachplanVO;
import com.xuecheng.feign.MediaServiceClient;
import com.xuecheng.mapper.CourseBaseMapper;
import com.xuecheng.mapper.CourseMarketMapper;
import com.xuecheng.mapper.CoursePublishMapper;
import com.xuecheng.mapper.CoursePublishPreMapper;
import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MqMessageService;
import com.xuecheng.service.ICourseBaseService;
import com.xuecheng.service.ICoursePublishService;
import com.xuecheng.service.ITeachplanService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CoursePublishServiceImpl implements ICoursePublishService {
    @Autowired
    private ICourseBaseService courseBaseService;
    @Autowired
    private ITeachplanService teachplanService;
    @Autowired
    private CourseBaseMapper courseBaseMapper;
    @Autowired
    private CourseMarketMapper courseMarketMapper;
    @Autowired
    private CoursePublishPreMapper coursePublishPreMapper;
    @Autowired
    private CoursePublishMapper coursePublishMapper;
    @Autowired
    private MqMessageService mqMessageService;
    @Autowired
    private MediaServiceClient mediaServiceClient;


    /**
     * 获取课程预览信息
     *
     * @param courseId 课程 id
     * @return
     */
    @Override
    public CoursePreviewVO getCoursePreviewInfo(Long courseId) {
        // 课程基本信息、营销信息
        CourseBaseInfoVO courseBaseInfo = courseBaseService.getCourseBaseInfo(courseId);
        // 课程计划信息
        List<TeachplanVO> teachplanTree = teachplanService.getTreeNodes(courseId);

        CoursePreviewVO coursePreviewDto = new CoursePreviewVO();
        coursePreviewDto.setCourseBase(courseBaseInfo);
        coursePreviewDto.setTeachplans(teachplanTree);
        return coursePreviewDto;
    }

    /**
     * 提交审核
     * 注意：设计课程预发布表，实现课程提交后机构修改课程与平台审核课程解耦合
     *
     * @param companyId 机构id
     * @param courseId  课程id
     */
    @Transactional
    @Override
    public void commitAudit(Long companyId, Long courseId) {
        // 约束校验
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        // 课程审核状态
        String auditStatus = courseBase.getAuditStatus();
        // 当前审核状态为已提交不允许再次提交
        if ("202003".equals(auditStatus)) {
            throw new ServiceException("当前为等待审核状态，审核完成可以再次提交。");
        }
        // 本机构只允许提交本机构的课程
        if (!courseBase.getCompanyId().equals(companyId)) {
            throw new ServiceException("不允许提交其它机构的课程.");
        }
        // 课程图片是否填写
        if (StringUtils.isEmpty(courseBase.getPic())) {
            throw new ServiceException("提交失败，请上传课程图片");
        }
        // 添加课程预发布记录
        CoursePublishPre coursePublishPre = new CoursePublishPre();
        // 课程基本信息加部分营销信息
        CourseBaseInfoVO courseBaseInfo = courseBaseService.getCourseBaseInfo(courseId);
        BeanUtils.copyProperties(courseBaseInfo, coursePublishPre);
        // 课程营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        // 转为 json
        String courseMarketJson = JSON.toJSONString(courseMarket);
        // 将课程营销信息 json 数据放入课程预发布表
        coursePublishPre.setMarket(courseMarketJson);
        // 查询课程计划信息
        List<TeachplanVO> teachplanTree = teachplanService.getTreeNodes(courseId);
        if (teachplanTree.size() <= 0) {
            throw new ServiceException("提交失败，还没有添加课程计划");
        }
        // 转 json
        String teachplanTreeString = JSON.toJSONString(teachplanTree);
        coursePublishPre.setTeachplan(teachplanTreeString);
        // 设置预发布记录状态,已提交
        coursePublishPre.setStatus("202003");
        // 教学机构 id
        coursePublishPre.setCompanyId(companyId);
        // 提交时间
        coursePublishPre.setCreateDate(LocalDateTime.now());
        coursePublishPre.setId(courseId);
        CoursePublishPre coursePublishPreUpdate = coursePublishPreMapper.selectById(courseId);
        if (coursePublishPreUpdate == null) {
            // 添加课程预发布记录
            coursePublishPreMapper.insert(coursePublishPre);
        } else {
            coursePublishPreMapper.updateById(coursePublishPre);
        }
        // 更新课程基本表的审核状态
        courseBase.setAuditStatus("202003");
        courseBaseMapper.updateById(courseBase);
    }

    @Transactional
    @Override
    public void publish(Long companyId, Long courseId) {

        // 约束校验
        // 查询课程预发布表
        CoursePublishPre coursePublishPre = coursePublishPreMapper.selectById(courseId);
        if (coursePublishPre == null) {
            throw new ServiceException("请先提交课程审核，审核通过才可以发布");
        }
        // 本机构只允许提交本机构的课程
        if (!coursePublishPre.getCompanyId().equals(companyId)) {
            throw new ServiceException("不允许提交其它机构的课程");
        }
        // 课程审核状态
        String auditStatus = coursePublishPre.getStatus();
        // 审核通过方可发布
        if (!"202004".equals(auditStatus)) {
            throw new ServiceException("操作失败，课程审核通过方可发布");
        }
        // 保存课程发布信息
        saveCoursePublish(courseId);
        // 保存消息表
        saveCoursePublishMessage(courseId);
        // 删除课程预发布表对应记录
        coursePublishPreMapper.deleteById(courseId);
    }

    @Override
    public File generateCourseHtml(Long courseId) {
        // 静态化文件
        File htmlFile = null;
        try {
            // 配置 freemarker
            Configuration configuration = new Configuration(Configuration.getVersion());
            // 加载模板
            // 选指定模板路径,classpath 下 templates 下
            // 得到 classpath 路径
            String classpath = this.getClass().getResource("/").getPath();
            configuration.setDirectoryForTemplateLoading(new File("E:\\javaCode\\JavaEE_Study\\xuecheng-plus-project\\xuecheng-plus-project\\xuecheng-plus-content\\xuecheng-plus-content-service\\src\\main\\resources\\templates\\"));
            // 设置字符编码
            configuration.setDefaultEncoding("utf-8");
            // 指定模板文件名称
            Template template = configuration.getTemplate("course_template.ftl");
            // 准备数据
            CoursePreviewVO coursePreviewInfo = this.getCoursePreviewInfo(courseId);
            Map<String, Object> map = new HashMap<>();
            map.put("model", coursePreviewInfo);
            // 静态化
            // 参数 1：模板，参数 2：数据模型
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
            // System.out.println(content);
            // 将静态化内容输出到文件中
            InputStream inputStream = IOUtils.toInputStream(content);
            // 创建静态化文件
            htmlFile = File.createTempFile("course", ".html");
            log.debug("课程静态化，生成静态文件:{}", htmlFile.getAbsolutePath());
            // 输出流
            FileOutputStream outputStream = new FileOutputStream(htmlFile);
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            log.error("课程静态化异常:{}", e.toString());
            throw new ServiceException("课程静态化异常");
        }
        return htmlFile;
    }


    @Override
    public void uploadCourseHtml(Long courseId, File file) {

        // 将file转为multipartFile
        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(file);
        // 远程调用上传接口
        String course =
                mediaServiceClient.upload(multipartFile, null, "course/" + courseId + ".html");
        if (course == null) {
            throw new ServiceException("上传静态文件异常");
        }
    }

    @Override
    public CoursePublish getCoursePublish(Long courseId) {
        return coursePublishMapper.selectById(courseId);
    }

    private void saveCoursePublish(Long courseId) {
        // 整合课程发布信息
        // 查询课程预发布表
        CoursePublishPre coursePublishPre = coursePublishPreMapper.selectById(courseId);
        if (coursePublishPre == null) {
            throw new ServiceException("课程预发布数据为空");
        }
        CoursePublish coursePublish = new CoursePublish();
        // 拷贝到课程发布对象
        BeanUtils.copyProperties(coursePublishPre, coursePublish);
        coursePublish.setStatus("203002");
        CoursePublish coursePublishUpdate = coursePublishMapper.selectById(courseId);
        if (coursePublishUpdate == null) {
            // 不存在，插入
            coursePublishMapper.insert(coursePublish);
        } else {
            // 存在，更新
            coursePublishMapper.updateById(coursePublish);
        }
        // 更新课程基本表的发布状态
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        courseBase.setStatus("203002");
        courseBaseMapper.updateById(courseBase);
    }

    /**
     * @param courseId 课程id
     * @return void
     * @description 保存消息表记录
     */
    private void saveCoursePublishMessage(Long courseId) {
        MqMessage mqMessage = mqMessageService.addMessage("course_publish",
                String.valueOf(courseId), null, null);
        if (mqMessage == null) {
            throw new ServiceException(CommonError.UNKOWN_ERROR.toString());
        }
    }
}