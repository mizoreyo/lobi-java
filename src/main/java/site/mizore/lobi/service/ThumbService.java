package site.mizore.lobi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.mizore.lobi.entity.param.ThumbCreateParam;
import site.mizore.lobi.entity.po.Thumb;
import site.mizore.lobi.entity.vo.ThumbStatusVO;

public interface ThumbService extends IService<Thumb> {
    /**
     * 点赞
     *
     * @param param
     * @return
     */
    Thumb createThumb(ThumbCreateParam param);

    /**
     * 查询点赞状态
     *
     * @param article
     * @return
     */
    ThumbStatusVO thumbStatus(Long article);

    /**
     * 取消点赞
     *
     * @param article
     * @return
     */
    boolean deleteThumb(Long article);
}
