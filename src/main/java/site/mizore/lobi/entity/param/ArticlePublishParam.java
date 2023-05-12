package site.mizore.lobi.entity.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "ArticlePublishParam", description = "发布文章参数")
public class ArticlePublishParam {

    @NotNull
    @ApiModelProperty("文章id")
    private Long id;

    @NotEmpty
    @ApiModelProperty("文章标题")
    private String title;

    @NotEmpty
    @ApiModelProperty("文章内容")
    private String content;

    @ApiModelProperty("专题id")
    private Long subject;

}
