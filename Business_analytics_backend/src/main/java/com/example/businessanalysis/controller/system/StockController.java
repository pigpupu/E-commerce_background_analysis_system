package com.example.businessanalysis.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.businessanalysis.common.WrapMapper;
import com.example.businessanalysis.common.Wrapper;
import com.example.businessanalysis.pojo.normalUserPojo.StockQueryPojo;
import com.example.businessanalysis.service.InstockService;
import com.example.businessanalysis.service.StockService;
import com.example.businessanalysis.vo.echart.ChartVo;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName StockController
 * @Description 库存量展示
 * @Author 74707
 * @Date 2022/10/30 16:21
 * @Version V1.0
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/stock")
public class StockController {

  @Resource
  private StockService stockService;

  @Resource
  private InstockService instockService;

  private List<Date> getMyStartDate(StockQueryPojo stockQueryPojo){
    System.out.println(stockQueryPojo);
    List<Date> res =new ArrayList<>();
    if(stockQueryPojo.getStartTimeStr() == null) {
      stockQueryPojo.setStartTimeStr(stockQueryPojo.getDefaultStart());
      stockQueryPojo.setEndTimeStr(stockQueryPojo.getDefaultEnd());
    }
    System.out.println(stockQueryPojo.getDefaultStart());
    System.out.println(stockQueryPojo.getDefaultEnd());

    Date startTimeDate = Date.valueOf(stockQueryPojo.getStartTimeStr());
    Date endTimeDate = Date.valueOf(stockQueryPojo.getEndTimeStr());
    res.add(startTimeDate);
    res.add(endTimeDate);
    return res;
  }
  /**
   * @Description 总库存数变化
   */
  @RequestMapping("/stockVolumeChart")
  public Wrapper<ChartVo> getPeriodStockVolumeVo(@RequestBody StockQueryPojo stockQueryPojo) {
    if(stockQueryPojo.getCategoryID()==null) stockQueryPojo.setCategoryID(1);
    List<Date> dateList = getMyStartDate(stockQueryPojo);
    Date startTimeDate = dateList.get(0);
    Date endTimeDate = dateList.get(1);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, stockService.
        getStockVolumeChartVo(stockQueryPojo.getCategoryID(), startTimeDate, endTimeDate,
            stockQueryPojo.getShowSize(), stockQueryPojo.getInputName(),
            stockQueryPojo.getPrecise()));
  }


  /**
   * @Description 入库数变化
   */
  @RequestMapping("/instockVolumeChart")
  public Wrapper<ChartVo> getPeriodInstockVolumeVo(@RequestBody StockQueryPojo stockQueryPojo) {
    if(stockQueryPojo.getCategoryID()==null) stockQueryPojo.setCategoryID(1);
    List<Date> dateList = getMyStartDate(stockQueryPojo);
    Date startTimeDate = dateList.get(0);
    Date endTimeDate = dateList.get(1);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, instockService.
        getInstockVolumeChartVo(stockQueryPojo.getCategoryID(), startTimeDate, endTimeDate,
            stockQueryPojo.getShowSize(), stockQueryPojo.getInputName(),
            stockQueryPojo.getPrecise()));
  }

  /**
   * @Description 入库额变化 入库额=入库数量*入库单价
   */
  @RequestMapping("/instockMoneyChart")
  public Wrapper<ChartVo> getPeriodInstockMoneyVo(@RequestBody StockQueryPojo stockQueryPojo) {
    if(stockQueryPojo.getCategoryID()==null) stockQueryPojo.setCategoryID(1);
    List<Date> dateList = getMyStartDate(stockQueryPojo);
    Date startTimeDate = dateList.get(0);
    Date endTimeDate = dateList.get(1);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, instockService.
        getInstockMoneyChartVo(stockQueryPojo.getCategoryID(), startTimeDate, endTimeDate,
            stockQueryPojo.getShowSize(), stockQueryPojo.getInputName(),
            stockQueryPojo.getPrecise()));
  }


  /**
   * @Description 入库数量表格分页展示 时间（天）
   */
  @RequestMapping("/stockVolumePage")
  public Wrapper<IPage<Map<String, Object>>> getStockVolumePage(@RequestBody StockQueryPojo stockQueryPojo) {
    if(stockQueryPojo.getCategoryID()==null) stockQueryPojo.setCategoryID(1);
    List<Date> dateList = getMyStartDate(stockQueryPojo);
    Date startTimeDate = dateList.get(0);
    Date endTimeDate = dateList.get(1);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, stockService.
        getStockVolumePage(stockQueryPojo.getNowPage(), stockQueryPojo.getPageSize(),
            stockQueryPojo.getCategoryID(), startTimeDate, endTimeDate,
            stockQueryPojo.getInputName(), stockQueryPojo.getPrecise()));
  }


  /**
   * @Description 入库数量表格分页展示 时间（天）
   */
  @RequestMapping("/instockVolumePage")
  public Wrapper<IPage<Map<String, Object>>> getInstockVolumePage(@RequestBody StockQueryPojo stockQueryPojo) {
    if(stockQueryPojo.getCategoryID()==null) stockQueryPojo.setCategoryID(1);
    List<Date> dateList = getMyStartDate(stockQueryPojo);
    Date startTimeDate = dateList.get(0);
    Date endTimeDate = dateList.get(1);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, instockService.
        getInstockVolumePage(stockQueryPojo.getNowPage(), stockQueryPojo.getPageSize(),
            stockQueryPojo.getCategoryID(), startTimeDate, endTimeDate,
            stockQueryPojo.getInputName(), stockQueryPojo.getPrecise()));
  }

  /**
   * @Description 入库额表格分页展示 时间（天）
   */
  @RequestMapping("/instockMoneyPage")
  public Wrapper<IPage<Map<String, Object>>> getInstockMoneyPage(@RequestBody StockQueryPojo stockQueryPojo) {
    if(stockQueryPojo.getCategoryID()==null) stockQueryPojo.setCategoryID(1);
    List<Date> dateList = getMyStartDate(stockQueryPojo);
    Date startTimeDate = dateList.get(0);
    Date endTimeDate = dateList.get(1);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, instockService.
        getInstockMoneyPage(stockQueryPojo.getNowPage(), stockQueryPojo.getPageSize(),
            stockQueryPojo.getCategoryID(), startTimeDate, endTimeDate,
            stockQueryPojo.getInputName(), stockQueryPojo.getPrecise()));
  }

}
