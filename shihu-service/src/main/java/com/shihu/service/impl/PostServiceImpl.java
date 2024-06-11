package com.shihu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihu.entity.Post;
import com.shihu.service.PostService;
import com.shihu.mapper.PostMapper;
import org.springframework.stereotype.Service;

/**
* @author 26867
* @description 针对表【Post(帖子信息表)】的数据库操作Service实现
* @createDate 2024-06-11 18:26:43
*/
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService{

}




