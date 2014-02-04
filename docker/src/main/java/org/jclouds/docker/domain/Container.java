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

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

/**
 * @author Andrea Turli
 */
public class Container {

   @SerializedName("Id")
   private String id;
   @SerializedName("Names")
   private List<String> names;
   @SerializedName("Created")
   private String created;
   @SerializedName("Path")
   private String path;
   @SerializedName("Args")
   private String[] args;
   @SerializedName("Config")
   private Config config;
   @SerializedName("State")
   private State state;
   @SerializedName("Image")
   private String image;
   @SerializedName("NetworkSettings")
   private NetworkSettings networkSettings;
   @SerializedName("SysInitPath")
   private String sysInitPath;
   @SerializedName("ResolvConfPath")
   private String resolvConfPath;
   @SerializedName("Volumes")
   private Volumes volumes;
   @SerializedName("SizeRw")
   private long sizeRw;
   @SerializedName("SizeRootFs")
   private long sizeRootFs;
   @SerializedName("Command")
   private String command;
   @SerializedName("Status")
   private String status;
   @SerializedName("HostConfig")
   private HostConfig hostConfig;
   @SerializedName("Ports")
   private List<Port> ports;

   public Container(String id, List<String> names, String created, String path, String[] args, Config config, State state, String image,
                    NetworkSettings networkSettings, String sysInitPath, String resolvConfPath, Volumes volumes,
                    long sizeRw, long sizeRootFs, String command, String status, HostConfig hostConfig, List<Port> ports) {
      this.id = id;
      this.names = names;
      this.created = created;
      this.path = path;
      this.args = args;
      this.config = config;
      this.state = state;
      this.image = image;
      this.networkSettings = networkSettings;
      this.sysInitPath = sysInitPath;
      this.resolvConfPath = resolvConfPath;
      this.volumes = volumes;
      this.sizeRw = sizeRw;
      this.sizeRootFs = sizeRootFs;
      this.command = command;
      this.status = status;
      this.hostConfig = hostConfig;
      this.ports = ports;
   }

   public String getId() {
      return id;
   }

   public List<String> getNames() {
      return names;
   }

   public String getCreated() {
      return created;
   }

   public String getPath() {
      return path;
   }

   public String[] getArgs() {
      return args;
   }

   public Config getConfig() {
      return config;
   }

   public State getState() {
      return state;
   }

   public String getImage() {
      return image;
   }

   public NetworkSettings getNetworkSettings() {
      return networkSettings;
   }

   public String getSysInitPath() {
      return sysInitPath;
   }

   public String getResolvConfPath() {
      return resolvConfPath;
   }

   public Volumes getVolumes() {
      return volumes;
   }

   public long getSizeRw() { return sizeRw; }

   public long getSizeRootFs() { return sizeRootFs; }

   public String getCommand() { return command; }

   public String getStatus() { return status; }

   public HostConfig getHostConfig() { return hostConfig; }

   public List<Port> getPorts() {
      return ports;
   }

   @Override
   public String toString() {
      return "Container{" +
              "id='" + id + '\'' +
              ", names=" + names +
              ", created='" + created + '\'' +
              ", path='" + path + '\'' +
              ", args=" + Arrays.toString(args) +
              ", config=" + config +
              ", state=" + state +
              ", image='" + image + '\'' +
              ", networkSettings=" + networkSettings +
              ", sysInitPath='" + sysInitPath + '\'' +
              ", resolvConfPath='" + resolvConfPath + '\'' +
              ", volumes=" + volumes +
              ", sizeRw=" + sizeRw +
              ", sizeRootFs=" + sizeRootFs +
              ", command='" + command + '\'' +
              ", status='" + status + '\'' +
              ", hostConfig=" + hostConfig +
              ", ports=" + ports +
              '}';
   }

   public static Builder builder() {
      return new Builder();
   }

   public Builder toBuilder() {
      return builder().fromContainer(this);
   }

   public static final class Builder {

      private String id;
      private List<String> names;
      private String created;
      private String path;
      private String[] args;
      private Config config;
      private State state;
      private String image;
      private NetworkSettings networkSettings;
      private String sysInitPath;
      private String resolvConfPath;
      private Volumes volumes;
      private long sizeRw;
      private long sizeRootFs;
      private String command;
      private String status;
      private HostConfig hostConfig;
      private List<Port> ports;

      public Builder id(String id) {
         this.id = id;
         return this;
      }

      public Builder names(List<String> names) {
         this.names = names;
         return this;
      }

      public Builder created(String created) {
         this.created = created;
         return this;
      }

      public Builder path(String path) {
         this.path = path;
         return this;
      }

      public Builder args( String[] args) {
         this.args = args;
         return this;
      }

      public Builder config(Config config) {
         this.config = config;
         return this;
      }

      public Builder state(State state) {
         this.state = state;
         return this;
      }

      public Builder image(String imageName) {
         this.image = imageName;
         return this;
      }

      public Builder networkSettings(NetworkSettings networkSettings) {
         this.networkSettings = networkSettings;
         return this;
      }

      public Builder sysInitPath(String sysInitPath) {
         this.sysInitPath = sysInitPath;
         return this;
      }

      public Builder resolvConfPath(String resolvConfPath) {
         this.resolvConfPath = resolvConfPath;
         return this;
      }

      public Builder volumes(Volumes volumes) {
         this.volumes = volumes;
         return this;
      }

      public Builder sizeRw(long sizeRw) {
         this.sizeRw = sizeRw;
         return this;
      }

      public Builder sizeRootFs(long sizeRootFs) {
         this.sizeRootFs = sizeRootFs;
         return this;
      }

      public Builder command(String command) {
         this.command = command;
         return this;
      }

      public Builder status(String status) {
         this.status = status;
         return this;
      }

      public Builder hostConfig(HostConfig hostConfig) {
         this.hostConfig = hostConfig;
         return this;
      }

      public Builder ports(List<Port> ports) {
         this.ports = ports;
         return this;
      }

      public Container build() {
         return new Container(id, names, created, path, args, config, state, image,
                 networkSettings, sysInitPath, resolvConfPath, volumes, sizeRw, sizeRootFs, command, status, hostConfig, ports);
      }

      public Builder fromContainer(Container in) {
         return this
                 .id(in.getId())
                 .names(in.getNames())
                 .created(in.getCreated())
                 .path(in.getPath())
                 .args(in.getArgs())
                 .config(in.getConfig())
                 .state(in.getState())
                 .image(in.getImage())
                 .networkSettings(in.getNetworkSettings())
                 .sysInitPath(in.getSysInitPath())
                 .resolvConfPath(in.getResolvConfPath())
                 .volumes(in.getVolumes())
                 .sizeRw(in.getSizeRw())
                 .sizeRootFs(in.getSizeRootFs())
                 .command(in.getCommand())
                 .status(in.getStatus())
                 .hostConfig(in.getHostConfig())
                 .ports(in.getPorts());
      }
   }
}
