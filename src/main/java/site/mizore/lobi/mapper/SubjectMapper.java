package site.mizore.lobi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import site.mizore.lobi.entity.po.Subject;

import java.util.List;

@Mapper
public interface SubjectMapper extends BaseMapper<Subject> {

    List<Subject> queryHotSubjects(@Param("num") Integer num);

}
