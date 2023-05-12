package site.mizore.lobi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.entity.param.ArticleCreateParam;
import site.mizore.lobi.entity.param.ArticlePublishParam;
import site.mizore.lobi.entity.param.ArticleSaveParam;
import site.mizore.lobi.entity.po.Article;
import site.mizore.lobi.entity.vo.ArticleSummaryVO;
import site.mizore.lobi.entity.vo.ArticleVO;
import site.mizore.lobi.entity.vo.ArticleViewsVO;

import java.util.List;

public interface ArticleService extends IService<Article> {

    /**
     * 获取文章信息
     *
     * @param id
     * @return
     */
    ArticleVO getArticleInfo(Long id);

    /**
     * 创建文章
     *
     * @param param
     * @return
     */
    Article create(ArticleCreateParam param);

    /**
     * 用户删除文章
     *
     * @param id
     * @return
     */
    boolean deleteArticle(Long id);

    /**
     * 用户保存文章
     *
     * @param param
     * @return
     */
    Article saveArticle(ArticleSaveParam param);

    /**
     * 发布文章
     *
     * @param param
     * @return
     */
    Article publishArticle(ArticlePublishParam param);

    /**
     * 获取推荐文章
     *
     * @return
     */
    CommonPage<ArticleSummaryVO> getRecommendList(Integer page, Integer size);

    /**
     * 根据浏览量排序查询文章
     *
     * @param limit
     * @param userId
     * @return
     */
    List<ArticleViewsVO> getListByViews(Integer limit, Long userId);

    /**
     * 获取文章分页
     *
     * @param page
     * @param size
     * @param userId
     * @param subjectId
     * @param collectionId
     * @return
     */
    CommonPage<ArticleSummaryVO> getPageByCondition(Integer page, Integer size, Long userId, Long subjectId, Long collectionId);

    /**
     * 根据id列表获取
     *
     * @param ids
     * @return
     */
    List<ArticleSummaryVO> listByIds(List<Long> ids);
}
