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

package org.axonframework.auditing;

import org.axonframework.domain.Event;

import java.util.List;

/**
 * Interface describing a component capable of writing auditing entries to a log.
 *
 * @author Allard Buijze
 * @since 0.7
 */
public interface AuditLogger {

    /**
     * Writes a success entry to the audit logs.
     * <p/>
     * This method may be invoked in the thread dispatching the command. Therefore, considering writing asynchronously
     * when the underlying mechanism is slow.
     *
     * @param command     The command that has been handled
     * @param returnValue The return value of the command handler
     * @param events      The events that were generated during command handling
     */
    /**
     * 写入一个成功实体到审核日志
     * <p/>
     * 该方法可以在调度命令的线程中调用，所以当潜在机制较慢时，可以考虑异步写入
     * @param command
     * @param returnValue
     * @param events
     */
    void logSuccessful(Object command, Object returnValue, List<Event> events);

    /**
     * Writes a failure entry to the audit logs. The given list of events may contain events. In that case, these event
     * may have been stored in the events store and/or published to the event bus.
     * <p/>
     * This method may be invoked in the thread dispatching the command. Therefore, considering writing asynchronously
     * when the underlying mechanism is slow.
     *
     * @param command      The command being executed
     * @param failureCause The cause of the rollback. May be <code>null</code> if the rollback was not caused by an
     *                     exception
     * @param events       any events staged for storage or publishing
     */
    /**
     *  写入一个成功实体到审核日志,给定的事件可能包含事件，这些事件可能存到事件池或发布到事件总线上
     * <p/>
     * 该方法可以在调度命令的线程中调用，所以当潜在机制较慢时，可以考虑异步写入
     * @param command
     * @param failureCause
     * @param events
     */
    void logFailed(Object command, Throwable failureCause, List<Event> events);
}
