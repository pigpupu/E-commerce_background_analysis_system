package com.example.businessanalysis.controller.algorithm;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.io.file.LineSeparator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.businessanalysis.common.WrapMapper;
import com.example.businessanalysis.common.Wrapper;
import com.example.businessanalysis.domain.Good;
import com.example.businessanalysis.domain.GoodOrder;
import com.example.businessanalysis.domain.Python;
import com.example.businessanalysis.domain.SalesForecastModel;
import com.example.businessanalysis.domain.User;
import com.example.businessanalysis.mapper.GoodMapper;
import com.example.businessanalysis.mapper.GoodOrderMapper;
import com.example.businessanalysis.mapper.SalesForecastMapper;
import com.example.businessanalysis.pojo.normalUserPojo.SalesForecastPojo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName : SalesForecast
 * @Description : 销量预测相关均在此类
 * @Author : 桓
 * @Date: 2023/2/7  21:54
 */
@RestController
public class SalesForecast {
  // 设置路径
  private String pythonFilePath =
      System.getProperty("user.dir") + "/src/main/resources/python/" + "salesForecast.py";

  private String pythonDataPath =
      System.getProperty("user.dir") + "/src/main/resources/python/" + "dataSF.txt";

  @Resource
  private SalesForecastMapper salesForecastMapper;
  @Resource
  private GoodOrderMapper goodOrderMapper;
  @Resource
  private Python python;
  @Resource
  private GoodMapper goodMapper;

  private int statistic(Date begin, Date end, String id){
    QueryWrapper<GoodOrder> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("good_id",id).between("add_time",begin,end);
    int res = goodOrderMapper.selectCount(queryWrapper);
    return res;
  }

  private int categoryStatistic(Date begin,Date end, String id) {
    QueryWrapper<GoodOrder> queryWrapper = new QueryWrapper<>();
    QueryWrapper<Good> goodQuery = new QueryWrapper<>();
    QueryWrapper<Good> eq = goodQuery.select("id").eq("category_id", id);
    List<Good> goods = goodMapper.selectList(eq);
    int sum = 0;
    for(Good item:goods){
      queryWrapper.eq("good_id",item.getId()).between("add_time",begin,end);
      int res = goodOrderMapper.selectCount(queryWrapper);
      sum+=res;
    }
    return sum;
  }
  private String generate(String id) {
    try {
      // 从yml 文件中获取python路径
      String pythonPath = python.getPath();

      System.out.println("pythonPath:"+pythonPath);

      Process pr = Runtime.getRuntime().exec(pythonPath+" "+pythonFilePath+" "+pythonDataPath);
      //获取python文件运行后的输出


      System.out.println("process pr:"+pr);
      System.out.println("process pr:"+pr.getInputStream());
      BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
      String line;
      //System.out.println("BufferedReader in:"+in.readLine());

      if ((line = in.readLine()) != null) {
        System.out.println("in.readline 不为null");
        double k = Double.parseDouble(line);
        line = in.readLine();
        double b = Double.parseDouble(line);
        SalesForecastModel model = salesForecastMapper.selectOne(
            new QueryWrapper<SalesForecastModel>().eq("id", id)
        );
        if (model == null) {
          salesForecastMapper.insert(new SalesForecastModel(id,k,b,0));
        } else {
          salesForecastMapper.update(new SalesForecastModel(id,k,b,0),
              new UpdateWrapper<SalesForecastModel>().eq("id",id));
        }
        return "python返回，执行结果"+ k + b;
      }
      in.close();
      pr.waitFor();
    } catch (Exception e) {
      e.printStackTrace();
      return "执行python脚本失败";
    }
    return "执行完毕";
  }

  private int goodSalesForecast(String id,String beginDate,String endDate) {
    // 判断模型是否存在,并且尚未过期
    QueryWrapper<SalesForecastModel> queryWrapper = new QueryWrapper<>();
    System.out.println(id);
    queryWrapper.eq("id",id);
    SalesForecastModel salesForecastModel = salesForecastMapper.selectOne(queryWrapper);
    if(salesForecastModel==null || salesForecastModel.getDelFlag()==1){
      // 设计时间
      List<Date> time = new ArrayList<>();
      String firstMouth = "2016-9-1";
      Date date = DateUtil.parse(firstMouth);
      ArrayList<Integer> listx = new ArrayList<>();
      for(int i = 0; i < 44; i++){
        Date temp = DateUtil.offsetMonth(date, 1+i);
        time.add(temp);
        listx.add((int) DateUtil.between(date,temp,DateUnit.DAY));
      }
      // 获取数据库中数据
      List<Integer> listy = new ArrayList<>();
      int sum = 0;
      for(Date item:time){
        sum = sum+statistic(date,item,id);
        listy.add(sum);
        date = item;
      }
      // 写入文件
      StringBuilder x = new StringBuilder();
      StringBuilder y = new StringBuilder();
      for(int i:listx){
        x.append(i);
        x.append(" ");
      }
      for(int i:listy){
        y.append(i);
        y.append(" ");
      }

      FileWriter writer = new FileWriter(pythonDataPath);

      writer.write(x.toString());
      writer.append("\n");
      writer.append(y.toString());
      // 重新生成
      System.out.println(generate(id));
    }
    SalesForecastModel SFM = salesForecastMapper.selectOne(queryWrapper);
    // 判断非空
    if (SFM == null) {
      return -1;
      // 代表数据库中没有对应商品id
    }
    double k = SFM.getK();
    double b = SFM.getB();
    long betweenDays = DateUtil.between(DateUtil.parse(beginDate),DateUtil.parse(endDate),DateUnit.DAY);
    int res = (int) (betweenDays*k);
    return res;
  }

