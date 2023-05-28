package site.mizore.lobi.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel
public class CommentManageVO {

    @ApiModelProperty("评论id")
    private Long id;

    @ApiModelProperty("父评论id")
    private Long parent;

    @ApiModelProperty("文章")
    private ArticleViewsVO article;

    @ApiModelProperty("用户")
    private UserInfoVO user;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("评论时间")
    private LocalDateTime date;

}
