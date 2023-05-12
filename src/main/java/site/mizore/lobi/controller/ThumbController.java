package site.mizore.lobi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.mizore.lobi.api.CommonResult;
import site.mizore.lobi.entity.param.ThumbCreateParam;
import site.mizore.lobi.entity.po.Thumb;
import site.mizore.lobi.entity.vo.ThumbStatusVO;
import site.mizore.lobi.service.ThumbService;

@Api("ThumbController")
@RestController
@RequestMapping("/thumb")
@RequiredArgsConstructor
public class ThumbController {

    private final ThumbService thumbService;

    @ApiOperation("点赞")
    @PostMapping("/create")
    public CommonResult createThumb(@Validated @RequestBody ThumbCreateParam param) {
        Thumb thumb = thumbService.createThumb(param);
        return CommonResult.success(thumb);
    }

    @ApiOperation("查询点赞状态")
    @GetMapping("/status")
    public CommonResult thumbStatus(@RequestParam("article") Long article) {
        ThumbStatusVO result = thumbService.thumbStatus(article);
        return CommonResult.success(result);
    }

    @ApiOperation("取消赞")
    @DeleteMapping("/delete")
    public CommonResult deleteThumb(@RequestParam("article") Long article) {
        boolean result = thumbService.deleteThumb(article);
        return CommonResult.success(result);
    }

}
