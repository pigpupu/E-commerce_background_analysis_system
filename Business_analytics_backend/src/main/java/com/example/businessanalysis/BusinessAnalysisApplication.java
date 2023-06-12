package com.example.businessanalysis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@MapperScan("com.example.businessanalysis.mapper")
public class BusinessAnalysisApplication {

  public static void main(String[] args) {
    SpringApplication.run(BusinessAnalysisApplication.class, args);
  }

}
