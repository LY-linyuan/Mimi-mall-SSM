package com.bjpowernode.controller;

import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.bjpowernode.service.ProductInfoService;
import com.bjpowernode.utils.FileNameUtil;
import com.github.pagehelper.PageInfo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Author 临渊
 * @Date 2022-08-05 7:10
 */

@Controller
@RequestMapping("/prod")
public class ProductInfoAction {
    // 每页显示的记录数
    public static final int PAGE_SIZE = 5;

    // 异步上传文件名称
    String saveFileName = "";

    // 在界面层中一定有一个数据访问层对象
    @Autowired
    ProductInfoService productInfoService;

    // 前十全部商品不分页
    @RequestMapping("/getAll")
    public String getAll(HttpServletRequest request){
        List<ProductInfo> productInfoList = productInfoService.getAll();
        request.setAttribute("list", productInfoList);
        return "product";
    }

    // 显示第一页的5条记录
    @RequestMapping("/split")
    public String split(HttpServletRequest request){
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("prodVo");
        if (vo != null) {
            info = productInfoService.splitPageVo((ProductInfoVo) vo, PAGE_SIZE);
            request.getSession().removeAttribute("prodVo");
        } else {
            // 得到第一页的数据
            info = productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.setAttribute("info", info);
        return "product";
    }


    // 分页ajax翻页处理
    @ResponseBody
    @RequestMapping("/ajaxsplit")
    public void ajaxSplit(ProductInfoVo vo, HttpSession session) {
        // 取得当前page参数页面的数据
        // PageInfo info = productInfoService.splitPage(page, PAGE_SIZE);
        PageInfo info = productInfoService.splitPageVo(vo, PAGE_SIZE);
        session.setAttribute("info", info);
    }

    // 异步ajax文件上传处理
    @ResponseBody
    @RequestMapping("/ajaxImg")
    public Object ajaxImg(MultipartFile pimage, HttpServletRequest request) {
        // 提取文件名UUID + 上传图片的后缀 .jpg png
        saveFileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(pimage.getOriginalFilename());
        // 得到项目中的图片存储路径
        String path = request.getServletContext().getRealPath("/image_big");
        // 转存
        try {
            pimage.transferTo(new File(path + File.separator + saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 客户端JSON对象封装图片的路径, 为了在页面实现立即回显
        JSONObject object = new JSONObject();
        object.put("imgurl", saveFileName);
        return object.toString();
    }

    @RequestMapping("/save")
    public String save(ProductInfo info, HttpServletRequest request) {
        info.setpImage(saveFileName);
        info.setpDate(new Date());
        // info中有表单提交上阿里啊的五个数据
        int num = -1;
        try {
            num = productInfoService.save(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            request.setAttribute("msg", "增加成功");
        } else {
            request.setAttribute("msg", "增加失败");
        }
        // 清空图片的数据 saveFileName
        saveFileName = "";

        // 增加成功后重新访问数据库
        return "forward:/prod/split.action";
    }

    @RequestMapping("/one")
    public String one(int pid, ProductInfoVo vo, Model model, HttpSession session) {
        ProductInfo info = productInfoService.getById(pid);
        model.addAttribute("prod", info);
        session.setAttribute("prodVo", vo);
        return "update";
    }

    @RequestMapping("/update")
    public String update(ProductInfo info, HttpServletRequest request) {
        if (saveFileName != null) {
            info.setpImage(saveFileName);
        }
        int num = -1;
        try {
            num = productInfoService.update(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            request.setAttribute("msg", "更新成功");
        } else {
            request.setAttribute("msg", "更新失败");
        }
        saveFileName = "";
        return "forward:/prod/split.action";
    }

    @RequestMapping("/delete")
    public String delete(ProductInfoVo vo, Integer pid, HttpServletRequest request) {
        int num = -1;
        try {
            num = productInfoService.delete(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            request.setAttribute("msg", "删除成功");
            request.getSession().setAttribute("deleteProdVo", vo);
        } else {
            request.setAttribute("msg", "删除失败");
        }
        return "forward:/prod/deleteAjaxSpilt.action";
    }

    @ResponseBody
    @RequestMapping(value = "/deleteAjaxSpilt", produces = "text/html;charset=UTF-8")
    public Object deleteAjaxSpilt(HttpServletRequest request) {
        // 取得第一页的数据
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("deleteProdVo");
        if (vo != null) {
            info = productInfoService.splitPageVo((ProductInfoVo) vo, PAGE_SIZE);
        } else {
            info = productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.getSession().setAttribute("info", info);
        return request.getAttribute("msg");
    }

    // 批量删除商品
    @RequestMapping("/deleteBatch")
    public String deleteBatch(String pids, HttpServletRequest request){
        int num = -1;
        // 将传上来的字符串截开, 形成字符数组
        String[] ps = pids.split(",");
        try {
            num = productInfoService.deleteBatch(ps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            request.setAttribute("msg", "批量删除成功");
        } else {
            request.setAttribute("msg", "批量删除失败");
        }
        return "forward:/prod/deleteAjaxSpilt.action";
    }

    // 多条件查询功能
    @ResponseBody
    @RequestMapping("/condition")
    public void condition(ProductInfoVo vo, HttpSession session) {
        List<ProductInfo> productInfoList = productInfoService.selectCondition(vo);
        session.setAttribute("list", productInfoList);
    }

}
