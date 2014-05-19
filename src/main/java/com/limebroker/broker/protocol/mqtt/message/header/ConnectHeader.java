package com.limebroker.broker.protocol.mqtt.message.header;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import com.limebroker.broker.LimeBrokerException;
import com.limebroker.broker.protocol.mqtt.Util;

/**
 * Represents a variable header of a CONNECT message
 * 
 * @author <a href="mailto:mtaylor@redhat.com">Martyn Taylor</a>
 * 
 */
public class ConnectHeader extends VariableHeader {

    public static final String PROTOCOL_NAME = "MQIsdp";

    public static final byte PROTOCOL_VERSION = 3;

    // The protocol name in UTF-8 encoding.
    private String protocolName;

    // The MQTT protocol version
    private byte protocolVersion;

    // Contains flags for Username, Password, CleanSession, Will (Flag, QoS,
    // Retain)
    private byte connectFlags;

    // Represents the Keep Alive 2 byte header set in Connect Header.
    private int keepAliveTimer;

    /**
     * Creates a new Connect Header based on the given paramaters.
     * 
     * @param connectFlags
     * @param keepAliveTimer
     * @return
     */
    public static ConnectHeader getConnectHeader(byte connectFlags, int keepAliveTimer) {
        ConnectHeader ch = new ConnectHeader();
        ch.protocolName = PROTOCOL_NAME;
        ch.protocolVersion = PROTOCOL_VERSION;
        ch.connectFlags = connectFlags;
        ch.keepAliveTimer = keepAliveTimer;
        return ch;
    }

    /**
     * Read a connect header from a ByteBuf bytes and create a Connect Header object
     * 
     * @param buf
     * @return
     * @throws LimeBrokerException
     * @throws IOException
     */
    public static ConnectHeader readConnectHeader(ByteBuf buf) throws LimeBrokerException, IOException {
        ByteBufInputStream inputStream = new ByteBufInputStream(buf);
        try {
            ConnectHeader ch = new ConnectHeader();
            ch.protocolName = inputStream.readUTF();
            ch.protocolVersion = buf.readByte();
            ch.connectFlags = buf.readByte();
            ch.keepAliveTimer = Util.unsignedShortToInt(buf.readShort());
            return ch;
        } catch (IOException e) {
            throw new LimeBrokerException("Unable to read protocol name from buffer");
        } finally {
            inputStream.close();
        }
    }

    /**
     * Get the Connect message Keep Alive Timer for this connect message.
     * 
     * @return
     */
    public int getKeepAliveTimer() {
        return keepAliveTimer;
    }

    /**
     * Get the Protocol Name for this Connect Message. Should always be MQIsdp.
     * 
     * @return
     */
    public String getProtocolName() {
        return protocolName;
    }

    /**
     * Get the protocol version of this connect message.
     * 
     * @return
     */
    public byte getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * Get the all connect flags as byte for this connect header.
     * 
     * @return
     */
    public byte getConnectFlags() {
        return connectFlags;
    }

    /**
     * Get the will quality of service level for this connect message. See MQTT Spec for more details on this flag.
     * 
     * @return
     */
    public QoSLevel getWillQoS() {
        return QoSLevel.getWillQoSFromByte(connectFlags);
    }

    /**
     * Gets the username flag. See MQTT Spec for more details on this flag.
     * 
     * @return
     */
    public boolean getUsernameFlag() {
        return Util.getBooleanFlagFromByte(connectFlags, (byte) 0b10000000);
    }

    /**
     * Returns password flag. See MQTT Spec for more details on this flag.
     * 
     * @return
     */
    public boolean getPasswordFlag() {
        return Util.getBooleanFlagFromByte(connectFlags, (byte) 0b01000000);
    }

    /**
     * Returns Will Retain Flag. See MQTT Spec for more details on this flag.
     * 
     * @return
     */
    public boolean getWillRetainFlag() {
        return Util.getBooleanFlagFromByte(connectFlags, (byte) 0b00100000);
    }

    /**
     * Returns the Will Flag. See MQTT Spec for more details on this flag.
     * 
     * @return
     */
    public boolean getWillFlag() {
        return Util.getBooleanFlagFromByte(connectFlags, (byte) 0b00000100);
    }

    /**
     * Returns the clean session flag. See MQTT Spec for more details on this flag.
     * 
     * @return
     */
    public boolean getCleanSessionFlag() {
        return Util.getBooleanFlagFromByte(connectFlags, (byte) 0b00000010);
    }

    /**
     * Get the Reserved Flag. See MQTT Spec for more details on this flag.
     * 
     * @return
     */
    public boolean getReservedFlag() {
        return Util.getBooleanFlagFromByte(connectFlags, (byte) 0b00000001);
    }

    /**
     * Writes this Connect Header out to ByteBuf
     * 
     * @param buf
     * @throws IOException
     */
    public void write(ByteBuf buf) throws IOException {
        ByteBufOutputStream bbos = new ByteBufOutputStream(buf);
        try {
            bbos.writeUTF(PROTOCOL_NAME);
            buf.writeByte(PROTOCOL_VERSION);
            buf.writeByte(connectFlags);
            buf.writeChar((char) keepAliveTimer);
            bbos.flush();
        } finally {
            bbos.close();
        }
    }
}
