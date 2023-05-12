package site.mizore.lobi.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "ESPage", description = "es返回的分页结果")
public class CommonPage<T> {

    @ApiModelProperty("总数")
    private Long total;

    @ApiModelProperty("数据")
    List<T> data;

}
