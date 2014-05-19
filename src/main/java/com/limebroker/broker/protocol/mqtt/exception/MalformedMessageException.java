package com.limebroker.broker.protocol.mqtt.exception;

import com.limebroker.broker.LimeBrokerException;

public class MalformedMessageException extends LimeBrokerException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public MalformedMessageException(String message) {
        super(message);
    }

    public MalformedMessageException(Throwable t) {
        super(t);
    }
}
