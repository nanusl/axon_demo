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

package org.axonframework.eventsourcing;

import org.axonframework.domain.DomainEvent;
import org.axonframework.domain.StubDomainEvent;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * @author Allard Buijze
 */
@Entity
public class JpaEventSourcedAggregate extends AbstractEventSourcedAggregateRoot {

    @Basic
    private long counter;

    public void increaseCounter() {
        apply(new StubDomainEvent());
    }

    @Override
    protected void handle(DomainEvent event) {
        counter++;
    }
}
