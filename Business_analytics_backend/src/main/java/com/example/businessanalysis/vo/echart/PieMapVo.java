package com.example.businessanalysis.vo.echart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName PieVo
 * @Description 饼图和地图
 * @Author 74707
 * @Date 2022/10/30 11:38
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PieMapVo<T> {

  private String name;
  private T value;
}
