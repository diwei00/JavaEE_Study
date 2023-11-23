package com.example.chatroom.controller;

import com.example.chatroom.common.ApplicationVariable;
import com.example.chatroom.common.UnifyResult;
import com.example.chatroom.entity.Friend;
import com.example.chatroom.entity.User;
import com.example.chatroom.entity.vo.AddFriendResponseVO;
import com.example.chatroom.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    /**
     * 获得好友列表
     * @param request
     * @return
     */
    @GetMapping("/friendList")
    public UnifyResult selectFriendList(HttpServletRequest request) {
        // 由于拦截器，这个接口用户肯定会登录，为了确保系统安全性这里判断了一下
        HttpSession session = request.getSession(false);
        if(session == null) {
            return UnifyResult.fail(-1, "用户未登录！");
        }
        User user = (User) session.getAttribute(ApplicationVariable.SESSION_KEY_USERINFO);
        if(user == null) {
            return UnifyResult.fail(-1, "用户未登录");
        }
        // 返回好友列表
        List<Friend> friendList = friendService.selectFriendList(user.getUserId());
        return UnifyResult.success(friendList);
    }

    /**
     * 查找好友
     * @param username
     * @return
     */
    @PostMapping("searchFriend")
    public UnifyResult searchFriendList(String username) {
        if(!StringUtils.hasLength(username)) {
            return UnifyResult.fail(-1, "参数有误！");
        }
        List<Friend> friendList = friendService.searchFriendList(username);
        return UnifyResult.success(friendList);
    }

    /**
     * 获取当前用户都有哪些好友添加请求
     * @param user
     * @return
     */
    @GetMapping("/getAddFriend")
    public UnifyResult getAddFriendList(@SessionAttribute(ApplicationVariable.SESSION_KEY_USERINFO) User user) {

        // 查询好友申请ing列表
        List<AddFriendResponseVO> addFriendList = friendService.getAddFriendList(user.getUserId());

        return UnifyResult.success(addFriendList);

    }

    /**
     * 拒绝好友申请
     * @param user
     * @param userId
     * @return
     */
    @PostMapping("refuseAddFriend")
    public UnifyResult deleteAddFriend(@SessionAttribute(ApplicationVariable.SESSION_KEY_USERINFO) User user, Integer userId) {
        if(userId == null) {
            return UnifyResult.fail(-1, "参数有误");
        }
        // 删除数据库中所有关于这条申请记录（因为允许多次添加同一个好友）
        int result = friendService.deleteAddFriend(userId, user.getUserId());
        return UnifyResult.success(result);
    }


}
