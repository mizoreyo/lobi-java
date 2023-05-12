package site.mizore.lobi.entity.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "ThumbCreateParam", description = "点赞参数")
public class ThumbCreateParam {

    @NotNull
    @ApiModelProperty("文章id")
    private Long article;

}
