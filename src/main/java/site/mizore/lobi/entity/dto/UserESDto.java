package site.mizore.lobi.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "UserESDto", description = "用户在es中存储")
@Data
public class UserESDto {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("昵称")
    private String nickname;

}
