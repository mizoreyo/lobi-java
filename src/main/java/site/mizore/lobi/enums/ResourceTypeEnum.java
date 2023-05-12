package site.mizore.lobi.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ResourceTypeEnum", description = "资源类型枚举")
public enum ResourceTypeEnum {

    @ApiModelProperty("文章")
    ARTICLE("article", "文章"),
    @ApiModelProperty("专题")
    SUBJECT("subject", "专题"),
    @ApiModelProperty("文集")
    COLLECTION("collection", "文集"),
    @ApiModelProperty("用户")
    USER("user", "用户");

    @EnumValue
    private final String code;

    private final String desc;

    ResourceTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
