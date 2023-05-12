package site.mizore.lobi.entity.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "SubjectEditParam", description = "专题修改参数")
public class SubjectEditParam {

    @NotNull
    @ApiModelProperty("专题id")
    private Long id;

    @NotEmpty
    @ApiModelProperty("专题名")
    private String name;

    @ApiModelProperty("描述")
    private String descript;

    @ApiModelProperty("图标")
    private String logo;

}
