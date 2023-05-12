package site.mizore.lobi.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import site.mizore.lobi.entity.po.Subject;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "ArticleVO", description = "文章响应对象")
public class ArticleVO {

    @ApiModelProperty("文章id")
    private Long id;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章内容")
    private String content;

    @ApiModelProperty("作者id")
    private Long author;

    @ApiModelProperty("发布时间")
    private LocalDateTime date;

    @ApiModelProperty("更新时间")
    private LocalDateTime updated;

    @ApiModelProperty("专题id")
    private Long subject;

    @ApiModelProperty("作者信息")
    private UserCountVO authorInfo;

    @ApiModelProperty("专题信息")
    private Subject subjectInfo;


}
