package site.mizore.lobi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.mizore.lobi.api.CommonResult;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.entity.param.UserEditParam;
import site.mizore.lobi.entity.param.UserUpdateParam;
import site.mizore.lobi.entity.vo.UserCountVO;
import site.mizore.lobi.entity.vo.UserInfoVO;
import site.mizore.lobi.entity.vo.UserVO;
import site.mizore.lobi.service.UserService;

import java.util.List;

@Api("UserController")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation("获取用户信息")
    @GetMapping("/info/{id}")
    public CommonResult<UserInfoVO> getUserInfo(@PathVariable Long id) {
        UserInfoVO userInfoVO = userService.getUserInfo(id);
        return CommonResult.success(userInfoVO);
    }

    @ApiOperation("获取用户统计信息")
    @GetMapping("/info/count/{id}")
    public CommonResult userCountInfo(@PathVariable Long id) {
        UserCountVO countVO = userService.userCountInfo(id);
        return CommonResult.success(countVO);
    }

    @ApiOperation("修改用户信息")
    @PutMapping("/edit")
    public CommonResult editUser(@Validated @RequestBody UserEditParam param) {
        UserInfoVO user = userService.editUser(param);
        return CommonResult.success(user);
    }

    @ApiOperation("获取推荐用户")
    @GetMapping("/recommend")
    public CommonResult recommend() {
        List<UserCountVO> users = userService.recommend();
        return CommonResult.success(users);
    }

    @ApiOperation("管理: 获取用户分页")
    @GetMapping
    public CommonResult page(@RequestParam("page") Integer page,
                             @RequestParam("size") Integer size,
                             @RequestParam(value = "q", required = false) String q) {
        CommonPage<UserVO> userPage = userService.page(page, size, q);
        return CommonResult.success(userPage);
    }

    @ApiOperation("管理: 获取用户")
    @GetMapping("/{id}")
    public CommonResult get(@PathVariable Long id) {
        UserVO userVO = userService.get(id);
        return CommonResult.success(userVO);
    }

    @ApiOperation("管理: 更新用户")
    @PutMapping
    public CommonResult update(@Validated @RequestBody UserUpdateParam param) {
        userService.updateUser(param);
        return CommonResult.success("修改成功");
    }

    @ApiOperation("管理: 删除用户")
    @DeleteMapping("/{id}")
    public CommonResult delete(@PathVariable Long id) {
        userService.delete(id);
        return CommonResult.success("删除成功");
    }


}
