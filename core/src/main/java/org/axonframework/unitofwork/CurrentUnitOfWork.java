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

import java.util.Deque;
import java.util.LinkedList;

/**
 * Default entry point to gain access to the current UnitOfWork. Components managing transactional boundaries can
 * register and clear UnitOfWork instances, which components can use.
 *默认入口点可以访问当前的UnitOfWork。 管理事务边界的组件可以注册和清除UnitOfWork实例，哪些组件可以使用。
 * @author Allard Buijze
 * @since 0.6
 */
public abstract class CurrentUnitOfWork {

    private static final ThreadLocal<Deque<UnitOfWork>> CURRENT = new ThreadLocal<Deque<UnitOfWork>>();

    private CurrentUnitOfWork() {
    }

    /**
     * Indicates whether a unit of work has already been started. This method can be used by interceptors to prevent
     * nesting of UnitOfWork instances.
     *
     * @return whether a UnitOfWork has already been started.
     */
    public static boolean isStarted() {
        return CURRENT.get() != null && !CURRENT.get().isEmpty();
    }

    /**
     * Gets the UnitOfWork bound to the current thread. If no UnitOfWork has been started, an {@link
     * IllegalStateException} is thrown.
     * <p/>
     * To verify whether a UnitOfWork is already active, use {@link #isStarted()}.
     *
     * @return The UnitOfWork bound to the current thread.
     *
     * @throws IllegalStateException if no UnitOfWork is active
     */
    /**
     * 获取当前线程的工作单元，如果没有工作单元启动，那么{@link IllegalStateException} 异常会抛出
     * @return
     */
    public static UnitOfWork get() {
        if (isEmpty()) {
            throw new IllegalStateException("No UnitOfWork is currently started for this thread.");
        }
        return CURRENT.get().peek();
    }

    private static boolean isEmpty() {
        Deque<UnitOfWork> unitsOfWork = CURRENT.get();
        return unitsOfWork == null || unitsOfWork.isEmpty();
    }

    /**
     * Commits the current UnitOfWork. If no UnitOfWork was started, an {@link IllegalStateException} is thrown.
     *
     * @throws IllegalStateException if no UnitOfWork is currently started.
     * @see org.axonframework.unitofwork.UnitOfWork#commit()
     */
    /**
     * 提交当前的工作单元。如果没有工作单元启动，那么{@link IllegalStateException} 异常会抛出
     */
    public static void commit() {
        get().commit();
    }

    /**
     * Binds the given <code>unitOfWork</code> to the current thread. If other UnitOfWork instances were bound, they
     * will be marked as inactive until the given UnitOfWork is cleared.
     *
     * @param unitOfWork The UnitOfWork to bind to the current thread.
     */
    /**
     * 绑定工作单元到当前线程。如果有其他工作单元已经绑定，那么之前的工作单元会被标记为非激活状态，直到这个传入的工作单元被清除才会再次激活
     * @param unitOfWork
     */
    static void set(UnitOfWork unitOfWork) {
        if (CURRENT.get() == null) {
            CURRENT.set(new LinkedList<UnitOfWork>());
        }
        CURRENT.get().push(unitOfWork);
    }

    /**
     * Clears the UnitOfWork currently bound to the current thread, if that UnitOfWork is the given
     * <code>unitOfWork</code>. Otherwise, nothing happens.
     *
     * @param unitOfWork The UnitOfWork expected to be bound to the current thread.
     * @throws IllegalStateException when the given UnitOfWork was not the current active UnitOfWork. This exception
     *                               indicates a potentially wrong nesting of Units Of Work.
     */
    /**
     * 清除当前线程的与传入的工作单元一致的工作单元，有匹配的清除，没有的话，抛出清除不了的异常
     * @param unitOfWork
     */
    static void clear(UnitOfWork unitOfWork) {
        if (isStarted() && CURRENT.get().peek() == unitOfWork) {
            CURRENT.get().pop();
            if (CURRENT.get().isEmpty()) {
                CURRENT.remove();
            }
        } else {
            throw new IllegalStateException("Could not clear this UnitOfWork. It is not the active one.");
        }
    }
}
