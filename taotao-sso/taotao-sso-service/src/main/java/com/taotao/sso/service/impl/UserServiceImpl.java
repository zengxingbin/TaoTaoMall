package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.taotao.jedis.JedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserService;
import com.taotao.utils.JsonUtils;

/**
 * 用户处理Service
 * @author Administrator
 *
 */
@Service
public class UserServiceImpl implements UserService{
    //注入mapper
    @Autowired
    private TbUserMapper userMapper;
    //注入jedis
    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_ID}")
    private String SESSION_ID;
    @Value("${EXPIRE_TIME}")
    private int EXPIRE_TIME;
    @Override
    public TaotaoResult checkData(String data, int type) {
        //创建查询条件
        TbUserExample example = new TbUserExample();
        Criteria criteria = example.createCriteria();
        //根据类型拼装查询条件
        //判断用户名是否可用
        if(type == 1) {
            criteria.andUsernameEqualTo(data);
        }else if(type == 2) {
            //判断手机号是否可用
            criteria.andPhoneEqualTo(data);
        }else if(type == 3) {
            //判断邮箱是否可用
            criteria.andEmailEqualTo(data);
        }else {
            return TaotaoResult.build(400, "参数内容非法！");
        }
        //执行查询
        List<TbUser> list = userMapper.selectByExample(example);
        if(list != null && list.size() > 0)
            return TaotaoResult.ok(false);
        return TaotaoResult.ok(true);
    }
    @Override
    public TaotaoResult register(TbUser user) {
        //进行用户注册数据校验
        if(StringUtils.isBlank(user.getUsername())) {
            return TaotaoResult.build(400, "用户名不能为空!");
        }else {
            if(!(boolean) checkData(user.getUsername(), 1).getData()) 
                return TaotaoResult.build(500, "用户名已存在");
        }
        if(StringUtils.isBlank(user.getPassword()))
            return TaotaoResult.build(400, "密码不能为空!");
        if(!StringUtils.isBlank(user.getPhone()) && !(boolean)checkData(user.getPhone(), 2).getData())
            return TaotaoResult.build(500, "手机号已经被使用!");
        if(!StringUtils.isBlank(user.getEmail()) && !(boolean)checkData(user.getEmail(),3).getData())
            return TaotaoResult.build(500, "邮箱已经被使用!");
        //补全pojo属性
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //对密码进行md5加密,使用spring框架中内置的MD5加密工具
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        //插入到数据库
        userMapper.insert(user);
        return TaotaoResult.ok();
    }
    @Override
    public TaotaoResult login(String username, String password) {
        //从数据库中查询用户名和密码,创建查询条件
        TbUserExample example = new TbUserExample();
        Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        //查询
        List<TbUser> list = userMapper.selectByExample(example);
        if(list == null || list.size() == 0)
            return TaotaoResult.build(500, "用户名或密码错误!");
        TbUser user = list.get(0);
        //对用户密码进行MD5加密后再去数据库中查询
        if(!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes())))
            return TaotaoResult.build(500, "用户名或密码错误!");
        //用户登录信息正确后，把用户信息保存在redis中，用UUIdUtils生成的token做为key
        String token = UUID.randomUUID().toString();
        //为了安全，用户密码不应该放在redis中，所以要删掉
        user.setPassword(null);
        jedisClient.set(SESSION_ID + ":" + token, JsonUtils.objectToJson(user));
        //设置过期时间
        jedisClient.expire(SESSION_ID + ":" + token, EXPIRE_TIME);
        return TaotaoResult.ok(token);
    }
    @Override
    public TaotaoResult getUserByTokenn(String token) {
        //根据token从redis中去除用户信息
        String json = jedisClient.get(SESSION_ID + ":" + token);
        if(StringUtils.isBlank(json))
            return TaotaoResult.build(400, "用户登录已过期");
        //重新设置用户登录的过期时间
        jedisClient.expire(SESSION_ID + ":" + token, EXPIRE_TIME);
        //json数据转成pojo返回
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        return TaotaoResult.ok(user);
    }
    @Override
    public TaotaoResult logout(String token) {
        //实现用户安全退出，将用户的信息从redis中删除掉
        jedisClient.del(token);
        return TaotaoResult.ok();
    }
}
