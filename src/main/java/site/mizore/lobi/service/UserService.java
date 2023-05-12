package site.mizore.lobi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.entity.param.UserEditParam;
import site.mizore.lobi.entity.po.User;
import site.mizore.lobi.entity.vo.UserCountVO;
import site.mizore.lobi.entity.vo.UserInfoVO;

import java.util.List;

public interface UserService extends IService<User> {

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    UserInfoVO getUserInfo(Long id);

    /**
     * 搜索用户
     *
     * @param q
     * @param page
     * @param size
     * @return
     */
    CommonPage<UserCountVO> searchUsers(String q, Integer page, Integer size);

    /**
     * 获取用户统计数据
     *
     * @param id
     * @return
     */
    UserCountVO userCountInfo(Long id);

    /**
     * 修改用户信息
     *
     * @param param
     * @return
     */
    UserInfoVO editUser(UserEditParam param);

    /**
     * 根据id列表
     *
     * @param ids
     * @return
     */
    List<UserCountVO> listByIds(List<Long> ids);

    /**
     * 获取推荐用户
     *
     * @return
     */
    List<UserCountVO> recommend();
}
