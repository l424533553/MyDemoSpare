package com.xuanyuan.originalcode.android.rxjava;

/**
 * 作者：罗发新
 * 时间：2020/4/15 0015    星期三
 * 邮件：424533553@qq.com
 * 说明：io.reactivex.Observer的源码研究
 * 已进行博客研究写作：https://blog.csdn.net/luo_boke/article/details/105535610
 */

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * Provides a mechanism for receiving push-based notifications.
 * 提供接收基于推送的通知的机制。
 * <p>
 * When an {@code Observer} is subscribed to an {ObservableSource} through the { ObservableSource#subscribe(IO_Reactivex_Observer)} method,
 * 当一个Observer被订阅为observatesource通过订阅者方法 ，
 * <p>
 * the {@code ObservableSource} calls {@link #onSubscribe(Disposable)}  with a {@link Disposable} that allows disposing the sequence at any time,
 * then the {@code ObservableSource} may call the Observer's {@link #onNext} method any number of times to provide notifications.
 * ObservableSource可以在任何时间通知被观察者状态已改变，该{@code observatesource}可以调用观察者的{@link\onNext}方法提供通知的次数不限。
 * <p>
 * A well-behaved {@code ObservableSource} will call an {@code Observer}'s {@link #onComplete} method exactly once or the {@code Observer}'s {@link #onError} method exactly once.
 * 行为良好的{@code observatesource}将准确地调用{@code Observer}的{@link}onComplete}方法一次，或者准确地调用{@code observator}的{@link}onError}方法一次。
 *
 * <p>
 * Calling the {@code Observer}'s method must happen in a serialized fashion, that is,
 * 调用{@code Observer}的方法必须以序列化的方式进行，也就是说，
 * they must not be invoked concurrently by multiple threads in an overlapping fashion
 * 他们一定不能同时被调用__ 被多线程用重叠的方式
 * <p>
 * and the invocation pattern must adhere to the following protocol:
 * 并且调用模式必须 遵守以下规则：
 * 顺序模式是 onSubscribe >>  onNext >> (onError | onComplete)
 * <p>
 * Subscribing an {@code Observer} to multiple {@code ObservableSource}s is not recommended.
 * 不建议将一个观察者订阅到多个被观察者上，容易造成混乱
 * If such reuse happens, it is the duty of the {@code Observer} implementation to be ready to receive multiple calls to
 * its methods and ensure proper concurrent behavior of its business logic.
 * 如果发生了这样的事情，有必要准备实现被多次调用方法，并且确保正确的并发业务逻辑
 * <p>
 * Calling {@link #onSubscribe(Disposable)}, {@link #onNext(Object)} or {@link #onError(Throwable)} with a null argument is forbidden.
 * observable.subscribe(Observer observer)调用，使用一个null的参数是会被禁止的
 * <p>
 * <p>
 * The implementations of the {@code onXXX} methods should avoid throwing runtime exceptions other than the following cases
 * 在实现onXXX时应该避免抛出运行时异常除了以下的原因，见链接中的文章
 * (see <a href="https://github.com/reactive-streams/reactive-streams-jvm#2.13">Rule 2.13</a> of the Reactive Streams specification反应流的规范):
 *
 * <li>If the argument is {@code null}, the methods can throw a {@code NullPointerException}.
 * 如果observable.subscribe(Observer observer) 的参数是null,则会抛出一个 NullPointerException
 * <p>
 * Note though that RxJava prevents {@code null}s to enter into the flow and thus there is generally no need to check for nulls in flows assembled from standard sources and intermediate operators.
 * 请注意虽然Rxjava阻止空参进入流，因此通常是没有检查flows中null 的组装标准来源和中间运营商
 * <p>
 * If there is a fatal error (such as {@code VirtualMachineError})如果有一个致命的错误，如VirtualMachineError
 * <p>
 * Violating Rule 2.13 results in undefined flow behavior. Generally, the following can happen:
 * 违反规则2.13结果未定义流的行为。一般来说,以下可能发生
 * <p>
 * 1、An upstream operator turns it into an {@link #onError} call，一个上游运营商把它转为一个onError 调用
 * 2、If the flow is synchronous, the {ObservableSource#subscribe(IO_Reactivex_Observer)} throws instead of returning normally
 * 如果flow是同步的, ObservableSource的subscribe()方法将被抛出而不是正常的返回
 * <p>
 * 3、If the flow is asynchronous, the exception propagates up to the component ({@Scheduler} or {@link java.util.concurrent.Executor})
 * providing the asynchronous boundary the code is running and either routes the exception to the global
 * 如果flow是异步的，除了通讯组件Scheduler 或者 Executor 提供正在运行的异步边界代码，或者其他的全局异常。
 * 如抛出onError(Throwable)或者UncaughtExceptionHandler.uncaughtException(Thread, Throwable)
 * {@link io.reactivex.rxjava3.plugins.RxJavaPlugins#onError(Throwable)} handler or the current thread's
 * {@link Thread.UncaughtExceptionHandler#uncaughtException(Thread, Throwable)} handler.</li>
 * <p>
 * From the {@code Observable}'s perspective,an {@code Observer} is the end consumer thus it is the {@code Observer}'s responsibility to handle the error case and signal it "further down".
 * 从被观察者的角度来看一个观察者是最终的消费者，因此被观察者有责任去处理error的原因并且给出 "further down"信号
 * <p>
 * This means unreliable code in the {@code onXXX} methods should be wrapped into `try-catch`es,specifically in {@link #onError(Throwable)} or {@link #onComplete()},
 * and handled there (for example, by logging it or presenting the user with an error dialog)
 * 这意味着在 onXXX中的不可靠的方法应该被try-catch包裹并处理它们( 如 打log日志或者给一个错误提示对话框)，特别是onError 和 onComplete中
 * <p>
 * However, if the error would be thrown from {@link #onNext(Object)}, <a href="https://github.com/reactive-streams/reactive-streams-jvm#2.13">Rule 2.13</a> mandates
 * 然而，如果error应该从onNext(Object)方法中被抛出,<a href="https://github.com/reactive-streams/reactive-streams-jvm#2.13">Rule 2.13 规定
 * <p>
 * the implementation calls {@link Disposable#dispose()} and signals the exception in a way that is adequate to the target context,
 * 实现调用或者标记这个exception的方法应该 适合匹配上下文
 * for example, by calling {@link #onError(Throwable)} on the same {@code Observer} instance.
 * 例如：在同一个 Observer实例中调用onError(Throwable)方法
 * <p>
 * If, for some reason, the {@code Observer} won't follow Rule 2.13,the {@Observable#safeSubscribe(IO_Reactivex_Observer)} can wrap it with the necessary safeguards
 * 处于一些原因不遵守Rule2.13, Observable.safeSubscribe(Observer)方法可以一些安全措施下使用，
 * <p>
 * and route exceptions thrown from {@code onNext} into {@code onError} and route exceptions thrown
 * 并且route exceptions被从onNext 或者 onError抛出 ，或者被从onError和onComplete抛出的全局route exceptions处理通过
 * from {@code onError} and {@code onComplete} into the global error handler via {@link io.reactivex.rxjava3.plugins.RxJavaPlugins#onError(Throwable)}.
 *
 * @param <T> the type of item the Observer expects to observe 观察者预计观察的类型
 * @see <a href="http://reactivex.io/documentation/observable.html">ReactiveX documentation: Observable</a>
 */
public interface IO_Reactivex_Observer<T> {

    /**
     * Provides the Observer with the means of cancelling (disposing)
     * 为 Observer 提供了取消 连接Observable的同步和异步的两种方法（从 onNext(Object)的内部方法中），
     * the connection (channel) with the Observable in both synchronous (from within {@link #onNext(Object)}) and asynchronous manner.
     *
     * @param d the Disposable instance whose {@link Disposable#dispose()} can
     *          be called anytime to cancel the connection
     *          Disposable的实例对象可以通过dispose()随时取消 观察者和被观察者直接的关系，放弃该次观察
     * @since 2.0
     * ******** 在订阅observable时回调，可以在这里调用Disposable.dispose取消订阅或者将Disposable对象保存起来以便在后续某个时刻取消订阅。
     */
    void onSubscribe(@NonNull Disposable d);

    /**
     * Provides the Observer with a new item to observe.
     * 为观察者提供了一个新的项目 去观察
     * <p>
     * The {@Observable} may call this method 0 or more times.
     * 被观察者可能一次或多次调用该方法
     * <p>
     * The {@code Observable} will not call this method again after it calls either {@link #onComplete} or {@link #onError}.
     * Observable调用了onComplete或者onError方法后将不再调用该方法，
     *
     * @param t the item emitted by the Observable  参数对Observable是可见的
     *          <p>
     *          *******  在ObservableEmitter.onNext执行后回调，onNext表示的是整个响应链中的一环，在这里处理响应链中的其中一个任务，可以多次调用。
     */
    void onNext(@NonNull T t);

    /**
     * Notifies the Observer that the {Observable} has experienced an error condition.
     * 通知观察者,Observable发生了一个error condition
     * <p>
     * If the { Observable} calls this method, it will not thereafter call {@link #onNext} or {@link #onComplete}.
     * 如果Observable调用了这个方法，在这之后将不再调用onNext 或者 onComplete方法
     *
     * @param e the exception encountered by the Observable   Observable碰到的exception
     *          <p>
     *          *******在ObservableEmitter.onError执行后或者链中任一环节出现异常时回调，表示任务执行失败。
     */
    void onError(@NonNull Throwable e);

    /**
     * Notifies the Observer that the {Observable} has finished sending push-based notifications.
     * 通知Observer,  Observable已经完成了推送的消息
     * <p>
     * The {Observable} will not call this method if it calls {@link #onError}.
     * Observable将不再调用这个方法,如果它调用过了onError方法
     * <p>
     * ******在ObservableEmitter.onComplete执行后回调，表示任务已全部完成，可以在这里做收尾工作。
     */
    void onComplete();

}
