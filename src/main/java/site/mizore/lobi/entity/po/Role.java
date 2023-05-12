package site.mizore.lobi.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_role")
@ApiModel(value = "Role", description = "角色实体")
public class Role {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("角色id")
    private Long id;

    @ApiModelProperty("角色名")
    private String name;

    @ApiModelProperty("角色描述")
    private String comment;

}
