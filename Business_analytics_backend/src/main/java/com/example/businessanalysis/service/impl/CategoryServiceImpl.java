package com.example.businessanalysis.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.businessanalysis.domain.Category;
import com.example.businessanalysis.mapper.CategoryMapper;
import com.example.businessanalysis.service.CategoryService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * @author 74707
 * @description 针对表【t_category(类别表)】的数据库操作Service实现
 * @createDate 2022-10-26 12:01:20
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {

  public List<Integer> findAllSubCatIdsByCatId(Integer typeId) {
    Category category = this.getById(typeId);
    if (category != null && category.getId() == 1) {    // 所有类别
      // 通过list查询到所有类别
      return this.list().stream().map(Category::getId).collect(Collectors.toList());
    }

    // 返回分类id(包括下级分类id)
    List<Integer> result = new ArrayList<>();
    result.add(typeId);
    return getSubTypeId(typeId, result);
  }

  /**
   * 递归循环 判断查询的id对应的下级是否还存在下级分类
   */
  private List<Integer> getSubTypeId(Integer typeId, List<Integer> result) {
    // id值作为pid去查询
    List<Category> goodsTypes = this.baseMapper.selectList(
        Wrappers.<Category>lambdaQuery().eq(Category::getParentId, typeId));
    // 判断是否存在下级分类 不存在则结束递归
    if (null != goodsTypes) {
      for (Category item : goodsTypes) {
        result.add(item.getId());
        getSubTypeId(item.getId(), result);
      }
    }
    return result;
  }

}




