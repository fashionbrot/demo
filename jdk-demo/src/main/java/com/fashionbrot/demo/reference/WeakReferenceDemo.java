package com.fashionbrot.demo.reference;

import com.fashionbrot.demo.model.User;

import java.lang.ref.WeakReference;

/**
 * 弱引用
 * 在垃圾回收期线程扫描它所管辖的内存区域的过程中，
 * 一旦发现了只具有若引用的对象，不管当前内存空间是否足够，都会回收它的内存
 */
public class WeakReferenceDemo {

    public static void main(String[] args) {
        User user = new User();
        /*设置username*/
        user.setUname("廖泽民");

        /*把对象放在弱引用中*/
        WeakReference<User> weak = new WeakReference<User>(user);

        /*把user对象置空，然后再从若引用中取值*/
        user = null;

        int i = 0;

        /*weak.get()表示从引用中取得对象*/
        while (weak.get() != null) {

            System.out.println(String.format("从弱引用中取值: %s, count: %d", weak.get().getUname(), ++i));
            if (i % 10 == 0) {
                System.gc();
                System.out.println("内存回收方法被调用");
            }
            try {
                Thread.sleep(500);
            } catch (Exception e) {

            }
        }
        System.out.println("对象已经被JVM回收");
    }

}
