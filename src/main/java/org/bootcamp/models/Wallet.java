package org.bootcamp.models;

import org.bootcamp.services.CryptoCurrencyService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Wallet {
    private BigDecimal fiatMoney;
    private final Map<CryptoCurrency, BigDecimal> myCryptoCurrencies;

    public Wallet() {
        this.fiatMoney = BigDecimal.ZERO;
        this.myCryptoCurrencies = new HashMap<>();
        CryptoCurrencyService service = CryptoCurrencyService.getInstance();
        for (CryptoCurrency cryptoCurrency : service.getAvailableCryptoCurrencies().keySet()) {
            myCryptoCurrencies.put(cryptoCurrency, BigDecimal.ZERO);
        }
    }

    public BigDecimal getFiatMoney() {
        return fiatMoney;
    }

    public Map<CryptoCurrency, BigDecimal> getMyCryptoCurrencies() {
        return myCryptoCurrencies;
    }

    public boolean checkFunds(BigDecimal neededMoney) {
        System.out.print("You want subtract ");
        System.out.print(neededMoney);
        System.out.print(" and you have ");
        System.out.println(fiatMoney);
        return fiatMoney.compareTo(neededMoney) > 0;
    }

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
     * Recarga la criptomoneda con el valor indicado
     * @param cryptoCurrency que se encuentra en la billetera
     * @param quantity que se añadirá a la billetera
     */
    public void rechargeCryptoCurrency(CryptoCurrency cryptoCurrency, BigDecimal quantity) {
        BigDecimal currentQuantity = myCryptoCurrencies.get(cryptoCurrency);
        if (quantity.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal newQuantity = currentQuantity.add(quantity);
            myCryptoCurrencies.replace(cryptoCurrency, newQuantity);
        }
    }

    /**
     * Descuenta el valor indicado para la criptomoneda correspondiente
     * @param cryptoCurrency que se encuentra en la billetera
     * @param quantity que se descontará de la billetera
     * @return true Si la criptomoneda estaba presente, false caso contrario
     */
    public boolean subtractCryptoCurrency(CryptoCurrency cryptoCurrency, BigDecimal quantity) throws CryptoCurrencyException {
        BigDecimal currentQuantity = myCryptoCurrencies.get(cryptoCurrency);
        if (currentQuantity != null) {
            BigDecimal newQuantity = currentQuantity.subtract(quantity);
            if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
                throw new CryptoCurrencyException("Fondos Insuficientes para completar la transacción. Valores negativos no están permitidos");
            }
            myCryptoCurrencies.replace(cryptoCurrency, newQuantity);
        }
        return currentQuantity != null;
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
