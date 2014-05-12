package com.limebroker.broker.protocol.mqtt.header;

import static org.junit.Assert.assertEquals;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import org.junit.Test;

import com.limebroker.broker.LimeBrokerException;
import com.limebroker.broker.protocol.mqtt.message.header.ConnectHeader;
import com.limebroker.broker.protocol.mqtt.message.header.QoSLevel;

/**
 * ConnectHeader Tests
 * 
 * @author <a href="mailto:mtaylor@redhat.com">Martyn Taylor</a>
 * 
 */
public class ConnectHeaderTest {

    @Test
    public void readConnectHeaderTest() throws IOException, LimeBrokerException {
        ByteBuf buf = Unpooled.buffer();
        ByteBufOutputStream bbos = new ByteBufOutputStream(buf);
        try {
            bbos.writeUTF(ConnectHeader.PROTOCOL_NAME);
            buf.writeByte(ConnectHeader.PROTOCOL_VERSION);
            buf.writeByte((byte) 0b11111111);
            // Write the Keep Alive Timer max value = 65535
            buf.writeByte(0b11111111);
            buf.writeByte(0b11111111);
        } finally {
            bbos.close();
        }

        ConnectHeader ch = ConnectHeader.readConnectHeader(buf);
        assertEquals(ConnectHeader.PROTOCOL_NAME, ch.getProtocolName());
        assertEquals(ConnectHeader.PROTOCOL_VERSION, ch.getProtocolVersion());
        assertEquals((byte) 0b11111111, ch.getConnectFlags());
        assertEquals(65535, ch.getKeepAliveTimer());
    }

    @Test
    public void getUsernameFlagTest() {
        ConnectHeader ch = ConnectHeader.getConnectHeader((byte) 0b10000000, 0);
        assertEquals(true, ch.getUsernameFlag());

        ch = ConnectHeader.getConnectHeader((byte) 0b01111111, 0);
        assertEquals(false, ch.getUsernameFlag());
    }

    @Test
    public void getPasswordFlagTest() {
        ConnectHeader ch = ConnectHeader.getConnectHeader((byte) 0b01000000, 0);
        assertEquals(true, ch.getPasswordFlag());

        ch = ConnectHeader.getConnectHeader((byte) 0b10111111, 0);
        assertEquals(false, ch.getPasswordFlag());
    }

    @Test
    public void getWillRetainFlagTest() {
        ConnectHeader ch = ConnectHeader.getConnectHeader((byte) 0b00100000, 0);
        assertEquals(true, ch.getWillRetainFlag());

        ch = ConnectHeader.getConnectHeader((byte) 0b11011111, 0);
        assertEquals(false, ch.getWillRetainFlag());
    }

    @Test
    public void getWillFlagTest() {
        ConnectHeader ch = ConnectHeader.getConnectHeader((byte) 0b00000100, 0);
        assertEquals(true, ch.getWillFlag());

        ch = ConnectHeader.getConnectHeader((byte) 0b111111011, 0);
        assertEquals(false, ch.getWillFlag());
    }

    @Test
    public void getCleanSessionFlagTest() {
        ConnectHeader ch = ConnectHeader.getConnectHeader((byte) 0b00000010, 0);
        assertEquals(true, ch.getCleanSessionFlag());

        ch = ConnectHeader.getConnectHeader((byte) 0b11111101, 0);
        assertEquals(false, ch.getCleanSessionFlag());
    }

    @Test
    public void getWillQoS() {
        ConnectHeader ch = ConnectHeader.getConnectHeader((byte) 0b11100111, 0);
        assertEquals(QoSLevel.AT_MOST_ONCE, ch.getWillQoS());

        ch = ConnectHeader.getConnectHeader((byte) 0b11101111, 0);
        assertEquals(QoSLevel.AT_LEAST_ONCE, ch.getWillQoS());

        ch = ConnectHeader.getConnectHeader((byte) 0b11110111, 0);
        assertEquals(QoSLevel.EXACTLY_ONCE, ch.getWillQoS());

        ch = ConnectHeader.getConnectHeader((byte) 0b111111111, 0);
        assertEquals(null, ch.getWillQoS());
    }
}
