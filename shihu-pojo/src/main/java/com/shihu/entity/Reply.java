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
 * 回复信息表
 * @TableName Reply
 */
@TableName(value ="Reply")
@Data
public class Reply implements Serializable {
    /**
     * 回复ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 所属帖子ID
     */
    private Integer postId;

    /**
     * 回复用户ID
     */
    private Integer userId;

    /**
     * 回复时间
     */
    private LocalDateTime createTime;

    /**
     * 回复状态：0-已删除，1-正常
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}