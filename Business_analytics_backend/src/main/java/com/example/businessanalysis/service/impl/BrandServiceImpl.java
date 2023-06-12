package com.example.businessanalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.businessanalysis.domain.Brand;
import com.example.businessanalysis.mapper.BrandMapper;
import com.example.businessanalysis.service.BrandService;
import org.springframework.stereotype.Service;

/**
 * @author 74707
 * @description 针对表【t_brand(品牌表)】的数据库操作Service实现
 * @createDate 2022-10-26 12:01:20
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand>
    implements BrandService {

}




