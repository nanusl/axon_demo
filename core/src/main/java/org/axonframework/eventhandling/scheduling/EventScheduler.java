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

package org.axonframework.eventhandling.scheduling;

import org.axonframework.domain.ApplicationEvent;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * Interface towards a mechanism capable of scheduling the publication of events. The accuracy of the publication time
 * depends on the exact implementation used.
 *
 * @author Allard Buijze
 * @since 0.7
 */
public interface EventScheduler {

    /**
     * Schedule the given <code>event</code> for publication at the given <code>triggerDateTime</code>. The returned
     * ScheduleToken can be used to cancel the planned publication.
     *
     * @param triggerDateTime The moment to trigger publication of the event
     * @param event           The event to publish
     * @return the token to use when cancelling the schedule
     */
    ScheduleToken schedule(DateTime triggerDateTime, ApplicationEvent event);

    /**
     * Schedule the given <code>event</code> for publication after the given <code>triggerDuration</code>.  The returned
     * ScheduleToken can be used to cancel the planned publication.
     *
     * @param triggerDuration The amount of time to wait before publishing the event
     * @param event           The event to publish
     * @return the token to use when cancelling the schedule
     */
    ScheduleToken schedule(Duration triggerDuration, ApplicationEvent event);

    /**
     * Schedules the given <code>event</code> for publication. The event contains the time at which it should be
     * published. The returned ScheduleToken can be used to cancel the planned publication.
     *
     * @param event The event to publish
     * @return the token to use when cancelling the schedule
     */
    ScheduleToken schedule(ScheduledEvent event);

    /**
     * Cancel the publication of a scheduled event. If the events has already been published, this method does nothing.
     *
     * @param scheduleToken the token returned when the event was scheduled
     * @throws IllegalArgumentException if the token belongs to another scheduler
     */
    void cancelSchedule(ScheduleToken scheduleToken);

}
