package site.mizore.lobi.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.component.SecurityTool;
import site.mizore.lobi.entity.dto.ArticleESDto;
import site.mizore.lobi.entity.param.ArticleCreateParam;
import site.mizore.lobi.entity.param.ArticlePublishParam;
import site.mizore.lobi.entity.param.ArticleSaveParam;
import site.mizore.lobi.entity.po.*;
import site.mizore.lobi.entity.vo.*;
import site.mizore.lobi.enums.ResourceTypeEnum;
import site.mizore.lobi.exception.Asserts;
import site.mizore.lobi.mapper.*;
import site.mizore.lobi.service.ArticleService;
import site.mizore.lobi.service.ESService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final UserMapper userMapper;

    private final SubjectMapper subjectMapper;

    private final ESService esService;

    private final SubscribeMapper subscribeMapper;

    private final ThumbMapper thumbMapper;

    private final SecurityTool securityTool;

    @Override
    @Transactional
    public ArticleVO getArticleInfo(Long id) {
        Article article = getBaseMapper().selectById(id);
        if (ObjectUtil.isNull(article)) {
            return null;
        }
        ArticleVO articleVO = new ArticleVO();
        BeanUtil.copyProperties(article, articleVO);
        // 用户信息
        User user = userMapper.selectById(article.getAuthor());
        UserCountVO countVO = new UserCountVO();
        BeanUtil.copyProperties(user, countVO);
        LambdaQueryChainWrapper<Subscribe> subscribeLambdaQuery = new LambdaQueryChainWrapper<>(subscribeMapper);
        Long articleCount = lambdaQuery().eq(Article::getPublish, 1).eq(Article::getAuthor, user.getId()).count();
        countVO.setArticleCount(articleCount);
        Long subCount = subscribeLambdaQuery.eq(Subscribe::getType, ResourceTypeEnum.USER).eq(Subscribe::getResource, user.getId()).count();
        countVO.setSubCount(subCount);
        articleVO.setAuthorInfo(countVO);
        Subject subject = subjectMapper.selectById(article.getSubject());
        articleVO.setSubjectInfo(subject);
        // 浏览量+1
        Article updateArticle = new Article();
        updateArticle.setViews(article.getViews() + 1);
        updateArticle.setId(article.getId());
        updateById(updateArticle);
        return articleVO;
    }

    @Override
    @Transactional
    public Article create(ArticleCreateParam param) {
        Article article = new Article();
        BeanUtil.copyProperties(param, article);
        User onlineUser = securityTool.getOnlineUser();
        article.setAuthor(onlineUser.getId());
        article.setUpdated(LocalDateTime.now());
        save(article);
        return article;
    }

    @Override
    @Transactional
    public boolean deleteArticle(Long id) {
        User onlineUser = securityTool.getOnlineUser();
        List<Article> articles = lambdaQuery().eq(Article::getAuthor, onlineUser.getId()).eq(Article::getId, id).list();
        if (articles.isEmpty()) {
            Asserts.fail("找不到文章或者无权限删除");
        }
        // es删除
        esService.deleteArticle(id);
        // 删除关联数据
        LambdaUpdateChainWrapper<Subscribe> subLambdaUpdate = new LambdaUpdateChainWrapper<>(subscribeMapper);
        subLambdaUpdate.eq(Subscribe::getType, ResourceTypeEnum.ARTICLE).eq(Subscribe::getResource, id).remove();
        LambdaUpdateChainWrapper<Thumb> thumbLambdaUpdate = new LambdaUpdateChainWrapper<>(thumbMapper);
        thumbLambdaUpdate.eq(Thumb::getArticle, id).remove();
        return removeById(id);
    }

    @Override
    @Transactional
    public Article saveArticle(ArticleSaveParam param) {
        User onlineUser = securityTool.getOnlineUser();
        List<Article> articles = lambdaQuery().eq(Article::getAuthor, onlineUser.getId()).eq(Article::getId, param.getId()).list();
        if (articles.isEmpty()) {
            Asserts.fail("找不到文章或者无权限修改");
        }
        Article article = new Article();
        BeanUtil.copyProperties(param, article);
        article.setUpdated(LocalDateTime.now());
        updateById(article);
        // 只有发布的文章才更新es
        if (articles.get(0).getPublish().equals(1)) {
            // 更新es
            ArticleESDto dto = new ArticleESDto();
            BeanUtil.copyProperties(article, dto);
            dto.setContent(markDownToText(dto.getContent()));
            esService.updateArticle(dto);
        }
        return article;
    }

    @Override
    public Article publishArticle(ArticlePublishParam param) {
        User onlineUser = securityTool.getOnlineUser();
        List<Article> articles = lambdaQuery().eq(Article::getAuthor, onlineUser.getId()).eq(Article::getId, param.getId()).list();
        if (articles.isEmpty()) {
            Asserts.fail("找不到文章或者无权限发布");
        }
        if (articles.get(0).getPublish() == 1) {
            Asserts.fail("文章已发布，请勿重复发布");
        }
        Article article = new Article();
        BeanUtil.copyProperties(param, article);
        article.setPublish(1);
        article.setDate(LocalDateTime.now());
        article.setUpdated(LocalDateTime.now());
        // 保存至es
        ArticleESDto articleESDto = new ArticleESDto();
        BeanUtil.copyProperties(article, articleESDto);
        articleESDto.setContent(markDownToText(articleESDto.getContent()));
        esService.saveArticle(articleESDto);
        updateById(article);
        return article;
    }

    @Override
    public CommonPage<ArticleSummaryVO> getRecommendList(Integer page, Integer size) {
        Page<Article> queryPage = Page.of(page, size);
        Page<Article> articlePage = lambdaQuery().eq(Article::getPublish, 1).orderByDesc(Article::getDate).page(queryPage);
        List<ArticleSummaryVO> articleSummaryVOS = articlePage.getRecords().stream().map(article -> {
            ArticleSummaryVO summaryVO = new ArticleSummaryVO();
            BeanUtil.copyProperties(article, summaryVO);
            summaryVO.setSummary(getSummary(article.getContent()));
            User user = userMapper.selectById(article.getAuthor());
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtil.copyProperties(user, userInfoVO);
            summaryVO.setAuthorInfo(userInfoVO);
            return summaryVO;
        }).collect(Collectors.toList());
        CommonPage<ArticleSummaryVO> finalPage = new CommonPage<>();
        finalPage.setTotal(articlePage.getTotal());
        finalPage.setData(articleSummaryVOS);
        return finalPage;
    }

    @Override
    public List<ArticleViewsVO> getListByViews(Integer limit, Long userId) {
        List<Article> articles = lambdaQuery().eq(Article::getAuthor, userId).orderByDesc(Article::getViews).last("limit " + limit).list();
        return articles.stream().map(article -> {
            ArticleViewsVO articleViewsVO = new ArticleViewsVO();
            BeanUtil.copyProperties(article, articleViewsVO);
            return articleViewsVO;
        }).collect(Collectors.toList());
    }

    @Override
    public CommonPage<ArticleSummaryVO> getPageByCondition(Integer page, Integer size, Long userId, Long subjectId, Long collectionId) {
        Page<Article> queryPage = Page.of(page, size);
        LambdaQueryChainWrapper<Article> lambdaQuery = lambdaQuery().eq(Article::getPublish, 1);
        if (ObjectUtil.isNotNull(userId)) {
            lambdaQuery = lambdaQuery.eq(Article::getAuthor, userId);
        }
        if (ObjectUtil.isNotNull(subjectId)) {
            lambdaQuery = lambdaQuery.eq(Article::getSubject, subjectId);
        }
        if (ObjectUtil.isNotNull(collectionId)) {
            lambdaQuery = lambdaQuery.eq(Article::getCollection, collectionId);
        }
        Page<Article> resultPage = lambdaQuery.orderByDesc(Article::getDate).page(queryPage);
        CommonPage<ArticleSummaryVO> finalPage = new CommonPage<>();
        finalPage.setTotal(resultPage.getTotal());
        List<ArticleSummaryVO> articleSummaryVOS = resultPage.getRecords().stream().map(article -> {
            ArticleSummaryVO summaryVO = new ArticleSummaryVO();
            BeanUtil.copyProperties(article, summaryVO);
            summaryVO.setSummary(getSummary(article.getContent()));
            User user = userMapper.selectById(article.getAuthor());
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtil.copyProperties(user, userInfoVO);
            summaryVO.setAuthorInfo(userInfoVO);
            return summaryVO;
        }).collect(Collectors.toList());
        finalPage.setData(articleSummaryVOS);
        return finalPage;
    }

    @Override
    public List<ArticleSummaryVO> listByIds(List<Long> ids) {
        if (ObjectUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<Article> articles = lambdaQuery().in(Article::getId, ids).list();
        List<ArticleSummaryVO> articleSummaryVOS = articles.stream().map(article -> {
            ArticleSummaryVO summaryVO = new ArticleSummaryVO();
            BeanUtil.copyProperties(article, summaryVO);
            summaryVO.setSummary(getSummary(article.getContent()));
            User user = userMapper.selectById(article.getAuthor());
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtil.copyProperties(user, userInfoVO);
            summaryVO.setAuthorInfo(userInfoVO);
            return summaryVO;
        }).collect(Collectors.toList());
        return articleSummaryVOS;
    }

    /**
     * 获取摘要
     *
     * @param markdownText
     * @return
     */
    public String getSummary(String markdownText) {
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String html = renderer.render(parser.parse(markdownText));
        // remove html tags
        String plainText = Jsoup.parse(html).text();
        if (plainText.length() > 150) {
            return plainText.substring(0, 150) + "...";
        }
        return plainText;
    }

    /**
     * markdown转纯文本
     *
     * @param markdown
     * @return
     */
    public String markDownToText(String markdown) {
        if (ObjectUtil.isNull(markdown)) {
            return null;
        }
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String html = renderer.render(parser.parse(markdown));
        // remove html tags
        return Jsoup.parse(html).text();
    }

}
