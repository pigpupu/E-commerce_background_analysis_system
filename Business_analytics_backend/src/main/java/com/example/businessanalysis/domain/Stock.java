package com.example.businessanalysis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 库存表
 *
 * @TableName t_stock
 */
@TableName(value = "t_stock")
@Data
public class Stock extends Statistic implements Serializable {

  @TableField(exist = false)
  private static final long serialVersionUID = 1L;
  /**
   * id
   */
  @TableId(type = IdType.AUTO)
  private Integer id;
  /**
   * 商品id
   */
  private String goodId;

  /**
   * 本日的数量（有正负）
   */
  private Integer num;
  /**
   * 总的库存量（是加上该单据发生后的即时库存余额）
   */
  private Integer stockNum;
  /**
   * 添加日期
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