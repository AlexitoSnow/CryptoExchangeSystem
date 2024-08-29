package org.bootcamp.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class Transaction {
    private final TransactionAction action;
    private final CryptoCurrency cryptoCurrency;
    private final BigDecimal amount;
    private final BigDecimal price;
    private String transactionID;
    private final User seller;
    private final User buyer;

    public Transaction(TransactionAction action, CryptoCurrency cryptoCurrency, BigDecimal amount, BigDecimal price, User seller, User buyer) {
        assert (action != null && cryptoCurrency != null && amount != null && price != null && seller != null && buyer != null);
        this.action = action;
        this.cryptoCurrency = cryptoCurrency;
        this.amount = amount;
        this.price = price;
        this.seller = seller;
        this.buyer = buyer;
        generateTransactionID();
    }

    private void generateTransactionID() {
        String epochSecond = String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        transactionID = seller.getUserID() + "@" + epochSecond + "@" + buyer.getUserID();
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

    public User getSeller() {
        return seller;
    }

    public User getBuyer() {
        return buyer;
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
        return "Transaction{" +
                "action=" + action +
                ", cryptoCurrency=" + cryptoCurrency.getDisplayName() +
                ", amount=" + amount +
                ", price=" + price +
                ", transactionID='" + transactionID + '\'' +
                ", seller=" + seller.getUserID() +
                ", buyer=" + buyer.getUserID() +
                '}';
    }
}
