package site.mizore.lobi.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.component.SecurityTool;
import site.mizore.lobi.entity.param.CommentCreateParam;
import site.mizore.lobi.entity.po.Article;
import site.mizore.lobi.entity.po.Comment;
import site.mizore.lobi.entity.po.User;
import site.mizore.lobi.entity.vo.ArticleViewsVO;
import site.mizore.lobi.entity.vo.CommentManageVO;
import site.mizore.lobi.entity.vo.CommentVO;
import site.mizore.lobi.entity.vo.UserInfoVO;
import site.mizore.lobi.exception.Asserts;
import site.mizore.lobi.mapper.ArticleMapper;
import site.mizore.lobi.mapper.CommentMapper;
import site.mizore.lobi.mapper.UserMapper;
import site.mizore.lobi.service.CommentService;
import site.mizore.lobi.service.MessageService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final SecurityTool securityTool;

    private final UserMapper userMapper;

    private final ArticleMapper articleMapper;

    private final MessageService messageService;

    @Override
    @Transactional
    public Comment createComment(CommentCreateParam param) {
        if (!param.getParent().equals(0L)) {
            List<Comment> comments = lambdaQuery().eq(Comment::getArticle, param.getArticle()).eq(Comment::getId, param.getParent()).list();
            if (comments.isEmpty()) {
                Asserts.fail("找不到父评论");
            }
        }
        Comment comment = new Comment();
        BeanUtil.copyProperties(param, comment);
        User onlineUser = securityTool.getOnlineUser();
        comment.setUser(onlineUser.getId());
        comment.setDate(LocalDateTime.now());
        // 新建评论消息
        if (param.getParent().equals(0L)) {
            // 查询文章作者id
            Article article = articleMapper.selectById(param.getArticle());
            messageService.createCommentMessage(onlineUser.getId(), article.getAuthor(), article.getId(), param.getContent());
        } else {
            // 查询父评论发送者
            Comment comment1 = getById(param.getParent());
            messageService.createCommentMessage(onlineUser.getId(), comment1.getUser(), param.getArticle(), param.getContent());
        }
        save(comment);
        return comment;
    }

    @Override
    public List<CommentVO> getCommentTree(Long article) {
        List<Comment> comments = lambdaQuery().eq(Comment::getArticle, article).list();
        List<CommentVO> commentVOS = comments.stream().map(comment -> {
            CommentVO commentVO = new CommentVO();
            BeanUtil.copyProperties(comment, commentVO);
            User user = userMapper.selectById(comment.getUser());
            commentVO.setNickname(user.getNickname());
            commentVO.setAvatar(user.getAvatar());
            if (!comment.getParent().equals(0L)) {
                Comment parent = getById(comment.getParent());
                User parentUser = userMapper.selectById(parent.getUser());
                commentVO.setParentNickname(parentUser.getNickname());
            }
            return commentVO;
        }).collect(Collectors.toList());
        List<CommentVO> column1List = commentVOS.stream().filter(commentVO -> commentVO.getParent().equals(0L)).collect(Collectors.toList());
        column1List.forEach(commentVO -> {
            List<CommentVO> children = new ArrayList<>();
            generateColumn2(children, commentVO, commentVOS, 0);
            commentVO.setChildren(children);
        });
        return column1List;
    }

    @Override
    public CommonPage<CommentManageVO> page(Integer page, Integer size, String q) {
        Page<Comment> queryPage = Page.of(page, size);
        LambdaQueryChainWrapper<Comment> commentLambdaQuery = lambdaQuery();
        if (StrUtil.isNotBlank(q)) {
            commentLambdaQuery.like(Comment::getContent, q);
        }
        Page<Comment> commentPage = commentLambdaQuery.page(queryPage);
        CommonPage<CommentManageVO> commonPage = new CommonPage<>();
        commonPage.setTotal(commentPage.getTotal());
        List<CommentManageVO> data = commentPage.getRecords().stream().map(comment -> {
            CommentManageVO manageVO = new CommentManageVO();
            BeanUtil.copyProperties(comment, manageVO);
            Article article = articleMapper.selectById(comment.getArticle());
            ArticleViewsVO articleViewsVO = new ArticleViewsVO();
            BeanUtil.copyProperties(article, articleViewsVO);
            manageVO.setArticle(articleViewsVO);
            User user = userMapper.selectById(comment.getUser());
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtil.copyProperties(user, userInfoVO);
            manageVO.setUser(userInfoVO);
            return manageVO;
        }).collect(Collectors.toList());
        commonPage.setData(data);
        return commonPage;
    }

    @Override
    public void delete(Long id) {
        removeById(id);
        List<Long> children = lambdaQuery().eq(Comment::getParent, id).list().stream().map(Comment::getId).collect(Collectors.toList());
        if (children.isEmpty()) {
            return;
        } else {
            children.forEach(this::delete);
        }
    }

    public void generateColumn2(List<CommentVO> column2List, CommentVO commentVO, List<CommentVO> commentVOS, int index) {
        commentVOS.forEach(commentVO1 -> {
            if (commentVO1.getParent().equals(commentVO.getId())) {
                if (index == 0) {
                    commentVO1.setParentNickname(null);
                }
                column2List.add(commentVO1);
                generateColumn2(column2List, commentVO1, commentVOS, index + 1);
            }
        });
    }


}
