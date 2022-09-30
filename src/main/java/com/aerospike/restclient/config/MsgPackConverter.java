package com.aerospike.restclient.config;

import com.aerospike.client.Value;
import com.aerospike.restclient.util.serializers.MsgPackGeoJSONSerializer;
import com.aerospike.restclient.util.serializers.MsgPackObjKeySerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

public class MsgPackConverter extends AbstractJackson2HttpMessageConverter {
    static final MediaType mediaType = new MediaType("application", "msgpack");

    MsgPackConverter() {
        super(getASMsgPackObjectMapper(), mediaType);
        ObjectMapper mapper = getObjectMapper();
        mapper.registerModule(new ParameterNamesModule());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        addSerializerModules(mapper);
    }

    public static ObjectMapper getASMsgPackObjectMapper() {
        MessagePackFactory aerospikeMsgPackFactory = new MessagePackFactory();
        return new ObjectMapper(aerospikeMsgPackFactory);
    }

    private static void addSerializerModules(ObjectMapper mapper) {
        SimpleModule recModule = new SimpleModule();
        recModule.addSerializer(Value.GeoJSONValue.class, new MsgPackGeoJSONSerializer());
        recModule.addKeySerializer(Object.class, new MsgPackObjKeySerializer());
        mapper.registerModule(recModule);
    }
}
