package com.example.chatroom.component;

import com.example.chatroom.entity.Friend;
import com.example.chatroom.entity.MessageSession;
import com.example.chatroom.entity.MessageSessionUser;
import com.example.chatroom.service.MessageSessionService;
import com.example.chatroom.util.RedisUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Description
 * @Author wh
 * @Date 2025/1/4 16:51
 */

@Slf4j
@Component
public class InitApplicationCacheData {

    @Autowired
    private MessageSessionService messageSessionService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 业务会话缓存预热
     */
    @PostConstruct
    private void initSessionCache() {
        log.info("[初始化session缓存] 开始!");
        List<Integer> sessionIdList = messageSessionService.selectAllSessionId();
        if(CollectionUtils.isEmpty(sessionIdList)) {
            log.info("[初始化session缓存] 获取sessionIdList为空,不做处理！");
            return;
        }
        // 清除现有缓存数据
        redisUtil.delete(sessionIdList);
        log.info("[初始化session缓存] 清除现有缓存数据 会话个数:{}", sessionIdList.size());
        sessionIdList.forEach(sessionId -> {
            List<Integer> friends = messageSessionService.selectUsersBySessionId(sessionId);
            if(!CollectionUtils.isEmpty(friends)) {
                Integer[] friendArr = new Integer[friends.size()];
                friends.toArray(friendArr);
                redisUtil.rightPushAll(sessionId, friendArr);
            }
        });
        log.info("[初始化session缓存] 完成，处理会话个数:{}", sessionIdList.size());
    }


}
