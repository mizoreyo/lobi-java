package site.mizore.lobi.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "CollectionCountVO", description = "文集统计响应")
public class CollectionCountVO {

    @ApiModelProperty("文集id")
    private Long id;

    @ApiModelProperty("文集名")
    private String name;

    @ApiModelProperty("文章数")
    private Long articleCount;

    @ApiModelProperty("关注数")
    private Long subCount;

    @ApiModelProperty("创建者信息")
    private UserInfoVO creator;

}
