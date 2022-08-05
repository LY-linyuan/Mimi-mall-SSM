package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.AdminMapper;
import com.bjpowernode.pojo.Admin;
import com.bjpowernode.pojo.AdminExample;
import com.bjpowernode.service.AdminService;
import com.bjpowernode.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 临渊
 * @Date 2022-08-04 17:58
 */

@Service
public class AdminServiceImpl implements AdminService {

    // 在业务逻辑层中, 一定会有数据访问层对象
    @Autowired
    AdminMapper adminMapper;


    @Override
    public Admin login(String name, String pwd) {
        // 根据传入用户名到数据库中 查询相应的用户对象
        // 如果有条件一定要创建AdminExample的对象, 用来封装对象
        AdminExample adminExample = new AdminExample();
        /**
         *  如何添加条件
         *      select * from admin where a_name="admin"
         */
        // 添加用户名a_name条件
        adminExample.createCriteria().andANameEqualTo(name);
        List<Admin> adminList = adminMapper.selectByExample(adminExample);
        if (adminList.size() > 0) {
            // 用户名不重复 并且不为空  返回唯一值
            Admin admin = adminList.get(0);
            // 密码密文比较
            String miPwd = MD5Util.getMD5(pwd);
            if (admin.getaPass().equals(miPwd)) {
                return admin;
            }
        }

        return null;
    }
}
