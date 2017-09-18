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

package org.axonframework.eventsourcing;

import org.axonframework.domain.AbstractAggregateRoot;
import org.axonframework.domain.AggregateDeletedEvent;
import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.domain.DomainEvent;
import org.axonframework.domain.DomainEventStream;
import org.axonframework.util.Assert;
import org.axonframework.util.ReflectionUtils;

import javax.persistence.MappedSuperclass;
import java.util.Collection;

/**
 * Abstract convenience class to be extended by all aggregate roots. The AbstractEventSourcedAggregateRoot tracks all
 * uncommitted events. It also provides convenience methods to initialize the state of the aggregate root based on a
 * {@link org.axonframework.domain.DomainEventStream}, which can be used for event sourcing.
 *
 * @author Allard Buijze
 * @since 0.1
 */
/**
 * 抽象方便班由所有聚集根延伸。 AbstractEventSourcedAggregateRoot跟踪所有未提交的事件。 它还提供了方便的方法来初始化基于{@link DomainEventStream}的聚合根的状态，可用于事件朔源。
 */
@MappedSuperclass
public abstract class AbstractEventSourcedAggregateRoot extends AbstractAggregateRoot
        implements EventSourcedAggregateRoot {

    private static final long serialVersionUID = 5868786029296883724L;

    /**
     * Initializes the aggregate root using a random aggregate identifier.
     */
    protected AbstractEventSourcedAggregateRoot() {
        super();
    }

    /**
     * Initializes the aggregate root using the provided aggregate identifier.
     *
     * @param identifier the identifier of this aggregate
     */
    protected AbstractEventSourcedAggregateRoot(AggregateIdentifier identifier) {
        super(identifier);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This implementation is aware of two special types of <code>DomainEvents</code>: The
     * <code>AggregateDeletedEvent</code>, which indicates that the aggregate is deleted and the
     * <code>AggregateSnapshot</code>, which is a snapshot event, containing the actual aggregate inside.
     * <p/>
     * When an <code>AggregateDeletedEvent</code> is encountered, a <code>AggregateDeletedException</code> is thrown,
     * unless there are events following the <code>AggregateDeletedEvent</code>. This could be the case when an event is
     * added to the stream as a correction to an earlier event.
     * <p/>
     * <code>AggregateSnapshot</code> events are used to initialize the aggregate with the correct version ({@link
     * #getVersion()}).
     *
     * @throws IllegalStateException     if this aggregate was already initialized.
     * @throws AggregateDeletedException if the event stream contains an event of type {@link AggregateDeletedEvent} (or
     *                                   subtype).
     */
    @Override
    public void initializeState(DomainEventStream domainEventStream) {
        Assert.state(getUncommittedEventCount() == 0, "Aggregate is already initialized");
        long lastSequenceNumber = -1;
        while (domainEventStream.hasNext()) {
            DomainEvent event = domainEventStream.next();
            if (event instanceof AggregateDeletedEvent) {
                throw new AggregateDeletedException(String.format(
                        "Aggregate with identifier [%s] not found. It has been deleted.",
                        event.getAggregateIdentifier()));
            }
            lastSequenceNumber = event.getSequenceNumber();
            if (!(event instanceof AggregateSnapshot)) {
                handleRecursively(event);
            }
        }
        initializeEventStream(lastSequenceNumber);
    }

    /**
     * Apply the provided event. Applying events means they are added to the uncommitted event queue and forwarded to
     * the {@link #handle(DomainEvent)} event handler method} for processing.
     * <p/>
     * The event is applied on all entities part of this aggregate.
     *
     * @param event The event to apply
     */
    /**
     * 通过该方法提供事件，该事件添加到未提交事件队列，并且驱使{@link #handle(org.axonframework.domain.DomainEventMessage)} 事件处理器} 来处理
     * <p/>
     * 该事件应用于此聚合的所有实体部分。
     * @param event
     */
    protected void apply(DomainEvent event) {
        registerEvent(event);
        handleRecursively(event);
    }

    private void handleRecursively(DomainEvent event) {
        handle(event);
        for (AbstractEventSourcedEntity entity : getChildEntities()) {
            entity.registerAggregateRoot(this);
            entity.handleRecursively(event);
        }
    }

    /**
     * Returns a list of event sourced entities directly referenced by the aggregate root.
     * <p/>
     * The default implementation uses reflection to find references to {@link AbstractEventSourcedEntity}
     * implementations.
     * <p/>
     * It will look for entities: <ul><li> directly referenced in a field;<li> inside fields containing an {@link
     * Iterable};<li>inside both they keys and the values of fields containing a {@link java.util.Map}</ul>
     *
     * @return a list of event sourced entities contained in this aggregate
     */
    /**
     * 返回由该实体直接引用的事件源实体的集合。 可以返回null或一个空列表，以表示没有子实体可用。 集合也可能包含空值。
     * <p/>
     * 事件按照返回值的迭代器提供的顺序传播给孩子。
     * @return
     */
    protected Collection<AbstractEventSourcedEntity> getChildEntities() {
        return ReflectionUtils.findFieldValuesOfType(this, AbstractEventSourcedEntity.class);
    }

    /**
     * Apply state changes based on the given event.
     * <p/>
     * Note: Implementations of this method should *not* perform validation.
     *
     * @param event The event to handle
     */
    /**
     * 通过给定事件改变聚合状态
     * @param event
     */
    protected abstract void handle(DomainEvent event);

    @Override
    public Long getVersion() {
        return getLastCommittedEventSequenceNumber();
    }
}
