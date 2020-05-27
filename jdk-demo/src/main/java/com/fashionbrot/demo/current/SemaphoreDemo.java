package com.fashionbrot.demo.current;


import org.apache.commons.lang3.RandomUtils;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 食堂有2个打饭窗口：需要定义一个permits=2的Semaphore对象。
 * 学生 按次序 排队打饭：此Semaphore对象是公平的。
 * 有20个学生：定义20个学生线程。
 * 打到饭的学生：调用了acquireUninterruptibly()方法，无法被中断
 * 吃泡面的学生：调用了tryAcquire(timeout,TimeUnit)方法，并且等待时间超时了
 * 吃大餐的学生：调用了acquire()方法，并且被中断了
 *
 */
public class SemaphoreDemo {

    /**
     * 打饭窗口
     * 2：   2个打饭窗口
     * true：公平队列-FIFO
     */
    static Semaphore semaphore = new Semaphore(2, true);

    public static void main(String[] args) throws InterruptedException {

        //101班的学生
        Thread[] students101 = new Thread[5];
        for (int i = 0; i < 20; i++) {
            //前10个同学都在耐心的等待打饭
            if (i < 10) {
                new Thread(new Student("打饭学生" + i, SemaphoreDemo.semaphore, 0)).start();
            } else if (i >= 10 && i < 15) {//这5个学生没有耐心打饭，只会等1000毫秒
                new Thread(new Student("泡面学生" + i, SemaphoreDemo.semaphore, 1)).start();
            } else {//这5个学生没有耐心打饭
                students101[i - 15] = new Thread(new Student("聚餐学生" + i, SemaphoreDemo.semaphore, 2));
                students101[i - 15].start();
            }
        }
        //
        Thread.sleep(5000);
        for (int i = 0; i < 5; i++) {
            students101[i].interrupt();
        }
    }


    /**
     * <p>打饭学生</p>
     *
     **/
    static class Student implements Runnable {
        //学生姓名
        private String name;
        //打饭许可
        private Semaphore semaphore;
        /**
         * 打饭方式
         * 0    一直等待直到打到饭
         * 1    等了一会不耐烦了，回宿舍吃泡面了
         * 2    打饭中途被其他同学叫走了，不再等待
         */
        private int type;

        public Student(String name, Semaphore semaphore, int type) {
            this.name = name;
            this.semaphore = semaphore;
            this.type = type;
        }

        /**
         * <p>打饭</p>
         *
         * @author hanchao 2018/3/31 19:49
         **/
        @Override
        public void run() {
            //根据打饭情形分别进行不同的处理
            switch (type) {
                //打饭时间
                //这个学生很有耐心，它会一直排队直到打到饭
                case 0:
                    //排队
                    semaphore.acquireUninterruptibly();
                    //进行打饭
                    try {
                        Thread.sleep(RandomUtils.nextLong(1000, 3000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //将打饭机会让后后面的同学
                    semaphore.release();
                    //打到了饭
                    System.out.println(name + " 终于打到了饭.");
                    break;

                //这个学生没有耐心，等了1000毫秒没打到饭，就回宿舍泡面了
                case 1:
                    //排队
                    try {
                        //如果等待超时，则不再等待，回宿舍吃泡面
                        if (semaphore.tryAcquire(RandomUtils.nextInt(6000, 16000), TimeUnit.MILLISECONDS)) {
                            //进行打饭
                            try {
                                Thread.sleep(RandomUtils.nextLong(1000, 3000));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //将打饭机会让后后面的同学
                            semaphore.release();
                            //打到了饭
                            System.out.println(name + " 终于打到了饭.");
                        } else {
                            //回宿舍吃泡面
                            System.out.println(name + " 回宿舍吃泡面.");
                        }
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                    }
                    break;

                //这个学生也很有耐心，但是他们班突然宣布聚餐，它只能放弃打饭了
                case 2:
                    //排队
                    try {
                        semaphore.acquire();
                        //进行打饭
                        try {
                            Thread.sleep(RandomUtils.nextLong(1000, 3000));
                        } catch (InterruptedException e) {
                            //e.printStackTrace();
                        }
                        //将打饭机会让后后面的同学
                        semaphore.release();
                        //打到了饭
                        System.out.println(name + " 终于打到了饭.");
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        //被叫去聚餐，不再打饭
                        System.out.println(name + " 全部聚餐，不再打饭.");
                    }
                    break;
                default:
                    break;
            }
        }
    }


}
