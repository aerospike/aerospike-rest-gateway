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
package com.aerospike.restclient.util.converters;

import com.aerospike.client.Operation;

import java.util.List;
import java.util.Map;

/**
 * Deprecated. This class should only be used in /v1/operate and /v1/execute until removed.
 */
@Deprecated
public class OperationsConverter {

    public static Operation[] mapListToOperationsArray(List<Map<String, Object>> ops) {

        return ops.stream().map(OperationConverter::convertMapToOperation).toArray(Operation[]::new);
    }
}
