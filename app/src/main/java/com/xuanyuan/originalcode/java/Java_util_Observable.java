package com.xuanyuan.originalcode.java;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * 作者：罗发新
 * 时间：2020/4/15 0015    星期三
 * 邮件：424533553@qq.com
 * 说明：java.util.Observable的源码研究
 * 已进行博客研究写作：https://blog.csdn.net/luo_boke/article/details/105535610
 */

/**
 * This class represents an observable object, or "data"
 * in the model-view paradigm. It can be subclassed to represent an
 * object that the application wants to have observed.
 * 此类表示模型视图范例中的可观察对象或“数据”。可以将其子类化以表示应用程序想要观察的对象。
 * <p>
 * An observable object can have one or more observers. An observer
 * may be any object that implements interface <tt>Observer</tt>.
 *  一个可观察对象可以具有一个或多个观察者。观察者可以是实现接口Observer的任何对象。
 * <p>
 * After an observable instance changes, an application calling the
 * <code>Observable</code>'s <code>notifyObservers</code> method
 * causes all of its observers to be notified of the change by a call
 * to their <code>update</code> method.
 * 在可观察实例发生更改后，调用Observable.notifyObservers方法的应用程序将通过调用其update方法来通知其所有观察者更改。
 *
 * <p>
 * The order in which notifications will be delivered is unspecified.
 * 发送通知的顺序不是有序的
 * The default implementation provided in the Observable class will
 * notify Observers in the order in which they registered interest, but
 * subclasses may change this order, use no guaranteed order, deliver
 * notifications on separate threads, or may guarantee that their
 * subclass follows this order, as they choose.
 * Observable类中提供的默认实现将按照观察者注册兴趣的顺序通知观察者，但是子类可以更改此顺序、使用无保证顺序、
 * 在单独的线程上传递通知，或者可以保证它们的子类根据自己的选择遵循此顺序。
 *
 * <p>
 * Note that this notification mechanism has nothing to do with threads
 * and is completely separate from the <tt>wait</tt> and <tt>notify</tt>
 * mechanism of class <tt>Object</tt>.
 * 注意，这个通知机制与线程无关，与类对象的wait和notify机制完全分离
 * <p>
 * When an observable object is newly created, its set of observers is empty.
 * 当一个可观察的对象刚刚创建时，它的观察者集是空的，此时需要手动的添加观察者
 * Two observers are considered the same if and only if the equals method returns true for them.
 * 当两个观察者通过 equals 方法放回true时，判定这个两个观察者是同一个
 * <p>
 * 1.Observable.notifyObservers()对应方法 Observer.update(java.util.Observable, java.lang.Object)
 * 2. this.setChanged(); 将状态设置为changed = true 后才能进行 this.notifyObservers(state)，此时该方法才能真正有效
 */
public class Java_util_Observable extends Observable {
    private boolean changed = false;
    /**
     * 用于存储观察者
     */
    private Vector<Observer> obs;

    /**
     * Construct an Observable with zero Observers.
     */
    public Java_util_Observable() {
        obs = new Vector<>();
    }

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     * 将观察者添加到此对象的观察者集，前提是它与已存在于该集中的某个观察者不同。
     * 未指定将通知传递给多个观察者的顺序。请参阅类注释。
     *
     * @param o an observer to be added.
     * @throws NullPointerException if the parameter o is null.
     */
    public synchronized void addObserver(Observer o) {
        if (o == null)
            throw new NullPointerException();
        if (!obs.contains(o)) {
            obs.addElement(o);
        }
    }

    /**
     * Deletes an observer from the set of observers of this object.
     * 从该对象的观察者集中删除一个观察者
     * <p>
     * Passing <CODE>null</CODE> to this method will have no effect.
     * 传递一个null ,对该方法无影响
     *
     * @param o the observer to be deleted.
     */
    public synchronized void deleteObserver(Observer o) {
        obs.removeElement(o);
    }

    /**
     * If this object has changed, as indicated by the
     * <code>hasChanged</code> method, then notify all of its observers
     * and then call the <code>clearChanged</code> method to
     * indicate that this object has no longer changed.
     * 如果此对象已更改，如<code>has changed</code>方法所示，则通知其所有观察者，
     * 然后调用<code>clearChanged</code>方法以指示此对象已不再更改。只会通知观察者一次
     * <p>
     * Each observer has its <code>update</code> method called with two arguments:
     * 每个观察者都有其用两个参数调用的<code>update</code>方法
     * <p>
     * this observable object and <code>null</code>. In other words, this method is equivalent to:
     * 这个可观察对象和<code>null</code>。换句话说，这种方法相当于：
     * <blockquote><tt>
     * notifyObservers(null)</tt></blockquote>
     *
     * @see Observable#clearChanged()
     * @see Observable#hasChanged()
     * @see Observer#update(Observable, Object)
     */
    public void notifyObservers() {
        notifyObservers(null);
    }

