package com.example.businessanalysis.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.example.businessanalysis.domain.GoodOrder;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 74707
 * @description 针对表【t_good_order(商品销售订单表)】的数据库操作Mapper
 * @createDate 2022-10-26 12:01:20
 * @Entity com.example.businessanalysis.domain.GoodOrder
 */
@Repository
public interface GoodOrderMapper extends BaseMapper<GoodOrder> {

  //面向csdn编程，我也不懂为什么要这几个注解

  @MapKey(value = "id")
  public List<Map<String, Object>> selectGoodOrderSaleMoneyMap(
      @Param(Constants.WRAPPER) Wrapper<GoodOrder> queryWrapper);

  @MapKey(value = "id")
  public List<Map<String, Object>> selectGoodOrderSaleVolumeMap(
      @Param(Constants.WRAPPER) Wrapper<GoodOrder> queryWrapper);

  @MapKey(value = "id")
  public List<Map<String, Object>> selectGoodOrderCustomerCompareVolume(
      @Param(Constants.WRAPPER) Wrapper<GoodOrder> queryWrapper);

  @MapKey(value = "id")
  public List<Map<String, Object>> selectGoodOrderCustomerCompareMoney(
      @Param(Constants.WRAPPER) Wrapper<GoodOrder> queryWrapper);
}




