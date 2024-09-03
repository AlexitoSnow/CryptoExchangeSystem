package org.bootcamp.services;

/**
 * Exception thrown when there is an issue with the account service operations.
 */
public class AccountServiceException extends Exception {

    /**
     * Constructs a new AccountServiceException with the specified detail message.
     *
     * @param message the detail message
     */
    public AccountServiceException(String message) {
        super(message);
    }
}
