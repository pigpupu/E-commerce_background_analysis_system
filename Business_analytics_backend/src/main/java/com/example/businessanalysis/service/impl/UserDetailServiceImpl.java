package com.example.businessanalysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.businessanalysis.domain.User;
import com.example.businessanalysis.mapper.UserMapper;
import com.example.businessanalysis.pojo.normalUserPojo.LoginUser;
import javax.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @ClassName : UserDetailServiceImpl
 * @Description : UserDetail 实现类
 * @Author : 桓
 * @Date: 2022/11/22  20:59
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
  @Resource
  UserMapper userMapper;
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("account",username);
    User user = userMapper.selectOne(queryWrapper);
    if (user == null){
      throw new RuntimeException("账号不存在");
    }

    return new LoginUser(user,user.getRoleId());
  }
}
