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

/**
 * The <code>UnitOfWorkFactory</code> interface is used to obtain UnitOfWork instances to manage activity in command
 * handling processes.
 * <p/>
 * All UnitOfWork instances returned by this factory have been started. It is the responsibility of the caller to either
 * invoke commit or rollback on these instances when done.
 *
 * @author Allard Buijze
 * @since 0.7
 */
/**
 * UnitOfWorkFactory接口用于获取UnitOfWork实例以管理命令处理过程中的活动。
 * <p/>
 * 此工厂返回的所有UnitOfWork实例已启动。 完成后，调用者有责任在这些实例上调用提交或回滚。
 * @author Administrator
 *
 */
public interface UnitOfWorkFactory {

    /**
     * Creates a new UnitOfWork instance. The instance's {@link UnitOfWork#isStarted()} method returns
     * <code>true</code>.
     *
     * @return a new UnitOfWork instance, which has been started.
     */
    /**
     * 返回一个UnitOfWork实例，该实例的中的方法{@link UnitOfWork#isStarted()}返回true
     * @return 返回一个已启动的工作单元实例。
     */
    UnitOfWork createUnitOfWork();

}
