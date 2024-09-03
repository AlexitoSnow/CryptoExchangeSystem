package org.bootcamp.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * Represents a transaction involving a cryptocurrency.<br>
 * Each transaction includes an action
 * (such as buy, sell, or exchange), the type of cryptocurrency, the amount involved, the price,
 * and the date and time the transaction was created.<br>
 * A unique transaction ID is generated based
 * on the timestamp, action, and cryptocurrency.
 * @see TransactionAction
 * @see CryptoCurrency
 */
public class Transaction {
    private final TransactionAction action;
    private final CryptoCurrency cryptoCurrency;
    private final BigDecimal amount;
    private final BigDecimal price;
    private String transactionID;
    private final LocalDateTime dateTime;

    /**
     * Constructor for a Transaction
     *
     * @param action the action performed in the transaction (e.g., SELL, BUY, EXCHANGE)
     * @param cryptoCurrency the type of cryptocurrency involved in the transaction
     * @param amount the amount of cryptocurrency involved in the transaction
     * @param price the price of the cryptocurrency at the time of the transaction
     */
    public Transaction(TransactionAction action, CryptoCurrency cryptoCurrency, BigDecimal amount, BigDecimal price) {
        this.action = action;
        this.cryptoCurrency = cryptoCurrency;
        this.amount = amount;
        this.price = price;
        this.dateTime = LocalDateTime.now();
        generateTransactionID();
    }

    /**
     * Generates a unique transaction ID based on the current timestamp, action, and cryptocurrency.
     * The transaction ID is a combination of the epoch second, action name, and cryptocurrency shorthand symbol.
     */
    private void generateTransactionID() {
        String epochSecond = String.valueOf(dateTime.toEpochSecond(ZoneOffset.UTC));
        transactionID = epochSecond + "@" + action.name() + "@" + cryptoCurrency.getShorthandSymbol();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return Objects.equals(transactionID, that.transactionID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(transactionID);
    }

    @Override
    public String toString() {
        return String.format("%s\t %s\t %s\t %s",
                action.name(),
                cryptoCurrency.getShorthandSymbol(),
                amount.setScale(2, RoundingMode.HALF_UP),
                price.setScale(2, RoundingMode.HALF_UP));
    }
}
