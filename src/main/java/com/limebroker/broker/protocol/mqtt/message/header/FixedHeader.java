package com.limebroker.broker.protocol.mqtt.message.header;

import io.netty.buffer.ByteBuf;

import com.limebroker.broker.LimeBrokerException;
import com.limebroker.broker.protocol.mqtt.exception.MalformedMessageException;

/**
 * Represents Fixed Header component of any MQTT Message.
 * 
 * @author Martyn Taylor <mtaylor@redhat.com>
 * 
 */
public class FixedHeader {

    // Max length of MQTT message in bytes.
    public static int MAX_LENGTH = 268435455;

    // First byte of the header.
    private byte byte1;

    // Remaining Length value of this
    private int remainingLength;

    /**
     * Decodes a FixedHeader object from the ByteBuf
     * 
     * @param buf
     * @return
     * @throws LimeBrokerException
     */
    public static FixedHeader decode(ByteBuf buf) throws LimeBrokerException {
        try {
            return new FixedHeader(buf.readByte(), decodeRemainingLength(buf));
        } catch (IndexOutOfBoundsException e) {
            throw new MalformedMessageException("Unable to decode Fixed Header");
        }
    }

    /**
     * Creates new Fixed Header instance by setting byte flags and remaining length.
     * 
     * @param buf
     * @return
     * @throws LimeBrokerException
     */
    public FixedHeader(byte byte1, int remainingLength) throws LimeBrokerException {
        this.byte1 = byte1;
        this.remainingLength = remainingLength;
    }

    /**
     * Encode this Fixed Header onto ByteBuf
     * 
     * @param buf
     * @throws LimeBrokerException
     */
    public void encode(ByteBuf buf) throws LimeBrokerException {
        buf.writeByte(byte1);
        FixedHeader.encodeRemainingLength(remainingLength, buf);
    }

    /**
     * Converts an integer to the variable length encoding of MQTT and writes the result to buffer
     * 
     * @param length
     * @return byte[]
     * @throws LimeBrokerException
     */
    public static void encodeRemainingLength(int length, ByteBuf buffer) throws LimeBrokerException {
        checkLength(length);
        do {
            byte digit = (byte) (length % 128);
            length = length / 128;
            if (length > 0) {
                digit = (byte) (digit | 0x80);
            }
            buffer.writeByte(digit);
        } while (length > 0);
    }

    /**
     * Converts an integer to the variable length encoding of MQTT and writes the result to buffer
     * 
     * @param length
     * @return byte[]
     * @throws LimeBrokerException
     */
    public static int decodeRemainingLength(ByteBuf buffer) throws LimeBrokerException {
        int multiplier = 1;
        int length = 0;
        byte digit;
        do {
            digit = buffer.readByte();
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
            throw new LimeBrokerException("Message length exceeds maximum length: " + MAX_LENGTH + "bytes");
        }
    }

    /**
     * Gets the Message Type of this MQTT Message Header
     * 
     * @return
     */
    public MessageType getMessageType() {
        return MessageType.getMessageTypeFromWireByte(byte1);
    }

    /**
     * Gets the QoSLevel of this MQTT Message Header
     * 
     * @return
     */
    public QoSLevel getQoSLevel() {
        return QoSLevel.getQoSFromByte(byte1);
    }

    /**
     * Get the retain flag of this MQTT Message Header
     * 
     * @return
     */
    public boolean getRetain() {
        return (byte1 & 0b00000001) != 0;
    }

    /**
     * Get the Duplicate flag of this MQTT Message Header
     * 
     * @return
     */
    public boolean getDupFlag() {
        return (byte1 & 0b00001000) != 0;
    }

    /**
     * Get the Remaining Length in bytes for the MQTT Message.
     * 
     * @return
     */
    public int getRemainingLength() {
        return remainingLength;
    }

    /**
     * Get the size of the Fixed Header either 2 or 4 bytes
     * 
     */
    public byte getSize() {
        return (byte) ((remainingLength > 127) ? 4 : 2);
    }
}