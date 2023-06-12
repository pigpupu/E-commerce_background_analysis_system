package com.example.businessanalysis.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @ClassName : StringToDate
 * @Description : 日期转换
 * @Author : 桓
 * @Date: 2022/11/3  9:59
 */

public class MyDateUtils {

  /**
   * 将字符串转化为对应的Date对象
   *
   * @param strDate yyyy-MM-dd格式的字符串日期
   * @return Date 日期
   */
  public static Date stringToDate(String strDate) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    try {
      date = dateFormat.parse(strDate);

    } catch (ParseException e) {
      System.out.println("exception " + dateFormat);
    }
    return date;
  }

  /**
   * Cst格式的日期转换
   *
   * @param Cst      Cst格式的日期字符串
   * @return String
   */
  public static String getNormalDateFromCstString(String Cst) {
    DateFormat formate1 = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat formate2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
    Date datex = new Date();
    try {
      datex = formate2.parse(Cst);
    } catch (Exception e) {
      e.printStackTrace();
    }
    String dateString = formate1.format(datex);
    return dateString;
  }


  /** 比较两个时间相差分钟 */
  public static double calculatetimeGapMinute(Date date1, Date date2) {
    double minute = 0;
    double millisecond = date2.getTime() - date1.getTime();
    minute = millisecond / (60 * 1000);
    return minute;
  }
}

