package com.shihu.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shihu.entity.Post;
import com.shihu.entity.Report;
import com.shihu.entity.User;
import com.shihu.service.PostService;
import com.shihu.service.ReportService;
import com.shihu.service.UserService;
import com.shihu.vo.ArticleModerationVo;
import com.shihu.vo.ReportPageVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shihu.result.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author vocmi
 * @Email 2686782542@qq.com
 * @Date 2024-06-23
 */
@RestController
@RequestMapping("api/admin/v1")
@Slf4j
public class adminPostController {
    @Resource
    private PostService postService;

    @Resource
    private ReportService reportService;

    @Resource
    private UserService userService;

    @GetMapping("/articles")
    public Result getPageQuery(Integer page, Integer per_page) {
        //1.分页条件
        Page<Post> pageNo = Page.of(page, per_page);

        //2.排序条件
        pageNo.addOrder(OrderItem.asc("id"));

        LambdaQueryWrapper<Post> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();

        //3.分页查询
        Page<Post> pagePost = postService.page(pageNo,objectLambdaQueryWrapper.eq(Post::getStatus,0));

        //4.解析
        List<Post> records = pagePost.getRecords();
        long total = pagePost.getTotal();

        List<ArticleModerationVo> articleModerationVos = new ArrayList<>();

        records.forEach((post -> {
            ArticleModerationVo articleModerationVo = BeanUtil.copyProperties(post, ArticleModerationVo.class);
            Integer userId = post.getUserId();
            User user = userService.getById(userId);
            articleModerationVo.setUsername(user.getUsername());
            articleModerationVos.add(articleModerationVo);
        }));

        HashMap<String, Object> postPageMap = new HashMap<>();
        postPageMap.put("articles", articleModerationVos);
        postPageMap.put("total",total);

        return Result.success(postPageMap);
    }

    @GetMapping("/byHittingBack")
    @Transactional
    public Result byHittingBack(Integer postId,Integer status){
        Post post = postService.getById(postId);
        post.setStatus(status);
        postService.updateById(post);

        return Result.success();
    }

    @GetMapping("/reportPage")
    public Result reportPage(Integer page, Integer per_page) {
        //1.分页条件
        Page<Report> pageNo = Page.of(page, per_page);

        //2.排序条件
        pageNo.addOrder(OrderItem.asc("id"));

        LambdaQueryWrapper<Report> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();

        //3.分页查询
        Page<Report> pageReport = reportService.page(pageNo, objectLambdaQueryWrapper.eq(Report::getStatus, 0));

        //4.解析
        List<Report> records = pageReport.getRecords();
        long total = pageReport.getTotal();

        List<ReportPageVo> reportPageVos = new ArrayList<>();
        records.forEach((report -> {
            ReportPageVo reportPageVo = BeanUtil.copyProperties(report, ReportPageVo.class);
            Post post = postService.getById(report.getPostId());
            User user = userService.getById(report.getReporterId());
            reportPageVo.setInformer(user.getUsername());
            reportPageVo.setContent(post.getContent());
            reportPageVos.add(reportPageVo);
        }));

        HashMap<String, Object> reportPageMap = new HashMap<>();
        reportPageMap .put("articles", reportPageVos);
        reportPageMap .put("total",total);

        return Result.success(reportPageMap);
    }

    @GetMapping("/reportStatus")
    @Transactional
    public Result reportStatus(Integer reportId,Integer status){
        Report report = reportService.getById(reportId);
        report.setStatus(status);
        reportService.updateById(report);
        if (status == 1){
            Integer postId = report.getPostId();
            Post post = postService.getById(postId);
            post.setStatus(2);
            postService.updateById(post);
        }

        return Result.success();
    }
}
