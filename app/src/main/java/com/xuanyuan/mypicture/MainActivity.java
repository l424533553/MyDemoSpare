package com.xuanyuan.mypicture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.xuanyuan.unit.Persion;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private Intent service;
    private String TAG = getClass().getName();

    private TestService mLocalService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.tvHello2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createObservable();

                // 创建 一个onSubscribe 对象

//                Observable<String> myObservable = Observable.just("Hello, world!");

            }
        });


        findViewById(R.id.tvHello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mObservable.subscribe(mObserver);

//                System.out.println(" 调用 mEmitter.onNext  ");
//                mEmitter.onNext("Hello");
//                System.out.println(" 调用 mEmitter.onComplete  ");
//                mEmitter.onComplete();
            }
        });
    }


    private void sendMessage() {
        if (mEmitter != null) {
            mEmitter.onNext("哈哈哈");
        }
    }

    private ObservableEmitter<String> mEmitter;
    private Observable<String> mObservable;

    private void createObservable() {

//        mObservable= Observable.just("Hello, world!");

//        mObservable= Observable.from .from("Hello"," world!","how","are","you");



                mObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        System.out.println("  Observable  has  subscribe");
                    }
                });




//        mObservable = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) {
//                System.out.println("  Observable  has  create");
//
//                mEmitter = emitter;
//                System.out.println("  mEmitter.hashCode() " + mEmitter.hashCode());
//            }
//        });

    }

    // 一个类，一般hashCode是不一样的
    // 此时会发现 mEmitter 和 Disposable d 中的 hasCode是一样的
    private Observer<String> mObserver = new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {
            System.out.println("Observer.onSubscribe");
            if (d != null) {
                System.out.println("  d.hashCode()=" + d.hashCode());
//                System.out.println("  d.getClass()=" + d.getClass().getCanonicalName());
            }

        }

        @Override
        public void onNext(String s) {
            if (mEmitter != null) {
                System.out.println("  mEmitter.hashCode() " + mEmitter.hashCode());
            }

            System.out.println("Observer.onNext  内容=" + s);
            Log.i(TAG, "  +++ onNext" + s);
        }

        @Override
        public void onError(Throwable e) {
            Log.i(TAG, "  +++ onError");
        }

        @Override
        public void onComplete() {
            if (mEmitter != null) {
                System.out.println("  mEmitter.hashCode() " + mEmitter.hashCode());
            }

            System.out.println("Observer.onComplete ");
            Log.i(TAG, "  +++ onComplete");
        }
    };


    public void test() {
        Observable<Integer> observable1;
        Observable.just(3).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "rx -- onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "rx -- onNext" + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "rx -- onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "rx -- onComplete");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(localServiceConnection);
    }

    //通过连接服务，获得服务对象mLocalService，这样就可以调用服务端的方法
    private ServiceConnection localServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TestService.LocalBinder localBinder = (TestService.LocalBinder) service;
            mLocalService = localBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private Handler mHandler;

    private void initHandler() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                addHandler(msg);
                return false;
            }
        });
    }

    private void addHandler(Message msg) {

        int what = msg.what;
        Log.i("0000000000", "第2代 handler" + System.currentTimeMillis());
        if (555666 == what) {
            Log.i("0000000000", "第2代 打开了 Weight " + System.currentTimeMillis());
        }
    }
}
