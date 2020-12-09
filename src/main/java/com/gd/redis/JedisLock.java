package com.gd.redis;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @author ZhouHR
 */
@Slf4j
public class JedisLock {

    private Jedis jedis;
    private String lockKey = "lock";
    /**
     * 获取锁的过程中，最大耗时，单位毫秒
     */
    private int timeout = 5 * 1000;
    /**
     * 锁的有效时间，单位毫秒
     */
    private int expire = 60 * 1000;
    private boolean locked = false;

    public JedisLock(Jedis jedis) {
        this.jedis = jedis;
    }

    public JedisLock(Jedis jedis, String key) {
        this.jedis = jedis;
        this.lockKey = key;
    }

    public JedisLock(Jedis jedis, String key, int timeout, int expire) {
        this.jedis = jedis;
        this.lockKey = key;
        this.timeout = timeout;
        this.expire = expire;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * 获取锁，默认超时时间为5 * 1000毫秒，锁的有效时间为60 * 1000毫秒
     *
     * @return 成功获取锁，返回true，否则返回false
     */
    public boolean lock() {
        return lock(timeout, expire);
    }

    /**
     * 获取锁
     *
     * @param timeout 获取锁的过程中，最大耗时，单位毫秒
     * @param expire  该锁的有效时间，单位毫秒
     * @return 成功获取锁，返回true，否则返回false
     */
    public boolean lock(int timeout, int expire) {
        try {
            while (timeout >= 0) {
                if (jedis.setnx(lockKey, "") == 1) {
                    jedis.expire(lockKey, expire);
                    locked = true;
                    return locked;
                }

                timeout -= 100;
                Thread.sleep(100);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return false;
    }

    /**
     * lock release
     */
    public void unlock() {
        try {
            if (locked) {
                jedis.del(lockKey);
                locked = false;
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
