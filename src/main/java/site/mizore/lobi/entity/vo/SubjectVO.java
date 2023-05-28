package site.mizore.lobi.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SubjectVO", description = "专题响应")
public class SubjectVO {

    @ApiModelProperty("专题id")
    private Long id;

    @ApiModelProperty("专题名")
    private String name;

    @ApiModelProperty("描述")
    private String descript;

    @ApiModelProperty("专题作者")
    private UserInfoVO creator;

    @ApiModelProperty("图标")
    private String logo;

}
