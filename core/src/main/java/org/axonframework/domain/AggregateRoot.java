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

package org.axonframework.domain;

/**
 * Interface defining a contract for entities that represent the aggregate root.
 *
 * @author Allard Buijze
 * @see org.axonframework.domain.AbstractAggregateRoot
 * @since 0.1
 */
/**
 * 接口用于定义一些聚合根实体的约定
 * @author Administrator
 */
public interface AggregateRoot {

    /**
     * Returns the identifier of this aggregate.
     *
     * @return the identifier of this aggregate
     */
    /**
     * 返回聚合的唯一标识符
     * @return
     */
    AggregateIdentifier getIdentifier();

    /**
     * Clears the events currently marked as "uncommitted".
     */
    /**
     * 清理掉一些被标记为未提交的事件，以及清理掉一些一致的事件回调方法
     */
    void commitEvents();

    /**
     * Returns the number of uncommitted events currently available in the aggregate.
     *
     * @return the number of uncommitted events currently available in the aggregate.
     */
    /**
     * 获取当前可用的有效的未提交事件的个数
     * @return
     */
    int getUncommittedEventCount();

    /**
     * Returns a DomainEventStream to the events in the aggregate that have been raised since creation or the last
     * commit.
     *
     * @return the DomainEventStream to the uncommitted events.
     */
    /**
     * 获取所有从聚合创建到最近提交以来所有的未提交事件
     * @return
     */
    DomainEventStream getUncommittedEvents();

    /**
     * Returns the current version number of the aggregate, or <code>null</code> if the aggregate is newly created. This
     * version must reflect the version number of the aggregate on which changes are applied.
     * <p/>
     * Each time the aggregate is <em>modified and stored</em> in a repository, the version number must be increased by
     * at least 1. This version number can be used by optimistic locking strategies and detection of conflicting
     * concurrent modification.
     * <p/>
     * Typically the sequence number of the last committed event on this aggregate is used as version number.
     *
     * @return the current version number of this aggregate, or <code>null</code> if no events were ever committed
     */
    /**
     * 返回当前聚合的版本，为null表示该聚合时最近创建的。这个版本号反应聚合发生了什么改变
     * <p/>
     * 聚合的没次修改和保存到仓库，都会更新一个版本号，该版本号可以作为一个乐观锁的策略来发现当前的修改冲突
     * <p/>
     * 一般来说，将之前时间提交的序列化作为聚合的版本号
     * @return
     */
    Long getVersion();
}
