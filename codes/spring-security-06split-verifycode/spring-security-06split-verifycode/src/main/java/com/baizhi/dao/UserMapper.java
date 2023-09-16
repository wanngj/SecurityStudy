package com.baizhi.dao;


import com.baizhi.entity.Role;
import com.baizhi.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    //根据用户 id 获取角色信息
    List<Role> getUserRoleByUid(Integer uid);
    //根据用户名获取用户信息
    User loadUserByUsername(String username);
}