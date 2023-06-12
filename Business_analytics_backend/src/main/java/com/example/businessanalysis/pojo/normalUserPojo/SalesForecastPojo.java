package com.example.businessanalysis.pojo.normalUserPojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName : SalesForecastPojo
 * @Description : 销量预测实体类
 * @Author : 桓
 * @Date: 2023/2/8  17:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesForecastPojo implements Serializable {
  private static final long serialVersionUID = 1L;
  //商品编号
  private String goodId;
  //开始日期
  private String beginDate;
  //结束日期
  private String endDate;
}
