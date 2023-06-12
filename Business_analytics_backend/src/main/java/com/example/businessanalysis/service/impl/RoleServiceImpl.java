package com.example.businessanalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.businessanalysis.domain.Role;
import com.example.businessanalysis.mapper.RoleMapper;
import com.example.businessanalysis.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * @author 74707
 * @description 针对表【t_role(角色表)】的数据库操作Service实现
 * @createDate 2022-10-26 12:01:20
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService {

}




