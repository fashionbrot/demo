package com.fashionbrot.demo.current;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 子线程获取主线程 值
 *
 * 
 */
public class InheritableThreadLocalDemo {


    public static ThreadLocal<Integer> threadLocal = new InheritableThreadLocal<>();

    public static void main(String args[]){
        threadLocal.set(new Integer(123));

        ExecutorService executorService =Executors.newFixedThreadPool(10);

        for (int i=0;i<10;i++) {
            Thread thread = new MyThread();
            thread.start();
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("ExecutorService threadLocal:"+threadLocal.get());
                }
            });
        }
        System.out.println("main = " + threadLocal.get());
    }

    static class MyThread extends Thread{
        @Override
        public void run(){
            System.out.println("ThreadName:"+Thread.currentThread().getName()+" MyThread = " + threadLocal.get());
        }
    }

}
