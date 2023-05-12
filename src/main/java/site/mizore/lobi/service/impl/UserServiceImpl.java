package site.mizore.lobi.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.component.SecurityTool;
import site.mizore.lobi.entity.param.UserEditParam;
import site.mizore.lobi.entity.po.*;
import site.mizore.lobi.entity.vo.UserCountVO;
import site.mizore.lobi.entity.vo.UserInfoVO;
import site.mizore.lobi.enums.ResourceTypeEnum;
import site.mizore.lobi.exception.Asserts;
import site.mizore.lobi.mapper.*;
import site.mizore.lobi.service.UserService;
import site.mizore.lobi.util.MarkdownUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final ArticleMapper articleMapper;

    private final CollectionMapper collectionMapper;

    private final SubjectMapper subjectMapper;

    private final MarkdownUtil markdownUtil;

    private final SecurityTool securityTool;

    private final SubscribeMapper subscribeMapper;

    @Override
    public UserInfoVO getUserInfo(Long id) {
        UserInfoVO userInfoVO = new UserInfoVO();
        User user = getBaseMapper().selectById(id);
        BeanUtil.copyProperties(user, userInfoVO);
        return userInfoVO;
    }

    @Override
    public CommonPage<UserCountVO> searchUsers(String q, Integer page, Integer size) {
        Page<User> userPage = Page.of(page, size);
        Page<User> resPage = lambdaQuery().like(User::getNickname, q).page(userPage);
        CommonPage<UserCountVO> finalPage = new CommonPage<>();
        finalPage.setTotal(resPage.getTotal());
        finalPage.setData(resPage.getRecords().stream().map(user -> {
            UserCountVO countVO = new UserCountVO();
            BeanUtil.copyProperties(user, countVO);
            LambdaQueryChainWrapper<Article> articleLambdaQuery = new LambdaQueryChainWrapper<>(articleMapper);
            LambdaQueryChainWrapper<Subscribe> subscribeLambdaQuery = new LambdaQueryChainWrapper<>(subscribeMapper);
            Long articleCount = articleLambdaQuery.eq(Article::getPublish, 1).eq(Article::getAuthor, user.getId()).count();
            Long subCount = subscribeLambdaQuery.eq(Subscribe::getType, ResourceTypeEnum.USER).eq(Subscribe::getResource, user.getId()).count();
            countVO.setArticleCount(articleCount);
            countVO.setSubCount(subCount);
            return countVO;
        }).collect(Collectors.toList()));
        return finalPage;
    }

    @Override
    public UserCountVO userCountInfo(Long id) {
        // 用户信息
        User user = getById(id);
        if (ObjectUtil.isNull(user)) {
            Asserts.fail("找不到用户");
        }
        // 文章数
        LambdaQueryChainWrapper<Article> aLambdaQuery = new LambdaQueryChainWrapper<>(articleMapper);
        Long articleCount = aLambdaQuery.eq(Article::getPublish, 1).eq(Article::getAuthor, id).count();
        // 粉丝数
        LambdaQueryChainWrapper<Subscribe> s2LambdaQuery = new LambdaQueryChainWrapper<>(subscribeMapper);
        Long subCount = s2LambdaQuery.eq(Subscribe::getType, ResourceTypeEnum.USER).eq(Subscribe::getResource, id).count();
        // 关注数
        LambdaQueryChainWrapper<Subscribe> s1LambdaQuery = new LambdaQueryChainWrapper<>(subscribeMapper);
        Long followCount = s1LambdaQuery.eq(Subscribe::getType, ResourceTypeEnum.USER).eq(Subscribe::getSubscriber, id).count();
        // 文集信息
        LambdaQueryChainWrapper<Collection> cLambdaQuery = new LambdaQueryChainWrapper<>(collectionMapper);
        List<Collection> collections = cLambdaQuery.eq(Collection::getCreator, id).list();
        // 专题信息
        LambdaQueryChainWrapper<Subject> sLambdaQuery = new LambdaQueryChainWrapper<>(subjectMapper);
        List<Subject> subjects = sLambdaQuery.eq(Subject::getCreator, id).list();
        // 组装
        UserCountVO countVO = new UserCountVO();
        BeanUtil.copyProperties(user, countVO);
        countVO.setArticleCount(articleCount);
        countVO.setCollections(collections);
        countVO.setSubjects(subjects);
        countVO.setSubCount(subCount);
        countVO.setFollowCount(followCount);
        return countVO;
    }

    @Transactional
    @Override
    public UserInfoVO editUser(UserEditParam param) {
        User onlineUser = securityTool.getOnlineUser();
        if (!onlineUser.getId().equals(param.getId())) {
            Asserts.fail("不能修改其他用户信息");
        }
        User user = new User();
        BeanUtil.copyProperties(param, user);
        updateById(user);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(user, userInfoVO);
        return userInfoVO;
    }

    @Override
    public List<UserCountVO> listByIds(List<Long> ids) {
        if (ObjectUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<User> users = lambdaQuery().in(User::getId, ids).list();
        List<UserCountVO> userCountVOS = users.stream().map(user -> {
            UserCountVO countVO = new UserCountVO();
            BeanUtil.copyProperties(user, countVO);
            LambdaQueryChainWrapper<Article> articleLambdaQuery = new LambdaQueryChainWrapper<>(articleMapper);
            LambdaQueryChainWrapper<Subscribe> subscribeLambdaQuery = new LambdaQueryChainWrapper<>(subscribeMapper);
            Long articleCount = articleLambdaQuery.eq(Article::getPublish, 1).eq(Article::getAuthor, user.getId()).count();
            Long subCount = subscribeLambdaQuery.eq(Subscribe::getType, ResourceTypeEnum.USER).eq(Subscribe::getResource, user.getId()).count();
            countVO.setArticleCount(articleCount);
            countVO.setSubCount(subCount);
            return countVO;
        }).collect(Collectors.toList());
        return userCountVOS;
    }

    @Override
    public List<UserCountVO> recommend() {
        User onlineUser = securityTool.getOnlineUser();
        Long userId = null;
        if (ObjectUtil.isNotNull(onlineUser)) {
            userId = onlineUser.getId();
        }
        List<Long> recommendUserIds = subscribeMapper.getUnsubUserIdsBySubCount(userId);
        List<UserCountVO> userCountVOS = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(recommendUserIds)) {
            userCountVOS = listByIds(recommendUserIds);
        }
        return userCountVOS.stream().sorted(Comparator.comparing(UserCountVO::getSubCount, Comparator.reverseOrder())).collect(Collectors.toList());
    }

}
