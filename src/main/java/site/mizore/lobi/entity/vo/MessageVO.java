package site.mizore.lobi.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import site.mizore.lobi.enums.MessageTypeEnum;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "MessageVO", description = "消息响应")
public class MessageVO {

    @ApiModelProperty("消息id")
    private Long id;

    //0L代表系统发的消息
    @ApiModelProperty("发送者id")
    private Long sender;

    @ApiModelProperty("接收者id")
    private Long receiver;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("消息类型")
    private MessageTypeEnum type;

    @ApiModelProperty("附加内容: 用户id")
    private Long exUser;

    @ApiModelProperty("用户昵称")
    private String exNickname;

    @ApiModelProperty("用户头像")
    private String exAvatar;

    @ApiModelProperty("附加内容: 文章id")
    private Long exArticle;

    @ApiModelProperty("文章标题")
    private String exArticleTitle;

    @ApiModelProperty("是否已读")
    private Integer readed;

    @ApiModelProperty("发送消息时间")
    private LocalDateTime date;

}
