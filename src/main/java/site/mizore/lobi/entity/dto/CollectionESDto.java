package site.mizore.lobi.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "CollectionESDto", description = "文集在es中存储")
@Data
public class CollectionESDto {

    @ApiModelProperty("文集id")
    private Long id;

    @ApiModelProperty("文集名")
    private String name;

}
