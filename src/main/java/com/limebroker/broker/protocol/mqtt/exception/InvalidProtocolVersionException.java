package com.limebroker.broker.protocol.mqtt.exception;

import com.limebroker.broker.LimeBrokerException;

public class InvalidProtocolVersionException extends LimeBrokerException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public InvalidProtocolVersionException(String message) {
        super(message);
    }

    public InvalidProtocolVersionException(Throwable t) {
        super(t);
    }
}
