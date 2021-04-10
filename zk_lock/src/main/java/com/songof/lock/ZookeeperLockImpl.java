package com.songof;

import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;

/**
 * @author SongOf
 * @ClassName ZookeeperLockImpl
 * @Description
 * @Date 2021/4/9 23:40
 * @Version 1.0
 */
public class ZookeeperLockImpl implements Lock {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperLockImpl.class);

    public void lock() {

    }

    public void lockInterruptibly() throws InterruptedException {

    }

    public boolean tryLock() {
        return false;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    public void unlock() {

    }

    public Condition newCondition() {
        return null;
    }
}
