package site.mizore.lobi.entity.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import site.mizore.lobi.enums.ResourceTypeEnum;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "SubscribeOperateParam", description = "操作订阅参数")
public class SubscribeOperateParam {

    @NotNull
    @ApiModelProperty("资源id")
    private Long resource;

    @NotNull
    @ApiModelProperty("资源类型")
    private ResourceTypeEnum type;

}
