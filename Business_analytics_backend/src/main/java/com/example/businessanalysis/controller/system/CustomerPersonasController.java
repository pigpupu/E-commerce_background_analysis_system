package com.example.businessanalysis.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.businessanalysis.common.WrapMapper;
import com.example.businessanalysis.common.Wrapper;
import com.example.businessanalysis.domain.User;
import com.example.businessanalysis.mapper.CustomerMapper;
import com.example.businessanalysis.mapper.UserMapper;
import com.example.businessanalysis.utils.EqualCount;
import com.example.businessanalysis.vo.UserVo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName : Personas
 * @Description : 客户画像控制类
 * @Author : 桓
 * @Date: 2022/11/1  15:45
 */
@CrossOrigin
@RestController
@RequestMapping("/customer")
public class CustomerPersonasController {


  @Resource
  CustomerMapper customer;
  @Resource
  EqualCount<CustomerMapper> equalCount;
  @Resource
  UserMapper userMapper;


  /**
   * 传入客户id 返回一系列信息
   */
  @GetMapping("personas")
  public Wrapper<UserVo> getPersonas(@RequestParam String id) {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("id",id);
    User user = userMapper.selectOne(queryWrapper);
    UserVo userVo = new UserVo(user);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE,id+"的信息",userVo);

  }

  /**
   * 用户类别比较
   *
   * @return Wrapper<Map < String,Integer>>
   */
  @GetMapping("personas/type")
  public Wrapper<Map<String,Integer>> getTypeRant() {
    Map<String,Integer> result = new HashMap<>();
    int a = equalCount.getColCount(customer, "customer_type", 0);
    int b = equalCount.getColCount(customer, "customer_type", 1);
    result.put("零售用户",a);
    result.put("批发用户",b);
    //result中存储着 零售客户 和 批发客户各自的数量
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, "零售客户和批发客户数量", result);
  }

  /**
   * 用户性别比较
   *
   * @return Wrapper<List < Integer>>
   */
  @GetMapping("/personas/sex")
  public  Wrapper<Map<String,Integer>> getSexRant() {
    Map<String,Integer> result = new HashMap<>();
    int a = equalCount.getColCount(customer, "gender", 0);
    int b = equalCount.getColCount(customer, "gender", 1);
    result.put("男",a);
    result.put("女",b);

    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, "客户性别数量", result);
  }

  /**
   * 用户婚姻比较
   *
   * @return Wrapper<List < Integer>>
   */
  @GetMapping("/personas/marriage")
  public Wrapper<Map<String,Integer>> getMarriageRant() {
    Map<String,Integer> result = new HashMap<>();
    String[] arr = {"不明","未婚","已婚"};
    for (int i = 0; i < 3; i++) {
      int temp = equalCount.getColCount(customer, "marriage", i);
      result.put(arr[i],temp);
    }
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, "客户婚姻情况", result);
  }

  /**
   * 用户学历比较
   *
   * @return Wrapper<List < Integer>>
   */
  @GetMapping("/personas/degree")
  public  Wrapper<Map<String,Integer>> getWrapperRant() {
    Map<String,Integer> result = new HashMap<>();
    String[] arr = {"不明","小学及以下","中学","高中","大学","大学及以上"};
    for (int i = 0; i < 6; i++) {
      int temp = equalCount.getColCount(customer, "degree", i);
      result.put(arr[i],temp);
    }
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, "客户学历情况", result);
  }

  /**
   * 用户收入比较
   *
   * @return Wrapper<List < Integer>>
   */
  @GetMapping("/personas/income_level")
  public  Wrapper<Map<String,Integer>> getIncomeLevelRant() {
    Map<String,Integer> result = new HashMap<>();
    String[] arr = {"不明","低收入","中收入","高收入"};
    for (int i = 0; i < 4; i++) {
      int temp = equalCount.getColCount(customer, "income_level", i);
      result.put(arr[i],temp);
    }
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, "客户收入情况", result);
  }
}
