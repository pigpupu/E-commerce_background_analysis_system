package com.example.businessanalysis.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 客户地址表
 * @TableName t_customer_address
 */
@TableName(value ="t_customer_address")
@Data
public class CustomerAddress implements Serializable {
    /**
     * 客户ID
     */
    @TableId
    private String id;

    /**
     * 省ID
     */
    private Integer province;

    /**
     * 市ID
     */
    private Integer city;

    /**
     * 详细地址
     */
    private String detail;

    /**
     * 创建时间
     */
    private Date addTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 逻辑删除标志
     */
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}