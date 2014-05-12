package com.limebroker.broker.protocol.mqtt.header;

import static org.junit.Assert.assertEquals;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import org.junit.Test;

import com.limebroker.broker.LimeBrokerException;
import com.limebroker.broker.protocol.mqtt.Util;

/**
 * Message ID Header Tests
 * 
 * @author <a href="mailto:mtaylor@redhat.com">Martyn Taylor</a>
 * 
 */
public class MessageIdHeaderTest {

    @Test
    public void readMessageIdHeaderTest() throws IOException,
            LimeBrokerException {
        ByteBuf buf = Unpooled.buffer();
        try {
            buf.writeShort(52);
            FixedHeader fh = FixedHeader.createFixedHeader((byte) 3, 0);
            MessageIDHeader midh = MessageIDHeader.readMessageIDHeader(buf, fh);
            assertEquals(52, midh.getMessageId());
        } finally {
            buf.release();
        }
    }
}