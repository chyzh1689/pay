package com.qh.system.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.qh.redis.RedisConstants;
import com.qh.redis.service.RedisUtil;
import com.qh.system.dao.ConfigDao;
import com.qh.system.domain.ConfigDO;
import com.qh.system.service.ConfigService;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigDao configDao;


    @Override
    @Cacheable(value = RedisConstants.cache_config, key = "#configItem")
    public ConfigDO get(String configItem) {
        return configDao.getByItem(configItem);
    }

    @Override
    public List<ConfigDO> list(Map<String, Object> map) {
        return configDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return configDao.count(map);
    }

    @Cacheable(value = RedisConstants.cache_config, key = "#config.configItem")
    public ConfigDO save(ConfigDO config) {
        RedisUtil.syncConfig(config, false);
        configDao.save(config);
        return config;
    }

    @CachePut(value = RedisConstants.cache_config, key = "#config.configItem")
    @Override
    public ConfigDO update(ConfigDO config) {
        RedisUtil.syncConfig(config, false);
        configDao.update(config);
        return config;
    }

    @Override
    public int remove(Integer id) {
        return configDao.remove(id);
    }

    @Override
    public int batchRemove(Integer[] ids) {
        return configDao.batchRemove(ids);
    }

    /*
     * <p>Title: batchRemove</p> <p>Description: </p>
     * @param ids
     * @param configItems
     * @param parentItems
     * @see com.qh.system.service.ConfigService#batchRemove(java.lang.Integer[], java.lang.String[])
     */

    @Override
    public void batchRemove(Integer[] ids, String[] configItems, String[] parentItems) {
        int result = configDao.batchRemove(ids);
        if (result > 0) {
            for (int i = 0; i < ids.length; i++) {
                RedisUtil.getRedisTemplate().delete(RedisConstants.cache_config + configItems[i]);
                RedisUtil.delConfig(configItems[i], parentItems[i]);
            }
        }
    }

    /*
     * <p>Title: remove</p> <p>Description: </p>
     * @param id
     * @param cofigItem
     * @return
     * @see com.qh.system.service.ConfigService#remove(java.lang.Integer, java.lang.String)
     */
    @CacheEvict(value = RedisConstants.cache_config, key = "#configItem")
    public int remove(Integer id, String configItem, String parentItem) {
        int result = configDao.remove(id);
        if (result > 0) {
            RedisUtil.delConfig(configItem, parentItem);
        }
        return result;
    }

    /*
     * <p>Title: exit</p> <p>Description: </p>
     * @param params
     * @return
     * @see com.qh.system.service.ConfigService#exit(java.util.Map)
     */

    @Override
    public boolean exit(Map<String, Object> params) {
        return configDao.list(params).size() > 0;
    }

}
