package com.example.businessanalysis.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Statistic
 * @Description 特别注意，本类为自定义类，不是数据库实体类 statistic，所有需要按时间统计的类都继承于此，如GoodOrder统计销量 Instock统计入库量....
 * @Author 74707
 * @Date 2022/10/30 14:02
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statistic {


  @TableField(value = "DATE_FORMAT(add_time,'%Y-%m-%d')")
  protected String addTimeDay;

  @TableField(value = "DATE_FORMAT(add_time,'%x-%v')")
  protected String addTimeWeek;

  @TableField(value = "DATE_FORMAT(add_time,'%Y-%m')")
  protected String addTimeMonth;

  @TableField(value = "DATE_FORMAT(add_time,'%Y')")
  protected String addTimeYear;

  @TableField(select = false, value = "sum(total_price)")
  protected BigDecimal periodMoneyTotal;

  @TableField(select = false, value = "sum(num)")
  protected Integer periodVolumeTotal;
}
