package com.example.businessanalysis.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.example.businessanalysis.pojo.normalUserPojo.LoginUser;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName : MyJwtUtil
 * @Description : 用于生成jwt的工具类
 * @Author : 桓
 * @Date: 2022/11/22  10:54
 */
public class MyJwtUtil {
  public static String createToken(String account){
    DateTime now = DateTime.now();
    DateTime newTime = now.offsetNew(DateField.MINUTE, 30);

    Map<String,Object> payload = new HashMap<String,Object>();
    //签发时间
    payload.put(JWTPayload.ISSUED_AT, now);
    //过期时间
    payload.put(JWTPayload.EXPIRES_AT, newTime);
    //生效时间
    payload.put(JWTPayload.NOT_BEFORE, now);
    //载荷
    payload.put("userName",account);
    String key = "huan";
    String token = JWTUtil.createToken(payload, key.getBytes());
    return token;
  }


  public static String createTokenLongtime(String account){
    DateTime now = DateTime.now();
    //设为七天之后过期
    DateTime newTime = now.offsetNew(DateField.DAY_OF_MONTH, 7);

    Map<String,Object> payload = new HashMap<String,Object>();
    //签发时间
    payload.put(JWTPayload.ISSUED_AT, now);
    //过期时间
    payload.put(JWTPayload.EXPIRES_AT, newTime);
    //生效时间
    payload.put(JWTPayload.NOT_BEFORE, now);
    //载荷
    payload.put("userName",account);
    String key = "huan";
    String token = JWTUtil.createToken(payload, key.getBytes());
    return token;
  }
}
