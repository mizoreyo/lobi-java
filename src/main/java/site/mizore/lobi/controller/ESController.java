package site.mizore.lobi.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.mizore.lobi.api.CommonResult;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.entity.vo.ArticleSummaryVO;
import site.mizore.lobi.entity.vo.CollectionCountVO;
import site.mizore.lobi.entity.vo.SubjectCountVO;
import site.mizore.lobi.entity.vo.UserCountVO;
import site.mizore.lobi.service.CollectionService;
import site.mizore.lobi.service.ESService;
import site.mizore.lobi.service.SubjectService;
import site.mizore.lobi.service.UserService;

@Api("ESController")
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class ESController {

    private final ESService esService;

    private final UserService userService;

    private final CollectionService collectionService;

    private final SubjectService subjectService;

    @ApiOperation("搜索文章")
    @GetMapping("/article")
    public CommonResult searchArticles(@RequestParam("q") String q,
                                       @RequestParam("page") Integer page,
                                       @RequestParam("size") Integer size) {
        CommonPage<ArticleSummaryVO> articles = esService.searchArticles(q, page, size);
        return CommonResult.success(articles);
    }

    @ApiOperation("搜索用户")
    @GetMapping("/user")
    public CommonResult searchUsers(@RequestParam("q") String q,
                                    @RequestParam("page") Integer page,
                                    @RequestParam("size") Integer size) {
        CommonPage<UserCountVO> users = userService.searchUsers(q, page, size);
        return CommonResult.success(users);
    }

    @ApiOperation("搜索文集")
    @GetMapping("/collection")
    public CommonResult searchCollections(@RequestParam("q") String q,
                                          @RequestParam("page") Integer page,
                                          @RequestParam("size") Integer size) {
        CommonPage<CollectionCountVO> collections = collectionService.searchCollections(q, page, size);
        return CommonResult.success(collections);
    }

    @ApiOperation("搜索专题")
    @GetMapping("/subject")
    public CommonResult searchSubjects(@RequestParam("q") String q,
                                       @RequestParam("page") Integer page,
                                       @RequestParam("size") Integer size) {
        CommonPage<SubjectCountVO> subjects = subjectService.searchSubjects(q, page, size);
        return CommonResult.success(subjects);
    }

}
