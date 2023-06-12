package com.example.businessanalysis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 商品销售订单表
 *
 * @TableName t_good_order
 */
@TableName(value = "t_good_order")
@Data
public class GoodOrder extends Statistic implements Serializable {

  @TableField(exist = false)
  private static final long serialVersionUID = 1L;
  /**
   * 订单id
   */
  @TableId
  private String id;
  /**
   * 客户id
   */
  private String customerId;
  /**
   * 商品id
   */
  private String goodId;
  /**
   * 商品名字
   */
  @TableField(exist = false)
  private String goodName;
  /**
   * 单价
   */
  private BigDecimal price;
  /**
   * 数量
   */
  private Integer num;
  /**
   * 订单总价
   */

  private BigDecimal totalPrice;
  /**
   * 用户是否评论 0否 1是
   */
  private Integer isComment;
  /**
   * 订单支付时间
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