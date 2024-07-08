package com.shihu.controller.user;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shihu.dto.*;
import com.shihu.entity.*;
import com.shihu.mapper.PostMapper;
import com.shihu.mapper.ReplyMapper;
import com.shihu.mapper.UserLikeMapper;
import com.shihu.mapper.UserMapper;
import com.shihu.service.*;
import com.shihu.util.UserHolder;
import com.shihu.vo.PostCommentVo;
import com.shihu.vo.PostDetailsVo;
import com.shihu.vo.PostPageVo;
import com.shihu.vo.PostReportsVo;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import shihu.constant.MessageConstant;
import shihu.exception.ClientException;
import shihu.result.Result;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/user/v1")
@Slf4j
public class PostController {

    @Resource
    private PostService postService;

    @Resource
    private UserLikeService userLikeService;

    @Resource
    private PostMapper postMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private ReplyService replyService;

    @Resource
    private ReportService reportService;

    @Resource
    private UserLikeMapper userLikeMapper;


    @GetMapping("/articles")
    public Result getPageQuery(Integer page, Integer per_page) {
        //1.分页条件
        Page<Post> pageNo = Page.of(page, per_page);

        //2.排序条件
        pageNo.addOrder(OrderItem.asc("id"));

        LambdaQueryWrapper<Post> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();

        //3.分页查询
        Page<Post> pagePost = postService.page(pageNo,objectLambdaQueryWrapper.eq(Post::getStatus,1));

        //4.解析
        List<Post> records = pagePost.getRecords();
        List<PostPageVo> postPageVos = new ArrayList<>();

        records.forEach((post -> {
            PostPageVo postPageVo = BeanUtil.copyProperties(post, PostPageVo.class);
            postPageVos.add(postPageVo);
        }));

        HashMap<String, List<PostPageVo>> postPageMap = new HashMap<>();
        postPageMap.put("articles", postPageVos);

        return Result.success(postPageMap);
    }

    @PostMapping("/submitText")
    @Transactional
    public Result postSubmitText(@RequestBody PostSubmitTextDto postSubmitTextDto) {
        Post post = BeanUtil.copyProperties(postSubmitTextDto, Post.class);

        Integer id = UserHolder.getUser().getId();
        post.setUserId(id);
        post.setCreateTime(LocalDateTime.now());
        post.setStatus(0);

        postService.save(post);

        return Result.success();
    }

    @GetMapping("/postDetails")
    @Transactional
    public Result postDetail(Integer id) {
        Post post = postMapper.selectById(id);
        Integer userId = post.getUserId();

        //修改浏览次数加一,并保存到数据库中
        post.setViewCount(post.getViewCount() + 1);
        postService.updateById(post);

        User user = userMapper.selectById(userId);
        String username = user.getUsername();

        PostDetailsVo postDetailsVo = BeanUtil.copyProperties(post, PostDetailsVo.class);
        postDetailsVo.setUsername(username);
        postDetailsVo.setUpdateTime(LocalDateTime.now());

        if (BeanUtil.isEmpty(post)) {
            throw new ClientException(MessageConstant.ID_NOT_FOUND);
        }

        //获取当前登录用户的userId
        UserDto userDto = UserHolder.getUser();
        Integer userDtoId = userDto.getId();
        LambdaQueryWrapper<UserLike> userLikeLambdaQueryWrapper = new LambdaQueryWrapper<>();

        //判断表中是否有点赞信息，如果没有，加入，默认为0
        Long count = userLikeMapper.selectCount(userLikeLambdaQueryWrapper.eq(UserLike::getUserId, userDtoId).eq(UserLike::getPostId, id));
        if (count == 0){
            UserLike userLike = new UserLike();
            userLike.setUserId(userDtoId);
            userLike.setPostId(id);
            userLike.setIsLiked(0);
            userLikeService.save(userLike);
            postDetailsVo.setIsLiked(0);
        }else {
            //如果有进行查询
            UserLike userLike = userLikeService.getOne(userLikeLambdaQueryWrapper.eq(UserLike::getUserId, userDtoId).eq(UserLike::getPostId, id));
            postDetailsVo.setIsLiked(userLike.getIsLiked());
        }

        return Result.success(postDetailsVo);
    }

    @PostMapping("/like")
    @Transactional
    public Result postLike(@RequestBody UserLikeDto userLikeDto) {
        Integer postId = userLikeDto.getPostId();
        Integer userId = userLikeDto.getUserId();
        Integer isLike = userLikeDto.getIsLike();

        LambdaQueryWrapper<UserLike> userLikeDtoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        UserLike userLike = userLikeService.getOne(userLikeDtoLambdaQueryWrapper.eq(UserLike::getPostId, postId).eq(UserLike::getUserId,userId));

        userLike.setIsLiked(isLike);
        userLikeService.updateById(userLike);

        LambdaQueryWrapper<Post> postLambdaQueryWrapper = new LambdaQueryWrapper<>();

        Post post = postService.getOne(postLambdaQueryWrapper.eq(Post::getId, postId));

        Integer likeCount = post.getLikeCount();

        if (userLikeDto.getIsLike() == 0) {
            likeCount = likeCount - 1;
        } else {
            likeCount = likeCount + 1;
        }

        post.setLikeCount(likeCount);

        postService.updateById(post);

        return Result.success();
    }

    @GetMapping("/getComment")
    public Result GetComment(Integer postId) {
        List<PostCommentVo> postCommentVoList = new ArrayList<>();

        LambdaQueryWrapper<Reply> replyLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Reply> replies = replyMapper.selectList(replyLambdaQueryWrapper.eq(Reply::getPostId, postId));

        for (Reply reply : replies) {
            PostCommentVo postCommentVo = BeanUtil.copyProperties(reply, PostCommentVo.class);
            Integer id = reply.getUserId();
            User user = userMapper.selectById(id);
            postCommentVo.setUsername(user.getUsername());
            postCommentVoList.add(postCommentVo);
        }
        return Result.success(postCommentVoList);
    }

    @PostMapping("/submitComment")
    @Transactional
    public Result postSubmitComment(@RequestBody PostSubmitCommentDto postSubmitCommentDto) {
        Integer postId = postSubmitCommentDto.getPostId();
        Post post = postMapper.selectById(postId);

        //修改评论次数加一,并保存到数据库中
        post.setReplyCount(post.getReplyCount() + 1);
        postService.updateById(post);

        Reply reply = BeanUtil.copyProperties(postSubmitCommentDto, Reply.class);

        reply.setCreateTime(LocalDateTime.now());

        replyService.save(reply);
        return Result.success();
    }

    @PostMapping("/reports")
    @Transactional
    public Result postReports(@RequestBody PostReportsVo postReportsVo) {
        Report report = BeanUtil.copyProperties(postReportsVo, Report.class);

        report.setCreateTime(LocalDateTime.now());

        reportService.save(report);

        return Result.success();
    }
}
