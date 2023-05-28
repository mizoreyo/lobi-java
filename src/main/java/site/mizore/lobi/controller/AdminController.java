package site.mizore.lobi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.mizore.lobi.api.CommonResult;
import site.mizore.lobi.entity.param.UserLoginParam;
import site.mizore.lobi.entity.param.UserParam;
import site.mizore.lobi.entity.po.User;
import site.mizore.lobi.entity.vo.UserInfoVO;
import site.mizore.lobi.service.AdminService;

import java.util.HashMap;
import java.util.Map;

@Api("AdminController")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    private final AdminService adminService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public CommonResult<User> register(@Validated @RequestBody UserParam userParam) {
        User user = adminService.register(userParam);
        if (user == null) {
            return CommonResult.failed("用户已存在");
        }
        return CommonResult.success(user);
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public CommonResult login(@Validated @RequestBody UserLoginParam userLoginParam) {
        String token = adminService.login(userLoginParam);
        if (token == null) {
            return CommonResult.failed();
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation("用户信息")
    @GetMapping("/info")
    public CommonResult<UserInfoVO> info() {
        UserInfoVO userInfoVO = adminService.info();
        return CommonResult.success(userInfoVO);
    }

    @ApiOperation("管理员登录")
    @PostMapping("/login/manager")
    public CommonResult managerLogin(@Validated @RequestBody UserLoginParam userLoginParam) {
        String token = adminService.managerLogin(userLoginParam);
        if (token == null) {
            return CommonResult.failed("非管理员用户");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

}
