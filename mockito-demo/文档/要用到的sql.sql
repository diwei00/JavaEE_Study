create database mockito_demo CHARACTER SET utf8mb4;
use mockito_demo;

drop table if exists user;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT comment '主键',
  `username` varchar(100) NOT NULL comment '用户名称',
  `phone` varchar(50) NOT NULL comment '电话',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB comment '用户表';

drop table if exists user_feature;
CREATE TABLE `user_feature` (
  `id` bigint NOT NULL AUTO_INCREMENT comment '主键',
  `user_id` bigint NOT NULL comment '用户id:用户表的主键',
  `feature_value` varchar(150) NOT NULL comment '用户的特征值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB comment '用户特征表';

insert into user(username,phone) values('乐之者java','12345678912');
insert into user_feature(user_id,feature_value) values(1,'abc');
insert into user_feature(user_id,feature_value) values(1,'def');
insert into user_feature(user_id,feature_value) values(1,'ghi');