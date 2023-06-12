package com.example.businessanalysis.pojo.normalUserPojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName VipPojo
 * @Description 升级高级用户的订单pojo
 * @Author 74707
 * @Date 2022/11/10 20:04
 * @Version V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class VipPojo {

  /**
   * 商户订单号，必填
   */
  private String out_trade_no;
  /**
   * 订单名称，必填
   */
  private String subject;
  /**
   * 付款金额，必填 根据支付宝接口协议，必须使用下划线
   */
  private String total_amount;

  /**
   * 对应的客户账号
   */
  private String account;

  /**
   * 超时时间参数
   */
  private String timeout_express = "10m";
  /**
   * 产品编号 当面付
   */
  private String product_code = "FAST_INSTANT_TRADE_PAY";

}

