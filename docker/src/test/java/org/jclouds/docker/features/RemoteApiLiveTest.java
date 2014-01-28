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
package org.jclouds.docker.features;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import org.jclouds.docker.domain.Config;
import org.jclouds.docker.domain.Container;
import org.jclouds.docker.domain.Image;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * @author Andrea Turli
 */
public class RemoteApiLiveTest extends BaseDockerApiLiveTest {

   public static final String DEFAULT_IMAGE = "busybox";
   public static final String CENTOS_IMAGE = "centos";

   @BeforeClass
   private void init() {
      setupProperties();
      InputStream createImageStream = api().createImage(CENTOS_IMAGE);
      consumeStream(createImageStream, false);
   }

   @Test
   public void testVersion() {
      Assert.assertEquals("0.7.5", api().getVersion().getVersion());
   }

   @Test(dependsOnMethods = "testVersion")
   public void testCrudForImage() throws IOException, InterruptedException {
      List<Image> images = api().listImages(true);
      int size = images.size();

      InputStream createImageStream = api().createImage(DEFAULT_IMAGE);
      String createImageStdout = consumeStream(createImageStream, false);

      InputStream deleteImageStream = api().deleteImage(DEFAULT_IMAGE);
      String deleteImageStdout = consumeStream(deleteImageStream, false);

      assertEquals(api().listImages(true).size(), size);
   }

   @Test(dependsOnMethods = "testCrudForImage")
   public void testCrudForContainer() throws IOException, InterruptedException {
      List<Container> containers = api().listContainers(true);
      int size = containers.size();

      Config config = Config.builder()
              .image(CENTOS_IMAGE)
              .cmd(ImmutableList.of("/bin/sh", "-c", "while true; do echo hello world; sleep 1; done"))
              .attachStdout(true)
              .attachStderr(true)
              .volumesFrom("")
              .workingDir("")
              .build();
      Container container = api().createContainer(config);
      assertEquals(api().getContainer(container.getId()), container);

      api().removeContainer(container.getId(), true);
      assertEquals(api().listContainers(true).size(), size);
   }

   public void testStartAndStopContainer() throws IOException, InterruptedException {
      Config config = Config.builder().image(CENTOS_IMAGE)
              //.cmd(ImmutableList.of("/bin/sh", "-c", "while true; do echo hello world; sleep 1; done"))
              .cmd(ImmutableList.of("/bin/sh", "-c", "yum -y install openssh-server openssh-clients; chkconfig sshd on; service sshd start"))
              .attachStdout(true)
              .attachStderr(true)
              .volumesFrom("")
              .workingDir("")
              .build();
      Container container = api().createContainer(config);
      String containerId = container.getId();
      api().startContainer(containerId);
      Assert.assertTrue(api().getContainer(containerId).getState().isRunning());

      Image image = api().commit(containerId, "sshd", "centos + ssh server");
      List<Image> images = api().listImages(true);
      api().stopContainer(containerId);
      Assert.assertFalse(api().getContainer(containerId).getState().isRunning());
      api().removeContainer(containerId, true);
   }

   @AfterClass
   private void cleanUp() {
      List<Container> containers = api().listContainers(true);
      for (Container container : containers) {
         api().stopContainer(container.getId());
         api().removeContainer(container.getId(), true);
      }
   }

   private RemoteApi api() {
      return api.getRemoteApi();
   }

   private String consumeStream(InputStream stream, boolean swallowIOException) {
      String result = null;
      try {
         result = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
         Closeables.close(stream, swallowIOException);
      } catch (IOException e) {
         Assert.fail();
      }
      return result;
   }
}
