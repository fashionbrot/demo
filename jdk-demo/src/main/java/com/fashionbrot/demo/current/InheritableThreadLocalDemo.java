package com.fashionbrot.demo.current;

/**
 * 子线程获取主线程 值
 *
 * 
 */
public class InheritableThreadLocalDemo {


    public static ThreadLocal<Integer> threadLocal = new InheritableThreadLocal<>();

    public static void main(String args[]){
        threadLocal.set(new Integer(123));

        for (int i=0;i<10;i++) {
            Thread thread = new MyThread();
            thread.start();
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
