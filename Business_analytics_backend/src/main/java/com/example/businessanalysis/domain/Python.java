package com.example.businessanalysis.domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName : Python
 * @Description : 注意! 此类在数据库中无对应表,只是用来读取yml配置文件
 * @Author : 桓
 * @Date: 2023/2/9  15:44
 */
@Component
@Data
public class Python {
  @Value("${python.path}")
  private String path;
}
