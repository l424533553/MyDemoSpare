package com.xuanyuan.caseanalysis.rxbinding;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxCompoundButton;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent;
import com.xuanyuan.caseanalysis.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

@SuppressLint("CheckResult")
public class RxBindingActivity extends AppCompatActivity {

    public CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbinding);
        mCompositeDisposable = new CompositeDisposable();

    }

    /**
     * 使用RxBinding可做到点击防抖的效果
     * throttleFirst(long windowDuration, TimeUnit unit)，
     * 设置一定时间内只响应首次(throttleFirst)或者末次(throttleLast)的点击事件。windowDuration为防抖时间，unit为时间单位。
     */
    @SuppressLint("CheckResult")
    private void viewShake(View btnLogin) {
//        RxView.clicks(btnLogin)
        RxView.longClicks(btnLogin).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        Toast.makeText(RxBindingActivity.this, "11111111111", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void otherWay(View view) {
        //当btnLogin绘制时触发
        RxView.draws(view)
//        //滑动时触发
//        RxView.scrollChangeEvents(btnLogin)
//        //拖拽监听
//        RxView.drags(btnLogin)
                // 内容输入变化触发  ，相当于 addTextChangedListener
//        RxTextView.textChanges(userNameEt).
                // 控件操作时间间隔
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void combineLatest(TextView userNameEt, TextView userPasswordEt) {
        Observable<CharSequence> userNameObservable = RxTextView.textChanges(userNameEt);
        Observable<CharSequence> userPwdObservable = RxTextView.textChanges(userPasswordEt);
        // 相当于合并
        Observable.combineLatest(userNameObservable, userPwdObservable,
                new BiFunction<CharSequence, CharSequence, Object>() {
                    @Override
                    public Object apply(CharSequence userName, CharSequence userPwd) throws Exception {
                        Toast.makeText(RxBindingActivity.this, "33333", Toast.LENGTH_LONG).show();
                        // 设置按钮是否可用(或者改变背景颜色)
                        boolean isEnable = (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPwd));

                        return null;
                    }
                });
    }


    /**
     * 控件操作时间间隔
     *
     * @param mUserNameEt
     */
    private void debounce(TextView mUserNameEt) {
//        RxTextView.textChanges(mUserNameEt).debounce(1200, TimeUnit.MILLISECONDS)
        //    textChanges    内部封装了TextWatcher文本改变监听
        RxTextView.textChanges(mUserNameEt)
                .map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(CharSequence charSequence) throws Exception {
                        return String.valueOf(charSequence);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i("1111122222", "debounce");
                    }
                });


        // 内部同样封装了TextWatcher文本改变监听。不同的是其返回数据的类型为
        // TextViewTextChangeEvent，内部包含详细的文本改变数据。
        RxTextView.textChangeEvents(mUserNameEt)
                .subscribe(new Consumer<TextViewTextChangeEvent>() {
                    @Override
                    public void accept(TextViewTextChangeEvent textViewTextChangeEvent) throws Exception {
                        Log.e("rx_binding_test", "textChanges:文本改变了:" + "before:" + textViewTextChangeEvent.getBefore() +
                                ",start:" + textViewTextChangeEvent.getStart() + ",text:" + textViewTextChangeEvent.getText() +
                                ",count:" + textViewTextChangeEvent.getCount());
                    }
                });


    }

    /**
     * 接口轮询，轮询操作
     */
    private void interval() {
        Observable.interval(2, 2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) {
                        Log.i("111122222", "interval");
                    }
                });
    }

    /**
     * 延时操作
     */
    private void timer() {
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.i("111122222", "timer");
                    }
                });
    }

    private void other(View btnClick) throws Exception {
        RxView.visibility(btnClick).accept(true);

//        RxTextView.text(etRxTextView).accept("呵呵");
//        RxTextView.hint(etRxTextView).accept("请输入姓名");
//        RxTextView.color(etRxTextView).accept(Color.parseColor("#00ff00"));
    }

    private void editorActions(TextView etRxTextView) {
        // 内部封装了OnEditorActionListener软键盘回车点击监
        RxTextView.editorActions(etRxTextView)
                .subscribe(integer -> {
                    Log.e("rx_binding_test", "editorActions:输入完毕，点击回车:");
                });

        // 内部同样封装了OnEditorActionListener软键盘回车点击监听。不同的是它的返回类型为
        // TextViewEditorActionEvent，包含actionId，keyEvent等信息
        RxTextView.editorActionEvents(etRxTextView)
                .subscribe(textViewEditorActionEvent -> {
                    KeyEvent keyEvent = textViewEditorActionEvent.getKeyEvent();
                    if (keyEvent == null) {
                        return;
                    }
                    //判断up状态
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                        Log.e("rx_binding_test", "editorActionEvents:输入完毕，点击回车:" + textViewEditorActionEvent.getKeyEvent());
                    }
                });
    }

    /**
     * 应用场景在 输入框中进行搜索，不必搜索string一改便就立马进行操作，可进行500ms的时间过滤噻
     */
    private void txtChangeSearch(TextView etRxTextView) {
        RxTextView.textChanges(etRxTextView)
                //限流时间500ms
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                //CharSequence转换为String
                .map(CharSequence::toString)
                .subscribe(s -> {
                    //这里可以查询数据库或请求服务器查询
                    Log.e("rx_binding_test", "textChanges:文本改变了:" + s);
                });
    }

    private void checkedChanges(CompoundButton cbContract) {
        RxCompoundButton.checkedChanges(cbContract)
                .subscribe(aBoolean -> {
                    // 判断条件
                    cbContract.setEnabled(true);

                });
    }

    /**
     * 一个 验证 输入 框输入内容检测的 方法
     */
    private void rxEditText(EditText mEditName, EditText mEditPwd, Button btnLogin) {
        Observable<String> mNameObservable = RxTextView.textChanges(mEditName).map(new Function<CharSequence, String>() {
            @Override
            public String apply(CharSequence charSequence) throws Exception {
                return String.valueOf(charSequence);
            }
        });

        Observable<String> mPasswordObservable = RxTextView.textChanges(mEditPwd).map(new Function<CharSequence, String>() {
            @Override
            public String apply(CharSequence charSequence) throws Exception {
                return String.valueOf(charSequence);
            }
        });

        Observable.combineLatest(mNameObservable, mPasswordObservable,
                (name, password) -> isNameValid(name) && isPwdValid(password))
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        btnLogin.setEnabled(true);
                        RxView.clicks(btnLogin).subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                Toast.makeText(RxBindingActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private boolean isNameValid(String name) {
        return "RxBind".equals(name);
    }

    private boolean isPwdValid(String pwd) {
        return "123".equals(pwd);
    }


}
