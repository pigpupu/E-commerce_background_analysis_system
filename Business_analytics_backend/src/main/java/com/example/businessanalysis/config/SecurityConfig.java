package com.example.businessanalysis.config;

import com.example.businessanalysis.filter.JwtAuthenticationTokenFilter;
import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @ClassName : SecurityConfig
 * @Description : spring security 配置类
 * @Author : 桓
 * @Date: 2022/11/22  9:33
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Resource
  private JwtAuthenticationTokenFilter filter;

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception{
    return super.authenticationManagerBean();
  }


  // 放行/login路由

  @Override
  protected void configure(HttpSecurity http) throws Exception{
    http
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .cors().and()
        .authorizeRequests()
        .antMatchers("/login","/register","/forgetPassword","/resetPassword").anonymous()
        // 这样放行还是会经过过滤器链
        .anyRequest().authenticated();

    http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
  }
}
