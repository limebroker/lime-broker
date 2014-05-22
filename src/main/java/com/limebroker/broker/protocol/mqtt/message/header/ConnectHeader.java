package com.limebroker.broker.protocol.mqtt.message.header;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.EOFException;
import java.io.IOException;

import com.limebroker.broker.LimeBrokerException;
import com.limebroker.broker.protocol.mqtt.Util;
import com.limebroker.broker.protocol.mqtt.exception.InvalidProtocolException;
import com.limebroker.broker.protocol.mqtt.exception.InvalidProtocolVersionException;
import com.limebroker.broker.protocol.mqtt.exception.MalformedMessageException;

/**
 * Represents a variable header of a CONNECT message
 * 
 * @author <a href="mailto:mtaylor@redhat.com">Martyn Taylor</a>
 * 
 */
public class ConnectHeader extends VariableHeader {

    public static final String PROTOCOL_NAME = "MQIsdp";

    public static final byte PROTOCOL_VERSION = 3;

    private static final byte USERNAME_FLAG_MASK = (byte) 0b10000000;

    private static final byte PASSWORD_FLAG_MASK = (byte) 0b01000000;

    private static final byte WILL_RETAIN_FLAG_MASK = (byte) 0b00100000;

    private static final byte WILL_FLAG_MASK = (byte) 0b00000100;

    private static final byte CLEAN_SESSION_MASK = (byte) 0b00000010;

    private static final byte RESERVED_MASK = (byte) 0b0000001;

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
     * Creates a new MQTT V3 Connect Header based by setting each flag
     */
    public ConnectHeader(boolean usernameFlag, boolean passwordFlag, boolean willFlag, QoSLevel willQoS,
        boolean willRetainFlag, boolean cleanSession, int keepAliveTimer) {

        this.keepAliveTimer = keepAliveTimer;
        connectFlags = 0b00000000;
        if (usernameFlag) {
            connectFlags |= USERNAME_FLAG_MASK;
        }
        if (passwordFlag) {
            connectFlags |= PASSWORD_FLAG_MASK;
        }
        if (willFlag) {
            connectFlags |= WILL_FLAG_MASK;
        }
        if (willRetainFlag) {
            connectFlags |= WILL_RETAIN_FLAG_MASK;
        }
        if (cleanSession) {
            connectFlags |= CLEAN_SESSION_MASK;
        }
        connectFlags |= (willQoS.getCode() << 3);
    }

    /**
     * Creates a new MQTT V3 Connect Header based on the given paramaters.
     */
    public ConnectHeader(byte connectFlags, int keepAliveTimer) {
        protocolName = PROTOCOL_NAME;
        protocolVersion = PROTOCOL_VERSION;
        this.connectFlags = connectFlags;
        this.keepAliveTimer = keepAliveTimer;
    }

    /**
     * Read a connect header from a ByteBuf bytes and create a Connect Header object
     * 
     * @param buf
     * @return ConnectHeader
     * @throws LimeBrokerException
     * @throws IOException
     */
    public static ConnectHeader decode(ByteBuf buf) throws LimeBrokerException, IOException {
        try (ByteBufInputStream inputStream = new ByteBufInputStream(buf)) {

            String protocol = inputStream.readUTF();
            if (!protocol.equals(PROTOCOL_NAME)) {
                throw new InvalidProtocolException("Protocol Name: " + protocol + " is not valid");
            }

            byte version = buf.readByte();
            if (version != PROTOCOL_VERSION) {
                throw new InvalidProtocolVersionException("Protocol Version: " + version + " is not supported");
            }

            return new ConnectHeader(buf.readByte(), (char) buf.readShort());
        } catch (IndexOutOfBoundsException e) {
            throw new MalformedMessageException("Connect header message is malformed");
        }
    }

    /**
     * Encodes a ConnectHeader onto a ByteBuf
     * 
     * @param buf
     * @return
     * @throws LimeBrokerException
     * @throws IOException
     */
    public void encode(ByteBuf buf) throws LimeBrokerException, IOException {
        try (ByteBufOutputStream bbos = new ByteBufOutputStream(buf)) {
            bbos.writeUTF(PROTOCOL_NAME);
            bbos.writeByte(PROTOCOL_VERSION);
            bbos.writeByte(connectFlags);
            bbos.writeChar(keepAliveTimer);
            bbos.flush();
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
        return Util.getBooleanFlagFromByte(connectFlags, (byte) USERNAME_FLAG_MASK);
    }

    /**
     * Returns password flag. See MQTT Spec for more details on this flag.
     * 
     * @return
     */
    public boolean getPasswordFlag() {
        return Util.getBooleanFlagFromByte(connectFlags, (byte) PASSWORD_FLAG_MASK);
    }

    /**
     * Returns Will Retain Flag. See MQTT Spec for more details on this flag.
     * 
     * @return
     */
    public boolean getWillRetainFlag() {
        return Util.getBooleanFlagFromByte(connectFlags, (byte) WILL_RETAIN_FLAG_MASK);
    }

    /**
     * Returns the Will Flag. See MQTT Spec for more details on this flag.
     * 
     * @return
     */
    public boolean getWillFlag() {
        return Util.getBooleanFlagFromByte(connectFlags, (byte) WILL_FLAG_MASK);
    }

    /**
     * Returns the clean session flag. See MQTT Spec for more details on this flag.
     * 
     * @return
     */
    public boolean getCleanSessionFlag() {
        return Util.getBooleanFlagFromByte(connectFlags, CLEAN_SESSION_MASK);
    }

    /**
     * Get the Reserved Flag. See MQTT Spec for more details on this flag.
     * 
     * @return
     */
    public boolean getReservedFlag() {
        return Util.getBooleanFlagFromByte(connectFlags, RESERVED_MASK);
    }
}
