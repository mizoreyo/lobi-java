package site.mizore.lobi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.entity.param.SubjectEditParam;
import site.mizore.lobi.entity.param.SubjectParam;
import site.mizore.lobi.entity.po.Subject;
import site.mizore.lobi.entity.vo.SubjectCountVO;

import java.util.List;

public interface SubjectService extends IService<Subject> {

    /**
     * 用户创建专题
     *
     * @param subjectParam
     * @return
     */
    Subject createSubject(SubjectParam subjectParam);

    /**
     * 搜索专题
     *
     * @param q
     * @return
     */
    List<Subject> searchSubject(String q);

    /**
     * 获取热门专题
     *
     * @return
     */
    List<Subject> hotSubject();

    /**
     * 搜索专题
     *
     * @param q
     * @param page
     * @param size
     * @return
     */
    CommonPage<SubjectCountVO> searchSubjects(String q, Integer page, Integer size);

    /**
     * 获取专题统计信息
     *
     * @param id
     * @return
     */
    SubjectCountVO subjectCountInfo(Long id);

    /**
     * 获取专题信息
     *
     * @param id
     * @return
     */
    Subject getSubjectInfo(Long id);

    /**
     * 编辑专题
     *
     * @param param
     * @return
     */
    Subject editSubject(SubjectEditParam param);

    /**
     * 根据id列表
     *
     * @param ids
     * @return
     */
    List<SubjectCountVO> listByIds(List<Long> ids);
}
