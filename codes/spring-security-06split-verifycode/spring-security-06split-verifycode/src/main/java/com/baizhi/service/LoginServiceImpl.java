package com.baizhi.service;

import com.baizhi.common.ResponseResult;
import com.baizhi.entity.User;
import com.baizhi.util.JacksonUtil;
import com.baizhi.util.NonUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
@Service
public class LoginServiceImpl {




    @Resource
    private StringRedisTemplate stringStringRedisTemplate;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public ResponseResult<Map<String, Object>> login(@Validated User tryLoginUser) {
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
//        Authentication authenticate = null;
//        try {
//            authenticate = authenticationManager.authenticate(authenticationToken);
//        } catch (AuthenticationException e) {
//          return new ResponseResult<>(400, e.getMessage(), null);
//
//        }
        User existsUser = (User) userDetailsService.loadUserByUsername(tryLoginUser.getUsername());

        if (NonUtil.isNon(existsUser)) {
            return new ResponseResult<>(400, "用户不存在", null);
        }

        if (!bCryptPasswordEncoder.matches(tryLoginUser.getPassword(), existsUser.getPassword())) {
            return new ResponseResult<>(400, "密码错误", null);
        }


        String token = UUID.randomUUID().toString().replace("-", "");

        existsUser.setPassword(null);
        //放入 redis
        stringStringRedisTemplate.opsForValue().set(token, JacksonUtil.to(existsUser), 60, TimeUnit.MINUTES);


        Map<String, Object> result = new HashMap<>();

        result.put("user", existsUser);
        result.put("token", token);
        return new ResponseResult<>(200, "登陆成功", result);
    }

//
}
