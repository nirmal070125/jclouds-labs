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

/**
 * @author Andrea Turli
 */
public class Version {
   @SerializedName("Arch")
   private String arch;
   @SerializedName("GitCommit")
   private String gitCommit;
   @SerializedName("GoVersion")
   private String goVersion;
   @SerializedName("KernelVersion")
   private String kernelVersion;
   @SerializedName("Os")
   private String os;
   @SerializedName("Version")
   private String version;

   public Version(String arch, String gitCommit, String goVersion, String kernelVersion, String os, String version) {
      this.arch = arch;
      this.gitCommit = gitCommit;
      this.goVersion = goVersion;
      this.kernelVersion = kernelVersion;
      this.os = os;
      this.version = version;
   }

   public String getArch() {
      return arch;
   }

   public String getGitCommit() {
      return gitCommit;
   }

   public String getGoVersion() {
      return goVersion;
   }

   public String getKernelVersion() {
      return kernelVersion;
   }

   public String getOs() {
      return os;
   }

   public String getVersion() {
      return version;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Version version1 = (Version) o;
      if (arch != null ? !arch.equals(version1.arch) : version1.arch != null) return false;
      if (gitCommit != null ? !gitCommit.equals(version1.gitCommit) : version1.gitCommit != null) return false;
      if (goVersion != null ? !goVersion.equals(version1.goVersion) : version1.goVersion != null) return false;
      if (kernelVersion != null ? !kernelVersion.equals(version1.kernelVersion) : version1.kernelVersion != null)
         return false;
      if (os != null ? !os.equals(version1.os) : version1.os != null) return false;
      if (version != null ? !version.equals(version1.version) : version1.version != null) return false;
      return true;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(arch, gitCommit, goVersion, os, version);
   }

   @Override
   public String toString() {
      return "Version{" +
              "arch='" + arch + '\'' +
              ", gitCommit='" + gitCommit + '\'' +
              ", goVersion='" + goVersion + '\'' +
              ", kernelVersion='" + kernelVersion + '\'' +
              ", os='" + os + '\'' +
              ", version='" + version + '\'' +
              '}';
   }

   public static Builder builder() {
      return new Builder();
   }

   public Builder toBuilder() {
      return builder().fromVersion(this);
   }

   public static final class Builder {

      private String arch;
      private String gitCommit;
      private String goVersion;
      private String kernelVersion;
      private String os;
      private String version;

      public Builder arch(String arch) {
         this.arch = arch;
         return this;
      }

      public Builder gitCommit(String gitCommit) {
         this.gitCommit = gitCommit;
         return this;
      }

      public Builder goVersion(String goVersion) {
         this.goVersion = goVersion;
         return this;
      }

      public Builder kernelVersion(String kernelVersion) {
         this.kernelVersion = kernelVersion;
         return this;
      }

      public Builder os(String os) {
         this.os = os;
         return this;
      }

      public Builder version(String version) {
         this.version = version;
         return this;
      }

      public Version build() {
         return new Version(arch, gitCommit, goVersion, kernelVersion, os, version);
      }

      public Builder fromVersion(Version in) {
         return this
                 .arch(in.getArch())
                 .gitCommit(in.getGitCommit())
                 .goVersion(in.getGoVersion())
                 .kernelVersion(in.getKernelVersion())
                 .os(in.getOs())
                 .version(in.getVersion());
      }
   }
}
