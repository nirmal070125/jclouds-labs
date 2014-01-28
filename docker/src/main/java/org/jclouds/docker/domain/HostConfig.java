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

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import org.jclouds.javax.annotation.Nullable;

import java.util.List;

/**
 * @author Andrea Turli
 */
public class HostConfig {

   @SerializedName("ContainerIDFile")
   private String containerIDFile;
   @SerializedName("Binds")
   private String binds;
   @SerializedName("Privileged")
   private boolean privileged;
   @SerializedName("PortBindings")
   private PortBindings portBindings;
   @SerializedName("Links")
   private List<String> links = Lists.newArrayList();
   @SerializedName("PublishAllPorts")
   private boolean publishAllPorts;

   public HostConfig(String containerIDFile, String binds, boolean privileged,
                     PortBindings portBindings, @Nullable List<String> links, boolean publishAllPorts) {
      this.containerIDFile = containerIDFile;
      this.binds = binds;
      this.privileged = privileged;
      this.portBindings = portBindings;
      this.links.addAll(links);
      this.publishAllPorts = publishAllPorts;
   }

   public String getContainerIDFile() {
      return containerIDFile;
   }

   public String getBinds() {
      return binds;
   }

   public boolean isPrivileged() {
      return privileged;
   }

   public PortBindings isPortBindings() {
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
}
