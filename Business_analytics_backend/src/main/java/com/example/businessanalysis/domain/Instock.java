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
 * 入库表
 * @TableName t_instock
 */
@TableName(value ="t_instock")
@Data
public class Instock extends Statistic implements Serializable {
  /**
   * 入库id
   */
  @TableId(type = IdType.AUTO)
  private Integer id;

  /**
   * 商品id
   */
  private String goodId;

  /**
   * 入库单价
   */
  private BigDecimal price;

  /**
   * 库存单位
   */
  private String unit;

  /**
   * 入库数量
   */
  private Integer num;

  /**
   * 入库总金额
   */
  private BigDecimal totalPrice;

  /**
   * 入库日期
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