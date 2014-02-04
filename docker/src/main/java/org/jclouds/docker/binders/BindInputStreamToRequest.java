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
package org.jclouds.docker.binders;

import com.google.common.io.Files;
import org.jclouds.docker.compute.features.internal.Archives;
import org.jclouds.http.HttpRequest;
import org.jclouds.io.Payload;
import org.jclouds.io.Payloads;
import org.jclouds.io.payloads.InputStreamPayload;
import org.jclouds.rest.Binder;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 
 * @author Andrea Turli
 */
@Singleton
public class BindInputStreamToRequest implements Binder {

   @SuppressWarnings("unchecked")
   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      checkArgument(checkNotNull(input, "input") instanceof File, "this binder is only valid for File!");
      checkNotNull(request, "request");

      File dockerFile = (File) input;
      File tmpDir = Files.createTempDir();
      final File targetFile = new File(tmpDir + File.separator + "Dockerfile");
      try {
         Files.copy(dockerFile, targetFile);
         File archive = Archives.tar(tmpDir, File.createTempFile("archive", ".tar"));
         FileInputStream data = new FileInputStream(archive);
         Payload payload = Payloads.newInputStreamPayload(data);
         payload.getContentMetadata().setContentLength(data.getChannel().size());
         payload.getContentMetadata().setContentType(MediaType.TEXT_PLAIN);
         request.setPayload(payload);
      } catch (IOException e) {
         e.printStackTrace();
      }
      return request;
   }
}
