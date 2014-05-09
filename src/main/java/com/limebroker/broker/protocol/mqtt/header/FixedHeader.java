package com.limebroker.broker.protocol.mqtt.header;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

import com.limebroker.broker.LimeBrokerException;

/**
 * Represents Fixed Header component of any MQTT Message.
 * 
 * @author Martyn Taylor <mtaylor@redhat.com>
 * 
 */
public class FixedHeader {

    // Max length of MQTT message in bytes.
    public static long MAX_LENGTH = 268435455;

    private enum MessageType {
        CONNECT, CONNACK, PUBLISH, PUBACK, PUBREC, PUBREL, PUBCOMP, SUBSCRIBE, UNSUBSCRIBE, UNSUBACK, PINGREQ, PINGRESP, DISCONNECT
    }

    private enum QoSLevel {
        AT_MOST_ONCE, AT_LEAST_ONCE, EXACTLY_ONCE
    }

    private MessageType messageType;
    private QoSLevel qosLevel;
    private boolean duplicate;
    private boolean retain;
    private int remainingLength;

    private FixedHeader() {
    };

    /**
     * Converts an integer to the variable length encoding of MQTT and writes
     * the result to buffer
     * 
     * @param length
     * @return byte[]
     * @throws LimeBrokerException
     */
    public static void encodeRemainingLength(long length, ByteBuffer buffer)
            throws LimeBrokerException {
        checkLength(length);
        do {
            byte digit = (byte) (length % 128);
            length = length / 128;
            if (length > 0) {
                digit = (byte) (digit | 0x80);
            }
            buffer.put(digit);
        } while (length > 0);
    }

    /**
     * Converts an integer to the variable length encoding of MQTT and writes
     * the result to buffer
     * 
     * @param length
     * @return byte[]
     * @throws LimeBrokerException
     */
    public static long decodeRemainingLength(ByteBuffer buffer)
            throws LimeBrokerException {
        long multiplier = 1;
        long length = 0;
        byte digit;
        do {
            digit = buffer.get();
            length += (digit & 127) * multiplier;
            multiplier *= 128;
        } while ((digit & 128) != 0);
        checkLength(length);
        return length;
    }

    /**
     * Returns the number of bytes needed to encode the remainingLength
     * 
     * @param length
     * @return
     * @throws LimeBrokerException
     */
    private static void checkLength(long length) throws LimeBrokerException {
        // TODO i18n
        if (length > MAX_LENGTH) {
            throw new LimeBrokerException(
                    "Message length exceeds maximum length: " + MAX_LENGTH
                            + "bytes");
        }
    }
}
