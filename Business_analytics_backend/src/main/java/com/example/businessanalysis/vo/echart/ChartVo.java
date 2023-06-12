package com.example.businessanalysis.vo.echart;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ChartVo
 * @Description 基本柱状图，折线图 / 饼图，地图的数据
 * @Author 74707
 * @Date 2022/10/30 10:49
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartVo<T> {

  private List<String> names;
  private List<T> values;
  private List<PieMapVo<T>> pieMapVoList;

}
