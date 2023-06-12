package com.example.businessanalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.businessanalysis.domain.Region;
import com.example.businessanalysis.mapper.RegionMapper;
import com.example.businessanalysis.service.RegionService;
import org.springframework.stereotype.Service;

/**
 * @author 74707
 * @description 针对表【t_region(行政区域表)】的数据库操作Service实现
 * @createDate 2022-10-26 12:01:20
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region>
    implements RegionService {

}




