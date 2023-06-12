package com.example.businessanalysis.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.businessanalysis.domain.Stock;
import com.example.businessanalysis.vo.echart.ChartVo;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 74707
 * @description 针对表【t_stock(库存表)】的数据库操作Service
 * @createDate 2022-10-26 12:01:20
 */
public interface StockService extends IService<Stock> {


  List<String> findAllFitGoodId(Integer categoryID,String inputName,Integer precise);

  /**
   * 统计一段时间按天/周/月/年 内category_id商品入库基本图表数据（volume)
   *
   * @param categoryID
   * @param startTime
   * @param endTime
   * @param showSize
   * @return ChartVo<Integer> 包装的图表数据
   */
  ChartVo<Integer> getStockVolumeChartVo(Integer categoryID, Date startTime,
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
  public IPage<Map<String, Object>> getStockVolumePage(Integer nowPage, Integer pageSize,
      Integer categoryID, Date startTime, Date endTime, String inputName,Integer precise);


}
