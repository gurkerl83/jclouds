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
package org.jclouds.cloudsigma.compute;

import static org.jclouds.compute.util.ComputeServiceUtils.getCores;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.Set;

import org.jclouds.compute.BaseTemplateBuilderLiveTest;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.OsFamilyVersion64Bit;
import org.jclouds.compute.domain.Template;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;

/**
 * 
 * @author Adrian Cole
 */
@Test(groups = "live", testName = "CloudSigmaZurichTemplateBuilderLiveTest")
public class CloudSigmaZurichTemplateBuilderLiveTest extends BaseTemplateBuilderLiveTest {

   public CloudSigmaZurichTemplateBuilderLiveTest() {
      provider = "cloudsigma-zrh";
   }

   @Override
   protected Predicate<OsFamilyVersion64Bit> defineUnsupportedOperatingSystems() {
      return Predicates.not(new Predicate<OsFamilyVersion64Bit>() {
         @Override
         public boolean apply(OsFamilyVersion64Bit input) {
            switch (input.family) {
            case UBUNTU:
               return (input.version.equals("11.04") || input.version.equals("10.04") && !input.is64Bit)
                     || (input.version.equals("10.10") && input.is64Bit) || input.version.equals("");
            case SOLARIS:
               return !input.is64Bit;
            case DEBIAN:
               return input.is64Bit;
            case CENTOS:
               return input.version.equals("5.5") || input.version.equals("")
                     || (input.version.matches("5.0") && input.is64Bit)
                     || (input.version.matches("6.0") && !input.is64Bit);
            case WINDOWS:
               return input.version.equals("2008 R2") || (input.version.equals("2008") && !input.is64Bit)
                     || input.version.equals("") || (input.version.equals("2003"));
            default:
               return false;
            }
         }

      });
   }

   @Override
   public void testDefaultTemplateBuilder() throws IOException {
      Template defaultTemplate = context.getComputeService().templateBuilder().build();
      assertEquals(defaultTemplate.getImage().getOperatingSystem().getVersion(), "11.04");
      assertEquals(defaultTemplate.getImage().getOperatingSystem().is64Bit(), true);
      assertEquals(defaultTemplate.getImage().getId(), "7fad4fe1-daf3-4cb8-a847-082aae4d8506");
      assertEquals(defaultTemplate.getImage().getOperatingSystem().getFamily(), OsFamily.UBUNTU);
      assertEquals(defaultTemplate.getImage().getDefaultCredentials().identity, "cloudsigma");
      assertEquals(getCores(defaultTemplate.getHardware()), 1.0d);
   }

   @Override
   protected Set<String> getIso3166Codes() {
      return ImmutableSet.<String> of("CH-ZH");
   }
}