package com.limebroker.broker.protocol.mqtt;

/**
 * Utils for MQTT Protocol
 * 
 * @author <a href="mailto:mtaylor@redhat.com">Martyn Taylor</a>
 * 
 */
public class Util {
    public static boolean getBooleanFlagFromByte(byte b, byte mask) {
        return (b & mask) != 0;
    }

    public static int unsignedShortToInt(short s) {
        return 0xFFFF & s;
    }
}
