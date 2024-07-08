package com.shihu.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author vocmi
 * @Email 2686782542@qq.com
 * @Date 2024-06-23
 */
@Data
@NoArgsConstructor
public class ReportPageVo {
    private Integer id;
    private String reason;
    private String informer;
    private String content;
    private LocalDateTime createTime;
    private Integer status;
}
