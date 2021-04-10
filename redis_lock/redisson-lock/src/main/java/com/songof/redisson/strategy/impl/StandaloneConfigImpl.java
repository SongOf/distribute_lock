package com.songof.redisson.strategy.impl;

import com.songof.redisson.constant.GlobalConstant;
import com.songof.redisson.entity.RedissonProperties;
import com.songof.redisson.strategy.RedissonConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;

/**
 * @author SongOf
 * @ClassName StandaloneConfigImpl
 * @Description
 * @Date 2021/3/27 22:16
 * @Version 1.0
 */
@Slf4j
public class StandaloneConfigImpl implements RedissonConfigService {

    public Config createRedissonConfig(RedissonProperties redissonProperties) {

        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            int database = redissonProperties.getDatabase();
            String redisAddr = GlobalConstant.REDIS_CONNECTION_PREFIX.getConstant_value() + address;
            config.useSingleServer().setAddress(redisAddr);
            config.useSingleServer().setDatabase(database);
            //密码可以为空
            if (StringUtils.isNotBlank(password)) {
                config.useSingleServer().setPassword(password);
            }
            log.info("初始化[单机部署]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("单机部署 Redisson init error", e);
        }
        return config;
    }
}
