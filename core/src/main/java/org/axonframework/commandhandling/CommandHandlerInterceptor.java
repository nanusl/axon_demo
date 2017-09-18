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
 * Workflow interface that allows for customized command handler invocation chains. A CommandHandlerInterceptor can add
 * customized behavior to command handler invocations, both before and after the invocation.
 * <p/>
 * 工作流接口，允许自定义命令处理器执行链，一个命令拦截器可以添加自定义行为的命令处理执行链到 命令拦截器之前或之后
 * @author Allard Buijze
 * @since 0.5
 */
public interface CommandHandlerInterceptor {

    /**
     * The handle method is invoked each time a command is dispatched through the event bus that the
     * CommandHandlerInterceptor is declared on. The incoming command and contextual information can be found in the
     * given <code>commandContext</code>.
     * <p/>
     * The interceptor is responsible for the continuation of the dispatch process by invoking the {@link
     * org.axonframework.commandhandling.InterceptorChain#proceed(Object)} method on the given
     * <code>interceptorChain</code>.
     * <p/>
     * Any information gathered by interceptors may be attached to the command context. This information is made
     * available to the CommandCallback provided by the dispatching component.
     * <p/>
     * Interceptors are highly recommended not to change the type of the command handling result, as the dispatching
     * component might expect a result of a specific type.
     *
     * @param command          The command being dispatched
     * @param unitOfWork       The UnitOfWork in which
     * @param interceptorChain The interceptor chain that allows this interceptor to proceed the dispatch process
     * @return the result of the command handler. May have been modified by interceptors.
     *
     * @throws Throwable any exception that occurs while handling the command
     */
    /**
     * 当一个命令通过一条已被CommandHandlerInterceptor声明的命令总线分发时，该方法将被执行，命令已及命令的上下文信息可以在unitOfWork中找到
     * <p/>
     * 拦截器主要是通过{@link InterceptorChain}上的方法process来执行的
     * <p/>
     * 拦截器收集到的所有信息，都可能附加到unitOfWork，这些信息对于CommandCallback可能会有用
     * <p/>
     * 拦截器建议不要改变命令处理的结果，因为分发组件可能期望得到一个确定类型的结果
     * @param command 将被分发的命令
     * @param unitOfWork	工作单元
     * @param interceptorChain	拦截链，允许拦截器处理分发进程
     * @return
     */
    Object handle(Object command, UnitOfWork unitOfWork, InterceptorChain interceptorChain) throws Throwable;

}
