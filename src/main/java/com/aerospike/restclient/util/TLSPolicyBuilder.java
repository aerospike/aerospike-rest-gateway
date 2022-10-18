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

import com.aerospike.client.policy.TlsPolicy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

public class TLSPolicyBuilder {

    boolean enabled;
    String keystorePath;
    char[] keystorePassword;
    char[] keyPassword;
    String truststorePath;
    char[] truststorePassword;
    List<String> allowedCiphers;
    List<String> allowedProtocols;
    Boolean forLoginOnly;

    public TLSPolicyBuilder(boolean enabled, String keystorePath, char[] keystorePassword, char[] keyPassword,
                            String truststorePath, char[] truststorePassword, List<String> allowedCiphers,
                            List<String> allowedProtocols, Boolean forLoginOnly) {
        this.enabled = enabled;
        this.keystorePath = keystorePath;
        this.keystorePassword = keystorePassword;
        this.keyPassword = keyPassword;
        this.truststorePath = truststorePath;
        this.truststorePassword = truststorePassword;
        this.allowedCiphers = allowedCiphers;
        this.allowedProtocols = allowedProtocols;
        this.forLoginOnly = forLoginOnly;
    }

    public TlsPolicy build() {
        if (!enabled) {
            return null;
        }

        TlsPolicy policy = new TlsPolicy();
        if (keystorePath != null || truststorePath != null) {
            addSSLContext(policy);
        }

        if (allowedCiphers != null) {
            policy.ciphers = allowedCiphers.toArray(new String[]{});
        }

        if (allowedProtocols != null) {
            policy.protocols = allowedProtocols.toArray(new String[]{});
        }

        if (forLoginOnly != null) {
            policy.forLoginOnly = forLoginOnly;
        }

        return policy;
    }

    private void addSSLContext(TlsPolicy tlsPolicy) {
        tlsPolicy.context = getSSLContext();
    }

    private SSLContext getSSLContext() {
        SSLContextBuilder ctxBuilder = SSLContexts.custom();

        if (keystorePath != null) {
            loadKeyStore(ctxBuilder);
        }

        if (truststorePath != null) {
            loadTrustStore(ctxBuilder);
        }

        try {
            return ctxBuilder.build();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new ASTLSConfigException("Failed to build SSLContext.");
        }
    }

    private void loadTrustStore(SSLContextBuilder ctxBuilder) {
        File tsFile = new File(truststorePath);

        try {
            if (truststorePassword != null) {
                ctxBuilder.loadTrustMaterial(tsFile, truststorePassword);
            } else {
                ctxBuilder.loadTrustMaterial(tsFile);
            }
        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException e) {
            throw new ASTLSConfigException("Failed To load truststore.");
        }
    }

    private void loadKeyStore(SSLContextBuilder ctxBuilder) {
        File ksFile = new File(keystorePath);

        if (keystorePassword == null) {
            throw new ASTLSConfigException("If Keystore Path is provided, Keystore Password must be provided.");
        }

        try {
            if (keyPassword == null) {
                // If keyPass is not provided, assume it is the same as the keystore password
                ctxBuilder.loadKeyMaterial(ksFile, keystorePassword, keystorePassword);
            } else {
                ctxBuilder.loadKeyMaterial(ksFile, keystorePassword, keyPassword);
            }
        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | UnrecoverableKeyException |
                 IOException e) {
            throw new ASTLSConfigException("Failed To load keystore.");
        }
    }

}

class ASTLSConfigException extends RuntimeException {

    private static final long serialVersionUID = 7499868056464128887L;

    public ASTLSConfigException(String msg) {
        super(msg);
    }

}