package com.limebroker.broker.protocol.mqtt.message.header;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

import java.io.IOException;

/**
 * Represents MQTT Publish header
 *
 * @author <a href="mailto:mtaylor@redhat.com">Martyn Taylor</a>
 *
 */
public class PublishHeader extends MessageIDHeader {

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
     * Create new Publish Header with a message Id.
     *
     * @param topicName
     * @return
     */
    public static PublishHeader createPublishHeader(String topicName, int messageId) {
        PublishHeader ph = PublishHeader.createPublishHeader(topicName);
        ph.messageId = messageId;
        return ph;
    }

    /**
     * Read a Publish Header from a ByteBuf
     *
     * @param buf
     * @return
     * @throws IOException
     */
    public static PublishHeader readPublishHeader(ByteBuf buf, FixedHeader header) throws IOException {
        ByteBufInputStream is = new ByteBufInputStream(buf);
        PublishHeader ph = new PublishHeader();
        try {
            ph.topicName = is.readUTF();
            ph.messageId = readMessageId(header, buf);
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
