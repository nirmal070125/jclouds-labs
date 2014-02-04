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
package org.jclouds.docker.compute.functions;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeMetadataBuilder;
import org.jclouds.docker.domain.Container;
import org.jclouds.docker.domain.Port;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LocationScope;
import org.jclouds.domain.LoginCredentials;

import java.util.List;

import static com.google.common.collect.Iterables.getOnlyElement;

/**
 * @author Andrea Turli
 */
public class ContainerToNodeMetadata implements Function<Container, NodeMetadata> {

   @Override
   public NodeMetadata apply(Container container) {
      // TODO
      String group = "experiment";
      String name = "container";

      NodeMetadataBuilder nodeMetadataBuilder = new NodeMetadataBuilder();
      nodeMetadataBuilder.name(name).ids(container.getId()).group(group);
      // TODO Set up location properly
      LocationBuilder locationBuilder = new LocationBuilder();
      locationBuilder.description("");
      locationBuilder.id("");
      locationBuilder.scope(LocationScope.HOST);
      nodeMetadataBuilder.location(locationBuilder.build());
      //nodeMetadataBuilder.hostname(container.getNetworkSettings().getIpAddress());
      if(container.getStatus() != null) {
         nodeMetadataBuilder.status(container.getStatus().contains("Up") ? NodeMetadata.Status.RUNNING : NodeMetadata.Status.SUSPENDED);
      } else {
         nodeMetadataBuilder.status(container.getState().isRunning() ? NodeMetadata.Status.RUNNING : NodeMetadata.Status.SUSPENDED);
      }
      nodeMetadataBuilder.imageId(container.getImage());
      //nodeMetadataBuilder.hardware()
      List<String> publicIpAddresses = Lists.newArrayList();
      publicIpAddresses.add("172.18.42.1"); // docker host
      nodeMetadataBuilder.loginPort(getLoginPort(container));
      nodeMetadataBuilder.publicAddresses(publicIpAddresses);
      nodeMetadataBuilder.privateAddresses(publicIpAddresses);

      /* TODO OS
      IGuestOSType guestOSType = virtualboxManager.get().getVBox().getGuestOSType(vm.getOSTypeId());
      OsFamily family = parseOsFamilyOrUnrecognized(guestOSType.getDescription());
      String version = parseVersionOrReturnEmptyString(family, guestOSType.getDescription(), osVersionMap);
      OperatingSystem os = OperatingSystem.builder().description(guestOSType.getDescription()).family(family)
              .version(version).is64Bit(guestOSType.getIs64Bit()).build();
      nodeMetadataBuilder.operatingSystem(os);
      */
      String guestOsUser = "root"; //vm.getExtraData(GUEST_OS_USER);
      String guestOsPassword = "password"; //vm.getExtraData(GUEST_OS_PASSWORD);
      nodeMetadataBuilder.credentials(LoginCredentials.builder()
              .user(guestOsUser)
              .password(guestOsPassword)
              .authenticateSudo(true).build());
      return nodeMetadataBuilder.build();
   }

   private int getLoginPort(Container container) {
      if (container.getNetworkSettings() != null) {
         return Integer.parseInt(getOnlyElement(container.getNetworkSettings().getPorts().get("22/tcp")).get("HostPort"));
      } else if (container.getPorts() != null) {
         for (Port port : container.getPorts()) {
            if (port.getPrivatePort() == 22) {
               return port.getPublicPort();
            }
         }
      }
      throw new IllegalStateException("Cannot determine the login port for " + container.getId());
   }
}
