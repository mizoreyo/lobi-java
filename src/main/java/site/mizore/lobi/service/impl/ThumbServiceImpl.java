package site.mizore.lobi.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.mizore.lobi.component.SecurityTool;
import site.mizore.lobi.entity.param.ThumbCreateParam;
import site.mizore.lobi.entity.po.Article;
import site.mizore.lobi.entity.po.Thumb;
import site.mizore.lobi.entity.po.User;
import site.mizore.lobi.entity.vo.ThumbStatusVO;
import site.mizore.lobi.exception.Asserts;
import site.mizore.lobi.mapper.ArticleMapper;
import site.mizore.lobi.mapper.ThumbMapper;
import site.mizore.lobi.service.MessageService;
import site.mizore.lobi.service.ThumbService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThumbServiceImpl extends ServiceImpl<ThumbMapper, Thumb> implements ThumbService {

    private final SecurityTool securityTool;

    private final MessageService messageService;

    private final ArticleMapper articleMapper;

    @Override
    public Thumb createThumb(ThumbCreateParam param) {
        User onlineUser = securityTool.getOnlineUser();
        Thumb thumb = new Thumb();
        List<Thumb> list = lambdaQuery().eq(Thumb::getArticle, param.getArticle()).eq(Thumb::getUser, onlineUser.getId()).list();
        if (!list.isEmpty()) {
            Asserts.fail("已点赞，请勿重复点赞");
        }
        thumb.setArticle(param.getArticle());
        thumb.setUser(onlineUser.getId());
        // 查询文章作者
        Article article = articleMapper.selectById(param.getArticle());
        // 新建点赞消息
        messageService.createThumbMessage(onlineUser.getId(), article.getAuthor(), article.getId());
        save(thumb);
        return thumb;
    }

    @Override
    public ThumbStatusVO thumbStatus(Long article) {
        User onlineUser = securityTool.getOnlineUser();
        ThumbStatusVO thumbStatusVO = new ThumbStatusVO();
        if (ObjectUtil.isNull(onlineUser)) {
            thumbStatusVO.setStatus(false);
        } else {
            List<Thumb> list = lambdaQuery().eq(Thumb::getArticle, article).eq(Thumb::getUser, onlineUser.getId()).list();
            thumbStatusVO.setStatus(!list.isEmpty());
        }
        Long count = lambdaQuery().eq(Thumb::getArticle, article).count();
        thumbStatusVO.setCount(count);
        return thumbStatusVO;
    }

    @Override
    public boolean deleteThumb(Long article) {
        User onlineUser = securityTool.getOnlineUser();
        return lambdaUpdate().eq(Thumb::getArticle, article).eq(Thumb::getUser, onlineUser.getId()).remove();
    }
}
