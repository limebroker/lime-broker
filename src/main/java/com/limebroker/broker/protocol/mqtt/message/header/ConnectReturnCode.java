package com.limebroker.broker.protocol.mqtt.message.header;

/**
 * MessageType Enum represents MQTT Connect Return Code Type of the ConnectAck
 * Header
 * 
 * @author <a href="mailto:mtaylor@redhat.com">Martyn Taylor</a>
 * 
 */
public enum ConnectReturnCode {

    CONNECTION_ACCEPTED((byte) 0),
    CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION((byte) 1),
    CONNECTION_REFUSED_IDENTIFIER_REJECTED((byte) 2),
    CONNECTION_REFUSED_SERVER_UNAVAILABLE((byte) 3),
    CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD((byte) 4),
    CONNECTION_REFUSED_NOT_AUTHORIZED((byte) 5);

    private static ConnectReturnCode[] returnCodes;

    // Used to created static array for looking up QoS based on code. Assumes
    // codes are incremented by 1 and start at 0.
    static {
        returnCodes = new ConnectReturnCode[ConnectReturnCode.values().length];
        for (ConnectReturnCode crc : ConnectReturnCode.values()) {
            returnCodes[crc.getCode()] = crc;
        }
    }

    private final byte code;

    private ConnectReturnCode(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    /**
     * Returns a Connect Return Code from the connect return code byte of the
     * Connect Acknowledgement Header.
     * 
     * @param code
     * @return
     */
    public static ConnectReturnCode valueOf(byte code) {
        try {
            return returnCodes[code];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
}