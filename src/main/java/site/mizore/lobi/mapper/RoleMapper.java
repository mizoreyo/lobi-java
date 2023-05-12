package site.mizore.lobi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import site.mizore.lobi.entity.po.Role;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}
