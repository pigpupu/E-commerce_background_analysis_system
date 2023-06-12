package com.example.businessanalysis.service;

import com.example.businessanalysis.domain.Recommend;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;

/**
* @author 74707
* @description 针对表【t_recommend(基于购买的商品推荐算法)】的数据库操作Service
* @createDate 2023-02-07 10:25:02
*/
public interface RecommendService extends IService<Recommend> {
  Map<String, Object> queryBaseAction(Integer nowPage, Integer pageSize);
}
