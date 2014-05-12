package com.limebroker.broker.protocol.mqtt.message.header;

import io.netty.buffer.ByteBuf;

import com.limebroker.broker.protocol.mqtt.Util;

/**
 * Represents any MQTT Variable Header that requires Message ID. This list
 * includes: Publish, PubishAck, PublshRec, PublistComp, Subscribe,
 * SubscribeAck, Unsubscribe, UnsubscribeAck
 * 
 * @author <a href="mailto:mtaylor@redhat.com">Martyn Taylor</a>
 * 
 */
public class MessageIDHeader extends VariableHeader {

    public static final int NO_MESSAGE_ID = -1;

    protected int messageId;

    protected MessageIDHeader() {
    }

    /**
     * Create new Message ID Header.
     * 
     * @param topicName
     * @return
     */
    public static MessageIDHeader createPublishHeader(int messageId) {
        MessageIDHeader midh = new MessageIDHeader();
        midh.messageId = messageId;
        return midh;
    }

    /**
     * Read a Message ID Header from a ByteBuf
     * 
     * @param buf
     * @return
     */
    public static MessageIDHeader readMessageIDHeader(ByteBuf buf,
            FixedHeader header) {
        MessageIDHeader midh = new MessageIDHeader();
        midh.messageId = readMessageId(header, buf);
        return midh;
    }

    /**
     * Retrieve the message ID.
     * 
     * @return
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * Read the message ID from the wire if the appropriate flags are set in the
     * header other wise return the empty message ID value.
     * 
     * @param header
     * @param buf
     * @return
     */
    protected static int readMessageId(FixedHeader header, ByteBuf buf) {
        if ((header.getQoSLevel() == QoSLevel.AT_LEAST_ONCE)
                || (header.getQoSLevel() == QoSLevel.EXACTLY_ONCE)) {
            return Util.unsignedShortToInt(buf.readShort());
        }
        return NO_MESSAGE_ID;
    }
}
