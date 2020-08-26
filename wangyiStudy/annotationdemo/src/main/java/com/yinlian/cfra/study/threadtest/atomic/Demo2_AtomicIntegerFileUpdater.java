package com.yinlian.cfra.study.threadtest.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @description
 * @author: suicaijiao
 * @create: 2020-06-22 19:25
 **/
public class Demo2_AtomicIntegerFileUpdater {
    //实例化一个更新器，指定要做原子修改实体类字段
    private static AtomicIntegerFieldUpdater<User> atom =
            AtomicIntegerFieldUpdater.newUpdater(User.class, "id");

    public static void main(String[] args) {
        User user = new User(100, 100, "Kody");
        // 修改哪个类的哪个字段，声明AtomicIntegerFieldUpdater指定要修改的是id字段
        atom.addAndGet(user,50);
        System.out.println("addAndGet(user,50)   调用后值变为："+user);
    }
}

class User {
    volatile int id;
    volatile int age;

    private String name;

    public User(int id, int age, String name) {
        this.id = id;
        this.age = age;
        this.name = name;
    }

    public String toString() {
        return "id:" + id + "" + "age:" + age;
    }
}
