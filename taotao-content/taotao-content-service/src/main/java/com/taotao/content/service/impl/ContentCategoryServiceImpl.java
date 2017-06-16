package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    // 调用dao
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        // 设置查询条件
        TbContentCategoryExample example = new TbContentCategoryExample();
        Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        // 执行查询
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
        // 创建EasyUITreeNode的list集合
        List<EasyUITreeNode> resultList = new ArrayList<>();
        // 遍历list集合
        for (TbContentCategory tbContentCategory : list) {
            // 创建EasyUITreeNode,信息封装到EasyUITreeNode中
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            node.setState(tbContentCategory.getIsParent() ? "closed" : "open");
            // 添加EasyUITreeNode到resultList中
            resultList.add(node);
        }
        return resultList;
    }

    @Override
    public TaotaoResult addContentCatetory(long parentId, String name) {
        // 创建ContentCategory,补全信息后插入到数据库
        TbContentCategory tbContentCategory = new TbContentCategory();
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setName(name);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setSortOrder(1);
        // 1、表示正常
        tbContentCategory.setStatus(1);
        tbContentCategory.setUpdated(new Date());
        tbContentCategory.setCreated(new Date());
        // 插入到数据库
        tbContentCategoryMapper.insert(tbContentCategory);
        // 获取父节点，判断父节点状态
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent()) {
            parent.setIsParent(true);
            // 更新到数据库
            tbContentCategoryMapper.updateByPrimaryKey(parent);
        }
        return TaotaoResult.ok(tbContentCategory);
    }

    @Override
    public TaotaoResult renameContentCategory(long id, String name) {
        // 根据id查询内容分类
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
        // 修改名称
        tbContentCategory.setName(name);
        // 更新到数据库
        tbContentCategoryMapper.updateByPrimaryKey(tbContentCategory);
        return new TaotaoResult().ok();
    }

    @Override
    public TaotaoResult deleteContentCategory(long id) {
        // 根据id从数据库中查到此内容分类
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
        // 递归删除
        delete(tbContentCategory);
        return TaotaoResult.ok();
    }

    private void delete(TbContentCategory tbContentCategory) {
        // 删除改结点
        tbContentCategoryMapper.deleteByPrimaryKey(tbContentCategory.getId());
        // 获取父节点的id
        long parentId = tbContentCategory.getParentId();
        // 获取父节点
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if (parent != null) {
            // 创建查询条件，获取其兄弟元素的集合
            TbContentCategoryExample example = new TbContentCategoryExample();
            Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(parentId);
            List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
            // 判断集合的大小，如果集合长度为0，说明没有其他兄弟元素了，改变父节点的状态
            if (list.size() == 0) {
                parent.setIsParent(false);
                // 更新父节点
                tbContentCategoryMapper.updateByPrimaryKey(parent);
            }
        }
        // 判断此结点是否是父节点
        if (tbContentCategory.getIsParent()) {
            // 若是，则把它下面的所有子元素也删掉
            // 创建查询条件，获取子元素集合
            TbContentCategoryExample example2 = new TbContentCategoryExample();
            Criteria criteria2 = example2.createCriteria();
            criteria2.andParentIdEqualTo(tbContentCategory.getId());
            List<TbContentCategory> list2 = tbContentCategoryMapper.selectByExample(example2);
            // 遍历删除子元素
            for (TbContentCategory content : list2)
                delete(content);
        }
    }

}
