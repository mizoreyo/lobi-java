package site.mizore.lobi.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import site.mizore.lobi.entity.po.Collection;
import site.mizore.lobi.entity.po.Subject;

import java.time.LocalDateTime;

@Data
@ApiModel
public class ArticleManageVO {

    @ApiModelProperty("文章id")
    private Long id;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章内容")
    private String content;

    @ApiModelProperty("作者")
    private UserInfoVO author;

    @ApiModelProperty("发布时间")
    private LocalDateTime date;

    @ApiModelProperty("更新时间")
    private LocalDateTime updated;

    @ApiModelProperty("专题")
    private Subject subject;

    @ApiModelProperty("文集")
    private Collection collection;

    @ApiModelProperty("是否发布")
    private Integer publish;

    @ApiModelProperty("浏览量")
    private Integer views;

}
