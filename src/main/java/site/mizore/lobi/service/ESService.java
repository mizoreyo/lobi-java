package site.mizore.lobi.service;

import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.entity.dto.ArticleESDto;
import site.mizore.lobi.entity.vo.ArticleSummaryVO;

public interface ESService {

    /**
     * 新建文章
     *
     * @param dto
     */
    void saveArticle(ArticleESDto dto);

    /**
     * 更新文章
     *
     * @param dto
     */
    void updateArticle(ArticleESDto dto);

    /**
     * 删除文章
     *
     * @param id
     */
    void deleteArticle(Long id);

    /**
     * 搜索文章
     *
     * @param q
     * @return
     */
    CommonPage<ArticleSummaryVO> searchArticles(String q, Integer page, Integer size);

}
