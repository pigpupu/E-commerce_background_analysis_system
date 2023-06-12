package com.example.businessanalysis.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.businessanalysis.domain.Instock;
import com.example.businessanalysis.vo.echart.ChartVo;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;

/**
 * @author 74707
 * @description 针对表【t_instock(入库表)】的数据库操作Service
 * @createDate 2022-10-26 12:01:20
 */
public interface InstockService extends IService<Instock> {


  /**
   * 统计一段时间按天/周/月/年内category_id=x的商品入库基本图表数据（money）
   *
   * @param categoryID
   * @param startTime
   * @param endTime
   * @return ChartVo<Integer> 包装的图表数据
   */
  ChartVo<BigDecimal> getInstockMoneyChartVo(Integer categoryID, Date startTime,
      Date endTime, Integer showSize,String inputName,Integer precise);

  /**
   * 统计一段时间按天/周/月/年 内category_id=x的商品入库基本图表数据（volume)
   *
   * @param categoryID
   * @param startTime
   * @param endTime
   * @return ChartVo<Integer> 包装的图表数据
   */
  ChartVo<Integer> getInstockVolumeChartVo(Integer categoryID, Date startTime,
      Date endTime, Integer showSize,String inputName,Integer precise);

  /**
   *
   * @param nowPage
   * @param pageSize
   * @param categoryID
   * @param startTime
   * @param endTime
   * @param inputName
   * @return
   */
  IPage<Map<String, Object>> getInstockVolumePage(Integer nowPage, Integer pageSize,
      Integer categoryID,
      Date startTime, Date endTime, String inputName,Integer precise);

  /**
   *
   * @param nowPage
   * @param pageSize
   * @param categoryID
   * @param startTime
   * @param endTime
   * @param inputName
   * @return
   */
  IPage<Map<String, Object>> getInstockMoneyPage(Integer nowPage, Integer pageSize,
      Integer categoryID,
      Date startTime, Date endTime, String inputName,Integer precise);

}
