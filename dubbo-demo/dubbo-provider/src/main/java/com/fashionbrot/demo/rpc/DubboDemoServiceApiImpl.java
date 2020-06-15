package com.fashionbrot.demo.rpc;

import com.fashionbrot.demo.DubboDemoServiceApi;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class DubboDemoServiceApiImpl implements DubboDemoServiceApi {

    private AtomicLong atomicLong=new AtomicLong();

    @Override
    public String testServiceApi(String abc) {
        atomicLong.incrementAndGet();
        System.out.println("testServiceApi provider success:"+atomicLong.get());
        return abc+1;
    }
}
