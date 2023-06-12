package com.example.businessanalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.businessanalysis.domain.Customer;
import com.example.businessanalysis.mapper.CustomerMapper;
import com.example.businessanalysis.service.CustomerService;
import org.springframework.stereotype.Service;

/**
 * @author 74707
 * @description 针对表【t_customer(客户表)】的数据库操作Service实现
 * @createDate 2022-10-26 12:01:20
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer>
    implements CustomerService {

}




