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

package org.axonframework.eventsourcing;

import org.axonframework.domain.DomainEvent;
import org.axonframework.util.ReflectionUtils;

import java.util.Collection;

/**
 * Base class for Event Sourced entities that are not at the root of the aggregate. Instead of keeping track of
 * uncommitted events themselves, these entities refer to their aggregate root to do that for them. A DomainEvent
 * published in any of the entities in an Aggregate is published to all entities in the entire aggregate.
 *
 * @author Allard Buijze
 * @since 0.7
 */
/*
 * 事件源的基础实现类但不能作为聚合根存在，为了跟踪自身未提交的事件，这些实体都关联到一个聚合根上，一个领域事件的发布 都是整合到聚合中的所有需要发布的事件中
 */
public abstract class AbstractEventSourcedEntity {

    private volatile AbstractEventSourcedAggregateRoot aggregateRoot;

    /**
     * Returns a list of event sourced entities directly referenced by this entity.
     * <p/>
     * The default implementation uses reflection to find references to <code>AbstractEventSourcedEntity</code>
     * implementations.
     * <p/>
     * It will look for them in the following locations: <ul><li> directly referenced in a field;<li> inside fields
     * containing an {@link Iterable};<li>inside both they keys and the values of fields containing a {@link
     * java.util.Map}</ul>
     *
     * @return a list of event sourced entities contained in this aggregate
     */
    protected Collection<AbstractEventSourcedEntity> getChildEntities() {
        return ReflectionUtils.findFieldValuesOfType(this, AbstractEventSourcedEntity.class);
    }

    /**
     * Register the aggregate root with this entity. The entity uses this aggregate root to report applied Domain
     * Events. The aggregate root is responsible for tracking all applied events.
     * <p/>
     * A parent entity is responsible for invoking this method on its child entities prior to propagating events to it.
     * Typically, this means all entities have their aggregate root set before any actions are taken on it.
     *
     * @param aggregateRootToRegister the root of the aggregate this entity is part of.
     */
    /**
     * 使用该实体注册聚合根。 实体必须使用此聚合根来应用领域事件。 聚合根负责跟踪所有应用的事件。
     * <p/>
     * 父实体负责在传播事件之前对其子实体调用此方法。
     * 通常，这意味着在对其执行任何操作之前，所有实体都必须设置其聚合根集
     * @param aggregateRootToRegister
     */
    protected void registerAggregateRoot(AbstractEventSourcedAggregateRoot aggregateRootToRegister) {
        if (this.aggregateRoot != null && this.aggregateRoot != aggregateRootToRegister) {
            throw new IllegalStateException("Cannot register new aggregate. "
                                                    + "This entity is already part of another aggregate");
        }
        this.aggregateRoot = aggregateRootToRegister;
    }

    /**
     * Report the given <code>event</code> for handling in the current instance (<code>this</code>), as well as all the
     * entities referenced by this instance.
     *
     * @param event The event to handle
     */
    /**
     * 向当前实例以及引用该实例的所有的实例报告传入的事件消息
     * @param event
     */
    void handleRecursively(DomainEvent event) {
        handle(event);
        for (AbstractEventSourcedEntity entity : getChildEntities()) {
            entity.registerAggregateRoot(aggregateRoot);
            entity.handleRecursively(event);
        }
    }

    /**
     * Apply state changes based on the given event.
     * <p/>
     * Note: Implementations of this method should *not* perform validation.
     *
     * @param event The event to handle
     */
    /**
     * 根据传入的事件改变实体的状态。
     * <p/>
     * 注意: 该方法的实现可能没有做校验
     * @param event
     */
    protected abstract void handle(DomainEvent event);

    /**
     * Apply the provided event. Applying events means they are added to the uncommitted event queue and forwarded to
     * the {@link #handle(DomainEvent)} event handler method} for processing.
     * <p/>
     * Note that all entities part of the aggregate that this entity is part of are notified of the event.
     *
     * @param event The event to apply
     */
    /**
     * 应用提供的事件，意味着将事件添加到非提交事件列表，并交给事件处理器处理
     * @param event
     */
    protected void apply(DomainEvent event) {
        aggregateRoot.apply(event);
    }
}
