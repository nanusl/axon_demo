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

package org.axonframework.commandhandling;

import org.axonframework.unitofwork.UnitOfWork;

/**
 * Marks an instance that is capable of handling commands. CommandHandlers need to be subscribed to a {@link CommandBus}
 * in order to receive command of the specified type <code>T</code>.
 * 标记一个实例可以处理命令，并需要将自己订阅到命令总线上，以便于自己接收命令
 * @author Allard Buijze
 * @param <T> The type of command this handler can handle 命令的一种类型， 声明后该命令处理器值处理该给定的命令
 * @since 0.5
 */
public interface CommandHandler<T> {

    /**
     * Handles the given <code>command</code>.
     * 处理给定的命令
     * @param command    The command to process.
     * @param unitOfWork The UnitOfWork the command is processed in
     * @return The result of the command processing, if any.
     *
     * @throws Throwable any exception that occurs during command handling
     */
    Object handle(T command, UnitOfWork unitOfWork) throws Throwable;

}
