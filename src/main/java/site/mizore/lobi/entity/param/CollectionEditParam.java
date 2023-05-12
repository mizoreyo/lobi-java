package site.mizore.lobi.entity.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "CollectionEditParam", description = "编辑文集参数")
public class CollectionEditParam {

    @NotNull
    @ApiModelProperty("文集id")
    private Long id;

    @NotEmpty
    @ApiModelProperty("文集名")
    private String name;

}
