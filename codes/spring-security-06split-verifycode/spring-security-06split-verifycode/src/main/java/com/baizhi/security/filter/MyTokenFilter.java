package com.baizhi.security.filter;

import com.alibaba.fastjson.JSON;
import com.baizhi.entity.User;
import com.baizhi.util.JacksonUtil;
import com.baizhi.util.WebUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author wang
 * @version V1.0
 * @company Broada.com
 * @date 2023/7/30 11:57
 */
@Component
public class MyTokenFilter extends GenericFilterBean {

    @Resource
    private RedisTemplate<String, String> stringStringRedisTemplate;


//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        System.out.println("进入MyTokenFilter过滤器");
//        String token = (String) request.getHeader("token");
//        if (token != null) {
//
//            String loginUser = (String) stringStringRedisTemplate.opsForValue().get(token);
//
//            if (loginUser != null) {
////                UsernamePasswordAuthenticationToken authentication = JSON.parseObject(loginUser, UsernamePasswordAuthenticationToken.class);
//
//                User user = new ObjectMapper().readValue(loginUser, User.class);
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
//
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
////                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//                User loginUser1 = WebUtil.getLoginUser();
//                stringStringRedisTemplate.opsForValue().set(token, loginUser,60, TimeUnit.MINUTES);
//                System.out.println(loginUser1);
//            }
//        }
//        doFilter(request, response, filterChain);
//        System.out.println("退出MyTokenFilter过滤器");
//    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("进入MyTokenFilter过滤器");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("token");
        if (token != null) {

            String loginUser = stringStringRedisTemplate.opsForValue().get(token);

            if (loginUser != null) {

                User user = JacksonUtil.from(loginUser, User.class);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);

//                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                User loginUser1 = WebUtil.getLoginUser();
                stringStringRedisTemplate.opsForValue().set(token, loginUser,60, TimeUnit.MINUTES);
                System.out.println(loginUser1);
            }
        }
        chain.doFilter(request, response);
//        doFilter(request, response, filterChain);
        System.out.println("退出MyTokenFilter过滤器");
    }
}
