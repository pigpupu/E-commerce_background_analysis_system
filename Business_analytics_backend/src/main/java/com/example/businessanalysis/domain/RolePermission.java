package com.example.businessanalysis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 角色权限表
 *
 * @TableName t_role_permission
 */
@TableName(value = "t_role_permission")
@Data
public class RolePermission implements Serializable {

  @TableField(exist = false)
  private static final long serialVersionUID = 1L;
  /**
   *
   */
  @TableId(type = IdType.AUTO)
  private Integer id;
  /**
   * 角色id
   */
  private Integer roleId;
  /**
   * 权限id
   */
  private Integer permissionId;
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