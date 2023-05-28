package site.mizore.lobi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.entity.param.CommentCreateParam;
import site.mizore.lobi.entity.po.Comment;
import site.mizore.lobi.entity.vo.CommentManageVO;
import site.mizore.lobi.entity.vo.CommentVO;

import java.util.List;

public interface CommentService extends IService<Comment> {
    /**
     * 新建评论
     *
     * @param param
     * @return
     */
    Comment createComment(CommentCreateParam param);

    /**
     * 获取评论树
     *
     * @param article
     * @return
     */
    List<CommentVO> getCommentTree(Long article);

    /**
     * 获取评论分页
     *
     * @param page
     * @param size
     * @param q
     * @return
     */
    CommonPage<CommentManageVO> page(Integer page, Integer size, String q);

    /**
     * 删除评论
     *
     * @param id
     */
    void delete(Long id);
}
