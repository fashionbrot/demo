package com.fashionbrot.demo.rpc;

import com.fashionbrot.demo.DubboDemoServiceApi;

public class DubboDemoServiceApiImpl implements DubboDemoServiceApi {


    @Override
    public String testServiceApi(String abc) {
        System.out.println("testServiceApi provider success");
        return abc+1;
    }
}
