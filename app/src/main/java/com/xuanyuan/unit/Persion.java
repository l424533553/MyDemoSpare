package com.xuanyuan.unit;

/**
 * 作者：罗发新
 * 时间：2020/4/17 0017    星期五
 * 邮件：424533553@qq.com
 * 说明：
 */
public class Persion {
    private int age;
    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void  test(){
        System.out.println(" test中的内部测试 ");
        System.out.println(" getClass() "+getClass());

    }
}
