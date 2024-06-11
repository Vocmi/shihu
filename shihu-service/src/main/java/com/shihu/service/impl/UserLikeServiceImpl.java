package com.shihu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihu.entity.UserLike;
import com.shihu.service.UserLikeService;
import com.shihu.mapper.UserLikeMapper;
import org.springframework.stereotype.Service;

/**
* @author 26867
* @description 针对表【User_Like(点赞信息表)】的数据库操作Service实现
* @createDate 2024-06-11 19:01:19
*/
@Service
public class UserLikeServiceImpl extends ServiceImpl<UserLikeMapper, UserLike>
    implements UserLikeService{

}




