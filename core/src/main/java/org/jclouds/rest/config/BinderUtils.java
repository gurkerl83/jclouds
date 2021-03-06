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
package org.jclouds.rest.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import com.google.inject.Binder;
import com.google.inject.Provider;

/**
 * 
 * @author Adrian Cole
 */
public class BinderUtils {

   public static <K, V> void bindClient(Binder binder, Class<K> syncClientType,
            Class<V> asyncClientType, Map<Class<?>, Class<?>> delegates) {
      Provider<K> asyncProvider = new ClientProvider<K, V>(syncClientType, asyncClientType,
               delegates);
      binder.requestInjection(asyncProvider);
      binder.bind(syncClientType).toProvider(asyncProvider);
   }

   public static <T> void bindAsyncClient(Binder binder, Class<T> asyncClientType) {
      Provider<T> asyncProvider = new AsyncClientProvider<T>(asyncClientType);
      binder.requestInjection(asyncProvider);
      binder.bind(asyncClientType).toProvider(asyncProvider);
   }

   @SuppressWarnings("unchecked")
   public static <T> T newNullProxy(Class<T> clazz) {
      return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] { clazz },
               new InvocationHandler() {

                  @Override
                  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                     return null;
                  }

               });
   }
}