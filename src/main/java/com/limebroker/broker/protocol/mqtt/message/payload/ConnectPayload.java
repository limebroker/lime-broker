package com.limebroker.broker.protocol.mqtt.message.payload;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

import java.io.IOException;

import com.limebroker.broker.protocol.mqtt.exception.IdentifierRejectedException;
import com.limebroker.broker.protocol.mqtt.exception.MalformedMessageException;
import com.limebroker.broker.protocol.mqtt.message.header.ConnectHeader;
import com.limebroker.broker.protocol.mqtt.message.header.FixedHeader;

public class ConnectPayload {

    private String clientId;
    private String willTopic;
    private String willMessage;
    private String username;
    private String password;

    private FixedHeader fixedHeader;
    private ConnectHeader connectHeader;

    private ConnectPayload() {
    }

    public static ConnectPayload readConnectPayload(ByteBuf buf, ConnectHeader ch, FixedHeader fh)
        throws IdentifierRejectedException, MalformedMessageException, IOException {
        ConnectPayload cp = new ConnectPayload();
        cp.fixedHeader = fh;
        cp.connectHeader = ch;
        cp.read(buf);
        return cp;
    }

    private void read(ByteBuf buf) throws IdentifierRejectedException, MalformedMessageException, IOException {
        ByteBufInputStream is = new ByteBufInputStream(buf);
        try {
            clientId = is.readUTF();
            if ((clientId.length() > 21) || (clientId.length() < 1)) {
                throw new IdentifierRejectedException(clientId);
            }

            if (connectHeader.getWillFlag()) {
                willTopic = is.readUTF();
                willMessage = is.readUTF();
            }

            if ((fixedHeader.getRemainingLength() > buf.readerIndex()) && (connectHeader.getUsernameFlag())) {
                username = is.readUTF();
            }

            if ((fixedHeader.getRemainingLength() > buf.readerIndex()) && (connectHeader.getPasswordFlag())) {
                password = is.readUTF();
            }
        } catch (IOException e) {
            throw new MalformedMessageException(e);
        } finally {
            is.close();
        }
    }

    public String getClientId() {
        return clientId;
    }

    public String getWillTopic() {
        return willTopic;
    }

    public String getWillMessage() {
        return willMessage;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
