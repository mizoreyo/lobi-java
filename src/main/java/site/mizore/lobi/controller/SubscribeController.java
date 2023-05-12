package site.mizore.lobi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.mizore.lobi.api.CommonResult;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.entity.param.SubscribeOperateParam;
import site.mizore.lobi.entity.po.Subscribe;
import site.mizore.lobi.entity.vo.SubStatusVO;
import site.mizore.lobi.enums.ResourceTypeEnum;
import site.mizore.lobi.service.SubscribeService;

@Api("SubscribeController")
@RestController
@RequestMapping("/subscribe")
@RequiredArgsConstructor
public class SubscribeController {

    private final SubscribeService subscribeService;

    @ApiOperation("订阅")
    @PostMapping("/create")
    public CommonResult createSubscribe(@Validated @RequestBody SubscribeOperateParam param) {
        Subscribe subscribe = subscribeService.createSubscribe(param);
        return CommonResult.success(subscribe);
    }

    @ApiOperation("取关")
    @DeleteMapping("/delete")
    public CommonResult deleteSubscribe(@RequestParam("resource") Long resource,
                                        @RequestParam("type") ResourceTypeEnum type) {
        boolean result = subscribeService.deleteSubscribe(resource, type);
        return CommonResult.success(result);
    }

    @ApiOperation("查询订阅状态")
    @GetMapping("/status")
    public CommonResult isSubscribed(@RequestParam("resource") Long resource,
                                     @RequestParam("type") ResourceTypeEnum type) {
        SubStatusVO statusVO = subscribeService.subscribeStatus(resource, type);
        return CommonResult.success(statusVO);
    }

    @ApiOperation("获取用户订阅")
    @GetMapping("/page/my")
    public CommonResult getMySubscribePage(@RequestParam("type") ResourceTypeEnum type,
                                           @RequestParam("page") Integer page,
                                           @RequestParam("size") Integer size) {
        CommonPage<Object> commonPage = subscribeService.getMySubscribePage(type, page, size);
        return CommonResult.success(commonPage);
    }

}
