package com.example.businessanalysis.pojo.normalUserPojo;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @ClassName SaleQueryPojo
 * @Description 查询销售量的pojo
 * @Author 74707
 * @Date 2022/11/21 19:22
 * @Version V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockQueryPojo {
  private Integer nowPage=new Integer(1);
  private Integer pageSize=new Integer(10);;
  private Integer categoryID=new Integer(1);;
  private String startTimeStr=getDefaultStart();
  private String endTimeStr=getDefaultEnd();
  private Integer showSize=new Integer(4);
  private String inputName;
  private Integer precise=new Integer(0);

  public String getDefaultEnd(){
    LocalDate today = LocalDate.now();
    Date startTime = Date.valueOf(today);
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    return sdf.format(startTime);
  }

  public String getDefaultStart(){
    LocalDate today = LocalDate.now();
    LocalDate past = today.minusYears(5);
    Date endTime = Date.valueOf(past);
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    return sdf.format(endTime);
  }
}
