package com.example.businessanalysis.service;

import com.alipay.api.AlipayApiException;
import com.example.businessanalysis.pojo.normalUserPojo.VipPojo;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * @ClassName AliPayService
 * @Description 支付宝沙箱流程
 * @Author ljq
 * @Date 2022/11/10 20:42
 * @Version V1.0
 */
@Service
public interface AliPayService {

  /**
   * 用户进行支付
   *
   * @param vipPojo
   * @return String 成功交易跳转支付宝官方页面的URL
   * @throws AlipayApiException
   */
   String aliPay(VipPojo vipPojo) throws AlipayApiException;

  /**
   * 查询订单支付状态
   *
   * @param out_trade_no 用户订单交易号
   * @return Map 订单重要信息的返回
   */
   Map queryPayStatus(String out_trade_no);

  /**
   * 成功支付后修改客户等级
   *
   * @param out_trade_no 用户订单交易号
   * @param account      用户账号
   * @return Integer 成功code
   */
   Integer getPayStatusCode(String out_trade_no, String account);
}
