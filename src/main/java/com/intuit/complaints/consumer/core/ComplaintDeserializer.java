package com.intuit.complaints.consumer.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.complaints.dal.Complaint;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class ComplaintDeserializer implements Deserializer<Complaint> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void close() {
    }


    @Override
    public Complaint deserialize(final String topic, final byte[] bytes) {
        try {
            return mapper.readValue(bytes, new TypeReference<Complaint>() {
            });
        } catch (final IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
