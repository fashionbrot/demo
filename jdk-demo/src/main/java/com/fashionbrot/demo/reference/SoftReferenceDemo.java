package com.fashionbrot.demo.reference;

import com.fashionbrot.demo.model.User;

import java.lang.ref.SoftReference;

/**
 * 强引用
 *
 *  SoftReference是强引用，它保存的对象实例，除非JVM即将OutOfMemory，否则不会被GC回收。
 * 这个特性使得它特别适合设计对象Cache。对于Cache，我们希望被缓存的对象最好始终常驻内存，但是如果JVM内存吃紧，
 * 为了不发生OutOfMemoryError导致系统崩溃，必要的时候也允许JVM回收Cache的内存，
 * 待后续合适的时机再把数据重新Load到Cache中。这样可以系统设计得更具弹性。
 */
public class SoftReferenceDemo {

    public static void main(String[] args) {
        User user = new User();
        /* 设置用户名 */
        user.setUname("廖泽民");

        /* 创建强引用对象 */
        SoftReference<User> soft = new SoftReference<User>(user);

        /* 把user对象置空，然后再从强引用中取值【注：要先存在引用中再置空，注意顺序啊】 */
        user = null;
        int i = 0;
        while (soft.get() != null) {
            System.out.println(String.format("从强引用中获取对象: %s, count: %d", soft.get().getUname(), ++i));
            if (i % 10 == 0) {
                System.gc();
                System.out.println("内存回收方法被调用!");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
        }
        System.out.println("对象已经被JVM回收!");
    }


}
