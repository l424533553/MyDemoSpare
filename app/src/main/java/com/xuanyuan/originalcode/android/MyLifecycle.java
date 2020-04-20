package com.xuanyuan.originalcode.android;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * 生命周期观察者
 */
public class MyLifecycle implements LifecycleObserver {
    private final String TAG = getClass().getName();

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void on_create() {
        Log.i(TAG,"Lifecycle.Event.ON_CREATE");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void on_resume() {
        Log.i(TAG,"Lifecycle.Event.ON_RESUME");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void on_pause() {
        Log.i(TAG,"Lifecycle.Event.ON_PAUSE");
    }
}