  private int categorySalesForecast(String id,String beginDate,String endDate) {
    // 判断模型是否存在,并且尚未过期
    QueryWrapper<SalesForecastModel> queryWrapper = new QueryWrapper<>();
    System.out.println(id);
    queryWrapper.eq("id",id);
    SalesForecastModel salesForecastModel = salesForecastMapper.selectOne(queryWrapper);
    if(salesForecastModel==null || salesForecastModel.getDelFlag()==1){
      // 设计时间
      List<Date> time = new ArrayList<>();
      String firstMouth = "2016-9-1";
      Date date = DateUtil.parse(firstMouth);
      ArrayList<Integer> listx = new ArrayList<>();
      for(int i = 0; i < 44; i++){
        Date temp = DateUtil.offsetMonth(date, 1+i);
        time.add(temp);
        listx.add((int) DateUtil.between(date,temp,DateUnit.DAY));
      }
      // 获取数据库中数据
      List<Integer> listy = new ArrayList<>();
      int sum = 0;
      for(Date item:time){
        sum = sum+categoryStatistic(date,item,id);
        listy.add(sum);
        date = item;
      }
      // 写入文件
      StringBuilder x = new StringBuilder();
      StringBuilder y = new StringBuilder();
      for(int i:listx){
        x.append(i);
        x.append(" ");
      }
      for(int i:listy){
        y.append(i);
        y.append(" ");
      }

      FileWriter writer = new FileWriter(pythonDataPath);

      writer.write(x.toString());
      writer.append("\n");
      writer.append(y.toString());
      // 重新生成
      System.out.println(generate(id));
    }
    SalesForecastModel SFM = salesForecastMapper.selectOne(queryWrapper);
    // 判断非空
    if (SFM == null) {
      return -1;
      // 代表数据库中没有对应大类
    }
    double k = SFM.getK();
    double b = SFM.getB();
    long betweenDays = DateUtil.between(DateUtil.parse(beginDate),DateUtil.parse(endDate),DateUnit.DAY);
    int res = (int) (betweenDays*k);
    return res;
  }


  @GetMapping("/SalesForecast/four")
  public Wrapper<Map<String,Integer>> four(@RequestParam String categoryId,@RequestParam int period){
    HashMap<String, Integer> map = new HashMap<>();
    String beginDate = "2020-4-10";
    Date begin = DateUtil.parse(beginDate);
    Date end = null;
    ArrayList<Date> dates = new ArrayList<>();
    if (period==1){
      end = DateUtil.offsetMonth(begin,12);
      dates.add(DateUtil.offsetMonth(begin,-12));
      dates.add(DateUtil.offsetMonth(begin,-24));
      dates.add(DateUtil.offsetMonth(begin,-36));

    }else if(period==2){
      end = DateUtil.offsetMonth(begin,1);
      dates.add(DateUtil.offsetMonth(begin,-1));
      dates.add(DateUtil.offsetMonth(begin,-2));
      dates.add(DateUtil.offsetMonth(begin,-3));

    }else if(period==3){
      end = DateUtil.offsetWeek(begin,1);
      dates.add(DateUtil.offsetWeek(begin,-1));
      dates.add(DateUtil.offsetWeek(begin,-2));
      dates.add(DateUtil.offsetWeek(begin,-3));

    }else if(period==4){
      end = DateUtil.offsetDay(begin,1);
      dates.add(DateUtil.offsetDay(begin,-1));
      dates.add(DateUtil.offsetDay(begin,-2));
      dates.add(DateUtil.offsetDay(begin,-3));
    }else{
      return WrapMapper.wrap(Wrapper.ILLEGAL_ARGUMENT_CODE_,"周期不合法",map);
    }
    int far = categorySalesForecast(categoryId,begin.toString(),end.toString());
    map.put("预测值",far);

    for (int i = 0; i < 3; i++) {
      map.put("第"+(i+1),categoryStatistic(begin,dates.get(i),categoryId));
    }
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE,"查询成功",map);
  }

  @PostMapping("/SalesForecast")
  public Wrapper<Integer> salesForecast(@RequestBody SalesForecastPojo salesForecastPojo){
   int res = goodSalesForecast(salesForecastPojo.getGoodId(),salesForecastPojo.getBeginDate(),salesForecastPojo.getEndDate());
   if (res == -1) {
     return WrapMapper.wrap(Wrapper.ERROR_CODE, "无对应商品id", -1);
   }
    return WrapMapper.wrap(Wrapper.SUCCESS_CODE,"执行完毕",res);
  }
}
