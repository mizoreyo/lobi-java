package site.mizore.lobi.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.component.SecurityTool;
import site.mizore.lobi.entity.po.Article;
import site.mizore.lobi.entity.po.Message;
import site.mizore.lobi.entity.po.User;
import site.mizore.lobi.entity.vo.MessageVO;
import site.mizore.lobi.enums.MessageTypeEnum;
import site.mizore.lobi.exception.Asserts;
import site.mizore.lobi.mapper.ArticleMapper;
import site.mizore.lobi.mapper.MessageMapper;
import site.mizore.lobi.mapper.UserMapper;
import site.mizore.lobi.service.MessageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private final SecurityTool securityTool;

    private final UserMapper userMapper;

    private final ArticleMapper articleMapper;

    @Override
    public void createCommentMessage(Long commenter, Long receiver, Long articleId, String comment) {
        Message message = new Message();
        message.setSender(0L);
        message.setType(MessageTypeEnum.COMMENT);
        message.setContent(comment);
        message.setReceiver(receiver);
        message.setExUser(commenter);
        message.setExArticle(articleId);
        message.setDate(LocalDateTime.now());
        save(message);
    }

    @Override
    public void createSubscribeMessage(Long subscriber, Long receiver) {
        Message message = new Message();
        message.setSender(0L);
        message.setType(MessageTypeEnum.SUBSCRIBE);
        message.setReceiver(receiver);
        message.setExUser(subscriber);
        message.setDate(LocalDateTime.now());
        save(message);
    }

    @Override
    public void createLikeMessage(Long whoLike, Long receiver, Long articleId) {
        Message message = new Message();
        message.setSender(0L);
        message.setType(MessageTypeEnum.LIKE);
        message.setReceiver(receiver);
        message.setExArticle(articleId);
        message.setExUser(whoLike);
        message.setDate(LocalDateTime.now());
        save(message);
    }

    @Override
    public void createThumbMessage(Long whoThumb, Long receiver, Long articleId) {
        Message message = new Message();
        message.setSender(0L);
        message.setType(MessageTypeEnum.THUMB);
        message.setReceiver(receiver);
        message.setExArticle(articleId);
        message.setExUser(whoThumb);
        message.setDate(LocalDateTime.now());
        save(message);
    }

    @Override
    public CommonPage<MessageVO> getMyMessagePage(MessageTypeEnum type, Integer page, Integer size) {
        User onlineUser = securityTool.getOnlineUser();
        Page<Message> queryPage = Page.of(page, size);
        Page<Message> messagePage = lambdaQuery().eq(Message::getType, type).eq(Message::getReceiver, onlineUser.getId()).orderByDesc(Message::getDate).page(queryPage);
        CommonPage<MessageVO> finalPage = new CommonPage<>();
        finalPage.setTotal(messagePage.getTotal());
        List<MessageVO> messageVOS = messagePage.getRecords().stream().map(message -> {
            MessageVO messageVO = new MessageVO();
            BeanUtil.copyProperties(message, messageVO);
            if (ObjectUtil.isNotNull(message.getExUser())) {
                User user = userMapper.selectById(message.getExUser());
                messageVO.setExNickname(user.getNickname());
                messageVO.setExAvatar(user.getAvatar());
            }
            if (ObjectUtil.isNotNull(message.getExArticle())) {
                Article article = articleMapper.selectById(message.getExArticle());
                messageVO.setExArticleTitle(article.getTitle());
            }
            return messageVO;
        }).collect(Collectors.toList());
        finalPage.setData(messageVOS);
        return finalPage;
    }

    @Override
    public Message setMessageReaded(Long id) {
        Message message = getById(id);
        User onlineUser = securityTool.getOnlineUser();
        if (!message.getReceiver().equals(onlineUser.getId())) {
            Asserts.fail("此消息不属于你");
        }
        Message updateMessage = new Message();
        updateMessage.setReaded(1);
        updateMessage.setId(id);
        updateById(updateMessage);
        return updateMessage;
    }

    @Override
    public boolean deleteMessage(Long id) {
        Message message = getById(id);
        User onlineUser = securityTool.getOnlineUser();
        if (!message.getReceiver().equals(onlineUser.getId())) {
            Asserts.fail("此消息不属于你");
        }
        return removeById(id);
    }

    @Override
    public void setAllMessageReaded(MessageTypeEnum type) {
        User onlineUser = securityTool.getOnlineUser();
        lambdaUpdate().eq(Message::getReceiver, onlineUser.getId()).eq(Message::getType, type).set(Message::getReaded, 1).update();
    }

    @Override
    public void deleteAllMessage(MessageTypeEnum type) {
        User onlineUser = securityTool.getOnlineUser();
        lambdaUpdate().eq(Message::getReceiver, onlineUser.getId()).eq(Message::getType, type).remove();
    }

    @Override
    public boolean hasUnreadMessage() {
        User onlineUser = securityTool.getOnlineUser();
        List<Message> list = lambdaQuery().eq(Message::getReceiver, onlineUser.getId()).eq(Message::getReaded, 0).list();
        return !list.isEmpty();
    }
}
