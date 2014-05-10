package com.limebroker.broker.protocol.mqtt.header;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * MessageType Tests
 * 
 * @author Martyn Taylor <mtaylor@redhat.com>
 * 
 */
public class QoSLevelTest {

    @Test
    public void valueOfTest() {
        assertEquals(QoSLevel.AT_MOST_ONCE, QoSLevel.valueOf((byte) 0));
        assertEquals(QoSLevel.AT_LEAST_ONCE, QoSLevel.valueOf((byte) 1));
        assertEquals(QoSLevel.EXACTLY_ONCE, QoSLevel.valueOf((byte) 2));
        assertEquals(null, QoSLevel.valueOf((byte) -1));
    }

    @Test
    public void getQoSFromByteTest() {
        assertEquals(QoSLevel.AT_MOST_ONCE, QoSLevel.getQoSFromByte((byte) 0b11110000));
        assertEquals(QoSLevel.AT_LEAST_ONCE, QoSLevel.getQoSFromByte((byte) 0b11111010));
        assertEquals(QoSLevel.EXACTLY_ONCE, QoSLevel.getQoSFromByte((byte) 0b11111100));
    }
}