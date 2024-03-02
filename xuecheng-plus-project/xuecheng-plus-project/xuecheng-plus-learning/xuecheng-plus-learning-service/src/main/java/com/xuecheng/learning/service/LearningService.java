package com.xuecheng.learning.service;

import com.xuecheng.base.model.RestResponse;

/**
 * @author Mr.M
 * @version 1.0
 * @description 学习过程管理 service 接口
 * @date 2022/10/2 16:07
 */
public interface LearningService {
    /**
     * @param courseId    课程 id
     * @param teachplanId 课程计划 id
     * @param mediaId     视频文件 id
     */
    RestResponse<String> getVideo(String userId, Long courseId, Long teachplanId, String mediaId);
}