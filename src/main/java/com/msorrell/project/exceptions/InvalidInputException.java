package com.msorrell.project.exceptions;

/**
 * Exception that is thrown when invalid input is received.
 */
public class InvalidInputException extends RuntimeException {

    /**
     * Constructor.
     * @param message Message
     */
    public InvalidInputException(final String message) {
        super(message);
    }
}
