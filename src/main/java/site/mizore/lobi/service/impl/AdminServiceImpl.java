package site.mizore.lobi.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.mizore.lobi.component.SecurityTool;
import site.mizore.lobi.entity.param.UserLoginParam;
import site.mizore.lobi.entity.param.UserParam;
import site.mizore.lobi.entity.po.AdminUserDetails;
import site.mizore.lobi.entity.po.Role;
import site.mizore.lobi.entity.po.User;
import site.mizore.lobi.entity.po.UserRole;
import site.mizore.lobi.entity.vo.UserInfoVO;
import site.mizore.lobi.exception.ApiException;
import site.mizore.lobi.exception.Asserts;
import site.mizore.lobi.mapper.RoleMapper;
import site.mizore.lobi.mapper.UserMapper;
import site.mizore.lobi.mapper.UserRoleMapper;
import site.mizore.lobi.service.AdminService;
import site.mizore.lobi.util.JwtTokenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    private final UserRoleMapper userRoleMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    private final SecurityTool securityTool;

    @Override
    public UserDetails loadUserByUsername(String username) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User user = userMapper.selectOne(userQueryWrapper);
        if (ObjectUtil.isNotNull(user)) {
            QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
            userRoleQueryWrapper.eq("user_id", user.getId());
            List<Role> roles = new ArrayList<>();
            List<UserRole> userRoles = userRoleMapper.selectList(userRoleQueryWrapper);
            if (ObjectUtil.isNotEmpty(userRoles)) {
                List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
                QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
                roles = roleMapper.selectBatchIds(roleIds);
            }
            return new AdminUserDetails(user, roles);
        }
        throw new ApiException("用户名或密码错误");
    }


    @Transactional
    @Override
    public User register(UserParam userParam) {
        User user = new User();
        BeanUtil.copyProperties(userParam, user);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        List<User> users = userMapper.selectList(queryWrapper);
        if (users.size() > 0) {
            return null;
        }
        //将密码进行加密操作
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        user.setEnable(1);
        userMapper.insert(user);
        return user;
    }

    @Override
    public String login(UserLoginParam userLoginParam) {
        String token = null;
        UserDetails userDetails = loadUserByUsername(userLoginParam.getUsername());
        if (!passwordEncoder.matches(userLoginParam.getPassword(), userDetails.getPassword())) {
            Asserts.fail("用户名或密码错误");
        }
        if (!userDetails.isEnabled()) {
            Asserts.fail("帐号已被禁用");
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        token = jwtTokenUtil.generateToken(userDetails);
        return token;
    }

    @Override
    public UserInfoVO info() {
        User user = securityTool.getOnlineUser();
        if (user == null) {
            return null;
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(user, userInfoVO);
        return userInfoVO;
    }
}
