package site.mizore.lobi.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "CollectionVO", description = "文集响应")
public class CollectionVO {

    @ApiModelProperty("文集id")
    private Long id;

    @ApiModelProperty("文集名")
    private String name;

    @ApiModelProperty("文集作者")
    private UserInfoVO creator;

}
