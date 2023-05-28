package site.mizore.lobi.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.component.SecurityTool;
import site.mizore.lobi.entity.param.SubjectEditParam;
import site.mizore.lobi.entity.param.SubjectParam;
import site.mizore.lobi.entity.po.Article;
import site.mizore.lobi.entity.po.Subject;
import site.mizore.lobi.entity.po.Subscribe;
import site.mizore.lobi.entity.po.User;
import site.mizore.lobi.entity.vo.SubjectCountVO;
import site.mizore.lobi.entity.vo.SubjectVO;
import site.mizore.lobi.entity.vo.UserInfoVO;
import site.mizore.lobi.enums.ResourceTypeEnum;
import site.mizore.lobi.exception.Asserts;
import site.mizore.lobi.mapper.ArticleMapper;
import site.mizore.lobi.mapper.SubjectMapper;
import site.mizore.lobi.mapper.SubscribeMapper;
import site.mizore.lobi.mapper.UserMapper;
import site.mizore.lobi.service.SubjectService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    private final UserMapper userMapper;

    private final SecurityTool securityTool;

    private final ArticleMapper articleMapper;

    private final SubscribeMapper subscribeMapper;

    @Override
    @Transactional
    public Subject createSubject(SubjectParam subjectParam) {
        List<Subject> subjects = lambdaQuery().eq(Subject::getName, subjectParam.getName()).list();
        if (ObjectUtil.isNotEmpty(subjects)) {
            Asserts.fail("已存在同名专题");
        }
        Subject subject = new Subject();
        BeanUtil.copyProperties(subjectParam, subject);
        User user = securityTool.getOnlineUser();
        subject.setCreator(user.getId());
        save(subject);
        return subject;
    }

    @Override
    public List<Subject> searchSubject(String q) {
        if (q == null) {
            return new ArrayList<>();
        }
        return lambdaQuery().like(Subject::getName, q).list();
    }

    @Override
    public List<Subject> hotSubject() {
        return getBaseMapper().queryHotSubjects(10);
    }

    @Override
    public CommonPage<SubjectCountVO> searchSubjects(String q, Integer page, Integer size) {
        Page<Subject> subjectPage = Page.of(page, size);
        Page<Subject> resPage = lambdaQuery().like(Subject::getName, q).page(subjectPage);
        CommonPage<SubjectCountVO> finalPage = new CommonPage<>();
        finalPage.setTotal(resPage.getTotal());
        finalPage.setData(resPage.getRecords().stream().map(subject -> {
            LambdaQueryChainWrapper<Article> articleLambdaQuery = new LambdaQueryChainWrapper<>(articleMapper);
            LambdaQueryChainWrapper<Subscribe> subscribeLambdaQuery = new LambdaQueryChainWrapper<>(subscribeMapper);
            SubjectCountVO countVO = new SubjectCountVO();
            BeanUtil.copyProperties(subject, countVO);
            Long articleCount = articleLambdaQuery.eq(Article::getPublish, 1).eq(Article::getSubject, subject.getId()).count();
            countVO.setArticleCount(articleCount);
            Long subCount = subscribeLambdaQuery.eq(Subscribe::getType, ResourceTypeEnum.SUBJECT).eq(Subscribe::getResource, subject.getId()).count();
            countVO.setArticleCount(articleCount);
            countVO.setSubCount(subCount);
            return countVO;
        }).collect(Collectors.toList()));
        return finalPage;
    }

    @Override
    public SubjectCountVO subjectCountInfo(Long id) {
        Subject subject = getById(id);
        if (ObjectUtil.isNull(subject)) {
            Asserts.fail("找不到专题");
        }
        SubjectCountVO countVO = new SubjectCountVO();
        BeanUtil.copyProperties(subject, countVO);
        // 查询文章数
        LambdaQueryChainWrapper<Article> aLambdaQuery = new LambdaQueryChainWrapper<>(articleMapper);
        Long articleCount = aLambdaQuery.eq(Article::getSubject, id).count();
        countVO.setArticleCount(articleCount);
        // 查询关注数
        LambdaQueryChainWrapper<Subscribe> subscribeLambdaQuery = new LambdaQueryChainWrapper<>(subscribeMapper);
        Long subCount = subscribeLambdaQuery.eq(Subscribe::getType, ResourceTypeEnum.SUBJECT).eq(Subscribe::getResource, id).count();
        countVO.setSubCount(subCount);
        // 查询创建者信息
        User user = userMapper.selectById(subject.getCreator());
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(user, userInfoVO);
        countVO.setCreator(userInfoVO);
        return countVO;
    }

    @Override
    public Subject getSubjectInfo(Long id) {
        return getById(id);
    }

    @Override
    public Subject editSubject(SubjectEditParam param) {
        User onlineUser = securityTool.getOnlineUser();
        List<Subject> subjects = lambdaQuery().eq(Subject::getId, param.getId()).eq(Subject::getCreator, onlineUser.getId()).list();
        if (subjects.isEmpty()) {
            Asserts.fail("找不到专题或无权限编辑");
        }
        Subject updateSubject = new Subject();
        BeanUtil.copyProperties(param, updateSubject);
        updateById(updateSubject);
        return updateSubject;
    }

    @Override
    public List<SubjectCountVO> listByIds(List<Long> ids) {
        if (ObjectUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<Subject> subjects = lambdaQuery().in(Subject::getId, ids).list();
        List<SubjectCountVO> subjectCountVOS = subjects.stream().map(subject -> {
            LambdaQueryChainWrapper<Article> articleLambdaQuery = new LambdaQueryChainWrapper<>(articleMapper);
            LambdaQueryChainWrapper<Subscribe> subscribeLambdaQuery = new LambdaQueryChainWrapper<>(subscribeMapper);
            SubjectCountVO countVO = new SubjectCountVO();
            BeanUtil.copyProperties(subject, countVO);
            Long articleCount = articleLambdaQuery.eq(Article::getPublish, 1).eq(Article::getSubject, subject.getId()).count();
            countVO.setArticleCount(articleCount);
            Long subCount = subscribeLambdaQuery.eq(Subscribe::getType, ResourceTypeEnum.SUBJECT).eq(Subscribe::getResource, subject.getId()).count();
            countVO.setArticleCount(articleCount);
            countVO.setSubCount(subCount);
            return countVO;
        }).collect(Collectors.toList());
        return subjectCountVOS;
    }

    @Override
    public CommonPage<SubjectVO> page(Integer page, Integer size, String q) {
        Page<Subject> queryPage = Page.of(page, size);
        LambdaQueryChainWrapper<Subject> subjectLambdaQuery = lambdaQuery();
        if (StrUtil.isNotBlank(q)) {
            subjectLambdaQuery.like(Subject::getName, q);
        }
        Page<Subject> subjectPage = subjectLambdaQuery.page(queryPage);
        CommonPage<SubjectVO> commonPage = new CommonPage<>();
        commonPage.setTotal(subjectPage.getTotal());
        List<SubjectVO> data = subjectPage.getRecords().stream().map(this::subjectToVO).collect(Collectors.toList());
        commonPage.setData(data);
        return commonPage;
    }

    @Override
    public SubjectVO get(Long id) {
        Subject subject = getById(id);
        if (subject == null) {
            Asserts.fail("找不到专题");
        }
        return subjectToVO(subject);
    }

    @Override
    public void updateSubject(SubjectEditParam param) {
        Subject subject = new Subject();
        BeanUtil.copyProperties(param, subject);
        updateById(subject);
    }

    @Override
    public void delete(Long id) {
        removeById(id);
        // 删除关注
        LambdaUpdateChainWrapper<Subscribe> subLambdaUpdate = new LambdaUpdateChainWrapper<>(subscribeMapper);
        subLambdaUpdate.eq(Subscribe::getType, ResourceTypeEnum.SUBJECT).eq(Subscribe::getResource, id).remove();
    }

    private SubjectVO subjectToVO(Subject subject) {
        SubjectVO subjectVO = new SubjectVO();
        BeanUtil.copyProperties(subject, subjectVO);
        User user = userMapper.selectById(subject.getCreator());
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(user, userInfoVO);
        subjectVO.setCreator(userInfoVO);
        return subjectVO;
    }

}
