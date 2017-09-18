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

package org.axonframework.repository;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.AggregateRoot;

/**
 * The repository provides an abstraction of the storage of aggregates.
 *  该接口的实现类用于保存聚合到仓库
 * @author Allard Buijze
 * @param <T> The type of aggregate this repository stores.
 * @since 0.1
 */
public interface Repository<T extends AggregateRoot> {

    /**
     * Load the aggregate with the given unique <code>aggregateIdentifier</code>, expecting the version of the aggregate
     * to be equal to the given <code>expectedVersion</code>. If the <code>expectedVersion</code> is <code>null</code>,
     * no version validation is done.
     * <p/>
     * When versions do not match, implementations may either raise an exception immediately when loading an aggregate,
     * or at any other time while the aggregate is registered in the current Unit Of Work.
     *
     * @param aggregateIdentifier The identifier of the aggregate to load
     * @param expectedVersion     The expected version of the aggregate to load, or <code>null</code> to indicate the
     *                            version should not be checked
     * @return The aggregate root with the given identifier.
     *
     * @throws AggregateNotFoundException if aggregate with given id cannot be found
     * @throws ConflictingModificationException
     *                                    if the <code>expectedVersion</code> did not match the aggregate's actual
     *                                    version
     * @see org.axonframework.unitofwork.UnitOfWork
     */
    /**
     * 通过给定的aggregateIdentifier(聚合唯一标识符)获取聚合，并期望获取给定的版本号的聚合。如果该给定值为空，则对返回的聚合不做验证
     * <p/>
     * 如果版本不匹配，该实习类可能会在加载的时候立即抛出异常。或者会在将该聚合注册到工作单元的时候
     * @param aggregateIdentifier
     * @param expectedVersion
     * @throws AggregateNotFoundException 找不到对应识别码的聚合
     * @throws ConflictingModificationException 版本不一致
     * @return
     */
    T load(AggregateIdentifier aggregateIdentifier, Long expectedVersion);

    /**
     * Load the aggregate with the given unique identifier. No version checks are done when loading an aggregate,
     * meaning that concurrent access will not be checked for.
     *
     * @param aggregateIdentifier The identifier of the aggregate to load
     * @return The aggregate root with the given identifier.
     *
     * @throws AggregateNotFoundException if aggregate with given id cannot be found
     */
    /**
     * 根据聚合识别码获取聚合
     * @param aggregateIdentifier
     * @throws AggregateNotFoundException 找不到对应识别码的聚合
     * @return
     */
    T load(AggregateIdentifier aggregateIdentifier);

    /**
     * Adds the given <code>aggregate</code> to the repository. The version of this aggregate must be <code>null</code>,
     * indicating that it has not been previously persisted.
     * <p/>
     * This method will not force the repository to save the aggregate immediately. Instead, it is registered with the
     * current UnitOfWork. To force storage of an aggregate, commit the current unit of work
     * (<code>CurrentUnitOfWork.commit()</code>)
     *
     * @param aggregate The aggregate to add to the repository.
     * @throws IllegalArgumentException if the given aggregate is not newly created. This means {@link
     *                                  org.axonframework.domain.AggregateRoot#getVersion()} must return
     *                                  <code>null</code>.
     */
    /**
     * 添加聚合到仓库，版本必须为空。这样意味着这个聚合之前没有被持久化
     * <p/>
     * 该方法不会再方法执行后立即添加到仓库，而是会将该聚合注册到工作单元，等待工作单元提交后再保存到仓库
     *
     * @throws IllegalArgumentException 表示该聚合不是新建立的，这个意味着 聚合的版本必须为null
     * @param aggregate
     */
    void add(T aggregate);
}
