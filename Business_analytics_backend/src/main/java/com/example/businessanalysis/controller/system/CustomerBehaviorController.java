package com.example.businessanalysis.controller.system;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.businessanalysis.common.WrapMapper;
import com.example.businessanalysis.common.Wrapper;
import com.example.businessanalysis.domain.Category;
import com.example.businessanalysis.domain.Customer;
import com.example.businessanalysis.domain.Good;
import com.example.businessanalysis.domain.GoodOrder;
import com.example.businessanalysis.mapper.CategoryMapper;
import com.example.businessanalysis.mapper.CustomerMapper;
import com.example.businessanalysis.mapper.GoodMapper;
import com.example.businessanalysis.mapper.GoodOrderMapper;
import com.example.businessanalysis.utils.EqualCount;
import com.example.businessanalysis.utils.MyDateUtils;
import com.example.businessanalysis.vo.CustomerBuy;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.math3.optim.InitialGuess;
import org.bouncycastle.jcajce.provider.symmetric.AES.Wrap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName : CustomerBehaviorController
 * @Description : 客户行为，包含下单周期+单品变化+（客诉情况+流失预警 未完成）
 * @Author : 桓
 * @Date: 2022/11/1  20:11
 */
@CrossOrigin
@RestController
@RequestMapping("/customer")
public class CustomerBehaviorController {

  //需要使用多表查询 使用两个表
  //下单周期是指:客户多次下单的平均时间间隔,
  //假设查询时传入参数 客户id  起始时间 结束时间
  //返回值为:指定时间内的下单次数
  @Resource
  GoodOrderMapper goodOrderMapper;

  @Resource
  CustomerMapper customerMapper;

  @Resource
  CategoryMapper categoryMapper;

  @Resource
  EqualCount<GoodOrderMapper> equalCount;

  @Resource
  GoodMapper goodMapper;

