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

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.google.common.net.HostAndPort;
import org.jclouds.compute.callables.RunScriptOnNode;
import org.jclouds.compute.domain.ExecResponse;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeMetadataBuilder;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.docker.DockerApiMetadata;
import org.jclouds.docker.domain.Container;
import org.jclouds.docker.domain.Port;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LocationScope;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.location.Provider;
import org.jclouds.logging.Logger;
import org.jclouds.ssh.SshClient;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.getOnlyElement;
import static org.jclouds.compute.util.ComputeServiceUtils.parseOsFamilyOrUnrecognized;
import static org.jclouds.compute.util.ComputeServiceUtils.parseVersionOrReturnEmptyString;
import static org.jclouds.ssh.SshClient.*;

/**
 * @author Andrea Turli
 */
public class ContainerToNodeMetadata implements Function<Container, NodeMetadata> {

   @Resource
   @Named(ComputeServiceConstants.COMPUTE_LOGGER)
   protected Logger logger = Logger.NULL;

   private final Function<Container, SshClient> sshClientForContainer;

   @Inject
   public ContainerToNodeMetadata(Function<Container, SshClient> sshClientForContainer) {
      this.sshClientForContainer = sshClientForContainer;
   }

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
      // TODO setup hardware and hostname properly
      //nodeMetadataBuilder.hostname(container.getNetworkSettings().getIpAddress());
      //nodeMetadataBuilder.hardware()
      if(container.getStatus() != null) {
         nodeMetadataBuilder.status(container.getStatus().contains("Up") ? NodeMetadata.Status.RUNNING : NodeMetadata.Status.SUSPENDED);
      } else {
         nodeMetadataBuilder.status(container.getState().isRunning() ? NodeMetadata.Status.RUNNING : NodeMetadata.Status.SUSPENDED);
      }
      nodeMetadataBuilder.imageId(container.getImage());
      List<String> publicIpAddresses = Lists.newArrayList();
      publicIpAddresses.add("192.168.42.43"); // TODO docker host
      nodeMetadataBuilder.loginPort(getLoginPort(container));
      nodeMetadataBuilder.publicAddresses(publicIpAddresses);
      nodeMetadataBuilder.privateAddresses(publicIpAddresses);

      SshClient client = sshClientForContainer.apply(container);
      try {
         client.connect();
         URL url = Resources.getResource("os-details.sh");
         String script = Resources.toString(url, Charsets.UTF_8);
         ExecResponse osResponse = client.exec(script);
         Map<String, String> osInfo = Splitter.on(";").trimResults().withKeyValueSeparator(":").split(osResponse.getOutput());

         OsFamily family = parseOsFamilyOrUnrecognized(osInfo.get("os"));
         OperatingSystem os = OperatingSystem.builder().description(osResponse.getOutput().trim())
                                                       .family(family)
                                                       .version(osInfo.get("version"))
                                                       .is64Bit(osInfo.get("arch").equals("64"))
                                                       .build();
         nodeMetadataBuilder.operatingSystem(os);
      } catch (IOException e) {
         throw Throwables.propagate(e);
      } finally {
         if (client != null)
            client.disconnect();
      }

      String guestOsUser = "root";
      String guestOsPassword = "password";
      LoginCredentials creds = LoginCredentials.builder()
              .user(guestOsUser)
              .password(guestOsPassword)
              .authenticateSudo(true).build();
      nodeMetadataBuilder.credentials(creds);
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
