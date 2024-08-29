package org.bootcamp.models;

import java.math.BigDecimal;
import java.util.Objects;

public final class CryptoCurrency {
    private String cryptoID;
    private final String displayName;
    private final String shorthandSymbol;
    private BigDecimal currentValue;

    public CryptoCurrency(String displayName, String shorthandSymbol, BigDecimal currentValue) {
        this.displayName = displayName;
        this.currentValue = currentValue;
        this.shorthandSymbol = shorthandSymbol;
        generateCryptoID();
    }

    private void generateCryptoID() {
        cryptoID = shorthandSymbol + '@' + shorthandSymbol.hashCode();
    }

    public String getDisplayName() {
        return displayName;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public String getShorthandSymbol() {
        return shorthandSymbol;
    }

    public void updateValue(BigDecimal newValue) {
        currentValue = newValue;
    }

    @Override
    public String toString() {
        return displayName + '(' + shorthandSymbol + ')' + ": " + currentValue;
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