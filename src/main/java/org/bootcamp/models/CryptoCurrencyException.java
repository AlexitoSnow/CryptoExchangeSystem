package org.bootcamp.models;

/**
 * Exception thrown to indicate an error related to cryptocurrency operations.<br>
 * This exception is used to signal issues such as invalid transactions or operations
 * involving cryptocurrencies.
 */
public class CryptoCurrencyException extends Exception {

    /**
     * Constructor for CryptoCurrencyException
     *
     * @param message the detail message explaining the reason for the exception
     */
    public CryptoCurrencyException(String message) {
        super(message);
    }
}