    /**
     * If this object has changed, as indicated by the
     * <code>hasChanged</code> method, then notify all of its observers
     * and then call the <code>clearChanged</code> method to indicate
     * that this object has no longer changed.
     * <p>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and the <code>arg</code> argument.
     *
     * @param arg any object.
     * @see Observable#clearChanged()
     * @see Observable#hasChanged()
     * @see Observer#update(Observable, Object)
     */
    public void notifyObservers(Object arg) {
        /*
         * a temporary array buffer, used as a snapshot of the state of current Observers.
         * 临时保存的各观察者的状态
         */
        Object[] arrLocal;

        synchronized (this) {
            /* We don't want the Observer doing callbacks into arbitrary code while holding its own Monitor.
             * 我们不希望观察者在保留自己的监视器的同时回调到任意代码中。
             *
             * The code where we extract each Observable from the Vector and store the state of the Observer
             * needs synchronization, but notifying observers does not (should not).
             * 我们从向量中提取每个观测值并存储观察者状态的代码需要同步，但是通知观察者不需要（不应该）。
             *
             * The worst result of any potential race-condition here is that:
             * 任何潜在竞争条件的最坏结果是：
             * 1) a newly-added Observer will miss a notification in progress
             * 1） 新添加的观察者将错过正在进行的通知
             *
             * 2) a recently unregistered Observer will be wrongly notified when it doesn't care
             * 2） 一个最近未注册的观察者在不在乎的时候会被错误地通知
             */

            // Android-changed: Call out to hasChanged() to figure out if something changes.
            // Upstream code avoids calling the nonfinal hasChanged() from the synchronized block,
            // but that would break compatibility for apps that override that method.
            // Android changed：调用hasChanged（）以确定是否有更改。上游代码避免从synchronized块调用nonfinal hasChanged（），
            // 但这将破坏覆盖该方法的应用程序的兼容性。
            // if (!changed)
            if (!hasChanged())
                return;
            arrLocal = obs.toArray();
            //如果有改变将 changed=false,同时进行后面的操作
            clearChanged();
        }

        // 通知每个观察者进行update操作
        for (int i = arrLocal.length - 1; i >= 0; i--)
            ((Observer) arrLocal[i]).update(this, arg);
    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     * 清理所有的观察者
     */
    public synchronized void deleteObservers() {
        obs.removeAllElements();
    }

    /**
     * Marks this <tt>Observable</tt> object as having been changed; the
     * <tt>hasChanged</tt> method will now return <tt>true</tt>.
     * 通知观察者有更改后，设置标识已经更改过改变
     */
    protected synchronized void setChanged() {
        changed = true;
    }

    /**
     * Indicates that this object has no longer changed, or that it has
     * already notified all of its observers of its most recent change,
     * so that the <tt>hasChanged</tt> method will now return <tt>false</tt>.
     * This method is called automatically by the
     * <code>notifyObservers</code> methods.
     * 指示此对象已不再更改，或已通知其所有观察者其最近的更改，
     * 因此hasChanged方法现在将返回false此方法由notifyObservators方法自动调用。
     *
     * @see Observable#notifyObservers()
     * @see Observable#notifyObservers(Object)
     */
    protected synchronized void clearChanged() {
        changed = false;
    }

    /**
     * Tests if this object has changed.
     * 被观察者是否有数据的改变，有改变则通知观察者update
     *
     * @return <code>true</code> if and only if the <code>setChanged</code>
     * method has been called more recently than the
     * <code>clearChanged</code> method on this object;
     * <code>false</code> otherwise.
     * @see Observable#clearChanged()
     * @see Observable#setChanged()
     */
    public synchronized boolean hasChanged() {
        return changed;
    }

    /**
     * Returns the number of observers of this <tt>Observable</tt> object.
     * 返回 被观察者的数量
     *
     * @return the number of observers of this object.
     */
    public synchronized int countObservers() {
        return obs.size();
    }
}

class Java_util_Observer implements Observer {

    /**
     * 当 Observable对象调用notifyObservers(),会触发观察者的upDate方法。
     */
    @Override
    public void update(Observable o, Object arg) {

    }
}
