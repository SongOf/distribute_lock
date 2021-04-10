package com.songof;

import com.songof.entity.LockResource;
import com.songof.lock.MysqlLockImpl;
import com.songof.mapper.LockResourceMapper;
import com.songof.service.LockResourceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * @author SongOf
 * @ClassName LockTest
 * @Description
 * @Date 2021/4/10 17:01
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LockTest {
    @Autowired
    private LockResourceService lockResourceService;

    private int c = 0;
    private String addr = null;

    @Test
    public void contextLoads() {
        try {
            addr = InetAddress.getLocalHost().getHostAddress();//获得本机IP
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                MysqlLockImpl mysqlLock = new MysqlLockImpl("a123", lockResourceService);
                boolean ret = false;
                ret = mysqlLock.tryLock(1225L, TimeUnit.MILLISECONDS);
                if (ret) {
                    c += 3;
                    System.out.println(Thread.currentThread().getId() + "-aaaaa-" + c);
                    c -= 3;
                }
                mysqlLock.unlock();
            }).start();
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deadLockTest() {
        LockResource lockResource = new LockResource();
        lockResource.setResourceName("a1234");
        try {
            addr = InetAddress.getLocalHost().getHostAddress();//获得本机IP
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        lockResource.setNodeInfo(addr + "-" + Thread.currentThread().getId() + "");
        lockResource.setCount(1L);
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                lockResource.setNodeInfo(addr + "-" + Thread.currentThread().getId() + "");
                lockResourceService.lock(lockResource);
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
}
