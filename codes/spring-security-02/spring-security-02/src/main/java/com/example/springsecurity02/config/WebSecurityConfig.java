package com.example.springsecurity02.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import javax.annotation.Resource;

/**
 * @author wang
 * @version V1.0
 * @company Broada.com
 * @date 2023/7/23 17:42
 */

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private  MyUserDetailService myUserDetailService;
//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
//        userDetailsService.createUser(User.withUsername("aaa").password("{noop}1234").roles("admin").build());
//        return userDetailsService;
//    }

    //springboot 对 security 默认配置中  在工厂中默认创建 AuthenticationManager
//    默认⾃动配置创建全局AuthenticationManager 默认找当前项⽬中是否存在⾃
//定义 UserDetailService 实例 ⾃动将当前项⽬ UserDetailService 实例
//设置为数据源
//    @Autowired
//    public void initialize(AuthenticationManagerBuilder builder) throws Exception {
//        System.out.println("springboot 默认配置: " + builder);
//        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//        inMemoryUserDetailsManager.createUser(User.withUsername("aaa").password("{noop}123").roles("p1").build());
//        builder.userDetailsService(inMemoryUserDetailsManager);
//    }

//    自定义AuthenticationManager  推荐  并没有在工厂中暴露出来
    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        System.out.println("自定义AuthenticationManager: " + builder);
//        builder.userDetailsService(userDetailsService()); //这里 改的 认证方式 是 DaoAuthenticationProvider （默认的）
        builder.userDetailsService(myUserDetailService);
    }

    //作用: 用来将自定义AuthenticationManager在工厂中进行暴露,可以在任何位置注入
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/index").permitAll()//放行的资源写前面
                .mvcMatchers("/auth/login").permitAll()//放行的资源写前面
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/auth/login")
                .loginProcessingUrl("/doLogin")//登录请求拦截的url,也就是form表单提交时指定的action 这里实际没啥用
                .usernameParameter("uname")
                .passwordParameter("passwd")
//                .successForwardUrl("/index") //认证成功 forward 跳转路径  始终在认证成功之后跳转到指定请求
                //.defaultSuccessUrl("/index", true) //认证成功 redirect 之后跳转   根据上一保存请求进行成功跳转
                .successHandler(new MyAuthenticationSuccessHandler())// 前后端分离时使用
                //.failureForwardUrl("/login.html") //认证失败之后 forward 跳转
                //.failureUrl("/login.html") // 默认 认证失败之后 redirect 跳转
                .failureHandler(new MyAuthenticationFailureHandler()) //用来自定义认证失败之后处理  前后端分离解决方案
                .and()
                .logout()
//                .logoutUrl("/logout") //默认 退出登录的url  默认请求方式必须: GET
                .logoutRequestMatcher(new OrRequestMatcher(new AntPathRequestMatcher("/aa", "GET"),
                                      new AntPathRequestMatcher("/bb", "POST"))) //自定义退出登录的url  可以指定多个url
//                .invalidateHttpSession(true) //默认 会话失效

                .clearAuthentication(true)   //默认 清楚认证标记
//                .logoutSuccessUrl("/auth/login")
                .logoutSuccessHandler(new MyLogoutSuccessHandler()) //自定义退出登录成功之后处理  前后端分离解决方案
                .and().csrf().disable();
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }
}
