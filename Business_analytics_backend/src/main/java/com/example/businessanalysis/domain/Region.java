package com.example.businessanalysis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 行政区域表
 * @TableName t_region
 */
@TableName(value ="t_region")
@Data
public class Region implements Serializable {
  /**
   *
   */
  @TableId
  private Integer id;

  /**
   *
   */
  private Integer layerId;

  /**
   * 行政区域父ID，例如区县的pid指向市，市的pid指向省，省的pid则是0
   */
  private Integer parentId;

  /**
   * 行政区域名称
   */
  private String regionName;

  /**
   * 行政区域类型，如如1则是省， 如果是2则是市，如果是3则是区县
   */
  private Integer regionType;

  @TableField(exist = false)
  private static final long serialVersionUID = 1L;
}