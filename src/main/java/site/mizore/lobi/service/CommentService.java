package site.mizore.lobi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.mizore.lobi.entity.param.CommentCreateParam;
import site.mizore.lobi.entity.po.Comment;
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
}
