package com.xuanyuan.mypicture;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * 作者：罗发新
 * 时间：2020/4/13 0013    星期一
 * 邮件：424533553@qq.com
 * 说明：
 */
public class TestService extends Service {
    boolean flag;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    //向客户端返回的IBinder对象
    public class LocalBinder extends Binder {
        TestService getService() {
            return TestService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        flag = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    Log.i("11111", "打印测试");
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    @Override
    public void onDestroy() {
        flag = false;
        super.onDestroy();

        Log.i("11111", "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

}
