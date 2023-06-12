package com.example.businessanalysis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单评价表
 *
 * @TableName t_order_comment
 */
@TableName(value = "t_order_comment")
@Data
public class OrderComment implements Serializable {

  @TableField(exist = false)
  private static final long serialVersionUID = 1L;
  /**
   * id
   */
  @TableId(type = IdType.AUTO)
  private Integer id;
  /**
   * 订单id
   */
  private Integer orderId;
  /**
   * 客户id
   */
  private Integer customerId;
  /**
   * 评价等级
   */
  private Integer star;
  /**
   * 评价详情
   */
  private String detail;
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