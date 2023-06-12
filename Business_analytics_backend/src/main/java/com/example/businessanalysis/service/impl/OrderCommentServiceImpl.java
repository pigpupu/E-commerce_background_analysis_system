package com.example.businessanalysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.businessanalysis.domain.OrderComment;
import com.example.businessanalysis.mapper.OrderCommentMapper;
import com.example.businessanalysis.service.OrderCommentService;
import org.springframework.stereotype.Service;

/**
 * @author 74707
 * @description 针对表【t_order_comment(订单评价表)】的数据库操作Service实现
 * @createDate 2022-10-26 12:01:20
 */
@Service
public class OrderCommentServiceImpl extends ServiceImpl<OrderCommentMapper, OrderComment>
    implements OrderCommentService {

}




