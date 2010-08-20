/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
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
 * ====================================================================
 */

package org.jclouds.vcloud.compute.suppliers;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.jclouds.concurrent.FutureIterables.transformParallel;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.Constants;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.logging.Logger;
import org.jclouds.vcloud.compute.functions.ImagesInOrganization;
import org.jclouds.vcloud.domain.Organization;

import com.google.common.base.Function;
import com.google.common.base.Supplier;

/**
 * @author Adrian Cole
 */
@Singleton
public class VCloudExpressImageSupplier implements Supplier<Set<? extends Image>> {

   @Resource
   @Named(ComputeServiceConstants.COMPUTE_LOGGER)
   public Logger logger = Logger.NULL;

   private final Supplier<Map<String, ? extends Organization>> orgMap;
   private final ImagesInOrganization imagesInOrganization;
   private final ExecutorService executor;

   @Inject
   VCloudExpressImageSupplier(Supplier<Map<String, ? extends Organization>> orgMap, ImagesInOrganization imagesInOrganization,
            @Named(Constants.PROPERTY_USER_THREADS) ExecutorService executor) {
      this.orgMap = checkNotNull(orgMap, "orgMap");
      this.imagesInOrganization = checkNotNull(imagesInOrganization, "imagesInOrganization");
      this.executor = checkNotNull(executor, "executor");
   }

   @Override
   public Set<? extends Image> get() {
      Iterable<? extends Organization> orgs = checkNotNull(orgMap.get().values(), "orgs");
      Iterable<Iterable<? extends Image>> images = transformParallel(orgs,
               new Function<Organization, Future<Iterable<? extends Image>>>() {

                  @Override
                  public Future<Iterable<? extends Image>> apply(final Organization from) {
                     checkNotNull(from, "org");
                     return executor.submit(new Callable<Iterable<? extends Image>>() {

                        @Override
                        public Iterable<? extends Image> call() throws Exception {
                           return imagesInOrganization.apply(from);
                        }

                     });
                  }

               }, executor, null, logger, "images in " + orgs);
      return newLinkedHashSet(concat(images));
   }
}