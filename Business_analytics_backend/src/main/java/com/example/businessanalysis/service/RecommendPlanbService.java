package com.example.businessanalysis.service;

import com.example.businessanalysis.domain.RecommendPlanb;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;

/**
* @author 74707
* @description 针对表【t_recommend_planb(基于购买的商品推荐表)】的数据库操作Service
* @createDate 2023-02-07 14:13:25
*/
public interface RecommendPlanbService extends IService<RecommendPlanb> {
  Map<String, Object> queryBaseScore(Integer nowPage, Integer pageSize);
}
