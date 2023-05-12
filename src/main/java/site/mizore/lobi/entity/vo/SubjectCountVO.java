package site.mizore.lobi.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SubjectCountVO", description = "专题统计响应")
public class SubjectCountVO {

    @ApiModelProperty("专题id")
    private Long id;

    @ApiModelProperty("专题名")
    private String name;

    @ApiModelProperty("描述")
    private String descript;

    @ApiModelProperty("图标")
    private String logo;

    @ApiModelProperty("文章数")
    private Long articleCount;

    @ApiModelProperty("关注数")
    private Long subCount;

    @ApiModelProperty("创建者信息")
    private UserInfoVO creator;

}
