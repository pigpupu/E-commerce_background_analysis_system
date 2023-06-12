package com.example.businessanalysis.controller.system;

import cn.hutool.core.util.IdUtil;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.businessanalysis.common.WrapMapper;
import com.example.businessanalysis.common.Wrapper;
import com.example.businessanalysis.domain.User;
import com.example.businessanalysis.mapper.UserMapper;
import com.example.businessanalysis.pojo.normalUserPojo.UserLoginPojo;
import com.example.businessanalysis.pojo.normalUserPojo.VipPojo;
import com.example.businessanalysis.service.AliPayService;
import com.example.businessanalysis.service.UserService;
import com.example.businessanalysis.utils.VerCodeGenerateUtils;
import com.example.businessanalysis.vo.UserVo;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName : UserController
 * @Description : 用户行为类
 * @Author : 桓 ljq
 * @Date: 2022/11/1  20:21
 */
@CrossOrigin
@RestController
public class UserController {

  @Resource
  public UserService userService;

  @Resource
  public UserMapper userMapper;

  @Resource
  public AliPayService aliPayService;

  /**
   * 使用用户账户名和用户密码登录
   *
   * @param user
   * @return Wrapper<Integer>
   */
  @PostMapping("/login")
  public Wrapper<String> userLogin(@RequestBody UserLoginPojo user) {
    return userService.login(user);
  }

  /**
   * 注册界面响应
   *
   * @param user
   * @return Wrapper<Integer>
   */
  @PostMapping(value = "/register")
  public Wrapper<String> register(@RequestBody UserLoginPojo user) {
    return userService.register(user);
  }

  /**
   * 忘记密码，获取验证码5min失效
   * @param user
   * @return
   */
  @RequestMapping("/forgetPassword")
  public Wrapper<Boolean> forgetPassword(@RequestBody UserLoginPojo user) {
    Boolean res=userService.forgetPassword(user);
    return WrapMapper.wrap(res);
  }

  /**
   * 将验证码一起提交
   * 重置密码为用户输入的密码--user.newPassword
   * @param user
   * @return
   */
  @RequestMapping("/resetPassword")
  public Wrapper<Boolean> resetPassword(@RequestBody UserLoginPojo user) {
    Boolean res=userService.resetPassword(user);
    return WrapMapper.wrap(res);
  }

  /**
   * 修改密码界面响应
   *
   * @param user
   * @return Wrapper<Integer>
   */
  @RequestMapping("/alterPassword")
  public Wrapper<Integer> alterPassword(@RequestBody UserLoginPojo user) {
    Boolean res = userService.alterPassword(user);
    if(res) return WrapMapper.wrap(Wrapper.SUCCESS_CODE, "修改成功",200);
    else return WrapMapper.wrap(Wrapper.ERROR_CODE, "系统数据错误",500);
  }


  /**
   * 更新头像界面响应
   *
   * @param file
   * @param account
   * @return Wrapper<Integer>
   */
  @RequestMapping(value = "/updateImage")
  public Wrapper<Integer> updateImage(MultipartFile file, String account) {
    Boolean res;
    try {
      res = userService.updateImage(file, account);
    } catch (Exception e) {
      res = false;
    }
    if (res == true) {
      return WrapMapper.wrap(Wrapper.SUCCESS_CODE, "上传成功", 200);
    } else {
      return WrapMapper.wrap(Wrapper.ERROR_CODE, "上传失败", 500);
    }
  }

  /**
   * 更新用户文本信息界面响应
   *
   * @param user
   * @return Wrapper<Integer>
   */
  @RequestMapping(value = "/updateCenter")
  public Wrapper<Integer> updateCenter(User user) {
    Boolean res = userService.updateUser(user);
    if (res == true) {
      return WrapMapper.wrap(Wrapper.SUCCESS_CODE, "修改成功", 200);
    } else {
      return WrapMapper.wrap(Wrapper.ERROR_CODE, "修改失败", 500);
    }
  }

  /**
   * 更新用户文本信息界面响应
   *
   * @param Url
   * @param account
   * @return Wrapper<Integer>
   */
  @RequestMapping(value = "/updateImageForZzs")
  public Wrapper<Integer> updateImageForZzs(String Url,String account) {
    User user = new User();
    user.setImage(Url);
    Integer res = userMapper.update(user, Wrappers.<User>lambdaUpdate().eq(User::getAccount, account));
    return WrapMapper.wrapRes(res);
  }

  /**
   * 获取用户信息
   *
   * @param account
   * @return Wrapper<UserInfoPojo>
   */
  @RequestMapping(value = "/getOwnInfo")
  public Wrapper<UserVo> getUserInfo(String account) {
    UserVo userVo = userService.queryByAccount(account).get(0);
    if (userVo != null) {
      return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, userVo);
    } else {
      return WrapMapper.wrap(Wrapper.ERROR_CODE, Wrapper.ERROR_MESSAGE, null);
    }
  }

  /**
   * 成为vip界面响应
   *
   * @param vipPojo
   * @return Wrapper<String>
   * @throws AlipayApiException
   */
  @PostMapping(value = "/getVipTrade")
  public Wrapper<Map<String,String>> getVipRole(VipPojo vipPojo) throws AlipayApiException {
    Map<String ,String> res =new HashMap<>();
    System.out.println(vipPojo);
    vipPojo.setOut_trade_no(IdUtil.fastUUID());
    vipPojo.setTotal_amount("0.05");
    vipPojo.setSubject("VIP升级");
    System.out.println(vipPojo);
    res.put("out_trade_no",vipPojo.getOut_trade_no());
    res.put("jumpUrl",aliPayService.aliPay(vipPojo));
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, "订单号和跳转界面", res);
  }

  /**
   * 查询订单交易状态
   *
   * @param out_trade_no
   * @param account
   * @return Wrapper<Integer>
   * @throws AlipayApiException
   */
  @RequestMapping("/queryTradeResult")
  public Wrapper<Integer> queryResult(String out_trade_no, String account) {
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, "查询结果",
        aliPayService.getPayStatusCode(out_trade_no, account));
  }
}
