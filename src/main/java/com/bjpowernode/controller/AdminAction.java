package com.bjpowernode.controller;

import com.bjpowernode.pojo.Admin;
import com.bjpowernode.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 临渊
 * @Date 2022-08-04 21:24
 */

@Controller
@RequestMapping("/admin")
public class AdminAction {
    // 切记 在所有的界面层一定会有业务逻辑层对象
    @Autowired
    AdminService adminService;


    // 实现登录判断并执行相应的跳转
    @RequestMapping("/login")
    public String login(HttpServletRequest request, String name, String pwd) {
        Admin admin = adminService.login(name, pwd);
        if (admin != null) {
            // 登录成功
            request.setAttribute("admin", admin);
            return "main";
        } else {
            // 登录失败
            request.setAttribute("errmsg", "用户名或密码不正确!");
            return "login";
        }
    }
}
