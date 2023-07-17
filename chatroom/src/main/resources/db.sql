drop database if exists chatroom;
create database chatroom DEFAULT CHARACTER SET utf8mb4;
use chatroom;

-- 创建user表
drop table if exists  user;
create table user(
    userId int primary key auto_increment,
    username varchar(20) unique,
    password varchar(10) not null,
    email varchar(20) not null
);
-- 添加一条数据
insert into user values(1, 'wuhao', '123', '2945608334@qq.com');
insert into user values(2, 'zhansan', '123', '2945608334@qq.com');
insert into user values(3, 'lisi', '123', '2945608334@qq.com');
insert into user values(4, 'wangwu', '123', '2945608334@qq.com');


-- 创建 用户-好友 关联表
drop table if exists friend;
create table friend(
    userId int,
    friendId int
);
insert into friend values (1, 2),(2, 1);
insert into friend values (1, 3),(3, 1);
insert into friend values (1, 4),(4, 1);

-- 创建会话表
drop table if exists message_session;
create table message_session(
    sessionId int primary key auto_increment,
    -- 上次访问时间
    lastTime datetime
);

insert into message_session values(1, '2000-05-01 00:00:00');
insert into message_session values(2, '2000-06-01 00:00:00');

-- 创建 会话-用户 关联表（用户与会话为多对多关系）
drop table if exists message_session_user;
create table message_session_user(
    sessionId int,
    userId int
);

insert into message_session_user values (1, 1),(1, 2);
insert into message_session_user values (2, 1),(2, 3);

-- 创建消息表
drop table if exists message;
create table message(
    messageId int primary key auto_increment,
    fromId int, -- 来自那个用户
    sessionId int, -- 发送给那个会话
    content varchar(2048), -- 消息正文
    postTime datetime -- 消息发送时间
);


-- wuhao和zhangsan发的消息
insert into message values (1, 1, 1, '今晚吃啥', '2000-05-01 17:00:00');
insert into message values (2, 2, 1, '随便', '2000-05-01 17:01:00');
insert into message values (3, 1, 1, '那吃面?', '2000-05-01 17:02:00');
insert into message values (4, 2, 1, '不想吃', '2000-05-01 17:03:00');
insert into message values (5, 1, 1, '那你想吃啥', '2000-05-01 17:04:00');
insert into message values (6, 2, 1, '随便', '2000-05-01 17:05:00');
insert into message values (11, 1, 1, '那吃米饭炒菜?', '2000-05-01 17:06:00');
insert into message values (8, 2, 1, '不想吃', '2000-05-01 17:07:00');
insert into message values (9, 1, 1, '那你想吃啥?', '2000-05-01 17:08:00');
insert into message values (10, 2, 1, '随便', '2000-05-01 17:09:00');

-- wuhao和lisi发的消息
insert into message values(7, 1, 2, '晚上一起约?', '2000-05-02 12:00:00');



