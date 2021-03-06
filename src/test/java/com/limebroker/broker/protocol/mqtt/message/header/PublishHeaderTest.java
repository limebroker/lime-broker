package com.limebroker.broker.protocol.mqtt.message.header;

import static org.junit.Assert.assertEquals;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import org.junit.Test;

import com.limebroker.broker.LimeBrokerException;

/**
 * Publish Header Tests
 * 
 * @author <a href="mailto:mtaylor@redhat.com">Martyn Taylor</a>
 * 
 */
public class PublishHeaderTest {

    @Test
    public void readPublishHeaderTest() throws IOException, LimeBrokerException {
        ByteBuf buf = Unpooled.buffer();
        ByteBufOutputStream os = new ByteBufOutputStream(buf);
        try {
            String topicName = "My Topic Name";
            os.writeUTF(topicName);
            FixedHeader fh = new FixedHeader((byte) 0, 0);
            PublishHeader ph = PublishHeader.readPublishHeader(buf, fh);
            assertEquals(topicName, ph.getTopicName());
        } finally {
            os.close();
            buf.release();
        }
    }
}