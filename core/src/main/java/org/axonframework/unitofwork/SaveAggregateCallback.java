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

/**
 * Callback used by UnitOfWork instances to be invoked when the UnitOfWork wishes to store an aggregate. This callback
 * abstracts the actual storage mechanism away from the UnitOfWork itself.
 *
 * @author Allard Buijze
 * @param <T> The type of aggregate this callback handles
 * @since 0.6
 */
/**
 * 回调，用于处理当UnitOfWork希望保持聚合，
 * 该接口的实例可以将实际的存储机制从UnitOfWork中分离出来
 * @author Administrator
 *
 * @param <T>
 */
public interface SaveAggregateCallback<T extends AggregateRoot> {

    /**
     * Invoked when the UnitOfWork wishes to store an aggregate.
     *
     * @param aggregate The aggregate to store
     */
    /**
     * 当UnitOfWork希望保持实例时，执行
     * @param aggregate
     */
    void save(T aggregate);

}
