package site.mizore.lobi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import site.mizore.lobi.entity.po.Subscribe;

import java.util.List;

@Mapper
public interface SubscribeMapper extends BaseMapper<Subscribe> {

    /**
     * 获取用户未关注的前五个关注数高的用户的id
     *
     * @param user
     * @return
     */
    List<Long> getUnsubUserIdsBySubCount(@Param("user") Long user);

}
