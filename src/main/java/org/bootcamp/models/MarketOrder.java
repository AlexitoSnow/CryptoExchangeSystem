package org.bootcamp.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a market order in the system. Each market order includes the type of order
 * (selling or buying), the user who placed the order, the cryptocurrency involved, the amount,
 * the price, and the date and time the order was created. A unique order ID is generated based
 * on the order type, cryptocurrency, and timestamp.
 */
public class MarketOrder {
    private final OrderType orderType;
    private final User user;
    private final CryptoCurrency cryptoCurrency;
    private final BigDecimal amount;
    private final BigDecimal price;
    private String orderID;
    private final LocalDateTime dateTime;

    /**
     * Constructor for the MarketOrder
     *
     * @param orderType the type of order (SELLING or BUY)
     * @param user the user who placed the order
     * @param cryptoCurrency the type of cryptocurrency involved in the order
     * @param amount the amount of cryptocurrency involved in the order
     * @param price the price of the cryptocurrency at the time of the order
     */
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

    /**
     * Generates a unique order ID based on the order type, cryptocurrency, and current timestamp.
     * The order ID is a combination of the order type name, cryptocurrency shorthand symbol, and timestamp.
     */
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
