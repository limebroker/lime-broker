package com.limebroker.broker.protocol.mqtt.message;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

import com.limebroker.broker.LimeBrokerException;
import com.limebroker.broker.protocol.mqtt.message.header.ConnectHeader;
import com.limebroker.broker.protocol.mqtt.message.header.FixedHeader;
import com.limebroker.broker.protocol.mqtt.message.payload.ConnectPayload;

public class Connect extends Message {

    private ConnectHeader connectHeader;

    private ConnectPayload payload;

    public static Message decode(ByteBuf buf, FixedHeader fixedHeader) throws LimeBrokerException, IOException {
        Connect message = new Connect();
        message.fixedHeader = fixedHeader;
        message.connectHeader = ConnectHeader.decode(buf);
        message.payload = ConnectPayload.decode(buf, message.connectHeader, message.fixedHeader);
        return message;
    }

    public ConnectHeader getConnectHeader() {
        return connectHeader;
    }

    public ConnectPayload getPayload() {
        return payload;
    }
}