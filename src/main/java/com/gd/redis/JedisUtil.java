package com.gd.redis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.util.Pool;

/**
 * @author tanshuai
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class JedisUtil {

    private static Pool pool;
    public static Serializer stringSerializer = new StringSerializer();
    public static Serializer jdkSerializer = new JdkSerializer();

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        JedisUtil.pool = pool;
    }

    public void destroy() {
        pool.destroy();
    }

    @SuppressWarnings("static-access")
    public void setKeySerializer(Serializer keySerializer) {
        this.stringSerializer = keySerializer;
    }

    @SuppressWarnings("static-access")
    public void setValueSerializer(Serializer valueSerializer) {
        this.jdkSerializer = valueSerializer;
    }

    public static Jedis getClient() {
        return (Jedis) pool.getResource();
    }

    @SuppressWarnings("unchecked")
    public static void release(Jedis jedis) {
        pool.returnResource(jedis);
    }

    /**
     * 设置非字符串类型的值
     *
     * @return 状态码
     */
    public static String set(Object key, Object value) {
        Jedis jedis = getClient();
        String code;
        try {
            code = jedis.set(stringSerializer.serialize(key),
                    jdkSerializer.serialize(value));
        } finally {
            release(jedis);
        }
        return code;
    }

    /**
     * 设置非字符串类型的值(包含过期时间 单位秒)
     *
     * @return 状态码
     */
    public static String set(Object key, Object value, int timeout) {
        Jedis jedis = getClient();
        String code;
        try {
            code = jedis.setex(stringSerializer.serialize(key), timeout,
                    jdkSerializer.serialize(value));
        } finally {
            release(jedis);
        }
        return code;
    }

    /**
     * 取出列表中多条数据，并删除
     *
     * @param key      键值
     * @param count    条数
     * @param position 位置，0：左,1:右
     * @param <T>      对象泛型
     * @return 取出的列表集合
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> pop(Object key, int count, int position) {
        List<T> result = Collections.emptyList();
        Jedis jedis = getClient();
        try {
            byte[] bytes = stringSerializer.serialize(key);

            Pipeline pipelined = jedis.pipelined();
            // 判断从列表哪侧弹出
            if (position == 0) {
                for (int i = 0; i < count; i++) {
                    pipelined.lpop(bytes);
                }
            } else if (position == 1) {
                for (int i = 0; i < count; i++) {
                    pipelined.rpop(bytes);
                }
            }

            List<Object> objects = pipelined.syncAndReturnAll();
            result = new ArrayList<T>(objects.size());

            for (Object object : objects) {
                if (object != null)
                    result.add((T) jdkSerializer.deserialize((byte[]) object));
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            release(jedis);
            pool.returnResource(jedis);
        }

        log.info("result:{}", result);
        return result;
    }

    /**
     * 取Sorted Sets中内容，安装score从高到低排序，取出后自动删除
     *
     * @param end 从下标0开始，到下标end结束
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> zpop(Object key, int end) {
        List<T> result = Collections.emptyList();
        Jedis jedis = getClient();

        try {
            byte[] keyBytes = stringSerializer.serialize(key);
            Set<byte[]> set = jedis.zrevrange(keyBytes, 0, end);
            result = new ArrayList<T>(set.size());

            for (byte[] bytes : set) {
                result.add((T) jdkSerializer.deserialize(bytes));
                jedis.zrem(bytes);
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            release(jedis);
        }
        return result;
    }

    public static long zadd(Object key, Object value, double score) {
        Jedis jedis = getClient();
        long result;
        try {
            result = jedis.zadd(stringSerializer.serialize(key), score,
                    jdkSerializer.serialize(value));
        } finally {
            release(jedis);
        }
        return result;
    }

    /*
     * 取出list全部数据(不做删除)
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> lrange(Object key) {
        List<T> result = new ArrayList<T>();
        Jedis jedis = getClient();
        byte[] keyBytes = stringSerializer.serialize(key);
        try {
            List<byte[]> list = jedis.lrange(keyBytes, 0, -1);
            for (byte[] b : list) {
                result.add((T) jdkSerializer.deserialize(b));
            }
        } catch (Exception e) {
            log.error(key + "从redis里面取值出错:" + e.getMessage());
        } finally {
            release(jedis);
        }
        return result;
    }

    /*
     * 取出list全部数据(不做删除)
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getList(Object key) {
        Jedis jedis = getClient();
        List<T> result = new ArrayList<T>();
        byte[] keyBytes = stringSerializer.serialize(key);
        try {
            List<byte[]> list = jedis.lrange(keyBytes, 0, -1);
            for (byte[] b : list) {
                result.add((T) jdkSerializer.deserialize(b));
            }
//			jedis.del(keyBytes);
        } catch (Exception e) {
            log.error(key + "从redis里面取值出错:" + e.getMessage());
        }
        return result;
    }

    /**
     * 设置list的值 参数Object
     *
     * @return 状态码
     */
    public static Long setList(Object key, Object value) {
        Jedis jedis = getClient();
        long s = 0;
        try {
            s = jedis.rpush(stringSerializer.serialize(key),
                    jdkSerializer.serialize(value));
        } catch (Exception e) {
            log.error("设值过期时间出错，key:{},message:{}", key, e.getMessage());
        } finally {
            release(jedis);
        }
        return s;
    }

    /**
     * 设置list的值 参数Object
     *
     * @return 状态码
     */
    public static Long setList(Jedis jedis, Object key, List value) {
        long s = 0;
        try {
            // s = jedis.rpush(stringSerializer.serialize(key),
            // jdkSerializer.serialize(value));
            for (Object obj : value) {
                jedis.rpush(stringSerializer.serialize(key),
                        jdkSerializer.serialize(obj));
            }
        } catch (Exception e) {
            log.error("设值过期时间出错，key:{},message:{}", key, e.getMessage());
        }
        return s;
    }

    /*
     * 设置值(设置token专用) 用锁
     */
    public static void setLock(Object key, Object value) {
        try {
            Jedis jedis = getClient();
            JedisLock lock = new JedisLock(jedis, (String) key + "_Lock");
            if (lock.lock()) {
                jedis.set(stringSerializer.serialize(key),
                        jdkSerializer.serialize(value));
            }
            lock.unlock();
            release(jedis);
        } catch (Exception e) {
            log.error(key + "从redis设置值加锁出错:" + e.getMessage());
        }
    }

    /*
     * 获取值(获取token专用) 用锁
     */
    public static Object getLock(Object key) {
        Jedis jedis = getClient();
        JedisLock lock = new JedisLock(jedis, (String) key + "_Lock");
        try {
            if (lock.lock()) {
                byte[] b = jedis.get(stringSerializer.serialize(key));
                if (b != null) {
                    return jdkSerializer.deserialize(b);
                } else {
                    return null;
                }
            }
            return null;
        } catch (Exception e) {
            log.error(key + "redis获取值出错:" + e.getMessage());
            return null;
        } finally {
            lock.unlock();
            release(jedis);
        }
    }

    /*
     * 删除key
     */
    public static Long delete(Object key) {
        Jedis jedis = getClient();
        long s = 0;
        try {
            s = jedis.del(stringSerializer.serialize(key));
        } catch (Exception e) {
            log.error(key + "redis里面删除出错:" + e.getMessage());
        } finally {
            release(jedis);
        }
        return s;
    }

    public static void listDeleteByValue(String key, Object value) {
        Jedis jedis = getClient();
        try {
            jedis.lrem(stringSerializer.serialize(key), 1,
                    jdkSerializer.serialize(value));
        } catch (Exception e) {
            log.error(key + "redis里面list根据value删除出错:" + e.getMessage());
        } finally {
            release(jedis);
        }
    }

    /*
     * 通过key获取object
     */
    public static Object get(Object key) {
        Jedis jedis = getClient();
        try {
            byte[] b = jedis.get(stringSerializer.serialize(key));
            if (b != null) {
                return jdkSerializer.deserialize(b);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(key + "redis获取值出错:" + e.getMessage());
            return null;
        } finally {
            release(jedis);
        }
    }

    /*
     * 设置list的值 参数List
     */
    public static void setList(Object key, List list) {
        Jedis jedis = getClient();
        //先删除key
        jedis.del(key.toString());

        byte[] keyBytes = stringSerializer.serialize(key);
        Pipeline pipelined = jedis.pipelined();
        try {
            for (Object obj : list) {
                pipelined.rpush(keyBytes, jdkSerializer.serialize(obj));
            }
            pipelined.sync();
        } catch (Exception e) {
            log.error(key + "redis存储值出错:" + e.getMessage());
        } finally {
            release(jedis);
        }
    }

    /*
     * 延长时间
     */
    /*
     * 设置list的值 参数List
     */
    public static void expireTime(Object key, int time) {
        Jedis jedis = getClient();
        try {
            jedis.expire(stringSerializer.serialize(key), time);
        } catch (Exception e) {
            log.error(key + "redis延长会话出错:" + e.getMessage());
        } finally {
            release(jedis);
        }
    }

    /**
     * 设置list的值 参数Object
     *
     * @return 状态码
     */
    public static Long setSet(Object key, Object value) {
        Jedis jedis = getClient();
        long s = 0;
        try {
            s = jedis.sadd(stringSerializer.serialize(key),
                    jdkSerializer.serialize(value));
        } catch (Exception e) {
            log.error("设置set出错，key:{},message:{}", key, e.getMessage());
        } finally {
            release(jedis);
        }
        return s;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getSet(Object key) {
        List<T> result = new ArrayList<T>();
        Jedis jedis = getClient();
        byte[] keyBytes = stringSerializer.serialize(key);
        try {
            // List<byte[]> list = jedis.lrange(keyBytes, 0, -1);
            Set<byte[]> set = jedis.smembers(keyBytes);
            /*
             * for (byte[] b : list) { result.add((T)
             * jdkSerializer.deserialize(b)); }
             */
            for (byte[] b : set) {
                result.add((T) jdkSerializer.deserialize(b));
            }
        } catch (Exception e) {
            log.error(key + "set从redis里面取值出错:" + e.getMessage());
        } finally {
            release(jedis);
        }
        return result;
    }

    public static void setDeleteByValue(String key, Object value) {
        Jedis jedis = getClient();
        try {
            jedis.srem(stringSerializer.serialize(key),
                    jdkSerializer.serialize(value));
        } catch (Exception e) {
            log.error(key + "redis里面list根据value删除出错:" + e.getMessage());
        } finally {
            release(jedis);
        }
    }

}