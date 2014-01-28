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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;
import org.jclouds.javax.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @author Andrea Turli
 */
public class Config {

   @SerializedName("Hostname")
   private String hostname;
   @SerializedName("User")
   private String user;
   @SerializedName("Memory")
   private int memory;
   @SerializedName("MemorySwap")
   private int memorySwap;
   @SerializedName("AttachStdin")
   private boolean attachStdin;
   @SerializedName("AttachStdout")
   private boolean attachStdout;
   @SerializedName("AttachStderr")
   private boolean attachStderr;
   @SerializedName("PortSpecs")
   private String portSpecs;
   @SerializedName("Tty")
   private boolean tty;
   @SerializedName("OpenStdin")
   private boolean openStdin;
   @SerializedName("StdinOnce")
   private boolean stdinOnce;
   @SerializedName("Env")
   private String env;
   @SerializedName("Cmd")
   private List<String> cmd;
   @SerializedName("Dns")
   private String dns;
   @SerializedName("Image")
   private String image;
   @SerializedName("Volumes")
   private Map<String, Object> volumes;
   @SerializedName("VolumesFrom")
   private String volumesFrom;
   @SerializedName("WorkingDir")
   private String workingDir;

   Config(String hostname, String user, int memory, int memorySwap, boolean attachStdin, boolean attachStdout,
          boolean attachStderr, String portSpecs, boolean tty, boolean openStdin, boolean stdinOnce, String env,
          @Nullable List<String> cmd, String dns, String image, @Nullable Map<String, Object> volumes, String volumesFrom,
          String workingDir) {
      this.hostname = hostname;
      this.user = user;
      this.memory = memory;
      this.memorySwap = memorySwap;
      this.attachStdin = attachStdin;
      this.attachStdout = attachStdout;
      this.attachStderr = attachStderr;
      this.portSpecs = portSpecs;
      this.tty = tty;
      this.openStdin = openStdin;
      this.stdinOnce = stdinOnce;
      this.env = env;
      this.cmd = cmd == null ? Lists.<String>newArrayList() : cmd;
      this.dns = dns;
      this.image = image;
      this.volumes = volumes == null ? Maps.<String, Object>newHashMap() : volumes;
      this.volumesFrom = volumesFrom;
      this.workingDir = workingDir;
   }

   public String getHostname() {
      return hostname;
   }

   public String getUser() {
      return user;
   }

   public int getMemory() {
      return memory;
   }

   public int getMemorySwap() {
      return memorySwap;
   }

   public boolean isAttachStdin() {
      return attachStdin;
   }

   public boolean isAttachStdout() {
      return attachStdout;
   }

   public boolean isAttachStderr() {
      return attachStderr;
   }

   public String getPortSpecs() {
      return portSpecs;
   }

   public boolean isTty() {
      return tty;
   }

   public boolean isOpenStdin() {
      return openStdin;
   }

   public boolean isStdinOnce() {
      return stdinOnce;
   }

   public String getEnv() {
      return env;
   }

   public List<String> getCmd() {
      return cmd;
   }

   public String getDns() {
      return dns;
   }

   public String getImage() {
      return image;
   }

   public Map<String, Object> getVolumes() {
      return volumes;
   }

   public String getVolumesFrom() {
      return volumesFrom;
   }

   public String getWorkingDir() {
      return workingDir;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this)
              .add("hostname", hostname)
              .add("user", user)
              .add("memory", memory)
              .add("memorySwap", memorySwap)
              .add("attachStdin", attachStdin)
              .add("attachStdout", attachStdout)
              .add("attachStderr", attachStderr)
              .add("portSpecs", portSpecs)
              .add("tty", tty)
              .add("openStdin", openStdin)
              .add("stdinOnce", stdinOnce)
              .add("env", env)
              .add("cmd", cmd)
              .add("dns", dns)
              .add("image", image)
              .add("volumes", volumes)
              .add("volumesFrom", volumesFrom)
              .add("workingDir", workingDir)
              .toString();
   }

   public static Builder builder() {
      return new Builder();
   }

   public Builder toBuilder() {
      return builder().fromConfig(this);
   }

   public static final class Builder {
      private String hostname;
      private String user;
      private int memory;
      private int memorySwap;
      private boolean attachStdin;
      private boolean attachStdout;
      private boolean attachStderr;
      private String portSpecs;
      private boolean tty;
      private boolean openStdin;
      private boolean stdinOnce;
      private String env;
      private ImmutableList.Builder<String> cmd = ImmutableList.builder();
      private String dns;
      private String image;
      private ImmutableMap.Builder<String, Object> volumes = ImmutableMap.builder();
      private String volumesFrom;
      private String workingDir;

      public Builder hostname(String hostname) {
         this.hostname = hostname;
         return this;
      }

      public Builder user(String user) {
         this.user = user;
         return this;
      }

      public Builder memory(int memory) {
         this.memory = memory;
         return this;
      }

      public Builder memorySwap(int memorySwap) {
         this.memorySwap = memorySwap;
         return this;
      }

      public Builder attachStdin(boolean attachStdin) {
         this.attachStdin = attachStdin;
         return this;
      }

      public Builder attachStdout(boolean attachStdout) {
         this.attachStdout = attachStdout;
         return this;
      }

      public Builder attachStderr(boolean attachStderr) {
         this.attachStderr = attachStderr;
         return this;
      }

      public Builder portSpecs(String portSpecs) {
         this.portSpecs = portSpecs;
         return this;
      }

      public Builder tty(boolean tty) {
         this.tty = tty;
         return this;
      }

      public Builder openStdin(boolean openStdin) {
         this.openStdin = openStdin;
         return this;
      }

      public Builder stdinOnce(boolean stdinOnce) {
         this.stdinOnce = stdinOnce;
         return this;
      }

      public Builder env(String env) {
         this.env = env;
         return this;
      }

      public Builder cmd(List<String> cmd) {
         this.cmd = ImmutableList.builder();
         this.cmd.addAll(cmd);
         return this;
      }

      public Builder dns(String dns) {
         this.dns = dns;
         return this;
      }

      public Builder image(String image) {
         this.image = image;
         return this;
      }

      public Builder volumes(Map<String, Object> volumes) {
         this.volumes = ImmutableMap.builder();
         this.volumes.putAll(volumes);
         return this;
      }

      public Builder volumesFrom(String volumesFrom) {
         this.volumesFrom = volumesFrom;
         return this;
      }

      public Builder workingDir(String workingDir) {
         this.workingDir = workingDir;
         return this;
      }

      public Config build() {
         return new Config(hostname, user, memory, memorySwap, attachStdin, attachStdout, attachStderr, portSpecs,
                 tty, openStdin, stdinOnce, env, cmd.build(), dns, image, volumes.build(), volumesFrom, workingDir);
      }

      public Builder fromConfig(Config in) {
         return this
                 .hostname(in.getHostname())
                 .user(in.getUser())
                 .memory(in.getMemory())
                 .memorySwap(in.getMemorySwap())
                 .attachStdin(in.isAttachStdin())
                 .attachStdout(in.isAttachStdout())
                 .attachStderr(in.isAttachStderr())
                 .portSpecs(in.getPortSpecs())
                 .tty(in.isTty())
                 .openStdin(in.isOpenStdin())
                 .stdinOnce(in.isStdinOnce())
                 .env(in.getEnv())
                 .cmd(in.getCmd())
                 .dns(in.getDns())
                 .image(in.getImage())
                 .volumes(in.getVolumes())
                 .volumesFrom(in.getVolumesFrom())
                 .workingDir(in.getWorkingDir());
      }
   }
}
