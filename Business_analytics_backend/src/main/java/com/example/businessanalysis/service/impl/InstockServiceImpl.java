package com.example.businessanalysis.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.businessanalysis.common.exception.BusinessException;
import com.example.businessanalysis.domain.Instock;
import com.example.businessanalysis.mapper.InstockMapper;
import com.example.businessanalysis.service.InstockService;
import com.example.businessanalysis.service.StockService;
import com.example.businessanalysis.utils.ChartVoUtils;
import com.example.businessanalysis.vo.echart.ChartVo;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author 74707
 * @description 针对表【t_instock(入库表)】的数据库操作Service实现
 * @createDate 2022-10-26 12:01:20
 */
@Service
public class InstockServiceImpl extends ServiceImpl<InstockMapper, Instock>
    implements InstockService {

  @Resource
  public InstockMapper instockMapper;

  @Resource
  public StockService stockService;


  @Override
  public ChartVo<BigDecimal> getInstockMoneyChartVo(Integer categoryID, Date startTime,
      Date endTime, Integer showSize,String inputName,Integer precise) {
    List<String> AllFitGoodID=stockService.findAllFitGoodId(categoryID,inputName,precise);
    if(AllFitGoodID==null || AllFitGoodID.size()==0) return null;

    LambdaQueryWrapper<Instock> lqw = Wrappers.<Instock>lambdaQuery()
        .in(Instock::getGoodId, AllFitGoodID).
        between(Instock::getAddTime, startTime, endTime);

    List<Instock> list = new ArrayList<>();

    switch (showSize) {
      case 1:
        list = instockMapper.selectList(lqw.groupBy(Instock::getAddTimeDay)
            .select(Instock::getAddTimeDay, Instock::getPeriodMoneyTotal));
        break;
      case 2:
        list = instockMapper.selectList(lqw.groupBy(Instock::getAddTimeWeek)
            .select(Instock::getAddTimeWeek, Instock::getPeriodMoneyTotal));
        break;
      case 3:
        list = instockMapper.selectList(lqw.groupBy(Instock::getAddTimeMonth)
            .select(Instock::getAddTimeMonth, Instock::getPeriodMoneyTotal));
        break;
      case 4:
        list = instockMapper.selectList(lqw.groupBy(Instock::getAddTimeYear)
            .select(Instock::getAddTimeYear, Instock::getPeriodMoneyTotal));
        break;
    }
    if(CollectionUtil.isEmpty(list)) throw new BusinessException("暂时没有符合条件的入库金额记录");
    return ChartVoUtils.getChartMoneyVo(list, showSize);
  }

  @Override
  public ChartVo<Integer> getInstockVolumeChartVo(Integer categoryID, Date startTime,
      Date endTime, Integer showSize,String inputName,Integer precise) {

    List<String> AllFitGoodID=stockService.findAllFitGoodId(categoryID,inputName,precise);
    if(AllFitGoodID==null || AllFitGoodID.size()==0) return null;

    LambdaQueryWrapper<Instock> lqw = Wrappers.<Instock>lambdaQuery()
        .in(Instock::getGoodId, AllFitGoodID).
        between(Instock::getAddTime, startTime, endTime);

    List<Instock> list = new ArrayList<>();

    switch (showSize) {
      case 1:
        list = instockMapper.selectList(lqw.groupBy(Instock::getAddTimeDay)
            .select(Instock::getAddTimeDay, Instock::getPeriodVolumeTotal));
        break;
      case 2:
        list = instockMapper.selectList(lqw.groupBy(Instock::getAddTimeWeek)
            .select(Instock::getAddTimeWeek, Instock::getPeriodVolumeTotal));
        break;
      case 3:
        list = instockMapper.selectList(lqw.groupBy(Instock::getAddTimeMonth)
            .select(Instock::getAddTimeMonth, Instock::getPeriodVolumeTotal));
        break;
      case 4:
        list = instockMapper.selectList(lqw.groupBy(Instock::getAddTimeYear)
            .select(Instock::getAddTimeYear, Instock::getPeriodVolumeTotal));
        break;
    }
    if(CollectionUtil.isEmpty(list)) throw new BusinessException("暂时没有符合条件的入库数目记录");
    return ChartVoUtils.getChartVolumeVo(list, showSize);
  }

  @Override
  public IPage<Map<String, Object>> getInstockMoneyPage(Integer nowPage, Integer pageSize,
      Integer categoryID, Date startTime, Date endTime, String inputName,Integer precise) {

    List<String> AllFitGoodID=stockService.findAllFitGoodId(categoryID,inputName,precise);
    if(AllFitGoodID==null || AllFitGoodID.size()==0) return null;

    IPage<Map<String, Object>> re = instockMapper.selectMapsPage(new Page<>(nowPage, pageSize),
        Wrappers.<Instock>lambdaQuery().in(Instock::getGoodId, AllFitGoodID)
            .between(Instock::getAddTime, startTime, endTime)
            .groupBy(Instock::getAddTimeDay)
            .select(Instock::getAddTimeDay, Instock::getPeriodMoneyTotal));
    if(Objects.isNull(re) || CollectionUtil.isEmpty(re.getRecords()))
      throw new BusinessException("暂时没有符合条件的入库金额记录");
    return re;
  }


  @Override
  public IPage<Map<String, Object>> getInstockVolumePage(Integer nowPage, Integer pageSize,
      Integer categoryID, Date startTime, Date endTime, String inputName,Integer precise) {
    List<String> AllFitGoodID=stockService.findAllFitGoodId(categoryID,inputName,precise);
    if(AllFitGoodID==null || AllFitGoodID.size()==0) return null;
    IPage<Map<String, Object>> re = instockMapper.selectMapsPage(new Page<>(nowPage, pageSize),
        Wrappers.<Instock>lambdaQuery().in(Instock::getGoodId, AllFitGoodID)
            .between(Instock::getAddTime, startTime, endTime)
            .groupBy(Instock::getAddTimeDay)
            .select(Instock::getAddTimeDay, Instock::getPeriodVolumeTotal));
    if(Objects.isNull(re) || CollectionUtil.isEmpty(re.getRecords()))
      throw new BusinessException("暂时没有符合条件的入库数目记录");
    return re;
  }

}




