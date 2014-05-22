package com.limebroker.broker.protocol.mqtt.message.payload;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.EOFException;
import java.io.IOException;

import javax.swing.plaf.metal.MetalIconFactory.FileIcon16;

import com.limebroker.broker.LimeBrokerException;
import com.limebroker.broker.protocol.mqtt.exception.IdentifierRejectedException;
import com.limebroker.broker.protocol.mqtt.exception.MalformedMessageException;
import com.limebroker.broker.protocol.mqtt.exception.ValidationException;
import com.limebroker.broker.protocol.mqtt.message.header.ConnectHeader;
import com.limebroker.broker.protocol.mqtt.message.header.FixedHeader;

public class ConnectPayload {

    private String clientId;
    private String willTopic;
    private String willMessage;
    private String username;
    private String password;

    private ConnectHeader connectHeader;

    private ConnectPayload() {
    }

    public ConnectPayload(ConnectHeader connectHeader, String clientId, String willTopic, String willMessage, String username,
        String password) {
        this.connectHeader = connectHeader;
        this.clientId = clientId;
        if (connectHeader.getWillFlag()) {
            this.willTopic = willTopic;
            this.willMessage = willMessage;
        }
        if (connectHeader.getUsernameFlag()) {
            this.username = username;
        }
        if (connectHeader.getPasswordFlag()) {
            this.password = password;
        }
    }

    /**
     * Decodes a ConnectPayload from ByteBuf
     * 
     * @param buf
     * @param ch
     * @param fh
     * @return
     * @throws LimeBrokerException
     * @throws IOException
     */
    public static ConnectPayload decode(ByteBuf buf, ConnectHeader ch, FixedHeader fh) throws LimeBrokerException, IOException {
        ConnectPayload cp = new ConnectPayload();
        cp.connectHeader = ch;

        try (ByteBufInputStream is = new ByteBufInputStream(buf)) {
            cp.decodeClientId(is);
            cp.decodeWill(is);
            cp.decodeUsername(is, buf, fh);
            cp.decodePassword(is, buf, fh);
        } catch (EOFException e) {
            throw new MalformedMessageException(e);
        }
        return cp;
    }

    public void encode(ByteBuf buf) throws IOException, ValidationException {
        // TODO (mtaylor) validation should be implemented on all message objects and called prior to encode.
        try (ByteBufOutputStream os = new ByteBufOutputStream(buf)) {
            os.writeUTF(clientId);
            if (connectHeader.getWillFlag()) {
                os.writeUTF(willTopic);
                os.writeUTF(willMessage);
            }
            if (connectHeader.getUsernameFlag()) {
                os.writeUTF(username);
            }
            if (connectHeader.getPasswordFlag()) {
                os.writeUTF(password);
            }
            os.flush();
        } catch (NullPointerException e) {
            throw new ValidationException("This Connect Payload is not properly formed");
        }
    }

    /**
     * Decodes the Client ID from InputStream.
     * 
     * @param is
     * @throws IOException
     * @throws IdentifierRejectedException
     */
    private void decodeClientId(ByteBufInputStream is) throws IOException, IdentifierRejectedException {
        clientId = is.readUTF();
        if ((clientId.length() > 21) || (clientId.length() < 1)) {
            throw new IdentifierRejectedException(clientId);
        }
    }

    /**
     * Decodes the Will Message and Topic
     * 
     * @param is
     * @throws IOException
     */
    private void decodeWill(ByteBufInputStream is) throws IOException {
        if (connectHeader.getWillFlag()) {
            willTopic = is.readUTF();
            willMessage = is.readUTF();
        }
    }

    /**
     * Reads username. MQTT v3.1 Specification states that the remaining length field must have precendence over the username
     * flag to support backwards compatibility woith 3.0.
     * 
     * @param is
     * @param fh
     * @throws IOException
     */
    private void decodeUsername(ByteBufInputStream is, ByteBuf buf, FixedHeader fh) throws IOException {
        if ((fh.getRemainingLength() > buf.readerIndex()) && (connectHeader.getUsernameFlag())) {
            username = is.readUTF();
        }
    }

    /**
     * Reads password. MQTT v3.1 Specification states that the remaining length field must have precendence over the password
     * flag to support backwards compatibility woith 3.0.
     * 
     * @param is
     * @param fh
     * @throws IOException
     */
    private void decodePassword(ByteBufInputStream is, ByteBuf buf, FixedHeader fh) throws IOException {
        if ((fh.getRemainingLength() > buf.readerIndex()) && (connectHeader.getPasswordFlag())) {
            password = is.readUTF();
        }
    }

    /**
     * Get the UTF-8 encoded Client ID
     * 
     * @return String
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Get the UTF-8 encoded Will Topic Name.
     * 
     * @return STring
     */
    public String getWillTopic() {
        return willTopic;
    }

    /**
     * Get the UTF-8 encoded Will Message
     * 
     * The Will Message defines the content of the message that is published to the Will Topic if the client is unexpectedly
     * disconnected.
     * 
     * @return
     */
    public String getWillMessage() {
        return willMessage;
    }

    /**
     * Get the UTF-8 encoded username
     * 
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the UTF-8 encoded password
     * 
     * @return
     */
    public String getPassword() {
        return password;
    }
}