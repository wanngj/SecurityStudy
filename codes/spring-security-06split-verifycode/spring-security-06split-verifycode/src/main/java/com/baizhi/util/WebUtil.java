package com.baizhi.util;

import com.baizhi.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 * @author wang
 * @version V1.0
 * @company Broada.com
 * @date 2023/8/2 20:55
 */

public class WebUtil {

    public static User getLoginUser() {
        UsernamePasswordAuthenticationToken authentication =(UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        user.setUsername(authentication.getName());
        user.setRoles((List)authentication.getAuthorities());
        return user;
    }

}
