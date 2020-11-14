package com.ajpeng.mall.mmall.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
public class UpdateLockTimeoutTask implements Runnable {
    //uuid
    private String uuid;
    private StringRedisTemplate stringRedisTemplate;
    private String key;
    public UpdateLockTimeoutTask(String uuid, StringRedisTemplate stringRedisTemplate, String key){
        this.uuid = uuid;
        this.stringRedisTemplate = stringRedisTemplate;
        this.key = key;
    }

    @Override
    public void run() {
        //以uuid为key，当前线程id为value保存到Redis中
        stringRedisTemplate.opsForValue().set(uuid, String.valueOf(Thread.currentThread().getId()),60*60,TimeUnit.SECONDS);
        //定义更新锁的过期时间
        while (true) {
            stringRedisTemplate.expire(key, 30, TimeUnit.SECONDS);
            try {
                //每隔10秒执行一次
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                log.error("thread interrupted");
            }
        }
    }
}
