package com.shihu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shihu.entity.Report;
import com.shihu.service.ReportService;
import com.shihu.mapper.ReportMapper;
import org.springframework.stereotype.Service;

/**
* @author 26867
* @description 针对表【Report(举报信息表)】的数据库操作Service实现
* @createDate 2024-06-11 18:26:44
*/
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report>
    implements ReportService{

}




