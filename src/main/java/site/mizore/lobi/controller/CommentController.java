package site.mizore.lobi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.mizore.lobi.api.CommonResult;
import site.mizore.lobi.entity.param.CommentCreateParam;
import site.mizore.lobi.entity.po.Comment;
import site.mizore.lobi.entity.vo.CommentVO;
import site.mizore.lobi.service.CommentService;

import java.util.List;

@Api("CommentController")
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @ApiOperation("新建评论")
    @PostMapping("/create")
    public CommonResult createComment(@Validated @RequestBody CommentCreateParam param) {
        Comment comment = commentService.createComment(param);
        return CommonResult.success(comment);
    }

    @ApiOperation("获取评论树")
    @GetMapping("/tree")
    public CommonResult getCommentTree(@RequestParam("article") Long article) {
        List<CommentVO> tree = commentService.getCommentTree(article);
        return CommonResult.success(tree);
    }

}
