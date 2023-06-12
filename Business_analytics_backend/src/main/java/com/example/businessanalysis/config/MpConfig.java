package com.example.businessanalysis.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MpConfig
 * @Description mp分页配置
 * @Author 74707
 * @Date 2022/10/26 19:57
 * @Version V1.0
 */
@Configuration
public class MpConfig {

  @Bean
  public MybatisPlusInterceptor mpInterceptor() {
    MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
    mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
    return mybatisPlusInterceptor;
  }

}
