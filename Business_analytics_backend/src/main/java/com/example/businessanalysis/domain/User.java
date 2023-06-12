package com.example.businessanalysis.domain;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 *
 * @TableName t_user
 */
@TableName(value = "t_user")
@Data

public class User implements Serializable {

  @TableField(exist = false)
  private static final long serialVersionUID = 1L;
  /**
   * 用户昵称
   */
  @Alias("userName")
  private String userName;
  /**
   * 用户账户名
   */
  @TableId
  @Alias("account")
  private String account;
  /**
   * 用户密码MD5加密
   */
  @Alias("userPassword")
  private String userPassword;
  /**
   * 用户性别 0男 1女
   */
  @Alias("gender")
  private Integer gender;
  /**
   * 用户生日
   */
  @Alias("birthday")
  private Date birthday;
  /**
   * 用户生日字符串
   */
  @TableField(exist = false)
  private String birthdayStr;
  /**
   *
   */
  @Alias("mobile")
  private String mobile;
  /**
   *
   */
  @Alias("email")
  private String email;
  @Alias("image")
  private String image;

  /**
   * 上次登录时间
   */
  @Alias("lastLoginTime")
  private Date lastLoginTime;
  /**
   * 用户状态 1在线 0离线
   */
  @Alias("isOnline")
  private Integer isOnline;
  /**
   * 创建时间
   */
  @Alias("addTime")
  private Date addTime;
  /**
   * 更新时间
   */
  @Alias("updateTime")
  private Date updateTime;
  /**
   * 逻辑删除标志
   */
  @Alias("delFlag")
  private Integer delFlag;
  /**
   * 2普通用户  1高级用户 0管理员
   */
  @Alias("roleId")
  private Integer roleId;

}