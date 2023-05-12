package site.mizore.lobi.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ArticleSummaryVO", description = "文章简介对象")
public class ArticleSummaryVO {

    @ApiModelProperty("文章id")
    private Long id;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("简介")
    private String summary;

    @ApiModelProperty("作者信息")
    private UserInfoVO authorInfo;

}
