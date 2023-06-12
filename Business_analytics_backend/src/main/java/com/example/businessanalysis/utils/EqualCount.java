package com.example.businessanalysis.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.Date;
import org.springframework.stereotype.Component;

/**
 * @ClassName : EqualCount
 * @Description : 用于表查询的工具类
 * @Author : 桓
 * @Date: 2022/11/2  11:06
 */
@Component
public class EqualCount<T extends BaseMapper> {

  /**
   * 获取tableObj 的 colName 等于 value的总和
   *
   * @param tableObj 表对象
   * @param colName  列名
   * @param value    值
   * @return int count
   * @author 桓
   * @date 9:43 2022/11/3
   */
  public int getColCount(T tableObj, String colName, Object value) {
    QueryWrapper<T> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(colName, value);
    int res = tableObj.selectCount(queryWrapper);
    return res;
  }

  /**
   * @param tableObj  表格对象
   * @param colName   列名
   * @param value     值
   * @param colDate   时间列
   * @param startTime 开始时间
   * @param endTime   结束时间
   * @return int 总和
   * @author 桓
   * @date 9:51 2022/11/3
   */
  public int getInTimeCount(T tableObj, String colName, Object value, String colDate,
      Date startTime,
      Date endTime) {
    QueryWrapper<T> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(colName, value).between(colDate, startTime, endTime);
    int res = tableObj.selectCount(queryWrapper);
    return res;
  }
}
