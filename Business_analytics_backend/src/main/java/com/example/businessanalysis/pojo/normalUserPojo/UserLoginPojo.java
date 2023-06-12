package com.example.businessanalysis.pojo.normalUserPojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName : UserLogin
 * @Description : 用于用户登录注册改密码时传输JSON对象的类
 * @Author : 桓
 * @Date: 2022/11/4  20:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginPojo implements Serializable {

  private static final  long serialVersionUID = 1L;
  /**
   * 用户账号,必填
   */
  private String account;

  /**
   * 用户密码,必填
   */
  private String userPassword;

  /**
   * 用户新密码,选填
   */
  private String newPassword;

  /**
   *  记住我,选填
   */
  private int rememberMe;

  /**
   *  记住我,选填
   */
  private String verCode;

  private Date sendTime;
}
