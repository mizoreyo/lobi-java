package site.mizore.lobi.entity.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "CommentCreateParam", description = "新增评论参数")
public class CommentCreateParam {

    @NotNull
    @ApiModelProperty("文章id")
    private Long article;

    @NotEmpty
    @ApiModelProperty("评论内容")
    private String content;

    // 若为0则表示评论博主
    @NotNull
    @ApiModelProperty("父评论id")
    private Long parent;

}
