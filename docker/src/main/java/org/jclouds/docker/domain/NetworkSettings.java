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

import java.util.List;
import java.util.Map;

/**
 * @author Andrea Turli
 *
 * {
"IPAddress": "172.17.0.3",
"IPPrefixLen": 16,
"Gateway": "172.17.42.1",
"Bridge": "docker0",
"PortMapping": null,
"Ports": {
"22/tcp": [
{
"HostIp": "0.0.0.0",
"HostPort": "10022"
}
]
}
}

 */
public class NetworkSettings {

   @SerializedName("IPAddress")
   private String ipAddress;
   @SerializedName("IPPrefixLen")
   private int ipPrefixLen;
   @SerializedName("Gateway")
   private String gateway;
   @SerializedName("Bridge")
   private String bridge;
   @SerializedName("Ports")
   private Map<String, List<Map<String, String>>> ports;

   public NetworkSettings(String ipAddress, int ipPrefixLen, String gateway, String bridge, Map<String, List<Map<String, String>>> ports) {
      this.ipAddress = ipAddress;
      this.ipPrefixLen = ipPrefixLen;
      this.gateway = gateway;
      this.bridge = bridge;
      this.ports = ports;
   }

   public String getIpAddress() {
      return ipAddress;
   }

   public int getIpPrefixLen() {
      return ipPrefixLen;
   }

   public String getGateway() {
      return gateway;
   }

   public String getBridge() {
      return bridge;
   }

   public Map<String, List<Map<String, String>>> getPorts() {
      return ports;
   }

   @Override
   public String toString() {
      return "NetworkSettings{" +
              "ipAddress='" + ipAddress + '\'' +
              ", ipPrefixLen=" + ipPrefixLen +
              ", gateway='" + gateway + '\'' +
              ", bridge='" + bridge + '\'' +
              ", ports=" + ports +
              '}';
   }
}
