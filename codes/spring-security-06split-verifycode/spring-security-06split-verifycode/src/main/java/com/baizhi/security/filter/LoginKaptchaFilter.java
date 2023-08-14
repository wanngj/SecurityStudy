package com.baizhi.security.filter;

import com.baizhi.entity.User;
import com.baizhi.security.exception.KaptchaNotEqualException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author wang
 * @version V1.0
 * @company Broada.com
 * @date 2023/7/30 9:00
 */

public class LoginKaptchaFilter extends UsernamePasswordAuthenticationFilter {


    private String KaptchaParameter = Kaptcha_KEY;

    public static final String Kaptcha_KEY = "Kaptcha";

    @Resource
    private StringRedisTemplate stringStringRedisTemplate;


    public String getKaptchaParameter() {
        return KaptchaParameter;
    }

    public void setKaptchaParameter(String kaptchaParameter) {
        KaptchaParameter = kaptchaParameter;
    }




    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("进入LoginKaptchaFilter过滤器");
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map<String, String> map = objectMapper.readValue(request.getInputStream(), Map.class);
                String o = map.get(getKaptchaParameter());

                String attribute = (String) request.getSession().getAttribute(getKaptchaParameter());
//                if (!(StringUtils.hasText(o) && StringUtils.hasText(attribute) && o.equals(attribute))) {
//                    throw new KaptchaNotEqualException("验证码错误");
//                }

                String username = map.get(getUsernameParameter());
                String password = map.get(getPasswordParameter());
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
                return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

        return super.attemptAuthentication(request, response);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse resp, FilterChain chain, Authentication authResult) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("msg", "登录成功");
        User user = (User) authResult.getPrincipal();
        result.put("用户信息", user);
        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus(HttpStatus.OK.value());

        String token = UUID.randomUUID().toString().replace("-", "");
        result.put("token", token);
        //放入 redis
        stringStringRedisTemplate.opsForValue().set( token, new ObjectMapper().writeValueAsString(user), 60 , TimeUnit.MINUTES);
//        stringStringRedisTemplate.expire("loginUser", 60 , TimeUnit.SECONDS);

        String s = new ObjectMapper().writeValueAsString(result);
        resp.getWriter().println(s);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse resp, AuthenticationException exception) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("msg", "登录失败: " + exception.getMessage());
        resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        resp.setContentType("application/json;charset=UTF-8");
        String s = new ObjectMapper().writeValueAsString(result);
        resp.getWriter().println(s);
    }




}
