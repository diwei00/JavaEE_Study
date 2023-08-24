// 设置点击切换对话和好友界面
function initSwitchTab() {
    var tabSession = jQuery(".tab .tab-session");
    var tabFriend = jQuery(".tab .tab-friend");
    var sessionList = jQuery("#session-list");
    var friendList = jQuery("#friend-list");
    var tabAddFriend = jQuery(".tab .tab-add-friend");
    var addFriendList = jQuery("#add-friend-list");

    // 选中会话列表
    tabSession.click(function() {
        // 设置css属性
        tabSession.css("backgroundImage", 'url(img/对话.png)')
        tabFriend.css("backgroundImage", 'url(img/用户2.png)')
        tabAddFriend.css("backgroundImage", 'url(img/添加好友2.png)');
        
        // 会话列表显示，好友列表隐藏（设置html属性）
        sessionList.attr("class", "list");
        friendList.attr("class", "list hide");
        addFriendList.attr("class", "list hide");

    });
    // 选中好友列表
    tabFriend.click(function() {

        tabSession.css("backgroundImage", 'url(img/对话2.png)')
        tabFriend.css("backgroundImage", 'url(img/用户.png)')
        tabAddFriend.css("backgroundImage", 'url(img/添加好友2.png)');
        // 会话列表隐藏，好友列表显示
        sessionList.attr("class", "list hide");
        friendList.attr("class", "list");
        addFriendList.attr("class", "list hide");
    });

    // 选中添加好友列表
    tabAddFriend.click(function () {
        // 设置css属性
        tabSession.css("backgroundImage", 'url(img/对话2.png)')
        tabFriend.css("backgroundImage", 'url(img/用户2.png)')
        tabAddFriend.css("backgroundImage", 'url(img/添加好友.png)');

        // 会话列表显示，好友列表隐藏（设置html属性）
        sessionList.attr("class", "list hide");
        friendList.attr("class", "list hide");
        addFriendList.attr("class", "list");

    });
}
initSwitchTab();

// 获取用户信息
function getUserInfo() {
    var username = jQuery("#username");
    jQuery.ajax({
       url: "/user/userInfo",
       type: "GET",
       data:{},
       success:function (res) {
           if(res.data != null && res.data.userId > 0) {
               // 数据响应成功
               username.attr("user-id", res.data.userId);
               username.html(res.data.username);
           }else {
                alert("当前用户未登录！");
                location.href = "login.html";
           }
       }
    });

}
getUserInfo();

// 获取好友列表
function getFriendList() {
    let friendList = document.querySelector("#friend-list");
    // 请求后端得到好友列表
    jQuery.ajax({
        url: "/friend/friendList",
        type: "GET",
        data:{},
        success:function(res) {
            if(res.data != null && res.data.length > 0) {
                // 得到好友列表
                // 首先清空好友列表
                friendList.innerHTML = "";
                
                for(let friend of res.data) {
                    // 保存friendId到li标签上
                    let li = document.createElement("li");
                    li.setAttribute("friend-id", friend.friendId);

                    li.innerHTML = '<h4>' + friend.friendName + '</h4>';
                    friendList.appendChild(li);

                    // 为每个li标签添加点击属性
                    li.onclick = function() {
                        clickFriend(friend);
                    }
                }

            }else {
                // // 暂无好友
                // friendList.innerHTML = "<p style='margin-left: 20px; color: rgb(238, 238, 238)'>暂无好友</p>";
            }
        },
        error:function() {
            console.log("获取好友列表失败！");
        }
    });


}
getFriendList();

