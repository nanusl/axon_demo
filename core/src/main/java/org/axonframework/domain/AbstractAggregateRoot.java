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

package org.axonframework.domain;

import javax.persistence.Basic;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.io.Serializable;

/**
 * Very basic implementation of the AggregateRoot interface. It provides the mechanism to keep track of uncommitted
 * events and maintains a version number based on the number of events generated.
 *
 * @author Allard Buijze
 * @since 0.6
 */
/**
 * AggregateRoot 接口的最基础实现类，他提供保持跟踪非提交事件以及维护一个版本号用于产生一批事件的机制
 * @author Administrator
 *
 */
@MappedSuperclass
public abstract class AbstractAggregateRoot implements AggregateRoot, Serializable {

    private static final long serialVersionUID = 6330592271927197888L;

    @Transient
    private EventContainer eventContainer;

    @Id
    private String id;

    @Basic(optional = true)
    private volatile Long lastEventSequenceNumber;

    @SuppressWarnings({"UnusedDeclaration"})
    @Version
    private volatile Long version;

    /**
     * Initializes the aggregate root using a random aggregate identifier.
     */
    protected AbstractAggregateRoot() {
        this(new UUIDAggregateIdentifier());
    }

    /**
     * Initializes the aggregate root using the provided aggregate identifier.
     *
     * @param identifier the identifier of this aggregate
     */
    protected AbstractAggregateRoot(AggregateIdentifier identifier) {
        if (identifier == null) {
            throw new IllegalArgumentException("Aggregate identifier may not be null.");
        }
        this.id = identifier.asString();
        eventContainer = new EventContainer(identifier);
    }

    /**
     * Registers an event to be published when the aggregate is saved.
     *
     * @param event the event to register
     */
    /**当聚合被保存时，这些事件将会被注册到发布列表中
     */
    protected void registerEvent(DomainEvent event) {
        eventContainer.addEvent(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainEventStream getUncommittedEvents() {
        if (eventContainer == null) {
            return new SimpleDomainEventStream();
        }
        return eventContainer.getEventStream();
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Callers should not expect the exact same instance, nor an instance of the same class as provided in the
     * constructor. When this aggregate has been serialized or persisted using JPA, the identifier returned here is an
     * instance of {@link StringAggregateIdentifier}.
     */
    @Override
    public AggregateIdentifier getIdentifier() {
        return eventContainer.getAggregateIdentifier();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commitEvents() {
        lastEventSequenceNumber = eventContainer.getLastSequenceNumber();
        eventContainer.commit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getUncommittedEventCount() {
        return eventContainer != null ? eventContainer.size() : 0;
    }

    /**
     * Initialize the event stream using the given sequence number of the last known event. This will cause the new
     * events to be attached to this aggregate to be assigned a continuous sequence number.
     *
     * @param lastSequenceNumber The sequence number of the last event from this aggregate
     */
    /**
     * 使用最后一个已知事件的给定序列号初始化事件流。 这将导致将新事件附加到该聚合中以分配连续的序列号
     * @param lastSequenceNumber
     */
    protected void initializeEventStream(long lastSequenceNumber) {
        eventContainer.initializeSequenceNumber(lastSequenceNumber);
        lastEventSequenceNumber = lastSequenceNumber >= 0 ? lastSequenceNumber : null;
    }

    /**
     * Returns the sequence number of the last committed event, or <code>null</code> if no events have been committed
     * before.
     *
     * @return the sequence number of the last committed event
     */
    /**
     * 获取最后已提交的事件的序列号
     * @return
     */
    protected Long getLastCommittedEventSequenceNumber() {
        return eventContainer.getLastCommittedSequenceNumber();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getVersion() {
        return version;
    }

    /**
     * JPA / EJB3 @PostLoad annotated method used to initialize the fields in this class after an instance has been
     * loaded from persistent storage.
     * <p/>
     * Subclasses are responsible for invoking this method if they provide their own {@link @PostLoad} annotated method.
     * Failure to do so will inevitably result in <code>NullPointerException</code>.
     *
     * @see PostLoad
     */
    @PostLoad
    protected void performPostLoadInitialization() {
        eventContainer = new EventContainer(new StringAggregateIdentifier(id));
        eventContainer.initializeSequenceNumber(lastEventSequenceNumber);
    }
}
