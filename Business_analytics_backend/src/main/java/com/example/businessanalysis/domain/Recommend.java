package com.example.businessanalysis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 基于购买的商品推荐算法
 * @TableName t_recommend
 */
@TableName(value ="t_recommend")
@Data
public class Recommend implements Serializable {
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