// 获取会话列表
function getSessionList() {
    let sessionListUL = document.querySelector("#session-list");
    jQuery.ajax({
        url: "/session/sessionList",
        type: "GET",
        data:{},
        success:function(res) {
            if(res.data != null && res.data.length > 0) {
                // 存在会话

                // 首先清空当前会话列表
                sessionListUL.innerHTML = "";
                for (let session of res.data) {
                    // 对于lastMessage长度进行处理
                    if (session.lastMessage.length > 10) {
                        session.lastMessage = session.lastMessage.substring(0, 10) + "...";
                    }

                    let li = document.createElement("li");
                    // 添加sessionId到li标签中
                    li.setAttribute("message-session-id", session.sessionId);

                    li.innerHTML += '<h3>' + session.friends[0].friendName + '</h3>';
                    li.innerHTML += '<p>' + session.lastMessage + '</p>';

                    // 添加li标签到dom元素中
                    sessionListUL.appendChild(li);

                    // 为每个li标签添加点击事件
                    li.onclick = function () {
                        clickSession(li);
                    }

                }

            }
            // }else {
            //     // 当前没有会话
            //     sessionListUL.innerHTML = "<p style='margin-left: 20px; color: rgb(238, 238, 238)'>当前没有会话！</p>";
            //
            // }
        }
    });
}
getSessionList();

function clickSession(currentLi) {
    // 设置高亮
    let allLis = document.querySelectorAll("#session-list>li");

    activeSession(allLis, currentLi);
    // 获取指定会话历史消息
    let sessionId = currentLi.getAttribute("message-session-id");
    getHistoryMessage(sessionId);

    let session = document.querySelector(".right .session");
    let friends = document.querySelector(".right .friends");
    // 控制右侧页面显示
    session.className = "session";
    friends.className = "friends hide";


}
function activeSession(allLis, currentLi) {
    // 遍历所有li标签，和点击一致就设置高亮，其他为原本模样

    for(let li of allLis) {
        if(li === currentLi) {
            li.className = "selected";
        }else {
            li.className = "";
        }
    }
}

// 获取历史消息
function getHistoryMessage(sessionId) {
    let titleDiv = document.querySelector(".right .title");
    titleDiv.innerHTML = "";
    let messageShowDiv = document.querySelector(".right .message-show");
    messageShowDiv.innerHTML = "";

    // 设置当前点击会话右侧好友名
    let h3 = document.querySelector("#session-list .selected>h3");
    if(h3 != null) {
        titleDiv.innerHTML = h3.innerHTML;
    }
   
    
    // 发起请求，拿到历史消息
    jQuery.ajax({
        url: "/message/getMessage?sessionId=" + sessionId,
        type: "GET",
        success:function(res) {
            for(let message of res.data) {
                addMessage(message, messageShowDiv);

                // 构造好消息之后，滚动条自动滚动到最下面
                scrollBottom(messageShowDiv);
            }
        }
        
    });
}
// 控制滚动条到底部
function scrollBottom(elem) {
    // 可视区域高度
    let clientHeight = elem.offsetHeight;

    // 内容总高度
    let scrollHeight = elem.scrollHeight;

    // 横向  纵向   
    elem.scrollTo(0, scrollHeight - clientHeight);

}
function addMessage(message, messageShowDiv) {
    // 得到当前登录用户名
    let username = document.querySelector(".left .user").innerHTML;

    // 这个div展示一条消息
    let div = document.createElement('div');

    div.innerHTML += '<div class="box">' + 
    '<h4>'+ message.fromName +'</h4>' + '<p>'+ message.content +'</p>'
    + ' </div>';

    if(message.fromName === username) {
        // 设置消息在右侧区域
        div.className = 'message message-right';

    }else {
        // 设置消息在左侧区域
        div.className = 'message message-left';
    }
    messageShowDiv.appendChild(div);   
}

// 点击好友列表
function clickFriend(friend) {
    // 判定当前好友是否有会话，如果有，置顶会话，显示历史消息，高亮显示
    // 如果没有，创建会话，置顶，告诉服务器
    let sessionLi = findSessionByName(friend.friendName);
    let sessionListUL = document.querySelector("#session-list");
    if(sessionLi != null) {
        // 存在点击好友会话
        // 置顶操作
        sessionListUL.insertBefore(sessionLi, sessionListUL.children[0]);
        // 模拟点击，显示高亮，和历史消息(上面实现过)
        sessionLi.click();


    }else {
        // 不存在点击好友会话
        // 创建li标签
        sessionLi = document.createElement("li");
        sessionLi.innerHTML = '<h3>'+ friend.friendName +'</h3>' + '<p></p>';
        sessionListUL.appendChild(sessionLi);
        // 置顶当前li标签
        sessionListUL.insertBefore(sessionLi, sessionListUL.children[0]);
        // 创建点击事件
        sessionLi.onclick = function() {
            clickSession(sessionLi);
        }
        // 模拟点击，显示高亮和历史消息
        sessionLi.click();
        // 发送消息给服务器，存储当前新增会话信息
        createSession(friend.friendId, sessionLi);
    }
    // 切换到会话列表
    let tabSession = document.querySelector(".tab .tab-session");
    // 模拟点击进行切换
    tabSession.click();


}
function findSessionByName(friendName) {
    let sessionLis = document.querySelectorAll("#session-list>li");
    for(li of sessionLis) {
        let h3 = li.querySelector('h3');
        if(h3.innerHTML == friendName) {
            return li;
        }
    }
    return null;
}

