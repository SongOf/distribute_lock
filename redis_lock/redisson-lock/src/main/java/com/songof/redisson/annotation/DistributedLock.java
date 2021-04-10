package com.songof.redisson.annotation;

import java.lang.annotation.*;

/**
 * @author SongOf
 * @AnnotationName DistributedLock
 * @Description 基于注解的分布式式锁
 * @Date 2021/3/27 22:52
 * @Version 1.0
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DistributedLock {

    /**
     * 锁的名称
     */
    String value() default "redisson";

    /**
     * 锁的有效时间
     */
    int leaseTime() default 10;
}
