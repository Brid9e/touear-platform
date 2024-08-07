package com.touear.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * 安全管理配置
 * @author sjw
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    //提供用户信息，这里没有从数据库查询用户信息，在内存中模拟
//    @Bean
//    public UserDetailsService userDetailsService(){
//        InMemoryUserDetailsManager inMemoryUserDetailsManager =
//                new InMemoryUserDetailsManager();
//        inMemoryUserDetailsManager.createUser(User.withUsername("zs").password("123").authorities("admin").build());
//        return inMemoryUserDetailsManager;
//    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    //密码编码器：不加密
    @Bean
    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    //授权规则配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()                                //授权配置
                .antMatchers("/oauth/**", "/login/**","/logout/**")
                .permitAll()  //登录路径放行
                .anyRequest().authenticated()                   //其他路径都要认证之后才能访问
                .and().formLogin()                              //允许表单登录
                .and().logout().permitAll()                    //登出路径放行
                .and().csrf().disable();                        //关闭跨域伪造检查
    }


    public static void main(String[] args) {
        String password = "123456";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //生成密码
        String encode = passwordEncoder.encode(password);
        log.error(encode);
        //校验密码,参数1是输入的明文 ，参数2是正确密码加密后的串
        boolean matches = passwordEncoder.matches(password, encode);


    }
}
