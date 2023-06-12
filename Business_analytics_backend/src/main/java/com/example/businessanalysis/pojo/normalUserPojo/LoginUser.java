package com.example.businessanalysis.pojo.normalUserPojo;

import com.example.businessanalysis.domain.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @ClassName : LoginUser
 * @Description : 用户登录实体类
 * @Author : 桓
 * @Date: 2022/11/22  9:42
 */
@Component
@Data
@NoArgsConstructor
public class LoginUser implements UserDetails, Serializable {
  private User user;


  // 这里将返回用户权限信息

  private List<String> authorizeList;

  public LoginUser(User user) {
    this(user,user.getRoleId());
  }

  public LoginUser(User user,int roleId){
    this.user = user;
    // 查询权限信息,封装到UserDetail对象中
    List<String> list = new ArrayList<>();
    if(roleId>-1){
      list.add("normal user");
    }
    if(roleId>0){
      list.add("advanced user");
    }
    if(roleId>1){
      list.add("admin");
    }
    authorizeList = list;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> res = new ArrayList<>();
    for(String i : authorizeList){
      res.add(new SimpleGrantedAuthority(i));
    }
    return res;
  }

  @Override
  public String getPassword() {
    return user.getUserPassword();
  }

  @Override
  public String getUsername() {
    return user.getAccount();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true ;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
  private static final  long serialVersionUID = 1L;
}
