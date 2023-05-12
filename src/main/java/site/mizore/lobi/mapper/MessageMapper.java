package site.mizore.lobi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import site.mizore.lobi.entity.po.Message;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
