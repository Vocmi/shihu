package com.shihu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 点赞信息表
 * @TableName User_Like
 */
@TableName(value ="User_Like")
@Data
public class UserLike implements Serializable {
    /**
     * 点赞记录ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 点赞用户ID
     */
    private Integer userId;

    /**
     * 被点赞的帖子ID
     */
    private Integer postId;

    /**
     * 点赞状态：1-点赞，0-取消点赞
     */
    private Integer isLiked;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}