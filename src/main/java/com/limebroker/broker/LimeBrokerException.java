package com.limebroker.broker;

/**
 * LimeBrokerException
 *
 * @author Martyn Taylor <mtaylor@redhat.com>
 *
 */
public class LimeBrokerException extends Exception {
    private static final long serialVersionUID = 1L;

    public LimeBrokerException(String msg) {
        super(msg);
    }

    public LimeBrokerException(Throwable t) {
        super(t);
    }
}
