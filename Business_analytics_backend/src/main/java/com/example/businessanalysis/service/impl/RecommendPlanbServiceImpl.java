package com.example.businessanalysis.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.businessanalysis.common.exception.BusinessException;
import com.example.businessanalysis.domain.Recommend;
import com.example.businessanalysis.domain.RecommendPlanb;
import com.example.businessanalysis.mapper.RecommendMapper;
import com.example.businessanalysis.service.RecommendPlanbService;
import com.example.businessanalysis.mapper.RecommendPlanbMapper;
import com.example.businessanalysis.vo.RecommendPlanbVo;
import com.example.businessanalysis.vo.RecommendVo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author 74707
* @description 针对表【t_recommend_planb(基于购买的商品推荐表)】的数据库操作Service实现
* @createDate 2023-02-07 14:13:25
*/
@Service
public class RecommendPlanbServiceImpl extends ServiceImpl<RecommendPlanbMapper, RecommendPlanb>
    implements RecommendPlanbService{

  @Resource
  public RecommendPlanbMapper recommendPlanbMapper;

  public Map<String, Object> queryBaseScore(Integer nowPage, Integer pageSize){
    Map<String, Object> re = new HashMap<>();
    List<RecommendPlanbVo> res;
    IPage<RecommendPlanb> raw=recommendPlanbMapper.selectPage(new Page<>(nowPage, pageSize),null);
    if (Objects.isNull(raw) || CollectionUtil.isEmpty(raw.getRecords())) {
      throw new BusinessException("冷启动ing，目前暂无推荐");
    }

    res=raw.getRecords().stream().map(v->new RecommendPlanbVo(v)).collect(Collectors.toList());
    re.put("total", raw.getTotal());
    re.put("pages", raw.getPages());
    re.put("pageSize", raw.getSize());
    re.put("current", raw.getCurrent());
    re.put("records", res);
    return re;
  }
}




