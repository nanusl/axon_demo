/*
 * Copyright (c) 2011. Axon Framework
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

package org.axonframework.eventhandling;

/**
 * Specialist interface for implementations of an event listener that redirect actual processing to another instance.
 *
 * @author Allard Buijze
 * @since 0.6
 */
/**
 * 该接口用于一个监听器实例，将具体的处理过程转发给其他实例来处理
 * @author Administrator
 *
 */
public interface EventListenerProxy extends EventListener {

    /**
     * Returns the instance that this proxy delegates all event handling to.
     *
     * @return the instance that this proxy delegates all event handling to
     */
    /**
     * 返回此代理委托所有事件处理的实例。
     * @return
     */
    Object getTarget();
}
