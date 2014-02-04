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
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.ImageBuilder;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.docker.DockerApi;
import org.jclouds.logging.Logger;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.get;

/**
 * @author Andrea Turli
 */
public class ImageToImage implements Function<org.jclouds.docker.domain.Image, org.jclouds.compute.domain.Image> {

   private static final String CENTOS = "centos";
   private static final String UBUNTU = "ubuntu";

   @Resource
   @Named(ComputeServiceConstants.COMPUTE_LOGGER)
   protected Logger logger = Logger.NULL;

   @Override
   public Image apply(org.jclouds.docker.domain.Image from) {
      checkNotNull(from, "image");
      String description = checkNotNull(Iterables.getFirst(from.getRepoTags(), ""), "productItem.description");

      OsFamily osFamily = osFamily().apply(description);
      if (osFamily == OsFamily.UNRECOGNIZED) {
         logger.debug("Cannot determine os family for item: %s", from);
      }

      String osVersion = parseVersion(description);
      if (osVersion == null) {
         logger.debug("Cannot determine os version for item: %s", from);
      }

      OperatingSystem os = OperatingSystem.builder()
              .description(description)
              .family(osFamily)
              .version(osVersion)
              .is64Bit(true) // TODO how to detect it via API?
              .build();

      return new ImageBuilder()
              .ids(from.getId())
              .description(description)
              .operatingSystem(os)
              .status(Image.Status.AVAILABLE)
              .build();
   }

   /**
    * Parses the item description to determine the OSFamily
    *
    * @return the @see OsFamily or OsFamily.UNRECOGNIZED
    */
   public static Function<String, OsFamily> osFamily() {
      return new Function<String, OsFamily>() {
         @Override
         public OsFamily apply(final String description) {
            if (description != null) {
               if (description.contains(CENTOS)) return OsFamily.CENTOS;
               else if (description.contains(UBUNTU)) return OsFamily.UBUNTU;
            }
            return OsFamily.UNRECOGNIZED;
         }
      };
   }

   private static String parseVersion(String description) {
      return get(Splitter.on(":").split(description), 1);
   }

}
