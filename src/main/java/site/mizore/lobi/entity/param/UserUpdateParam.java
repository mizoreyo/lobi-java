package site.mizore.lobi.entity.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value = "UserAddParam", description = "修改用户参数")
public class UserUpdateParam {

    @NotNull
    @ApiModelProperty("用户id")
    private Long id;

    @NotEmpty
    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("个人介绍")
    private String introduction;

    @ApiModelProperty("角色")
    private List<Long> roles;

}
