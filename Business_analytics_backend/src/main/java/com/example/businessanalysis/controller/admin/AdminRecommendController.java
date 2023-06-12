package com.example.businessanalysis.controller.admin;

import com.example.businessanalysis.common.WrapMapper;
import com.example.businessanalysis.common.Wrapper;
import com.example.businessanalysis.domain.Customer;
import com.example.businessanalysis.domain.RecommendPlanb;
import com.example.businessanalysis.service.CustomerService;
import com.example.businessanalysis.service.GoodOrderService;
import com.example.businessanalysis.service.RecommendPlanbService;
import com.example.businessanalysis.service.RecommendService;
import com.example.businessanalysis.service.impl.GoodServiceImpl;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName AdminRecommendController
 * @Description 管理员商品推荐
 * @Author 74707
 * @Date 2023/2/7 10:12
 * @Version V1.0
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/admin/recommend")
public class AdminRecommendController {
  @Resource
  public RecommendService recommendService;

  @Resource
  public RecommendPlanbService recommendPlanbService;

  @RequestMapping(value = "/planA")
  public Wrapper<Map<String,Object>> queryBaseAction(Integer nowPage, Integer pageSize){
    Map<String ,Object> res=recommendService.queryBaseAction(nowPage,pageSize);
    return WrapMapper.wrap(res);
  }

  @RequestMapping(value = "/planB")
  public Wrapper<Map<String,Object>> queryBaseScore(Integer nowPage, Integer pageSize){
    Map<String ,Object> res=recommendPlanbService.queryBaseScore(nowPage,pageSize);
    return WrapMapper.wrap(res);
  }
}
