package com.limebroker.broker.protocol.mqtt.exception;

import com.limebroker.broker.LimeBrokerException;

public class IdentifierRejectedException extends LimeBrokerException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public IdentifierRejectedException(String clientId) {
        super(clientId + " NOT VALID");
    }
}
