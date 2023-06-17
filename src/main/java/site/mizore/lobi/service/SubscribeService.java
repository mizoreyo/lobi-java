package site.mizore.lobi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.entity.param.SubscribeOperateParam;
import site.mizore.lobi.entity.po.Subscribe;
import site.mizore.lobi.entity.vo.SubStatusVO;
import site.mizore.lobi.entity.vo.UserInfoVO;
import site.mizore.lobi.enums.ResourceTypeEnum;

public interface SubscribeService extends IService<Subscribe> {
    /**
     * 订阅
     *
     * @return
     */
    Subscribe createSubscribe(SubscribeOperateParam param);

    /**
     * 取关
     *
     * @param resource
     * @param type
     * @return
     */
    boolean deleteSubscribe(Long resource, ResourceTypeEnum type);

    /**
     * 获取用户订阅
     *
     * @param type
     * @param page
     * @param size
     * @return
     */
    CommonPage<Object> getMySubscribePage(ResourceTypeEnum type, Integer page, Integer size);

    /**
     * 查询订阅状态
     *
     * @param resource
     * @param type
     * @return
     */
    SubStatusVO subscribeStatus(Long resource, ResourceTypeEnum type);

    /**
     * 获取粉丝列表
     *
     * @param type
     * @param resId
     * @param page
     * @param size
     * @return
     */
    CommonPage<UserInfoVO> getFollowerList(ResourceTypeEnum type, Long resId, Integer page, Integer size);
}
