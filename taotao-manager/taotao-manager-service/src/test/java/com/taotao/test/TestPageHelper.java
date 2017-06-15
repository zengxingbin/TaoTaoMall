package com.taotao.test;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;

public class TestPageHelper {
    @Test
    public void testPageHelper() {
        //获取spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        TbItemMapper tbItemMapper = applicationContext.getBean(TbItemMapper.class);
        //执行查询前，设置分页信息
        PageHelper.startPage(1, 10);
        //查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = tbItemMapper.selectByExample(example);
        //方式一获取信息
        Page<TbItem> page = (Page<TbItem>)list;
        //方式二获取信息，通过pageInfo获取信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        System.out.println("总记录数:" + pageInfo.getTotal());
        System.out.println("总页数:" + pageInfo.getPages());
        System.out.println("第几页" + pageInfo.getPageNum());
        System.out.println("每页显示多少条" + pageInfo.getPageSize());
        
    }
}
