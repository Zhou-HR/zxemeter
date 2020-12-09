package com.gd.redis;

import java.util.Set;

import redis.clients.jedis.Jedis;

/**
 * @author ZhouHR
 */
public class Test {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Jedis jedis = new Jedis("120.55.163.230", 6379);
//		jedis.auth("password");
        jedis.connect();

        Set<String> keys = jedis.keys("*");

        jedis.disconnect();
    }

}
