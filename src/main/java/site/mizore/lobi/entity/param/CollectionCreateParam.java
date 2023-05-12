package site.mizore.lobi.entity.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel(value = "CollectionAddParam", description = "创建文集参数")
public class CollectionCreateParam {

    @NotEmpty
    @ApiModelProperty("文集名")
    private String name;

}
