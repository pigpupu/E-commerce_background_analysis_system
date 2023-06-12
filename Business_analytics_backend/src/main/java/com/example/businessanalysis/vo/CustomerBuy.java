package com.example.businessanalysis.vo;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @ClassName : CustomerBuy
 * @Description : 客户单品变化中客户购买的商品信息
 * @Author : 桓
 * @Date: 2022/11/3  10:43
 */
@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBuy {

  private String id;
  private String goodName;
  private BigDecimal price;
  private int num;
  private Date buyTime;
}
