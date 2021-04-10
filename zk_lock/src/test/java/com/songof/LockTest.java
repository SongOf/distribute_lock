package com.songof;

import com.songof.lock.ZookeeperLockImpl;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author SongOf
 * @ClassName LockTest
 * @Description
 * @Date 2021/4/10 15:08
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LockTest {
    private int c = 0;

    @Test
    public void zookeeperLockTest() {
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("152.136.102.229:2181")
                .sessionTimeoutMs(1000)    // 连接超时时间
                .connectionTimeoutMs(1000) // 会话超时时间
                // 刚开始重试间隔为1秒，之后重试间隔逐渐增加，最多重试不超过三次
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        //创建分布式锁, 锁空间的根节点路径为/curator/lock
        InterProcessMutex mutex = new InterProcessMutex(client, "/curator/lock");
        try {
            mutex.acquire();
            mutex.acquire();
            //获得了锁, 进行业务流程
            System.out.println("Enter mutex");
            //完成业务流程, 释放锁
            mutex.release();
            mutex.release();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭客户端
            client.close();
        }
    }

    @Test
    public void zookeeperLockTest2() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("152.136.102.229:2181")
                .sessionTimeoutMs(1000)    // 连接超时时间
                .connectionTimeoutMs(1000) // 会话超时时间
                // 刚开始重试间隔为1秒，之后重试间隔逐渐增加，最多重试不超过三次
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                //创建分布式锁, 锁空间的根节点路径为/curator/lock
                InterProcessMutex mutex = new InterProcessMutex(client, "/curator/lock");
                try {
                    mutex.acquire();
                    //获得了锁, 进行业务流程
                    c += 3;
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getId() + "-aaaaa-" + c);
                    c -= 3;
                    //完成业务流程, 释放锁
                    mutex.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            try {
                Thread.sleep(200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void zookeeperLockTest3() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("152.136.102.229:2181")
                .sessionTimeoutMs(1000)    // 连接超时时间
                .connectionTimeoutMs(1000) // 会话超时时间
                // 刚开始重试间隔为1秒，之后重试间隔逐渐增加，最多重试不超过三次
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                //创建分布式锁, 锁空间的根节点路径为/curator/lock
                ZookeeperLockImpl zookeeperLock = new ZookeeperLockImpl("a123", client);
                try {
                    zookeeperLock.lock();
                    //获得了锁, 进行业务流程
                    c += 3;
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getId() + "-aaaaa-" + c);
                    c -= 3;
                    //完成业务流程, 释放锁
                    zookeeperLock.unlock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            try {
                Thread.sleep(200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void zookeeperLockTest4() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("152.136.102.229:2181")
                .sessionTimeoutMs(1000)    // 连接超时时间
                .connectionTimeoutMs(1000) // 会话超时时间
                // 刚开始重试间隔为1秒，之后重试间隔逐渐增加，最多重试不超过三次
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                //创建分布式锁, 锁空间的根节点路径为/curator/lock
                ZookeeperLockImpl zookeeperLock = new ZookeeperLockImpl("a123", client);
                try {
                    boolean b = zookeeperLock.tryLock(50, TimeUnit.MILLISECONDS);
                    if(b){
                        //获得了锁, 进行业务流程
                        c += 3;
                        System.out.println(Thread.currentThread().getId() + "-aaaaa-" + c);
                        c -= 3;
                        //完成业务流程, 释放锁
                        zookeeperLock.unlock();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
