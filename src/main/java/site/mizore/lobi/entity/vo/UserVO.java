package site.mizore.lobi.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import site.mizore.lobi.entity.po.Role;

import java.util.List;

@Data
@ApiModel(value = "UserVO", description = "用户响应")
public class UserVO {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("个人介绍")
    private String introduction;

    @ApiModelProperty("角色")
    private List<Role> roles;

}
