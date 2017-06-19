// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// TODO: High-level file comment.

/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.guestbook;

import io.github.resilience4j.bulkhead.Bulkhead;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by rayt on 5/1/17.
 */
public class HelloworldService {
  private final RestTemplate restTemplate;
  private final String endpoint;
  private static final Map<String, String> BULHEAD_RESPONSE = new HashMap<>();
  static {
    BULHEAD_RESPONSE.put("greeting", "Unable to connect");
  }

  public HelloworldService(RestTemplate restTemplate, String endpoint) {
    this.restTemplate = restTemplate;
    this.endpoint = endpoint;
  }

  public Map<String, String> greeting(String name) {
    try {
      return restTemplate.getForObject(endpoint + "/" + name, Map.class);
    } catch (HttpStatusCodeException e) {
      if (e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
        return BULHEAD_RESPONSE;
      } else {
        throw e;
      }
    }
  }
}
