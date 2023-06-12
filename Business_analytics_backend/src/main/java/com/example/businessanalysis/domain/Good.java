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
 * 商品表
 *
 * @TableName t_good
 */
@TableName(value = "t_good")
@Data
public class Good implements Serializable {

  @TableField(exist = false)
  private static final long serialVersionUID = 1L;
  /**
   * 商品id
   */
  @TableId(value ="id")
  private String id;
  /**
   * 商品名称
   */
  private String goodName;
  /**
   * 类别id
   */
  private Integer categoryId;

  /**
   * 类别名称
   */
  @TableField(exist = false)
  private String categoryName;
  /**
   * 品牌id
   */
  private Integer brandId;
  /**
   * 品牌名称
   */
  @TableField(exist = false)
  private String brandName;
  /**
   * 商品当前价格
   */
  private BigDecimal price;
  /**
   * 商品重量(g)
   */
  private BigDecimal productWeightG;

  /**
   * 商品长度(cm)
   */
  private BigDecimal productLengthCm;

  /**
   * 商品高度(cm)
   */
  private BigDecimal productHeightCm;

  /**
   * 商品宽度(cm)
   */
  private BigDecimal productWidthCm;

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
  /**
   *
   */
  private String image;
}