package com.shihu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 举报信息表
 * @TableName Report
 */
@TableName(value ="Report")
@Data
public class Report implements Serializable {
    /**
     * 举报ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 举报人ID
     */
    private Integer reporterId;

    /**
     * 被举报的帖子ID
     */
    private Integer postId;

    /**
     * 举报理由
     */
    private String reason;

    /**
     * 举报时间
     */
    private LocalDateTime createTime;

    /**
     * 处理状态：0-未处理，1-举报成功，2-举报失败
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}