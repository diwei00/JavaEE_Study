package com.example.easyexceldemo.controller;

import com.alibaba.excel.EasyExcel;
import com.example.easyexceldemo.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author wh
 * @Date 2024/4/23 16:14
 */

@RequestMapping("/excel")
@RestController
public class EasyExcelDemo {


    @GetMapping("/get")
    public void exportExcel(HttpServletResponse response) throws IOException {
        // 准备数据
        List<User> list = new ArrayList<>();
        list.add(new User(1, "wuhao", 21, "男"));
        list.add(new User(2, "wuhao", 21, "男"));
        list.add(new User(3, "wuhao", 21, "男"));
        list.add(new User(4, "wuhao", 21, "男"));
        list.add(new User(5, "wuhao", 21, "男"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), User.class).sheet("模板").doWrite(list);
    }
}
