package com.example.businessanalysis.filter;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.example.businessanalysis.domain.User;
import com.example.businessanalysis.pojo.normalUserPojo.LoginUser;
import com.example.businessanalysis.service.UserService;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @ClassName : JwtAuthenticationTokenFilter
 * @Description : 自定义Jwt过滤器
 * @Author : 桓
 * @Date: 2022/11/22  9:36
 */
@Service
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
  @Resource
  UserService userService;
  @Resource
  RedisTemplate<Object,Object> redisTemplate;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = request.getHeader("token");
    if(token==null){
      filterChain.doFilter(request,response);
      return;
    }
    JWT jwt = null;
    try {
      jwt = JWTUtil.parseToken(token);
    }catch (RuntimeException e){
      e.printStackTrace();
      System.out.println("token 错误");
    }
    //验证jwt
    String key = "huan";
    String userName = (String) jwt.setKey(key.getBytes()).getPayload("userName");

    if (!JWT.of(token).setKey(key.getBytes()).validate(0)) {
      // 强制下线
      userService.setLogout(userName);
      throw new RuntimeException("jwt 过期");
    }


    // 获取权限信息封装到Authentication中

    User user = userService.getUser(userName);
    if(user == null) {

      throw new RuntimeException("用户不存在");
    }

    //从redis中读取loginUser;
    LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get(userName);
    if (loginUser==null){
      throw new RuntimeException("用户未登录");
    }

    // 如果这个token已经登录过了
    if(userService.isOnline(userName)){
      System.out.println("用户已登录..");
    }
    userService.setLogin(userName);

    //  完成授权
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());

    // 用于后续验证器调用
    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    // 放行
    filterChain.doFilter(request,response);
  }
}
