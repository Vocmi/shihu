package com.shihu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihu.entity.OperationLog;
import com.shihu.service.OperationLogService;
import com.shihu.mapper.OperationLogMapper;
import org.springframework.stereotype.Service;

/**
* @author 26867
* @description 针对表【Operation_Log(操作日志表)】的数据库操作Service实现
* @createDate 2024-06-11 18:26:43
*/
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog>
    implements OperationLogService{

}




