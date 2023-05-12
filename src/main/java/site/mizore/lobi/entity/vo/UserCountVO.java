package site.mizore.lobi.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import site.mizore.lobi.entity.po.Collection;
import site.mizore.lobi.entity.po.Subject;

import java.util.List;

@Data
@ApiModel(value = "UserCountVO", description = "用户统计信息响应")
public class UserCountVO {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("个人介绍")
    private String introduction;

    @ApiModelProperty("文章数")
    private Long articleCount;

    @ApiModelProperty("粉丝数")
    private Long subCount;

    @ApiModelProperty("关注数")
    private Long followCount;

    @ApiModelProperty("文集信息")
    private List<Collection> collections;

    @ApiModelProperty("专题信息")
    private List<Subject> subjects;

}
