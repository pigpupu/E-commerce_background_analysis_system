package com.example.businessanalysis.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.stream.CollectorUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.businessanalysis.common.exception.BusinessException;
import com.example.businessanalysis.domain.Good;
import com.example.businessanalysis.domain.Stock;
import com.example.businessanalysis.mapper.GoodMapper;
import com.example.businessanalysis.mapper.StockMapper;
import com.example.businessanalysis.service.CategoryService;
import com.example.businessanalysis.service.GoodService;
import com.example.businessanalysis.service.StockService;
import com.example.businessanalysis.utils.ChartVoUtils;
import com.example.businessanalysis.vo.echart.ChartVo;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author 74707
 * @description 针对表【t_stock(库存表)】的数据库操作Service实现
 * @createDate 2022-10-26 12:01:20
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock>
    implements StockService {

  @Resource
  public StockMapper stockMapper;

  @Resource
  public CategoryService categoryService;

  @Resource
  public GoodService goodService;

  @Resource
  public GoodMapper goodMapper;

  public List<String> findAllFitGoodId(Integer categoryID,String inputName,Integer precise){

    List<String> AllFitGoodID =new ArrayList<>();
    if(StringUtils.isBlank(inputName)){
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

      if(CollectionUtil.isEmpty(ALlFitCategoryId)) throw new BusinessException("种类不存在");

      AllFitGoodID = goodService.findAllGoodIdByCatId(ALlFitCategoryId);
      if(CollectionUtil.isEmpty(AllFitGoodID)) throw new BusinessException("所选分类下暂无具体商品");
    }else{
      List<Good> raw= new ArrayList<>();
      if(precise==0){
        raw= goodMapper.selectList(Wrappers.<Good>lambdaQuery().like(Good::getGoodName,inputName));
      }else{
        raw= goodMapper.selectList(Wrappers.<Good>lambdaQuery().eq(Good::getGoodName,inputName));
      }

      if(CollectionUtil.isEmpty(raw)) throw new BusinessException("目前没有符合条件的库存记录可供显示");
      AllFitGoodID=raw.stream().map(u->u.getId()).collect(Collectors.toList());
    }
    return AllFitGoodID;
  }


  @Override
  public ChartVo<Integer> getStockVolumeChartVo(Integer categoryID, Date startTime,
      Date endTime, Integer showSize,String inputName,Integer precise) {

    List<String> AllFitGoodID=findAllFitGoodId(categoryID,inputName,precise);
    if(AllFitGoodID==null || AllFitGoodID.size()==0) return null;

    LambdaQueryWrapper<Stock> lqw = Wrappers.<Stock>lambdaQuery()
        .in(Stock::getGoodId, AllFitGoodID).
        between(Stock::getAddTime, startTime, endTime);

    List<Stock> list = new ArrayList<>();

    switch (showSize) {
      case 1:
        list = stockMapper.selectList(lqw.groupBy(Stock::getAddTimeDay)
            .select(Stock::getAddTimeDay, Stock::getPeriodVolumeTotal));
        break;
      case 2:
        list = stockMapper.selectList(lqw.groupBy(Stock::getAddTimeWeek)
            .select(Stock::getAddTimeWeek, Stock::getPeriodVolumeTotal));
        break;
      case 3:
        list = stockMapper.selectList(lqw.groupBy(Stock::getAddTimeMonth)
            .select(Stock::getAddTimeMonth, Stock::getPeriodVolumeTotal));
        break;
      case 4:
        list = stockMapper.selectList(lqw.groupBy(Stock::getAddTimeYear)
            .select(Stock::getAddTimeYear, Stock::getPeriodVolumeTotal));
        break;
    }
    if(CollectionUtil.isEmpty(list)) throw new BusinessException("目前没有符合条件的库存记录可供显示");
    return ChartVoUtils.getChartVolumeVo(list, showSize);
  }

  @Override
  public IPage<Map<String, Object>> getStockVolumePage(Integer nowPage, Integer pageSize,
      Integer categoryID, Date startTime, Date endTime, String inputName,Integer precise) {

    System.out.println("-------------------------------------");

    List<String> AllFitGoodID=findAllFitGoodId(categoryID,inputName,precise);
    if(AllFitGoodID==null || AllFitGoodID.size()==0) return null;

    System.out.println("-------------------------------------");

    IPage<Map<String, Object>> re = stockMapper.selectMapsPage(new Page<>(nowPage, pageSize),
        Wrappers.<Stock>lambdaQuery().in(Stock::getGoodId, AllFitGoodID)
            .between(Stock::getAddTime, startTime, endTime)
            .groupBy(Stock::getAddTimeDay)
            .select(Stock::getAddTimeDay, Stock::getPeriodVolumeTotal));
    System.out.println("************------------************"+re.getRecords());
    if(Objects.isNull(re) || CollectionUtil.isEmpty(re.getRecords()))
      throw new BusinessException("目前没有符合条件的库存记录可供显示");
    return re;
  }


}




