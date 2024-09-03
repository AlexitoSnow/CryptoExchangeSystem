package org.bootcamp.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class MarketOrder {
    private final OrderType orderType;
    private final User user;
    private final CryptoCurrency cryptoCurrency;
    private final BigDecimal amount;
    private final BigDecimal price;
    private String orderID;
    private final LocalDateTime dateTime;

    public MarketOrder(OrderType orderType, User user, CryptoCurrency cryptoCurrency, BigDecimal amount, BigDecimal price) {
        this.orderType = orderType;
        this.user = user;
        this.cryptoCurrency = cryptoCurrency;
        this.amount = amount;
        this.price = price;
        dateTime = LocalDateTime.now();
        generateOrderID();
    }

    public String getOrderID() {
        return orderID;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public User getUser() {
        return user;
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

    private void generateOrderID() {
        orderID = orderType.name() + "@" + cryptoCurrency.getShorthandSymbol() + "@" + dateTime.toString();
    }

    @Override
    public String toString() {
        return "Order " + orderID +
                "\nCryptoCurrency: " + amount + cryptoCurrency.getShorthandSymbol() +
                "\nPrice: $" + price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarketOrder that)) return false;
        return Objects.equals(orderID, that.orderID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderID);
    }
}
