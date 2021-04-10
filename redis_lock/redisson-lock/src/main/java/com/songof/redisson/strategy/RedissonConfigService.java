package com.songof.redisson.strategy;

import com.songof.redisson.entity.RedissonProperties;
import org.redisson.config.Config;

/**
 * @author SongOf
 * @InterfaceName RedissonConfigService
 * @Description Redisson配置构建接口
 * @Date 2021/3/27 22:16
 * @Version 1.0
 */
public interface RedissonConfigService {

    /***
    * @Description 根据不同的Redis配置策略创建对应的Config
    * @Param [redissonProperties]
    * @return org.redisson.config.Config
    */
    public Config createRedissonConfig(RedissonProperties redissonProperties);
}