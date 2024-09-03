package org.bootcamp.models;

import org.bootcamp.services.ExchangeService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The Wallet class represents a user's wallet that holds fiat money and various cryptocurrencies.<br>
 * It provides methods to check and manage funds, including adding and subtracting both fiat money and cryptocurrencies.
 */
public class Wallet {
    private BigDecimal fiatMoney;
    private final Map<CryptoCurrency, BigDecimal> myCryptoCurrencies;

    /**
     * Constructs a new Wallet with zero fiat money and initializes the cryptocurrency holdings to zero.
     * Retrieves the available cryptocurrencies from the ExchangeService.
     */
    public Wallet() {
        this.fiatMoney = BigDecimal.ZERO;
        this.myCryptoCurrencies = new HashMap<>();
        ExchangeService service = ExchangeService.getInstance();
        for (CryptoCurrency cryptoCurrency : service.getAvailableCryptoCurrencies().keySet()) {
            myCryptoCurrencies.put(cryptoCurrency, BigDecimal.ZERO);
        }
    }

    public Map<CryptoCurrency, BigDecimal> getMyCryptoCurrencies() {
        return myCryptoCurrencies;
    }

    /**
     * Checks if the wallet has sufficient fiat money for a given amount.
     *
     * @param neededMoney the amount of fiat money needed
     * @return true if the wallet has enough fiat money, false otherwise
     */
    public boolean checkFunds(BigDecimal neededMoney) {
        return fiatMoney.compareTo(neededMoney) >= 0;
    }

    /**
     * Checks if the wallet has sufficient cryptocurrency for a given amount.
     *
     * @param cryptoCurrency the cryptocurrency to check
     * @param neededQuantity the amount of cryptocurrency needed
     * @return true if the wallet has enough of the specified cryptocurrency, false otherwise
     */
    public boolean checkCryptoFunds(CryptoCurrency cryptoCurrency, BigDecimal neededQuantity) {
        return myCryptoCurrencies.get(cryptoCurrency).compareTo(neededQuantity) >= 0;
    }

    /**
     * Adds a specified amount of fiat money to the wallet.
     *
     * @param value the amount of fiat money to add
     */
    public void addFiatMoney(BigDecimal value) {
        fiatMoney = fiatMoney.add(value);
    }

    /**
     * Subtract fiat money if is available
     * @param value to subtract
     * @return true if the wallet has enough funds and subtract the value, false otherwise
     */
    public boolean subtractFiatMoney(BigDecimal value) {
        if (checkFunds(value)) {
            fiatMoney = fiatMoney.subtract(value);
            return true;
        }
        return false;
    }

    /**
     * Recharges the specified quantity of a cryptocurrency in the wallet.
     *
     * @param cryptoCurrency the cryptocurrency to be recharged
     * @param quantity the quantity of cryptocurrency to add
     */
    public void rechargeCryptoCurrency(CryptoCurrency cryptoCurrency, BigDecimal quantity) {
        BigDecimal currentQuantity = myCryptoCurrencies.get(cryptoCurrency);
        if (quantity.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal newQuantity = currentQuantity.add(quantity);
            myCryptoCurrencies.replace(cryptoCurrency, newQuantity);
        }
    }

    /**
     * Subtracts the specified quantity of a cryptocurrency from the wallet if sufficient funds are available.
     *
     * @param cryptoCurrency the cryptocurrency to be subtracted
     * @param quantity the quantity of cryptocurrency to subtract
     * @return true if the subtraction was successful, false otherwise
     */
    public boolean subtractCryptoCurrency(CryptoCurrency cryptoCurrency, BigDecimal quantity) {
        if (checkCryptoFunds(cryptoCurrency, quantity)) {
            BigDecimal currentQuantity = myCryptoCurrencies.get(cryptoCurrency);
            myCryptoCurrencies.replace(cryptoCurrency, currentQuantity.subtract(quantity));
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wallet wallet)) return false;
        return Objects.equals(fiatMoney, wallet.fiatMoney) && Objects.equals(myCryptoCurrencies, wallet.myCryptoCurrencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fiatMoney, myCryptoCurrencies);
    }

    @Override
    public String toString() {
        StringBuilder message = new StringBuilder("Account Balance: $" + fiatMoney +
                "\nCryptoCurrencies:\n");
        for(CryptoCurrency cryptoCurrency : myCryptoCurrencies.keySet()) {
            message.append(cryptoCurrency.getShorthandSymbol()).append(": ").append(myCryptoCurrencies.get(cryptoCurrency)).append('\n');
        }
        return message.toString();
    }
}
