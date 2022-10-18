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
package com.aerospike.restclient.util.deserializers;

import com.aerospike.restclient.util.RestClientErrors.MalformedMsgPackError;
import org.msgpack.core.MessageFormat;
import org.msgpack.core.MessagePackException;
import org.msgpack.value.ImmutableValue;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MsgPackBinParser extends MsgPackParser {
    private Map<String, Object> bins = null;

    public MsgPackBinParser(InputStream stream) {
        super(stream);
    }

    public Map<String, Object> parseBins() {
        try {
            unpackBins();
        } catch (IOException e) {
            throw new MalformedMsgPackError("Invalid msgpack representation");
        }

        return bins;
    }

    private void unpackBins() throws IOException {
        try {
            MessageFormat form = unpacker.getNextFormat();
            if (form != MessageFormat.MAP16 && form != MessageFormat.MAP32 && form != MessageFormat.FIXMAP) {
                throw new MalformedMsgPackError(
                        String.format("Bins must be a map<String, Object> got  %s", form.toString()));

            }
            int size = unpacker.unpackMapHeader();
            bins = new HashMap<String, Object>(size);
            for (int i = 0; i < size; i++) {
                ImmutableValue keyValue = unpacker.unpackValue();
                if (!keyValue.isStringValue()) {
                    throw new MalformedMsgPackError(
                            String.format("Binnames must be strings got %s", keyValue.getValueType().toString()));
                }
                String binName = keyValue.toString();
                Object binValue = unpackValue();
                bins.put(binName, binValue);
            }

            if (unpacker.hasNext()) {
                throw new MalformedMsgPackError();
            }
        } catch (MessagePackException e) {
            throw new MalformedMsgPackError("Failed to deserialize MsgPack data");
        }
    }

}
