package com.example.businessanalysis.service.impl;

import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.businessanalysis.config.AliPayConfig;
import com.example.businessanalysis.domain.User;
import com.example.businessanalysis.mapper.UserMapper;
import com.example.businessanalysis.pojo.normalUserPojo.VipPojo;
import com.example.businessanalysis.service.AliPayService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @ClassName AliPayServiceImpl
 * @Description
 * @Author 74707
 * @Date 2022/11/10 20:45
 * @Version V1.0
 */
@Service
public class AliPayServiceImpl implements AliPayService {

  @Resource
  private AliPayConfig alipayConfig;

  @Resource
  private UserMapper userMapper;

  @Override
  public String aliPay(VipPojo vipPojo) throws AlipayApiException {
    // 支付宝网关
    String serverUrl = alipayConfig.getGatewayUrl();
    // APPID
    String appId = alipayConfig.getAppId();
    // 商户私钥, 即PKCS8格式RSA2私钥
    String privateKey = alipayConfig.getPrivateKey();
    // 格式化为 json 格式
    String format = "json";
    // 字符编码格式
    String charset = alipayConfig.getCharset();
    // 支付宝公钥, 即对应APPID下的支付宝公钥
    String alipayPublicKey = alipayConfig.getPublicKey();
    // 签名方式
    String signType = alipayConfig.getSignType();
    // 页面跳转同步通知页面路径
    String returnUrl = alipayConfig.getReturnUrl();
    // 服务器异步通知页面路径
    String notifyUrl = alipayConfig.getNotifyUrl();

    // 1、获得初始化的AlipayClient
    AlipayClient alipayClient = new DefaultAlipayClient(
        serverUrl, appId, privateKey, format, charset, alipayPublicKey, signType);

    // 2、设置请求参数
    AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
    // 页面跳转同步通知页面路径
    alipayRequest.setReturnUrl(returnUrl);
    // 服务器异步通知页面路径
    alipayRequest.setNotifyUrl(notifyUrl);
    // 封装参数(以json格式封装)
    alipayRequest.setBizContent(JSONUtil.toJsonStr(vipPojo));
    // 3、请求支付宝进行付款，并获取支付结果也就是支付页面的URL
    String result = alipayClient.pageExecute(alipayRequest, "get").getBody();
    // 返回付款信息
    return result;
  }

  public Map queryPayStatus(String out_trade_no) {
    System.out.println("需要查询支付状态的订单编号:" + out_trade_no);
    Map map = new HashMap();
    //1、创建交易查询请求对象
    AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
    //2、设置查询参数
    request.setBizContent("{" +
        "    \"out_trade_no\":\"" + out_trade_no + "\"," +//设置电商平台生成订单编号
        "    \"trade_no\":\"\"}");//支付宝生成交易流水号
    // 支付宝网关
    String serverUrl = alipayConfig.getGatewayUrl();
    // APPID
    String appId = alipayConfig.getAppId();
    // 商户私钥, 即PKCS8格式RSA2私钥
    String privateKey = alipayConfig.getPrivateKey();
    // 格式化为 json 格式
    String format = "json";
    // 字符编码格式
    String charset = alipayConfig.getCharset();
    // 支付宝公钥, 即对应APPID下的支付宝公钥
    String alipayPublicKey = alipayConfig.getPublicKey();
    // 签名方式
    String signType = alipayConfig.getSignType();
    // 页面跳转同步通知页面路径
    String returnUrl = alipayConfig.getReturnUrl();
    // 服务器异步通知页面路径
    String notifyUrl = alipayConfig.getNotifyUrl();
    //发出查询
    AlipayClient alipayClient = new DefaultAlipayClient(
        serverUrl, appId, privateKey, format, charset, alipayPublicKey, signType);

    try {
      AlipayTradeQueryResponse response = alipayClient.execute(request);
      //获取响应状态码
      String code = response.getCode();
      System.out.println("交易查询返回结果:" + response.getBody());
      if (code != null && code.equals("10000")) {
        //判断交易状态 交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、
        // TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
        // TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
        //String tradeStatus = response.getTradeStatus();
        //封装交易状态
        map.put("tradestatus", response.getTradeStatus());
        //支付宝生成交易流水号
        map.put("trade_no", response.getTradeNo());
        //订单编号
        map.put("out_trade_no", out_trade_no);
      } else {
        System.out.println("交易查询失败:" + code);
      }
    } catch (AlipayApiException e) {
      e.printStackTrace();
    }
    return map;
  }

  public Integer getPayStatusCode(String out_trade_no, String account) {
    int x = 0;
    while (true) {
      //调用实现类方法
      Map map = queryPayStatus(out_trade_no);
      if (map == null) {
        //查询异常，不在查询
        return 500;
        //跳出循环
      }
      //判断返回状态
      //交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、
      // TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
      // TRADE_SUCCESS（交易支付成功）、
      // TRADE_FINISHED（交易结束，不可退款）
      //判断交易状态为 TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）
      if (map.get("tradestatus") != null && map.get("tradestatus").equals("TRADE_CLOSED")) {
        return 100;
      }
      if (map.get("tradestatus") != null && map.get("tradestatus").equals("TRADE_SUCCESS")) {
        //调用用户，修改用户的id
        User user = new User();
        user.setRoleId(1);
        userMapper.update(user, Wrappers.<User>lambdaUpdate().eq(User::getAccount, account));
        return 200;
      }
      if (map.get("tradestatus") != null && map.get("tradestatus").equals("TRADE_FINISHED")) {
        return 300;
      }
      if (map.get("tradestatus") != null && map.get("tradestatus").equals("TRADE_CLOSED")) {
        return 400;
      }
      x++;
      if (x >= 30) {
        return 600;
      }
      //为了防止一直查询，网关屏蔽掉，可以让查询线程等待3秒
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

}
