package site.mizore.lobi.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_subject")
@ApiModel(value = "Subject", description = "专题实体")
public class Subject {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("专题id")
    private Long id;

    @ApiModelProperty("专题名")
    private String name;

    @ApiModelProperty("描述")
    private String descript;

    @ApiModelProperty("创建者id")
    private Long creator;

    @ApiModelProperty("图标")
    private String logo;

}
