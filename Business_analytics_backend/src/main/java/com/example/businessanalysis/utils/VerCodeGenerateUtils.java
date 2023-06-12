package com.example.businessanalysis.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @ClassName VerCodeGenerateUtils
 * @Description 生成验证码
 * @Author 74707
 * @Date 2022/11/8 8:35
 * @Version V1.0
 */
public class VerCodeGenerateUtils {

  /*
   * 验证码包含的字段，可自己设置
   */
  private static final String SYMBOLS = "0123456789ABCDEFGHIGKLMNOPQRSTUVWXYZ";
  private static final Random RANDOM = new SecureRandom();

  /**
   * 生成 6 位数的随机数字
   *
   * @return String 验证码
   */
  public static String generateVerCode() {
    char[] numbers = new char[6];
    for (int i = 0; i < numbers.length; i++) {
      numbers[i] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
    }
    return new String(numbers);
  }
}
