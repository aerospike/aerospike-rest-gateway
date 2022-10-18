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
package com.aerospike.restclient.service;

import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.handlers.TruncateHandler;
import com.aerospike.restclient.util.AerospikeClientPool;
import com.aerospike.restclient.util.converters.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class AerospikeTruncateServiceV1 implements AerospikeTruncateService {

    @Autowired
    private AerospikeClientPool clientPool;

    @Override
    public void truncate(AuthDetails authDetails, String namespace, String set, String dateString) {
        Calendar calendar = DateConverter.iso8601StringToCalendar(dateString);
        TruncateHandler.create(clientPool.getClient(authDetails)).truncate(null, namespace, set, calendar);
    }
}
