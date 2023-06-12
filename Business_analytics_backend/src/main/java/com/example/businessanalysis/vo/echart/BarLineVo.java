package com.example.businessanalysis.vo.echart;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName BarVo
 * @Description 柱状图
 * @Author 74707
 * @Date 2022/10/26 23:45
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarLineVo<T> {

  private List<String> names;
  private List<T> values;
}
