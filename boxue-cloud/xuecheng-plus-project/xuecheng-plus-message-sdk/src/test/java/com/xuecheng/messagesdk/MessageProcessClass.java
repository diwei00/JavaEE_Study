package com.xuecheng.messagesdk;

import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MessageProcessAbstract;
import com.xuecheng.messagesdk.service.MqMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MessageProcessClass extends MessageProcessAbstract {
    @Autowired
    private MqMessageService mqMessageService;

    // 任务处理逻辑
    @Override
    public boolean execute(MqMessage mqMessage) {
        return false;
    }
}
