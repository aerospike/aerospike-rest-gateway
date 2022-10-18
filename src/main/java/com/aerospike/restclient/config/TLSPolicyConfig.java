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
package com.aerospike.restclient.config;

import com.aerospike.restclient.util.TLSPolicyBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TLSPolicyConfig {

    @Value("${aerospike.restclient.ssl.enabled:#{false}}")
    private Boolean enabled;
    @Value("${aerospike.restclient.ssl.keystorepath:#{null}}")
    private String keystorePath;
    @Value("${aerospike.restclient.ssl.keystorepassword:#{null}}")
    private String keystorePassword;
    @Value("${aerospike.restclient.ssl.keypassword:#{null}}")
    private String keyPassword;

    @Value("${aerospike.restclient.ssl.truststorepath:#{null}}")
    private String truststorePath;
    @Value("${aerospike.restclient.ssl.truststorepassword:#{null}}")
    private String truststorePassword;

    @Value("${aerospike.restclient.ssl.forloginonly:#{false}}")
    private Boolean forLoginOnly;

    @Value("${aerospike.restclient.ssl.allowedciphers:#{null}}")
    private List<String> allowedCiphers;
    @Value("${aerospike.restclient.ssl.allowedprotocols:#{null}}")
    private List<String> allowedProtocols;

    @Bean
    public TLSPolicyBuilder ConfigTLSPolicyBuilder() {
        return new TLSPolicyBuilder(enabled, keystorePath,
                keystorePassword == null ? null : keystorePassword.toCharArray(),
                keyPassword == null ? null : keyPassword.toCharArray(), truststorePath,
                truststorePassword == null ? null : truststorePassword.toCharArray(), allowedCiphers, allowedProtocols,
                forLoginOnly);
    }
}