package com.limebroker.broker.protocol.mqtt.header;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

/**
 * Represents MQTT Publish header
 * 
 * @author <a href="mailto:mtaylor@redhat.com">Martyn Taylor</a>
 * 
 */
public class PublishHeader extends VariableHeader {

    private String topicName;

    private PublishHeader() {
    }

    /**
     * Create new Publish Header.
     * 
     * @param topicName
     * @return
     */
    public static PublishHeader createPublishHeader(String topicName) {
        PublishHeader ph = new PublishHeader();
        ph.topicName = topicName;
        return ph;
    }

    /**
     * Read a Publish Header from a ByteBuf
     * 
     * @param buf
     * @return
     * @throws IOException
     */
    public static PublishHeader readPublishHeader(ByteBuf buf)
            throws IOException {
        ByteBufInputStream is = new ByteBufInputStream(buf);
        PublishHeader ph = new PublishHeader();
        try {
            ph.topicName = is.readUTF();
        } finally {
            is.close();
        }
        return ph;
    }

    /**
     * Retrieve the topic name the publish message.
     * 
     * @return
     */
    public String getTopicName() {
        return topicName;
    }
}
