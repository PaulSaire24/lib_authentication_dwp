package com.bbva.pdwy.lib.r008.mock;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class ObjectMapperHelper {

    private static final ObjectMapperHelper INSTANCE = new ObjectMapperHelper();
    private ObjectMapper mapper;

    private ObjectMapperHelper() { this.mapper = new ObjectMapper(); }

    public static ObjectMapperHelper getInstance() { return INSTANCE; }

    public <T> T readValue(final InputStream src, final Class<T> valueType) throws IOException {
        return mapper.readValue(src, valueType);
    }

}
