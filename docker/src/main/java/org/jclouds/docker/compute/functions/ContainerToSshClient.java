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
import com.google.common.net.HostAndPort;
import com.google.inject.Inject;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.docker.DockerApi;
import org.jclouds.docker.domain.Container;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.logging.Logger;
import org.jclouds.ssh.SshClient;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.ssh.SshClient.Factory;

@Singleton
public class ContainerToSshClient implements Function<Container, SshClient> {

   @Resource
   @Named(ComputeServiceConstants.COMPUTE_LOGGER)
   protected Logger logger = Logger.NULL;

   private final DockerApi api;
   private final Factory sshClientFactory;

   @Inject
   public ContainerToSshClient(DockerApi api, Factory sshClientFactory) {
      this.api = checkNotNull(api, "api");
      this.sshClientFactory = sshClientFactory;
   }

   @Override
   public SshClient apply(Container container) {
      container = api.getRemoteApi().inspectContainer(container.getId());

      String guestOsUser = "root";
      String guestOsPassword = "password";
      LoginCredentials loginCredentials = LoginCredentials.builder()
              .user(guestOsUser)
              .password(guestOsPassword)
              .authenticateSudo(true).build();

      Map<String,List<Map<String,String>>> portBindings = container.getHostConfig().getPortBindings();
      int sshPort = Integer.parseInt(portBindings.get("22/tcp").get(0).get("HostPort"));

      String clientIpAddress = "192.168.42.43";

      return sshClientFactory.create(HostAndPort.fromParts(clientIpAddress, sshPort), loginCredentials);
   }

}
