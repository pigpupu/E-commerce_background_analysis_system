package com.example.businessanalysis.utils;

import com.example.businessanalysis.domain.Statistic;
import com.example.businessanalysis.vo.echart.ChartVo;
import com.example.businessanalysis.vo.echart.PieMapVo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName ChartVoUti
 * @Author ljq
 * @Date 2022/10/30 13:29
 * @Version V1.0
 * @Description ChartVo数据包装汇总工具类 目前的基础的柱状图/折线图/饼图/地图热力图 的数据都汇总在这里的工具类
 */
public class ChartVoUtils {

  /**
   * 包装库存额和销售额
   *
   * @param list 原始数据
   * @return ChartVo<BigDecimal>
   */
  public static ChartVo<BigDecimal> getChartMoneyVo(List<? extends Statistic> list,
      Integer showSize) {
    List<String> names = new ArrayList<>();
    List<BigDecimal> values = new ArrayList<>();
    List<PieMapVo<BigDecimal>> pieMapVoList = new ArrayList<>();
    switch (showSize) {
      case 1:
        names = list.stream().map(item -> item.getAddTimeDay()).
            collect(Collectors.toList());

        values = list.stream().map(item -> item.getPeriodMoneyTotal()).collect(Collectors.toList());

        pieMapVoList = list.stream().map(item -> new PieMapVo<BigDecimal>(item.getAddTimeDay(),
            item.getPeriodMoneyTotal())).collect(Collectors.toList());
        break;
      case 2:
        names = list.stream().map(item -> item.getAddTimeWeek()).
            collect(Collectors.toList());

        values = list.stream().map(item -> item.getPeriodMoneyTotal()).collect(Collectors.toList());

        pieMapVoList = list.stream().map(item -> new PieMapVo<BigDecimal>(item.getAddTimeWeek(),
            item.getPeriodMoneyTotal())).collect(Collectors.toList());
        break;
      case 3:
        names = list.stream().map(item -> item.getAddTimeMonth()).
            collect(Collectors.toList());

        values = list.stream().map(item -> item.getPeriodMoneyTotal()).collect(Collectors.toList());

        pieMapVoList = list.stream().map(item -> new PieMapVo<BigDecimal>(item.getAddTimeMonth(),
            item.getPeriodMoneyTotal())).collect(Collectors.toList());
        break;
      case 4:
        names = list.stream().map(item -> item.getAddTimeYear()).
            collect(Collectors.toList());

        values = list.stream().map(item -> item.getPeriodMoneyTotal()).collect(Collectors.toList());

        pieMapVoList = list.stream().map(item -> new PieMapVo<BigDecimal>(item.getAddTimeYear(),
            item.getPeriodMoneyTotal())).collect(Collectors.toList());
        break;
    }
    return new ChartVo(names, values, pieMapVoList);
  }

  /**
   * 包装库存额和销售额地图
   *
   * @param list 原始数据
   * @return ChartVo<BigDecimal>
   */
  public static ChartVo<BigDecimal> getChartMoneyMapVo(List<Map<String, Object>> list) {

    List<String> names = new ArrayList<>();
    List<BigDecimal> values = new ArrayList<>();
    List<PieMapVo<BigDecimal>> pieMapVoList = new ArrayList<>();

    names = list.stream().map(item -> (String) item.get("region_name"))
        .collect(Collectors.toList());
    values = list.stream().map(item -> (BigDecimal) item.get("periodTotal"))
        .collect(Collectors.toList());

    pieMapVoList = list.stream()
        .map(item -> new PieMapVo<BigDecimal>((String) item.get("region_name"),
            (BigDecimal) item.get("periodTotal")))
        .collect(Collectors.toList());

    return new ChartVo(names, values, pieMapVoList);
  }

