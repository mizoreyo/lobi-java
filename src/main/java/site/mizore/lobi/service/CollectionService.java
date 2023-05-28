package site.mizore.lobi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.mizore.lobi.common.CommonPage;
import site.mizore.lobi.entity.param.CollectionCreateParam;
import site.mizore.lobi.entity.param.CollectionEditParam;
import site.mizore.lobi.entity.po.Collection;
import site.mizore.lobi.entity.vo.CollectionCountVO;
import site.mizore.lobi.entity.vo.CollectionInfoVO;
import site.mizore.lobi.entity.vo.CollectionVO;

import java.util.List;

public interface CollectionService extends IService<Collection> {
    /**
     * 获取用户文集
     *
     * @return
     */
    List<CollectionInfoVO> getInfoList();

    /**
     * 用户新建文集
     *
     * @param param
     * @return
     */
    Collection createCollection(CollectionCreateParam param);

    /**
     * 用户删除文集
     *
     * @param id
     * @return
     */
    boolean deleteCollection(Long id);

    /**
     * 用户编辑文集
     *
     * @param param
     * @return
     */
    Collection editCollection(CollectionEditParam param);

    /**
     * 搜索文集
     *
     * @param q
     * @param page
     * @param size
     * @return
     */
    CommonPage<CollectionCountVO> searchCollections(String q, Integer page, Integer size);

    /**
     * 获取文集统计数据
     *
     * @param id
     * @return
     */
    CollectionCountVO collectionCountInfo(Long id);

    /**
     * 根据id列表
     *
     * @param ids
     * @return
     */
    List<CollectionCountVO> listByIds(List<Long> ids);

    /**
     * 获取文集分页
     *
     * @param page
     * @param size
     * @param q
     * @return
     */
    CommonPage<CollectionVO> page(Integer page, Integer size, String q);

    /**
     * 获取文集
     *
     * @param id
     * @return
     */
    CollectionVO get(Long id);

    /**
     * 修改文集
     *
     * @param param
     */
    void updateCollection(CollectionEditParam param);

    /**
     * 删除文集
     *
     * @param id
     */
    void delete(Long id);
}
