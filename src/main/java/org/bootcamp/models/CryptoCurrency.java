package org.bootcamp.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Represents a cryptocurrency with a display name, shorthand symbol, original value, and current value.<br>
 * Each cryptocurrency has a unique ID generated based on its shorthand symbol.
 * @see org.bootcamp.services.ExchangeService
 * @see org.bootcamp.services.TradingService
 */
public final class CryptoCurrency {
    private String cryptoID;
    private final String displayName;
    private final String shorthandSymbol;
    private BigDecimal currentValue;
    private final BigDecimal originalValue;

    /**
     * Constructs a new CryptoCurrency with the specified display name, shorthand symbol, and original value.
     * Initializes the current value to the original value and generates a unique cryptocurrency ID.
     *
     * @param displayName the display name of the cryptocurrency
     * @param shorthandSymbol the shorthand symbol of the cryptocurrency
     * @param originalValue the original value of the cryptocurrency
     */
    public CryptoCurrency(String displayName, String shorthandSymbol, BigDecimal originalValue) {
        this.displayName = displayName;
        this.originalValue = originalValue;
        this.currentValue = originalValue;
        this.shorthandSymbol = shorthandSymbol;
        generateCryptoID();
    }

    /**
     * Generates a unique cryptocurrency ID based on the shorthand symbol and its hash code.
     */
    private void generateCryptoID() {
        cryptoID = shorthandSymbol + '@' + shorthandSymbol.hashCode();
    }

    public String getDisplayName() {
        return displayName;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public BigDecimal getOriginalValue() {
        return originalValue;
    }

    public String getShorthandSymbol() {
        return shorthandSymbol;
    }

    /**
     * Updates the current value of the cryptocurrency to the specified new value.
     *
     * @param newValue the new value of the cryptocurrency
     */
    public void updateCurrentValue(BigDecimal newValue) {
        currentValue = newValue;
    }

    @Override
    public String toString() {
        return displayName + '(' + shorthandSymbol + ')' + ": " + currentValue.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CryptoCurrency that)) return false;
        return Objects.equals(cryptoID, that.cryptoID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cryptoID);
    }
}