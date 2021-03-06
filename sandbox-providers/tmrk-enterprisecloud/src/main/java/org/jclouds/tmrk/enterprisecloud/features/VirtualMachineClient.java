/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.tmrk.enterprisecloud.features;

import org.jclouds.concurrent.Timeout;
import org.jclouds.tmrk.enterprisecloud.domain.network.AssignedIpAddresses;
import org.jclouds.tmrk.enterprisecloud.domain.vm.VirtualMachine;
import org.jclouds.tmrk.enterprisecloud.domain.vm.VirtualMachines;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Provides synchronous access to VirtualMachine.
 * <p/>
 * 
 * @see VirtualMachineAsyncClient
 * @see <a href=
 *      "http://support.theenterprisecloud.com/kb/default.asp?id=984&Lang=1&SID="
 *      />
 * @author Jason King
 */
@Timeout(duration = 180, timeUnit = TimeUnit.SECONDS)
public interface VirtualMachineClient {

    /**
     * returns information regarding virtual machines defined in a compute pool
     * @param uri the uri of the compute pool
     * @return the virtual machines
     */
   VirtualMachines getVirtualMachines(URI uri);

   /**
    * The Get Virtual Machines by ID call returns information regarding a
    * specified virtual machine defined in an environment.
    * @param uri the id of the virtual machine
    * @return the virtual Machine or null if not found
    */
   VirtualMachine getVirtualMachine(URI uri);

    /**
     * The Get Virtual Machines Assigned IP Addresses call returns information
     * regarding the IP addresses assigned to a specified virtual machine in a compute pool.
     * @param uri the assignedIpAddresses call
     * @return the assigned ip addresses
     */
   AssignedIpAddresses getAssignedIpAddresses(URI uri);

}
