package site.mizore.lobi.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SubStatusVO", description = "订阅状态响应")
public class SubStatusVO {

    @ApiModelProperty("订阅状态")
    private Boolean status;

    @ApiModelProperty("关注数")
    private Long count;

}
