package com.xuanyuan.originalcode.android.rxjava;

import android.util.Log;

import org.reactivestreams.Subscriber;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;


/**
 * 作者：罗发新
 * 时间：2020/4/16 0016    星期四
 * 邮件：424533553@qq.com
 * 说明：
 */
public class RxJavaTest {


    public static void main(String[] args) {
//        test();
//        System.out.println("测试问题");
        GGG ggg = new GGG();

    }

    private static void observerTest() {

    }

    private static void test() {


    }


}

class GGG {
    private String TAG = getClass().getName();


}

class HHH {
    private String TAG = getClass().getName();

    //数据发射源对象
    private ObservableEmitter observableEmitter;

    private void sendData() {
        if (observableEmitter != null) {
            for (int i = 0; i < 10; i++) {
                //数据发射源对象发射数据
                observableEmitter.onNext("Observable  数据data " + i);
            }
            observableEmitter.onComplete();
        }
    }

    public ObservableOnSubscribe<String> observableOnSubscribe = new ObservableOnSubscribe<String>() {
        @Override
        public void subscribe(@NonNull ObservableEmitter<String> e) {
            Log.i("123456789", "subscribe");
            observableEmitter = e;
            observableEmitter.onNext("Observable  数据data");
            sendData();
        }
    };

    public Observable<String> observable = Observable.create(observableOnSubscribe);


}