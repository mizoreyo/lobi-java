package site.mizore.lobi.controller;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.mizore.lobi.api.CommonResult;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.entity.param.ArticleCreateParam;
import site.mizore.lobi.entity.param.ArticlePublishParam;
import site.mizore.lobi.entity.param.ArticleSaveParam;
import site.mizore.lobi.entity.po.Article;
import site.mizore.lobi.entity.vo.ArticleSummaryVO;
import site.mizore.lobi.entity.vo.ArticleVO;
import site.mizore.lobi.entity.vo.ArticleViewsVO;
import site.mizore.lobi.service.ArticleService;

import java.util.List;

@Api("ArticleController")
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @ApiOperation("新建文章")
    @PostMapping("/create")
    public CommonResult<Article> create(@Validated @RequestBody ArticleCreateParam param) {
        Article article = articleService.create(param);
        return CommonResult.success(article);
    }

    @ApiOperation("获取文章信息")
    @GetMapping("/info/{id}")
    public CommonResult<ArticleVO> getArticleInfo(@PathVariable("id") Long id) {
        ArticleVO articleVO = articleService.getArticleInfo(id);
        if (ObjectUtil.isNull(articleVO)) {
            return CommonResult.failed("文章不存在");
        }
        return CommonResult.success(articleVO);
    }

    @ApiOperation("用户删除文章")
    @DeleteMapping("/delete/{id}")
    public CommonResult deleteArticle(@PathVariable("id") Long id) {
        boolean result = articleService.deleteArticle(id);
        if (result) {
            return CommonResult.success(true);
        }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation("保存文章")
    @PutMapping("/save")
    public CommonResult saveArticle(@Validated @RequestBody ArticleSaveParam param) {
        Article article = articleService.saveArticle(param);
        return CommonResult.success(article);
    }

    @ApiOperation("发布文章")
    @PutMapping("/publish")
    public CommonResult publishArticle(@Validated @RequestBody ArticlePublishParam param) {
        Article article = articleService.publishArticle(param);
        return CommonResult.success(article);
    }

    @ApiOperation("向用户推荐文章")
    @GetMapping("/recommend")
    public CommonResult getRecommendList(@RequestParam("page") Integer page,
                                         @RequestParam("size") Integer size) {
        CommonPage<ArticleSummaryVO> summaryVOS = articleService.getRecommendList(page, size);
        return CommonResult.success(summaryVOS);
    }

    @ApiOperation("根据浏览量排序查询")
    @GetMapping("/list/views")
    public CommonResult getListByViews(@RequestParam(value = "limit", defaultValue = "5") Integer limit,
                                       @RequestParam("userId") Long userId) {
        List<ArticleViewsVO> articles = articleService.getListByViews(limit, userId);
        return CommonResult.success(articles);
    }

    @ApiOperation("查询分页")
    @GetMapping("/page")
    public CommonResult getPageByCondition(@RequestParam("page") Integer page,
                                           @RequestParam("size") Integer size,
                                           @RequestParam(value = "userId", required = false) Long userId,
                                           @RequestParam(value = "subjectId", required = false) Long subjectId,
                                           @RequestParam(value = "collectionId", required = false) Long collectionId) {
        CommonPage<ArticleSummaryVO> articlePage = articleService.getPageByCondition(page, size, userId, subjectId, collectionId);
        return CommonResult.success(articlePage);
    }

}
