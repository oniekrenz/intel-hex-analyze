package de.niekrenz.intelhex.analyze.memmodel;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import java.io.InputStream;

public class MemoryModelParser {

    private final ObjectReader objectReader;

    public MemoryModelParser() {
        objectReader = new ObjectMapper(new YAMLFactory())
                .readerFor(MemoryModel.class)
                .with(DeserializationFeature.USE_LONG_FOR_INTS);
    }

    public MemoryModel parse(InputStream inputStream) {
        try {
            return objectReader.readValue(inputStream);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
