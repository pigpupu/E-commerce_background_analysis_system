package com.example.businessanalysis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 基于购买的商品推荐表
 * @TableName t_recommend_planb
 */
@TableName(value ="t_recommend_planb")
@Data
public class RecommendPlanb implements Serializable {
    /**
     * 客户id
     */
    private String customerId;

    /**
     * 商品id
     */
    private String goodId;

    /**
     * 推荐指数
     */
    private Double score;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}