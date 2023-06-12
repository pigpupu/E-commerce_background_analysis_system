package com.example.businessanalysis.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.businessanalysis.domain.GoodOrder;
import com.example.businessanalysis.pojo.normalUserPojo.SaleQueryPojo;
import com.example.businessanalysis.vo.echart.ChartVo;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 74707
 * @description 针对表【t_good_order(商品销售订单表)】的数据库操作Service
 * @createDate 2022-10-26 12:01:20
 */
public interface GoodOrderService extends IService<GoodOrder> {

  /**
   * 通过种类id统计该种类下一段时间内所有的订单id
   *
   * @param categoryID
   * @param startTime
   * @param endTime
   * @return List<Integer> 所有符合条件的订单id
   */
//  List<String> findOrderIdByCatId(Integer categoryID, Date startTime, Date endTime);

  /**
   * 通过种类id或商品名称模糊统计该种类下一段时间内所有的订单id
   *
   * @param categoryID
   * @param startTime
   * @param endTime
   * @return List<Integer> 所有符合条件的订单id
   */
//  List<String> findALlFitOrderId(Integer categoryID, Date startTime, Date endTime,
//      String inputName,Integer precise);

  /**
   * 某种类/某商品名稱的销售量地图（sale volume map)
   *
   * @param startTime
   * @param endTime
   * @param inputName
   * @return ChartVo<Integer> 封装的图表数据
   */
  ChartVo<Integer> getSaleVolumeMapVo(Integer categoryID, Date startTime, Date endTime,
      String inputName,Integer precise);

  /**
   * 某种类/某商品名稱的销售额地图（sale money map）
   *
   * @param startTime
   * @param endTime
   * @param inputName
   * @return ChartVo<Integer> 封装的图表数据
   */
  ChartVo<BigDecimal> getSaleMoneyMapVo(Integer categoryID, Date startTime, Date endTime,
      String inputName,Integer precise);

  /**
   * 某种类/购买某商品名称的客户销售量比较(customer volume)
   *
   * @param categoryID
   * @param startTime
   * @param endTime
   * @param inputName
   * @return ChartVo<Integer> 封装的图表数据
   */
  ChartVo<Integer> getCustomerCompareVolumeChartVo(Integer categoryID, Date startTime,
      Date endTime, String inputName,Integer precise);

  /**
   * 某种类/购买某商品名称的客户销售额比较(customer money)
   *
   * @param categoryID
   * @param startTime
   * @param endTime
   * @param inputName
   * @return ChartVo<BigDecimal> 封装的图表数据
   */
  ChartVo<BigDecimal> getCustomerCompareMoneyChartVo(Integer categoryID, Date startTime,
      Date endTime, String inputName,Integer precise);

  /**
   * 某种类/某商品名称每天的销售额分页展示表格(sale money table)
   *
   * @param nowPage
   * @param pageSize
   * @param categoryID
   * @param startTime
   * @param endTime
   * @param inputName
   * @return IPage<Map < String, Object>> 分页数据
   */
  IPage<Map<String, Object>> getSaleMoneyPage(Integer nowPage, Integer pageSize,
      Integer categoryID,
      Date startTime, Date endTime, String inputName,Integer precise);

  /**
   * 某种类/某商品名称每天的销售量分页展示表格(sale volume table)
   *
   * @param nowPage
   * @param pageSize
   * @param categoryID
   * @param startTime
   * @param endTime
   * @param inputName
   * @return IPage<Map < String, Object>> 分页数据
   */
  IPage<Map<String, Object>> getSaleVolumePage(Integer nowPage, Integer pageSize,
      Integer categoryID,
      Date startTime, Date endTime, String inputName,Integer precise);

  /**
   * 统计一段时间按天/周/月/年内某种类/某商品名称商品销售额基本图表数据（sale money）
   *
   * @param categoryID
   * @param startTime
   * @param endTime
   * @param inputName
   * @return ChartVo<BigDecimal> 封装的图表数据
   */
  ChartVo<BigDecimal> getSaleMoneyChartVo(Integer categoryID, Date startTime, Date endTime,
      Integer showSize,
      String inputName,Integer precise);

  /**
   * 统计一段时间按天/周/月/年 内某种/某商品名称类商品销售量基本图表数据（sale volume)
   *
   * @param categoryID
   * @param startTime
   * @param endTime
   * @param showSize
   * @param inputName
   * @return ChartVo<Integer> 封装的图表数据
   */
  ChartVo<Integer> getSaleVolumeChartVo(Integer categoryID, Date startTime, Date endTime,
      Integer showSize, String inputName,Integer precise);

  /**
   * 销售额/量 基本图表下的展示该种类下销售额top10的商品 若有inputname单独处理--》
   * 如果该关键字能定位到几个商品，就降序显示几个商品
   *
   * @param categoryID
   * @param startTime
   * @param endTime
   * @param inputName
   * @return List<Map < String, Object>>
   */
  List<Map<String, Object>> getTopSaleVolumeGoodVo(Integer categoryID, Date startTime,
      Date endTime, String inputName,Integer precise);

  /**
   * 销售额/量 基本图表下的展示该种类下销售额top10的商品 若有inputname单独处理--》
   * 如果该关键字能定位到几个商品，就降序显示几个商品
   *
   * @param categoryID
   * @param startTime
   * @param endTime
   * @param inputName
   * @return List<Map < String, Object>>
   */
  List<Map<String, Object>> getTopSaleMoneyGoodVo(Integer categoryID, Date startTime,
      Date endTime, String inputName,Integer precise);

  /**
   * 通过精确搜索跳转到商品的详情页后所展示内容 两种方法到达： a.搜素页面直接写inputName--》确定的inputName这里用
   * b.在销售额/库存底下传入inputName，在getTopSaleMoney中已经给会了good_id,good_name,
   * 前端再度传回good_name作为参数
   *
   * @return Map< String, Object >
   */
  Map<String, Object> getDetailGoodVoByName(SaleQueryPojo saleQueryPojo);


  Map<String, Object> getDetailGoodVoById(SaleQueryPojo saleQueryPojo);


}
