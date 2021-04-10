package com.songof.redisson;

import com.google.common.base.Preconditions;
import com.songof.redisson.constant.RedisConnectionType;
import com.songof.redisson.entity.RedissonProperties;
import com.songof.redisson.strategy.RedissonConfigService;
import com.songof.redisson.strategy.impl.ClusterConfigImpl;
import com.songof.redisson.strategy.impl.MasterslaveConfigImpl;
import com.songof.redisson.strategy.impl.SentineConfigImpl;
import com.songof.redisson.strategy.impl.StandaloneConfigImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;

/**
 * @author SongOf
 * @ClassName RedissonManager
 * @Description Redisson核心配置，用于提供初始化的redisson实例
 * @Date 2021/3/26 23:52
 * @Version 1.0
 */
@Slf4j
@Data
public class RedissonManager {
    private Config config = new Config();
    private Redisson redisson = null;

    public RedissonManager() {}

    public RedissonManager(RedissonProperties redissonProperties) {
        try {
            config = RedissonConfigFactory.getInstance().createConfig(redissonProperties);
            redisson = (Redisson)Redisson.create(config);
        } catch (Exception e) {
            log.error("Redisson init error", e);
            throw new IllegalArgumentException("please input correct configurations," +
                    "connectionType must in standalone/sentinel/cluster/masterslave");
        }
    }

    /***
    * @Description Redisson连接方式配置工厂
    * 双重检查锁
    */
    public static class RedissonConfigFactory {
        private RedissonConfigFactory() {}

        private static volatile RedissonConfigFactory factory = null;

        public static RedissonConfigFactory getInstance() {
            if(factory == null) {
                synchronized (Object.class) {
                    if(factory == null) {
                        factory = new RedissonConfigFactory();
                    }
                }
            }

            return factory;
        }

        /***
        * @Description 根据连接类型获取对应连接方式的配置,基于策略模式
        * @Param [redissonProperties]
        * @return org.redisson.config.Config
        */
        public Config createConfig(RedissonProperties redissonProperties) {
            Preconditions.checkNotNull(redissonProperties);
            Preconditions.checkNotNull(redissonProperties.getAddress(), "redisson.lock.server.address cannot be NULL!");
            Preconditions.checkNotNull(redissonProperties.getType(), "redisson.lock.server.password cannot be NULL");
            Preconditions.checkNotNull(redissonProperties.getDatabase(), "redisson.lock.server.database cannot be NULL");
            String connectionType = redissonProperties.getType();

            RedissonConfigService redissonConfigService = null;
            if (connectionType.equals(RedisConnectionType.STANDALONE.getConnection_type())) {
                redissonConfigService = new StandaloneConfigImpl();
            } else if (connectionType.equals(RedisConnectionType.SENTINEL.getConnection_type())) {
                redissonConfigService = new SentineConfigImpl();
            } else if (connectionType.equals(RedisConnectionType.CLUSTER.getConnection_type())) {
                redissonConfigService = new ClusterConfigImpl();
            } else if (connectionType.equals(RedisConnectionType.MASTERSLAVE.getConnection_type())) {
                redissonConfigService = new MasterslaveConfigImpl();
            } else {
                throw new IllegalArgumentException("创建Redisson连接Config失败！当前连接方式:" + connectionType);
            }
            return redissonConfigService.createRedissonConfig(redissonProperties);
        }
    }
}
