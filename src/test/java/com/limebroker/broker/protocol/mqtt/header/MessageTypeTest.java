package com.limebroker.broker.protocol.mqtt.header;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.limebroker.broker.protocol.mqtt.message.header.MessageType;

/**
 * MessageType Tests
 * 
 * @author Martyn Taylor <mtaylor@redhat.com>
 * 
 */
public class MessageTypeTest {

    @Test
    public void getMessageTypeFromByteTest() {
        assertEquals(MessageType.CONNECT,
                MessageType.getMessageTypeFromByte((byte) 0b00000001));
        assertEquals(MessageType.DISCONNECT,
                MessageType.getMessageTypeFromByte((byte) 0b00001110));
    }

    @Test
    public void getMessageTypeFromWireByteTest() {
        assertEquals(MessageType.CONNECT,
                MessageType.getMessageTypeFromWireByte((byte) 0b00011111));
        assertEquals(MessageType.DISCONNECT,
                MessageType.getMessageTypeFromWireByte((byte) 0b11100000));
    }
}