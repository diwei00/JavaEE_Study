package com.example.demo.mapper;

import com.example.demo.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// 添加注解，让MyBatis管理（让spring框架去管理）
@Mapper
public interface UserMapper {
    List<UserEntity> getAll();

    // @Param("uid")参数重命名，重命名之后原来名字就失效了
    UserEntity getUserById(@Param("uid") Integer id);

    UserEntity getUserByName(@Param("username") String username);

    UserEntity login(UserEntity user);

    List<UserEntity> getAllByIdOrder(@Param("order") String order);

    // 修改密码
    int updatePassword(@Param("id") String id,
                       @Param("password") String password,
                       @Param("newPassword") String newPassword);

    int deleteById(@Param("id") String id);

    int addUser(UserEntity user);

    // 添加user且获取id
    int addUserGetId(UserEntity user);

    // 根据用户名模糊查询
    List<UserEntity> getListByName(@Param("username") String name);

    // 测试动态sql，使用对应标签
    // 动态sql目的就是为了保证数据的一致性，插入数据的时候有些数据是有默认值的（defined），当不插入
    // 相应字段的时候（我们希望是默认值，而不是数据类型的默认值），那么sql就需要动态改变
    int addUser2(UserEntity user);

    int addUser3(UserEntity user);
}
