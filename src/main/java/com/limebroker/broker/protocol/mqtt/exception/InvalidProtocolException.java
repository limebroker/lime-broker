package com.limebroker.broker.protocol.mqtt.exception;

import com.limebroker.broker.LimeBrokerException;

public class InvalidProtocolException extends LimeBrokerException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public InvalidProtocolException(String message) {
        super(message);
    }

    public InvalidProtocolException(Throwable t) {
        super(t);
    }
}
