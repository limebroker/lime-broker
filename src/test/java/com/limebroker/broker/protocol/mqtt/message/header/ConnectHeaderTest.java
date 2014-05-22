package com.limebroker.broker.protocol.mqtt.message.header;

import static org.junit.Assert.assertEquals;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import org.junit.After;
import org.junit.Test;

import com.limebroker.broker.LimeBrokerException;
import com.limebroker.broker.protocol.mqtt.exception.InvalidProtocolException;
import com.limebroker.broker.protocol.mqtt.exception.InvalidProtocolVersionException;
import com.limebroker.broker.protocol.mqtt.exception.MalformedMessageException;

/**
 * ConnectHeader Tests
 * 
 * @author <a href="mailto:mtaylor@redhat.com">Martyn Taylor</a>
 * 
 */
public class ConnectHeaderTest {

    ByteBuf buf;
    ByteBufOutputStream bbos;
    ByteBufInputStream bbis;

    @After
    public void tearDown() throws IOException {
        if (buf != null) {
            buf.release();
        }
        if (bbos != null) {
            bbos.close();
        }
        if (bbis != null) {
            bbis.close();
        }
    }

    /**
     * Since we are storing flags in a single byte, we ensure that the byte is created correctly.
     * 
     * @throws IOException
     * @throws LimeBrokerException
     */
    @Test
    public void flagConstructorTest() throws IOException, LimeBrokerException {
        ConnectHeader header = new ConnectHeader(true, true, true, QoSLevel.AT_LEAST_ONCE, true, true, 0);
        assertEquals((byte) 0b11101110, header.getConnectFlags());

        header = new ConnectHeader(false, false, false, QoSLevel.AT_MOST_ONCE, false, false, 0);
        assertEquals((byte) 0b00000000, header.getConnectFlags());
    }

    @Test
    public void decodeConnectHeaderTest() throws IOException, LimeBrokerException {
        createBuffers(true);
        bbos.writeUTF(ConnectHeader.PROTOCOL_NAME);
        buf.writeByte(ConnectHeader.PROTOCOL_VERSION);
        buf.writeByte((byte) 0b11111111);
        // Write the Keep Alive Timer max value = 65535
        buf.writeByte(0b11111111);
        buf.writeByte(0b11111111);

        ConnectHeader ch = ConnectHeader.decode(buf);
        assertEquals(ConnectHeader.PROTOCOL_NAME, ch.getProtocolName());
        assertEquals(ConnectHeader.PROTOCOL_VERSION, ch.getProtocolVersion());
        assertEquals((byte) 0b11111111, ch.getConnectFlags());
        assertEquals(65535, ch.getKeepAliveTimer());
    }

    @Test(expected = InvalidProtocolException.class)
    public void decodeConnectHeaderIncorrectProtocolNameTest() throws IOException, LimeBrokerException {
        createBuffers(true);
        bbos.writeUTF("FooBar");
        buf.writeByte(ConnectHeader.PROTOCOL_VERSION);
        ConnectHeader.decode(buf);
    }

    @Test(expected = InvalidProtocolVersionException.class)
    public void decodeConnectHeaderIncorrectProtocolVersionTest() throws IOException, LimeBrokerException {
        createBuffers(true);
        bbos.writeUTF(ConnectHeader.PROTOCOL_NAME);
        buf.writeByte(2);
        ConnectHeader.decode(buf);
    }

    @Test(expected = MalformedMessageException.class)
    public void decodeConnectHeaderMalformedHeaderTest() throws IOException, LimeBrokerException {
        createBuffers(true);
        bbos.writeUTF(ConnectHeader.PROTOCOL_NAME);
        buf.writeByte(ConnectHeader.PROTOCOL_VERSION);
        buf.writeByte((byte) 0b11111111);
        buf.writeByte(0b11111111);
        // Missing Last Byte
        ConnectHeader.decode(buf);
    }

    @Test
    public void encodeConnectHeaderTest() throws LimeBrokerException, IOException {
        createBuffers(false);
        ConnectHeader header = new ConnectHeader(true, true, true, QoSLevel.AT_LEAST_ONCE, true, true, 0);
        header.encode(buf);
        bbis = new ByteBufInputStream(buf);
        assertEquals(bbis.readUTF(), ConnectHeader.PROTOCOL_NAME);
        assertEquals(bbis.readByte(), ConnectHeader.PROTOCOL_VERSION);
        assertEquals(bbis.readByte(), header.getConnectFlags());
        assertEquals((int) bbis.readChar(), header.getKeepAliveTimer());
    }

    @Test
    public void getUsernameFlagTest() {
        ConnectHeader ch = new ConnectHeader((byte) 0b10000000, 0);
        assertEquals(true, ch.getUsernameFlag());

        ch = new ConnectHeader((byte) 0b01111111, 0);
        assertEquals(false, ch.getUsernameFlag());
    }

    @Test
    public void getPasswordFlagTest() {
        ConnectHeader ch = new ConnectHeader((byte) 0b01000000, 0);
        assertEquals(true, ch.getPasswordFlag());

        ch = new ConnectHeader((byte) 0b10111111, 0);
        assertEquals(false, ch.getPasswordFlag());
    }

    @Test
    public void getWillRetainFlagTest() {
        ConnectHeader ch = new ConnectHeader((byte) 0b00100000, 0);
        assertEquals(true, ch.getWillRetainFlag());

        ch = new ConnectHeader((byte) 0b11011111, 0);
        assertEquals(false, ch.getWillRetainFlag());
    }

    @Test
    public void getWillFlagTest() {
        ConnectHeader ch = new ConnectHeader((byte) 0b00000100, 0);
        assertEquals(true, ch.getWillFlag());

        ch = new ConnectHeader((byte) 0b111111011, 0);
        assertEquals(false, ch.getWillFlag());
    }

    @Test
    public void getCleanSessionFlagTest() {
        ConnectHeader ch = new ConnectHeader((byte) 0b00000010, 0);
        assertEquals(true, ch.getCleanSessionFlag());

        ch = new ConnectHeader((byte) 0b11111101, 0);
        assertEquals(false, ch.getCleanSessionFlag());
    }

    @Test
    public void getWillQoS() {
        ConnectHeader ch = new ConnectHeader((byte) 0b11100111, 0);
        assertEquals(QoSLevel.AT_MOST_ONCE, ch.getWillQoS());

        ch = new ConnectHeader((byte) 0b11101111, 0);
        assertEquals(QoSLevel.AT_LEAST_ONCE, ch.getWillQoS());

        ch = new ConnectHeader((byte) 0b11110111, 0);
        assertEquals(QoSLevel.EXACTLY_ONCE, ch.getWillQoS());

        ch = new ConnectHeader((byte) 0b111111111, 0);
        assertEquals(null, ch.getWillQoS());
    }

    private void createBuffers(boolean outputStream) {
        buf = Unpooled.buffer();
        if (outputStream) {
            bbos = new ByteBufOutputStream(buf);
        }
    }
}
