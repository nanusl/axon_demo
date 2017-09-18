/*
 * Copyright (c) 2010-2011. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.unitofwork;

import org.axonframework.domain.AggregateRoot;
import org.axonframework.domain.Event;
import org.axonframework.eventhandling.EventBus;

/**
 * This class represents a UnitOfWork in which modifications are made to aggregates. A typical UnitOfWork scope is the
 * execution of a command. A UnitOfWork may be used to prevent individual events from being published before a number of
 * aggregates has been processed. It also allows repositories to manage resources, such as locks, over an entire
 * transaction. Locks, for example, will only be released when the UnitOfWork is either committed or rolled back.
 * <p/>
 * The current UnitOfWork can be obtained using {@link CurrentUnitOfWork#get()}.
 *
 * @author Allard Buijze
 * @since 0.6
 */
/**
 * 该类代表一个能够修改聚合根的工作单元，一个典型的工作单元的范围是一个命令的执行。一个工作单元主要用于，防止在聚合根被处理完成前，单个的事件被发布出去。
 * 它也能通过仓库来管理资源，比如 锁。事务，比如锁在事件已提交或回滚后才能释放
 * <p/>
 * 获取当前工作单元 可以通过 {@link CurrentUnitOfWork#get()}
 * @author Administrator
 *
 */
public interface UnitOfWork {

    /**
     * Commits the UnitOfWork. All registered aggregates that have not been registered as stored are saved in their
     * respective repositories, buffered events are sent to their respective event bus, and all registered
     * UnitOfWorkListeners are notified.
     * <p/>
     * After the commit (successful or not), the UnitOfWork is unregistered from the CurrentUnitOfWork.
     *
     * @throws IllegalStateException if the UnitOfWork wasn't started
     */
    /**
     * 提交工作单元，所有已注册但没有注册到相对应仓库的聚合根，缓存事件将会发送到对应的事件总线，所有已注册的工作单元监听器都将被通知
     * <p/>
     * 不管是否成功提交，该工作单元将会从CurrentUnitOfWork移除，并清理掉所有已占用资源，这也意味着，如果提交失败，所有东西被清掉
     */
    void commit();

    /**
     * Clear the UnitOfWork of any buffered changes. All buffered events and registered aggregates are discarded and
     * registered {@link org.axonframework.unitofwork.UnitOfWorkListener}s are notified.
     * <p/>
     * If the rollback is a result of an exception, consider using {@link #rollback(Throwable)} instead.
     */
    /**
     * 清理掉所有的缓存，所有的缓存事件和已注册的聚合根将会被丢弃  并且通知所有注册到该工作单元的监听器
     * <p/>
     * 如果 引起回滚的是因为异常，那么建议使用 {@link UnitOfWork#rollback(Throwable)}来代替
     */
    void rollback();

    /**
     * Clear the UnitOfWork of any buffered changes. All buffered events and registered aggregates are discarded and
     * registered {@link org.axonframework.unitofwork.UnitOfWorkListener}s are notified.
     *
     * @param cause The cause of the rollback. May be <code>null</code>.
     * @throws IllegalStateException if the UnitOfWork wasn't started
     */
    /**
     *  清理掉所有的缓存，所有的缓存事件和已注册的聚合根将会被丢弃  并且通知所有注册到该工作单元的监听器
     * @param cause 引起回退的异常
     */
    void rollback(Throwable cause);

    /**
     * Starts the current unit of work, preparing it for aggregate registration. The UnitOfWork instance is registered
     * with the CurrentUnitOfWork.
     */
    /**
     * 启动当前工作单元，准备注册聚合根。并将当前工作单元注册到 CurrentUnitOfWork
     */
    void start();

    /**
     * Indicates whether this UnitOfWork is started. It is started when the {@link #start()} method has been called, and
     * if the UnitOfWork has not been committed or rolled back.
     *
     * @return <code>true</code> if this UnitOfWork is started, <code>false</code> otherwise.
     */
    /**
     * 判断工作单元是否已经启动，并且该工作单元没有提交和回滚
     * @return
     */
    boolean isStarted();

    /**
     * Register a listener that listens to state changes in this UnitOfWork. This typically allows components to clean
     * up resources, such as locks, when a UnitOfWork is committed or rolled back. If a UnitOfWork is partially
     * committed, only the listeners bound to one of the committed aggregates is notified.
     *
     * @param listener The listener to notify when the UnitOfWork's state changes.
     */
    /**
     * 注册UnitOfWork监听器，用于监听UnitOfWork的状态改变，一般用于允许组件清理资源，比如锁，当UnitOfWork提交或者回滚。 如果一个工作单元部分提交。
     * 那么只有关联到这个已提交聚合根的监听器被通知
     * @param listener
     */
    void registerListener(UnitOfWorkListener listener);

    /**
     * Register an aggregate with this UnitOfWork. These aggregates will be saved (at the latest) when the UnitOfWork is
     * committed. This method returns the instance of the aggregate root that should be used as part of the processing
     * in this Unit Of Work.
     * <p/>
     * If an aggregate of the same type and with the same identifier has already been registered, one of two things may
     * happen, depending on the actual implementation: <ul><li>the instance that has already been registered is
     * returned. In which case the given <code>saveAggregateCallback</code> is ignored.</li><li>an IllegalStateException
     * is thrown to indicate an illegal attempt to register a duplicate</li></ul>.
     *
     * @param aggregateRoot         The aggregate root to register in the UnitOfWork
     * @param saveAggregateCallback The callback that is invoked when the UnitOfWork wants to store the registered
     *                              aggregate
     * @param <T>                   the type of aggregate to register
     * @return The actual aggregate instance to use
     *
     * @throws IllegalStateException if this Unit Of Work does not support registrations of aggregates with identical
     *                               type and identifier
     */
    /**
     * 注册一个聚合根到UnitOfWork，当UnitOfWork提交时该聚合根将被保存到仓库。该方法的返回聚合根实例将会作为该工作单元处理进程的组成
     * <p/>
     * 如果一个聚合已有一个一样的已经被注册，一下两件有可能发生
     * <ul>
     * <li>1,返回已被注册的聚合<code>SaveAggregateCallback<code/>将被忽略<li/>
     * <li>2,抛出一个异常IllegalStateException，提示非法尝试注册一个重复的聚合，<li/>
     * <ul/>
     * @param aggregateRoot 被注册的聚合根
     * @param saveAggregateCallback 当UnitOfWork将要保存这个聚合根的时候，回调函数将会被执行
     * @return
     */
    <T extends AggregateRoot> T registerAggregate(T aggregateRoot,
                                                  SaveAggregateCallback<T> saveAggregateCallback);

    /**
     * Request to publish the given <code>event</code> on the given <code>eventBus</code>. The UnitOfWork may either
     * publish immediately, or buffer the events until the UnitOfWork is committed.
     *
     * @param event    The event to be published on the event bus
     * @param eventBus The event bus on which to publish the event
     */
    /**
     * 请求将一个事件发布到事件总线上。UnitOfWork可以立即发布也可以缓存事件直到UnitOfWork提交再发布
     * @param event			将要发布到事件总线的事件
     * @param eventBus		事件总线
     */
    void publishEvent(Event event, EventBus eventBus);
}
