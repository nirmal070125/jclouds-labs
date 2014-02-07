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

import com.google.gson.annotations.SerializedName;
import org.jclouds.javax.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @author Andrea Turli
 */
public class HostConfig {

   @SerializedName("ContainerIDFile")
   private String containerIDFile;
   @SerializedName("Binds")
   private List<String> binds;
   @SerializedName("Privileged")
   private boolean privileged;
   @SerializedName("PortBindings")
   private Map<String, List<Map<String, String>>> portBindings;
   @SerializedName("Links")
   private List<String> links;
   @SerializedName("PublishAllPorts")
   private boolean publishAllPorts;

   public HostConfig(String containerIDFile, List<String> binds, boolean privileged,
                     Map<String, List<Map<String, String>>> portBindings, @Nullable List<String> links,
                     boolean publishAllPorts) {
      this.containerIDFile = containerIDFile;
      this.binds = binds;
      this.privileged = privileged;
      this.portBindings = portBindings;
      this.links = links;
      this.publishAllPorts = publishAllPorts;
   }

   public String getContainerIDFile() {
      return containerIDFile;
   }

   public List<String> getBinds() {
      return binds;
   }

   public boolean isPrivileged() {
      return privileged;
   }

   public Map<String, List<Map<String, String>>> getPortBindings() {
      return portBindings;
   }

   @Nullable
   public List<String> getLinks() {
      return links;
   }

   public boolean isPublishAllPorts() {
      return publishAllPorts;
   }

   @Override
   public String toString() {
      return "HostConfig{" +
              "containerIDFile='" + containerIDFile + '\'' +
              ", binds='" + binds + '\'' +
              ", privileged=" + privileged +
              ", portBindings=" + portBindings +
              ", links=" + links +
              ", publishAllPorts=" + publishAllPorts +
              '}';
   }

   public static Builder builder() {
      return new Builder();
   }

   public Builder toBuilder() {
      return builder().fromHostConfig(this);
   }

   public static final class Builder {

      private String containerIDFile;
      private List<String> binds;
      private boolean privileged;
      private Map<String, List<Map<String, String>>> portBindings;
      private List<String> links;
      private boolean publishAllPorts;

      public Builder containerIDFile(String containerIDFile) {
         this.containerIDFile = containerIDFile;
         return this;
      }

      public Builder binds(List<String> binds) {
         this.binds = binds;
         return this;
      }

      public Builder privileged(boolean privileged) {
         this.privileged = privileged;
         return this;
      }

      public Builder links(List<String> links) {
         this.links = links;
         return this;
      }

      public Builder portBindings(Map<String, List<Map<String, String>>> portBindings) {
         this.portBindings = portBindings;
         return this;
      }

      public Builder publishAllPorts(boolean publishAllPorts) {
         this.publishAllPorts = publishAllPorts;
         return this;
      }

      public HostConfig build() {
         return new HostConfig(containerIDFile, binds, privileged, portBindings, links, publishAllPorts);
      }

      public Builder fromHostConfig(HostConfig in) {
         return this
                 .containerIDFile(in.getContainerIDFile())
                 .binds(in.getBinds())
                 .privileged(in.isPrivileged())
                 .links(in.getLinks())
                 .portBindings(in.getPortBindings())
                 .publishAllPorts(in.isPublishAllPorts());
      }
   }
}
