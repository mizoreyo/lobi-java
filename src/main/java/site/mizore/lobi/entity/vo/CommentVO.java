package site.mizore.lobi.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "CommentVO", description = "评论响应")
public class CommentVO {

    @ApiModelProperty("评论id")
    private Long id;

    @ApiModelProperty("文章id")
    private Long article;

    @ApiModelProperty("用户id")
    private Long user;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("父评论id")
    private Long parent;

    @ApiModelProperty("昵称")
    private String parentNickname;

    @ApiModelProperty("评论时间")
    private LocalDateTime date;

    @ApiModelProperty("子评论")
    private List<CommentVO> children;

}
