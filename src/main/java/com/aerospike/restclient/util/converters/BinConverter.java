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

import com.aerospike.client.Bin;
import com.aerospike.client.Value;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.RestClientErrors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gnu.crypto.util.Base64;

import java.util.List;
import java.util.Map;

public class BinConverter {
    private static final ObjectMapper mapper = JSONMessageConverter.getJSONObjectMapper();

    @SuppressWarnings("unchecked")
    public static Bin[] binsFromMap(Map<String, Object> binMap) {
        // We may need a more explicit "Bins" model for better swagger documentation.
        // through the CDT and translate.
        int index = 0;
        Bin[] binArray = new Bin[binMap.size()];

        for (Map.Entry<String, Object> entry : binMap.entrySet()) {
            /* Let the user pass null, to delete a bin */
            Object value = entry.getValue();
            if (value == null) {
                binArray[index] = (Bin.asNull(entry.getKey()));
            } else if (value instanceof Map) {
                Map<String, Object> mapVal = (Map<String, Object>) value;
                Value asVal;

                if (isGeoJSON(mapVal)) {
                    try {
                        asVal = Value.getAsGeoJSON(mapper.writeValueAsString(value));
                    } catch (JsonProcessingException e) {
                        throw new RestClientErrors.InvalidGeoJSON(String.format("Unable to parse GeoJSON: %s", e));
                    }
                } else if (isBytes(mapVal)) {
                    try {
                        AerospikeAPIConstants.SpecifiedType.Type type = AerospikeAPIConstants.SpecifiedType.Type.valueOf(
                                (String) mapVal.get(AerospikeAPIConstants.SpecifiedType.Keys.specifiedTypeKey));
                        byte[] byteArr = Base64.decode(
                                (String) mapVal.get(AerospikeAPIConstants.SpecifiedType.Keys.specifiedValueKey));
                        asVal = switch (type) {
                            case BYTE_ARRAY -> Value.get(byteArr);
                            case GEO_JSON ->
                                // GEO_JSON is deprecated but only documented here https://stackoverflow.com/questions/70945453/how-to-insert-geojson-using-aerospike-rest-client
                                    Value.getAsGeoJSON(new String(byteArr));
                            default -> Value.get(mapVal);
                        };
                    } catch (Exception e) {
                        throw new RestClientErrors.InvalidBinValue(
                                String.format("Error parsing typed bin parameter: %s", e));
                    }
                } else {
                    asVal = Value.get(mapVal);
                }

                binArray[index] = new Bin(entry.getKey(), asVal);

            } else {
                binArray[index] = binFromObject(entry.getKey(), value);
            }
            index++;
        }
        return binArray;
    }

    public static Bin binFromObject(String key, Object value) {
        if (value instanceof Integer castVal) {
            return new Bin(key, castVal);
        } else if (value instanceof Short castVal) {
            return new Bin(key, castVal);
        } else if (value instanceof Long castVal) {
            return new Bin(key, castVal);
        } else if (value instanceof String castVal) {
            return new Bin(key, castVal);
        } else if (value instanceof Boolean castVal) {
            return new Bin(key, castVal);
        } else if (value instanceof Float castVal) {
            return new Bin(key, castVal);
        } else if (value instanceof Double castVal) {
            return new Bin(key, castVal);
        } else if (value instanceof List<?> castVal) {
            return new Bin(key, castVal);
        } else if (value instanceof Map<?, ?> castVal) {
            return new Bin(key, castVal);
        } else if (value instanceof Byte castVal) {
            return new Bin(key, castVal);
        } else if (value instanceof byte[] castVal) {
            return new Bin(key, castVal);
        } else if (value instanceof Value castVal) {
            return new Bin(key, castVal);
        } else {
            throw new RestClientErrors.InvalidBinValue(
                    String.format("Unsupported bin type for key %s : %s", key, value.getClass().getSimpleName()));
        }
    }

    private static boolean isGeoJSON(Map<String, Object> value) {
        return (isGeoJSONGeometry(value) || isGeoJSONFeature(value));
    }

    // Only checks keys to determine if is supported geojson.
    private static boolean isGeoJSONGeometry(Map<String, Object> value) {
        // TODO: Make the Bins model more expressive.
        return (value.size() == 2 && value.containsKey(AerospikeAPIConstants.GeoJSON.Keys.TYPE) && value.containsKey(
                AerospikeAPIConstants.GeoJSON.Keys.COORDINATES));
    }

    // Only checks keys to determine if is supported geojson.
    private static boolean isGeoJSONFeature(Map<String, Object> value) {
        return (value.size() == 3 && value.containsKey(AerospikeAPIConstants.GeoJSON.Keys.TYPE) && value.containsKey(
                AerospikeAPIConstants.GeoJSON.Keys.GEOMETRY) && value.containsKey(
                AerospikeAPIConstants.GeoJSON.Keys.PROPERTIES));
    }

    private static boolean isBytes(Map<String, Object> value) {
        return (value.size() == 2 && value.containsKey(
                AerospikeAPIConstants.SpecifiedType.Keys.specifiedTypeKey) && value.containsKey(
                AerospikeAPIConstants.SpecifiedType.Keys.specifiedValueKey));
    }
}