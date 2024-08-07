package com.shihu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihu.entity.Role;
import com.shihu.service.RoleService;
import com.shihu.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 26867
* @description 针对表【Role(角色信息表)】的数据库操作Service实现
* @createDate 2024-06-11 18:26:44
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

}




