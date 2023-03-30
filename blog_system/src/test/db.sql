-- 主要写建库建表语句
create database if not exists blog_system charset utf8;
use blog_system;


-- 删除旧表（如果存在的话）
drop table if exists user;
drop table if exists blog;

-- 建表
create table user (
    userId int primary key auto_increment,   -- 设置自增主键
    username varchar(20) unique,  -- 用户名不能重复
    password varchar(20)
);


create table blog(
    blogId int primary key auto_increment,
    title varchar(20),
    content varchar(4096),
    postTime datetime,
    userId int,
    foreign key (userId) references user(userId) -- 设置外键，对数据进行约束

);


-- 构造测试数据
insert into user values(1, '吴浩', 123);
insert into blog values(1, '博客标题1', '博客正文', now(), 1);
insert into blog values(2, '博客标题2', '博客正文', now(), 1);
insert into blog values(3, '博客标题3', '博客正文', now(), 1);
