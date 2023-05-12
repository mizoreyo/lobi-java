package site.mizore.lobi.entity.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "ArticleCreateParam", description = "创建文章参数")
public class ArticleCreateParam {

    @NotEmpty
    @ApiModelProperty("文章标题")
    private String title;

    @NotNull
    @ApiModelProperty("文集id")
    private Long collection;

}
