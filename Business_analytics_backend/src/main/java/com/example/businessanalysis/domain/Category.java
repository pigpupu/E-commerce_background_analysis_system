package com.example.businessanalysis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 类别表
 *
 * @TableName t_category
 */
@TableName(value = "t_category")
@Data
public class Category implements Serializable {

  @TableField(exist = false)
  private static final long serialVersionUID = 1L;
  /**
   * 类别id
   */
  @TableId(type = IdType.AUTO)
  private Integer id;
  /**
   * 类别名称
   */
  private String categoryName;
  /**
   * 父类别id
   */
  private Integer parentId;
  /**
   * 排序顺序
   */
  private Integer sortOrder;
  /**
   * 是否为父类别
   */
  private Integer isParent;
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