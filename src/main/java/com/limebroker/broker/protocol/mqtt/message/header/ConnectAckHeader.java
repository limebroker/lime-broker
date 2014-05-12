package com.limebroker.broker.protocol.mqtt.message.header;

import io.netty.buffer.ByteBuf;

/**
 * Represents MQTT Connect Ack Header
 * 
 * @author <a href="mailto:mtaylor@redhat.com">Martyn Taylor</a>
 * 
 */
public class ConnectAckHeader extends VariableHeader {

    private byte returnCode;

    private ConnectAckHeader() {
    }

    /**
     * Create new Connect Ack Header.
     * 
     * @param returnCode
     * @return
     */
    public static ConnectAckHeader createConnectAckHeader(
            ConnectReturnCode returnCode) {
        ConnectAckHeader cah = new ConnectAckHeader();
        cah.returnCode = returnCode.getCode();
        return cah;
    }

    /**
     * Read a Connect Ack Header from a ByteBuf
     * 
     * @param buf
     * @return
     */
    public static ConnectAckHeader readConnectAckHeader(ByteBuf buf) {
        ConnectAckHeader cah = new ConnectAckHeader();
        cah.returnCode = buf.readByte();
        return cah;
    }

    /**
     * Retried the Connect Return Code for this message.
     * 
     * @return
     */
    public ConnectReturnCode getConnectReturnCode() {
        return ConnectReturnCode.valueOf(returnCode);
    }
}
