package com.example.businessanalysis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 客户表
 * @TableName t_customer
 */
@TableName(value ="t_customer")
@Data
public class Customer implements Serializable {
  /**
   * 客户id
   */
  @TableId
  private String id;

  /**
   * 客户名称
   */
  private String customerName;

  /**
   * 客户出生日期
   */
  private Date birthday;

  /**
   * 客户性别 0男1女
   */
  private Integer gender;

  /**
   * 客户婚姻情况 0不明 1未婚 2已婚
   */
  private Integer marriage;

  /**
   * 客户学历 0不明 1小学及以下 2中学 3高中 4大学 5大学及以上
   */
  private Integer degree;

  /**
   * 收入水平 0不明 1低收入 2中收入 3高收入
   */
  private Integer incomeLevel;

  /**
   * 客户类型 0为零售客户 1是批发客户
   */
  private Integer customerType;

  /**
   * 客户感兴趣的商品id
   */
  private String interestId;

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

  @TableField(exist = false)
  private static final long serialVersionUID = 1L;
}