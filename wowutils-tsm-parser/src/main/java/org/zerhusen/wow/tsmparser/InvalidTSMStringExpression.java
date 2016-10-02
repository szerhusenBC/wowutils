package org.zerhusen.wow.tsmparser;

/**
 * Created by stephan on 02.10.16.
 */
public class InvalidTSMStringExpression extends RuntimeException {
    public InvalidTSMStringExpression(String message, Throwable cause) {
        super(message, cause);
    }
}
