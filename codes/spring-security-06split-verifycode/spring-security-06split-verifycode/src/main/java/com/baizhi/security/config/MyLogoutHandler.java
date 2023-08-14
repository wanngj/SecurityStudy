package com.baizhi.security.config;

import com.baizhi.entity.User;
import com.baizhi.util.WebUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wang
 * @version V1.0
 * @company Broada.com
 * @date 2023/8/5 16:03
 */
@Configuration
public class MyLogoutHandler implements LogoutHandler {


    @Resource
    private RedisTemplate<String, String> stringStringRedisTemplate;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = (String) request.getHeader("token");
        if (token != null) {

            String loginUser = (String) stringStringRedisTemplate.opsForValue().get(token);

            if (loginUser != null) {

                stringStringRedisTemplate.delete(token);
            }

        }
    }
}

