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
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.RestClientErrors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gnu.crypto.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.ast.Str;

import java.util.Map;
import java.util.Optional;

public class BinConverter {

    private static final Logger logger = LogManager.getLogger(BinConverter.class);
    private static final ObjectMapper mapper = JSONMessageConverter.getJSONObjectMapper();

    @SuppressWarnings("unchecked")
    public static Bin[] binsFromMap(Map<String, Object> binMap) {
        int index = 0;
        Bin[] binArray = new Bin[binMap.size()];
        for (Map.Entry<String, Object> entry : binMap.entrySet()) {
            /* Let the user pass null, to delete a bin */
            Object value = entry.getValue();
            if (entry.getValue() == null) {
                binArray[index] = (Bin.asNull(entry.getKey()));
            } else if (entry.getValue() instanceof Map) {
                Map<String,Object> mapVal = (Map<String, Object>) value;
                Value asVal;

                if (isGeoJSON(mapVal)) {
                    try {
                        asVal = Value.getAsGeoJSON(mapper.writeValueAsString(value));
                    } catch (JsonProcessingException e) {
                        throw new RestClientErrors.InvalidGeoJSON(String.format("Unable to parse GeoJSON: %s", e));
                    }
                } else if (isBytes(mapVal)) {
                    try {
                        SpecifiedType type = SpecifiedType.valueOf((String) mapVal.get(specifiedTypeKey));
                        byte[] byteArr = Base64.decode((String) mapVal.get(specifiedValueKey));
                        switch (type) {
                            case BYTE_ARRAY:
                                asVal = Value.get(byteArr);
                                break;
                            case GEO_JSON:
                                asVal = Value.getAsGeoJSON(new String(byteArr));
                                break;
                            default:
                                asVal = Value.get(mapVal);
                        }
                    } catch (Exception e) {
                        throw new RestClientErrors.InvalidBinValue("Error parsing typed bin parameter");
                    }
                } else {
                    asVal = Value.get(mapVal);
                }

                binArray[index] = new Bin(entry.getKey(), asVal);

            } else {
                binArray[index] = new Bin(entry.getKey(), entry.getValue());
            }
            index++;
        }
        return binArray;
    }

    private static final String specifiedTypeKey = "type";
    private static final String specifiedValueKey = "value";

//    private static Value optSpecified(Map<String, Object> value) {
//        if (isGeoJSON(value)) {
//            try {
//                return Value.getAsGeoJSON(mapper.writeValueAsString(value));
//            } catch (JsonProcessingException e) {
//                throw new RestClientErrors.InvalidGeoJSON(String.format("Unable to parse GeoJSON: %s", e));
//            }
//        }
//
//        // Specifying GEOJSON this way is now deprecated.  Providing bytes is still supported.
//        if (value.size() == 2)
//            try {
//                SpecifiedType type = SpecifiedType.valueOf((String) value.get(specifiedTypeKey));
//                byte[] byteArr = Base64.decode((String) value.get(specifiedValueKey));
//                switch (type) {
//                    case BYTE_ARRAY:
//                        return Value.get(byteArr);
//                    case GEO_JSON:
//                        return Value.getAsGeoJSON(new String(byteArr));
//                }
//            } catch (Exception e) {
//                throw new RestClientErrors.Invalid "Error parsing typed bin parameter", e);
//            }
//    }

    private static boolean isGeoJSON(Map<String, Object> value) {
        return (value.size() == 2 &&
                value.containsKey(AerospikeAPIConstants.GeoJSON.Keys.TYPE) &&
                value.containsKey(AerospikeAPIConstants.GeoJSON.Keys.COORDINATES)
        );
    }

    private static boolean isBytes(Map<String, Object> value) {
        return (value.size() == 2 &&
                value.containsKey(specifiedTypeKey) &&
                value.containsKey(specifiedValueKey)
        );
    }
}

// GEO_JSON is deprecated but only documented here https://stackoverflow.com/questions/70945453/how-to-insert-geojson-using-aerospike-rest-client
enum SpecifiedType {
    BYTE_ARRAY, GEO_JSON
}
