package site.mizore.lobi.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import site.mizore.lobi.enums.MessageTypeEnum;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "Message", description = "消息实体")
@TableName("t_message")
public class Message {

    @TableId(type = IdType.AUTO)
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

    @ApiModelProperty("附加内容: 文章id")
    private Long exArticle;

    @ApiModelProperty("是否已读")
    private Integer readed;

    @ApiModelProperty("发送消息时间")
    private LocalDateTime date;

}
