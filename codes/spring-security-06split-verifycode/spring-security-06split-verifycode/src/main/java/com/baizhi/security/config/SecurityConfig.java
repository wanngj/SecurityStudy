package com.baizhi.security.config;

import com.baizhi.security.filter.LoginKaptchaFilter;
import com.baizhi.security.filter.MyTokenFilter;
import com.baizhi.security.metasource.CustomerSecurityMetaSource;
import com.baizhi.service.MyUserDetailService;
import com.baizhi.util.JacksonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.UrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wang
 * @version V1.0
 * @company Broada.com
 * @date 2023/7/30 8:43
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private MyUserDetailService myUserDetailService;

    @Resource
    private MyTokenFilter myTokenFilter;
    @Resource
    private MyLogoutHandler myLogoutHandler;

    @Resource
    private CustomerSecurityMetaSource customerSecurityMetaSource;


     @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


   // @Bean
    public LoginKaptchaFilter loginKaptchaFilter() throws Exception {
        //todo 移到别的地方去
        LoginKaptchaFilter loginKaptchaFilter = new LoginKaptchaFilter();
        loginKaptchaFilter.setKaptchaParameter("kaptcha");
        loginKaptchaFilter.setFilterProcessesUrl("/doLogin");
        loginKaptchaFilter.setUsernameParameter("uname");
        loginKaptchaFilter.setPasswordParameter("passwd");

        loginKaptchaFilter.setAuthenticationManager(authenticationManager());
//        //认证成功处理
//        loginKaptchaFilter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
//            @Override
//            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
//            。。。。。。。。。。。。。
//            }
//        });

//        //认证失败处理
//        loginKaptchaFilter.setAuthenticationFailureHandler((req, resp, ex) -> {
//        。。。。。。。。。。。。。。
//        });

        return loginKaptchaFilter;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //1.获取工厂对象
        ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
        //2.设置自定义 url 权限处理
        http.apply(new UrlAuthorizationConfigurer<>(applicationContext))
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(customerSecurityMetaSource);
                        //是否拒绝公共资源访问
                        object.setRejectPublicInvocations(false);
                        return object;
                    }
                });

        http.authorizeRequests()
//                .mvcMatchers(HttpMethod.GET,"/vc.jpg").permitAll()
//                .mvcMatchers("/admin").hasAnyRole("ADMIN") //具有ADMIN角色的用户才能访问
//                .mvcMatchers("/user").hasAnyRole("USER")//具有USER角色的用户才能访问
//                .mvcMatchers("/getInfo").hasAuthority("READ_INFO")//具有READ_INFO权限的用户才能访问
                .mvcMatchers("/doLogin").permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                .and()
                .logout()
                .logoutUrl("/logout")
                .addLogoutHandler(myLogoutHandler)
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse resp, Authentication auth) throws IOException, ServletException {
                        Map<String, Object> result = new HashMap<>();
                        result.put("msg", "注销成功");


//                        result.put("用户信息", auth.getPrincipal());

                        resp.setContentType("application/json;charset=UTF-8");
                        resp.setStatus(HttpStatus.OK.value());

//                        String s = new ObjectMapper().writeValueAsString(result);
                        String s = JacksonUtil.to(result);
                        resp.getWriter().println(s);

                    }
                })
                .and()
                .csrf().disable();


        http.addFilterBefore(myTokenFilter, UsernamePasswordAuthenticationFilter.class);
      //  http.addFilterAt(loginKaptchaFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
