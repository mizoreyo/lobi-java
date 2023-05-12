package site.mizore.lobi.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_user_role_relation")
@ApiModel(value = "UserRole", description = "用户角色实体")
public class UserRole {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("用户角色id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("角色id")
    private Long roleId;

}
