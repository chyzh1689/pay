package com.qh.system.service;

import com.qh.system.domain.ConfigDO;
import java.util.List;
import java.util.Map;

/**
 * 系统配置
 * 
 * @author chyzh
 * @email 3048427407@qq.com
 * @date 2017-10-26 17:12:22
 */
public interface ConfigService {

    ConfigDO get(String configItem);

    List<ConfigDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    ConfigDO save(ConfigDO config);

    ConfigDO update(ConfigDO config);

    int remove(Integer id);

    int batchRemove(Integer[] ids);

    /**
     * @Description 批量删除并更新缓存
     * @param ids
     * @param configItems
     * @param parentItems
     */
    void batchRemove(Integer[] ids, String[] configItems, String[] parentItems);

    /**
     * @Description 删除并更新缓存
     * @param id
     * @param cofigItem
     * @param parentItem
     * @return
     */
    int remove(Integer id, String cofigItem, String parentItem);

    /**
     * @Description 配置项是否存在
     * @param params
     * @return
     */
    boolean exit(Map<String, Object> params);
}
