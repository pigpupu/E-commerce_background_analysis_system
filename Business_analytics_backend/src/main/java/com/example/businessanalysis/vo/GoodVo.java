package com.example.businessanalysis.vo;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.businessanalysis.domain.Brand;
import com.example.businessanalysis.domain.Category;
import com.example.businessanalysis.domain.Good;
import com.example.businessanalysis.domain.Stock;
import com.example.businessanalysis.mapper.BrandMapper;
import com.example.businessanalysis.mapper.CategoryMapper;
import com.example.businessanalysis.mapper.StockMapper;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @ClassName GoodVo
 * @Description 用户查询返回视图
 * @Author 74707
 * @Date 2022/11/20 17:29
 * @Version V1.0
 */

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodVo{
  private String Id;
  private String goodName;
  private String categoryName;
  private String brandName;
  private BigDecimal price;
  private BigDecimal productWeightG;
  private BigDecimal productLengthCm;
  private BigDecimal productHeightCm;
  private BigDecimal productWidthCm;
  private String image;
  private String addTime;
  private String updateTime;

  @Resource
  public CategoryMapper categoryMapper;

  @Resource
  public BrandMapper brandMapper;

  public static GoodVo goodVo;

  @PostConstruct
  public void init(){
    goodVo =this;
  }
  public GoodVo(Good good){
    if(good==null) return;
    this.Id=good.getId();
    this.goodName=good.getGoodName();
    this.categoryName= Optional.ofNullable(goodVo.categoryMapper.
            selectOne(Wrappers.<Category>lambdaQuery().eq(Category::getId,good.getCategoryId())
                .eq(Category::getDelFlag,0)))
        .map(u->u.getCategoryName()).orElse("Unknown");
    this.brandName = Optional.ofNullable(goodVo.brandMapper.selectOne(Wrappers.<Brand>lambdaQuery().
            eq(Brand::getId,good.getBrandId()).eq(Brand::getDelFlag,0))).map(u->u.getBrandName())
        .orElse("Unknown");

    this.price = Optional.ofNullable(good.getPrice()).orElse(null);
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    this.image=good.getImage();
    this.productHeightCm=good.getProductHeightCm();
    this.productLengthCm=good.getProductLengthCm();
    this.productWidthCm=good.getProductWidthCm();
    this.productWeightG=good.getProductWeightG();
  }
}
