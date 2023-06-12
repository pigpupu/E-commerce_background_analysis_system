package com.example.businessanalysis.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.businessanalysis.common.WrapMapper;
import com.example.businessanalysis.common.Wrapper;
import com.example.businessanalysis.domain.Category;
import com.example.businessanalysis.mapper.CategoryMapper;
import com.example.businessanalysis.pojo.normalUserPojo.SaleQueryPojo;
import com.example.businessanalysis.service.GoodOrderService;
import com.example.businessanalysis.vo.echart.ChartVo;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SaleController
 * @Description 销售量展示界面： 幻想调用链：/queryAllCategory--》/basicMoneyChart--》/basicMoneyPage
 * -->/Top10GoodVo-----通过商品名称精确匹配---->/basicMoneyChart（展现单个商品的销售额情况，默认近30日）
 *
 * @Author ljq
 * @Date 2022/10/27 13:03
 * @Version V1.0
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/sale")

public class SaleController {

  @Resource
  private GoodOrderService goodOrderService;

  @Resource
  public CategoryMapper categoryMapper;

  private List<Date> getMyStartDate(SaleQueryPojo saleQueryPojo){
    System.out.println(saleQueryPojo);
    List<Date> res =new ArrayList<>();
    if(saleQueryPojo.getStartTimeStr() == null) {
      saleQueryPojo.setStartTimeStr(saleQueryPojo.getDefaultStart());
      saleQueryPojo.setEndTimeStr(saleQueryPojo.getDefaultEnd());
    }
    Date startTimeDate = Date.valueOf(saleQueryPojo.getStartTimeStr());
    Date endTimeDate = Date.valueOf(saleQueryPojo.getEndTimeStr());
    res.add(startTimeDate);
    res.add(endTimeDate);
    return res;
  }


  /**
   * 得到所有种类键值对
   *
   * @return
   */
  @RequestMapping(value = "/queryAllCategory")
  public List<Map<String, String>> queryAllCategory() {
    LambdaQueryWrapper<Category> lqw = Wrappers.<Category>lambdaQuery().
        select(Category::getCategoryName, Category::getId).orderByAsc(Category::getId);
    List<Map<String, Object>> re=
        categoryMapper.selectMaps(lqw);
    List<Map<String,String>> res = new ArrayList<>();
    for(Map<String,Object> m:re){
      Map<String,String> m2=new HashMap<>();
      String keystr="";
      Integer valuestr=0;
      int i=0;
      for(String s:m.keySet()){
        if(i==0){
          keystr=m.get(s).toString();
          m2.put("label",keystr);
        }
        else{
          m2.put("value",m.get(s).toString());
        }
        i++;
      }
      res.add(m2);
    }
    return res;
  }

  /**
   * @Description 销售量变化(基本柱折饼 ， 可天周月年 ）
   */
  @RequestMapping("/basicVolumeChart")
  public Wrapper<ChartVo> getPeriodSaleVolumeVo( @RequestBody SaleQueryPojo saleQueryPojo) {
    if(saleQueryPojo.getCategoryID()==null) saleQueryPojo.setCategoryID(1);
    List<Date> dateList = getMyStartDate(saleQueryPojo);
    Date startTimeDate = dateList.get(0);
    Date endTimeDate = dateList.get(1);
    ChartVo<Integer> res = goodOrderService.getSaleVolumeChartVo
        (saleQueryPojo.getCategoryID(), startTimeDate, endTimeDate,
            saleQueryPojo.getShowSize(), saleQueryPojo.getInputName(), saleQueryPojo.getPrecise());
    return WrapMapper.wrapRes(res);
  }


