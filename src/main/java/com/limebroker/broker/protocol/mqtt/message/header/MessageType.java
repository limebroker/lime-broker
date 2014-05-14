package com.limebroker.broker.protocol.mqtt.message.header;

/**
 * MessageType Enum represents MQTT Fixed Header Message Type
 *
 * @author Martyn Taylor <mtaylor@redhat.com>
 *
 */
public enum MessageType {
    CONNECT((byte) 1),
    CONNACK((byte) 2),
    PUBLISH((byte) 3),
    PUBACK((byte) 4),
    PUBREC((byte) 5),
    PUBREL((byte) 6),
    PUBCOMP((byte) 7),
    SUBSCRIBE((byte) 8),
    SUBACK((byte) 9),
    UNSUBSCRIBE((byte) 10),
    UNSUBACK((byte) 11),
    PINGREQ((byte) 12),
    PINGRESP((byte) 13),
    DISCONNECT((byte) 14);

    private final byte code;

    private MessageType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    /**
     * Returns a MessageType based MessageType 4bit code. As specified in MQTT Spec.
     *
     * @param code
     * @return
     */
    public static MessageType getMessageTypeFromByte(byte code) {
        for (MessageType mt : MessageType.values()) {
            if (mt.code == code) {
                return mt;
            }
        }
        return null;
    }

    /**
     * Returns a MessageType based on the 4bit byte code as it is read from the wire.
     *
     * @param code
     * @return
     */
    public static MessageType getMessageTypeFromWireByte(byte code) {
        code >>>= 4;
        code &= 0b00001111;
        return getMessageTypeFromByte(code);
    }
}