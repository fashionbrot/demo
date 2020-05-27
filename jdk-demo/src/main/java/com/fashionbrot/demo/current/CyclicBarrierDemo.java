package com.fashionbrot.demo.current;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


/**
 *  N个线程相互等待，任何一个线程完成之前，所有的线程都必须等待。
 *  CyclicBarrier 用于一组或几组线程，比如一组线程需要在一个时间点上达成一致，例如同时开始一个工作。
 *  另外，CyclicBarrier 的循环特性和构造函数所接受的 Runnable 参数也是 CountDownLatch 所不具备的。
 *
 * 基于 ReentrantLock 实现
 */
public class CyclicBarrierDemo {

    private static int SIZE = 5;
    private static CyclicBarrier cb;

    public static void main(String[] args) {

        /*cb = new CyclicBarrier(SIZE, new Runnable () {
            public void run() {
                System.out.println("所有任务已经开始");
                System.out.println("CyclicBarrier's parties is: "+ cb.getParties());
            }
        });*/
        cb = new CyclicBarrier(SIZE);

        // 新建5个任务
        for(int i=0; i<SIZE; i++)
            new InnerThread().start();

        System.out.println("所有任务已派发完成");
    }


    static class InnerThread extends Thread{
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " wait for CyclicBarrier.");

                // 将cb的参与者数量加1
                cb.await();

                // cb的参与者数量等于5时，才继续往后执行
                System.out.println(Thread.currentThread().getName() + " continued.");
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
