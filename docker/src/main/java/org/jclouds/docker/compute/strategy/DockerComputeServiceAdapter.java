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
package org.jclouds.docker.compute.strategy;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.jclouds.compute.ComputeServiceAdapter;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.HardwareBuilder;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.docker.DockerApi;
import org.jclouds.docker.compute.features.internal.Archives;
import org.jclouds.docker.domain.Config;
import org.jclouds.docker.domain.Container;
import org.jclouds.docker.domain.HostConfig;
import org.jclouds.docker.domain.Image;
import org.jclouds.domain.Location;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.io.Payload;
import org.jclouds.io.Payloads;
import org.jclouds.logging.Logger;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.contains;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.find;

/**
 * defines the connection between the {@link org.jclouds.docker.DockerApi} implementation and
 * the jclouds {@link org.jclouds.compute.ComputeService}
 */
@Singleton
public class DockerComputeServiceAdapter implements
        ComputeServiceAdapter<Container, Hardware, Image, Location> {

   @Resource
   @Named(ComputeServiceConstants.COMPUTE_LOGGER)
   protected Logger logger = Logger.NULL;

   private final DockerApi api;

   @Inject
   public DockerComputeServiceAdapter(DockerApi api) {
      this.api = checkNotNull(api, "api");
   }

   @Override
   public NodeAndInitialCredentials<Container> createNodeWithGroupEncodedIntoName(String group, String name,
                                                                                  Template template) {
      checkNotNull(template, "template was null");
      checkNotNull(template.getOptions(), "template options was null");

      String imageId = checkNotNull(template.getImage().getId(), "template image id must not be null");

      Map<String, Object> exposedPorts = Maps.newHashMap();
      int[] inboundPorts = template.getOptions().getInboundPorts();
      for (int inboundPort : inboundPorts) {
         exposedPorts.put(inboundPort + "/tcp", Maps.newHashMap());
      }
      Config config = Config.builder()
              .image(imageId)
              .cmd(ImmutableList.of("/usr/sbin/sshd", "-D"))
              .attachStdout(true)
              .attachStderr(true)
              .volumesFrom("")
              .workingDir("")
              .exposedPorts(exposedPorts)
              .build();

      logger.debug(">> creating new container (%s)", "newContainer");
      Container container = api.getRemoteApi().createContainer(config);
      logger.trace("<< container(%s)", container.getId());

      Map<String, List<Map<String, String>>> portBindings = Maps.newHashMap();
      for (int inboundPort : inboundPorts) {
         if (inboundPort != 22) {
            Map<String, String> map = Maps.newHashMap();
            map.put("HostPort", inboundPort + "");
            portBindings.put(inboundPort + "/tcp", ImmutableList.of(map));
         }
      }

      HostConfig hostConfig = HostConfig.builder()
              .portBindings(portBindings)
              .publishAllPorts(true)
              .privileged(true)
              .build();
      api.getRemoteApi().startContainer(container.getId(), hostConfig);
      container = api.getRemoteApi().inspectContainer(container.getId());
      return new NodeAndInitialCredentials<Container>(container, container.getId() + "",
              LoginCredentials.builder().user("root").password("password").build());
   }

   @Override
   public Iterable<Hardware> listHardwareProfiles() {
      Set<Hardware> hardware = Sets.newLinkedHashSet();
      hardware.add(new HardwareBuilder().ids("t1.micro").hypervisor("lxc").name("t1.micro").ram(512).build());
      hardware.add(new HardwareBuilder().ids("m1.small").hypervisor("lxc").name("m1.small").ram(1024).build());
      hardware.add(new HardwareBuilder().ids("m1.medium").hypervisor("lxc").name("m1.medium").ram(3840).build());
      hardware.add(new HardwareBuilder().ids("m1.large").hypervisor("lxc").name("m1.large").ram(8192).build());
      return hardware;
   }

   @Override
   public Set<Image> listImages() {
      Set<Image> images = api.getRemoteApi().listImages(true);

      buildImageIfNotAvailable(images, "jclouds/centos", "centos");
      buildImageIfNotAvailable(images, "jclouds/ubuntu", "ubuntu");

      List<Container> containers = api.getRemoteApi().listContainers(true);
      // removing the left over containers created during build image command
      for (Container container : containers) {
         if (container.getPorts() == null || container.getPorts().isEmpty()) { // ugly hack
            api.getRemoteApi().stopContainer(container.getId());
            api.getRemoteApi().removeContainer(container.getId(), true);
         }
      }
      return api.getRemoteApi().listImages(true);
   }

   private void buildImageIfNotAvailable(Set<Image> images, final String imageName, String folderName) {
      boolean available = false;
      for (Image image : images) {
         if(image.getRepoTags().get(0).startsWith(imageName)) {
            available = true;
         }
      }
      if(!available) {
         try {
            api.getRemoteApi().build(imageName, false, true, new File(Resources.getResource(folderName + "/Dockerfile").toURI()));
         } catch (URISyntaxException e) {
            throw Throwables.propagate(e);
         }
      }
   }

   @Override
   public Image getImage(final String id) {
      return find(listImages(), new Predicate<Image>() {

         @Override
         public boolean apply(Image input) {
            return input.getId().equals(id);
         }

      }, null);
   }

   @Override
   public Iterable<Container> listNodes() {
      return api.getRemoteApi().listContainers(true);
   }

   @Override
   public Iterable<Container> listNodesByIds(final Iterable<String> ids) {
      return filter(listNodes(), new Predicate<Container>() {

         @Override
         public boolean apply(Container server) {
            return contains(ids, server.getId());
         }
      });
   }

   @Override
   public Iterable<Location> listLocations() {
      return ImmutableSet.of();
   }

   @Override
   public Container getNode(String id) {
      return api.getRemoteApi().inspectContainer(id);
   }

   @Override
   public void destroyNode(String id) {
      api.getRemoteApi().stopContainer(id);
      api.getRemoteApi().removeContainer(id, true);
   }

   @Override
   public void rebootNode(String id) {
      api.getRemoteApi().startContainer(id);
   }

   @Override
   public void resumeNode(String id) {
      api.getRemoteApi().startContainer(id);
   }

   @Override
   public void suspendNode(String id) {
      api.getRemoteApi().stopContainer(id);
   }

}
