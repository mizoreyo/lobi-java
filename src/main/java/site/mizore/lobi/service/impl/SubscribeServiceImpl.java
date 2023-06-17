package site.mizore.lobi.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.component.SecurityTool;
import site.mizore.lobi.entity.param.SubscribeOperateParam;
import site.mizore.lobi.entity.po.Article;
import site.mizore.lobi.entity.po.Subscribe;
import site.mizore.lobi.entity.po.User;
import site.mizore.lobi.entity.vo.SubStatusVO;
import site.mizore.lobi.entity.vo.UserInfoVO;
import site.mizore.lobi.enums.ResourceTypeEnum;
import site.mizore.lobi.exception.Asserts;
import site.mizore.lobi.mapper.SubscribeMapper;
import site.mizore.lobi.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl extends ServiceImpl<SubscribeMapper, Subscribe> implements SubscribeService {

    private final SecurityTool securityTool;

    private final ArticleService articleService;

    private final SubjectService subjectService;

    private final CollectionService collectionService;

    private final UserService userService;

    private final MessageService messageService;

    @Override
    public Subscribe createSubscribe(SubscribeOperateParam param) {
        User onlineUser = securityTool.getOnlineUser();
        List<Subscribe> subscribes = lambdaQuery().eq(Subscribe::getResource, param.getResource())
                .eq(Subscribe::getSubscriber, onlineUser.getId())
                .eq(Subscribe::getType, param.getType()).list();
        if (!subscribes.isEmpty()) {
            Asserts.fail("已关注");
        }
        Subscribe subscribe = new Subscribe();
        BeanUtil.copyProperties(param, subscribe);
        subscribe.setSubscriber(onlineUser.getId());
        // 新建消息
        if (ResourceTypeEnum.ARTICLE.equals(param.getType())) {
            Article article = articleService.getById(param.getResource());
            messageService.createLikeMessage(onlineUser.getId(), article.getAuthor(), article.getId());
        }
        if (ResourceTypeEnum.USER.equals(param.getType())) {
            messageService.createSubscribeMessage(onlineUser.getId(), param.getResource());
        }
        save(subscribe);
        return subscribe;
    }

    @Override
    public boolean deleteSubscribe(Long resource, ResourceTypeEnum type) {
        User onlineUser = securityTool.getOnlineUser();
        return lambdaUpdate().eq(Subscribe::getSubscriber, onlineUser.getId())
                .eq(Subscribe::getResource, resource)
                .eq(Subscribe::getType, type).remove();
    }

    @Override
    public CommonPage<Object> getMySubscribePage(ResourceTypeEnum type, Integer page, Integer size) {
        User onlineUser = securityTool.getOnlineUser();
        Page<Subscribe> queryPage = Page.of(page, size);
        Page<Subscribe> subPage = lambdaQuery().eq(Subscribe::getSubscriber, onlineUser.getId()).eq(Subscribe::getType, type).page(queryPage);
        CommonPage<Object> result = new CommonPage<>();
        result.setTotal(subPage.getTotal());
        List<Long> resourceIds = subPage.getRecords().stream().map(Subscribe::getResource).collect(Collectors.toList());
        List<Object> resultRecords = new ArrayList<>();
        switch (type) {
            case ARTICLE:
                resultRecords.addAll(articleService.listByIds(resourceIds));
                break;
            case SUBJECT:
                resultRecords.addAll(subjectService.listByIds(resourceIds));
                break;
            case COLLECTION:
                resultRecords.addAll(collectionService.listByIds(resourceIds));
                break;
            case USER:
                resultRecords.addAll(userService.listByIds(resourceIds));
        }
        result.setData(resultRecords);
        return result;
    }

    @Override
    public SubStatusVO subscribeStatus(Long resource, ResourceTypeEnum type) {
        User onlineUser = securityTool.getOnlineUser();
        SubStatusVO statusVO = new SubStatusVO();
        if (ObjectUtil.isNull(onlineUser)) {
            statusVO.setStatus(false);
        } else {
            List<Subscribe> subscribes = lambdaQuery()
                    .eq(Subscribe::getType, type)
                    .eq(Subscribe::getResource, resource)
                    .eq(Subscribe::getSubscriber, onlineUser.getId()).list();
            statusVO.setStatus(!subscribes.isEmpty());
        }
        Long count = lambdaQuery().eq(Subscribe::getType, type).eq(Subscribe::getResource, resource).count();
        statusVO.setCount(count);
        return statusVO;
    }

    @Override
    public CommonPage<UserInfoVO> getFollowerList(ResourceTypeEnum type, Long resId, Integer page, Integer size) {
        Page<Subscribe> queryPage = Page.of(page, size);
        Page<Subscribe> subPage = lambdaQuery().eq(Subscribe::getResource, resId).eq(Subscribe::getType, type).page(queryPage);
        CommonPage<UserInfoVO> userPage = new CommonPage<>();
        userPage.setTotal(subPage.getTotal());
        List<UserInfoVO> data = subPage.getRecords().stream().map(subscribe -> {
            User user = userService.getById(subscribe.getSubscriber());
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtil.copyProperties(user, userInfoVO);
            return userInfoVO;
        }).collect(Collectors.toList());
        userPage.setData(data);
        return userPage;
    }

}
