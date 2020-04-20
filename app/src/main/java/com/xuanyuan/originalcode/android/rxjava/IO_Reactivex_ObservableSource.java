package com.xuanyuan.originalcode.android.rxjava;


import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

/**
 * Represents a basic, non-backpressured {@Observable} source base interface,consumable via an {@link Observer}.
 * 代表一个基本，无负压Observable 源的基本接口，通过一个可消费的Observer
 * @param <T> the element type
 * @since 2.0
 */
public interface IO_Reactivex_ObservableSource<T> {

    /**
     * Subscribes the given Observer to this ObservableSource instance.
     * 让订阅者即Observer订阅这个 ObservableSource的实例
     *
     * @param observer the Observer, not null
     * @throws NullPointerException if {@code observer} is null  参数为null,则抛 NullPointerException
     */
    void subscribe(@NonNull Observer<? super T> observer);

}