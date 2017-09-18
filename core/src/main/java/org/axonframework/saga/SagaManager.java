/*
 * Copyright (c) 2010. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.saga;

import org.axonframework.domain.Event;
import org.axonframework.eventhandling.EventListener;

/**
 * Interface toward the Manager of one or more types of Saga. The SagaManager is an infrastructure object responsible
 * for redirecting published Events to the correct Saga instances. The SagaManager will also manage the life cycle of
 * the Saga, based on these Events.
 * <p/>
 * Saga Managers must be thread safe. Implementations may choose to provide locking, such that access to the Saga
 * instances they manage is also thread safe.
 *
 * @author Allard Buijze
 * @since 0.7
 */
/**
 * 接口用于管理一个或多个类型的saga，sagaManager是一个基础对象用于将发布的事件关联到saga，基于这些发布的事件，sagaManager也能管理saga的生命周期
 * <p/>
 * sagaManager必须是线程安全的，该接口实例可以提供锁来实现访问saga也是线程安全的
 * @author Administrator
 *
 */
public interface SagaManager extends EventListener {

    /**
     * Handles the event by passing it to all Saga instances that have an Association Value found in the given event.
     * 将事件消息传递给所有关联到该消息中属性的saga实例
     * @param event the event to handle
     */
    @Override
    void handle(Event event);
}
