package ru.draen.hps.common.webmvc.utils;

import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

import java.time.Instant;

public class InstantSerializer implements CompactSerializer<Instant> {
    @Override
    public Instant read(CompactReader reader) {
        long epoch = reader.readInt64("epoch");
        int nano = reader.readInt32("nano");
        return Instant.ofEpochSecond(epoch, nano);
    }

    @Override
    public void write(CompactWriter writer, Instant object) {
        writer.writeInt64("epoch", object.getEpochSecond());
        writer.writeInt32("nano", object.getNano());
    }

    @Override
    public String getTypeName() {
        return "instant";
    }

    @Override
    public Class<Instant> getCompactClass() {
        return Instant.class;
    }
}
