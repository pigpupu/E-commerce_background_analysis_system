package com.example.businessanalysis.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 角色表
 *
 * @TableName t_role
 */
@TableName(value = "t_role")
@Data
public class Role implements Serializable {

  @TableField(exist = false)
  private static final long serialVersionUID = 1L;
  /**
   * 角色id
   */
  @TableId
  private Integer id;
  /**
   * 0管理员 1高级用户 2普通用户
   */
  private String roleName;
  /**
   * 角色描述
   */
  private String detail;
  /**
   * 是否启用
   */
  private Integer isEnabled;
  /**
   *
   */
  private Date addTime;
  /**
   *
   */
  private Date updateTime;
  /**
   *
   */
  private Integer delFlag;
}