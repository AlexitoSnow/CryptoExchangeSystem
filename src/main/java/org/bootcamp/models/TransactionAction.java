package org.bootcamp.models;

/**
 * Enum representing the possible actions that can be performed in a transaction.
 * The actions include:
 * <ul>
 *   <li>SELL - Selling cryptocurrencies</li>
 *   <li>BUY - Buying cryptocurrencies</li>
 *   <li>EXCHANGE - Buying cryptocurrencies from the Exchange</li>
 * </ul>
 * @see Transaction
 * @see CryptoCurrency
 */
public enum TransactionAction {
    SELL, BUY, EXCHANGE
}
