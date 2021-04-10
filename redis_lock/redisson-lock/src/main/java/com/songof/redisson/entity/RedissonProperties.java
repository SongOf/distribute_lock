package com.songof.redisson.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author SongOf
 * @ClassName RedissonProperties
 * @Description redis配置信息实体类
 * @Date 2021/3/27 11:22
 * @Version 1.0
 */
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "redisson.lock.server")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedissonProperties {
    /***
    * redis主机地址，ip：port，有多个用半角逗号分隔
    */
    private String address;
    /***
     * 连接类型，支持standalone-单机节点，sentinel-哨兵，cluster-集群，masterslave-主从
     */
    private String type;
    /***
     * 密码
     */
    private String password;
    /***
     * 选择的数据库
     */
    private int database;

    public RedissonProperties setPassword(String password) {
        this.password = password;
        return this;
    }

    public RedissonProperties setDatabase(int database) {
        this.database = database;
        return this;
    }
}
