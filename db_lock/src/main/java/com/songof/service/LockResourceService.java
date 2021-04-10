package com.songof.service;

import com.songof.entity.LockResource;

/**
 * @author SongOf
 * @InterfaceName LockResourceService
 * @Description
 * @Date 2021/4/10 16:40
 * @Version 1.0
 */

public interface LockResourceService {
    /**
     * 非阻塞获取锁
     *
     * @param lockResource
     * @return
     */
    boolean lock(LockResource lockResource);

    /**
     * 释放锁
     *
     * @param lockResource
     * @return
     */
    boolean unlock(LockResource lockResource);
}
