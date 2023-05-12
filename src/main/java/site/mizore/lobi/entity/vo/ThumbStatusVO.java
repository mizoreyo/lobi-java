package site.mizore.lobi.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ThumbStatusVO", description = "点赞状态响应")
public class ThumbStatusVO {

    @ApiModelProperty("点赞状态")
    private Boolean status;

    @ApiModelProperty("点赞数")
    private Long count;

}
