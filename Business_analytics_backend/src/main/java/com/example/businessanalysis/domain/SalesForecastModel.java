package com.example.businessanalysis.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName : SalesForecastModel
 * @Description : 销量预测模型
 * @Author : 桓
 * @Date: 2023/2/8  17:24
 */
@TableName(value = "t_sales_forecast_model")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesForecastModel implements Serializable {
  @TableField(exist = false)
  private static final long serialVersion = 1L;
  @TableId()
  private String id;
  private double k;
  private double b;
  /**
   *  用来判断是否过期
   */
  private Integer delFlag;
}
