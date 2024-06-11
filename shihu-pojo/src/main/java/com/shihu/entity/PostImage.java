package com.shihu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 帖子图片信息表
 * @TableName Post_Image
 */
@TableName(value ="Post_Image")
@Data
public class PostImage implements Serializable {
    /**
     * 图片ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 关联的帖子ID
     */
    private Integer postId;

    /**
     * 图片URL
     */
    private String imageUrl;

    /**
     * 上传时间
     */
    private Date uploadTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}