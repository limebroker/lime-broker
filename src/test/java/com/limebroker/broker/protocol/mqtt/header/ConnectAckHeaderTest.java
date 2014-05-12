package com.limebroker.broker.protocol.mqtt.header;

import static org.junit.Assert.assertEquals;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import org.junit.Test;

import com.limebroker.broker.LimeBrokerException;
import com.limebroker.broker.protocol.mqtt.message.header.ConnectAckHeader;
import com.limebroker.broker.protocol.mqtt.message.header.ConnectReturnCode;

/**
 * Connect Ack Header Tests
 * 
 * @author <a href="mailto:mtaylor@redhat.com">Martyn Taylor</a>
 * 
 */
public class ConnectAckHeaderTest {

    @Test
    public void readConnectAckHeaderTest() throws IOException,
            LimeBrokerException {
        ByteBuf buf = Unpooled.buffer();
        try {
            buf.writeByte(0b00000000);
            ConnectAckHeader cah = ConnectAckHeader.readConnectAckHeader(buf);
            assertEquals(ConnectReturnCode.CONNECTION_ACCEPTED,
                    cah.getConnectReturnCode());
        } finally {
            buf.release();
        }
    }
}