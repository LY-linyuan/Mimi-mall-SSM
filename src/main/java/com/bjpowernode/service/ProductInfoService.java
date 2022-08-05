package com.bjpowernode.service;

import com.bjpowernode.pojo.ProductInfo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author 临渊
 * @Date 2022-08-05 7:04
 */
public interface ProductInfoService {

    // 显示全部商品 不分页
    List<ProductInfo> getAll();

    // 分页功能
    PageInfo splitPage(int pageNum, int pageSize);

    // 增加商品
    int save(ProductInfo info);

    // 按主键id查询商品
    ProductInfo getById(int pid);

    // 更新商品
    int update(ProductInfo info);

    // 单个删除商品
    int delete(Integer pid);

    // 批量删除商品
    int deleteBatch(String[] ids);

}
