package com.limebroker.broker.protocol.mqtt.message.header;

import static org.junit.Assert.assertEquals;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import org.junit.Test;

import com.limebroker.broker.LimeBrokerException;

/**
 * Message ID Header Tests
 * 
 * @author <a href="mailto:mtaylor@redhat.com">Martyn Taylor</a>
 * 
 */
public class MessageIdHeaderTest {

    @Test
    public void readMessageIdHeaderTest() throws IOException, LimeBrokerException {
        ByteBuf buf = Unpooled.buffer();
        try {
            buf.writeShort(52);
            FixedHeader fh = new FixedHeader((byte) 3, 0);
            MessageIDHeader midh = MessageIDHeader.readMessageIDHeader(buf, fh);
            assertEquals(52, midh.getMessageId());
        } finally {
            buf.release();
        }
    }
}