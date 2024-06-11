package com.shihu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 操作日志表
 * @TableName Operation_Log
 */
@TableName(value ="Operation_Log")
@Data
public class OperationLog implements Serializable {
    /**
     * 操作日志ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 操作用户ID
     */
    private Integer userId;

    /**
     * 操作类型
     */
    private Object operationType;

    /**
     * 操作涉及的表名
     */
    private String targetTable;

    /**
     * 操作对象的ID
     */
    private Integer targetId;

    /**
     * 操作详情
     */
    private String details;

    /**
     * 操作时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}