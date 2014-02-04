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
package org.jclouds.docker.compute.features;

import org.jclouds.Fallbacks;
import org.jclouds.docker.binders.BindInputStreamToRequest;
import org.jclouds.docker.domain.Config;
import org.jclouds.docker.domain.Container;
import org.jclouds.docker.domain.HostConfig;
import org.jclouds.docker.domain.Image;
import org.jclouds.docker.domain.Version;
import org.jclouds.io.Payload;
import org.jclouds.io.payloads.InputStreamPayload;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.Headers;
import org.jclouds.rest.annotations.PayloadParam;
import org.jclouds.rest.binders.BindToJsonPayload;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Andrea Turli
 */
public interface RemoteApi extends Closeable {

   /**
    * Get the information of the current docker version.
    *
    * @return The information of the current docker version.
    */
   @Named("version")
   @GET
   @Path("/version")
   @Consumes(MediaType.APPLICATION_JSON)
   Version getVersion();

   /**
    * List containers
    *
    * @return the running containers.
    */
   @Named("containers:list")
   @GET
   @Path("/containers/json")
   @Consumes(MediaType.APPLICATION_JSON)
   @Fallback(Fallbacks.EmptySetOnNotFoundOr404.class)
   List<Container> listContainers(
           @QueryParam("all") boolean all,
           @QueryParam("limit") String limit,
           @QueryParam("since") String since,
           @QueryParam("before") String before);

   /**
    * List containers
    *
    * @return the running containers.
    */
   @Named("containers:list")
   @GET
   @Path("/containers/json")
   @Consumes(MediaType.APPLICATION_JSON)
   @Fallback(Fallbacks.EmptySetOnNotFoundOr404.class)
   List<Container> listContainers(
           @QueryParam("all") boolean all);

   /**
    * Create a container.
    *
    * @return the container created.
    */
   @Named("container:create")
   @POST
   @Path("/containers/create")
   @Consumes(MediaType.APPLICATION_JSON)
   Container createContainer(@BinderParam(BindToJsonPayload.class) Config config);

   @Named("container:inspect")
   @GET
   @Path("/containers/{id}/json")
   @Consumes(MediaType.APPLICATION_JSON)
   @Fallback(Fallbacks.NullOnNotFoundOr404.class)
   Container inspectContainer(@PathParam("id") String containerId);

   /**
    * Delete the container.
    *
    * @return the container.
    */
   @Named("container:delete")
   @DELETE
   @Path("/containers/{id}")
   void removeContainer(@PathParam("id") String containerId, @QueryParam("v") boolean v);

   /**
    * Start a container.
    *
    * @return the container.
    */
   @Named("container:start")
   @POST
   @Path("/containers/{id}/start")
   @Consumes(MediaType.APPLICATION_JSON)
   void startContainer(@PathParam("id") String containerId);

   /**
    * Start a container.
    *
    * @return the container.
    */
   @Named("container:start")
   @POST
   @Path("/containers/{id}/start")
   @Consumes(MediaType.APPLICATION_JSON)
   void startContainer(@PathParam("id") String containerId, @BinderParam(BindToJsonPayload.class) HostConfig hostConfig);

   /**
    * Stop a container.
    *
    * @return the container.
    */
   @Named("container:stop")
   @POST
   @Path("/containers/{id}/stop")
   @Consumes(MediaType.APPLICATION_JSON)
   InputStream stopContainer(@PathParam("id") String containerId);

   /**
    * Create a new image from a container’s changes
    *
    * @return a new image created from the current container's status.
    */
   @Named("container:commit")
   @POST
   @Path("/commit")
   @Consumes(MediaType.APPLICATION_JSON)
   Image commit(
           @QueryParam("container") String containerId, @QueryParam("repo") String repository, @QueryParam("m") String message);

   /**
    * Create a new image from a container’s changes
    *
    * @return a new image created from the current container's status.
    */
   @Named("container:commit")
   @POST
   @Path("/commit")
   @Consumes(MediaType.APPLICATION_JSON)
   InputStream commit(
           @QueryParam("container") String containerId, @QueryParam("repo") String repository, @QueryParam("tag") String tag,
           @QueryParam("m") String message, @QueryParam("author") String author, @QueryParam("run") String run);

   /**
    * List images
    *
    * @return the images available.
    */
   @Named("images:list")
   @GET
   @Path("/images/json")
   @Consumes(MediaType.APPLICATION_JSON)
   @Fallback(Fallbacks.EmptySetOnNotFoundOr404.class)
   Set<Image> listImages(@QueryParam("all") boolean all);

   // Image
   /**
    * Create an image.
    *
    * @return the image created.
    */
   @Named("image:create")
   @POST
   @Path("/images/create")
   @Consumes(MediaType.APPLICATION_JSON)
   InputStream createImage(@QueryParam("fromImage") String fromImage);

   /**
    * Create an image.
    *
    */
   @Named("image:create")
   @POST
   @Path("/images/create")
   @Consumes(MediaType.APPLICATION_JSON)
   InputStream createImage(
           @QueryParam("fromImage") String fromImage, @QueryParam("fromSrc") String fromSrc, @QueryParam("repo") String repo,
           @QueryParam("tag") String tag,  @QueryParam("registry") String registry);

   /**
    * Delete an image.
    *
    */
   @Named("image:delete")
   @DELETE
   @Path("/images/{name}")
   @Consumes(MediaType.APPLICATION_JSON)
   InputStream deleteImage(@PathParam("name") String name);


   /**
    * Build an image from Dockerfile via stdin
    *
    */
   @Named("image:build")
   @POST
   @Path("/build")
   @Consumes(MediaType.APPLICATION_JSON)
   @Headers(keys = "Content-Type", values = "application/tar")
   InputStream build(@QueryParam("t") String tag, Payload inputStream);

   /**
    * Build an image from Dockerfile via stdin
    *
    */
   @Named("image:build")
   @POST
   @Path("/build")
   @Consumes(MediaType.APPLICATION_JSON)
   @Headers(keys = "Content-Type", values = "application/tar")
   InputStream build(@QueryParam("t") String tag, @QueryParam("q") boolean quiet, @QueryParam("nocache") boolean nocache,
                     @BinderParam(BindInputStreamToRequest.class) File dockerFile);

}
