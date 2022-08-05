package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.ProductInfoMapper;
import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.ProductInfoExample;
import com.bjpowernode.service.ProductInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 临渊
 * @Date 2022-08-05 7:06
 */

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    // 切记业务逻辑从中一定有数据访问层对象
    @Autowired
    ProductInfoMapper productInfoMapper;

    @Override
    public List<ProductInfo> getAll() {
        return productInfoMapper.selectByExample(new ProductInfoExample());
    }

    @Override
    public PageInfo splitPage(int pageNum, int pageSize) {
        // 分页插件使用pageHelper工具类完成分页设置
        PageHelper.startPage(pageNum, pageSize);
        // 进行PageInfo数据封装
        // 进行有条件查询操作  必须要创建ProductInfoExample对象
        ProductInfoExample example = new ProductInfoExample();
        // 设置排序, 按住键降序排序
        example.setOrderByClause("p_id desc");
        // 设置完排序后取集合  取集合之前一定要先设置  PageHelper.startPage(pageNum, pageSize);
        List<ProductInfo> productInfoList = productInfoMapper.selectByExample(example);
        // 将查到的集合封装到 PageInfo
        PageInfo<ProductInfo> pageInfo = new PageInfo<>(productInfoList);
        return pageInfo;
    }

    @Override
    public int save(ProductInfo info) {
        return productInfoMapper.insert(info);
    }

    @Override
    public ProductInfo getById(int pid) {
        return productInfoMapper.selectByPrimaryKey(pid);
    }

    @Override
    public int update(ProductInfo info) {
        return productInfoMapper.updateByPrimaryKey(info);
    }

    @Override
    public int delete(Integer pid) {
        return productInfoMapper.deleteByPrimaryKey(pid);
    }

    @Override
    public int deleteBatch(String[] ids) {
        return productInfoMapper.deleteBatch(ids);
    }
}
