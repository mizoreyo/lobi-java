package site.mizore.lobi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.entity.po.Message;
import site.mizore.lobi.entity.vo.MessageVO;
import site.mizore.lobi.enums.MessageTypeEnum;

public interface MessageService extends IService<Message> {

    /**
     * 创建评论消息
     *
     * @param commenter
     * @param receiver
     * @param articleId
     * @param comment
     */
    void createCommentMessage(Long commenter, Long receiver, Long articleId, String comment);

    /**
     * 创建关注消息
     *
     * @param subscriber
     * @param receiver
     */
    void createSubscribeMessage(Long subscriber, Long receiver);

    /**
     * 创建喜欢消息
     *
     * @param whoLike
     * @param receiver
     * @param articleId
     */
    void createLikeMessage(Long whoLike, Long receiver, Long articleId);

    /**
     * 创建点赞消息
     *
     * @param whoThumb
     * @param receiver
     * @param articleId
     */
    void createThumbMessage(Long whoThumb, Long receiver, Long articleId);

    /**
     * 获取消息分页
     *
     * @param type
     * @param page
     * @param size
     * @return
     */
    CommonPage<MessageVO> getMyMessagePage(MessageTypeEnum type, Integer page, Integer size);

    /**
     * 设置消息状态为已读
     *
     * @param id
     * @return
     */
    Message setMessageReaded(Long id);

    /**
     * 删除消息
     *
     * @param id
     * @return
     */
    boolean deleteMessage(Long id);

    /**
     * 已读某类型全部消息
     *
     * @param type
     */
    void setAllMessageReaded(MessageTypeEnum type);

    /**
     * 删除某类型全部消息
     *
     * @param type
     */
    void deleteAllMessage(MessageTypeEnum type);

    /**
     * 查询是否有未读消息
     *
     * @return
     */
    boolean hasUnreadMessage();
}
