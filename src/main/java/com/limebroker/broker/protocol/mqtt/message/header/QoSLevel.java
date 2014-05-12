package com.limebroker.broker.protocol.mqtt.message.header;

/**
 * Represents Quality of Service flag in MQTT Header
 * 
 * @author <a href="mailto:mtaylor@redhat.com">Martyn Taylor</a>
 * 
 */
public enum QoSLevel {
    AT_MOST_ONCE((byte) 0), AT_LEAST_ONCE((byte) 1), EXACTLY_ONCE((byte) 2);

    private static QoSLevel[] qos;

    // Used to created static array for looking up QoS based on code. Assumes
    // codes are incremented by 1 and start at 0.
    static {
        qos = new QoSLevel[QoSLevel.values().length];
        for (QoSLevel q : QoSLevel.values()) {
            qos[q.getCode()] = q;
        }
    }

    private final byte code;

    private QoSLevel(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    /**
     * Returns a QoS based on QoS Value outlined in MQTT Spec wire.
     * 
     * @param code
     * @return
     */
    public static QoSLevel valueOf(byte code) {
        try {
            return qos[code];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Returns a QoS enum from bit 6 and 7 of the byte (Big Endian) as it would
     * be received over the wire.
     * 
     * @param code
     * @return
     */
    public static QoSLevel getQoSFromByte(byte code) {
        code >>>= 1;
        code &= 0b00000011;
        return valueOf(code);
    }

    /**
     * Returns a QoS enum from bit 3 and 4 of the connect header byte (Big
     * Endian) as it would be received over the wire.
     * 
     * @param code
     * @return
     */
    public static QoSLevel getWillQoSFromByte(byte code) {
        code >>>= 3;
        code &= 0b00000011;
        return valueOf(code);
    }
}
