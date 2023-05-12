package site.mizore.lobi.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "Comment", description = "评论实体")
@TableName("t_comment")
public class Comment {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("评论id")
    private Long id;

    @ApiModelProperty("文章id")
    private Long article;

    @ApiModelProperty("用户id")
    private Long user;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("父评论id")
    private Long parent;

    @ApiModelProperty("评论时间")
    private LocalDateTime date;

}
