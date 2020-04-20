package com.xuanyuan.originalcode.android;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import static android.os.Process.myPid;
import static android.os.Process.myTid;
import static android.os.Process.myUid;

/**
 * 作者：罗发新
 * 时间：2020/4/15 0015    星期三
 * 邮件：424533553@qq.com
 * 说明：在API<26是，需要实现LifecycleOwner，才能调用getLifecycle()
 * 生命周期组件：LifecycleOwner、LifecycleObserver
 */
@Deprecated
public class ProcessHelper extends AppCompatActivity implements LifecycleOwner {
    private String TAG = getClass().getName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        //
//        // 时间
//        Observer observer;
        Observable<String> observable = null;
//        //时间功能
//        observable.subscribe();
        // 你试试 对

        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

//        getLifecycle().addObserver();

    }

    private void test() {
        Log.i(TAG, "onCreate1: " + myPid());
        Log.i(TAG, "onCreate2: " + myTid());
        Log.i(TAG, "onCreate3: " + myUid());
        Log.i(TAG, "onCreate4: " + Thread.currentThread().getId());
        Log.i(TAG, "onCreate5: " + getMainLooper().getThread().getId());
        Log.i(TAG, "onCreate6: " + getTaskId());
        Log.i(TAG, "onCreate7: " + getApplicationInfo().uid);
        Log.i(TAG, "onCreate8: " + getApplicationInfo().processName);
    }

}
