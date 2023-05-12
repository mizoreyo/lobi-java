package site.mizore.lobi.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "SubjectESDto", description = "专题在es中存储")
@Data
public class SubjectESDto {

    @ApiModelProperty("专题id")
    private Long id;

    @ApiModelProperty("专题名")
    private String name;

}
