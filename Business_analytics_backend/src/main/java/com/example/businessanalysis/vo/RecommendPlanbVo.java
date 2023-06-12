package com.example.businessanalysis.vo;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.businessanalysis.domain.Customer;
import com.example.businessanalysis.domain.Good;
import com.example.businessanalysis.domain.Recommend;
import com.example.businessanalysis.domain.RecommendPlanb;
import com.example.businessanalysis.mapper.CustomerMapper;
import com.example.businessanalysis.mapper.GoodMapper;
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
public class RecommendPlanbVo {
  private String customerId;
  private String customerName;
  private String goodId;
  private String goodName;
  private Double score;

  @Resource
  public CustomerMapper customerMapper;
  @Resource
  public GoodMapper goodMapper;

  public static RecommendPlanbVo RecommendVo;
  @PostConstruct
  public void init(){
    RecommendVo =this;
  }

  public RecommendPlanbVo(RecommendPlanb recommendPlanb){
    if(recommendPlanb==null) return;
    this.customerId=recommendPlanb.getCustomerId();
    this.goodId=recommendPlanb.getGoodId();
    this.score=recommendPlanb.getScore();

    this.customerName= Optional.ofNullable(RecommendVo.customerMapper.selectOne(Wrappers.<Customer>lambdaQuery().
        eq(Customer::getId,recommendPlanb.getCustomerId()).eq(Customer::getDelFlag,0))).map
        (u->u.getCustomerName()).orElse("Unknown");

    this.goodName= Optional.ofNullable(RecommendVo.goodMapper.selectOne(Wrappers.<Good>lambdaQuery().
        eq(Good::getId,recommendPlanb.getGoodId()).eq(Good::getDelFlag,0))).map
        (u->u.getGoodName()).orElse("Unknown");
  }


}