function createSession(friendId, sessionLi) {
    jQuery.ajax({
        url: "session/createSession",
        type: "POST",
        data:{
            "toUserId": friendId
        },
        success:function(res) {
            // 创建会话成功
            sessionLi.setAttribute("message-session-id", res.data.sessionId);
        },
        error:function() {
            console.log("会话创建失败！");
        }

    });
}
// 发送消息模块，配置websocket
let websocket = new WebSocket("ws://" + location.host + "/message")
websocket.onopen = function() {
    // 连接建立完成
    console.log("连接建立完成");
}
websocket.onclose = function() {
    // 连接正常断开
    console.log("连接正常断开");
}
websocket.onerror = function() {
    // 连接异常断开
    console.log("连接异常断开");
}
websocket.onmessage = function(e) {
    // 收到消息后自动执行
    console.log("收到消息" + e.data);

    // 将接收消息转换为js对象
    let resp = JSON.parse(e.data);
    if(resp.type == "message") {
        // 进行页面内容设置
        handleMessage(resp);
    }else if(resp.type == "addFriend"){
        // 进行好友申请区域页面设置
        handleAddFriend(resp);

    }else if(resp.type == "friend"){
        // 同意好友请求，构造页面内容
        handleAgreeFriend(resp);
    }else {
        console.log("resp的type有误！");
    }

}

// 实现消息发送功能
function initSendButton() {
    let sendButton = document.querySelector(".right .ctrl button");
    let messageInput = document.querySelector(".right .message-input");
    sendButton.onclick = function() {
        // 判定输入框有无输入
        if(messageInput.value == null || messageInput.value == "") {
            return false;
        }
        // 得到li标签,存储了sessionId
        let selectedLi = document.querySelector("#session-list .selected");
        if(selectedLi == null) {
            // 当前没有会话被选中
            return;
        }

        // 得到点击会话中的sessionId
        let sessionId = selectedLi.getAttribute("message-session-id");

        // 构造数据，进行发送
        let req = {
            type: "message",
            sessionId: sessionId,
            content: messageInput.value
        };
        // 转换js对象为json字符串
        req = JSON.stringify(req);
        // 发送数据
        websocket.send(req);
        // 清空消息输入框
        messageInput.value = "";
    }
}
initSendButton();

// 实现消息接收功能
function handleMessage(resp) {
    // 展示客户端收到的消息
    // 1. 找到会话中对应li标签，如果没找到则创建li会话
    let curSessionLi  = findSessionLi(resp.sessionId);
    if(curSessionLi == null) {
        // 创建会话
        curSessionLi = document.createElement("li");
        curSessionLi.setAttribute("message-session-id", resp.sessionId);
        curSessionLi.innerHTML = '<h3>'+ resp.fromName +'</h3>' + '<p></p>';
        
        // 绑定点击事件
        curSessionLi.onclick = function() {
            clickSession(curSessionLi);
        }
    }
    // 2. 会话部分显示消息预览信息
    let p = curSessionLi.querySelector("p");
    let content = resp.content; 
    if(content.length > 10) {
        content = content.substring(0, 10) + "...";
    }
    p.innerHTML = content;
    // 3. 置顶当前会话
    let sessionListUL = document.querySelector("#session-list");
    sessionListUL.insertBefore(curSessionLi, sessionListUL.children[0]);
    // 4. 如果当前会话是被选中的，构造消息到右侧主消息区
    if(curSessionLi.className == 'selected') {
        // 构造一条消息到主消息区
        let messageShowDiv = document.querySelector(".right .message-show");
        addMessage(resp, messageShowDiv);
        scrollBottom(messageShowDiv);

    }
    
}
function findSessionLi(targetSessionId) {
    let sessionLis = document.querySelectorAll("#session-list li");
    for(let li of sessionLis) {
        let sessionId = li.getAttribute("message-session-id");
        if(sessionId == targetSessionId) {
            return li;
        }
    }
    // 两个客户端之间没有会话
    return null;
}

