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
public class State {
   @SerializedName("Pid")
   private String pid;
   @SerializedName("Running")
   private boolean running;
   @SerializedName("ExitCode")
   private int exitCode;
   @SerializedName("StartedAt")
   private String startedAt;
   @SerializedName("Ghost")
   private boolean ghost;

   public State(String pid, boolean running, int exitCode, String startedAt, boolean ghost) {
      this.pid = pid;
      this.running = running;
      this.exitCode = exitCode;
      this.startedAt = startedAt;
      this.ghost = ghost;
   }

   public String getPid() {
      return pid;
   }

   public boolean isRunning() {
      return running;
   }

   public int getExitCode() {
      return exitCode;
   }

   public String getStartedAt() {
      return startedAt;
   }

   public boolean isGhost() {
      return ghost;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this)
              .add("pid", pid)
              .add("running", running)
              .add("exitCode", exitCode)
              .add("startedAt", startedAt)
              .add("ghost", ghost)
              .toString();
   }
}
