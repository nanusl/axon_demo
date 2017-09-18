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

package org.axonframework.util;

/**
 * Interface describing components that are able to subscribe themselves to a component that can be subscribed to, such
 * as the CommandBus and EventBus.
 *
 * @author Allard Buijze
 * @since 0.5
 */
/**
 * 接口用于描述一个组件可以将自己订阅到其他组件，用于接收发布的订阅消息，如将消息订阅到这两个组件{@link CommandBus} {@link EventBus}
 * @author Administrator
 *
 *该组件在新版本已删除 因为如果一个组件可以被订阅，而有包含一个可订阅组件时会出现问题
 */
public interface Subscribable {

    /**
     * Unsubscribe this instance from its subscribed component.
     */
    /**
     * 将本实例从发布组件中移除订阅
     */
    void unsubscribe();

    /**
     * Subscribe this instance with its configured component.
     */
    /**
     * 将本实例订阅到发布组件
     */
    void subscribe();
}
