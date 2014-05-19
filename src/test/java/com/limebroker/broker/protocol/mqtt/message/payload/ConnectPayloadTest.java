package com.limebroker.broker.protocol.mqtt.message.payload;

import static org.junit.Assert.assertEquals;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.limebroker.broker.LimeBrokerException;
import com.limebroker.broker.protocol.mqtt.message.header.ConnectHeader;
import com.limebroker.broker.protocol.mqtt.message.header.FixedHeader;

public class ConnectPayloadTest {

    private ByteBuf buf;
    private ByteBuf header;
    private ByteBuf payload;

    private ByteBufOutputStream bbos;

    private String clientId;
    private String willTopic;
    private String willMessage;
    private String username;
    private String password;

    @Before
    public void setUp() throws Exception {
        header = Unpooled.buffer();
        payload = Unpooled.buffer();
        bbos = new ByteBufOutputStream(payload);

        clientId = "ClientID12345";
        willTopic = "WillTopic12345";
        willMessage = "WillMessage12345";
        username = "Username12345";
        password = "Password12345";
    }

    @After
    public void tearDown() throws Exception {
        bbos.close();
        header.release();
        payload.release();
    }

    /**
     * Tests that ConnectPayload with all flags and correct remainingLength and entrys is read correctly.
     *
     * @throws Exception
     */
    @Test
    public void readConnectPayloadWithAllFlagsTest() throws Exception {
        ConnectHeader connectHeader = ConnectHeader.getConnectHeader((byte) 0b11001110, 1);
        writeStringsToBuf(clientId, willTopic, willMessage, username, password);

        FixedHeader fh = writeFixedHeader((byte) 0b00010000);

        buf = Unpooled.wrappedBuffer(header, payload);
        ConnectPayload p = ConnectPayload.readConnectPayload(payload, connectHeader, fh);
        assertEquals(clientId, p.getClientId());
        assertEquals(willTopic, p.getWillTopic());
        assertEquals(willMessage, p.getWillMessage());
        assertEquals(username, p.getUsername());
        assertEquals(password, p.getPassword());
    }

    /**
     * Tests case outlined in v3.1 spec:
     *
     * Note that, for compatibility with the original MQTT V3 specification, the Remaining Length field from the fixed header
     * takes precedence over the User Name flag. Server implementations must allow for the possibility that the User Name flag
     * is set, but the User Name string is missing. This is valid, and connections should be allowed to continue.
     *
     * @throws Exception
     */
    @Test
    public void readConnectPayloadWithIncorrectRemainingLengthOnUsernameTest() throws Exception {
        ConnectHeader connectHeader = ConnectHeader.getConnectHeader((byte) 0b11001110, 1);
        writeStringsToBuf(clientId, willTopic, willMessage);
        FixedHeader fh = writeFixedHeader((byte) 0b00010000);

        buf = Unpooled.wrappedBuffer(header, payload);
        ConnectPayload p = ConnectPayload.readConnectPayload(payload, connectHeader, fh);
        assertEquals(clientId, p.getClientId());
        assertEquals(willTopic, p.getWillTopic());
        assertEquals(willMessage, p.getWillMessage());
        assertEquals(null, p.getUsername());
    }

    /**
     * Tests case outlined in v3.1 spec:
     *
     * Note that, for compatibility with the original MQTT V3 specification, the Remaining Length field from the fixed header
     * takes precedence over the Password flag. Server implementations must allow for the possibility that the Password flag is
     * set, but the Password string is missing. This is valid, and connections should be allowed to continue.
     *
     * @throws Exception
     */
    @Test
    public void readConnectPayloadWithIncorrectRemainingLengthOnPasswordTest() throws Exception {
        // Set Use
        ConnectHeader connectHeader = ConnectHeader.getConnectHeader((byte) 0b11001110, 1);
        writeStringsToBuf(clientId, willTopic, willMessage, username);
        FixedHeader fh = writeFixedHeader((byte) 0b00010000);

        buf = Unpooled.wrappedBuffer(header, payload);
        ConnectPayload p = ConnectPayload.readConnectPayload(payload, connectHeader, fh);
        assertEquals(clientId, p.getClientId());
        assertEquals(willTopic, p.getWillTopic());
        assertEquals(willMessage, p.getWillMessage());
        assertEquals(username, p.getUsername());
        assertEquals(null, p.getPassword());
    }

    private void writeStringsToBuf(String... strings) throws IOException {
        for (String s : strings) {
            bbos.writeUTF(s);
        }
        bbos.flush();
    }

    private FixedHeader writeFixedHeader(byte flags) throws LimeBrokerException {
        FixedHeader h = FixedHeader.createFixedHeader(flags, payload.readableBytes());
        h.write(header);
        return h;
    }
}
