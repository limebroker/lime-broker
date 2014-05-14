package com.limebroker.broker.protocol.mqtt.message.header;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.limebroker.broker.LimeBrokerException;
import com.limebroker.broker.protocol.mqtt.message.header.FixedHeader;

/**
 * FixedHeader Tests
 * 
 * @author Martyn Taylor <mtaylor@redhat.com>
 * 
 */
public class FixedHeaderTest {
    private Map<Integer, byte[]> generateRequiredLengthTestData() {
        Map<Integer, byte[]> m = new HashMap<Integer, byte[]>();
        m.put(Integer.valueOf(0), new byte[] { 0x00 });
        m.put(Integer.valueOf(127), new byte[] { 0x7F });
        m.put(Integer.valueOf(128), new byte[] { (byte) 0x80, 0x01 });
        m.put(Integer.valueOf(16384), new byte[] { (byte) 0x80, (byte) 0x80,
                0x01 });
        m.put(Integer.valueOf(2097152), new byte[] { (byte) 0x80, (byte) 0x80,
                (byte) 0x80, 0x01 });
        m.put(Integer.valueOf(FixedHeader.MAX_LENGTH), new byte[] {
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x7F });
        return m;
    }

    @Test
    public void encodeRemainingLengthTest() {
        try {
            for (Map.Entry<Integer, byte[]> entry : generateRequiredLengthTestData()
                    .entrySet()) {
                ByteBuf buffer = Unpooled.buffer(entry.getValue().length);
                FixedHeader.encodeRemainingLength(entry.getKey().intValue(),
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
            for (Map.Entry<Integer, byte[]> entry : generateRequiredLengthTestData()
                    .entrySet()) {
                ByteBuf buffer = Unpooled.buffer(entry.getValue().length);
                for (byte b : entry.getValue()) {
                    buffer.writeByte(b);
                }
                FixedHeader.decodeRemainingLength(buffer);
                assertArrayEquals(entry.getValue(), buffer.array());
            }
        } catch (LimeBrokerException e) {
            fail(e.getStackTrace().toString());
        }
    }

    @Test
    public void getRetainTest() {
        try {
            FixedHeader fh = FixedHeader
                    .createFixedHeader((byte) 0b00001111, 0);
            assertEquals(true, fh.getRetain());

            fh = FixedHeader.createFixedHeader((byte) 0b0000110, 0);
            assertEquals(false, fh.getRetain());
        } catch (Exception e) {
            fail(e.getStackTrace().toString());
        }
    }

    @Test
    public void getDupFlagTest() {
        try {
            FixedHeader fh = FixedHeader
                    .createFixedHeader((byte) 0b11111000, 0);
            assertEquals(true, fh.getDupFlag());

            fh = FixedHeader.createFixedHeader((byte) 0b1000110, 0);
            assertEquals(false, fh.getDupFlag());
        } catch (Exception e) {
            fail(e.getStackTrace().toString());
        }
    }
}