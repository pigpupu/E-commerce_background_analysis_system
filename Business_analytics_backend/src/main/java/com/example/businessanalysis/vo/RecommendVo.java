package com.example.businessanalysis.vo;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.businessanalysis.domain.Category;
import com.example.businessanalysis.domain.Customer;
import com.example.businessanalysis.domain.Good;
import com.example.businessanalysis.domain.Recommend;
import com.example.businessanalysis.mapper.BrandMapper;
import com.example.businessanalysis.mapper.CustomerMapper;
import com.example.businessanalysis.mapper.GoodMapper;
import java.lang.ref.WeakReference;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @ClassName RecommendVo
 * @Description 商品推荐视图
 * @Author 74707
 * @Date 2023/2/7 10:39
 * @Version V1.0
 */

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendVo {
  private String customerId;
  private String customerName;
  private String goodId;
  private String goodName;
  private Double score;

  @Resource
  public CustomerMapper customerMapper;
  @Resource
  public GoodMapper goodMapper;

  public static RecommendVo recommendVo;
  @PostConstruct
  public void init(){
    recommendVo =this;
  }

  public RecommendVo(Recommend recommend){
    if(recommend==null) return;
    this.customerId=recommend.getCustomerId();
    this.goodId=recommend.getGoodId();
    this.score=recommend.getScore();

    this.customerName= Optional.ofNullable(recommendVo.customerMapper.selectOne(Wrappers.<Customer>lambdaQuery().
        eq(Customer::getId,recommend.getCustomerId()).eq(Customer::getDelFlag,0))).map
        (u->u.getCustomerName()).orElse("Unknown");

    this.goodName= Optional.ofNullable(recommendVo.goodMapper.selectOne(Wrappers.<Good>lambdaQuery().
        eq(Good::getId,recommend.getGoodId()).eq(Good::getDelFlag,0))).map
        (u->u.getGoodName()).orElse("Unknown");
  }


}
