package com.example.businessanalysis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.businessanalysis.domain.Customer;
import com.example.businessanalysis.domain.User;
import com.example.businessanalysis.mapper.CustomerMapper;
import com.example.businessanalysis.mapper.GoodOrderMapper;
import com.example.businessanalysis.mapper.UserMapper;
import com.example.businessanalysis.service.GoodOrderService;
import com.example.businessanalysis.service.UserService;
import com.example.businessanalysis.utils.EqualCount;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

//此处为test类，不要理会，全是没扔的垃圾

@SpringBootTest
@MapperScan("com.example.businessanalysis.mapper")
class BusinessAnalysisApplicationTests {

  @Resource
  private UserMapper userMapper;

  @Resource
  private UserService userService;

  @Resource
  private GoodOrderService goodOrderService;

  @Resource
  private EqualCount<UserMapper> equalCount;

  @Resource
  private PasswordEncoder passwordEncoder;

  @Resource
  CustomerMapper customerMapper;

  @Test
  void testSave() {
    User user = new User();
    user.setUserName("ljq");
    userMapper.insert(user);
  }

  @Test
  void queryAllByPage() {
      User user=new User();
      user.setAccount("1234789");
      user.setRoleId(2);
      user.setGender(0);
      userService.updateUser(user);
  }

  @Test
  void saleTest() {
    Date startTime = Date.valueOf("2022-10-20");
    Date endTime = Date.valueOf("2022-10-28");
//        System.out.println(goodOrderService.getPeriodSaleMoneyBasicChartVo(2,startTime,endTime,3));
//        System.out.println(goodOrderService.getPeriodSaleVolumeBasicChartVo(2,startTime,endTime,3));
  }

  @Test
  void saleTest2() {
    Date startTime = Date.valueOf("2022-10-20");
    Date endTime = Date.valueOf("2022-12-28");
    //System.out.println(goodOrderService.getPeriodSaleMoneyMapBasicChartVo(2,startTime,endTime));
    //System.out.println(goodOrderService.getPeriodCustomerCompareVolumeBasicChartVo(2,startTime,endTime));
    //goodOrderService.getPeriodSaleVolumeMapBasicChartVo(2,startTime,endTime);

  }



  @Test
  public void testEncoder(){

    System.out.println(passwordEncoder.encode("123456"));
  }

  @Test
  @WithUserDetails("123456")
  public void testColumn(){
    QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
    queryWrapper.select("id");
    List<Customer> customers = customerMapper.selectList(queryWrapper);
    customers.forEach(System.out::println);
  }

}
