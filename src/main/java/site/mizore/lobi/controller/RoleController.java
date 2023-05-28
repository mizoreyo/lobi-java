package site.mizore.lobi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.mizore.lobi.api.CommonResult;
import site.mizore.lobi.entity.po.Role;
import site.mizore.lobi.service.RoleService;

import java.util.List;

@Api("RoleController")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @ApiOperation("查询所有角色")
    @GetMapping("/list")
    public CommonResult listRoles() {
        List<Role> roleList = roleService.list();
        return CommonResult.success(roleList);
    }


}
