package com.example.businessanalysis.controller.system;

import com.example.businessanalysis.domain.Good;
import com.example.businessanalysis.mapper.GoodMapper;
import com.example.businessanalysis.utils.SearchUtils;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SearchController
 * @Description 左侧搜索的控制类
 * @Author 74707
 * @Date 2022/11/8 23:18
 * @Version V1.0
 */

/**
 * @ClassName SaleController
 * @Description 为销售量展示图表提供数据
 * @Author ljq
 * @Date 2022/10/27 13:03
 * @Version V1.0
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/input/goodName")
public class SearchController {

  @Resource
  public GoodMapper goodMapper;

  /**
   * 商品名称搜索输入提供搜索提示
   *
   * @param inputName 输入
   * @return List<String>
   */
  @GetMapping("/tips")
  public List<String> getSearchTip(String inputName) {
    List<String> loadWords = goodMapper.selectList(null).stream().map(Good::getGoodName).
        collect(Collectors.toList());
    List<String> option = SearchUtils.search(inputName, loadWords);
    return option;
  }

}
