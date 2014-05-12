package com.limebroker.broker.protocol.mqtt.header;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.limebroker.broker.protocol.mqtt.message.header.ConnectReturnCode;

/**
 * Connect Return Code Tests
 * 
 * @author <a href="mailto:mtaylor@redhat.com">Martyn Taylor</a>
 * 
 */
public class ConnectReturnCodeTest {

    @Test
    public void valueOfTest() {
        assertEquals(ConnectReturnCode.CONNECTION_ACCEPTED,
                ConnectReturnCode.valueOf((byte) 0));
        assertEquals(
                ConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION,
                ConnectReturnCode.valueOf((byte) 1));
        assertEquals(ConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED,
                ConnectReturnCode.valueOf((byte) 2));
        assertEquals(ConnectReturnCode.CONNECTION_REFUSED_SERVER_UNAVAILABLE,
                ConnectReturnCode.valueOf((byte) 3));
        assertEquals(
                ConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD,
                ConnectReturnCode.valueOf((byte) 4));
        assertEquals(ConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED,
                ConnectReturnCode.valueOf((byte) 5));
        assertEquals(null, ConnectReturnCode.valueOf((byte) -1));
    }
}