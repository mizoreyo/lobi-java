package site.mizore.lobi.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Collection", description = "文集实体")
@TableName("t_collection")
public class Collection {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("文集id")
    private Long id;

    @ApiModelProperty("文集名")
    private String name;

    @ApiModelProperty("文集创建者id")
    private Long creator;

}
