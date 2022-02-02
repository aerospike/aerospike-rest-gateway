/*
 * Copyright 2019 Aerospike, Inc.
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

import com.aerospike.client.Bin;
import com.aerospike.client.Value;
import gnu.crypto.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

public class BinConverter {

    private static final Logger logger = LogManager.getLogger(BinConverter.class);

    @SuppressWarnings("unchecked")
    public static Bin[] binsFromMap(Map<String, Object> binMap) {
        int index = 0;
        Bin[] binArray = new Bin[binMap.size()];
        for (Map.Entry<String, Object> entry : binMap.entrySet()) {
            /* Let the user pass null, to delete a bin */
            if (entry.getValue() == null) {
                binArray[index] = (Bin.asNull(entry.getKey()));
            } else if (entry.getValue() instanceof Map) {
                Optional<Value> spec = optSpecified((Map<String, Object>) entry.getValue());
                binArray[index] = spec.map(value -> new Bin(entry.getKey(), value))
                        .orElseGet(() -> new Bin(entry.getKey(), entry.getValue()));
            } else {
                binArray[index] = new Bin(entry.getKey(), entry.getValue());
            }
            index++;
        }
        return binArray;
    }

    private static final String specifiedTypeKey = "type";
    private static final String specifiedValueKey = "value";

    private static Optional<Value> optSpecified(Map<String, Object> value) {
        if (value.size() == 2)
            try {
                SpecifiedType type = SpecifiedType.valueOf((String) value.get(specifiedTypeKey));
                byte[] byteArr = Base64.decode((String) value.get(specifiedValueKey));
                switch (type) {
                    case BYTE_ARRAY:
                        return Optional.of(Value.get(byteArr));
                    case GEO_JSON:
                        return Optional.of(Value.getAsGeoJSON(new String(byteArr)));
                }
            } catch (Exception e) {
                logger.error("Error parsing typed bin parameter", e);
            }
        return Optional.empty();
    }
}

enum SpecifiedType {
    BYTE_ARRAY, GEO_JSON
}
