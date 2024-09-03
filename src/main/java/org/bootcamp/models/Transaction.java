package org.bootcamp.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class Transaction {
    private final TransactionAction action;
    private final CryptoCurrency cryptoCurrency;
    private final BigDecimal amount;
    private final BigDecimal price;
    private String transactionID;
    private final LocalDateTime dateTime;

    public Transaction(TransactionAction action, CryptoCurrency cryptoCurrency, BigDecimal amount, BigDecimal price) {
        this.action = action;
        this.cryptoCurrency = cryptoCurrency;
        this.amount = amount;
        this.price = price;
        this.dateTime = LocalDateTime.now();
        generateTransactionID();
    }

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
