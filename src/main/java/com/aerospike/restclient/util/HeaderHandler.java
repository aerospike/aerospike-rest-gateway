/*
 * Copyright 2022 Aerospike, Inc.
 *
 * Portions may be licensed to Aerospike, Inc. under one or more contributor
 * license agreements WHICH ARE COMPATIBLE WITH THE APACHE LICENSE, VERSION 2.0.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.aerospike.restclient.util;

import com.aerospike.restclient.domain.auth.AuthDetails;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class HeaderHandler {

    private HeaderHandler() {
    }

    public static AuthDetails extractAuthDetails(String basicAuth) {
        if (basicAuth == null || !basicAuth.toLowerCase().startsWith("basic")) {
            return null;
        }

        try {
            String base64Credentials = basicAuth.substring(5).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            // credentials = username:password
            final String[] details = credentials.split(":", 2);
            return new AuthDetails(details[0], details[1]);
        } catch (Exception e) {
            throw new RestClientErrors.UnauthorizedError();
        }
    }
}