// 实现好友搜索功能
function searchFriends() {
    let input = document.querySelector(".search input");
    let searchButton = document.querySelector(".search #search-button");
    let friendsShow = document.querySelector(".right .friends-show");
    let session = document.querySelector(".right .session");
    let friends = document.querySelector(".right .friends");
    searchButton.onclick = function() {
        if(input.value == null || input.value == "") {
            // 用户没有输入啥也不做
            return;
        }
        friendsShow.innerHTML = "";
        // 控制右侧页面显示
       session.className = "session hide";
       friends.className = "friends";
        
        // 提交数据到服务器
        jQuery.ajax({
            url: "friend/searchFriend",
            type: "POST",
            data: {
                "username": input.value
            },
            success:function(res) {
                input.value = "";

                if(res.data != null && res.data.length > 0) {
                    // 查找到好友
                    for(let user of res.data) {
                        let div = document.createElement('div');
                        div.className = "friend";
                        div.setAttribute("userId", user.friendId);
                        div.setAttribute("username",  user.friendName);

                        let input = document.createElement('input');
                        input.type = "text";
                        input.placeholder = "发送添加好友申请";


                        div.innerHTML += '<span userId='+ user.friendId+'>'+ user.friendName +'</span>';
                        div.appendChild(input);
                        let button = document.createElement("button");
                        button.innerHTML = "添加";
                        div.appendChild(button);
                        // div.innerHTML += '<button onclick="addFriend(div, input)">添加</button>';
                        friendsShow.appendChild(div);
                        // 绑定点击事件
                        button.onclick = function () {
                            addFriend(div, input);
                        }
                    }
                }else {
                    friendsShow.innerHTML += '<h3 style="padding-left: 40%; margin-top: 10px">不存在当前用户</h3>';
                }
            }
        });
    }

}
searchFriends();

// 实现好友添加功能
function addFriend(div, input) {
    // 1. 用户未输入好友添加申请
    if(input.value == null || input.value == '') {
        alert("请先输入好友添加申请！");
        return;
    }
    // 2. 如果已经是好友就不能再进行添加操作
    let addUserName = div.getAttribute("username");
    let friendList = document.querySelectorAll(".main .left #friend-list h4");
    let username = document.querySelector(".main .left .user").innerHTML;

    // 新用户自己添加自己时，由于还没有用户，for循环进不去，因此在这里进行判断
    if(addUserName == username) {
        alert("你们已经是好友，无需再次添加！");
        input.value = "";
        return;
    }
    for(let friend of friendList) {
        if(addUserName == friend.innerHTML || addUserName == username) {
            alert("你们已经是好友，无需再次添加！");
            input.value = "";
            return;
        }
    }

    // 客户端通过websocket进行好友添加请求发送
    // 服务器进行消息转发
    // 客户端在线，直接调整好友请求列表
    // 客户端如果不在线，页面加载走数据库进行查询（服务端进行数据保存）

    // 构造请求对象
    let req = {
        type: "addFriend",
        userId: div.getAttribute("userId"),
        input: input.value
    };
    req = JSON.stringify(req);
    // 发送数据
    websocket.send(req);
    alert("好友添加请求已发送！");
    input.value = "";


}
// 实现接收好友添加功能
function handleAddFriend(resp) {
    // 构建添加好友列表
    let addFriendList = document.querySelector(".main .left #add-friend-list");
    let li = document.createElement('li');
    // 用li标签存储当前申请用户id
    li.setAttribute("userId", resp.userId);
    li.innerHTML = '<h3>'+ resp.username +'</h3>'
    + '<p>'+ resp.input +'</p>';

    let agreeButton = document.createElement("button");
    let refuseButton = document.createElement("button");
    agreeButton.innerHTML = "同意"
    refuseButton.innerHTML = "拒绝"
    li.appendChild(agreeButton);
    li.appendChild(refuseButton);

    addFriendList.appendChild(li);

    // 置顶当前li标签
    addFriendList.insertBefore(li, addFriendList.children[0]);

     // 为按钮绑定点击事件
    agreeButton.onclick = function() {
        // 同意好友请求
        agreeAddFriend(li);
    }
    refuseButton.onclick = function() {
        // 拒绝好友请求
        refuseAddFriend(li);
    }

}

