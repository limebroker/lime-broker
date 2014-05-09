package com.limebroker.broker.protocol.mqtt.header;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.limebroker.broker.LimeBrokerException;

/**
 * FixedHeader Tests
 * 
 * @author Martyn Taylor <mtaylor@redhat.com>
 * 
 */
public class FixedHeaderTest {
    private Map<Long, byte[]> generateRequiredLengthTestData() {
        Map<Long, byte[]> m = new HashMap<Long, byte[]>();
        m.put(Long.valueOf(0), new byte[] { 0x00 });
        m.put(Long.valueOf(127), new byte[] { 0x7F });
        m.put(Long.valueOf(128), new byte[] { (byte) 0x80, 0x01 });
        m.put(Long.valueOf(16384),
                new byte[] { (byte) 0x80, (byte) 0x80, 0x01 });
        m.put(Long.valueOf(2097152), new byte[] { (byte) 0x80, (byte) 0x80,
                (byte) 0x80, 0x01 });
        m.put(Long.valueOf(FixedHeader.MAX_LENGTH), new byte[] { (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, 0x7F });
        return m;
    }

    @Test
    public void encodeRemainingLengthTest() {
        try {
            for (Map.Entry<Long, byte[]> entry : generateRequiredLengthTestData()
                    .entrySet()) {
                ByteBuffer buffer = ByteBuffer
                        .allocate(entry.getValue().length);
                FixedHeader.encodeRemainingLength(entry.getKey().longValue(),
                        buffer);
                assertArrayEquals(entry.getValue(), buffer.array());
            }
        } catch (LimeBrokerException e) {
            fail(e.getStackTrace().toString());
        }
    }

    @Test
    public void decodeRemainingLengthTest() {
        try {
            for (Map.Entry<Long, byte[]> entry : generateRequiredLengthTestData()
                    .entrySet()) {
                ByteBuffer buffer = ByteBuffer
                        .allocate(entry.getValue().length);
                for (byte b : entry.getValue()) {
                    buffer.put(b);
                }
                buffer.rewind();
                FixedHeader.decodeRemainingLength(buffer);
                assertArrayEquals(entry.getValue(), buffer.array());
            }
        } catch (LimeBrokerException e) {
            fail(e.getStackTrace().toString());
        }
    }
}