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
package org.jclouds.aws.elb;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.jclouds.aws.domain.Region;
import org.jclouds.elb.ELBAsyncClient;
import org.jclouds.rest.RestContextFactory;
import org.testng.annotations.Test;

/**
 * Tests behavior of {@code ELBAsyncClient}
 * 
 * @author Adrian Cole
 */
// NOTE:without testName, this will not call @Before* and fail w/NPE during surefire
@Test(groups = "unit", testName = "aws.ELBAsyncClientTest")
public class ELBAsyncClientTest extends org.jclouds.elb.ELBAsyncClientTest {

   public ELBAsyncClientTest() {
      this.provider = "aws-elb";
   }

   @Override
   protected Properties getProperties() {
      return RestContextFactory.getPropertiesFromResource("/rest.properties");
   }

   public void testAllRegions() throws SecurityException, NoSuchMethodException, IOException {
      Method method = ELBAsyncClient.class.getMethod("describeLoadBalancersInRegion", String.class, String[].class);
      for (String region : Region.DEFAULT_REGIONS) {
         processor.createRequest(method, region);
      }
   }

}
