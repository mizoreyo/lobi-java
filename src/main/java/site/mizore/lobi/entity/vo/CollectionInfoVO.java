package site.mizore.lobi.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import site.mizore.lobi.entity.po.Article;

import java.util.List;

@Data
@ApiModel(value = "CollectionInfo", description = "文集信息响应")
public class CollectionInfoVO {

    @ApiModelProperty("文集id")
    private Long id;

    @ApiModelProperty("文集名")
    private String name;

    @ApiModelProperty("文集创建者id")
    private Long creator;

    @ApiModelProperty("文章列表")
    private List<Article> articles;

}
