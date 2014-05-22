package com.limebroker.broker.protocol.mqtt.exception;

import com.limebroker.broker.LimeBrokerException;

public class ValidationException extends LimeBrokerException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable t) {
        super(t);
    }
}
