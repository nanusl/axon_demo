/*
 * Copyright (c) 2010. Axon Framework
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

import java.util.List;
import java.util.Set;

/**
 * Interface describing a listener that is notified of state changes in the UnitOfWork it has been registered with.
 *
 * @author Allard Buijze
 * @since 0.6
 */
/**
 * 接口用于描述 一个监听器，UnitOfWOrk状态改变时将通知已注册的监听器
 * @author Administrator
 *
 */
public interface UnitOfWorkListener {

    /**
     * Invoked when the UnitOfWork is committed. The aggregate has been saved and the events have been scheduled for
     * dispatching. In some cases, the events could already have been dispatched. When processing of this method causes
     * an exception, a UnitOfWork may choose to call {@link #onRollback(Throwable)} consecutively.
     *
     * @see UnitOfWork#commit()
     */
    /**
     * UnitOfWOrk提交后，该方法执行。聚合被保存，并且事件将被调度分发。
     * 在某些情况下，事件可能已经被分发
     * 当该方法出现执行异常时，工作单元会调用{@link #onRollback(Throwable)}
     */
    void afterCommit();

    /**
     * Invoked when the UnitOfWork is rolled back. The UnitOfWork may choose to invoke this method when committing the
     * UnitOfWork failed, too.
     *
     * @param failureCause The exception (or error) causing the roll back
     * @see UnitOfWork#rollback(Throwable)
     */
    /**
     * 工作单元回滚时执行该方法。工作单元执行失败可能选择执行该方法，
     * @param failureCause 失败原因
     */
    void onRollback(Throwable failureCause);

    /**
     * Invoked before aggregates are committed, and before any events are published. This phase can be used to do
     * validation or other activity that should be able to prevent event dispatching in certain circumstances.
     * <p/>
     * Note that the given <code>events</code> may not contain the uncommitted domain events of each of the
     * <code>aggregateRoots</code>. To retrieve all events, collect all uncommitted events from the aggregate roots and
     * combine them with the list of events.
     *
     * @param aggregateRoots the aggregate roots being committed
     * @param events         Events that have been registered for dispatching with the UnitOfWork
     */
    /**
     * 聚合提交前并且在事件的发布前执行。这个阶段可以用于验证或其他行为来防止一些确定情况下的事件分发
     * <p/>
     * 注意，给定的事件中不包含聚合根中所有未提交领域事件。如果要获取所有事件，那么需要收集所有聚合根中事件，并将这些事件加入到事件列表中
     * @param aggregateRoots	需要被提交的聚合根
     * @param events	已注册的事件，并将要被分发的事件
     */
    void onPrepareCommit(Set<AggregateRoot> aggregateRoots, List<Event> events);

    /**
     * Notifies listeners that the UnitOfWork is being cleaned up. This gives listeners the opportunity to clean up
     * resources that might have been used during commit or rollback, such as remaining locks, open files, etc.
     * <p/>
     * This method is always called after all listeners have been notified of a commit or rollback.
     */
    /**
     * 工作单元将被清空时，通知所有已注册的监听器，，这个方法将给所有的监听器提供机会去清理掉哪些在提交或回滚时用到的资源，比如锁
     */
    void onCleanup();
}
