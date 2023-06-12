package com.example.businessanalysis.service;

import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.businessanalysis.domain.Category;
import java.util.List;

/**
 * @author 74707
 * @description 针对表【t_category(类别表)】的数据库操作Service
 * @createDate 2022-10-26 12:01:20
 */
public interface CategoryService extends IService<Category> {

  /**
   * 通过类别id寻找所有子类
   *
   * @param CatId
   * @return List<Integer> 所有符合条件的类别list
   */
  List<Integer> findAllSubCatIdsByCatId(Integer CatId);
}
