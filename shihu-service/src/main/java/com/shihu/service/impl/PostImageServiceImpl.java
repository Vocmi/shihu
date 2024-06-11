package com.shihu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihu.entity.PostImage;
import com.shihu.service.PostImageService;
import com.shihu.mapper.PostImageMapper;
import org.springframework.stereotype.Service;

/**
* @author 26867
* @description 针对表【Post_Image(帖子图片信息表)】的数据库操作Service实现
* @createDate 2024-06-11 18:26:43
*/
@Service
public class PostImageServiceImpl extends ServiceImpl<PostImageMapper, PostImage>
    implements PostImageService{

}




