package com.fashionbrot.demo.controller;


import com.fashionbrot.demo.entity.User;
import com.fashionbrot.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/test")
    @ResponseBody
    public int test(Long id){
        User user= User.builder()
                .age(1)
                .id(id)
                .email("fashionbrot@163.com")
                .name("张三")
                .createTime(new Date())
                .build();
        return userMapper.updateById(user);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public int insert(){
        User user= User.builder()
                .age(1)
                .email("fashionbrot@163.com")
                .name("张三")
                .createTime(new Date())
                .build();
        return userMapper.insert(user);
    }


}
