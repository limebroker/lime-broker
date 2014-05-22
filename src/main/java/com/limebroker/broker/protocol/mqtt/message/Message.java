package com.limebroker.broker.protocol.mqtt.message;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

import com.limebroker.broker.LimeBrokerException;
import com.limebroker.broker.protocol.mqtt.message.header.FixedHeader;

public class Message {

    protected FixedHeader fixedHeader;

    protected Message() {
    };

    public Message decode(ByteBuf buf) throws LimeBrokerException, IOException {
        fixedHeader = FixedHeader.decode(buf);
        switch (fixedHeader.getMessageType()) {
            case CONNECT:
                return Connect.decode(buf, fixedHeader);
            default:
                break;
        }
        return null;
    }
}