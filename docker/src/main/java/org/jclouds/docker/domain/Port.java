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

import com.google.gson.annotations.SerializedName;

/**
 * @author Andrea Turli
 */
public class Port {

   @SerializedName("PrivatePort")
   private int privatePort;
   @SerializedName("PublicPort")
   private int publicPort;
   @SerializedName("Type")
   private String type;
   @SerializedName("IP")
   private String ip;

   public Port(int privatePort, int publicPort, String type, String ip) {
      this.privatePort = privatePort;
      this.publicPort = publicPort;
      this.type = type;
      this.ip = ip;
   }

   public int getPrivatePort() {
      return privatePort;
   }

   public int getPublicPort() {
      return publicPort;
   }

   public String getType() {
      return type;
   }

   public String getIp() {
      return ip;
   }

   @Override
   public String toString() {
      return "Port{" +
              "privatePort=" + privatePort +
              ", publicPort=" + publicPort +
              ", type='" + type + '\'' +
              ", ip='" + ip + '\'' +
              '}';
   }
}
