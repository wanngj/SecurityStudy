package com.baizhi.service;

import com.baizhi.dao.UserDao;
import com.baizhi.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

//自定义 UserDetailService 实现
@Service
public class MyUserDetailService implements UserDetailsService {


    @Resource
    private  UserDao userDao;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userDao.loadUserByUid(username);
        if (ObjectUtils.isEmpty(user)) throw new RuntimeException("用户名不存在!");
        user.setRoles(userDao.getRolesByUid(user.getId()));
        return user;
    }
}