// 实现用户登录获取历史好友请求
function getHistoryAddFriend() {
    jQuery.ajax({
        url: "friend/getAddFriend",
        type: "GET",
        data: {},
        success:function(res) {
            // 首先清空原来消息内容
            let addFriendList = document.querySelector(".main .left #add-friend-list");
            addFriendList.innerHTML = "";
            if(res.data != null && res.data.length > 0) {
                // 存在历史好友请求
                // 遍历构造到好友请求列表
                for(let friend of res.data) {
                    let li = document.createElement("li");
                    // 用li标签存储当前申请用户id
                    li.setAttribute("userId", friend.userId);
                    li.innerHTML = '<h3>'+ friend.username +'</h3>'
                    + '<p>'+ friend.input +'</p>';
                    let agreeButton = document.createElement("button");
                    let refuseButton = document.createElement("button");
                    agreeButton.innerHTML = "同意"
                    refuseButton.innerHTML = "拒绝"
                    li.appendChild(agreeButton);
                    li.appendChild(refuseButton);

                    addFriendList.appendChild(li);

                    // 为按钮绑定点击事件
                    agreeButton.onclick = function() {
                        // 同意好友请求
                        agreeAddFriend(li);
                    }
                    refuseButton.onclick = function() {
                        // 拒绝好友请求
                        refuseAddFriend(li);
                    }
                }
            }
        }
    });
}
getHistoryAddFriend();
// 同意好友请求，通过websocket进行发送
function agreeAddFriend(li) {
    // 这里需要使用websocket进行通信，因为同意后，双方页面需要接收到服务器推送
    // 构造请求对象
    let req = {
        type: "friend",
        userId: li.getAttribute("userid")
    };
    req = JSON.stringify(req);
    websocket.send(req);
    console.log("同意好友请求" + req);

    /// 遍历所有li标签，删除所有关于点击用户的li标签
    deleteHtml(li);

}
// 拒绝好友请求
function refuseAddFriend(li) {
    // 直接删除好友申请表中的记录
    // 用户可能向同一个好友发送多条添加信息，直接全部删除
    jQuery.ajax({
       url: "friend/refuseAddFriend",
       type: "POST", 
       data: {
           userId: li.getAttribute("userid")
        },
        success:function(res) {
            // 遍历所有li标签，删除所有关于点击用户的li标签
            deleteHtml(li);
        }

    });

}

// 删除li标签
function deleteHtml(li) {
    let lis = document.querySelectorAll(".main .left #add-friend-list li");
    for(let addFriendLi of lis) {
        if(addFriendLi.getAttribute("userid") == li.getAttribute("userid")) {
            // 移除li中标签
            while (addFriendLi.firstChild) {
                addFriendLi.removeChild(addFriendLi.firstChild);
            }
            addFriendLi.remove();
            
        }
    }
}

// 同意好友请求，接收服务器推送消息
function handleAgreeFriend(resp) {
    // 构造好友列表
    let friendList = document.querySelector(".main .left #friend-list");
    let li = document.createElement("li");
    li.setAttribute("friend-id", resp.friendId);
    li.innerHTML = '<h4>' + resp.friendName + '</h4>';
    friendList.appendChild(li);

    // 为每个li标签添加点击属性
    li.onclick = function () {
        clickFriend(resp);
    }


}