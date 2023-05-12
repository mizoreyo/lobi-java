package site.mizore.lobi.component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import site.mizore.lobi.entity.po.User;
import site.mizore.lobi.mapper.UserMapper;

public class SecurityTool {

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取当前在线用户
     *
     * @return
     */
    public User getOnlineUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        String username = authentication.getName();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        return userMapper.selectOne(userQueryWrapper);
    }

}
