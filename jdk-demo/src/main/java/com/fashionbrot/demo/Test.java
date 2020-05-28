package com.fashionbrot.demo;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.fashionbrot.demo.model.User;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

    public static void main(String[] args) {
        TransmittableThreadLocal<User> threadLocal=new TransmittableThreadLocal();
        User user =  new User();
        user.setUname("ahahahha");
        threadLocal.set(user);

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i=0;i<10;i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(threadLocal.get().getUname());
                }
            });
        }
    }
}
