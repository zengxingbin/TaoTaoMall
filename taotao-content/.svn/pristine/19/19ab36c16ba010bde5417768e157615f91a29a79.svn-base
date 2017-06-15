package com.taotao.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestJedisSpring {
    @Test
    public void testJedisClientPool() {
        //初始化spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-jedis.xml");
        //获取jedisClientPool
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        jedisClient.set("jedisClientPool", "I'm jedisClientPool");
        String string = jedisClient.get("jedisClientPool");
        System.out.println(string); 
    }
    @Test
    public void testJedisClientCluster() {
        //初始化spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-jedis.xml");
        //获取jedisClinetCluster
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        //使用jedisClient操作redis数据库
        jedisClient.set("jedisClientCluster", "hello,I'm jedisClinetCluster");
        String string = jedisClient.get("jedisClientCluster");
        System.out.println(string);
        
    }
}
