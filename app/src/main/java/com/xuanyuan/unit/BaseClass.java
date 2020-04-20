package com.xuanyuan.unit;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

/**
 * 作者：罗发新
 * 时间：2020/4/17 0017    星期五
 * 邮件：424533553@qq.com
 * 说明：
 */
public class BaseClass extends Observable<BaseClass> {

    public BaseClass(Persion persion) {
        mPersion = persion;
    }

    private String name;
    private Persion mPersion;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Persion getPersion() {
        return mPersion;
    }

    public void setPersion(Persion persion) {
        mPersion = persion;
    }

    @Override
    protected void subscribeActual(Observer<? super BaseClass> observer) {
        // 测试过程

    }
}
