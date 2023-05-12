package site.mizore.lobi.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "Article", description = "文章对象")
@TableName("t_article")
public class Article {

    @TableId(type = IdType.AUTO)
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

    @ApiModelProperty("文集id")
    private Long collection;

    @ApiModelProperty("是否发布")
    private Integer publish;

    @ApiModelProperty("浏览量")
    private Integer views;

}
