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

package org.axonframework.eventhandling;

import org.axonframework.domain.Event;

/**
 * Specification of the mechanism on which the Event Listeners can subscribe for events and event publishers can publish
 * their events. The event bus dispatches event to all subscribed listeners.
 * <p/>
 * Implementations may or may not dispatch the events to event listeners in the dispatching thread.
 *
 * @author Allard Buijze
 * @see EventListener
 * @see SimpleEventBus
 * @since 0.1
 */
/**
 * 一种机制，让事件监听器订阅事件，事件发布器发布事件。
 * 事件总线将所有事件分发到所有订阅该事件的监听器上
 * <p/>
 * 接口实现可能也可能不会将事件发布到分发线程的事件订阅者
 * @author Administrator
 *
 */
public interface EventBus {

    /**
     * Publish an event on this bus. It is dispatched to all subscribed event listeners.
     *
     * @param event the event to publish
     */
    /**
     * 将事件集发布到事件总线上，这些事件最终将分发到订阅者。
     * <p/>
     * 实现可以将给定事件作为单批量处理。并将这个鞋事件分发到所有订阅者手里
     * @param event
     */
    void publish(Event event);

    /**
     * Subscribe the given <code>eventListener</code> to this bus. When subscribed, it will receive all events published
     * to this bus.
     *
     * @param eventListener The event listener to subscribe
     */
    /**
     * 将事件监听器订阅到总线上。如果已订阅，那么将接收到所有发布到总线上的事件
     * <p/>
     * 如果该监听器已经存在于总线上，那么不会发生任何事情
     * @param eventListener
     */
    void subscribe(EventListener eventListener);

    /**
     * Unsubscribe the given <code>eventListener</code> to this bus. When unsubscribed, it will no longer receive events
     * published to this bus.
     *
     * @param eventListener The event listener to unsubscribe
     */
    /**
     * 取消订阅，如果监听器从总线上取消订阅，那么将不再接收任何事件
     * @param eventListener
     */
    void unsubscribe(EventListener eventListener);

}
