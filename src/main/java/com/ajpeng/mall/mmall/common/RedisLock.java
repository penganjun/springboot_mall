package com.ajpeng.mall.mmall.common;

import java.util.concurrent.TimeUnit;

public interface  RedisLock {
    //加锁操作
    boolean tryLock(String key, long timeout, TimeUnit unit);
    //解锁操作
    void releaseLock(String key);
}
