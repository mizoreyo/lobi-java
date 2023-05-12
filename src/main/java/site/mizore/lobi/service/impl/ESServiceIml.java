package site.mizore.lobi.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.stereotype.Service;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.entity.dto.ArticleESDto;
import site.mizore.lobi.entity.po.Article;
import site.mizore.lobi.entity.po.User;
import site.mizore.lobi.entity.vo.ArticleSummaryVO;
import site.mizore.lobi.entity.vo.UserInfoVO;
import site.mizore.lobi.mapper.ArticleMapper;
import site.mizore.lobi.mapper.UserMapper;
import site.mizore.lobi.service.ESService;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ESServiceIml implements ESService {

    public static String articleIndex = "lobi_article";

    private final RestHighLevelClient restHighLevelClient;

    private final ArticleMapper articleMapper;

    private final UserMapper userMapper;

    @Override
    public void saveArticle(ArticleESDto dto) {
        IndexRequest indexRequest = new IndexRequest(articleIndex);
        indexRequest.source(JSONUtil.toJsonStr(dto), XContentType.JSON);
        indexRequest.id(String.valueOf(dto.getId()));
        indexRequest.create(true);
        try {
            IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateArticle(ArticleESDto dto) {
        UpdateRequest updateRequest = new UpdateRequest(articleIndex, String.valueOf(dto.getId()));
        Map<String, Object> kvs = new HashMap<>();
        kvs.put("id", dto.getId());
        kvs.put("title", dto.getTitle());
        kvs.put("content", dto.getContent());
        updateRequest.doc(kvs);
        try {
            restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteArticle(Long id) {
        DeleteRequest deleteRequest = new DeleteRequest(articleIndex, String.valueOf(id));
        try {
            restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CommonPage<ArticleSummaryVO> searchArticles(String q, Integer page, Integer size) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(articleIndex);
        // 构造查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(q, "title", "content");
        searchSourceBuilder.query(multiMatchQueryBuilder);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title").preTags("<span class='colored'>").postTags("</span>");
        highlightBuilder.field("content").preTags("<span class='colored'>").postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.from((page - 1) * size);
        searchSourceBuilder.size(size);
        searchRequest.source(searchSourceBuilder);
        //执行查询
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //解析查询结果
        List<ArticleSummaryVO> articleSummaryVOS = Arrays.stream(searchResponse.getHits().getHits()).map(hit -> {
            ArticleSummaryVO summaryVO = new ArticleSummaryVO();
            Map<String, Object> sourceMap = hit.getSourceAsMap();
            summaryVO.setId(Long.parseLong(sourceMap.get("id").toString()));
            summaryVO.setTitle((String) sourceMap.get("title"));
            String content = (String) sourceMap.get("content");
            if (content.length() > 150) {
                content = content.substring(0, 150);
            }
            summaryVO.setSummary(content);
            // 高亮替换
            if (ObjectUtil.isNotNull(hit.getHighlightFields().get("title"))) {
                List<String> titleHighlightTexts = Arrays.stream(hit.getHighlightFields().get("title").getFragments()).map(Text::string).collect(Collectors.toList());
                String title = StringUtils.join(titleHighlightTexts, "...");
                summaryVO.setTitle(title);
            }
            if (ObjectUtil.isNotNull(hit.getHighlightFields().get("content"))) {
                List<String> contentHighlightTexts = Arrays.stream(hit.getHighlightFields().get("content").getFragments()).map(Text::string).collect(Collectors.toList());
                String summary = StringUtils.join(contentHighlightTexts, "...");
                summaryVO.setSummary(summary);
            }
            // 查询作者信息
            Article article = articleMapper.selectById(summaryVO.getId());
            User user = userMapper.selectById(article.getAuthor());
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtil.copyProperties(user, userInfoVO);
            summaryVO.setAuthorInfo(userInfoVO);
            return summaryVO;
        }).collect(Collectors.toList());
        CommonPage<ArticleSummaryVO> esPage = new CommonPage<>();
        esPage.setData(articleSummaryVOS);
        esPage.setTotal(searchResponse.getHits().getTotalHits().value);
        return esPage;
    }

}
