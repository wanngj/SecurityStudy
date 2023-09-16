package com.baizhi.controller;

import com.baizhi.common.ResponseResult;
import com.baizhi.entity.User;
import com.baizhi.service.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author wang
 * @version V1.0
 * @company Broada.com
 * @date 2023/8/19 10:09
 */
@RestController
public class LoginController {

    @Resource
    private LoginServiceImpl loginServiceImpl;

    @PostMapping("/doLogin")
    public ResponseResult<Map<String, Object>> login(@RequestBody User user) {
       return loginServiceImpl.login(user);
    }
}
