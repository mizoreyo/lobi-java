package site.mizore.lobi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.mizore.lobi.entity.po.Role;
import site.mizore.lobi.mapper.RoleMapper;
import site.mizore.lobi.service.RoleService;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
