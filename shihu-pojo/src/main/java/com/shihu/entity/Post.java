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
 * 帖子信息表
 * @TableName Post
 */
@TableName(value ="Post")
@Data
public class Post implements Serializable {
    /**
     * 帖子ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 发帖用户ID
     */
    private Integer userId;

    /**
     * 发帖时间
     */
    private LocalDateTime createTime;

    /**
     * 最后编辑时间
     */
    private LocalDateTime updateTime;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 回复数
     */
    private Integer replyCount;

    /**
     * 帖子状态：0-待审核，1-正常，2-打回
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}