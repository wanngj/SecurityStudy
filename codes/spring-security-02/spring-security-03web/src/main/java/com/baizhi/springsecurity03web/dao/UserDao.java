package com.baizhi.springsecurity03web.dao;


import com.baizhi.springsecurity03web.entity.Role;
import com.baizhi.springsecurity03web.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {


    //提供根据用户名返回用户方法
    User loadUserByUsername(String username);

    //提供根据用户id查询用户角色信息方法
    List<Role> getRolesByUid(Integer uid);
}
