package com.example.businessanalysis.pojo.adminPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName : DataInsert
 * @Description : 用来导入数据的一个专用类
 * @Author : 桓
 * @Date: 2022/11/7  21:29
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DataInsert {
  String id;
  String customerName;
  String zhongleiId;
  String regime;
}
