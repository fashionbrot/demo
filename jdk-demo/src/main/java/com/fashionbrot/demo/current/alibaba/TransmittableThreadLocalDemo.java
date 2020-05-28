package com.fashionbrot.demo.current.alibaba;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.fashionbrot.demo.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 阿里 线程数据共享 demo
 * github：https://github.com/hejy12/transmittable-thread-local
 * /trænzˈmɪt; trænsˈmɪt/
 */
public class TransmittableThreadLocalDemo {

    //讲解 https://www.jianshu.com/p/29f4034f4250
    public static void main(String[] args) {
        TransmittableThreadLocal<User> threadLocal=new TransmittableThreadLocal();
        User user =  new User();
        user.setUname("alibaba");
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
