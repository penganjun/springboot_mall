package com.ajpeng.mall.mmall.controller;

import com.ajpeng.mall.mmall.common.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisLock redisLock;

    //下面的代码在高并发环境下是线程安全的吗？答案肯定不是线程安全的，因为上述扣减库存的操作会出现并行执行的情况。
    //用jmeter测试如下：
    //2020-10-19 14:42:56,696 - 库存扣减成功，当前库存为：49
    //2020-10-19 14:42:56,697 - 库存扣减成功，当前库存为：49
    //2020-10-19 14:43:00,703 - 库存扣减成功，当前库存为：48
    @RequestMapping("/submitOrder")
    public String submitOrder() throws Exception {
        int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
        if(stock > 0){
            stock -= 1;
            stringRedisTemplate.opsForValue().set("stock", String.valueOf(stock));
            log.info("库存扣减成功，当前库存为：{}", stock);
        }else{
            log.error("库存不足，扣减库存失败");
            return "failure";
        }
        return "success";
    }

    //假设当线程A首先执行stringRedisTemplate.opsForValue()的setIfAbsent()方法返回true，
    // 继续向下执行，正在执行业务代码时，抛出了异常，线程A直接退出了JVM。
    // 此时，stringRedisTemplate.delete(PRODUCT_ID);代码还没来得及执行，
    // 之后所有的线程进入提交订单的方法时，调用stringRedisTemplate.opsForValue()的setIfAbsent()方法都会返回false。
    // 导致后续的所有下单操作都会失败。这就是分布式场景下的死锁问题

    /**
     * 为了演示方便，我这里就简单定义了一个常量作为商品的id
     * 实际工作中，这个商品id是前端进行下单操作传递过来的参数
     */
    public static final String PRODUCT_ID = "100001";
    @RequestMapping("/submitOrder1")
    public String submitOrder1() throws Exception {
        //通过stringRedisTemplate来调用Redis的SETNX命令，key为商品的id，value为字符串“binghe”
        //实际上，value可以为任意的字符换
        Boolean isLocked = stringRedisTemplate.opsForValue().setIfAbsent(PRODUCT_ID, "binghe");
        //没有拿到锁，返回下单失败
        if(!isLocked){
            log.error("获取锁失败");
            return "failure";
        }
        int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
        if(stock > 0){
            stock -= 1;
            stringRedisTemplate.opsForValue().set("stock", String.valueOf(stock));
            log.info("库存扣减成功，当前库存为：{}", stock);
        }else{
            log.error("库存不足，扣减库存失败");
        }
        //业务执行完成，删除PRODUCT_ID key
        stringRedisTemplate.delete(PRODUCT_ID);
        return "success";
    }

    //下面代码是否真正解决了死锁的问题呢？我们在写代码时，不能只盯着代码本身，觉得上述代码没啥问题了。
    // 实际上，生产环境是非常复杂的。如果线程在成功加锁之后，执行业务代码时，还没来得及执行删除锁标志的代码，
    // 此时，服务器宕机了，程序并没有优雅的退出JVM。也会使得后续的线程进入提交订单的方法时，因无法成功的设置锁标志位而下单失败。所以说，上述的代码仍然存在问题。
    @RequestMapping("/submitOrder2")
    public String submitOrder2(){
        //通过stringRedisTemplate来调用Redis的SETNX命令，key为商品的id，value为字符串“binghe”
        //实际上，value可以为任意的字符换
        Boolean isLocked = stringRedisTemplate.opsForValue().setIfAbsent(PRODUCT_ID, "binghe");
        //没有拿到锁，返回下单失败
        if(!isLocked){
            log.error("获取锁失败");
            return "failure";
        }
        try{
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if(stock > 0){
                stock -= 1;
                stringRedisTemplate.opsForValue().set("stock", String.valueOf(stock));
                log.info("库存扣减成功，当前库存为：{}", stock);
            }else{
                log.error("库存不足，扣减库存失败");
            }
        }finally{
            //业务执行完成，删除PRODUCT_ID key
            stringRedisTemplate.delete(PRODUCT_ID);
        }
        return "success";
    }

    //我们在下单操作的方法中为分布式锁引入了超时机制，此时的代码还是无法真正避免死锁的问题，
    // 那“坑位”到底在哪里呢？试想，当程序执行完stringRedisTemplate.opsForValue().setIfAbsent()方法后，
    // 正要执行stringRedisTemplate.expire(PRODUCT_ID, 30, TimeUnit.SECONDS)代码时，服务器宕机了，你还别说，
    // 生产坏境的情况非常复杂，就是这么巧，服务器就宕机了。此时，后续请求进入提交订单的方法时，
    // 都会因为无法成功设置锁标志而导致后续下单流程无法正常执行。

    //引入Redis超时机制
    @RequestMapping("/submitOrder3")
    public String submitOrder3(){
        //通过stringRedisTemplate来调用Redis的SETNX命令，key为商品的id，value为字符串“binghe”
        //实际上，value可以为任意的字符换
        Boolean isLocked = stringRedisTemplate.opsForValue().setIfAbsent(PRODUCT_ID, "binghe");
        //没有拿到锁，返回下单失败
        if(!isLocked){
            log.error("获取锁失败");
            return "failure";
        }
        try{
            stringRedisTemplate.expire(PRODUCT_ID, 30, TimeUnit.SECONDS);
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if(stock > 0){
                stock -= 1;
                stringRedisTemplate.opsForValue().set("stock", String.valueOf(stock));
                log.info("库存扣减成功，当前库存为：{}", stock);
            }else{
                log.error("库存不足，扣减库存失败");
            }
        }finally{
            //业务执行完成，删除PRODUCT_ID key
            stringRedisTemplate.delete(PRODUCT_ID);
        }
        return "success";
    }

    @RequestMapping("/submitOrder4")
    public String submitOrder4(){
        //通过stringRedisTemplate来调用Redis的SETNX命令，key为商品的id，value为字符串“binghe”
        //实际上，value可以为任意的字符换
        Boolean isLocked = stringRedisTemplate.opsForValue().setIfAbsent(PRODUCT_ID, "binghe", 30, TimeUnit.SECONDS);
        //没有拿到锁，返回下单失败
        if(!isLocked){
            return "failure";
        }
        try{
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if(stock > 0){
                stock -= 1;
                stringRedisTemplate.opsForValue().set("stock", String.valueOf(stock));
                log.info("库存扣减成功，当前库存为：{}", stock);
            }else{
                log.error("库存不足，扣减库存失败");
            }
        }finally{
            //业务执行完成，删除PRODUCT_ID key
            stringRedisTemplate.delete(PRODUCT_ID);
        }
        return "success";
    }

    @RequestMapping("/submitOrder5")
    public String submitOrder5(){
        try {
            boolean isLock = redisLock.tryLock(PRODUCT_ID, 30, TimeUnit.SECONDS);
            if(isLock){
                log.info("lock success");
            }else{
                log.error("lock failure");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisLock.releaseLock(PRODUCT_ID);
        }
        return "success";
    }
}