  /**
   * @Description 销售额变化(基本柱折饼 ， 可天周月年 ）
   */
  @RequestMapping("/basicMoneyChart")
  public Wrapper<ChartVo> getPeriodSaleMoneyVo(@RequestBody SaleQueryPojo saleQueryPojo) {
    if(saleQueryPojo.getCategoryID()==null) saleQueryPojo.setCategoryID(1);
    List<Date> dateList = getMyStartDate(saleQueryPojo);
    Date startTimeDate = dateList.get(0);
    Date endTimeDate = dateList.get(1);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, goodOrderService.
        getSaleMoneyChartVo(saleQueryPojo.getCategoryID(), startTimeDate, endTimeDate,
            saleQueryPojo.getShowSize(), saleQueryPojo.getInputName(), saleQueryPojo.getPrecise()));
  }

  /**
   * @Description 销售额表格分页展示 时间（天） 销售额
   */
  @RequestMapping("/basicVolumePage")
  public Wrapper<IPage<Map<String, Object>>> getPeriodSaleVolumePage(@RequestBody SaleQueryPojo saleQueryPojo) {
    if(saleQueryPojo.getCategoryID()==null) saleQueryPojo.setCategoryID(1);
    List<Date> dateList = getMyStartDate(saleQueryPojo);
    Date startTimeDate = dateList.get(0);
    Date endTimeDate = dateList.get(1);

    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, goodOrderService.
        getSaleVolumePage(saleQueryPojo.getNowPage(), saleQueryPojo.getPageSize(),
            saleQueryPojo.getCategoryID(), startTimeDate, endTimeDate, saleQueryPojo.getInputName(),
            saleQueryPojo.getPrecise()));
  }

  /**
   * @Description 销售量表格分页展示 时间（天） 销售额
   */
  @RequestMapping("/basicMoneyPage")
  public Wrapper<IPage<Map<String, Object>>> getPeriodSaleMoneyPage( @RequestBody SaleQueryPojo saleQueryPojo) {
    if(saleQueryPojo.getCategoryID()==null) saleQueryPojo.setCategoryID(1);
    List<Date> dateList = getMyStartDate(saleQueryPojo);
    Date startTimeDate = dateList.get(0);
    Date endTimeDate = dateList.get(1);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, goodOrderService.
        getSaleMoneyPage(saleQueryPojo.getNowPage(), saleQueryPojo.getPageSize(),
            saleQueryPojo.getCategoryID(), startTimeDate, endTimeDate, saleQueryPojo.getInputName(),
            saleQueryPojo.getPrecise()));
  }

  /**
   * @Description 中国地图销售额变化（热点地图，认为本处不需要天周月年而是一段时间聚合即可）
   */
  @RequestMapping("/basicMoneyMapChart")
  public Wrapper<ChartVo> getPeriodSaleMoneyMapVo(@RequestBody SaleQueryPojo saleQueryPojo) {
    if(saleQueryPojo.getCategoryID()==null) saleQueryPojo.setCategoryID(1);
    List<Date> dateList = getMyStartDate(saleQueryPojo);
    Date startTimeDate = dateList.get(0);
    Date endTimeDate = dateList.get(1);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, goodOrderService.
        getSaleMoneyMapVo(saleQueryPojo.getCategoryID(), startTimeDate, endTimeDate,
            saleQueryPojo.getInputName(), saleQueryPojo.getPrecise()));
  }


  /**
   * @Description 中国地图销售额变化（热点地图，认为本处不需要天周月年而是一段时间聚合即可）
   */
  @RequestMapping("/basicVolumeMapChart")
  public Wrapper<ChartVo> getPeriodSaleVolumeMapVo(@RequestBody SaleQueryPojo saleQueryPojo) {
    if(saleQueryPojo.getCategoryID()==null) saleQueryPojo.setCategoryID(1);
    List<Date> dateList = getMyStartDate(saleQueryPojo);
    Date startTimeDate = dateList.get(0);
    Date endTimeDate = dateList.get(1);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, goodOrderService.
        getSaleVolumeMapVo(saleQueryPojo.getCategoryID(), startTimeDate, endTimeDate,
            saleQueryPojo.getInputName(), saleQueryPojo.getPrecise()));
  }

  /**
   * @Description 客户的零售批发销售量对比（customer volume）
   */
  @RequestMapping("/basicCustomerVolumeChart")
  public Wrapper<ChartVo> getPeriodCustomerVolumeVo(@RequestBody SaleQueryPojo saleQueryPojo) {
    if(saleQueryPojo.getCategoryID()==null) saleQueryPojo.setCategoryID(1);
    List<Date> dateList = getMyStartDate(saleQueryPojo);
    Date startTimeDate = dateList.get(0);
    Date endTimeDate = dateList.get(1);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, goodOrderService.
        getCustomerCompareVolumeChartVo(saleQueryPojo.getCategoryID(), startTimeDate, endTimeDate,
            saleQueryPojo.getInputName(),
            saleQueryPojo.getPrecise()));
  }

  /**
   * @Description 客户的零售批发销售额对比（customer money）
   */
  @RequestMapping("/basicCustomerMoneyChart")
  public Wrapper<ChartVo> getPeriodCustomerMoneyVo(@RequestBody SaleQueryPojo saleQueryPojo) {
    if(saleQueryPojo.getCategoryID()==null) saleQueryPojo.setCategoryID(1);
    List<Date> dateList = getMyStartDate(saleQueryPojo);
    Date startTimeDate = dateList.get(0);
    Date endTimeDate = dateList.get(1);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, goodOrderService.
        getCustomerCompareMoneyChartVo(saleQueryPojo.getCategoryID(), startTimeDate, endTimeDate,
            saleQueryPojo.getInputName(), saleQueryPojo.getPrecise()));
  }

  /**
   * @Description TOP 10 Sale GoodVo
   */
  @RequestMapping("/Top10MoneyGoodVo")
  public Wrapper<List<Map<String, Object>>> getTop10MoneyGoodVo(@RequestBody SaleQueryPojo saleQueryPojo) {
    if(saleQueryPojo.getCategoryID()==null) saleQueryPojo.setCategoryID(1);
    List<Date> dateList = getMyStartDate(saleQueryPojo);
    Date startTimeDate = dateList.get(0);
    Date endTimeDate = dateList.get(1);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, goodOrderService.
        getTopSaleMoneyGoodVo(saleQueryPojo.getCategoryID(), startTimeDate, endTimeDate,
            saleQueryPojo.getInputName(), saleQueryPojo.getPrecise()));
  }

  /**
   * @Description TOP 10 Sale GoodVo
   */
  @RequestMapping("/Top10VolumeGoodVo")
  public Wrapper<List<Map<String, Object>>> getTop10VolumeGoodVo(@RequestBody SaleQueryPojo saleQueryPojo) {
    if(saleQueryPojo.getCategoryID()==null) saleQueryPojo.setCategoryID(1);
    List<Date> dateList = getMyStartDate(saleQueryPojo);
    Date startTimeDate = dateList.get(0);
    Date endTimeDate = dateList.get(1);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, goodOrderService.
        getTopSaleVolumeGoodVo(saleQueryPojo.getCategoryID(), startTimeDate, endTimeDate,
            saleQueryPojo.getInputName(), saleQueryPojo.getPrecise()));
  }
  /**
   * @Description 获取商品详细信息默认近似匹配第一个
   * @deprecated
   */
  @RequestMapping("/getDetailGoodVoByName")
  public Wrapper<Map<String, Object>> getdetailGoodVo(@RequestBody SaleQueryPojo saleQueryPojo) {
    if(saleQueryPojo.getCategoryID()==null) saleQueryPojo.setCategoryID(1);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, goodOrderService.
        getDetailGoodVoByName(saleQueryPojo));
  }

  @RequestMapping("/getDetailGoodVoById")
  public Wrapper<Map<String, Object>> getdetailGoodVoById(@RequestBody SaleQueryPojo saleQueryPojo) {
    if(saleQueryPojo.getCategoryID()==null) saleQueryPojo.setCategoryID(1);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, goodOrderService.
        getDetailGoodVoById(saleQueryPojo));
  }
}
