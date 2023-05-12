package site.mizore.lobi.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "MessageTypeEnum", description = "消息类型枚举")
public enum MessageTypeEnum {

    @ApiModelProperty("评论")
    COMMENT("comment", "评论"),
    @ApiModelProperty("关注")
    SUBSCRIBE("subscribe", "关注"),
    @ApiModelProperty("点赞")
    THUMB("thumb", "点赞"),
    @ApiModelProperty("喜欢")
    LIKE("like", "喜欢"),
    @ApiModelProperty("私信")
    LETTER("letter", "私信");

    @EnumValue
    private final String code;

    private final String desc;

    MessageTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
