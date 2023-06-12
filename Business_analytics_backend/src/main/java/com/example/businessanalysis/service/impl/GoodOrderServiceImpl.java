package com.example.businessanalysis.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.businessanalysis.common.exception.BusinessException;
import com.example.businessanalysis.domain.Good;
import com.example.businessanalysis.domain.GoodOrder;
import com.example.businessanalysis.mapper.GoodMapper;
import com.example.businessanalysis.mapper.GoodOrderMapper;
import com.example.businessanalysis.pojo.normalUserPojo.SaleQueryPojo;
import com.example.businessanalysis.service.CategoryService;
import com.example.businessanalysis.service.GoodOrderService;
import com.example.businessanalysis.service.GoodService;
import com.example.businessanalysis.utils.ChartVoUtils;
import com.example.businessanalysis.vo.echart.ChartVo;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * @author 74707
 * @description 针对表【t_good_order(商品销售订单表)】的数据库操作Service实现
 * @createDate 2022-10-26 12:01:20
 */
@Service
public class GoodOrderServiceImpl extends ServiceImpl<GoodOrderMapper, GoodOrder>
    implements GoodOrderService {

  @Resource
  public GoodMapper goodMapper;

  @Resource
  public GoodOrderMapper goodOrderMapper;

  @Resource
  public CategoryService categoryService;

  @Resource
  public GoodService goodService;

  private List<String> findOrderIdByCatId(Integer categoryID, Date startTime, Date endTime) {
    //List<Integer> ALlFitCategoryId = categoryService.findAllSubCatIdsByCatId(categoryID);

    List<Integer> ALlFitCategoryId=new ArrayList<>();
    List<Integer> temp=new ArrayList<>();
    for(int i=1;i<=75;i++)
      temp.add(i);

    if(categoryID==75) {
      ALlFitCategoryId=temp;
    }else{
      ALlFitCategoryId.add(categoryID);
    }

    if (CollectionUtil.isEmpty(ALlFitCategoryId)) {
      throw new BusinessException("种类不存在");
    }
    List<String> AllFitGoodId = goodService.findAllGoodIdByCatId(ALlFitCategoryId);
    if (CollectionUtil.isEmpty(AllFitGoodId)) {
      throw new BusinessException("所选分类下暂无具体商品");
    }
    List<GoodOrder> AllFitOrderList = goodOrderMapper.selectList(Wrappers.<GoodOrder>lambdaQuery()
        .in(GoodOrder::getGoodId, AllFitGoodId).between(GoodOrder::getAddTime, startTime, endTime));
    if (CollectionUtil.isEmpty(AllFitOrderList)) {
      throw new BusinessException("此类商品在所选时间范围内无销售记录");
    }
    List<String> AllFitOrderId = AllFitOrderList.stream()
        .map(GoodOrder::getId).collect(Collectors.toList());
    return AllFitOrderId;
  }

  private List<String> findALlFitOrderId(Integer categoryID, Date startTime, Date endTime,
      String inputName, Integer precise) {
    List<String> AllFitOrderId;
    //分为两种：若商品名称不为空，选择like查询
    //若为空，按种类查询,调用findAllOrderIdByCatId
    if (StringUtils.isBlank(inputName)) {
      AllFitOrderId = findOrderIdByCatId(categoryID, startTime, endTime);
      if (CollectionUtil.isEmpty(AllFitOrderId)) {
        throw new BusinessException("所选时间范围内，没有符合输入的商品的销售记录");
      }
    } else {
      List<String> AllFitGoodId = new ArrayList<>();
      if (precise == 0) {
        AllFitGoodId = Optional.ofNullable(goodMapper.selectList(
                Wrappers.<Good>lambdaQuery().like(Good::getGoodName, inputName)))
            .map(u -> u.stream().map(Good::getId).collect(Collectors.toList())).orElse(null);
      } else {
        AllFitGoodId = Optional.ofNullable(goodMapper.selectList(
                Wrappers.<Good>lambdaQuery().eq(Good::getGoodName, inputName)))
            .map(u -> u.stream().map(Good::getId).collect(Collectors.toList())).orElse(null);
      }
      if (CollectionUtil.isEmpty(AllFitGoodId)) {
        throw new BusinessException("没有符合输入的商品名");
      }
      List<GoodOrder> AllFitOrderList = goodOrderMapper.selectList(
          Wrappers.<GoodOrder>lambdaQuery().in(
              GoodOrder::getGoodId,
              AllFitGoodId).between(GoodOrder::getAddTime, startTime, endTime));
      if (CollectionUtil.isEmpty(AllFitOrderList)) {
        throw new BusinessException("所选时间范围内，没有符合输入的商品的销售记录");
      }
      AllFitOrderId = AllFitOrderList.stream().map(GoodOrder::getId).collect(Collectors.toList());
    }
    return AllFitOrderId;
  }


  @Override
  public ChartVo<BigDecimal> getSaleMoneyMapVo(Integer categoryID, Date startTime, Date endTime,
      String inputName, Integer precise) {
    List<String> AllFitOrderId = findALlFitOrderId(categoryID, startTime, endTime,
        inputName, precise);
    if (AllFitOrderId == null || AllFitOrderId.size() == 0) {
      return null;
    }
    QueryWrapper<GoodOrder> qw = new QueryWrapper<>();
    qw.in("a.id", AllFitOrderId);
    List<Map<String, Object>> re = goodOrderMapper.selectGoodOrderSaleMoneyMap(qw);
    return ChartVoUtils.getChartMoneyMapVo(re);
  }

  @Override
  public ChartVo<Integer> getSaleVolumeMapVo(Integer categoryID, Date startTime, Date endTime,
      String inputName, Integer precise) {
    List<String> AllFitOrderId = findALlFitOrderId(categoryID, startTime, endTime,
        inputName, precise);
    if (AllFitOrderId == null || AllFitOrderId.size() == 0) {
      return null;
    }
    QueryWrapper<GoodOrder> qw = new QueryWrapper<>();
    qw.in("a.id", AllFitOrderId);
    List<Map<String, Object>> re = goodOrderMapper.selectGoodOrderSaleVolumeMap(qw);
    return ChartVoUtils.getChartVolumeMapVo(re);
  }

  @Override
  public ChartVo<Integer> getCustomerCompareVolumeChartVo(Integer categoryID, Date startTime,
      Date endTime,
      String inputName, Integer precise) {
    List<String> AllFitOrderId = findALlFitOrderId(categoryID, startTime, endTime,
        inputName, precise);
    if (AllFitOrderId == null || AllFitOrderId.size() == 0) {
      return null;
    }
    QueryWrapper<GoodOrder> qw = new QueryWrapper<>();
    qw.in("a.id", AllFitOrderId);
    List<Map<String, Object>> re = goodOrderMapper.selectGoodOrderCustomerCompareVolume(qw);
//        System.out.println("测试List<Map<String,Object>>:"+re.get(0));
//        System.out.println("测试List<Map<String,Object>>:"+re.get(0).keySet());
//        System.out.println("测试List<Map<String,Object>>:"+re.get(0).values());
//        System.out.println("测试List<Map<String,Object>>:"+re.get(0).get("customer_type"));
//        System.out.println("测试List<Map<String,Object>>:"+re.get(0).get("periodTotal"));
    return ChartVoUtils.getChartCustomerCompareVolumeVo(re);
  }

  @Override
  public ChartVo<BigDecimal> getCustomerCompareMoneyChartVo(Integer categoryID, Date startTime,
      Date endTime,
      String inputName, Integer precise) {
    List<String> AllFitOrderId = findALlFitOrderId(categoryID, startTime, endTime,
        inputName, precise);
    if (AllFitOrderId == null || AllFitOrderId.size() == 0) {
      return null;
    }
    QueryWrapper<GoodOrder> qw = new QueryWrapper<>();
    qw.in("a.id", AllFitOrderId);
    List<Map<String, Object>> re = goodOrderMapper.selectGoodOrderCustomerCompareMoney(qw);
    return ChartVoUtils.getChartCustomerCompareMoneyVo(re);
  }

  @Override
  public IPage<Map<String, Object>> getSaleMoneyPage(Integer nowPage, Integer pageSize,
      Integer categoryID,
      Date startTime, Date endTime, String inputName, Integer precise) {
    List<String> AllFitOrderId = findALlFitOrderId(categoryID, startTime, endTime,
        inputName, precise);
    if (AllFitOrderId == null || AllFitOrderId.size() == 0) {
      return null;
    }
    IPage<Map<String, Object>> re = goodOrderMapper.selectMapsPage(new Page<>(nowPage, pageSize),
        Wrappers.<GoodOrder>lambdaQuery().in(GoodOrder::getId, AllFitOrderId)
            .groupBy(GoodOrder::getAddTimeDay)
            .select(GoodOrder::getAddTimeDay, GoodOrder::getPeriodMoneyTotal));
//        System.out.println("测试List<Map<String,Object>>:"+re.getRecords().get(0));
//        System.out.println("测试List<Map<String,Object>>:"+re.getRecords().get(0).keySet());
//        System.out.println("测试List<Map<String,Object>>:"+re.getRecords().get(0).values());
    return re;
  }

  @Override
  public IPage<Map<String, Object>> getSaleVolumePage(Integer nowPage, Integer pageSize,
      Integer categoryID,
      Date startTime, Date endTime, String inputName, Integer precise) {
    List<String> AllFitOrderId = findALlFitOrderId(categoryID, startTime, endTime,
        inputName, precise);
    if (AllFitOrderId == null || AllFitOrderId.size() == 0) {
      return null;
    }
    IPage<Map<String, Object>> re = goodOrderMapper.selectMapsPage(new Page<>(nowPage, pageSize),
        Wrappers.<GoodOrder>lambdaQuery().in(GoodOrder::getId, AllFitOrderId)
            .groupBy(GoodOrder::getAddTimeDay)
            .select(GoodOrder::getAddTimeDay, GoodOrder::getPeriodVolumeTotal));
    return re;
  }

  @Override
  public ChartVo<BigDecimal> getSaleMoneyChartVo(Integer categoryID, Date startTime, Date endTime,
      Integer showSize,
      String inputName, Integer precise) {

    List<String> AllFitOrderId = findALlFitOrderId(categoryID, startTime, endTime,
        inputName, precise);
    if (AllFitOrderId == null || AllFitOrderId.size() == 0) {
      return null;
    }
    LambdaQueryWrapper<GoodOrder> lqw = Wrappers.<GoodOrder>lambdaQuery().in(
        GoodOrder::getId, AllFitOrderId);
    List<GoodOrder> list = new ArrayList<>();
    switch (showSize) {
      case 1:
        list = goodOrderMapper.selectList(lqw.groupBy(GoodOrder::getAddTimeDay)
            .select(GoodOrder::getAddTimeDay, GoodOrder::getPeriodMoneyTotal));
        break;
      case 2:
        list = goodOrderMapper.selectList(lqw.groupBy(GoodOrder::getAddTimeWeek)
            .select(GoodOrder::getAddTimeWeek, GoodOrder::getPeriodMoneyTotal));
        break;
      case 3:
        list = goodOrderMapper.selectList(lqw.groupBy(GoodOrder::getAddTimeMonth)
            .select(GoodOrder::getAddTimeMonth, GoodOrder::getPeriodMoneyTotal));
        break;
      case 4:
        list = goodOrderMapper.selectList(lqw.groupBy(GoodOrder::getAddTimeYear)
            .select(GoodOrder::getAddTimeYear, GoodOrder::getPeriodMoneyTotal));
        break;
    }
    return ChartVoUtils.getChartMoneyVo(list, showSize);
  }

  @Override
  public ChartVo<Integer> getSaleVolumeChartVo(Integer categoryID, Date startTime, Date endTime,
      Integer showSize,
      String inputName, Integer precise) {

    System.out.println("+++++++++++++++---------" + categoryID);
    System.out.println("+++++++++++++++---------" + startTime);
    System.out.println("+++++++++++++++---------" + endTime);
    List<String> AllFitOrderId = findALlFitOrderId(categoryID, startTime, endTime,
        inputName, precise);
    System.out.println("all Fit order id"+AllFitOrderId);
    if (AllFitOrderId == null || AllFitOrderId.size() == 0) {
      return null;
    }
    LambdaQueryWrapper<GoodOrder> lqw = Wrappers.<GoodOrder>lambdaQuery().in(
        GoodOrder::getId, AllFitOrderId);
    List<GoodOrder> list = new ArrayList<>();
    switch (showSize) {
      case 1:
        list = goodOrderMapper.selectList(lqw.groupBy(GoodOrder::getAddTimeDay)
            .select(GoodOrder::getAddTimeDay, GoodOrder::getPeriodVolumeTotal));
        break;
      case 2:
        list = goodOrderMapper.selectList(lqw.groupBy(GoodOrder::getAddTimeWeek)
            .select(GoodOrder::getAddTimeWeek, GoodOrder::getPeriodVolumeTotal));
        break;
      case 3:
        list = goodOrderMapper.selectList(lqw.groupBy(GoodOrder::getAddTimeMonth)
            .select(GoodOrder::getAddTimeMonth, GoodOrder::getPeriodVolumeTotal));
        break;
      case 4:
        list = goodOrderMapper.selectList(lqw.groupBy(GoodOrder::getAddTimeYear)
            .select(GoodOrder::getAddTimeYear, GoodOrder::getPeriodVolumeTotal));
        break;

      default:
        list = goodOrderMapper.selectList(lqw.groupBy(GoodOrder::getAddTimeDay)
            .select(GoodOrder::getAddTimeDay, GoodOrder::getPeriodVolumeTotal));
        break;
    }
    return ChartVoUtils.getChartVolumeVo(list, showSize);
  }

  @Override
  public List<Map<String, Object>> getTopSaleMoneyGoodVo(Integer categoryID, Date startTime,
      Date endTime, String inputName, Integer precise) {

    List<String> AllFitOrderId = findALlFitOrderId(categoryID, startTime, endTime,
        inputName, precise);
    if (AllFitOrderId == null || AllFitOrderId.size() == 0) {
      return null;
    }
    LambdaQueryWrapper<GoodOrder> lqw = Wrappers.<GoodOrder>lambdaQuery()
        .in(GoodOrder::getId, AllFitOrderId)
        .groupBy(GoodOrder::getGoodId).
        select(GoodOrder::getGoodId, GoodOrder::getPeriodVolumeTotal,
            GoodOrder::getPeriodMoneyTotal)
        .orderByDesc(GoodOrder::getPeriodMoneyTotal).last("limit 10");

    List<Map<String, Object>> re1 = goodOrderMapper.selectMaps(lqw);
    List<GoodOrder> re11 = goodOrderMapper.selectList(lqw);
    List<String> AllFitGoodId = re11.stream().map(GoodOrder::getGoodId)
        .collect(Collectors.toList());

    LambdaQueryWrapper<Good> lqw2 = Wrappers.<Good>lambdaQuery().in(Good::getId, AllFitGoodId).
        select(Good::getId, Good::getGoodName, Good::getImage, Good::getPrice);
    List<Map<String, Object>> re2 = goodMapper.selectMaps(lqw2);

    List<Map<String, Object>> res = re1.stream().map(m -> {
      re2.stream().filter(m2 -> Objects.equals(m.get("good_id"), m2.get("id"))).forEach(m2 -> {
        m.put("good_name", m2.get("good_name"));
        m.put("image", m2.get("image"));
        m.put("price", m2.get("price"));
      });
      return m;
    }).collect(Collectors.toList());
    System.out.println(res);
    return res;
  }

  @Override
  public Map<String, Object> getDetailGoodVoByName(SaleQueryPojo saleQueryPojo) {
    String goodId;
    String goodName = saleQueryPojo.getGoodName();
    if (StringUtils.isNotBlank(goodName)) {
      //如果当前输入有多个默认返回第一个
      Good good = goodMapper.selectOne(
          Wrappers.<Good>lambdaQuery().like(Good::getGoodName, goodName).
              eq(Good::getDelFlag,0).orderByAsc(Good::getId).last("limit 1"));
      if (good == null) {
        throw new BusinessException("数据库中暂时没有名为"+goodName+"的商品");
      }
      goodId = good.getId();
    } else {
      throw new BusinessException("请不要输入空参数");
    }
    //默认返回最近一段时间的的数据
    LocalDate today = LocalDate.now();
    LocalDate past = today.minusYears(1);
    Date startTime = Date.valueOf(past);
    Date endTime = Date.valueOf(today);
    if(StringUtils.isNotBlank(saleQueryPojo.getStartTimeStr())){
      startTime = Date.valueOf(saleQueryPojo.getStartTimeStr());
    }
    if(StringUtils.isNotBlank(saleQueryPojo.getEndTimeStr())){
      endTime = Date.valueOf(saleQueryPojo.getEndTimeStr());
    }

    Map<String, Object> re1;
    Map<String, Object> re2;
    LambdaQueryWrapper<GoodOrder> lqw = Wrappers.<GoodOrder>lambdaQuery()
        .eq(GoodOrder::getGoodId, goodId)
        .between(GoodOrder::getAddTime, startTime, endTime)
        .groupBy(GoodOrder::getGoodId).
        select(GoodOrder::getGoodId, GoodOrder::getPeriodVolumeTotal,
            GoodOrder::getPeriodMoneyTotal);
    List<Map<String, Object>> temp1 = goodOrderMapper.selectMaps(lqw);
    if (CollectionUtil.isNotEmpty(temp1)) {
      re1 = temp1.get(0);
    } else {
      throw new BusinessException("所选时间范围内，没有符合输入的商品的销售记录");
    }
    LambdaQueryWrapper<Good> lqw2 = Wrappers.<Good>lambdaQuery().eq(Good::getId, goodId).
     eq(Good::getDelFlag,0).select(Good::getId, Good::getGoodName, Good::getImage, Good::getPrice);
    List<Map<String, Object>> temp2 = goodMapper.selectMaps(lqw2);
    if (CollectionUtil.isNotEmpty(temp2)) {
      re2 = temp2.get(0);
    } else {
      throw new BusinessException("所选时间范围内，没有符合输入的商品的销售记录");
    }
    Map<String, Object> res = new HashMap<String, Object>();
    res.putAll(re1);
    res.putAll(re2);
    return res;
  }

  @Override
  public List<Map<String, Object>> getTopSaleVolumeGoodVo(Integer categoryID, Date startTime,
      Date endTime, String inputName, Integer precise) {
    List<String> AllFitOrderId = findALlFitOrderId(categoryID, startTime, endTime,
        inputName, precise);
    if (AllFitOrderId == null || AllFitOrderId.size() == 0) {
      return null;
    }
    LambdaQueryWrapper<GoodOrder> lqw = Wrappers.<GoodOrder>lambdaQuery()
        .in(GoodOrder::getId, AllFitOrderId)
        .groupBy(GoodOrder::getGoodId).
        select(GoodOrder::getGoodId, GoodOrder::getPeriodVolumeTotal,
            GoodOrder::getPeriodVolumeTotal)
        .orderByDesc(GoodOrder::getPeriodVolumeTotal).last("limit 10");

    List<Map<String, Object>> re1 = goodOrderMapper.selectMaps(lqw);
    List<GoodOrder> re11 = goodOrderMapper.selectList(lqw);
    List<String> AllFitGoodId = re11.stream().map(GoodOrder::getGoodId)
        .collect(Collectors.toList());

    LambdaQueryWrapper<Good> lqw2 = Wrappers.<Good>lambdaQuery().in(Good::getId, AllFitGoodId).
        select(Good::getId, Good::getGoodName, Good::getImage, Good::getPrice);
    List<Map<String, Object>> re2 = goodMapper.selectMaps(lqw2);

    List<Map<String, Object>> res = re1.stream().map(m -> {
      re2.stream().filter(m2 -> Objects.equals(m.get("good_id"), m2.get("id"))).forEach(m2 -> {
        m.put("good_name", m2.get("good_name"));
        m.put("image", m2.get("image"));
        m.put("price", m2.get("price"));
      });
      return m;
    }).collect(Collectors.toList());
    System.out.println(res);
    return res;
  }

  @Override
  public Map<String, Object> getDetailGoodVoById(SaleQueryPojo saleQueryPojo) {
    String Id=saleQueryPojo.getGoodId();
    LocalDate today = LocalDate.now();
    LocalDate past = today.minusYears(1);
    Date startTime = Date.valueOf(past);
    Date endTime = Date.valueOf(today);
    if(StringUtils.isNotBlank(saleQueryPojo.getStartTimeStr())){
      startTime = Date.valueOf(saleQueryPojo.getStartTimeStr());
    }
    if(StringUtils.isNotBlank(saleQueryPojo.getEndTimeStr())){
      endTime = Date.valueOf(saleQueryPojo.getEndTimeStr());
    }

    Map<String, Object> re1;
    Map<String, Object> re2;
    LambdaQueryWrapper<GoodOrder> lqw = Wrappers.<GoodOrder>lambdaQuery()
        .eq(GoodOrder::getGoodId, Id)
        .between(GoodOrder::getAddTime, startTime, endTime)
        .groupBy(GoodOrder::getGoodId).
        select(GoodOrder::getGoodId, GoodOrder::getPeriodVolumeTotal,
            GoodOrder::getPeriodMoneyTotal);
    List<Map<String, Object>> temp1 = goodOrderMapper.selectMaps(lqw);
    if (CollectionUtil.isNotEmpty(temp1)) {
      re1 = temp1.get(0);
    } else {
      throw new BusinessException("所选时间范围内，没有符合输入的商品的销售记录");
    }
    LambdaQueryWrapper<Good> lqw2 = Wrappers.<Good>lambdaQuery().eq(Good::getId, Id).
        select(Good::getId, Good::getGoodName, Good::getImage, Good::getPrice);
    List<Map<String, Object>> temp2 = goodMapper.selectMaps(lqw2);
    if (CollectionUtil.isNotEmpty(temp1)) {
      re2 = temp2.get(0);
    } else {
      throw new BusinessException("所选时间范围内，没有符合输入的商品的销售记录");
    }
    Map<String, Object> res = new HashMap<String, Object>();
    res.putAll(re1);
    res.putAll(re2);
    return res;
  }
}




