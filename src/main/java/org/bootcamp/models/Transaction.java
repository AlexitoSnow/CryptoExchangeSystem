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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public TransactionAction getAction() {
        return action;
    }

    public CryptoCurrency getCryptoCurrency() {
        return cryptoCurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getTransactionID() {
        return transactionID;
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
                amount.setScale(2, RoundingMode.HALF_UP).toString(),
                price.setScale(2, RoundingMode.HALF_UP).toString());
    }
}
