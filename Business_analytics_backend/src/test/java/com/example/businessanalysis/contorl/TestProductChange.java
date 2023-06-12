package com.example.businessanalysis.contorl;

import com.example.businessanalysis.domain.Good;
import com.example.businessanalysis.domain.GoodOrder;
import com.example.businessanalysis.mapper.GoodMapper;
import com.example.businessanalysis.mapper.GoodOrderMapper;
import com.example.businessanalysis.vo.CustomerBuy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @ClassName : TestProductChange
 * @Description : ProductChange的测试类
 * @Author : 桓
 * @Date: 2022/11/4  19:32
 */
@SpringBootTest
@MapperScan("com.example.businessanalysis.mapper")
public class TestProductChange {

  @Resource
  GoodOrderMapper goodOrderMapper;
  @Resource
  GoodMapper goodMapper;

  @Test
  public void testProductChange() {
    List<CustomerBuy> result = new ArrayList<>();
    Map<String, Object> selMap = new HashMap<>();
    selMap.put("customer_id", 2);
    List<GoodOrder> temp = goodOrderMapper.selectByMap(selMap);
    for (GoodOrder go : temp) {
      //? 这里的Id指定是哪个ID
      Good g = goodMapper.selectById(go.getGoodId());
      String gName = g.getGoodName();
      CustomerBuy cb = new CustomerBuy("2", gName, go.getPrice(), go.getNum(), go.getAddTime());
      result.add(cb);
    }
    result.forEach(System.out::println);
  }
}
