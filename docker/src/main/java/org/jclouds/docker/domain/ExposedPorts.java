/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.docker.domain;

import java.util.Set;

/**
 * @author Andrea Turli
 */
public class ExposedPorts {

   private String portAndProtocol;
   private Set<String> hostPorts;

   public ExposedPorts(String portAndProtocol, Set<String> hostPorts) {
      this.portAndProtocol = portAndProtocol;
      this.hostPorts = hostPorts;
   }

   public String getPortAndProtocol() {
      return portAndProtocol;
   }

   public Set<String> getHostPorts() {
      return hostPorts;
   }

   @Override
   public String toString() {
      return "ExposedPorts{" +
              "portAndProtocol='" + portAndProtocol + '\'' +
              ", hostPorts=" + hostPorts +
              '}';
   }

   public static Builder builder() {
      return new Builder();
   }

   public Builder toBuilder() {
      return builder().fromExposedPorts(this);
   }

   public static final class Builder {

      private String portAndProtocol;
      private Set<String> hostPorts;

      public Builder portAndProtocol(String portAndProtocol) {
         this.portAndProtocol = portAndProtocol;
         return this;
      }

      public Builder hostPorts(Set<String> hostPorts) {
         this.hostPorts = hostPorts;
         return this;
      }

      public ExposedPorts build() {
         return new ExposedPorts(portAndProtocol, hostPorts);
      }

      public Builder fromExposedPorts(ExposedPorts in) {
         return this.portAndProtocol(in.getPortAndProtocol())
                 .hostPorts(in.getHostPorts());
      }
   }
}
