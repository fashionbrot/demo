package com.fashionbrot.demo;

import com.fashionbrot.demo.DubboDemoServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {

    @Autowired
    private DubboDemoServiceApi dubboDemoServiceApi;

    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        return dubboDemoServiceApi.testServiceApi("test");
    }

}
