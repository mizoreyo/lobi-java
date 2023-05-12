package site.mizore.lobi.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import site.mizore.lobi.entity.param.UserLoginParam;
import site.mizore.lobi.entity.param.UserParam;
import site.mizore.lobi.entity.po.User;
import site.mizore.lobi.entity.vo.UserInfoVO;

public interface AdminService extends UserDetailsService {

    /**
     * 用户注册
     *
     * @param userParam
     * @return
     */
    User register(UserParam userParam);

    /**
     * 用户登录
     *
     * @param userLoginParam
     * @return
     */
    String login(UserLoginParam userLoginParam);

    /**
     * 获取用户信息
     *
     * @return
     */
    UserInfoVO info();
}
