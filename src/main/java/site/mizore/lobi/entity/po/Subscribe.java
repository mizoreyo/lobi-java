package site.mizore.lobi.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import site.mizore.lobi.enums.ResourceTypeEnum;

@Data
@ApiModel(value = "Subscribe", description = "订阅实体")
@TableName("t_subscribe")
public class Subscribe {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("订阅id")
    private Long id;

    @ApiModelProperty("资源id")
    private Long resource;

    @ApiModelProperty("订阅者")
    private Long subscriber;

    @ApiModelProperty("资源类型")
    private ResourceTypeEnum type;

}