  /**
   * @param id        客户id
   * @param beginDate 指定开始时间
   * @param endDate   结束时间
   * @return int 客户在周期内的下单次数
   * @author 桓
   * @date 10:19 2022/11/3
   **/
  @GetMapping("/buy_cycle")
  public Wrapper<Integer> getBuyCycle(@RequestParam int id,
      @RequestParam String beginDate, @RequestParam String endDate) {
    int res = equalCount.getInTimeCount(goodOrderMapper, "customer_id", id, "add_time",
        MyDateUtils.stringToDate(beginDate), MyDateUtils.stringToDate(endDate));
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, "指定时间段内,客户的下单次数", res);
  }

  /**
   * @param id 客户id
   * @return int 客户下单单品变化
   * @author 桓
   * @date 10:19 2022/11/3
   **/
  @RequestMapping("/product_change")
  public Wrapper<List<CustomerBuy>> productChange(
      @RequestParam(value = "id", defaultValue = "1") String id) {
    //这里约等于实现了多表查询
    //先把一个表中的数据查出来,之后利用查出来的数据接着查另一个表
    List<CustomerBuy> result = new ArrayList<>();
    Map<String, Object> selMap = new HashMap<>();
    selMap.put("customer_id", id);
    List<GoodOrder> temp = goodOrderMapper.selectByMap(selMap);
    for (GoodOrder go : temp) {
      Good g = goodMapper.selectById(go.getGoodId());
      String gName = g.getGoodName();
      CustomerBuy cb = new CustomerBuy(id, gName, go.getPrice(), go.getNum(), go.getAddTime());
      result.add(cb);
    }
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE, "客户下单单品变化", result);
  }

  /**
   * 接口1
   * @return 所有的客户id
   */
  @GetMapping("/allId")
  public Wrapper<List<String>> allId() {
    QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
    queryWrapper.select("id");
    List<Customer> customers = customerMapper.selectList(queryWrapper);
    ArrayList<String> ids = new ArrayList<>();
    for(Customer item:customers) {
      ids.add(item.getId());
    }
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE,"所有的客户id",ids);
  }

  /***
   * @author 桓
   * @date 22:45 2023/2/10
 * @param id 客户id
 * @return 姓名() 性别(男/女) 对象类型 婚姻状态、学历、流失预警
  **/

  @GetMapping("/getInfoById")
  public Wrapper<Map<String,Object>> getInfoById(@RequestParam String id) {
    QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("id",id);
    Customer customer = customerMapper.selectOne(queryWrapper);
    if (customer==null) {
      System.out.println("无id对应客户");
      return WrapMapper.wrap(Wrapper.ILLEGAL_ARGUMENT_CODE_,"无id对应用户");
    }
    HashMap<String, Object> stringObjectHashMap = new HashMap<>();
    stringObjectHashMap.put("姓名",customer.getCustomerName());
//    if (customer.getGender()==0){
//      stringObjectHashMap.put("性别","男");
//    }else {
//      stringObjectHashMap.put("性别","女");
//    }
//    if (customer.getMarriage()==0){
//      stringObjectHashMap.put("婚姻状况","不明");
//    }else if(customer.getMarriage()==1){
//      stringObjectHashMap.put("婚姻状况","未婚");
//    }else{
//      stringObjectHashMap.put("婚姻状况","已婚");
//    }
    stringObjectHashMap.put("性别",customer.getGender());
    stringObjectHashMap.put("婚姻状况",customer.getMarriage());
    stringObjectHashMap.put("客户类型",customer.getCustomerType());
    // 约定流失预警 0 安全 1 普通 2 警告 3 危险
    int warning = customer.getCustomerType()+customer.getMarriage()+customer.getMarriage()-1;
    if (warning<0)warning=0;
    stringObjectHashMap.put("流失预警",warning);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE,"成功",stringObjectHashMap);
  }

  private int stastic(String id, Date beginDate, Date endDate){
    QueryWrapper<GoodOrder> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("customer_id",id).between("add_time",beginDate,endDate);
    return goodOrderMapper.selectCount(queryWrapper);
  }
  @GetMapping("stastic1")
  public Wrapper<Integer> stastic1(@RequestParam String id, @RequestParam int length,@RequestParam String beginDate){
    int res = 0;
    Date begin = DateUtil.parse(beginDate);
    if(length==1){
      // 统计年
      Date end = DateUtil.offsetMonth(begin,12);
      res = stastic(id,begin,end);
    }else if (length == 2){
      Date end = DateUtil.offsetMonth(begin,1);
      res = stastic(id,begin,end);
    }else if (length == 3){
      Date end = DateUtil.offsetWeek(begin,1);
      res = stastic(id,begin,end);
    }else{
      return WrapMapper.wrap(Wrapper.ILLEGAL_ARGUMENT_CODE_,"length 不符合约定",res);

    }
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE,"查询成功",res);
  }
  private Map<String,Integer> compute(String id,Date begin,Date end){
    HashMap<String, Integer> map = new HashMap<>();
    QueryWrapper<GoodOrder> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("customer_id",id).between("add_time",begin,end);
    List<GoodOrder> goodOrders = goodOrderMapper.selectList(queryWrapper);
    // 统计不同种类商品的数量
    for(GoodOrder item : goodOrders){
      QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
      QueryWrapper<Good> goodQueryWrapper = new QueryWrapper<>();
      goodQueryWrapper.eq("id",item.getGoodId()).select("category_id");
      int categoryId = goodMapper.selectOne(goodQueryWrapper).getCategoryId();
      categoryQueryWrapper.eq("id",categoryId);

      String category = categoryMapper.selectOne(categoryQueryWrapper).getCategoryName();
      if(map.containsKey(category)) {
        map.replace(category,map.get(category)+1);
      }else{
        map.put(category,1);
      }
    }
    return map;
  }
  @GetMapping("/stastic2")
  public Wrapper<Map<String,Integer>> stastic2(@RequestParam String id){
    Date begin = DateUtil.parse("2015-1-1");
    Date end = DateUtil.parse("2021-1-1");
    Map<String, Integer> res = compute(id,begin,end);
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE,"查询成功",res);
  }
  @GetMapping("/stastic3")
  public Wrapper<Map<String,Integer>> stastic3(@RequestParam String id,@RequestParam int length,@RequestParam String beginDate){
    Map<String,Integer>  res = new HashMap<>();
    Date begin = DateUtil.parse(beginDate);
    if(length==1){
      // 统计年
      Date end = DateUtil.offsetMonth(begin,12);
      res = compute(id,begin,end);
    }else if (length == 2){
      Date end = DateUtil.offsetMonth(begin,1);
      res = compute(id,begin,end);
    }else if (length == 3){
      Date end = DateUtil.offsetWeek(begin,1);
      res = compute(id,begin,end);
    }else{
      return WrapMapper.wrap(Wrapper.ILLEGAL_ARGUMENT_CODE_,"length 不符合约定",res);

    }
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE,"查询成功",res);

  }
}
