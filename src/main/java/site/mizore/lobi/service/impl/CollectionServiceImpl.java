package site.mizore.lobi.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.component.SecurityTool;
import site.mizore.lobi.entity.param.CollectionCreateParam;
import site.mizore.lobi.entity.param.CollectionEditParam;
import site.mizore.lobi.entity.po.Article;
import site.mizore.lobi.entity.po.Collection;
import site.mizore.lobi.entity.po.Subscribe;
import site.mizore.lobi.entity.po.User;
import site.mizore.lobi.entity.vo.CollectionCountVO;
import site.mizore.lobi.entity.vo.CollectionInfoVO;
import site.mizore.lobi.entity.vo.CollectionVO;
import site.mizore.lobi.entity.vo.UserInfoVO;
import site.mizore.lobi.enums.ResourceTypeEnum;
import site.mizore.lobi.exception.Asserts;
import site.mizore.lobi.mapper.ArticleMapper;
import site.mizore.lobi.mapper.CollectionMapper;
import site.mizore.lobi.mapper.SubscribeMapper;
import site.mizore.lobi.mapper.UserMapper;
import site.mizore.lobi.service.ArticleService;
import site.mizore.lobi.service.CollectionService;
import site.mizore.lobi.util.MarkdownUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements CollectionService {

    private final SecurityTool securityTool;

    private final ArticleMapper articleMapper;

    private final MarkdownUtil markdownUtil;

    private final UserMapper userMapper;

    private final SubscribeMapper subscribeMapper;

    private final ArticleService articleService;

    @Override
    public List<CollectionInfoVO> getInfoList() {
        User onlineUser = securityTool.getOnlineUser();
        QueryWrapper<Collection> collectionQueryWrapper = new QueryWrapper<>();
        collectionQueryWrapper.eq("creator", onlineUser.getId());
        List<Collection> collections = getBaseMapper().selectList(collectionQueryWrapper);
        List<Long> collectIds = collections.stream().map(Collection::getId).collect(Collectors.toList());
        List<Article> articles = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(collections)) {
            QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
            articleQueryWrapper.in("collection", collectIds);
            articles = articleMapper.selectList(articleQueryWrapper);
        }
        Map<Long, List<Article>> collectionArticleMap = articles.stream().collect(Collectors.groupingBy(Article::getCollection, Collectors.toList()));
        List<CollectionInfoVO> vos = collections.stream().map(collection -> {
            CollectionInfoVO collectionInfoVO = new CollectionInfoVO();
            BeanUtil.copyProperties(collection, collectionInfoVO);
            collectionInfoVO.setArticles(Optional.ofNullable(collectionArticleMap.get(collection.getId())).orElse(new ArrayList<>()));
            return collectionInfoVO;
        }).collect(Collectors.toList());
        return vos;
    }

    @Override
    @Transactional
    public Collection createCollection(CollectionCreateParam param) {
        User onlineUser = securityTool.getOnlineUser();
        Collection collection = new Collection();
        BeanUtil.copyProperties(param, collection);
        collection.setCreator(onlineUser.getId());
        save(collection);
        return collection;
    }

    @Override
    @Transactional
    public boolean deleteCollection(Long id) {
        User onlineUser = securityTool.getOnlineUser();
        List<Collection> collections = lambdaQuery().eq(Collection::getId, id).eq(Collection::getCreator, onlineUser.getId()).list();
        if (collections.isEmpty()) {
            Asserts.fail("找不到文集或者无权限删除！");
        }
        LambdaQueryChainWrapper<Article> lambdaQueryChainWrapper = new LambdaQueryChainWrapper<>(articleMapper);
        List<Article> articles = lambdaQueryChainWrapper.eq(Article::getCollection, id).select(Article::getId).list();
        if (!articles.isEmpty()) {
            Asserts.fail("存在未删除文章，无法删除文集");
        }
        // 删除关联数据
        LambdaUpdateChainWrapper<Subscribe> subLambdaUpdate = new LambdaUpdateChainWrapper<>(subscribeMapper);
        subLambdaUpdate.eq(Subscribe::getType, ResourceTypeEnum.COLLECTION).eq(Subscribe::getResource, id).remove();
        return removeById(id);
    }

    @Override
    public Collection editCollection(CollectionEditParam param) {
        User onlineUser = securityTool.getOnlineUser();
        List<Collection> collections = lambdaQuery().eq(Collection::getId, param.getId()).eq(Collection::getCreator, onlineUser.getId()).list();
        if (collections.isEmpty()) {
            Asserts.fail("找不到文集或者无权限修改！");
        }
        Collection collection = new Collection();
        BeanUtil.copyProperties(param, collection);
        updateById(collection);
        return collection;
    }

    @Override
    public CommonPage<CollectionCountVO> searchCollections(String q, Integer page, Integer size) {
        Page<Collection> collectionPage = Page.of(page, size);
        Page<Collection> resPage = lambdaQuery().like(Collection::getName, q).page(collectionPage);
        CommonPage<CollectionCountVO> finalPage = new CommonPage<>();
        finalPage.setTotal(resPage.getTotal());
        finalPage.setData(resPage.getRecords().stream().map(collection -> {
            CollectionCountVO countVO = new CollectionCountVO();
            BeanUtil.copyProperties(collection, countVO);
            LambdaQueryChainWrapper<Article> articleLambdaQuery = new LambdaQueryChainWrapper<>(articleMapper);
            LambdaQueryChainWrapper<Subscribe> subscribeLambdaQuery = new LambdaQueryChainWrapper<>(subscribeMapper);
            Long articleCount = articleLambdaQuery.eq(Article::getPublish, 1).eq(Article::getCollection, collection.getId()).count();
            Long subCount = subscribeLambdaQuery.eq(Subscribe::getType, ResourceTypeEnum.COLLECTION).eq(Subscribe::getResource, collection.getId()).count();
            countVO.setArticleCount(articleCount);
            countVO.setSubCount(subCount);
            return countVO;
        }).collect(Collectors.toList()));
        return finalPage;
    }

    @Override
    public CollectionCountVO collectionCountInfo(Long id) {
        Collection collection = getById(id);
        if (ObjectUtil.isNull(collection)) {
            Asserts.fail("找不到文集");
        }
        CollectionCountVO countVO = new CollectionCountVO();
        BeanUtil.copyProperties(collection, countVO);
        // 查询文章数
        LambdaQueryChainWrapper<Article> aLambdaQuery = new LambdaQueryChainWrapper<>(articleMapper);
        Long articleCount = aLambdaQuery.eq(Article::getPublish, 1).eq(Article::getCollection, id).count();
        // 查询关注数
        LambdaQueryChainWrapper<Subscribe> subscribeLambdaQuery = new LambdaQueryChainWrapper<>(subscribeMapper);
        Long subCount = subscribeLambdaQuery.eq(Subscribe::getType, ResourceTypeEnum.COLLECTION).eq(Subscribe::getResource, id).count();
        countVO.setSubCount(subCount);
        // 查询用户
        User user = userMapper.selectById(collection.getCreator());
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(user, userInfoVO);
        // 组装
        countVO.setArticleCount(articleCount);
        countVO.setCreator(userInfoVO);
        return countVO;
    }

    @Override
    public List<CollectionCountVO> listByIds(List<Long> ids) {
        if (ObjectUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<Collection> collections = lambdaQuery().in(Collection::getId, ids).list();
        List<CollectionCountVO> collectionCountVOS = collections.stream().map(collection -> {
            CollectionCountVO countVO = new CollectionCountVO();
            BeanUtil.copyProperties(collection, countVO);
            LambdaQueryChainWrapper<Article> articleLambdaQuery = new LambdaQueryChainWrapper<>(articleMapper);
            LambdaQueryChainWrapper<Subscribe> subscribeLambdaQuery = new LambdaQueryChainWrapper<>(subscribeMapper);
            Long articleCount = articleLambdaQuery.eq(Article::getPublish, 1).eq(Article::getCollection, collection.getId()).count();
            Long subCount = subscribeLambdaQuery.eq(Subscribe::getType, ResourceTypeEnum.COLLECTION).eq(Subscribe::getResource, collection.getId()).count();
            countVO.setArticleCount(articleCount);
            countVO.setSubCount(subCount);
            return countVO;
        }).collect(Collectors.toList());
        return collectionCountVOS;
    }

    @Override
    public CommonPage<CollectionVO> page(Integer page, Integer size, String q) {
        Page<Collection> queryPage = Page.of(page, size);
        LambdaQueryChainWrapper<Collection> collectionLambdaQuery = lambdaQuery();
        if (StrUtil.isNotBlank(q)) {
            collectionLambdaQuery.like(Collection::getName, q);
        }
        Page<Collection> collectionPage = collectionLambdaQuery.page(queryPage);
        CommonPage<CollectionVO> commonPage = new CommonPage<>();
        commonPage.setTotal(collectionPage.getTotal());
        List<CollectionVO> data = collectionPage.getRecords().stream().map(this::collectionToVO).collect(Collectors.toList());
        commonPage.setData(data);
        return commonPage;
    }

    @Override
    public CollectionVO get(Long id) {
        Collection collection = getById(id);
        if (collection == null) {
            Asserts.fail("不存在文集");
        }
        return collectionToVO(collection);
    }

    @Override
    public void updateCollection(CollectionEditParam param) {
        Collection collection = new Collection();
        BeanUtil.copyProperties(param, collection);
        updateById(collection);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 删除关联数据
        LambdaUpdateChainWrapper<Subscribe> subLambdaUpdate = new LambdaUpdateChainWrapper<>(subscribeMapper);
        subLambdaUpdate.eq(Subscribe::getType, ResourceTypeEnum.COLLECTION).eq(Subscribe::getResource, id).remove();
        // 删除文章
        LambdaQueryChainWrapper<Article> articleLambdaQuery = new LambdaQueryChainWrapper<>(articleMapper);
        List<Long> articleIds = articleLambdaQuery.eq(Article::getCollection, id).list().stream().map(Article::getId).collect(Collectors.toList());
        articleIds.forEach(articleService::delete);
        removeById(id);
    }

    private CollectionVO collectionToVO(Collection collection) {
        CollectionVO collectionVO = new CollectionVO();
        BeanUtil.copyProperties(collection, collectionVO);
        User user = userMapper.selectById(collection.getCreator());
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(user, userInfoVO);
        collectionVO.setCreator(userInfoVO);
        return collectionVO;
    }

}
