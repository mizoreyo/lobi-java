package site.mizore.lobi.entity.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel(value = "SubjectParam", description = "专题创建参数")
public class SubjectParam {

    @NotEmpty
    @ApiModelProperty("专题名")
    private String name;

    @NotEmpty
    @ApiModelProperty("专题描述")
    private String descript;

}
