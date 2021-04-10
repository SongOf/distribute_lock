# 分布式锁

## 内容

- [`1. DB实现分布式锁`]
- [`2. Redis实现分布式锁`]
- [`3. Zookeeper实现分布式锁`]

## 分布式锁特点
- 互斥
```
同一时刻只能有一个线程获得锁
```
- 共享
```$xslt
读写锁的读锁即是共享锁
```
- 防止死锁
```$xslt
在分布式高并发的条件下，比如有个线程获得锁的同时，还没有来得及去释放锁，就因为系统故障或者其它原因使它无法执行释放锁的命令,导致其它线程都无法获得锁，造成死锁。
所以分布式非常有必要设置锁的有效时间，确保系统出现故障后，在一定时间内能够主动去释放锁，避免造成死锁的情况
```
- 性能
```$xslt
对于访问量大的共享资源，需要考虑减少锁等待的时间，避免导致大量线程阻塞。

所以在锁的设计时，需要考虑两点。
1、锁的颗粒度要尽量小。比如你要通过锁来减库存，那这个锁的名称你可以设置成是商品的ID,而不是任取名称。这样这个锁只对当前商品有效,锁的颗粒度小。
2、锁的范围尽量要小。比如只要锁2行代码就可以解决问题的，那就不要去锁10行代码了。
```
- 阻塞或非阻塞
```$xslt
线程获取锁失败后产生阻塞或直接返回失败
```
- 重入
```$xslt
同一个线程可以重复拿到同一个资源的锁。重入锁非常有利于资源的高效利用
```
- 公平锁
```$xslt
支持公平和非公平(可选)，线程按顺序获取锁或竞争获取
```
## DB分布式锁实现方案
### 锁表
```$xslt
CREATE TABLE `mysql_lock`.`sys_lock_resource`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `resource_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源名称',
  `count` int(11) NOT NULL COMMENT '锁次数，用于可重入',
  `node_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '机器或线程ID，用于可重入',
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '额外描述',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `lock_resource_name_uniqe`(`resource_name`) USING BTREE COMMENT '资源名称唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
```
### 加锁
```$xslt
查询数据库锁表看锁是否存在
存在则判断是不是相同节点和线程，相同则重入加1，不同则返回false
不存在则插入新的锁
```
### 解锁
```$xslt
查询数据库锁表看锁是否存在
存在则判断是不是相同节点和线程，相同则重入减1或删除，不同则返回false
不存在则返回false
```
```$xslt
使用数据库唯一索引保证互斥
使用数据库读写，性能不好
需要自己处理锁超时
```

## Redis分布式锁实现方案---Redisson实现

###原理
[`Redisson分布式锁的实现原理`](https://www.jianshu.com/p/47fd7f86c848)

## zookeeper分布式锁实现方案
```$xslt
使用zookeeper有序节点的特性保证互斥
使用临时节点处理未主动释放锁的情况
使用事件监听特性，释放锁时zookeeper会唤醒下一需要锁的线程(对比redis需要循环监听)，因此Zookeeper分布式锁是公平锁
```