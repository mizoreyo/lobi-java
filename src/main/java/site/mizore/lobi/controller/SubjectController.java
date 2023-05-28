package site.mizore.lobi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.mizore.lobi.api.CommonResult;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.entity.param.SubjectEditParam;
import site.mizore.lobi.entity.param.SubjectParam;
import site.mizore.lobi.entity.po.Subject;
import site.mizore.lobi.entity.vo.SubjectCountVO;
import site.mizore.lobi.entity.vo.SubjectVO;
import site.mizore.lobi.service.SubjectService;

import java.util.List;

@Api("SubjectController")
@RestController
@RequestMapping("/subject")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @ApiOperation("获取专题信息")
    @GetMapping("/info/{id}")
    public CommonResult getSubjectInfo(@PathVariable("id") Long id) {
        Subject subject = subjectService.getSubjectInfo(id);
        return CommonResult.success(subject);
    }

    @ApiOperation("编辑专题信息")
    @PutMapping("/edit")
    public CommonResult editSubject(@Validated @RequestBody SubjectEditParam param) {
        Subject subject = subjectService.editSubject(param);
        return CommonResult.success(subject);
    }

    @ApiOperation("创建专题")
    @PostMapping("/create")
    public CommonResult<Subject> createSubject(@Validated @RequestBody SubjectParam subjectParam) {
        Subject subject = subjectService.createSubject(subjectParam);
        return CommonResult.success(subject);
    }

    @ApiOperation("搜索专题")
    @GetMapping("/search")
    public CommonResult searchSubject(@RequestParam(value = "q", required = false) String q) {
        List<Subject> subjects = subjectService.searchSubject(q);
        return CommonResult.success(subjects);
    }

    @ApiOperation("热门专题")
    @GetMapping("/hot")
    public CommonResult hotSubject() {
        List<Subject> subjects = subjectService.hotSubject();
        return CommonResult.success(subjects);
    }

    @ApiOperation("专题统计信息")
    @GetMapping("/info/count/{id}")
    public CommonResult subjectCountInfo(@PathVariable("id") Long id) {
        SubjectCountVO countVO = subjectService.subjectCountInfo(id);
        return CommonResult.success(countVO);
    }

    @ApiOperation("管理: 获取专题分页")
    @GetMapping
    public CommonResult page(@RequestParam("page") Integer page,
                             @RequestParam("size") Integer size,
                             @RequestParam(value = "q", required = false) String q) {
        CommonPage<SubjectVO> subjectPage = subjectService.page(page, size, q);
        return CommonResult.success(subjectPage);
    }

    @ApiOperation("管理: 获取专题")
    @GetMapping("/{id}")
    public CommonResult get(@PathVariable Long id) {
        SubjectVO subjectVO = subjectService.get(id);
        return CommonResult.success(subjectVO);
    }

    @ApiOperation("管理: 修改专题")
    @PutMapping
    public CommonResult update(@Validated @RequestBody SubjectEditParam param) {
        subjectService.updateSubject(param);
        return CommonResult.success("修改成功");
    }

    @ApiOperation("管理: 删除专题")
    @DeleteMapping("/{id}")
    public CommonResult delete(@PathVariable Long id) {
        subjectService.delete(id);
        return CommonResult.success("删除成功");
    }


}