  /**
   * 包装库存量和销售量
   *
   * @param list 原始数据
   * @return ChartVo<Integer>
   */
  public static ChartVo<Integer> getChartVolumeVo(List<? extends Statistic> list,
      Integer showSize) {
    List<String> names = new ArrayList<>();
    List<Integer> values = new ArrayList<>();
    List<PieMapVo<Integer>> pieMapVoList = new ArrayList<>();
    switch (showSize) {
      case 1:
        names = list.stream().map(item -> item.getAddTimeDay()).
            collect(Collectors.toList());

        values = list.stream().map(item -> item.getPeriodVolumeTotal())
            .collect(Collectors.toList());

        pieMapVoList = list.stream().map(item -> new PieMapVo<Integer>(item.getAddTimeDay(),
            item.getPeriodVolumeTotal())).collect(Collectors.toList());
        break;
      case 2:
        names = list.stream().map(item -> item.getAddTimeWeek()).
            collect(Collectors.toList());

        values = list.stream().map(item -> item.getPeriodVolumeTotal())
            .collect(Collectors.toList());

        pieMapVoList = list.stream().map(item -> new PieMapVo<Integer>(item.getAddTimeWeek(),
            item.getPeriodVolumeTotal())).collect(Collectors.toList());
        break;
      case 3:
        names = list.stream().map(item -> item.getAddTimeMonth()).
            collect(Collectors.toList());

        values = list.stream().map(item -> item.getPeriodVolumeTotal())
            .collect(Collectors.toList());

        pieMapVoList = list.stream().map(item -> new PieMapVo<Integer>(item.getAddTimeMonth(),
            item.getPeriodVolumeTotal())).collect(Collectors.toList());
        break;
      case 4:
        names = list.stream().map(item -> item.getAddTimeYear()).
            collect(Collectors.toList());

        values = list.stream().map(item -> item.getPeriodVolumeTotal())
            .collect(Collectors.toList());

        pieMapVoList = list.stream().map(item -> new PieMapVo<Integer>(item.getAddTimeYear(),
            item.getPeriodVolumeTotal())).collect(Collectors.toList());
        break;
    }
    return new ChartVo(names, values, pieMapVoList);
  }

  /**
   * 包装库存量和销售量地图
   *
   * @param list 原始数据
   * @return ChartVo<Integer>
   */
  public static ChartVo<Integer> getChartVolumeMapVo(List<Map<String, Object>> list) {

    List<String> names = list.stream().map(item -> (String) item.get("region_name"))
        .collect(Collectors.toList());
    List<Integer> values = list.stream()
        .map(item -> Integer.parseInt(String.valueOf(item.get("periodTotal"))))
        .collect(Collectors.toList());

    List<PieMapVo<Integer>> pieMapVoList = list.stream()
        .map(item -> new PieMapVo<Integer>((String) item.get("region_name"),
            Integer.parseInt(String.valueOf(item.get("periodTotal")))))
        .collect(Collectors.toList());
    return new ChartVo(names, values, pieMapVoList);
  }

  /**
   * 包装客户量度比较
   *
   * @param list 原始数据
   * @return ChartVo<Integer>
   */
  public static ChartVo<Integer> getChartCustomerCompareVolumeVo(List<Map<String, Object>> list) {

    List<String> names = list.stream().map(item -> item.get("customer_type").toString())
        .collect(Collectors.toList());
    List<Integer> values = list.stream()
        .map(item -> Integer.parseInt(String.valueOf(item.get("periodTotal"))))
        .collect(Collectors.toList());

    List<PieMapVo<Integer>> pieMapVoList = list.stream()
        .map(item -> new PieMapVo<Integer>(item.get("customer_type").toString(),
            Integer.parseInt(String.valueOf(item.get("periodTotal")))))
        .collect(Collectors.toList());
    return new ChartVo(names, values, pieMapVoList);
  }

  /**
   * 包装客户额度比较
   *
   * @param list 原始数据
   * @return ChartVo<BigDecimal>
   */
  public static ChartVo<BigDecimal> getChartCustomerCompareMoneyVo(List<Map<String, Object>> list) {

    List<String> names = list.stream().map(item -> item.get("customer_type").toString())
        .collect(Collectors.toList());
    List<BigDecimal> values = list.stream().map(item -> (BigDecimal) item.get("periodTotal"))
        .collect(Collectors.toList());

    List<PieMapVo<BigDecimal>> pieMapVoList = list.stream()
        .map(item -> new PieMapVo<BigDecimal>(item.get("customer_type").toString(),
            (BigDecimal) item.get("periodTotal")))
        .collect(Collectors.toList());
    return new ChartVo(names, values, pieMapVoList);
  }
}
