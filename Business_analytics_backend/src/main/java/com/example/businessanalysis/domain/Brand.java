package com.example.businessanalysis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 品牌表
 *
 * @TableName t_brand
 */
@TableName(value = "t_brand")
@Data
public class Brand implements Serializable {

  @TableField(exist = false)
  private static final long serialVersionUID = 1L;
  /**
   * 品牌id
   */
  @TableId(type = IdType.AUTO)
  private Integer id;
  /**
   * 品牌名称
   */
  private String brandName;
  /**
   *
   */
  private Date addTime;
  /**
   *
   */
  private Date updateTime;
  /**
   * 逻辑删除标志
   */
  private Integer delFlag;
}