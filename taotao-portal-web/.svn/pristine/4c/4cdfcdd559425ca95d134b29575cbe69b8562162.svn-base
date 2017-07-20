package com.taotao.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestJedis {
    @Test
    public void testJedis() {
        //创建Jedis客户端，明确服务端ip和端口号
        Jedis jedis = new Jedis("192.168.19.99",6379);
        //通过客户端操作redis数据库
        jedis.set("name", "tom");
        String name = jedis.get("name");
        System.out.println(name);
        //关闭客户端
        jedis.close();
    }
    //使用jedis连接池
    @Test
    public void testJedisPool() {
        //创建jedis连接池,明确服务端ip和端口号
        JedisPool jedisPool = new JedisPool("192.168.19.99",6379);
        //获取连接
        Jedis jedis = jedisPool.getResource();//（方法级别上使用）
        //操作redis数据库
        jedis.set("sex", "male");
        String sex = jedis.get("sex");
        System.out.println(sex);
        //关闭连接
        jedis.close();
        //关闭连接池
        jedisPool.close();
    }
    //使用jedisCluster集群
    @Test
    public void testJedisCluster() {
        //创建ip和port的set集合即（HostAndPort的set集合）
        Set<HostAndPort> nodes = new HashSet<>();
        //添加结点ip和port到集合中
        nodes.add(new HostAndPort("192.168.19.99", 7001));
        nodes.add(new HostAndPort("192.168.19.99", 7002));
        nodes.add(new HostAndPort("192.168.19.99", 7003));
        nodes.add(new HostAndPort("192.168.19.99", 7004));
        nodes.add(new HostAndPort("192.168.19.99", 7005));
        nodes.add(new HostAndPort("192.168.19.99", 7006));
        //创建jedisCluster对象
        JedisCluster jedisCluster = new JedisCluster(nodes);
        //直接使用jedisCluster操作redis数据库
        jedisCluster.set("jedisCluster", "hello,jedisCluster nice to meet you");
        String string = jedisCluster.get("jedisCluster");
        System.out.println(string);
        //关闭jedisCluster
        jedisCluster.close();
    }
}
