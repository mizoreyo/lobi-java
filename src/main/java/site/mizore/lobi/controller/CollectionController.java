package site.mizore.lobi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.mizore.lobi.api.CommonResult;
import site.mizore.lobi.entity.param.CollectionCreateParam;
import site.mizore.lobi.entity.param.CollectionEditParam;
import site.mizore.lobi.entity.po.Collection;
import site.mizore.lobi.entity.vo.CollectionCountVO;
import site.mizore.lobi.entity.vo.CollectionInfoVO;
import site.mizore.lobi.service.CollectionService;

import java.util.List;

@Api("CollectionController")
@RestController
@RequestMapping("/collection")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @GetMapping("/info/list")
    @ApiOperation("获取用户的文集")
    public CommonResult getInfoList() {
        List<CollectionInfoVO> vos = collectionService.getInfoList();
        return CommonResult.success(vos);
    }

    @PostMapping("/create")
    @ApiOperation("用户创建文集")
    public CommonResult createCollection(@Validated @RequestBody CollectionCreateParam param) {
        Collection collection = collectionService.createCollection(param);
        return CommonResult.success(collection);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("用户删除文集")
    public CommonResult deleteCollection(@PathVariable("id") Long id) {
        boolean result = collectionService.deleteCollection(id);
        if (result) {
            return CommonResult.success(result);
        }
        return CommonResult.failed("删除失败");
    }

    @PutMapping("/edit")
    @ApiOperation("用户编辑文集")
    public CommonResult editCollection(@Validated @RequestBody CollectionEditParam param) {
        Collection collection = collectionService.editCollection(param);
        return CommonResult.success(collection);
    }

    @GetMapping("/info/count/{id}")
    @ApiOperation("文集统计数据")
    public CommonResult collectionCountInfo(@PathVariable("id") Long id) {
        CollectionCountVO countVO = collectionService.collectionCountInfo(id);
        return CommonResult.success(countVO);
    }

}
